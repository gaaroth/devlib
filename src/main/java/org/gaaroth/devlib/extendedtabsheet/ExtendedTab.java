package org.gaaroth.devlib.extendedtabsheet;

import com.vaadin.server.Resource;
import com.vaadin.ui.Component;

public abstract class ExtendedTab {
	
	private ExtendedTabSheet extendedTabSheet;

	//public ExtendedTab() { }
	
	public ExtendedTab(ExtendedTabSheet extendedTabSheet) {
		this.extendedTabSheet = extendedTabSheet;
	}
	
	public abstract String getId();
	public abstract Resource getIcon();
	public abstract String getCaption();
	public abstract Component getComponent();
	
	public void updateProperties() {
		extendedTabSheet.updateProperties(this);
	}
	
	public ExtendedTabSheet getExtendedTabSheet() {
		return extendedTabSheet;
	}

	public void setExtendedTabSheet(ExtendedTabSheet extendedTabSheet) {
		this.extendedTabSheet = extendedTabSheet;
	}

}
