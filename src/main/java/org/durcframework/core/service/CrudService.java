package org.durcframework.core.service;

import java.util.UUID;

import org.durcframework.core.DurcException;
import org.durcframework.core.ReflectHelper;
import org.durcframework.core.UserContext;
import org.durcframework.core.dao.BaseDao;

/**
 * 负责增删改查的Service
 * 
 * @author hc.tang
 * 
 * @param <Entity>
 * @param <Dao>
 */
public abstract class CrudService<Entity, Dao extends BaseDao<Entity>> extends SearchService<Entity, Dao> {

	/**
	 * 保存数据
	 * 
	 * @param entity
	 *            实体对象
	 * @return 返回添加后的实体对象
	 */
	public Entity save(Entity entity) {
		if (entity == null) {
			throw new DurcException("保存数据对象不能为null");
		}
		ReflectHelper reflectHelper = new ReflectHelper(entity);
		// 设置默认值userId、uuid
		if (UserContext.getInstance().getUser() != null) {
			reflectHelper.setMethodValue("userId", UserContext.getInstance().getUser().getId());
			reflectHelper.setMethodValue("uuid", UUID.randomUUID().toString().replaceAll("-", ""));
		}

		this.getDao().save(entity);

		return entity;
	}

	/**
	 * 根据判断来保存数据
	 * 
	 * @param entity
	 * @param saveHandler
	 *            实现该接口用来判断能否保存,返回true则调用save(entity)方法,返回false不保存
	 * @return
	 */
	public Entity save(Entity entity, SaveHandler<Entity> saveHandler) {
		if (saveHandler.canSave(entity)) {
			this.save(entity);
			return entity;
		}
		return null;
	}

	/**
	 * 修改
	 * 
	 * @param entity
	 *            实体对象
	 * @return 返回添加后的实体对象
	 */
	public Entity update(Entity entity) {
		if (entity == null) {
			throw new DurcException("修改数据对象不能为null");
		}
		ReflectHelper reflectHelper = new ReflectHelper(entity);

		// 设置默认值操作人id
		if (UserContext.getInstance().getUser() != null) {
			reflectHelper.setMethodValue("operaterId", UserContext.getInstance().getUser().getId());
		}
		this.getDao().update(entity);
		return entity;
	}

	/**
	 * 删除
	 * 
	 * @param entity
	 *            实体对象
	 */
	public void del(Entity entity) {
		if (entity == null) {
			throw new DurcException("删除数据对象不能为null");
		}

		this.getDao().del(entity);
	}

	/**
	 * 对象是否存在
	 * 
	 * @param entity
	 *            对象
	 * @return 存在返回true
	 */
	public boolean exist(Entity entity) {
		return this.get(entity) != null;
	}
}
