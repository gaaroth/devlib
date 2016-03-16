package org.gaaroth.devlib.field;

import java.lang.reflect.Field;

import org.gaaroth.devlib.utils.ReflectionUtils;

@SuppressWarnings({"serial", "unchecked"})
public abstract class AbstractNToNListField<M, C> extends AbstractOneToNListField<M> {
	
	protected Class<C> clazzChild;
	protected String childFieldNameInModel;
	//protected String modelFieldNameInChild;

	public AbstractNToNListField(Class<M> clazz, String parentFieldNameInModel, Class<C> clazzChild, String childFieldNameInModel/*, String modelFieldNameInChild*/){
		super(clazz, parentFieldNameInModel);
		this.clazzChild = clazzChild;
		this.childFieldNameInModel = childFieldNameInModel;
		//this.modelFieldNameInChild = modelFieldNameInChild;
	}
	
	public void setChildInModel(M model, C child) {
		Field fieldParent = ReflectionUtils.getField(clazz, childFieldNameInModel);
		try {
			fieldParent.set(model, child);
		} catch (Exception e) {
			throw new RuntimeException("Impossibile settare child nel model");
		}
	}
	
//	public void setModelInChild(M model, C child) {
//		Field fieldModel = ReflectionUtils.getField(clazzChild, modelFieldNameInChild);
//		try {
//			fieldModel.set(child, model);
//		} catch (Exception e) {
//			throw new RuntimeException("Impossibile settare model nel child");
//		}
//	}

}
