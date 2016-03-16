package org.gaaroth.devlib.field;

import org.gaaroth.devlib.utils.VaadinUtils;

import com.vaadin.server.FontAwesome;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class ValutaField extends TextField {
	
	public ValutaField() {
		setConverter(VaadinUtils.getStandardDoubleConverter());
		setImmediate(true);
		addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
		setIcon(FontAwesome.EUR);
		setNullRepresentation("");
	}

}