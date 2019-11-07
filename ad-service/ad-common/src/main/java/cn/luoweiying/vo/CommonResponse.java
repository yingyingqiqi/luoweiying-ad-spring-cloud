package cn.luoweiying.vo;

import java.io.Serializable;
/*@Data                                //get   set
@AllArgsConstructor                 //构造函数
@NoArgsConstructor*/
public class CommonResponse<T> implements Serializable {
    private Integer code;
    private String message;
    private T data;

    //成功私有构造
    private CommonResponse(T data) {
        this.code = 0;
        this.message = "success";
        this.data = data;
    }

    //失败私有调用  ， AdException负责调用
    private CommonResponse(CodeMsg codeMsg) {
        if (codeMsg == null) return;
        this.code = codeMsg.getCode();
        this.message = codeMsg.getMsg();
    }

    //外部调用接口
    public static <T> CommonResponse<T> success(T data) {
        return new CommonResponse<>(data);
    }

    public static <T> CommonResponse<T> error(CodeMsg msg) {
        return new CommonResponse<>(msg);
    }
    //为了更好的封装，不需要SET方法
    public int getCode() {
        return code;
    }

    public String getMsg() {
        return message;
    }

    public T getData() {
        return data;
    }


}
