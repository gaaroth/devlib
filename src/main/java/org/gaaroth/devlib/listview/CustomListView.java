package org.gaaroth.devlib.listview;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.gaaroth.devlib.extendedtabsheet.ExtendedTab;
import org.gaaroth.devlib.extendedtabsheet.ExtendedTabSheet;
import org.gaaroth.devlib.filtergenerator.FilterField;
import org.gaaroth.devlib.filtergenerator.FilterGenerator;
import org.gaaroth.devlib.notifications.CustomDialogs;
import org.gaaroth.devlib.propertyvaluegenerator.IconPropertyValueGenerator;
import org.vaadin.dialogs.ConfirmDialog;

import com.vaadin.addon.jpacontainer.EntityItem;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.data.Container.ItemSetChangeEvent;
import com.vaadin.data.Container.ItemSetChangeListener;
import com.vaadin.data.util.GeneratedPropertyContainer;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.CellReference;
import com.vaadin.ui.Grid.CellStyleGenerator;
import com.vaadin.ui.Grid.Column;
import com.vaadin.ui.Grid.FooterCell;
import com.vaadin.ui.Grid.FooterRow;
import com.vaadin.ui.Grid.HeaderCell;
import com.vaadin.ui.Grid.HeaderRow;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.renderers.ButtonRenderer;
import com.vaadin.ui.renderers.ClickableRenderer.RendererClickEvent;
import com.vaadin.ui.renderers.ClickableRenderer.RendererClickListener;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public abstract class CustomListView<M> {// extends VerticalLayout

	protected VerticalLayout mainLayout;

	protected final Logger logger = LogManager.getLogger(getClass());
	protected GeneratedPropertyContainer gpc;

	protected Label titleLabel;
	protected JPAContainer<M> container;
	protected ExtendedTabSheet extendedTabSheet;
	protected FilterGenerator<M> filterGenerator = null;

	protected MenuBar menuBar;
	protected MenuItem itemNuovo, itemToggleFiltri, itemPulisciFiltri, itemAggiornaGrid, separator1, separator2;
	protected Grid grid;
	protected FooterCell footerCell;

	/**
	 * Da chiamare per generare il widget
	 */
	// public Component generateWidget() {
	@Deprecated
	public CustomListView() {
		mainLayout = new VerticalLayout();

		container = JPAContainerFactory.make(getModelClass(), getEntityManager());
		mainLayout.setSizeFull();
		mainLayout.setSpacing(true);

		// HEADER
		if (getListTitle() != null) {
			HorizontalLayout header = new HorizontalLayout();
			header.setWidth("100%");
			header.setSpacing(true);
			titleLabel = new Label(getListTitle());
			titleLabel.setSizeUndefined();
			titleLabel.addStyleName(ValoTheme.LABEL_H1);
			titleLabel.addStyleName(ValoTheme.LABEL_NO_MARGIN);
			header.addComponents(titleLabel);
			mainLayout.addComponent(header);
		}

		extendedTabSheet = new ExtendedTabSheet();
		mainLayout.addComponent(extendedTabSheet.getTabSheet());
		mainLayout.setExpandRatio(extendedTabSheet.getTabSheet(), 1);

		// FILTRI
		filterGenerator = generateFilters();

		// TOOLBAR
		menuBar = new MenuBar();
		menuBar.setImmediate(true);
		menuBar.setWidth("100%");
		menuBar.addStyleName(ValoTheme.MENUBAR_BORDERLESS);
		itemAggiornaGrid = menuBar.addItem("Aggiorna", FontAwesome.REFRESH, new Command() {
			@Override
			public void menuSelected(MenuItem selectedItem) {
				refreshGrid();
			}
		});
		separator2 = addMenuBarSeparator(null);
		itemToggleFiltri = menuBar.addItem("Filtri", FontAwesome.FILTER, new Command() {
			@Override
			public void menuSelected(MenuItem selectedItem) {
				filterGenerator.toggleVisibility();
			}
		});
		itemPulisciFiltri = menuBar.addItem("Pulisci filtri", FontAwesome.TRASH_O, new Command() {
			@Override
			public void menuSelected(MenuItem selectedItem) {
				filterGenerator.resetFiltri();
			}
		});
		separator2 = addMenuBarSeparator(null);
		itemNuovo = menuBar.addItem("Nuovo", FontAwesome.PLUS, new Command() {
			@Override
			public void menuSelected(MenuItem selectedItem) {
				createNewTabAndFocus(getNewModel());
			}
		});

		// TABELLA
		grid = new Grid();
		grid.setCellStyleGenerator(new CellStyleGenerator() {
			@Override
			public String getStyle(CellReference cellReference) {
				String prop = (String) cellReference.getPropertyId();
				if ("show".equals(prop) || "edit".equals(prop) || "delete".equals(prop)) {
					return "grid-row-no-padding";
				}
				return "";
			}
		});
		grid.setFooterVisible(true);
		gpc = new GeneratedPropertyContainer(container);
		configureGrid();

		gpc.addGeneratedProperty("show", new IconPropertyValueGenerator(FontAwesome.SEARCH));
		grid.addColumn("show").setWidth(35).setHidden(true).setHeaderCaption("");
		// getAndPrepareCell(null, "show", true);

		if (grid.getColumn("edit") != null && grid.getColumn("delete") != null) {
			grid.getDefaultHeaderRow().join("edit", "delete").setText("");
		} else if (grid.getColumn("edit") != null) {
			grid.getColumn("edit").setHeaderCaption("");
		} else if (grid.getColumn("delete") != null) {
			grid.getColumn("delete").setHeaderCaption("");
		}

		FooterRow footerRow = grid.addFooterRowAt(0);
		List<Object> idCol = new ArrayList<Object>();
		for (Column col : grid.getColumns()) {
			idCol.add(col.getPropertyId());
		}

		footerCell = footerRow.join(idCol.toArray());
		footerCell.setText("Totale: " + container.size() + " righe");
		grid.setContainerDataSource(gpc);
		gpc.addListener(new ItemSetChangeListener() {
			@Override
			public void containerItemSetChange(ItemSetChangeEvent event) {
				footerCell.setText("Totale: " + container.size() + " righe");
			}
		});
		grid.setSizeFull();

		final VerticalLayout firstPageLayout = new VerticalLayout();
		firstPageLayout.setSizeFull();
		firstPageLayout.setImmediate(true);
		firstPageLayout.addComponent(menuBar);
		firstPageLayout.addComponent(filterGenerator.getComponent());
		firstPageLayout.addComponent(filterGenerator.getTokenRow());
		firstPageLayout.addComponent(grid);
		firstPageLayout.setExpandRatio(grid, 1);
		ExtendedTab extTab = new ExtendedTab(extendedTabSheet) {
			@Override
			public String getId() {
				return "0";
			}

			@Override
			public Resource getIcon() {
				return FontAwesome.LIST;
			}

			@Override
			public Component getComponent() {
				return firstPageLayout;
			}

			@Override
			public String getCaption() {
				return getListTitle();
			}
		};
		extendedTabSheet.addTab(extTab, null, false);
	}

	public Component asTabSheet() {
		return getMainLayout();
	}

	public Component asList() {
		return grid;
	}

	public VerticalLayout getMainLayout() {
		return mainLayout;
	}

	public MenuItem addMenuBarSeparator(MenuItem itemBefore) {
		MenuItem item = null;
		if (itemBefore == null) {
			item = menuBar.addItem("|", null);
		} else {
			item = menuBar.addItemBefore("|", null, null, itemBefore);
		}
		item.setEnabled(false);
		return item;
	}

	public void addEditDeleteColumn(boolean addEdit, boolean addDelete, HeaderRow headerRow) {
		if (addEdit) {
			gpc.addGeneratedProperty("edit", new IconPropertyValueGenerator(FontAwesome.EDIT));
			grid.addColumn("edit").setWidth(35).setRenderer(new ButtonRenderer/*HtmlButtonRenderer*/(new RendererClickListener() {
				@Override
				public void click(RendererClickEvent event) {
					editItem(event.getItemId());
				}
			}));
			getAndPrepareCell(headerRow, "edit");
		}
		if (addDelete) {
			gpc.addGeneratedProperty("delete", new IconPropertyValueGenerator(FontAwesome.TRASH_O));
			grid.addColumn("delete").setWidth(35).setRenderer(new ButtonRenderer/*HtmlButtonRenderer*/(new RendererClickListener() {
				@Override
				public void click(final RendererClickEvent event) {
					CustomDialogs.showConfirm("Conferma eliminazione", new ConfirmDialog.Listener() {
						@Override
						public void onClose(ConfirmDialog dialog) {
							if (dialog.isConfirmed()) {
								deleteItem(event.getItemId());
							}
						}
					});
				}
			}));
			getAndPrepareCell(headerRow, "delete");
		}
	}

	public void createNewTabAndFocus(M objectItem) {
		try {
			ExtendedTab item = createTab(objectItem);
			extendedTabSheet.addTabAndFocus(item, null, true);
		} catch (Exception e) {
			logger.error("Errore creazione nuova tab", e);
		}
	}

	public FilterGenerator<M> generateFilters() {
		FilterGenerator<M> fg = new FilterGenerator<M>(
			getModelClass(), getContainer(), getFilterFieldList()) {

			private Panel panel = new Panel();

			@Override
			public Component getComponent() {
				panel.setCaption("Filtri");
				panel.setWidth("100%");
				panel.setHeight("340px");
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

	public M createNewObject() {
		try {
			EntityItem<M> entityItem = null;
			entityItem = getContainer().createEntityItem(getModelClass().newInstance());
			return entityItem.getEntity();
		} catch (Exception e) {
			logger.error("Errore creazione nuovo oggetto", e);
		}
		return null;
	}

	public void refreshGrid() {
		getFilterGenerator().refreshContainer();
	}

	public void editItem(Object idItem) {
		M model = getContainer().getItem(idItem).getEntity();
		createNewTabAndFocus(model);
	}

	public void deleteItem(Object idItem) {
		EntityItem<M> entityItem = getContainer().getItem(idItem);
		getContainer().removeItem(entityItem.getItemProperty(getModelIdName()).getValue());
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

	public FilterGenerator<M> getFilterGenerator() {
		return filterGenerator;
	}

	public JPAContainer<M> getContainer() {
		return container;
	}

	public MenuBar getMenuBar() {
		return menuBar;
	}

	public Grid getGrid() {
		return grid;
	}

	public ExtendedTabSheet getTabSheet() {
		return extendedTabSheet;
	}

	public ExtendedTabSheet getExtendedTabSheet() {
		return extendedTabSheet;
	}

	public MenuItem getItemNuovo() {
		return itemNuovo;
	}

	public MenuItem getItemToggleFiltri() {
		return itemToggleFiltri;
	}

	public MenuItem getItemPulisciFiltri() {
		return itemPulisciFiltri;
	}

	public MenuItem getItemAggiornaGrid() {
		return itemAggiornaGrid;
	}

	public MenuItem getSeparator1() {
		return separator1;
	}

	public MenuItem getSeparator2() {
		return separator2;
	}

	/**
	 * Estensioni per ogni nuova lista
	 */
	public abstract EntityManager getEntityManager();
	
	public abstract String getModelIdName();

	public abstract Class<M> getModelClass();

	public abstract String getListTitle();

	public abstract void configureGrid();

	public abstract List<FilterField> getFilterFieldList();

	public abstract ExtendedTab createTab(M model);

	public abstract  M getNewModel();

}
