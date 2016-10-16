package org.durcframework.core.service;

public interface SaveHandler<Entity> {
	boolean canSave(Entity entity);
}
