package com.zhike.note.util.code;


//事件状态码
public class EventCode {

    //=========================sql  登录状态码  =========================
    public static final String LOGIN_EMAIL_PASSWORD_SUCCESS = "L_001";  //登录成功(邮箱和密码)
    public static final String LOGIN_FAIL = "L_002";  //登录失败
    public static final String LOGIN_LOG_CREATE_EXCEPTION = "L_003";  //登录日志创建异常
    public static final String LOGIN_LOG_CREATE_FAIL = "L_004";  //登录日志创建失败
    public static final String LOGIN_SAVE_USER_TOKEN_REDIS_EXCEPTION = "L_005";  //登录成功后存储用户信息至 redis 失败
    public static final String LOGIN_OUT_EXCEPTION = "L_006";  //退出登录异常
    public static final String LOGIN_OUT_SUCCESS = "L_007";  //退出登录成功
    public static final String LOGIN_INVALID = "L_008";  //登录失效

    //=========================sql  业务状态码  =========================
    public static final String SELECT_SUCCESS = "S_001";  //查询成功
    public static final String SELECT_EXCEPTION = "S_002";  //查询失败
    public static final String SELECT_ERROR = "S_003";  //查询错误
    public static final String SELECT_NONE = "S_004";  //未查到信息
    public static final String INSERT_EXCEPTION = "S_005";  //新增异常
    public static final String INSERT_ERROR = "S_006";  //新增错误
    public static final String UPDATE_EXCEPTION = "S_007";  //修改异常
    public static final String UPDATE_ERROR = "S_008";  //修改错误
    public static final String UPDATE_SUCCESS = "S_009";  //修改成功


    //=========================sql  账号状态码  =========================
    public static final String ACCOUNT_CLOCK = "A_001";  //账号被锁定
    public static final String ACCOUNT_EMAIL_WROME = "A_002";  //账号邮箱有误
    public static final String ACCOUNT_EMAIL_PASSWORD = "A_003";  //账号密码有误
    public static final String ACCOUNT_EMAIL_REGISTERED = "A_004";  //邮箱账号已被注册
    public static final String ACCOUNT_EMAIL_REGISTER_SUCCESS = "A_005";  //邮箱账号注册成功
    public static final String ACCOUNT_EMAIL_REGISTER_LOG_EXCEPTION = "A_006";  //邮箱账号注册日志异常
    public static final String ACCOUNT_EMAIL_REGISTER_LOG_ERROR = "A_007";  //邮箱账号注册日志错误


    //=========================sql  邮箱服务状态码  =========================
    public static final String EMAIL_SEND_VC_SUCCESS= "E_001";  //邮箱验证码发送成功
    public static final String EMAIL_SEND_VC_ERROR= "E_002";  //邮箱验证码发送失败
    public static final String EMAIL_SEND_VC_REDIS_ERROR= "E_003";  //邮箱存储失败
    public static final String EMAIL_SEND_INIT_PASSWORD_EXCEPTION= "E_004";  //邮箱注册失败


    //=========================sql  验证码的状态码  =========================
    public static final String VC_INVALID= "V_001";  //验证码已失效
    public static final String VC_MATCH_ERROR= "V_002";  //验证码匹配错误


    //=========================sql  验证参数的状态码  =========================
    public static final String PARAM_VC_WRONG = "P_001";  //验证码参数有误
    public static final String PARAM_VC_ERROR = "P_002";  //验证码错误
    public static final String PARAM_VC_KEY_WRONG = "P_003";  //验证码查询关键词有误
    public static final String PARAM_VC_KEY_EMAIL_WRONG = "P_004";  //邮箱注册账号和获取验证码的邮箱不匹配
    public static final String PARAM_USER_TOKEN_WRONG = "P_005";  //登录的 userToken 有误
    public static final String PARAM_TOP_WRONG = "P_006";  //置顶参数有误
    public static final String PARAM_THING_ID_WRONG = "P_007";  //小记编号参数有误
    public static final String PARAM_THING_COMPLETE_WRONG = "P_008";  //删除参数有误（是否为彻底删除的参数）
    public static final String PARAM_THING_RECYCLE_BIN_WRONG = "P_009";  //删除参数有误（是否为回收站操作的参数）


    //=========================sql  redis 的状态码  =========================
    public static final String REDIS_SERVE_ERROR = "R_001";  //redis服务



    //=========================sql  小记 的状态码  =========================
    public static final String THING_tOP_SUCCESS = "T_001";  //置顶小记成功
    public static final String THING_tOP_FAILED = "T_002";  //置顶小记失败
    public static final String THING_CANCET_tOP_SUCCESS = "T_003";  //取消置顶小记成功
    public static final String THING_CANCET_tOP_FAILED = "T_004";  //取消置顶小记失败
    public static final String THING_DELETE_SUCCESS = "T_005";  //删除小记成功
    public static final String THING_COMPLETE_DELETE_SUCCESS = "T_006";  //彻底删除小记成功
}
