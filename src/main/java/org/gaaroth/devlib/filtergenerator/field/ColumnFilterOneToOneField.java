package org.gaaroth.devlib.filtergenerator.field;

import javax.persistence.EntityManager;

import org.gaaroth.devlib.filtergenerator.FilterGenerator;
import org.gaaroth.devlib.filtergenerator.FilterGenerator.TipoFiltro;
import org.gaaroth.devlib.utils.ReflectionUtils;

import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.addon.jpacontainer.fieldfactory.SingleSelectConverter;
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
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.themes.ValoTheme;

import it.zero11.vaadin.asyncfiltercombobox.AsyncFilterComboBox;
import it.zero11.vaadin.asyncfiltercombobox.AsyncFilterComboBox.FilterChangeEventMode;

@SuppressWarnings({"serial", "rawtypes"})
public abstract class ColumnFilterOneToOneField<T> extends HorizontalLayout {
	
	public ColumnFilterOneToOneField(final FilterGenerator filterGenerator, final String propertyId, final String caption, Class<T> clazz) {
		this(filterGenerator, propertyId, caption, clazz, null, null);
	}
	
	public ColumnFilterOneToOneField(final FilterGenerator filterGenerator, final String propertyId, final String caption,
		Class<T> clazz, String descrizionePropertyId) {
		this(filterGenerator, propertyId, caption, clazz, descrizionePropertyId, null);
	}
	
	public ColumnFilterOneToOneField(final FilterGenerator filterGenerator, final String propertyId, final String caption,
			Class<T> clazz, String descrizionePropertyId, JPAContainer<T> container) {
		setSizeFull();

		final AsyncFilterComboBox field = new AsyncFilterComboBox();
		field.setWidth("100%");
		field.setHeight("22px");
		field.setFilterChangeTimeout(1000);
		field.setFilterChangeEventMode(FilterChangeEventMode.LAZY);
		field.setFilteringMode(FilteringMode.CONTAINS);
		field.setConverter(new SingleSelectConverter<T>(field));
		if (container != null) {
			field.setContainerDataSource(container);
		} else {
			field.setContainerDataSource(JPAContainerFactory.makeReadOnly(clazz, getEntityManager()));
		}
		field.setImmediate(true);
		field.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		
		if (descrizionePropertyId == null) {
			field.setItemCaptionPropertyId(ReflectionUtils.getDescrizionePropertyIdFromModel(clazz));
		} else {
			field.setItemCaptionPropertyId(descrizionePropertyId);
		}
		
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
					filterGenerator.aggiungiFiltro(propertyId, caption, TipoFiltro.UGUALE, valoreFiltro, valoreFiltroMostrare);
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
	
	public abstract EntityManager getEntityManager();
}
