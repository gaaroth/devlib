package org.gaaroth.devlib.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.Id;

public class ReflectionUtils {
	
	/**CONTROLLA SE COLLECTION (List, Set)*/
	public static boolean isStandardType(Class<?> dataType) {
		return isCollection(dataType) || isNumeric(dataType) || isBoolean(dataType) 
				|| isText(dataType) || isDate(dataType) || isChar(dataType);
	}
	
	public static boolean isCollection(Class<?> dataType) {
		return Collection.class.isAssignableFrom(dataType);
	}
	
	public static boolean isNumeric(Class<?> dataType) {
		return isBigInteger(dataType) || isBigDecimal(dataType) || isDouble(dataType) ||isByte(dataType)
				|| isFloat(dataType) || isInteger(dataType) || isLong(dataType) || isShort(dataType);
	}
		
	public static boolean isBigDecimal(Class<?> dataType) {
		return BigDecimal.class.isAssignableFrom(dataType);
	}
	
	public static boolean isBigInteger(Class<?> dataType) {
		return BigInteger.class.isAssignableFrom(dataType);
	}
	
	public static boolean isByte(Class<?> dataType) {
		return Byte.class.isAssignableFrom(dataType) || byte.class.isAssignableFrom(dataType);
	}
	
	public static boolean isShort(Class<?> dataType) {
		return Short.class.isAssignableFrom(dataType) || short.class.isAssignableFrom(dataType);
	}
	
	public static boolean isInteger(Class<?> dataType) {
		return Integer.class.isAssignableFrom(dataType) || int.class.isAssignableFrom(dataType);
	}
	
	public static boolean isLong(Class<?> dataType) {
		return Long.class.isAssignableFrom(dataType) || long.class.isAssignableFrom(dataType);
	}
	
	public static boolean isFloat(Class<?> dataType) {
		return Float.class.isAssignableFrom(dataType) || float.class.isAssignableFrom(dataType);
	}
	
	public static boolean isDouble(Class<?> dataType) {
		return Double.class.isAssignableFrom(dataType) || double.class.isAssignableFrom(dataType);
	}
	
	public static boolean isBoolean(Class<?> dataType) {
		return Boolean.class.isAssignableFrom(dataType) || boolean.class.isAssignableFrom(dataType);
	}
	
	public static boolean isDate(Class<?> dataType) {
		return Date.class.isAssignableFrom(dataType);
	}
	
	public static boolean isText(Class<?> dataType) {
		return String.class.isAssignableFrom(dataType);
	}
	
	public static boolean isChar(Class<?> dataType) {
		return char.class.isAssignableFrom(dataType);
	}
	
	
	public static boolean classHasAnnotation(Class<?> classToCheck, Class<? extends Annotation> annotationClass) {
		if (classToCheck != null && annotationClass != null) {
			Annotation annotation = classToCheck.getAnnotation(annotationClass);
			return annotation != null;
		}
		return false;
	}
	
	public static boolean fieldHasAnnotation(Field fieldToCheck, Class<? extends Annotation> annotationClass) {
		if (fieldToCheck != null && annotationClass != null) {
			Annotation annotation = fieldToCheck.getAnnotation(annotationClass);
			return annotation != null;
		}
		return false;
	}
	
	public static Field getFieldWithAnnotation(Class<?> objectClass, Class<? extends Annotation> annotationClass) {
		if (objectClass != null && annotationClass != null) {
			Field[] fields = objectClass.getDeclaredFields();
			for (Field field : fields) {
				if (fieldHasAnnotation(field, annotationClass)) {
					field.setAccessible(true);
					return field;
				}
			}
		}
		return null;
	}
	
	public static Field getFieldDescrizioneOrDenominazione(Class<?> objectClass) {
		if (objectClass != null) {
			Field[] fields = objectClass.getDeclaredFields();
			for (Field field : fields) {
				if (field.getName().equals("descrizione") || field.getName().equals("denominazione")) {
					field.setAccessible(true);
					return field;
				}
			}
		}
		return null;
	}
	
	public static Field getField(Class<?> objectClass, String fieldName) {
		if (objectClass != null) {
			Field[] fields = objectClass.getDeclaredFields();
			for (Field field : fields) {
				if (field.getName().equals(fieldName)) {
					field.setAccessible(true);
					return field;
				}
			}
		}
		return null;
	}
	
	public static Object getNomeFieldId(Class<?> objectClass, Object object) {
		if (objectClass != null) {
			Field fieldId = getFieldWithAnnotation(objectClass, Id.class);
			fieldId.setAccessible(true);
			try {
				return fieldId.getName();
			} catch (Exception e) {
				throw new RuntimeException("Errore recupero nome field ID");
			}
		}
		return null;
	}
	
	public static Object getValoreFieldId(Class<?> objectClass, Object object) {
		if (objectClass != null) {
			Field fieldId = getFieldWithAnnotation(objectClass, Id.class);
			fieldId.setAccessible(true);
			try {
				return fieldId.get(object);
			} catch (Exception e) {
				throw new RuntimeException("Errore recupero valore field ID");
			}
		}
		return null;
	}
	
	public static List<Field> getAllFieldsOfType(Class<?> objectClass, Class<?> typeClass) {
		List<Field> list = new ArrayList<Field>();
		if (objectClass != null) {
			Field[] fields = objectClass.getDeclaredFields();
			for (Field field : fields) {
				if (field.getType().equals(typeClass)) {
					field.setAccessible(true);
					list.add(field);
				}
			}
		}
		return list;
	}
	
	public static List<String> getAllPropertiesOfType(Class<?> objectClass, Class<?> typeClass) {
		List<String> list = new ArrayList<String>();
		List<Field> listField = getAllFieldsOfType(objectClass, typeClass);
		for (Field field : listField) {
			list.add(field.getName());
		}
		return list;
	}
	
	@SuppressWarnings("rawtypes")
	public static String getDescrizionePropertyIdFromModel(Class clazz) {
		String descrizionePropertyId = "";
		//casi perticolari per tipologia di classe
		java.lang.reflect.Field reflectionField;
		//controllo se c'è un field che si chiama "descrizione" o "denominazione"
		reflectionField = ReflectionUtils.getFieldDescrizioneOrDenominazione(clazz);
		if (reflectionField != null) {
			descrizionePropertyId = reflectionField.getName();
		} else {
		//in caso contrario uso il field ID
			reflectionField = ReflectionUtils.getFieldWithAnnotation(clazz, Id.class);
			descrizionePropertyId = reflectionField.getName();
		}
		return descrizionePropertyId;
	}

//	public static String getCaption(Class<?> objectClass, String property) {
//		if (objectClass != null) {
//			Field[] fields = objectClass.getDeclaredFields();
//			for (Field field : fields) {
//				if (field.getName().equals(property)) {
//					return 
//				}
//			}
//		}
//		return null;
//	}

}
