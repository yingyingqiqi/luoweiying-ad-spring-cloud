package cn.luoweiying.advice;

import cn.luoweiying.annotation.IgnoreResponseDataAdvice;
import cn.luoweiying.vo.CommonResponse;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

//RestControllerAdvice是ontrollerAdvice、ResponseBody的合成 告诉MVC容器，这有一个全局控制器（统一拦截器）要使用
//ResponseBodyAdvice 是HandlerMapping的一个子类，是针对posthandle不太够用新增的，方法已经提交或者已响应， 最后还可以再处理
//@RestControllerAdvice
@ControllerAdvice
@ResponseBody
public class CommonResponseDataAdvice implements ResponseBodyAdvice {
    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {

        if (returnType.getMethod().isAnnotationPresent(IgnoreResponseDataAdvice.class)
                ||returnType.getDeclaringClass().isAnnotationPresent(IgnoreResponseDataAdvice.class)) {
            return false;
        }
//        if (returnType.hasMethodAnnotation(IgnoreResponseDataAdvice.class)) return true;
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType,
                                  MediaType selectedContentType,
                                  Class selectedConverterType,
                                  ServerHttpRequest request,
                                  ServerHttpResponse response) {

        if (body == null)   return body;
        if (body instanceof CommonResponse) return body;   //异常情况特殊处理了

        CommonResponse<Object> commonResponse = CommonResponse.success(body); //成功
        return commonResponse;
    }
}
