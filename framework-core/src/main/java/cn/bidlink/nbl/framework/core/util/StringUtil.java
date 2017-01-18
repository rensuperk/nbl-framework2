package cn.bidlink.nbl.framework.core.util;

public final class StringUtil {
	
	public static final boolean isEmpty(String str){
		return !isNotEmpty(str);
	}
	public static final boolean isNotEmpty(String str){
		return str != null && str.trim().length() > 0;
	}
}