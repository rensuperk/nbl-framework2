package cn.bidlink.nbl.framework.springmvc;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class FastjsonHttpMessageConverter implements HttpMessageConverter<Object>{

	@Override
	public boolean canRead(Class<?> clazz, MediaType mediaType) {
		return true;
	}

	@Override
	public boolean canWrite(Class<?> clazz, MediaType mediaType) {
		return true;
	}

	@Override
	public List<MediaType> getSupportedMediaTypes() {
		return Arrays.asList(MediaType.APPLICATION_JSON);
	}

	@Override
	public Object read(Class<? extends Object> clazz,
			HttpInputMessage inputMessage) throws IOException,
			HttpMessageNotReadableException {
		String content = IOUtils.toString(inputMessage.getBody(), "UTF-8");
		return JSON.parseObject(content, clazz);
	}

	@Override
	public void write(Object object, MediaType contentType,
			HttpOutputMessage outputMessage) throws IOException,
			HttpMessageNotWritableException {
		String content = JSON.toJSONString(object, SerializerFeature.WriteMapNullValue);
		IOUtils.write(content, outputMessage.getBody(), "UTF-8");
	}
}