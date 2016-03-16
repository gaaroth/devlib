package org.gaaroth.devlib.converter;

import java.util.Locale;

import org.vaadin.gridutil.converter.SimpleStringConverter;

import com.vaadin.server.FontAwesome;

@SuppressWarnings("serial")
public class EmailConverter extends SimpleStringConverter<String> {

	public EmailConverter() {
		super(String.class);
	}

	@Override
	public String convertToPresentation(String value, Class<? extends String> targetType, Locale locale)
			throws com.vaadin.data.util.converter.Converter.ConversionException {
		
		if (value == null) {
			return "";
		} else {
			return FontAwesome.ENVELOPE.getHtml()+" <a href=\"mailto:"+value+"\" >"+value+"</a>";
		}
	}

}
