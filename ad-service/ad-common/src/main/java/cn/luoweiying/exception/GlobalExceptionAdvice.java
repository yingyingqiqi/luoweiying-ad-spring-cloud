package cn.luoweiying.exception;

import cn.luoweiying.vo.CodeMsg;
import cn.luoweiying.vo.CommonResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

//@RestControllerAdvice
@ControllerAdvice
@ResponseBody
public class GlobalExceptionAdvice {

    @ExceptionHandler(value = Exception.class)
    public CommonResponse<String> handlerAdException(HttpServletRequest req, Exception e) {
        e.printStackTrace();
        if (e instanceof cn.luoweiying.exception.AdException) {
            AdException ae = (AdException) e;
            return CommonResponse.error(ae.getCm());
        }
        if (e instanceof org.springframework.http.converter.HttpMessageNotReadableException) {
            return CommonResponse.error(CodeMsg.REQUEST_PARAM_ERROR);
        }
        return CommonResponse.success(e.toString());

    }

    //原写法
//    @ExceptionHandler(value = AdException.class)
//    public CommonResponse<String> handlerAdException1(HttpServletRequest req, AdException ae) {
//        return CommonResponse.error(ae.getCm());
//    }
}
