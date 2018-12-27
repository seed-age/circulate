package com.sunnsoft.sloa.util;

/**
 * @ClassName ConstantUtils
 * @Description: 全平台常量值, 状态值维护类;
 * @Auther: chenjian
 * @Date: 2018/12/22 12:40
 * @Version: 1.0
 **/
public class ConstantUtils {


    // 分页参数;
    public static final Integer DEFAULT_PAGE = 1;           // 分页查询默认页码;
    public static final Integer DEFAULT_PAGE_ROWS = 10;     // 分页查询默认每页数据条数;

    // 传阅状态
    public static final int MAIL_STATUS = 0;     // 无状态
    public static final int MAIL_WAIT_STATUS = 1;     // 待发传阅
    public static final int MAIL_DELETE_STATUS = 7;     // 已删除

    // 传阅流程状态
    public static final int MAIL_HALFWAY_STATUS = 1;     // 传阅中
    public static final int RECEIVE_AWAIT_STATUS = 2;     // 待办传阅
    public static final int MAIL_COMPLETE_STATUS = 3;     // 传阅已完成

    // 已收传阅状态
    public static final int RECEIVE_WAIT_STATUS = 4;     // 草稿传阅
    public static final int RECEIVE_UNREAD_STATUS = 5;     // 未读传阅
    public static final int RECEIVE_READ_STATUS = 6;     // 已读传阅
    public static final int RECEIVE_NOTOPEN_STATUS = 0;     // 未开封
    public static final int RECEIVE_OPEN_STATUS = 1;     // 已开封

    // OA人员状态
    public static final String OA_USER_PROBATION_STATUS = "0";     // 试用
    public static final String OA_USER_OFFICIAL_STATUS = "1";     // 正式
    public static final String OA_USER_TEMPORARY_STATUS = "2";     // 临时
    public static final String OA_USER_PROBATION_DELAY_STATUS = "3";     // 试用延期
    public static final String OA_USER_DISMISS_STATUS = "4";     // 解聘
    public static final String OA_USER_DIMISSION_STATUS = "5";     // 离职
    public static final String OA_USER_RETIRE_STATUS = "6";     // 退休
    public static final String OA_USER_INVALID_STATUS = "7";     // 无效


}
