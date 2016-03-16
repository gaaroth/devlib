package org.gaaroth.devlib.converter;

import java.util.Locale;

import org.vaadin.gridutil.converter.SimpleStringConverter;

import com.vaadin.server.FontAwesome;

@SuppressWarnings("serial")
public class IconAndTextConverter extends SimpleStringConverter<String>{
	
	private FontAwesome icon;

	public IconAndTextConverter(FontAwesome icon) {
		super(String.class);
		this.icon = icon;
	}

	public String convertToPresentation(String value, Class<? extends String> targetType, Locale locale)
		throws com.vaadin.data.util.converter.Converter.ConversionException {
		return icon.getHtml()+" "+value;
	}

}
