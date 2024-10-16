package garry.train.common.enums;

import garry.train.common.consts.CommonConst;
import lombok.Getter;

/**
 * @author Garry
 * 2024-09-06 15:53
 */
@Getter
public enum ResponseEnum {

    ERROR(-1, "服务器异常"),

    SUCCESS(0, "操作成功"),

    PARAMETER_INPUT_ERROR(1, "参数输入异常"),

    MEMBER_MOBILE_REGISTER_EXIST(2, "手机号已注册"),

    MEMBER_MESSAGE_CODE_SEND_FAILED(3, "短信验证码发送失败"),

    MEMBER_MOBILE_NOT_EXIST(4, "手机号输入有误"),

    MEMBER_CODE_NOT_EXIST(5, "验证码不存在或已过期"),

    MEMBER_WRONG_CODE(6, "验证码不正确"),

    MEMBER_WRONG_TOKEN(7, "JWT不存在或已过期"),

    MEMBER_THREAD_LOCAL_ERROR(8, "获取会员ID失败"),

    BUSINESS_WRONG_TRAIN_CODE(9, "车次编号错误或不存在"),

    DUPLICATE_KEY(10, "数据库唯一键异常"),

    BUSINESS_DUPLICATE_STATION_NAME(11, "站名已存在"),

    BUSINESS_DUPLICATE_TRAIN_STATION_INDEX(12, "站序已存在"),

    BUSINESS_DUPLICATE_TRAIN_STATION_NAME(13, "站名已存在"),

    BUSINESS_DUPLICATE_TRAIN_CARRIAGE_INDEX(14, "厢号已存在"),

    BATCH_SCHEDULER_ADD_FAILED_DISPATCH_ERROR(15, "创建定时任务失败: 调度异常"),

    BATCH_SCHEDULER_ADD_FAILED_JOB_NOT_FOUND(16, "创建定时任务失败: 任务类不存在"),

    BATCH_SCHEDULER_PAUSE_FAILED_DISPATCH_ERROR(17, "暂停定时任务失败: 调度异常"),

    BATCH_SCHEDULER_RESUME_FAILED_DISPATCH_ERROR(18, "重启定时任务失败: 调度异常"),

    BATCH_SCHEDULER_RESCHEDULE_FAILED_DISPATCH_ERROR(19, "更新定时任务失败: 调度异常"),

    BATCH_SCHEDULER_DELETE_FAILED_DISPATCH_ERROR(20, "删除定时任务失败: 调度异常"),

    BATCH_SCHEDULER_QUERY_FAILED_DISPATCH_ERROR(21, "查看定时任务失败: 调度异常"),

    BATCH_SCHEDULER_RUN_FAILED_DISPATCH_ERROR(22, "手动执行任务失败: 调度异常"),

    BUSINESS_DUPLICATE_DAILY_TRAIN_DATE_CODE(23, "该日的车次已存在"),

    BUSINESS_DUPLICATE_DAILY_TRAIN_STATION_DATE_TRAIN_CODE_INDEX(24, "该日车次的站序已存在"),

    BUSINESS_DUPLICATE_DAILY_TRAIN_STATION_DATE_TRAIN_CODE_NAME(25, "该日车次的站名已存在"),

    BUSINESS_DUPLICATE_DAILY_TRAIN_CARRIAGE_DATE_TRAIN_CODE_INDEX(26, "该日车次的车厢号已存在"),

    BUSINESS_TRAIN_NOT_FOUND(27, "该车次不存在"),

    API_ARGUMENT_MISMATCH(28, "接口参数不匹配"),

    BUSINESS_DUPLICATE_DAILY_TRAIN_TICKET_DATE_TRAIN_CODE_START_END(29, "该日该车次该起点终点站车票已存在"),

    MEMBER_EXCEED_PASSENGER_LIMIT(30, "乘车人总数不能超过上限" + CommonConst.PASSENGER_LIMIT),

    BUSINESS_SELL_FAILED_LACK_OF_TICKETS(31, "售票失败，该座位票已售出"),

    BUSINESS_CONFIRM_ORDER_CHECK_FAILED(32, "订单校验失败"),

    BUSINESS_CONFIRM_ORDER_LACK_OF_TICKETS(33, "余票不足，订单创建失败"),

    BUSINESS_CONFIRM_ORDER_DUPLICATE_PASSENGER(34, "同一乘客不能购买同一车次"),

    BUSINESS_CONFIRM_ORDER_CHOOSE_SEAT_FAILED(35, "选座失败，未能为所有乘客选到座位"),

    BUSINESS_CONFIRM_ORDER_HANDLE_SEAT_CHOSEN_FAILED(36, "处理选座结果数据失败"),

    BUSINESS_WEBSOCKET_MESSAGE_SEND_FAILED(37, "通过websocket发送消息失败"),

    BUSINESS_WEBSOCKET_CONNECT_FAILED(38, "连接websocket失败"),

    BUSINESS_WEBSOCKET_DISCONNECT_FAILED(39, "websocket断开连接失败"),

    BUSINESS_MESSAGE_UNTOP_FAILED(40, "取消置顶失败，消息还没有置顶"),

    BUSINESS_MARKDOWN_FILE_NOT_FOUND(41, "获取md页面失败，服务器找不到对应md文件"),

    BUSINESS_MARKDOWN_FILE_READ_FAILED(41, "获取md页面失败，md文件读取失败"),
    ;

    private final Integer code;

    private final String msg;

    ResponseEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
