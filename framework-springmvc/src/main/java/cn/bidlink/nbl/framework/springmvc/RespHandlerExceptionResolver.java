package cn.bidlink.nbl.framework.springmvc;

import java.util.Locale;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 统一异常处理
 * */
public class RespHandlerExceptionResolver extends SimpleMappingExceptionResolver {

	@Autowired
	private MessageSource messageSource;
	
	@Override
	protected ModelAndView doResolveException(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception exception) {
        exception.printStackTrace();
        RespDto respDto = null;
        response.setStatus(HttpServletResponse.SC_OK);
        if (exception instanceof RespException) {
        	String message = messageSource.getMessage(exception.getMessage(), null, exception.getMessage(), Locale.CHINESE);
            respDto = new RespDto(400, message);
        } else if (exception instanceof MethodArgumentNotValidException){
        	MethodArgumentNotValidException e = (MethodArgumentNotValidException) exception;
        	respDto = new RespDto(400, e.getBindingResult().getFieldError().getDefaultMessage());
        } else {
            respDto = new RespDto(400, "系统错误");
        }
        
        String content = JSON.toJSONString(respDto, SerializerFeature.WriteMapNullValue);
        RespUtils.responseJson(response, content);
        return null;
	}
}