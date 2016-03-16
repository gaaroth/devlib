package org.gaaroth.devlib.converter;

import java.util.Locale;

import org.joda.time.format.DateTimeFormat;
import org.vaadin.gridutil.converter.SimpleStringConverter;

@SuppressWarnings("serial")
public class StringDateConverter extends SimpleStringConverter<String> {
	
	public StringDateConverter() {
		super(String.class);
	}

	@Override
	public String convertToPresentation(String value, Class<? extends String> targetType, Locale locale)
			throws com.vaadin.data.util.converter.Converter.ConversionException {
		
		if (value == null) {
			return "";
		} else {
			try {
				return DateTimeFormat.forPattern("yyyy-MM-dd")
					.parseDateTime(value.substring(0, 10))
					.toString(DateTimeFormat.forPattern("dd/MM/yyyy"));
			} catch (Exception e) {
				return value;
			}
		}
	}

}
