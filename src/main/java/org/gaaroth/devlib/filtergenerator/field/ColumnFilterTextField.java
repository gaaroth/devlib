package org.gaaroth.devlib.filtergenerator.field;

import org.gaaroth.devlib.filtergenerator.FilterGenerator;
import org.gaaroth.devlib.filtergenerator.FilterGenerator.TipoFiltro;

import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings({"serial", "rawtypes"})
public class ColumnFilterTextField extends HorizontalLayout {
	
	public ColumnFilterTextField(final FilterGenerator filterGenerator, final String propertyId, final String caption) {
		setSizeFull();

		final TextField field = new TextField();
		field.setCaption(null);
		field.setRequired(false);
		field.setWidth("100%");
		field.setHeight("22px");
		final Button buttonFilter = new Button(FontAwesome.FILTER);
		//final LabelButton buttonFilter = new LabelButton(FontAwesome.FILTER.getHtml());
		buttonFilter.setWidth("35px");
		buttonFilter.setHeight("22px");
//		buttonFilter.addStyleName(ValoTheme.BUTTON_PRIMARY);
		buttonFilter.addStyleName(ValoTheme.BUTTON_LINK);
		buttonFilter.addStyleName(ValoTheme.BUTTON_SMALL);
		buttonFilter.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent clickEvent) {
				if (field.getValue() != null && !field.getValue().isEmpty()) {
					filterGenerator.aggiungiFiltro(propertyId, caption, TipoFiltro.CONTIENTE, field.getValue(), field.getValue());
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
		//field.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		field.addStyleName(ValoTheme.TEXTFIELD_SMALL);
		
		addComponent(field);
		addComponent(buttonFilter);
		setExpandRatio(field, 1);
	}

}