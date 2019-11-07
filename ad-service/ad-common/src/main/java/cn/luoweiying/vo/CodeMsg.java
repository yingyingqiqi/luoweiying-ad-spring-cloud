package cn.luoweiying.vo;

public class CodeMsg {
    private Integer code;
    private String msg;

    //通用异常
    public static CodeMsg SUCCESS = new CodeMsg(0,"success");//常量
    public static CodeMsg SERVER_ERROR = new CodeMsg(500100,"服务端异常");//常量
    public static CodeMsg SERVER_AdERROR = new CodeMsg(000000,"广告系统异常");//常量
//    public static CodeMsg BIND_ERROR = new CodeMsg(500101,"参数效验异常：");//常量
//    public static CodeMsg REQUEST_ILLEGAL = new CodeMsg(500102,"请求非法：");//常量
//    public static CodeMsg ACCESS_ILLEGAL = new CodeMsg(500104,"访问频繁");//常量
    public static CodeMsg REQUEST_PARAM_ERROR = new CodeMsg(400001,"请求参数错误");//常量
    public static CodeMsg SAME_NAME_ERROR = new CodeMsg(40002,"存在同名的用户");//常量
    public static CodeMsg PARSE_DATE_ERROR = new CodeMsg(40003,"日期转换异常");//常量
    public static CodeMsg CAN_NOT_FIND_RECORD = new CodeMsg(40004,"找不到数据记录");//常量
    public static CodeMsg SAME_NAME_UNIT_ERROR = new CodeMsg(40005,"存在同名的推广计划");//常量
    public static CodeMsg EUREKA_CLIENT_AD_SPONSOR_ERROR = new CodeMsg(-1,"服务断开，触发断容器");//常量
    public static CodeMsg INDEX_FILE_LODER_ERROR = new CodeMsg(-2,"全量索引加载异常");//常量




    //登陆模块
//    public static CodeMsg SESSION_ERROR = new CodeMsg(500210,"Session不存在或者已经失效");//常量
//    public static CodeMsg PASSWORD_EMPTY = new CodeMsg(500211,"登录密码不能为空");//常量
//    public static CodeMsg MOBILE_EMPTY = new CodeMsg(500212,"手机号不能为空");//常量
//    public static CodeMsg MOBILE_ERROR = new CodeMsg(500213,"手机号格式错误");//常量
//    public static CodeMsg MOBILE_NOT_EXIST = new CodeMsg(500214,"手机号不存在");//常量
//    public static CodeMsg PASSWORD_ERROR = new CodeMsg(500215,"密码不正确");//常量

    private CodeMsg(int code, String msg) {
        this.code = code ;
        this.msg = msg;
    }

    public CodeMsg fillArgs(Object... args) {
        int code = this.code;
        String message = String.format(this.msg, args);
        return new CodeMsg(code, message);
    }

    //为了更好的封装，不需要SET方法
    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    @Override
    public String toString() {
        return "CodeMsg{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                '}';
    }
}
