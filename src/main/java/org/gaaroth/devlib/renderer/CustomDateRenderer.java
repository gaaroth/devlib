package org.gaaroth.devlib.renderer;

import com.vaadin.ui.UI;
import com.vaadin.ui.renderers.DateRenderer;

@SuppressWarnings("serial")
public class CustomDateRenderer extends DateRenderer {
	
	public CustomDateRenderer() {
		super("%1$td/%1$tm/%1$tY", UI.getCurrent().getLocale());//tB month name
	}

}
