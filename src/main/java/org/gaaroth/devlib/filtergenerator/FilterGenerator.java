package org.gaaroth.devlib.filtergenerator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.Id;

import org.gaaroth.devlib.custom.CustomFieldFactory;
import org.gaaroth.devlib.notifications.CustomNotifications;
import org.gaaroth.devlib.responsive.WrappingHorizontalPanel;
import org.gaaroth.devlib.tokenrow.TokenRow;
import org.gaaroth.devlib.utils.FormatUtils;
import org.gaaroth.devlib.utils.ReflectionUtils;

import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.data.Container;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.Container.Filterable;
import com.vaadin.data.Container.Sortable;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.util.converter.AbstractStringToNumberConverter;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.data.util.filter.Like;
import com.vaadin.data.util.filter.Not;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Field;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings({ "serial", "rawtypes" })
public class FilterGenerator<T> {

	private Map<String, Field> mappaFields;

	private Container/*JPAContainer<T>*/ container;

	private List<Filter> staticFilters;
	private Map<Object, Boolean> staticSort;

	private Class<T> clazz;
	private BeanFieldGroup<T> fieldGroup;

	private WrappingHorizontalPanel filterSetupRow;
	private VerticalLayout rootLayout;
//	private GridLayout formGridLayout;
	private Button aggiungiFiltroButton;
	private ComboBox filtriSelezionabiliComboBox;
	private ComboBox tipoFiltroComboBox;
	private Field valoreFiltroField;
	private MenuBar filtriMenuBar;
	private Table filtriImpostatiTable;
	private Map<Object, Filter> filtriList;

	private TokenRow tokenRow;

	public enum TipoFiltro {
		UGUALE, DIVERSO, DIVERSO_STRING, CONTIENTE, MAGGIORE, MAGGIORE_UGUALE, MINORE, MINORE_UGUALE, SELEZIONATO, NON_SELEZIONATO;
	}

	private List<TipoFiltro> tipiFiltriTesto = Arrays.asList(TipoFiltro.CONTIENTE, TipoFiltro.DIVERSO_STRING, TipoFiltro.UGUALE);
	private List<TipoFiltro> tipiFiltriDataNumero = Arrays.asList(TipoFiltro.UGUALE, TipoFiltro.DIVERSO, TipoFiltro.MINORE, TipoFiltro.MAGGIORE,
			TipoFiltro.MINORE_UGUALE, TipoFiltro.MAGGIORE_UGUALE);
	private List<TipoFiltro> tipiFiltriBoolean = Arrays.asList(TipoFiltro.DIVERSO, TipoFiltro.UGUALE);

	public FilterGenerator(Class<T> classe, Container container, List<FilterField> filterFields) {

		this.clazz = classe;
		this.container = container;

		mappaFields = new HashMap<String, Field>();
		filtriList = new HashMap<Object, Filter>();

		staticFilters = new ArrayList<Filter>();
		staticSort = new HashMap<Object, Boolean>();
		
		aggiungiFiltroButton = new Button("Aggiungi filtro", FontAwesome.PLUS);
		aggiungiFiltroButton.addStyleName(ValoTheme.BUTTON_PRIMARY);
		aggiungiFiltroButton.setImmediate(true);
		aggiungiFiltroButton.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				if (filtriSelezionabiliComboBox.isValid() && tipoFiltroComboBox.isValid() && valoreFiltroField.isValid()) {
					aggiungiFiltroDaWidget();
				} else {
					CustomNotifications.showWarning("Compilare correttamente i campi");
				}
			}
		});
		fieldGroup = new BeanFieldGroup<T>(this.clazz);
		fieldGroup.setFieldFactory(new CustomFieldFactory());
		rootLayout = new VerticalLayout();
		rootLayout.setWidth("100%");
		rootLayout.setHeightUndefined();//.setHeight("300px");
		rootLayout.setSpacing(true);
		filterSetupRow = new WrappingHorizontalPanel(4);
		rootLayout.addComponent(filterSetupRow);
//		formGridLayout = new GridLayout();
//		formGridLayout.setWidth("100%");
//		formGridLayout.setHeight("300px");
//		formGridLayout.setSpacing(true);
//		formGridLayout.setColumns(4);
//		formGridLayout.setRows(3);
//		formGridLayout.setRowExpandRatio(2, 1);
		
		tokenRow = new TokenRow() {
			@Override
			public void onTokenRemoved(Object id) {
				removeFiltro(id);
			}

			@Override
			public void onTokenAdded(Object id) {
			}
		};
		//mainLayout.addComponent(tokenRow);

		filtriSelezionabiliComboBox = new ComboBox("Campo filtro");
		filtriSelezionabiliComboBox.setRequired(true);
		filtriSelezionabiliComboBox.setRequiredError("Campo non compilato correttamente");
		filtriSelezionabiliComboBox.setNullSelectionAllowed(false);
		filtriSelezionabiliComboBox.setImmediate(true);
		for (FilterField filter : filterFields) {
			filtriSelezionabiliComboBox.addItem(filter.getPropertyId());
			String caption = filter.getCaption();
			if (caption == null) {
				caption = filter.getCaption();
			}
			if (caption == null) {
				caption = FormatUtils.humanizeString(filter.getPropertyId());
			}

			filtriSelezionabiliComboBox.setItemCaption(filter.getPropertyId(), caption);
			Class<? extends Field> preferredField = filter.getPreferredFieldClass();
			Field field = fieldGroup.buildAndBind("Valore filtro", filter.getPropertyId(), preferredField);
			field.setRequired(true);
			field.setRequiredError("Campo non compilato correttamente");

			mappaFields.put(filter.getPropertyId(), field);
		}
		filtriSelezionabiliComboBox.addValueChangeListener(new ValueChangeListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void valueChange(ValueChangeEvent event) {
//				formGridLayout.removeComponent(1, 0);
//				formGridLayout.removeComponent(2, 0);
//				formGridLayout.removeComponent(3, 0);
				clearFilterSetupRow();
				
				Object propertyId = filtriSelezionabiliComboBox.getValue();
				if (propertyId != null) {
					valoreFiltroField = mappaFields.get(propertyId);
					valoreFiltroField.setValue(null);
					tipoFiltroComboBox.removeAllItems();
					if (valoreFiltroField instanceof TextField) {
						if (isFieldNumerico()) {
							tipoFiltroComboBox.addItems(tipiFiltriDataNumero);
						} else {
							tipoFiltroComboBox.addItems(tipiFiltriTesto);
						}
					} else if (valoreFiltroField instanceof DateField) {
						tipoFiltroComboBox.addItems(tipiFiltriDataNumero);
					} else {
						tipoFiltroComboBox.addItems(tipiFiltriBoolean);
					}
					setTipiFiltroCaptions();
					filterSetupRow.addComponent(tipoFiltroComboBox);
//					formGridLayout.addComponent(tipoFiltroComboBox, 1, 0);
					if (valoreFiltroField != null) {
						filterSetupRow.addComponent(valoreFiltroField);
//						formGridLayout.addComponent(valoreFiltroField, 2, 0);
					}
					filterSetupRow.addComponent(aggiungiFiltroButton, Alignment.BOTTOM_LEFT, "63px");
//					formGridLayout.addComponent(aggiungiFiltroButton, 3, 0);
//					formGridLayout.setComponentAlignment(aggiungiFiltroButton, Alignment.BOTTOM_RIGHT);
				}
			}
		});

		tipoFiltroComboBox = new ComboBox("Tipo filtro");
		tipoFiltroComboBox.setRequired(true);
		tipoFiltroComboBox.setRequiredError("Campo non compilato correttamente");
		tipoFiltroComboBox.setImmediate(true);
		tipoFiltroComboBox.setNullSelectionAllowed(false);

		filterSetupRow.addComponent(filtriSelezionabiliComboBox);
		//formGridLayout.addComponent(filtriSelezionabiliComboBox, 0, 0);

		HorizontalLayout tabellaOverHorizontalLayout = new HorizontalLayout();
		tabellaOverHorizontalLayout.setWidth("100%");
		tabellaOverHorizontalLayout.setHeightUndefined();
		tabellaOverHorizontalLayout.addComponent(new Label("Filtri correnti"));

		filtriMenuBar = new MenuBar();
		filtriMenuBar.addStyleName(ValoTheme.MENUBAR_SMALL);
		filtriMenuBar.addStyleName(ValoTheme.MENUBAR_BORDERLESS);
		filtriMenuBar.addItem("Pulisci filtri", FontAwesome.TRASH_O, new Command() {
			@Override
			public void menuSelected(MenuItem selectedItem) {
				resetFiltri();
			}
		});
//		MenuItem impostazioniMenuItem = filtriMenuBar.addItem("", FontAwesome.COG, null);
//		impostazioniMenuItem.addItem(localizedStrings.pulisciFiltri, FontAwesome.ERASER, new Command() {
//			@Override
//			public void menuSelected(MenuItem selectedItem) {
//				rimuoviTuttiFiltri();
//			}
//		});
//		impostazioniMenuItem.addItem(localizedStrings.salvaFiltri, FontAwesome.SAVE, new Command() {
//			@Override
//			public void menuSelected(MenuItem selectedItem) {
//				// TODO
//				CustomNotifications.showWarning("Non implementato");
//			}
//		});
//		impostazioniMenuItem.addItem(localizedStrings.eliminaFiltri, FontAwesome.TRASH_O, new Command() {
//			@Override
//			public void menuSelected(MenuItem selectedItem) {
//				// TODO
//				CustomNotifications.showWarning("Non implementato");
//			}
//		});
//		MenuItem elencoFiltriSalvatiMenuItem = impostazioniMenuItem.addItem(localizedStrings.caricaFiltri, FontAwesome.TRASH_O, null);
//		// TODO ciclo filtri salvati
//		elencoFiltriSalvatiMenuItem.addItem("Filtro base test", new Command() {
//			@Override
//			public void menuSelected(MenuItem selectedItem) {
//				// TODO
//				CustomNotifications.showWarning("Non implementato");
//			}
//		});
		tabellaOverHorizontalLayout.addComponent(filtriMenuBar);
		tabellaOverHorizontalLayout.setComponentAlignment(filtriMenuBar, Alignment.TOP_RIGHT);

		rootLayout.addComponent(tabellaOverHorizontalLayout);
//		formGridLayout.addComponent(tabellaOverHorizontalLayout, 0, 1, 3, 1);

		filtriImpostatiTable = new Table();
		filtriImpostatiTable.setWidth("100%");
		filtriImpostatiTable.setHeight("185px");
		filtriImpostatiTable.setImmediate(true);
		filtriImpostatiTable.addContainerProperty("Campo filtro", String.class, "");
		filtriImpostatiTable.addContainerProperty("Tipo filtro", String.class, "");
		filtriImpostatiTable.addContainerProperty("Valore filtro", String.class, "");
		filtriImpostatiTable.addContainerProperty("Elimina", Button.class, "");

		rootLayout.addComponent(filtriImpostatiTable);
		rootLayout.setExpandRatio(filtriImpostatiTable, 1);
//		formGridLayout.addComponent(filtriImpostatiTable, 0, 2, 3, 2);
	}

	private boolean isFieldNumerico() {
		Converter converter = ((TextField) valoreFiltroField).getConverter();
		if (converter != null && AbstractStringToNumberConverter.class.isAssignableFrom(converter.getClass())) {
			return true;
		} else {
			return false;
		}
	}

	private void setTipiFiltroCaptions() {
		try {
			tipoFiltroComboBox.setItemCaption(TipoFiltro.CONTIENTE, "Contiene");
		} catch (Exception e) {
		}
		try {
			tipoFiltroComboBox.setItemCaption(TipoFiltro.DIVERSO, "Diverso");
		} catch (Exception e) {
		}
		try {
			tipoFiltroComboBox.setItemCaption(TipoFiltro.DIVERSO_STRING, "Diverso");
		} catch (Exception e) {
		}
		try {
			tipoFiltroComboBox.setItemCaption(TipoFiltro.MAGGIORE, "Maggiore");
		} catch (Exception e) {
		}
		try {
			tipoFiltroComboBox.setItemCaption(TipoFiltro.MAGGIORE_UGUALE, "Maggiore o uguale");
		} catch (Exception e) {
		}
		try {
			tipoFiltroComboBox.setItemCaption(TipoFiltro.MINORE, "Minore");
		} catch (Exception e) {
		}
		try {
			tipoFiltroComboBox.setItemCaption(TipoFiltro.MINORE_UGUALE, "Minore o uguale");
		} catch (Exception e) {
		}
		try {
			tipoFiltroComboBox.setItemCaption(TipoFiltro.NON_SELEZIONATO, "Non selezionato");
		} catch (Exception e) {
		}
		try {
			tipoFiltroComboBox.setItemCaption(TipoFiltro.SELEZIONATO, "Selezionato");
		} catch (Exception e) {
		}
		try {
			tipoFiltroComboBox.setItemCaption(TipoFiltro.UGUALE, "Uguale");
		} catch (Exception e) {
		}

	}

	public void aggiungiFiltroDaWidget() {
		String campoFiltro = (String) filtriSelezionabiliComboBox.getValue();
		// l'ID � la propriet� da filtrare
		String captionFiltro = (String) filtriSelezionabiliComboBox.getItemCaption(campoFiltro);
		TipoFiltro tipoFiltro = (TipoFiltro) tipoFiltroComboBox.getValue();
		// l'ID � l'enum del tipo filtro
		
		Object valoreFiltro = null;
		String valoreFiltroMostrare = "";
		Object valoreFiltroObject = null;
		
		valoreFiltroObject = valoreFiltroField.getValue();
		valoreFiltro = aggiustaValoreFiltro(valoreFiltroObject);
		valoreFiltroMostrare = valoreFiltroObject.toString();
		if (ComboBox.class.isAssignableFrom(valoreFiltroField.getClass())) {
			JPAContainer container = (JPAContainer) ((ComboBox) valoreFiltroField).getContainerDataSource();
			Class<?> entityClass = container.getEntityClass();
			String idName = ReflectionUtils.getFieldWithAnnotation(entityClass, Id.class).getName();
			campoFiltro += "." + idName;
			valoreFiltroMostrare = ((ComboBox) valoreFiltroField).getItemCaption(valoreFiltroObject);
		} 
//		else if (DateField.class.isAssignableFrom(valoreFiltroField.getClass())) {
//			valoreFiltroMostrare = FormatUtils.getDateFormatter(FormatUtils.DATE_FORMAT).format(((DateField) valoreFiltroField).getValue());
//		}
		aggiungiFiltro(campoFiltro, captionFiltro, tipoFiltro, valoreFiltro, valoreFiltroMostrare);
	}

	public void aggiungiFiltro(String campoFiltro, String captionFiltro, TipoFiltro tipoFiltro, Object valoreFiltro, String valoreFiltroMostrare) {
		
		Button eliminaFiltroButton;
		final Object filtroId;
		String stringTipoFiltro;
		if (campoFiltro != null && tipoFiltro != null && valoreFiltro != null && valoreFiltroMostrare != null) {

			final Filter filtro;
			if (tipoFiltro.equals(TipoFiltro.UGUALE)) {
				filtro = new Compare.Equal(campoFiltro, valoreFiltro);
			} else if (tipoFiltro.equals(TipoFiltro.SELEZIONATO)) {
				filtro = new Compare.Equal(campoFiltro, true);
			} else if (tipoFiltro.equals(TipoFiltro.NON_SELEZIONATO)) {
				filtro = new Compare.Equal(campoFiltro, false);
			} else if (tipoFiltro.equals(TipoFiltro.MINORE)) {
				filtro = new Compare.Less(campoFiltro, valoreFiltro);
			} else if (tipoFiltro.equals(TipoFiltro.MINORE_UGUALE)) {
				filtro = new Compare.LessOrEqual(campoFiltro, valoreFiltro);
			} else if (tipoFiltro.equals(TipoFiltro.MAGGIORE)) {
				filtro = new Compare.Greater(campoFiltro, valoreFiltro);
			} else if (tipoFiltro.equals(TipoFiltro.MAGGIORE_UGUALE)) {
				filtro = new Compare.GreaterOrEqual(campoFiltro, valoreFiltro);
			} else if (tipoFiltro.equals(TipoFiltro.DIVERSO_STRING)) {
				filtro = new Not(new Like(campoFiltro, "%" + valoreFiltro + "%"));
			} else if (tipoFiltro.equals(TipoFiltro.DIVERSO)) {
					filtro = new Not(new Compare.Equal(campoFiltro, valoreFiltro));
			} else if (tipoFiltro.equals(TipoFiltro.CONTIENTE)) {
				filtro = new Like(campoFiltro, "%" + valoreFiltro + "%");
			} else {
				filtro = null;
			}
			eliminaFiltroButton = new Button(FontAwesome.TRASH_O);
			eliminaFiltroButton.addStyleName(ValoTheme.BUTTON_DANGER);
			eliminaFiltroButton.addStyleName(ValoTheme.BUTTON_ICON_ONLY);

			stringTipoFiltro = getCaptionTipoFiltro(tipoFiltro);

			filtroId = filtriImpostatiTable.addItem(
					Arrays.asList(captionFiltro, stringTipoFiltro, valoreFiltroMostrare, eliminaFiltroButton).toArray(), null);

			filtriList.put(filtroId, filtro);
			
			eliminaFiltroButton.addClickListener(new ClickListener() {
				@Override
				public void buttonClick(ClickEvent event) {
					removeFiltro(filtroId);
				}
			});

			aggiungiFiltroContainer(filtro);
			tokenRow.addToken(filtroId, "["+captionFiltro + "] " + stringTipoFiltro.toLowerCase() + " [" + valoreFiltroMostrare+"]");

			filtriSelezionabiliComboBox.setValue(null);
			clearFilterSetupRow();
//			formGridLayout.removeComponent(1, 0);
//			formGridLayout.removeComponent(2, 0);
//			formGridLayout.removeComponent(3, 0);
		} else {
			CustomNotifications.showWarning("Filtro non valido");
		}
	}
	
	/**
	 * Aggiunge un filtro invisibile ma sempre presente sul container
	 */
	public void aggiungiFiltroStatico(Filter filtroStatico) {
		staticFilters.add(filtroStatico);
		aggiungiFiltroContainer(filtroStatico);
	}
	
	private void clearFilterSetupRow() {
		filterSetupRow.removeAllComponents();
		filterSetupRow.addComponent(filtriSelezionabiliComboBox);
	}

	protected void removeFiltro(Object filtroId) {
		filtriImpostatiTable.removeItem(filtroId);
		rimuoviFiltroContainer(filtriList.get(filtroId));
		tokenRow.removeToken(filtroId);
		filtriList.remove(filtroId);
	}

	private String getCaptionTipoFiltro(TipoFiltro tipoFiltro) {
		if (tipoFiltro.equals(TipoFiltro.UGUALE)) {
			return "Uguale";
		} else if (tipoFiltro.equals(TipoFiltro.SELEZIONATO)) {
			return "Selezionato";
		} else if (tipoFiltro.equals(TipoFiltro.NON_SELEZIONATO)) {
			return "Non selezionato";
		} else if (tipoFiltro.equals(TipoFiltro.MINORE)) {
			return "Minore";
		} else if (tipoFiltro.equals(TipoFiltro.MINORE_UGUALE)) {
			return "Minore o uguale";
		} else if (tipoFiltro.equals(TipoFiltro.MAGGIORE)) {
			return "Maggiore";
		} else if (tipoFiltro.equals(TipoFiltro.MAGGIORE_UGUALE)) {
			return "Maggiore o uguale";
		} else if (tipoFiltro.equals(TipoFiltro.DIVERSO)) {
			return "Diverso";
		} else if (tipoFiltro.equals(TipoFiltro.DIVERSO_STRING)) {
			return "Diverso";
		} else if (tipoFiltro.equals(TipoFiltro.CONTIENTE)) {
			return "Contiene";
		} else {
			return "";
		}
	}

	public Object aggiustaValoreFiltro(Object valoreFiltroObject) {
		if (valoreFiltroObject instanceof Date) {
			return FormatUtils.getDateFormatter(FormatUtils.SQL_DATE_FORMAT).format(valoreFiltroObject);
		} else {
			return valoreFiltroObject;
		}
	}

	private void aggiungiFiltroContainer(Filter filtro) {
		if (container != null) {
			((Filterable)container).addContainerFilter(filtro);
		}
	}

	private void rimuoviFiltroContainer(Filter filtro) {
		if (container != null) {
			((Filterable)container).removeContainerFilter(filtro);
			refreshContainer();
		}
	}

	private void rimuoviTuttiFiltri() {
		tokenRow.removeAllTokens();
		filtriList.clear();
		filtriImpostatiTable.removeAllItems();
		((Filterable)container).removeAllContainerFilters();
		((Sortable)container).sort(new Object[0], new boolean[0]);
	}

	public void toggleVisibility() {
//		formGridLayout.setVisible(!formGridLayout.isVisible());
		rootLayout.setVisible(!rootLayout.isVisible());
	}

	public boolean isVisible() {
//		return formGridLayout.isVisible();
		return rootLayout.isVisible();
	}

	public Component getComponent() {
//		return formGridLayout;
		return rootLayout;
	}
	
	public Component getTokenRow() {
		return tokenRow;
	}

	public Map<String, Field> getMappaFields() {
		return mappaFields;
	}
	
	private void addFilters(List<Filter> listaFiltri) {
		for (Filter filtro : listaFiltri) {
			((Filterable)container).addContainerFilter(filtro);
		}
	}
	
	private void addSorts(Map<Object, Boolean> sortMap) {
		Object[] sortProperty = new Object[sortMap.size()];
		boolean[] sortDirection = new boolean[sortMap.size()];
		int i = 0;
		for (Entry<Object, Boolean> sort : sortMap.entrySet()) {
			sortProperty[i] = sort.getKey();
			sortDirection[i] = sort.getValue();
			i++;
		}
		((Sortable)container).sort(sortProperty, sortDirection);
	}

	protected List<Filter> getStaticFilters() {
		return staticFilters;
	}

	protected Map<Object, Boolean> getStaticSort() {
		return staticSort;
	}
	
	public void refreshContainer() {
		if (container.getClass().isAssignableFrom(JPAContainer.class)) {
			((JPAContainer)container).discard();
			((JPAContainer)container).refresh();
		}
	}
	
	/**
	 * Pulisce tutti filtri/sorting e ri-applica quelli statici
	 */
	public void resetFiltri() {
		rimuoviTuttiFiltri();
		addFilters(staticFilters);
		addSorts(staticSort);
	}
	
	public Container getContainer() {
		return container;
	}

	public void setContainer(Container container) {
		this.container = container;
	}

}
