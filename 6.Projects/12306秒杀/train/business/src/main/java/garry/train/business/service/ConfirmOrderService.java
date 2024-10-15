package garry.train.business.service;

import garry.train.business.enums.ConfirmOrderStatusEnum;
import garry.train.business.form.ConfirmOrderDoForm;
import garry.train.business.form.ConfirmOrderQueryForm;
import garry.train.business.form.ConfirmOrderTicketForm;
import garry.train.business.pojo.ConfirmOrder;
import garry.train.business.vo.ConfirmOrderQueryVo;
import garry.train.common.vo.PageVo;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author Garry
 * 2024-10-07 19:53
 */
public interface ConfirmOrderService {

    /**
     * 用于处理选座相关的业务，并提供相关字段
     */
    @Data
    class SeatChosen {

        /**
         * 会员id
         */
        private Long memberId;

        /**
         * 日期
         */
        private Date date;

        /**
         * 车次编号
         */
        private String trainCode;

        /**
         * 出发站
         */
        private String start;

        /**
         * 到达站
         */
        private String end;

        /**
         * 乘客的座位需求
         */
        private ConfirmOrderTicketForm ticket;

        /**
         * 箱序
         */
        private Integer carriageIndex;

        /**
         * 排号|01, 02
         */
        private String row;

        /**
         * 列号|枚举[SeatColEnum]
         */
        private String col;

        /**
         * 座位类型|枚举[SeatTypeEnum]
         */
        private String seatType;

        /**
         * 同车箱座序
         */
        private Integer carriageSeatIndex;

    }

    /**
     * 如果 statusEnum 为 INIT，则新插入一条 status = INIT 的 confirmOrder
     * 否则就是修改已有 confirmOrder 的状态
     */
    ConfirmOrder save(ConfirmOrderDoForm form, ConfirmOrderStatusEnum statusEnum);

    /**
     * 根据 memberId 查询所有的确认订单
     * 如果 form.memberId = null，则为管理员查询
     */
    PageVo<ConfirmOrderQueryVo> queryList(ConfirmOrderQueryForm form);

    /**
     * 根据 id(主键) 删除确认订单
     */
    void delete(Long id);

    /**
     * 处理订票操作
     */
    void doConfirm(ConfirmOrderDoForm form);

    /**
     * 用于检验用户是否会重复购买同一乘客某一确定始末站的车票
     */
    List<ConfirmOrder> queryByMemberIdAndDateAndTrainCodeAndStartAndEnd(Long memberId, Date date, String trainCode, String start, String end);

    /**
     * 业务数据校验，车次是否存在，车票是否存在，车次时间是否合法，
     * tickets 是否 > 0，是否有余票，同一乘客不能购买同一车次
     */
    boolean checkConfirmOrder(ConfirmOrderDoForm form);
}
