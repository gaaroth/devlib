package org.gaaroth.devlib.propertyvaluegenerator;

import com.vaadin.data.Item;
import com.vaadin.data.util.PropertyValueGenerator;

/**
 * Serve per colonne multiple dello stesso field.<br/>
 * Aggiungere al container pi� propriet� con id = id + "|1" etc etc <br/>
 * La classe poi rimuove il numero. Al momento gestisce solo 1 livello di entity.
 */
@SuppressWarnings("serial")
public class PlainPropertyValueGenerator<C> extends PropertyValueGenerator<C> {
	
	private Class<C> clazz;
	
	public PlainPropertyValueGenerator(Class<C> clazz) {
		this.clazz = clazz;
		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public C getValue(Item item, Object itemId, Object propertyId) {
		//TODO gestire sottocampi
		String[] campo = ((String)propertyId).split("\\|");
		return (C) item.getItemProperty(campo[0]).getValue();
	}

	@Override
	public Class<C> getType() {
		return clazz;
	}

}
