package org.gaaroth.devlib.field;

import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.DateField;

@SuppressWarnings("serial")
public class CustomDateTimeField extends DateField {
	
	public CustomDateTimeField() {
		this(true);
	}
	
	public CustomDateTimeField(boolean showTime) {
		super();
		showTime(showTime);
	}

	public void showTime(boolean showTime) {
		if (showTime) {
			setDateFormat("dd/MM/yyyy HH:mm");
			setResolution(Resolution.MINUTE);
		} else {
			setDateFormat("dd/MM/yyyy");
			setResolution(Resolution.DAY);
		}
	}

}
