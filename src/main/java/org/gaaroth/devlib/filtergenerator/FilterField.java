package org.gaaroth.devlib.filtergenerator;

import java.util.List;

import com.vaadin.data.Validator;
import com.vaadin.ui.Field;

@SuppressWarnings("rawtypes")
public class FilterField {
	
	private String caption;
	private String propertyId;
	private Class<? extends Field> preferredFieldClass = Field.class;
	private Field preferredField = null;
	private List<Validator> customValidators;
	
	public FilterField() {}
	
	public FilterField(String propertyId) {
		this(propertyId, null);
	}
	
	public FilterField(String propertyId, String caption) {
		this.propertyId = propertyId;
		this.caption = caption;
	}
	
	public String getCaption() {
		return caption;
	}
	
	public FilterField setCaption(String caption) {
		this.caption = caption;
		return this;
	}
	
	public String getPropertyId() {
		return propertyId;
	}
	
	public FilterField setPropertyId(String propertyId) {
		this.propertyId = propertyId;
		return this;
	}
	
	public Class<? extends Field> getPreferredFieldClass() {
		return preferredFieldClass;
	}
	
	public FilterField setPreferredFieldClass(Class<? extends Field> preferredFieldClass) {
		this.preferredFieldClass = preferredFieldClass;
		return this;
	}
	
	public Field getPreferredField() {
		return preferredField;
	}
	
	public FilterField setPreferredField(Field preferredField) {
		this.preferredField = preferredField;
		return this;
	}
	
	public List<Validator> getCustomValidators() {
		return customValidators;
	}
	
	public FilterField setCustomValidators(List<Validator> customValidators) {
		this.customValidators = customValidators;
		return this;
	}
}
