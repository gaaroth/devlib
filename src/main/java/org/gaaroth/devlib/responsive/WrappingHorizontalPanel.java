package org.gaaroth.devlib.responsive;

import com.vaadin.server.Responsive;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class WrappingHorizontalPanel extends CssLayout {
	
	public WrappingHorizontalPanel(int maxComponentsInRow, Component... components) {
		this(maxComponentsInRow);
		for (Component component : components) {
			addComponent(component);
		}
	}
	
	public WrappingHorizontalPanel(int maxComponentsInRow) {
		addStyleName("flexwrap"+maxComponentsInRow);
		setWidth("100%");
		setResponsive(true);
	}

	@Override
	public void addComponent(Component component) {
		addComponent(component, null, null);
	}
	
	@SuppressWarnings("rawtypes")
	public void addComponent(Component component, Alignment alignment, String height) {
		if (component instanceof AbstractField) {
			((AbstractField)component).setWidth("100%");
			((AbstractField)component).setValidationVisible(true);
		}
		
		VerticalLayout vl = new VerticalLayout();
		vl.addStyleName("itembox");
		vl.setHeight("100%");
		vl.setMargin(new MarginInfo(false, true, true, false));
		vl.addComponent(component);
		if (alignment != null) {
			vl.setComponentAlignment(component, alignment);
		}
		if (height != null) {
			vl.setHeight(height);
		}
		Responsive.makeResponsive(vl);
		super.addComponent(vl);
	}

}
