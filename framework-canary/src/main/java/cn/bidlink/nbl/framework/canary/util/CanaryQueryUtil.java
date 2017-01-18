package cn.bidlink.nbl.framework.canary.util;

import java.util.Collections;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import cn.bidlink.nbl.framework.core.util.ArrayUtil;

/**
 * TODO bohuilan 类描述.
 * 
 * @version : Ver 1.0
 * @author : <a href="mailto:huilanbo@ebnew.com">bohuilan</a>
 * @date : 2016年7月19日 下午4:32:40
 */
public class CanaryQueryUtil {
	
	public static final String parse(String name, Object value){
		return name + ":" + value;
	}
	public static final String parseRange(String name, Object begin, Object end){
		return name + ":[" + begin + "," + end + "]";
	}
	
	/**
	 * 添加查询参数,value为多个值的，如：查询值为01,02,03 或者 ct 医疗设备
	 */
	public static final String parseArray(String name, Object[] array, String logic) {
		StringBuilder fq = new StringBuilder();
		if(ArrayUtil.isNotEmpty(array)){
			int i = 0;
			for(Object value : array){
				if (!StringUtils.isEmpty(value)) {
					fq.append(name).append(":").append(value);
				}
				if(i < array.length - 1){
					fq.append(" ").append(logic).append(" ");
				}
			}
		}
		return fq.toString();
	}
}