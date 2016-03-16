package org.gaaroth.devlib.converter;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Locale;

import org.gaaroth.devlib.utils.ReflectionUtils;
import org.vaadin.gridutil.converter.SimpleStringConverter;

@SuppressWarnings({ "serial", "rawtypes" })
public class ListConverter<C> extends SimpleStringConverter<List> {
	
	private String nomeCampoDaMostrare = "";
	private Class<C> classeLista;
	
	public ListConverter(Class<C> classeLista, String nomeCampoDaMostrare) {
		super(List.class);
		this.classeLista = classeLista;
		this.nomeCampoDaMostrare = nomeCampoDaMostrare;
	}

	@Override
	public String convertToPresentation(List value, Class<? extends String> targetType, Locale locale)
			throws com.vaadin.data.util.converter.Converter.ConversionException {
		String valueReturn = "";
		for (Object obj : value) {
			valueReturn += getCampoMostrare(obj)+"<br/>";
		}
		return valueReturn;
	}

	private String getCampoMostrare(Object obj) {
		try {
			String[] catenaCampi = nomeCampoDaMostrare.split("\\.");
			
			Field currentField = null;
			Object currentObject = obj;
			for(String campoCatena : catenaCampi) {
				currentField = ReflectionUtils.getField((currentField!=null?currentField.getType():classeLista), campoCatena);
				currentField.setAccessible(true);
				currentObject = currentField.get(currentObject);
			}
			return currentObject.toString();
		} catch (Exception e) {
			throw new ConversionException(e);
		}
	}
	
}
