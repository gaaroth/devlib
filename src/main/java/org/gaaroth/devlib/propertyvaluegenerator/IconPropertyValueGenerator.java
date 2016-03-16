package org.gaaroth.devlib.propertyvaluegenerator;

import com.vaadin.data.Item;
import com.vaadin.data.util.PropertyValueGenerator;
import com.vaadin.server.FontIcon;

@SuppressWarnings("serial")
public final class IconPropertyValueGenerator extends PropertyValueGenerator<String> {
	   
    private final FontIcon fontIcon;
   
    public IconPropertyValueGenerator(FontIcon icon) {
        this.fontIcon = icon;
    }
   
    @Override
    public String getValue(Item item, Object itemId, Object propertyId) {
        return fontIcon.getHtml();
    }
    @Override
    public Class<String> getType() {
        return String.class;
    }
}
