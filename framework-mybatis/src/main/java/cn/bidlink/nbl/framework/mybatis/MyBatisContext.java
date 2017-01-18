package cn.bidlink.nbl.framework.mybatis;

import java.util.ArrayList;
import java.util.List;

import cn.bidlink.nbl.framework.mybatis.schema.DbColumn;

public class MyBatisContext {
	
	private static final ThreadLocal<List<DbColumn>> DB_COLUMNS = new ThreadLocal<>();
	
	public static final void addDefaultColumn(DbColumn dbColumn){
		if(DB_COLUMNS.get() == null){
			DB_COLUMNS.set(new ArrayList<DbColumn>());
		}
		DB_COLUMNS.get().add(dbColumn);
	}
	public static final List<DbColumn> getDefaultColumns(){
		return DB_COLUMNS.get();
	}
}