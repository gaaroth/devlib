package org.gaaroth.devlib.custom;

import java.util.List;

import org.gaaroth.devlib.filtergenerator.FilterField;
import org.gaaroth.devlib.filtergenerator.FilterGenerator;

import com.vaadin.data.Container;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Grid.CellReference;
import com.vaadin.ui.Grid.CellStyleGenerator;
import com.vaadin.ui.Grid.HeaderCell;
import com.vaadin.ui.Grid.HeaderRow;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class ExtendedCustomComponent<C> extends CustomComponent {
	
	@SuppressWarnings("rawtypes")
	public AbstractField configField(AbstractField field) {
		return configureField(field, "");
	}
	
	@SuppressWarnings("rawtypes")
	public AbstractField configureField(AbstractField field, String caption) {
		field.setWidth("100%");
		field.setValidationVisible(true);
		field.setImmediate(true);
		try { ((TextField)field).setNullRepresentation(""); } catch (Exception e) {}
		try { ((TextArea)field).setNullRepresentation(""); } catch (Exception e) {}
		field.setConversionError("Campo non compilato correttamente");
		field.setCaption(caption);
		return field;
	}
	
	/**
	 * Copre style default per celle show/edit/delete 
	 */
	public CellStyleGenerator getDefaultGridCellStyle() {
		return new CellStyleGenerator() {
			@Override
			public String getStyle(CellReference cellReference) {
				String prop = (String) cellReference.getPropertyId();
				if ("show".equals(prop) || "edit".equals(prop) || "delete".equals(prop)) {
					return "grid-row-no-padding";
				}
				return "";
			}
		};
	}
	
	public MenuItem addAndGetMenuBarSeparator(MenuBar menuBar) {
		return addAndGetMenuBarSeparator(menuBar, null);
	}
	
	public MenuItem addAndGetMenuBarSeparator(MenuBar menuBar, MenuItem itemBefore) {
		MenuItem item = null;
		if (itemBefore == null) {
			item = menuBar.addItem("|", null);
		} else {
			item = menuBar.addItemBefore("|", null, null, itemBefore);
		}
		item.setEnabled(false);
		return item;
	}

	public HeaderCell getAndPrepareCell(HeaderRow headerRow, Object cellId, boolean noPadding) {
		HeaderCell headerCell = headerRow.getCell(cellId);
		if (noPadding) {
			headerCell.setStyleName("grid-row-no-padding");
		} else {
			headerCell.setStyleName("grid-row-filters-cell");
		}
		return headerCell;
	}
	
	public HeaderCell getAndPrepareCell(HeaderRow headerRow, Object cellId) {
		return getAndPrepareCell(headerRow, cellId, false);
	}
	
	public Component createHeader(String headerText) {
		HorizontalLayout header = new HorizontalLayout();
		header.setWidth("100%");
		header.setSpacing(true);
		Label titleLabel = new Label(headerText);
		titleLabel.setSizeUndefined();
		titleLabel.addStyleName(ValoTheme.LABEL_H1);
		titleLabel.addStyleName(ValoTheme.LABEL_NO_MARGIN);
		header.addComponents(titleLabel);
		return header;
	}
	
	public FilterGenerator<C> createFilterGenerator(Class<C> clazz, Container container, List<FilterField> filterList) {
		FilterGenerator<C> fg = new FilterGenerator<C>(clazz, container, filterList) {

			private Panel panel = new Panel();

			@Override
			public Component getComponent() {
				panel.setCaption("Filtri");
				panel.setWidth("100%");
				panel.setHeight("350px");
				VerticalLayout layout = (VerticalLayout) super.getComponent();
				layout.setMargin(true);
				panel.setContent(layout);
				return panel;
			}

			@Override
			public void toggleVisibility() {
				panel.setVisible(!panel.isVisible());
			}

		};
		fg.toggleVisibility();
		return fg;
	}

}
