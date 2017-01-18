package cn.bidlink.nbl.framework.springmvc;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.HashSet;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class RequestBodyParamHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver{

	private static final String REQUEST_ATTRIBUTE_RequestBodyParam_JSON = "RequestBodyParam_JSON";
	
	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.hasParameterAnnotation(RequestBodyParam.class);
//		return MethodArgumentResolverRewrite.isSupport(parameter, isSupport);
	}

	@Override
	public Object resolveArgument(MethodParameter parameter,
			ModelAndViewContainer mavContainer, NativeWebRequest webRequest,
			WebDataBinderFactory binderFactory) throws Exception {
		//获得请求对象
		final HttpServletRequest servletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
		//将解析的对象放在Request的attribute中(RequestBodyParam_JSON)
		if(servletRequest.getAttribute(REQUEST_ATTRIBUTE_RequestBodyParam_JSON) == null){
			HttpInputMessage inputMessage = new ServletServerHttpRequest(servletRequest);
			String content = IOUtils.toString(inputMessage.getBody(), "UTF-8");
			JSONObject json = JSON.parseObject(content);
			servletRequest.setAttribute(REQUEST_ATTRIBUTE_RequestBodyParam_JSON, json);
		}
		JSONObject json = (JSONObject) servletRequest.getAttribute(REQUEST_ATTRIBUTE_RequestBodyParam_JSON);
		Class<?> paramType = parameter.getParameterType();
		RequestBodyParam param = parameter.getParameterAnnotation(RequestBodyParam.class);
		//针对集合参数的处理
		if(paramType.isAssignableFrom(ArrayList.class) || paramType.isAssignableFrom(HashSet.class)){
			//如果有泛型
			ParameterizedType pt = (ParameterizedType) parameter.getGenericParameterType();
            Class<?> clazz = (Class<?>) pt.getActualTypeArguments()[0];
			if(param != null){
				return JSON.parseArray(json.getString(param.value()), clazz);
			}else{
				return null;
			}
		//针对单个参数的处理
		}else{
			//return json.getObject(parameter.getParameterName(), paramType);//获取不到
			if(param != null){
				return json.getObject(param.value(), paramType);
			}else {
				return null;
			}
		}
	}
}