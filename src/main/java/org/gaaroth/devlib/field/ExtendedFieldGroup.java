package org.gaaroth.devlib.field;

import org.gaaroth.devlib.custom.CustomFieldFactory;
import org.gaaroth.devlib.utils.FormatUtils;

import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Field;

@SuppressWarnings("serial")
public class ExtendedFieldGroup extends FieldGroup {

	public ExtendedFieldGroup() {
		this.setFieldFactory(new CustomFieldFactory());
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Field<?> buildBindConfigure(String propertyId, Class fieldType) throws BindException {
		Field<?> field = null;
		if (fieldType != null) {
			field = super.buildAndBind("", propertyId, fieldType);
		} else {
			field = super.buildAndBind("", propertyId);
		}
		return configureField((AbstractField) field, FormatUtils.humanizeString(propertyId));
	}
	
	public Field<?> buildBindConfigure(String propertyId) throws BindException {
		return buildBindConfigure(propertyId, null);
	}
	
	@SuppressWarnings("rawtypes")
	public AbstractField configureField(AbstractField field, String caption) {
		field.setWidth("100%");
		field.setValidationVisible(true);
		field.setCaption(caption);
		return field;
	}
	
}
