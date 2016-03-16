package org.gaaroth.devlib.custom;

import org.gaaroth.devlib.field.CustomDateField;
import org.gaaroth.devlib.field.CustomDateTimeField;
import org.gaaroth.devlib.utils.ReflectionUtils;

import com.vaadin.data.fieldgroup.DefaultFieldGroupFieldFactory;
import com.vaadin.data.util.converter.StringToBigDecimalConverter;
import com.vaadin.data.util.converter.StringToDoubleConverter;
import com.vaadin.data.util.converter.StringToFloatConverter;
import com.vaadin.data.util.converter.StringToIntegerConverter;
import com.vaadin.data.util.converter.StringToLongConverter;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Field;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;

@SuppressWarnings("serial")
public class CustomFieldFactory extends DefaultFieldGroupFieldFactory {
	

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public <T extends Field> T createField(Class<?> dataType, Class<T> fieldType) {
		if (ReflectionUtils.isBoolean(dataType)) {
			CheckBox cb = new CheckBox();
			cb.setImmediate(true);
			cb.setValue(false);
			return (T) cb;
		} else if (ReflectionUtils.isBigDecimal(dataType)) {
			TextField tf = new TextField();
			tf.setImmediate(true);
			tf.setNullRepresentation("");
			tf.setConversionError("Campo non compilato correttamente");
			tf.setConverter(new StringToBigDecimalConverter());
			return (T) tf;
		} else if (ReflectionUtils.isDouble(dataType)) {
			TextField tf = new TextField();
			tf.setImmediate(true);
			tf.setNullRepresentation("");
			tf.setConversionError("Campo non compilato correttamente");
			tf.setConverter(new StringToDoubleConverter());
			return (T) tf;
		} else if (ReflectionUtils.isInteger(dataType) || ReflectionUtils.isBigInteger(dataType)) {
			TextField tf = new TextField();
			tf.setImmediate(true);
			tf.setNullRepresentation("");
			tf.setConversionError("Campo non compilato correttamente");
			tf.setConverter(new StringToIntegerConverter());
			return (T) tf;
		} else if (ReflectionUtils.isLong(dataType)) {
			TextField tf = new TextField();
			tf.setImmediate(true);
			tf.setNullRepresentation("");
			tf.setConversionError("Campo non compilato correttamente");
			tf.setConverter(new StringToLongConverter());
			return (T) tf;
		} else if (ReflectionUtils.isFloat(dataType)) {
			TextField tf = new TextField();
			tf.setImmediate(true);
			tf.setNullRepresentation("");
			tf.setConversionError("Campo non compilato correttamente");
			tf.setConverter(new StringToFloatConverter());
			return (T) tf;
		} else if (ReflectionUtils.isText(dataType)) {
			if (fieldType == TextArea.class) {
				TextArea tf = new TextArea();
				tf.setImmediate(true);
				tf.setNullRepresentation("");
				tf.setConversionError("Campo non compilato correttamente");
				return (T) tf;
			} else if (fieldType == PasswordField.class) {
				PasswordField tf = new PasswordField();
				tf.setImmediate(true);
				tf.setNullRepresentation("");
				tf.setConversionError("Campo non compilato correttamente");
				return (T) tf;
			} else {
				TextField tf = new TextField();
				tf.setImmediate(true);
				tf.setNullRepresentation("");
				tf.setConversionError("Campo non compilato correttamente");
				return (T) tf;
			}
//		} else if (ReflectionUtils.isCollection(dataType)) {
//			MasterDetailEditor mdf = new MasterDetailEditor(fieldFactory, containerForProperty, itemId, propertyId, UI.getCurrent());
//			mdf
		} else if (ReflectionUtils.isFloat(dataType)) {
			TextField tf = new TextField();
			tf.setImmediate(true);
			tf.setNullRepresentation("");
			tf.setConversionError("Campo non compilato correttamente");
			tf.setConverter(new StringToFloatConverter());
			return (T) tf;
		} else {
			if (fieldType == CustomDateField.class) {
				CustomDateField df = new CustomDateField();
				df.setImmediate(true);
				df.setConversionError("Campo non compilato correttamente");
				return (T) df;
			} else if (fieldType == DateField.class) {
				DateField df = new DateField();
				df.setImmediate(true);
				df.setConversionError("Campo non compilato correttamente");
				return (T) df;
			} else if (fieldType == CustomDateTimeField.class) {
				CustomDateTimeField df = new CustomDateTimeField();
				df.setImmediate(true);
				df.setConversionError("Campo non compilato correttamente");
				return (T) df;
			} else {
				return super.createField(dataType, fieldType);
			}
		}
	}

}
