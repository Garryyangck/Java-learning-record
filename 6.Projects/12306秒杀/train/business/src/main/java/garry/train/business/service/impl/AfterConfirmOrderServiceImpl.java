package garry.train.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import garry.train.business.enums.ConfirmOrderStatusEnum;
import garry.train.business.feign.MemberFeign;
import garry.train.business.form.ConfirmOrderDoForm;
import garry.train.business.form.DailyTrainSeatSaveForm;
import garry.train.business.form.DailyTrainTicketSaveForm;
import garry.train.business.form.TicketSaveForm;
import garry.train.business.pojo.ConfirmOrder;
import garry.train.business.pojo.DailyTrainSeat;
import garry.train.business.pojo.DailyTrainTicket;
import garry.train.business.service.*;
import garry.train.business.util.SellUtil;
import io.seata.core.context.RootContext;
import io.seata.spring.annotation.GlobalTransactional;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

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

    /**
     * 这会产生循环依赖，可能导致 Bean 的状态不一致，从而引发各种问题。
     * by AI: 检查 @Async 注解的使用：
     * 如果你在使用 @Async 注解时遇到了循环依赖问题，
     * 这可能是因为 @Async 会导致代理对象的创建，这可能会与循环依赖检测相互作用。
     * 在这种情况下，你可能需要重新设计你的异步方法，或者使用 @Lazy 注解来延迟 Bean 的创建。
     */
    @Resource
    @Lazy
    private ConfirmOrderService confirmOrderService;

    @Resource
    private MessageService messageService;

    @Resource
    private MemberFeign memberFeign;

    @Override
//    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.REPEATABLE_READ, rollbackFor = RuntimeException.class)
    @GlobalTransactional
    public boolean afterDoConfirm(List<ConfirmOrderService.SeatChosen> seatChosenList, ConfirmOrderDoForm form) {
        log.info("Seata 的全局事务 ID = {} (只有分布式事务生效时才会打印)", RootContext.getXID());

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
            log.info("daily_train_seat 修改 sell 售卖情况完成，乘客：{}", seatChosen.getTicket().getPassengerName());

            // daily_train_ticket 修改余票数
            List<DailyTrainTicket> tickets = dailyTrainTickets.stream()
                    .filter(dailyTrainTicket -> dailyTrainTicket.getStartIndex() <= startIndex
                            && dailyTrainTicket.getEndIndex() >= endIndex).toList(); // startIndex, endIndex 之间的所有车站的票都要减少
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
            log.info("daily_train_ticket 修改余票数完成，乘客：{}", seatChosen.getTicket().getPassengerName());

            // (member)ticket 增加用户购票的记录，还没有创建 ticket 表
            DailyTrainTicket dailyTrainTicket = dailyTrainTickets.stream()
                    .filter(ticket -> ticket.getDate().equals(seatChosen.getDate())
                            && ticket.getTrainCode().equals(seatChosen.getTrainCode())
                            && ticket.getStart().equals(seatChosen.getStart())
                            && ticket.getEnd().equals(seatChosen.getEnd())).toList().get(0);
            TicketSaveForm ticketSaveForm = getTicketSaveForm(seatChosen, dailyTrainTicket);
            memberFeign.save(ticketSaveForm);
            log.info("乘客 {} 的 (member)ticket 增加用户购票的记录完成，{}", seatChosen.getTicket().getPassengerName(), ticketSaveForm);
        }

        // confirm_order 修改状态为成功 (不要放到循环里面，confirm_order 的状态只需要修改一次就可以了)
        ConfirmOrder confirmOrder = confirmOrderService.save(form, ConfirmOrderStatusEnum.SUCCESS);
        log.info("将订单状态修改为 SUCCESS：{}", confirmOrder);

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
        log.info("将 formId = {} 的订单的所有购票消息发送完毕", form.getId());

        return true;
    }

    private static TicketSaveForm getTicketSaveForm(ConfirmOrderService.SeatChosen seatChosen, DailyTrainTicket dailyTrainTicket) {
        TicketSaveForm ticketSaveForm = new TicketSaveForm();
        ticketSaveForm.setMemberId(seatChosen.getMemberId());
        ticketSaveForm.setPassengerId(seatChosen.getTicket().getPassengerId());
        ticketSaveForm.setPassengerName(seatChosen.getTicket().getPassengerName());
        ticketSaveForm.setTrainDate(seatChosen.getDate());
        ticketSaveForm.setTrainCode(seatChosen.getTrainCode());
        ticketSaveForm.setCarriageIndex(seatChosen.getCarriageIndex());
        ticketSaveForm.setSeatRow(seatChosen.getRow());
        ticketSaveForm.setSeatCol(seatChosen.getCol());
        ticketSaveForm.setStartStation(seatChosen.getStart());
        ticketSaveForm.setStartTime(dailyTrainTicket.getStartTime());
        ticketSaveForm.setEndStation(seatChosen.getEnd());
        ticketSaveForm.setEndTime(dailyTrainTicket.getEndTime());
        ticketSaveForm.setSeatType(seatChosen.getSeatType());
        return ticketSaveForm;
    }
}
