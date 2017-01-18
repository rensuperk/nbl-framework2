package cn.bidlink.nbl.framework.core.util;

public final class ArrayUtil {
	
	public static final boolean isEmpty(Object[] array){
		return !isNotEmpty(array);
	}
	public static final boolean isNotEmpty(Object[] array){
		return array != null && array.length > 0;
	}
}