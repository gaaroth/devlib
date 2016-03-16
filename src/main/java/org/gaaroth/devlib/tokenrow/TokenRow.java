package org.gaaroth.devlib.tokenrow;

import java.util.HashMap;
import java.util.Map;

import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public abstract class TokenRow extends Panel {
	
	private HorizontalLayout mainLayout;
	private Map<Object, Button> mappaToken = new HashMap<Object, Button>();
	
	public TokenRow() {
		setWidth("100%");
		setHeightUndefined();//setHeight("42px");
		mainLayout = new HorizontalLayout();
		setContent(mainLayout);
	}
	
	public void addToken(final Object id, String descrizione) {
		if (!mappaToken.containsKey(id)) {
			Button button = new Button(descrizione, FontAwesome.TIMES);
			button.setWidthUndefined();
			button.addStyleName(ValoTheme.BUTTON_TINY);
			button.addStyleName(ValoTheme.BUTTON_LINK);
			button.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
			button.addStyleName(ValoTheme.BUTTON_ICON_ALIGN_RIGHT);
			button.addClickListener(new ClickListener() {
				@Override
				public void buttonClick(ClickEvent event) {
					removeToken(id);
				}
			});
			mappaToken.put(id, button);
			mainLayout.addComponent(button,0);
			mainLayout.setComponentAlignment(button, Alignment.MIDDLE_LEFT);
			onTokenAdded(id);
		}
	}

	public void removeToken(Object id) {
		if (mappaToken.containsKey(id)) {
			mainLayout.removeComponent(mappaToken.get(id));
			mappaToken.remove(id);
			onTokenRemoved(id);
		}
	}
	
	public void removeAllTokens() {
		mainLayout.removeAllComponents();
	}

	public abstract void onTokenRemoved(Object id);
	
	public abstract void onTokenAdded(Object id);

}
