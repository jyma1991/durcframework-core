package org.durcframework.core.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import net.sf.jxls.transformer.XLSTransformer;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Workbook;
import org.durcframework.core.DurcException;
import org.durcframework.core.SearchEntity;
import org.durcframework.core.dao.BaseDao;
import org.durcframework.core.expression.ExpressionQuery;
import org.durcframework.core.service.SearchService;
import org.durcframework.core.util.DateUtil;
import org.springframework.util.StringUtils;


/**
 * 负责导出的Controller,如果想要实现导出功能,可以继承该类
 * @author hc.tang
 * 2014年6月17日
 *
 * @param <Entity>
 * @param <Service>
 */
public abstract class ExportController<Entity, Service extends SearchService<Entity, ? extends BaseDao<Entity>>>
		extends SearchController<Entity, Service> {
	
	private static Logger logger = Logger.getLogger(ExportController.class);

	
	private ExportService exportService = new ExportService();
	
	/**
	 * 获取模板文件的绝对路径
	 * @return
	 */
	public abstract String getTemplateFilePath();
	
	protected String getModelName() {
		return "obj";
	}
	
	
	/**
	 * 通过查询参数导出
	 * @param searchEntity
	 * @param response
	 */
	public void exportBySearchEntity(
			SearchEntity searchEntity
			,HttpServletResponse response) {
		this.exportByQuery(this.buildExpressionQuery(searchEntity), response);
	}
	
	
	/**
	 * 通过条件导出
	 * @param query
	 * @param response
	 */
	public void exportByQuery(ExpressionQuery query,HttpServletResponse response){
		try {
			List<?> list = this.getService().find(query);

			doExport(list, response);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			throw new DurcException("导出Excel失败,请查看日志");
		}	
	}
	
	/**
	 * 导出
	 * @param list
	 * @param response
	 */
	public void doExport(List<?> list,HttpServletResponse response){
		check();
		
		ExportEntity exportEntity = new ExportEntity();
		
		exportEntity.setTemplateFilePath(this.getTemplateFilePath());
		exportEntity.setList(list);
		
		exportService.export(exportEntity,getModelMap(),response);
	}
	
	/**
	 * 类似于ModelMap的用法,如:<br><br>
	 * <code>
	 * 1.<br>
	 * Map&lt;String, Object&gt; model = super.getModelMapResult();<br>
	 * model.put("title", "学生信息");<br>
	 * excel中:<br>
	 * ${title}<br><br>
	 * 
	 * 2.<br>
	 * Student student = new Student();<br>
	 * Map&lt;String, Object&gt; model = super.getModelMapResult();<br>
	 * model.put("student", student);<br>
	 * excel中:<br>
	 * ${student.name};
	 * </code>
	 * @return
	 */
	protected Map<String, Object> getModelMap() {
		return new HashMap<String, Object>();
	}
	
	private static List<String> XLS_NAME_LIST = Arrays.asList(".xls",".XLS");
	
	private void check(){
		String tempFilePath = getTemplateFilePath();
		if(StringUtils.hasText(tempFilePath)){
			String suffix = tempFilePath.substring(tempFilePath.length() - 4);
			if(!XLS_NAME_LIST.contains(suffix)){
				logger.error("Excel模板文件必须以xls结尾");
				throw new DurcException("Excel模板文件必须以xls结尾");
			}
			if(!new File(tempFilePath).exists()){
				logger.error("Excel模板文件不存在");
				throw new DurcException("Excel模板文件不存在");
			}
		}
	}
	
	/**
	 * 返回导出文件名
	 * @return
	 */
	public String getExportFileName(){
		String timestamp = DateUtil.convertDateToString(new Date(), "yyyyMMddHHmmss");
		String templateFilePath = getTemplateFilePath();
		
		if(StringUtils.hasText(templateFilePath)){
			int index = templateFilePath.lastIndexOf("/") + 1;
			String name  = templateFilePath.substring(index, templateFilePath.length());
			return new StringBuilder(name).insert(name.indexOf("."), timestamp).toString();
		}else{
			return "export" + timestamp;
		}
		
	}

	//--------------------私有类---------------------// 
	private class ExportService {

		private static final String CONTENT_TYPE = "application/vnd.ms-excel;charset=GBK";
		private static final String F_HEADER_ARGU1 = "Content-Disposition";
		private static final String F_HEADER_ARGU2 = "attachment;filename=";

		public  void export(
				ExportEntity exportEntity, Map<String, Object> beans,
				HttpServletResponse response) {
			// 设置响应头
			response.setContentType(CONTENT_TYPE);
			response.setHeader(F_HEADER_ARGU1, F_HEADER_ARGU2 + getExportFileName());

			beans.put(getModelName(), exportEntity.getList());
			
			InputStream is = null;
			try {
				is = new BufferedInputStream(new FileInputStream(
						exportEntity.getTemplateFilePath()));
				ServletOutputStream out = response.getOutputStream();
				XLSTransformer transformer = new XLSTransformer();
				Workbook workbook = transformer.transformXLS(is, beans);
				workbook.write(out);
			} catch (FileNotFoundException e) {
				logger.error(e.getMessage(),e);
				throw new DurcException("Excel模板文件不存在,请联系管理员");
			} catch (Exception e) {
				logger.error(e.getMessage(),e);
				throw new DurcException("Excel导出错误");
			}finally{
				if(is != null){
					try {
						is.close();
					} catch (IOException e) {
						logger.error(e.getMessage(),e);
					}
				}
			}

		}
	}
	// 辅助类
	private class ExportEntity {
		
		private String templateFilePath;
		
		private List<?> list = Collections.emptyList();

		public String getTemplateFilePath() {
			return templateFilePath;
		}

		public void setTemplateFilePath(String templateFilePath) {
			this.templateFilePath = templateFilePath;
		}
		
		public List<?> getList() {
			return list;
		}

		public void setList(List<?> list) {
			this.list = list;
		}

	}

}
