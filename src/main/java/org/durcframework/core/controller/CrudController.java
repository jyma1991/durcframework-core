package org.durcframework.core.controller;

import java.util.UUID;

import org.durcframework.core.DurcException;
import org.durcframework.core.MessageResult;
import org.durcframework.core.ReflectHelper;
import org.durcframework.core.UserContext;
import org.durcframework.core.ValidateHolder;
import org.durcframework.core.dao.BaseDao;
import org.durcframework.core.service.CrudService;
import org.durcframework.core.util.MyBeanUtil;
import org.durcframework.core.util.ValidateUtil;

/**
 * 增删改查的Controller
 * 
 * @author hc.tang 2013年11月14日
 * @param <Entity>
 *            实体类
 * @param <Service>
 *            增删改查的Service
 */
public abstract class CrudController<Entity, Service extends CrudService<Entity, ? extends BaseDao<Entity>>>
		extends SearchController<Entity, Service> {

	/**
	 * 新增记录
	 * 
	 * @param entity
	 * @return
	 */
	public MessageResult save(Entity entity) {
        ReflectHelper reflectHelper = new ReflectHelper(entity);
        // 设置默认值userId、userName、uuid
        if(UserContext.getInstance().getUser()!=null){
        reflectHelper.setMethodValue("userId", UserContext.getInstance().getUser().getId());
        reflectHelper.setMethodValue("uuid", UUID.randomUUID().toString().replaceAll("-", ""));
        }
        ValidateHolder validateHolder = ValidateUtil.validate(entity);

		if (validateHolder.isSuccess()) {
			this.getService().save(entity);
			return success();
		}

		return error("添加失败", validateHolder.buildValidateErrors());
	}


    /**
     * 修改记录
     * 
     * @param entity
     * @return
     */
    public MessageResult update(Entity entity) {
        ReflectHelper reflectHelper = new ReflectHelper(entity);
        // 设置默认值operaterId
        if(UserContext.getInstance().getUser()!=null){
        reflectHelper.setMethodValue("operaterId", UserContext.getInstance().getUser().getId());
        }
        Entity e = this.get(entity);
		if (e == null) {
			throw new DurcException("修改失败-该记录不存在");
		}
		ValidateHolder validateHolder = ValidateUtil.validate(entity);

		if (validateHolder.isSuccess()) {
			MyBeanUtil.copyProperties(entity, e);
			getService().update(entity);
			return success();
		}

		return error("修改失败", validateHolder.buildValidateErrors());
	}

	private String firstToLower(String val) {
		return val.substring(0, 1).toLowerCase() + val.substring(1);

	}

	/**
	 * 删除记录
	 * 
	 * @param entity
	 * @return
	 */
	public MessageResult delete(Entity entity) {
		getService().del(entity);
		return success();
	}

}
