package org.gaaroth.devlib.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.vaadin.data.util.converter.Converter;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.FileResource;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.Tab;
import com.vaadin.ui.Table;

public class VaadinUtils {
	
	private static final Logger logger = LogManager.getLogger(VaadinUtils.class);
	
	public static void setGridColumns(Grid grid, List<String> properties) {
		for (String property : properties) {
			grid.addColumn(property);
			grid.getColumn(property).setHeaderCaption(FormatUtils.humanizeString(property));
		}
	}
	
	@SafeVarargs
	public static void setTableColumns(Table table, List<String>... properties) {
		List<String> propertyId = new ArrayList<String>();
		for (List<String> property : properties) {
			propertyId.add(property.get(0));
			table.setColumnHeader(property.get(0), property.get(1));
		}
		table.setVisibleColumns(propertyId.toArray());
	}
	
	public void setTableColumns(Table table, List<String> properties) {
		List<String> propertyId = new ArrayList<String>();
		String caption;
		for (String property : properties) {
			propertyId.add(property);
			caption = null;
			if (caption == null) {
				caption = FormatUtils.humanizeString(property);
			}
			table.setColumnHeader(property, caption);
		}
		table.setVisibleColumns(propertyId.toArray());
	}
	
	public static FileDownloader getFileDownloader(String filePath) {
		try {
			File file = new File(filePath);
			return getFileDownloader(file);
		} catch (Exception e) {
			logger.error("Errore creazione FileDownloader [path="+filePath+"]", e);
		}
		return null;
	}
	
	public static FileDownloader getFileDownloader(File file) {
		try {
			if (!file.exists()) {
				return null;
			}
			FileResource fr = new FileResource(file);
			FileDownloader fileDownloader = new FileDownloader(fr);
			return fileDownloader;
		} catch (Exception e) {
			logger.error("Errore creazione FileDownloader", e);
		}
		return null;
	}
	
	public static StreamResource getStreamResource(final ByteArrayOutputStream byteArrayOutputStream) {
		try {
			if (byteArrayOutputStream == null) {
				return null;
			}	
			@SuppressWarnings("serial")
			StreamSource streamSource = new StreamResource.StreamSource() {
				@Override
				public InputStream getStream() {
					return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
				}
			};			
			StreamResource streamResource = new StreamResource(streamSource, "inserimenti_prodotti.xls");
			return streamResource;
		} catch (Exception e) {
			logger.error("Errore creazione StreamResource", e);
		}
		return null;
	}
	
	@SuppressWarnings("serial")
	public static Converter<String, Double> getStandardDoubleConverter() {
		return new Converter<String, Double>(){
			@Override
			public Double convertToModel(String value, Class<? extends Double> targetType, Locale locale) throws ConversionException {
				Double valoreConvertito = null;
				try {
					valoreConvertito = Double.valueOf(value);
				} catch (Exception e) { }
				return valoreConvertito;
			}
			@Override
			public String convertToPresentation(Double value, Class<? extends String> targetType, Locale locale) throws ConversionException {
				if (value == null) {
					return "";
				} else {
					return value.toString();
				}
			}
			@Override
			public Class<Double> getModelType() {
				return Double.class;
			}
			@Override
			public Class<String> getPresentationType() {
				return String.class;
			}
		};
	}
	
	@SuppressWarnings("serial")
	public static Converter<String, Integer> getStandardIntegerConverter() {
		return new Converter<String, Integer>(){
			@Override
			public Integer convertToModel(String value, Class<? extends Integer> targetType, Locale locale) throws ConversionException {
				Integer valoreConvertito = null;
				try {
					valoreConvertito = Integer.valueOf(value);
				} catch (Exception e) { }
				return valoreConvertito;
			}
			@Override
			public String convertToPresentation(Integer value, Class<? extends String> targetType, Locale locale) throws ConversionException {
				if (value == null) {
					return "";
				} else {
					return value.toString();
				}
			}
			@Override
			public Class<Integer> getModelType() {
				return Integer.class;
			}
			@Override
			public Class<String> getPresentationType() {
				return String.class;
			}
		};
	}
	
	public static int getSelectedTabIndex(TabSheet tabSheet){
		Component selected_tab_id = tabSheet.getSelectedTab();
		Tab tab = tabSheet.getTab(selected_tab_id);
		return tabSheet.getTabPosition(tab);
	}
	
}
