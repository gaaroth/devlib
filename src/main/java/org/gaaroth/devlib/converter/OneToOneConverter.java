package org.gaaroth.devlib.converter;

import java.lang.reflect.Field;
import java.util.Locale;

import org.gaaroth.devlib.utils.ReflectionUtils;
import org.vaadin.gridutil.converter.SimpleStringConverter;

@SuppressWarnings("serial")
public class OneToOneConverter<C> extends SimpleStringConverter<C> {

	private Class<C> type;
	private String nomeCampo;
	
	public OneToOneConverter(Class<C> type) {
		this(type, null);
	}
	
	public OneToOneConverter(Class<C> type, String nomeCampo) {
		super(type);
		this.type = type;
		this.nomeCampo = nomeCampo;
	}

	@Override
	public String convertToPresentation(C value, Class<? extends String> targetType, Locale locale)
			throws com.vaadin.data.util.converter.Converter.ConversionException {
		try {
			Field field;
			if (nomeCampo != null) {
				field = ReflectionUtils.getField(type, nomeCampo);
			} else {	
				field = ReflectionUtils.getFieldDescrizioneOrDenominazione(type);
			}
			field.setAccessible(true);
			return (String)field.get(value);
		} catch (Exception e) {
			return "";
		}
	}

}


