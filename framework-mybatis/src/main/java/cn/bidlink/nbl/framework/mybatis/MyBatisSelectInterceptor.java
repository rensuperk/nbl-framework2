package cn.bidlink.nbl.framework.mybatis;

import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.update.Update;

import org.apache.ibatis.session.RowBounds;

public abstract class MyBatisSelectInterceptor extends MyBatisSqlInterceptor {
	
	public abstract String doSelect(Select select, RowBounds rowBounds);

	@Override
	public String doInsert(Insert insert) {
		return null;
	}

	@Override
	public String doUpdate(Update update) {
		return null;
	}

	@Override
	public String doDelete(Delete delete) {
		return null;
	}
}