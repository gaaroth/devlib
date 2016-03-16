package org.gaaroth.devlib.propertyvaluegenerator;

import com.vaadin.data.Item;
import com.vaadin.data.util.PropertyValueGenerator;

@SuppressWarnings("serial")
public class CustomEmptyPropertyValueGenerator extends PropertyValueGenerator<String> {
	
	@Override
	public String getValue(Item item, Object itemId, Object propertyId) {
		return "&nbsp;";
	}

	@Override
	public Class<String> getType() {
		return String.class;
	}

}
