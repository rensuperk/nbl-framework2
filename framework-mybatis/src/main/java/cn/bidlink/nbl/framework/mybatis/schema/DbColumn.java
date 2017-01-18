package cn.bidlink.nbl.framework.mybatis.schema;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.schema.Column;

public class DbColumn {
	
	private Column column;
	private Expression expression;
	
	public Column getColumn() {
		return column;
	}
	public void setColumn(Column column) {
		this.column = column;
	}
	public Expression getExpression() {
		return expression;
	}
	public void setExpression(Expression expression) {
		this.expression = expression;
	}
}