package org.gaaroth.devlib.filtergenerator.field;

import java.util.Date;

import org.gaaroth.devlib.filtergenerator.FilterGenerator;
import org.gaaroth.devlib.filtergenerator.FilterGenerator.TipoFiltro;
import org.gaaroth.devlib.utils.FormatUtils;
import org.joda.time.DateTime;

import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings({"serial", "rawtypes"})
public class ColumnFilterDateToDateTimeField extends HorizontalLayout {
	
	private DateField field;
	
	public ColumnFilterDateToDateTimeField(final FilterGenerator filterGenerator, final String propertyId, final String caption) {
		setSizeFull();
		field = new DateField();
		field.setCaption(null);
		field.setSizeFull();
		field.setWidth("100%");
		field.setHeight("22px");
		showTime(false);
		final Button buttonFilter = new Button(FontAwesome.FILTER);
//		buttonFilter.addStyleName(ValoTheme.BUTTON_PRIMARY);
		buttonFilter.addStyleName(ValoTheme.BUTTON_LINK);
		buttonFilter.addStyleName(ValoTheme.BUTTON_SMALL);
		buttonFilter.setWidth("35px");
		buttonFilter.setHeight("22px");
		buttonFilter.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent clickEvent) {
				if (field.getValue() != null) {
					//filterGenerator.aggiustaValoreFiltro() + TipoFiltro.UGUALE
					DateTime dateFiltro = new DateTime((Date)field.getValue());
					
					Object valoreFiltro = null;
					String valoreFiltroMostrare = null;
					
					valoreFiltro = dateFiltro.withTime(0, 0, 0, 0).toDate();
					valoreFiltroMostrare = FormatUtils.getDateFormatter(FormatUtils.DATETIME_FORMAT).format(valoreFiltro);
					filterGenerator.aggiungiFiltro(propertyId, caption, TipoFiltro.MAGGIORE_UGUALE, valoreFiltro, valoreFiltroMostrare);
					
					valoreFiltro = dateFiltro.plusDays(1).toDate();
					valoreFiltroMostrare = FormatUtils.getDateFormatter(FormatUtils.DATE_FORMAT).format(valoreFiltro);
					filterGenerator.aggiungiFiltro(propertyId, caption, TipoFiltro.MINORE, valoreFiltro, valoreFiltroMostrare);
					
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
		//field.addStyleName(ValoTheme.DATEFIELD_BORDERLESS);
		field.addStyleName(ValoTheme.DATEFIELD_SMALL);
		
		addComponent(field);
		addComponent(buttonFilter);
		setExpandRatio(field, 1);
	}
	
	public void showTime(boolean showTime) {
		if (showTime) {
			field.setDateFormat("dd/MM/yyyy HH:mm");
			field.setResolution(Resolution.MINUTE);
		} else {
			field.setDateFormat("dd/MM/yyyy");
			field.setResolution(Resolution.DAY);
		}
	}

}
