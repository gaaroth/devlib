package org.gaaroth.devlib.renderer;

import com.vaadin.ui.UI;
import com.vaadin.ui.renderers.DateRenderer;

@SuppressWarnings("serial")
public class CustomDateTimeRenderer extends DateRenderer {
	
	public CustomDateTimeRenderer() {
		super("%1$td/%1$tm/%1$tY %1$tH:%1$tM", UI.getCurrent().getLocale());//tB month name
	}

}
