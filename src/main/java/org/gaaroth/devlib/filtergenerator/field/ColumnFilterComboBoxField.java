package org.gaaroth.devlib.filtergenerator.field;

import java.util.Map;
import java.util.Map.Entry;

import org.gaaroth.devlib.filtergenerator.FilterGenerator;
import org.gaaroth.devlib.filtergenerator.FilterGenerator.TipoFiltro;

import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings({"serial", "rawtypes"})
public class ColumnFilterComboBoxField extends HorizontalLayout {
	
	public ColumnFilterComboBoxField(final FilterGenerator filterGenerator, final String propertyId, final String captionFiltro, Map<String, String> valori) {
		setSizeFull();

		final ComboBox field = new ComboBox();
		field.setWidth("100%");
		field.setHeight("22px");
		//field.setFilterChangeTimeout(1000);
		//field.setFilterChangeEventMode(FilterChangeEventMode.LAZY);
		field.setFilteringMode(FilteringMode.CONTAINS);
		//field.setConverter(new SingleSelectConverter<T>(field));
		for (Entry<String, String> entry : valori.entrySet()){
			Integer key = new Integer(entry.getKey());
			field.addItem(key);
			field.setItemCaption(key, entry.getValue());
		}
		field.setImmediate(true);
		field.setItemCaptionMode(ItemCaptionMode.EXPLICIT);
		
		final Button buttonFilter = new Button(FontAwesome.FILTER);
		buttonFilter.addStyleName(ValoTheme.BUTTON_LINK);
		buttonFilter.addStyleName(ValoTheme.BUTTON_SMALL);
		buttonFilter.setWidth("35px");
		buttonFilter.setHeight("22px");
		buttonFilter.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent clickEvent) {
				if (field.getValue() != null) {
					Object valoreFiltro = field.getValue();
					String valoreFiltroMostrare = field.getItemCaption(field.getValue());
					filterGenerator.aggiungiFiltro(propertyId, captionFiltro, TipoFiltro.UGUALE, valoreFiltro, valoreFiltroMostrare);
					field.clear();
				}
			}
		});
		field.addFocusListener(new FocusListener() {
			@Override
			public void focus(FocusEvent event) {
				buttonFilter.setClickShortcut(KeyCode.ENTER);
			}
		});
		field.addBlurListener(new BlurListener() {
			@Override
			public void blur(BlurEvent event) {
				buttonFilter.removeClickShortcut();
			}
		});
		field.addStyleName(ValoTheme.DATEFIELD_SMALL);
		
		addComponent(field);
		addComponent(buttonFilter);
		setExpandRatio(field, 1);
	}
}
