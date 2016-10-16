package org.durcframework.core.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.durcframework.core.DefaultGridResult;
import org.durcframework.core.GridResult;
import org.durcframework.core.EntityProcessor;
import org.durcframework.core.SearchSupport;
import org.durcframework.core.dao.BaseDao;
import org.durcframework.core.expression.Expression;
import org.durcframework.core.expression.ExpressionQuery;
import org.durcframework.core.service.SearchService;
import org.durcframework.core.util.ClassUtil;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 负责查询的Controller,新建的Controller如果只有查询功能可以继承这个类
 * 
 * @author hc.tang 2013年11月14日
 * 
 * @param <Entity>
 *            实体类
 * @param <Service>
 *            查询的Service
 */
public abstract class SearchController<Entity, Service extends SearchService<Entity, ? extends BaseDao<Entity>>> 
extends BaseController {

	public SearchController(){
		super();
	}
	
	private static final List<Expression> EMPTY_EXPRESSIONS = Collections.emptyList();
	
	private Service service;

	public Service getService() {
		return service;
	}

	@Autowired
	public void setService(Service service) {
		this.service = service;
	}	
	  
	public GridResult success(List<Entity> list){
		GridResult result = getGridResult();
		
		result.setList(list);
		result.setTotal(list.size());
		result.setPageCount(1);
		result.setPageIndex(1);
		
		return result;
	}
	
	/**
	 * 返回默认的结果类
	 * @return 默认的结果类
	 */
	protected GridResult getGridResult() {
		return new DefaultGridResult();
	}
	
	/**
	 * 通过对象获取记录,可以传主键值,也可以传整个对象
	 * @param id 如:get(21)或get(student)
	 * @return 返回实体对象
	 */
	public Entity get(Object id) {
		return this.service.get(id);
	}
	
	/**
	 * 根据查询类查询
	 * @param searchEntity
	 * @return 默认返回DefaultGridResult对象,可以重写getGridResult()方法返回自定义的GridResult
	 */
	public GridResult query(SearchSupport searchEntity) {
		return this.query(searchEntity, EMPTY_EXPRESSIONS);
	}
	
	/**
	 * 根据查询类查询
	 * @param searchEntity
	 * @param expressions 附加查询条件
	 * @return 默认返回DefaultGridResult对象,可以重写getGridResult()方法返回自定义的GridResult
	 */
	public GridResult query(SearchSupport searchEntity,List<Expression> expressions) {
		ExpressionQuery query = this.buildExpressionQuery(searchEntity);
		query.addAll(expressions);
		return this.query(query);
	} 
	
	/**
	 * 根据查询条件查询
	 * @param query
	 * @return 默认返回DefaultGridResult对象,可以重写getGridResult()方法返回自定义的GridResult
	 */
	public GridResult query(ExpressionQuery query) {
		return this.queryWithProcessor(query, null);
	}
	
	/**
	 * 根据查询类查询全部
	 * @param searchEntity 查询类
	 * @return 默认返回DefaultGridResult对象,可以重写getGridResult()方法返回自定义的GridResult
	 */
	public GridResult queryAll(SearchSupport searchEntity) {
		return this.queryAll(searchEntity, EMPTY_EXPRESSIONS);
	}
	
	/**
	 * 根据查询类查询全部
	 * @param searchEntity 查询类
	 * @param expressions 附加查询条件
	 * @return 默认返回DefaultGridResult对象,可以重写getGridResult()方法返回自定义的GridResult
	 */
	public GridResult queryAll(SearchSupport searchEntity,List<Expression> expressions) {
		ExpressionQuery query = this.buildExpressionQuery(searchEntity);
		query.addAll(expressions);
		return this.queryAll(query);
	}
	
	/**
	 * 根据查询条件查询全部
	 * @param query
	 * @return 默认返回DefaultGridResult对象,可以重写getGridResult()方法返回自定义的GridResult
	 */
	public GridResult queryAll(ExpressionQuery query) {
		query.setQueryAll(true);
		return this.query(query);
	}
	
	/**
	 * 根据查询类查询
	 * @param searchEntity 查询类
	 * @param processor 结果处理器
	 * @return 默认返回DefaultGridResult对象,可以重写getGridResult()方法返回自定义的GridResult
	 */
	public GridResult queryWithProcessor(SearchSupport searchEntity,EntityProcessor<Entity> processor) {
		return this.queryWithProcessor(searchEntity, EMPTY_EXPRESSIONS, processor);
	}
	
	/**
	 * 根据查询类查询
	 * @param searchEntity 查询类
	 * @param expressions 附加查询条件
	 * @param processor 结果处理器
	 * @return 默认返回DefaultGridResult对象,可以重写getGridResult()方法返回自定义的GridResult
	 */
	public GridResult queryWithProcessor(SearchSupport searchEntity,List<Expression> expressions,EntityProcessor<Entity> processor) {
		ExpressionQuery query = this.buildExpressionQuery(searchEntity);
		query.addAll(expressions);
		return this.queryWithProcessor(query, processor);
	}
	
	/**
	 * 查询全部
	 * @param query
	 * @param processor 结果处理器
	 * @return 默认返回DefaultGridResult对象,可以重写getGridResult()方法返回自定义的GridResult
	 */
	public GridResult queryAllWithProcessor(ExpressionQuery query,EntityProcessor<Entity> processor) {
		query.setQueryAll(true);
		return this.queryWithProcessor(query, processor);
	}
	
	/**
	 * 根据查询条件查询
	 * @param query 查询条件
	 * @param processor 结果处理器
	 * @param listFilter list过滤器
	 * @return 默认返回DefaultGridResult对象,可以重写getGridResult()方法返回自定义的GridResult
	 */
	public GridResult queryWithProcessor(ExpressionQuery query,
			EntityProcessor<Entity> processor) {
		
		List<Entity> list = this.service.find(query);
		
		int total = 0; // 总条数
		int start = query.getStart();
		int pageSize = query.getPageSize(); // 每页记录数
		int pageIndex = (start / pageSize) + 1; // 当前第几页
		
		if(list.size() > 0) {
			// 总数
			// 如果是查询全部则直接返回结果集条数
			// 如果是分页查询则还需要带入条件执行一下sql
			total = query.getIsQueryAll() ? list.size() : this.service.findTotalCount(query);
		}
				
		GridResult result = getGridResult();
		result.setList(list);
		result.setTotal(total);
		result.setStart(start);
		result.setPageIndex(pageIndex);
		result.setPageSize(pageSize);
		
		int pageCount = DefaultGridResult.calcPageCount(total, pageSize);
		
		result.setPageCount(pageCount);
		
		if(processor != null){
			List<Object> jsonObjList = processEntityToJSONObject(list, processor);
			result.setList(jsonObjList);
		}
		
		return result;
	}
	
	protected ExpressionQuery buildExpressionQuery(SearchSupport searchEntity) {
		ExpressionQuery query = new ExpressionQuery();

		query.addAnnotionExpression(searchEntity)
			.addPaginationInfo(searchEntity);

		return query;
	}

	// 将list中的entity对象处理成JSONObject对象
	private List<Object> processEntityToJSONObject(List<Entity> list,
			EntityProcessor<Entity> processor) {
		List<Object> jsonObjList = new ArrayList<Object>(list.size());
		
		for (Entity entity : list) {
			Map<String,Object> jsonObject = ClassUtil.convertObj2Map(entity);
			processor.process(entity, jsonObject);
			jsonObjList.add(jsonObject);
		}
		
		return jsonObjList;
	}
	
}
