package cn.bidlink.nbl.framework.springmvc;

import java.util.Collection;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class RespReturnValueHandler implements HandlerMethodReturnValueHandler{

	@Override
	public boolean supportsReturnType(MethodParameter returnType) {
		ResponseBody result = returnType.getMethodAnnotation(ResponseBody.class);
		return (result!=null);
	}
	
	@Override
	public void handleReturnValue(Object returnValue,
			MethodParameter returnType, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest) throws Exception {
		RespDto resp = new RespDto();
		RespAlias respAlias = returnType.getMethodAnnotation(RespAlias.class);
		if(respAlias != null){
			resp.put(respAlias.value(), returnValue);
		}else{
			if(returnValue instanceof RespDto){
				resp = (RespDto)returnValue;
			}else if(returnValue instanceof Map){
				resp.putAll((Map) returnValue);
			}else if(returnValue instanceof Collection){
				resp.put("rows", returnValue);
			}else{
				resp.put("obj", returnValue);
			}
		}
		HttpServletResponse response = webRequest.getNativeResponse(HttpServletResponse.class);
		mavContainer.setRequestHandled(true);
		String content = JSON.toJSONString(resp, SerializerFeature.WriteMapNullValue);
        RespUtils.responseJson(response, content);
	}
}