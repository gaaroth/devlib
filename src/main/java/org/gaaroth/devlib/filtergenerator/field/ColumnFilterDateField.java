package org.gaaroth.devlib.filtergenerator.field;

import org.gaaroth.devlib.filtergenerator.FilterGenerator;

@SuppressWarnings({"serial", "rawtypes"})
public class ColumnFilterDateField extends ColumnFilterDateTimeField {
	
	public ColumnFilterDateField(final FilterGenerator filterGenerator, final String propertyId, final String caption) {
		super(filterGenerator, propertyId, caption, false);
	}
	
	

}
