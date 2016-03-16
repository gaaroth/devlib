package org.gaaroth.devlib.extendedtabsheet;

import java.util.ArrayList;
import java.util.List;

import org.gaaroth.devlib.utils.VaadinUtils;

import com.vaadin.ui.Component;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.CloseHandler;
import com.vaadin.ui.TabSheet.Tab;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class ExtendedTabSheet {

	private List<ExtendedTab> tabAperte;
	private TabSheet tabSheet;
	
	public ExtendedTabSheet(){//TODO add boolean focusConfirm
		this.tabAperte = new ArrayList<ExtendedTab>();
		this.tabSheet = new TabSheet();
		this.tabSheet.setSizeFull();
		this.tabSheet.setCloseHandler(new CloseHandler() {
			@Override
			public void onTabClose(TabSheet tabsheet, Component tabContent) {
				removeTab(tabContent);
			}
		});
	}
	
	public void addFancyStyle() {
		this.tabSheet.addStyleName(ValoTheme.TABSHEET_COMPACT_TABBAR);
		this.tabSheet.addStyleName(ValoTheme.TABSHEET_FRAMED);
		this.tabSheet.addStyleName(ValoTheme.TABSHEET_PADDED_TABBAR);
	}
	
	public void removeFancyStyle() {
		this.tabSheet.removeStyleName(ValoTheme.TABSHEET_COMPACT_TABBAR);
		this.tabSheet.removeStyleName(ValoTheme.TABSHEET_FRAMED);
		this.tabSheet.removeStyleName(ValoTheme.TABSHEET_PADDED_TABBAR);
	}
	
	public void addPrimeStyle() {
		this.tabSheet.addStyleName("v-tabsheet-tabitemcell-prime");
	}
	
	public void removePrimeStyle() {
		this.tabSheet.removeStyleName("v-tabsheet-tabitemcell-prime");
	}
	
	public void addSecondStyle() {
		this.tabSheet.addStyleName("v-tabsheet-tabitemcell-second");
	}
	
	public void removeSecondStyle() {
		this.tabSheet.removeStyleName("v-tabsheet-tabitemcell-second");
	}

	public void addPlainStyle() {
		this.tabSheet.addStyleName("v-tabsheet-tabitemcell-white");
	}
	
	public void removePlainStyle() {
		this.tabSheet.removeStyleName("v-tabsheet-tabitemcell-white");
	}
	
	public void beforeRemoveTab(ExtendedTab extendedTab) { }
	public void afterRemoveTab(ExtendedTab extendedTab) { }
	
	public TabSheet getTabSheet() {
		return this.tabSheet;
	}
	
	public void removeCurrentTab() {
		removeTab(tabSheet.getTab(VaadinUtils.getSelectedTabIndex(tabSheet)).getComponent());
	}
	
	public void removeTab(Component tabContent) {
		ExtendedTab extendedTab = getExtendedTabByComponent(tabContent);
		removeTab(extendedTab);
	}
	
	public void removeTab(ExtendedTab extendedTab) {
		beforeRemoveTab(extendedTab);
		tabAperte.remove(extendedTab);
		tabSheet.removeTab(tabSheet.getTab(extendedTab.getComponent()));
		afterRemoveTab(extendedTab);
	}
	
	/**
	 * Caption e Icon si possono settare sul component e vengono renderizzate in "Tab" style
	 */
	public void addTab(final ExtendedTab tabComponent, final Integer position, final boolean closable) {
		addTab(tabComponent, position, closable, false);
	}
	
	/**
	 * Caption e Icon si possono settare sul component e vengono renderizzate in "Tab" style
	 */
	public void addTabAndFocus(final ExtendedTab tabComponent, final Integer position, final boolean closable) {
		addTab(tabComponent, position, closable, true);
	}
	
	private void addTab(final ExtendedTab tabComponent, final Integer position, final boolean closable, final boolean focus) {
		//, String caption, Resource icon
		final ExtendedTab tabComponentAperto = getExtendedTabById(tabComponent.getId());

		if (tabComponentAperto != null) {
			Tab currentTab = tabSheet.getTab(tabComponentAperto.getComponent());
			focusTab(currentTab);
//			CustomDialogs.showConfirm("Esiste gi� una tab aperta. Selezionarla?", new ConfirmDialog.Listener() {
//				@Override
//				public void onClose(ConfirmDialog arg0) {
//					Tab currentTab;
//					if (arg0.isConfirmed()) {
//						currentTab = tabSheet.getTab(tabComponentAperto.getComponent());
//						focusTab(currentTab);
//					}
//					if (arg0.isConfirmed()) {
//						currentTab = setTab(tabComponent, position, closable);
//					} else {
//						currentTab = tabSheet.getTab(tabComponentAperto);
//					}
//					if (focus) {
//						focusTab(currentTab);
//					}
//				}
//			});
		} else {
			Tab currentTab = setTab(tabComponent, position, closable);
			if (focus) {
				focusTab(currentTab);
			}
		};
	}
	
	public Tab setTab(ExtendedTab extendedTab, Integer position, boolean closable) {
		Tab newTab;
		tabAperte.add(extendedTab);
		if (position != null && position >= 0) {
			newTab = this.tabSheet.addTab(extendedTab.getComponent(), position);
		} else {
			newTab = this.tabSheet.addTab(extendedTab.getComponent());
		}
		updateProperties(extendedTab);
		newTab.setClosable(closable);
		return newTab;
	}
	
	public Tab focusTab(Tab tabToFocus) {
		this.tabSheet.setSelectedTab(tabToFocus);
		return tabToFocus;
	}

	public void updateProperties(ExtendedTab extendedTab) {
		//update id in map
		tabSheet.getTab(extendedTab.getComponent()).setCaption(extendedTab.getCaption());
		tabSheet.getTab(extendedTab.getComponent()).setIcon(extendedTab.getIcon());
	}
	
	public ExtendedTab getExtendedTabById(String id) {
		for (ExtendedTab extendedTab : tabAperte) {
			if (extendedTab.getId().equals(id)) {
				return extendedTab;
			}
		}
		return null;
	}
	
	public ExtendedTab getExtendedTabByComponent(Component component) {
		for (ExtendedTab extendedTab : tabAperte) {
			if (extendedTab.getComponent().equals(component)) {
				return extendedTab;
			}
		}
		return null;
	}

}
