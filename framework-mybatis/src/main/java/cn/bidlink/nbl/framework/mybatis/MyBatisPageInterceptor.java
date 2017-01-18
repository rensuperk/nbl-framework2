package cn.bidlink.nbl.framework.mybatis;

import net.sf.jsqlparser.statement.select.Limit;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectBody;
import net.sf.jsqlparser.statement.select.WithItem;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

@Intercepts({
	@Signature(type = Executor.class, 
		method = "query", 
		args = {
			MappedStatement.class, 
			Object.class, 
			RowBounds.class,
			ResultHandler.class
		}
	)
})
public class MyBatisPageInterceptor extends MyBatisSelectInterceptor {

	@Override
	public String doSelect(Select select, RowBounds rowBounds) {
		SelectBody selectBody = select.getSelectBody();
		if (selectBody instanceof PlainSelect) {
			PlainSelect plainSelect = (PlainSelect) selectBody;
			Limit limit = new Limit();
			limit.setOffset(rowBounds.getOffset());
			limit.setRowCount(rowBounds.getLimit());
			plainSelect.setLimit(limit);
			return plainSelect.toString();
		} else if (selectBody instanceof WithItem) {
			
		} else {
//			SetOperationList operationList = (SetOperationList) selectBody;
//			if (operationList.getPlainSelects() != null
//					&& operationList.getPlainSelects().size() > 0) {
//				List<PlainSelect> plainSelects = operationList
//						.getPlainSelects();
//				for (PlainSelect plainSelect : plainSelects) {
//					processPlainSelect(plainSelect);
//				}
//			}
//			if (!orderByHashParameters(operationList.getOrderByElements())) {
//				operationList.setOrderByElements(null);
//			}
		}
		return null;
	}
}