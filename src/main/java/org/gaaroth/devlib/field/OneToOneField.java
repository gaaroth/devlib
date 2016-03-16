package org.gaaroth.devlib.field;

import javax.persistence.EntityManager;

import org.gaaroth.devlib.utils.ReflectionUtils;

import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.addon.jpacontainer.fieldfactory.SingleSelectConverter;
import com.vaadin.shared.ui.combobox.FilteringMode;

import it.zero11.vaadin.asyncfiltercombobox.AsyncFilterComboBox;

@SuppressWarnings({"serial"})
public abstract class OneToOneField<T> extends AsyncFilterComboBox {

	public JPAContainer<T> container;
	
	public OneToOneField(Class<T> clazz, String descrizionePropertyId) {
		setFilterChangeTimeout(1000);
		setFilterChangeEventMode(FilterChangeEventMode.LAZY);
		setFilteringMode(FilteringMode.CONTAINS);
		container = JPAContainerFactory.makeReadOnly(clazz, getEntityManager());
		setConverter(new SingleSelectConverter<T>(this));
		setContainerDataSource(container);
		setImmediate(true);
		setItemCaptionMode(ItemCaptionMode.PROPERTY);
		
		if (descrizionePropertyId == null) {
			setItemCaptionPropertyId(ReflectionUtils.getDescrizionePropertyIdFromModel(clazz));
		} else {
			setItemCaptionPropertyId(descrizionePropertyId);
		}
	}
	
	public abstract EntityManager getEntityManager();

}
