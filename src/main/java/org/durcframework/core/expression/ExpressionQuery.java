package org.durcframework.core.expression;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.durcframework.core.SearchSupport;
import org.durcframework.core.expression.subexpression.ListExpression;
import org.durcframework.core.expression.subexpression.SqlExpression;
import org.durcframework.core.expression.subexpression.ValueExpression;

/**
 * 查询条件类
 * 
 * 2011-10-22
 */
public class ExpressionQuery {
	
	private static final String REG_SQL_INJECT = "([';\\*--\\|])+";
	// 起始索引
	private int start;
	// 数据长度,即每页10条记录
	private int limit = 10;
	// 排序信息
	private Set<String> orderInfo = new LinkedHashSet<String>();
	// 是否查询全部
	private boolean queryAll;
	
	private Map<String, Object> paramMap = new HashMap<String, Object>();

	private List<ValueExpression> valueExprList = new ArrayList<ValueExpression>();
	private List<JoinExpression> joinExprList = new ArrayList<JoinExpression>();
	private List<ListExpression> listExprList = new ArrayList<ListExpression>();
	private List<SqlExpression> sqlExpreList = new ArrayList<SqlExpression>();
	
	public static ExpressionQuery buildQueryAll(){
		ExpressionQuery query = new ExpressionQuery();
		query.queryAll = true;
		return query;
	}
	
	public void addAll(List<Expression> expressions){
		if(expressions != null){
			for (Expression expression : expressions) {
				this.add(expression);
			}
		}
	}
	
	/**
	 * 添加注解查询条件
	 * @param searchEntity
	 * @return
	 */
	public ExpressionQuery addAnnotionExpression(SearchSupport searchEntity) {
		List<Expression> expresList = ExpressionBuilder.buildExpressions(searchEntity);

		for (Expression express : expresList) {
			add(express);
		}
		
		return this;
	}
	
	/**
	 * 添加分页信息
	 */
	public ExpressionQuery addPaginationInfo(SearchSupport searchEntity){
		this.start = searchEntity.getStart();
		this.limit = searchEntity.getLimit();
		this.addSort(searchEntity.getSortname(), searchEntity.getSortorder());
		return this;
	}
	
	public ExpressionQuery addSqlExpression(SqlExpression expression) {
		sqlExpreList.add(expression);
		return this;
	}
	
	public ExpressionQuery addValueExpression(ValueExpression expression) {
		valueExprList.add(expression);
		return this;
	}

	public ExpressionQuery addJoinExpression(JoinExpression expression) {
		joinExprList.add(expression);
		return this;
	}

	public ExpressionQuery addListExpression(ListExpression expression) {
		listExprList.add(expression);
		return this;
	}

	public ExpressionQuery addParam(String name,Object value) {
		paramMap.put(name, value);
		return this;
	}


	public ExpressionQuery add(Expression expre) {
		expre.addToQuery(this);
		return this;
	}
	
	/**
	 * 是否查询全部
	 * @return
	 */
	public boolean getIsQueryAll() {
		return this.queryAll;
	}
	
	public void setQueryAll(boolean queryAll) {
		this.queryAll = queryAll;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public void setPageSize(int pageSize) {
		this.setLimit(pageSize);
	}

	public int getPageSize() {
		return this.getLimit();
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}
	
	/**
	 * 添加ASC排序字段,
	 * @param sortname 数据库字段名
	 * @return
	 */
	public ExpressionQuery addSort(String sortname){
		return this.addSort(sortname, null);
	}
	
	/**
	 * 添加排序字段
	 * @param sortname 数据库字段名
	 * @param sortorder 排序方式,ASC,DESC
	 * @return
	 */
	public ExpressionQuery addSort(String sortname,String sortorder) {
		
		if(sortname != null){
			// 简单防止SQL注入
			sortname = sortname.replaceAll(REG_SQL_INJECT,SqlContent.EMPTY);
			
			if(!SqlContent.ASC.equalsIgnoreCase(sortorder) 
					&& !SqlContent.DESC.equalsIgnoreCase(sortorder)){
				sortorder = SqlContent.ASC;
			}
			
			orderInfo.add(sortname + SqlContent.BLANK + sortorder);
		}
		
		return this;
	}
	
	/**
	 * 是否具备排序,是返回true
	 * @return
	 */
	public boolean getOrderable() {
		return orderInfo.size() > 0;
	}
	
	/**
	 * 返回排序信息
	 * @return 返回排序信息,如:id ASC,name ASC,date desc. 没有排序则返回""
	 */
	public String getOrder() {
		return StringUtils.join(orderInfo, SqlContent.COMMA);
	}
	
	/**
	 * 兼容老版本,不推荐.改用getOrder()
	 * @return
	 */
	@Deprecated
	public String getSortname() {
		if(this.getOrderable()){
			return this.getOrder();
		}
		return null;
	}
	
	/**
	 * 返回第一条记录
	 * 
	 * @return
	 */
	public int getFirstResult() {
		return this.start;
	}

	public void setFirstResult(int firstResult) {
		this.start = firstResult;
	}

	public List<ValueExpression> getValueExprList() {
		return valueExprList;
	}

	public List<JoinExpression> getJoinExprList() {
		return joinExprList;
	}

	public List<ListExpression> getListExprList() {
		return listExprList;
	}

	public Map<String, Object> getParamMap() {
		return paramMap;
	}

	public void setParamMap(Map<String, Object> paramMap) {
		this.paramMap = paramMap;
	}

}
