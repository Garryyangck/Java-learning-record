package garry.train.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import garry.train.business.enums.ConfirmOrderStatusEnum;
import garry.train.business.form.ConfirmOrderDoForm;
import garry.train.business.form.ConfirmOrderTicketForm;
import garry.train.business.form.DailyTrainSeatSaveForm;
import garry.train.business.form.DailyTrainTicketSaveForm;
import garry.train.business.pojo.ConfirmOrder;
import garry.train.business.pojo.DailyTrainSeat;
import garry.train.business.pojo.DailyTrainTicket;
import garry.train.business.service.*;
import garry.train.business.util.SellUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Garry
 * 2024-10-14 13:40
 */
@Slf4j
@Service
public class AfterConfirmOrderServiceImpl implements AfterConfirmOrderService {

    @Resource
    private DailyTrainStationService dailyTrainStationService;

    @Resource
    private DailyTrainSeatService dailyTrainSeatService;

    @Resource
    private DailyTrainTicketService dailyTrainTicketService;

    @Resource
    private ConfirmOrderService confirmOrderService;

    @Resource
    private MessageService messageService;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.REPEATABLE_READ, rollbackFor = RuntimeException.class)
    public boolean afterDoConfirm(List<ConfirmOrderService.SeatChosen> seatChosenList, ConfirmOrderDoForm form) {
        Integer startIndex = dailyTrainStationService.queryByDateAndTrainCodeAndName(form.getDate(), form.getTrainCode(), form.getStart()).get(0).getIndex();
        Integer endIndex = dailyTrainStationService.queryByDateAndTrainCodeAndName(form.getDate(), form.getTrainCode(), form.getEnd()).get(0).getIndex();
        List<DailyTrainSeat> dailyTrainSeats = dailyTrainSeatService.queryByDateAndTrainCode(form.getDate(), form.getTrainCode());
        List<DailyTrainTicket> dailyTrainTickets = dailyTrainTicketService.queryByDateAndTrainCode(form.getDate(), form.getTrainCode());

        for (ConfirmOrderService.SeatChosen seatChosen : seatChosenList) {
            // daily_train_seat 修改 sell 售卖情况
            DailyTrainSeat dailyTrainSeat = dailyTrainSeats.stream()
                    .filter(trainSeat -> seatChosen.getCarriageIndex().equals(trainSeat.getCarriageIndex())
                            && seatChosen.getCarriageSeatIndex().equals(trainSeat.getCarriageSeatIndex()))
                    .toList().get(0);
            DailyTrainSeatSaveForm seatSaveForm = BeanUtil.copyProperties(dailyTrainSeat, DailyTrainSeatSaveForm.class);
            seatSaveForm.setSell(SellUtil.sell(seatSaveForm.getSell(), startIndex, endIndex));
            dailyTrainSeatService.save(seatSaveForm);
            log.info("乘客 {} 的 daily_train_seat 修改 sell 售卖情况完成", seatChosen.getTicket().getPassengerName());

            // daily_train_ticket 修改余票数
            List<DailyTrainTicket> tickets = dailyTrainTickets.stream()
                    .filter(dailyTrainTicket -> dailyTrainTicket.getStartIndex() >= startIndex
                            && dailyTrainTicket.getEndIndex() <= endIndex).toList(); // startIndex, endIndex 之间的所有车站的票都要减少
            for (DailyTrainTicket dailyTrainTicket : tickets) {
                switch (seatChosen.getSeatType()) {
                    case "1":
                        dailyTrainTicket.setYdz(dailyTrainTicket.getYdz() - 1);
                        break;
                    case "2":
                        dailyTrainTicket.setEdz(dailyTrainTicket.getEdz() - 1);
                        break;
                    case "3":
                        dailyTrainTicket.setRw(dailyTrainTicket.getRw() - 1);
                        break;
                    case "4":
                        dailyTrainTicket.setYw(dailyTrainTicket.getYw() - 1);
                        break;
                    default:
                        break;
                }
                DailyTrainTicketSaveForm ticketSaveForm = BeanUtil.copyProperties(dailyTrainTicket, DailyTrainTicketSaveForm.class);
                dailyTrainTicketService.save(ticketSaveForm);
            }
            log.info("乘客 {} 的 daily_train_ticket 修改余票数完成", seatChosen.getTicket().getPassengerName());

            // TODO (member)ticket 增加用户购票的记录，还没有创建 ticket 表
            log.info("乘客 {} 的 (member)ticket 增加用户购票的记录完成", seatChosen.getTicket().getPassengerName());
        }

        // confirm_order 修改状态为成功 (不要放到循环里面，confirm_order 的状态只需要修改一次就可以了)
        List<ConfirmOrder> confirmOrders = confirmOrderService.queryByMemberIdAndDateAndTrainCodeAndStartAndEnd(form.getMemberId(), form.getDate(), form.getTrainCode(), form.getStart(), form.getEnd());
        for (ConfirmOrder confirmOrder : confirmOrders) {
            // 根据 tickets 判断该 confirmOrder 是否是我们要找的 confirmOrder
            List<ConfirmOrderTicketForm> ticketForms = JSON.parseObject(confirmOrder.getTickets(), new TypeReference<>() {
            });
            if (form.getTickets().equals(ticketForms)) {
                // 修改对应订单的状态为 SUCCESS
                form.setId(confirmOrder.getId());
                confirmOrderService.save(form, ConfirmOrderStatusEnum.SUCCESS);
                log.info("将订单状态修改为 SUCCESS：{}", confirmOrder);
                break;
            }
        }
        log.info("confirm_order 修改状态为 SUCCESS 完成");

        // 创建 message 入库，并通过 websocket 发送给前端浏览器
        for (ConfirmOrderService.SeatChosen seatChosen : seatChosenList) {
            String content = "您成功为乘客【%s】买到【%s】从【%s】开往【%s】的【%s】车次的车票：【%s号车厢，%s行%s列】。"
                    .formatted(seatChosen.getTicket().getPassengerName(), DateUtil.format(seatChosen.getDate(), "yyyy-MM-dd"),
                            seatChosen.getStart(), seatChosen.getEnd(),
                            seatChosen.getTrainCode(), seatChosen.getCarriageIndex(),
                            seatChosen.getRow(), seatChosen.getCol());
            messageService.sendSystemMessage(form.getMemberId(), content);
            log.info("发送消息：{}", content);
        }
        log.info("成功创建 message 入库，并通过 websocket 发送给前端浏览器");

        return true;
    }
}
