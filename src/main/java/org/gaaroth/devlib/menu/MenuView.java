package org.gaaroth.devlib.menu;

import java.util.Iterator;
import java.util.List;

import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Page.Styles;
import com.vaadin.server.Resource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public abstract class MenuView extends /*VerticalLayout*/ CustomComponent {

	public static final String STYLE_VISIBLE = "valo-menu-visible";
	public static final String STYLE_SELECTED = "selected";
	public static final String STYLE_BADGEWRAPPER = "badgewrapper";
	public static final String STYLE_MENUITEMS = "valo-menuitems";
	public static final String STYLE_MENU_TOGGLE = "valo-menu-toggle";
	
	private CssLayout menuItemsLayout;
	
//	private MenuBar settings = null;
//	private MenuItem settingsItem;

	public CssLayout getMenuItemsLayout() {
		return menuItemsLayout;
	}

	/**
	 * TODO! Aggiunti queste righe nel CSS
	 * .valo-menu { border-right: none; .badgewrapper { position: relative; padding: 0 !important; } }
	 */
	public MenuView() {
		Styles styles = Page.getCurrent().getStyles();
        styles.add(".valo-menu { border-right: none; .badgewrapper { position: relative; padding: 0 !important; } }");

		addStyleName(ValoTheme.MENU_ROOT);
		//setSizeUndefined();
		setWidthUndefined();
		setHeight("100%");
		
		//1
		CssLayout menuContent = new CssLayout();
		menuContent.addStyleName(ValoTheme.MENU_PART);
		menuContent.setWidth(null);
		menuContent.setHeight("100%");
		
		menuContent.addComponent(buildTitle());
//		menuContent.addComponent(settings);
		menuContent.addComponent(buildToggleButton());
		menuContent.addComponent(buildMenuItems());
		
		setCompositionRoot(menuContent);
		
		//2		
//		CssLayout menuHeader = new CssLayout();
//		menuHeader.addStyleName(ValoTheme.MENU_PART);
//		menuHeader.setSizeFull();
//		menuHeader.addComponent(buildTitle());
//		menuHeader.addComponent(buildToggleButton());
//		
//		Panel menuContent = new Panel();
//		menuContent.addStyleName(ValoTheme.MENU_PART);
//		menuContent.setHeight("100%");
//		menuContent.setWidth(null);
//		menuContent.addStyleName(STYLE_MENUITEMS);
//		menuContent.addStyleName(ValoTheme.PANEL_BORDERLESS);
//		menuContent.setContent(buildMenuItems());
//		
//		CssLayout menuFooter = new CssLayout();
//		menuFooter.addStyleName(ValoTheme.MENU_PART);
//		menuFooter.setWidth("100%");
//		menuFooter.setHeightUndefined();
//		menuFooter.addComponent(buildFooter());
//		
//		addComponents(menuHeader, menuContent, menuFooter);
//		setExpandRatio(menuContent, 1);
//		setComponentAlignment(menuFooter, Alignment.BOTTOM_LEFT);
		
	}

	public abstract List<Component> getMenuItems();
	public abstract List<String> getTitle();

	private Component buildTitle() {
		VerticalLayout titleWrapper = new VerticalLayout();
		titleWrapper.addStyleName(ValoTheme.MENU_TITLE);
		Label title;
		for (String line : getTitle()) {
			title = new Label(line, ContentMode.HTML);
			title.setSizeUndefined();
			titleWrapper.addComponent(title);
			titleWrapper.setComponentAlignment(title, Alignment.MIDDLE_CENTER);
		}
		return titleWrapper;
	}
	
//	private void setUserMenu() {
//		settings = new MenuBar();
//		settings.addStyleName(ValoTheme.MENU_ITEM);
//		settings.addStyleName("user-menu");
//		final UtenteModel utente = WebAppUI.getCurrent().getAppConfig().getUtenteAutenticato();
//		settingsItem = settings.addItem("", new ThemeResource("img/profile-pic-300px.jpg"), null);
//		settingsItem.setText(utente.getUsername());
//		//settingsItem = settings.addItem(utente.getUsername(), null, null);
//		settingsItem.addItem("Profilo", new Command() {
//			@Override
//			public void menuSelected(MenuItem selectedItem) {
//				// ProfilePreferencesWindow.open(user, false);
//			}
//		});
//		settingsItem.addItem("Preferenze", new Command() {
//			@Override
//			public void menuSelected(MenuItem selectedItem) {
//				// ProfilePreferencesWindow.open(user, true);
//			}
//		});
//		settingsItem.addSeparator();
//		settingsItem.addItem("Logout", new Command() {
//			@Override
//			public void menuSelected(MenuItem selectedItem) {
//				WebAppUI.getCurrent().getAppConfig().getEventBus().post(new SessioneEvent(null));
//			}
//		});
//	}

	private Component buildToggleButton() {
		Button valoMenuToggleButton = new Button("Menu", new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				if (/*getCompositionRoot()*/MenuView.this.getStyleName().contains(STYLE_VISIBLE)) {
					/*getCompositionRoot()*/MenuView.this.removeStyleName(STYLE_VISIBLE);
				} else {
					/*getCompositionRoot()*/MenuView.this.addStyleName(STYLE_VISIBLE);
				}
			}
		});
		valoMenuToggleButton.setIcon(FontAwesome.LIST);
		valoMenuToggleButton.addStyleName(STYLE_MENU_TOGGLE);
		valoMenuToggleButton.addStyleName(ValoTheme.BUTTON_BORDERLESS);
		valoMenuToggleButton.addStyleName(ValoTheme.BUTTON_SMALL);
		return valoMenuToggleButton;
	}
	
//	private Component buildFooter() {
//		VerticalLayout verticalLayout = new VerticalLayout();
//		verticalLayout.addComponent(new Label("FOOTER"));
//		verticalLayout.setHeight("100px");
//		return verticalLayout;
//	}
	
	private Component buildMenuItems() {
		menuItemsLayout = new CssLayout();
		menuItemsLayout.addStyleName(STYLE_MENUITEMS);
		menuItemsLayout.setHeightUndefined();
		for (Component component : getMenuItems()) {
			menuItemsLayout.addComponent(component);
		}
		return menuItemsLayout;
	}
	
	private void clearMenuSelection() {
		for (Iterator<Component> it = menuItemsLayout.iterator(); it.hasNext();) {
			Component next = it.next();
			try {
				((MenuItemButton)next).getButton().removeStyleName(STYLE_SELECTED);
			} catch (Exception e) {}
		}
	}
	
	/**
	 * Classe per creare sottotitoli
	 */
	public class MenuItemSubtitle extends Label {
		
		public MenuItemSubtitle(String caption) {
			super(caption, ContentMode.HTML);
			setPrimaryStyleName(ValoTheme.MENU_SUBTITLE);
			addStyleName("h4");
			setSizeUndefined();
		}
		
	}

	/**
	 * Classe per creare pulsanti
	 */
	public abstract class MenuItemButton extends CssLayout {
		
		private Button button;
		private Label badgeLabel;

		public MenuItemButton(String caption, Resource icon) {
			button = new Button();
			button.setPrimaryStyleName(ValoTheme.MENU_ITEM);
			button.setIcon(icon);
			button.setCaption(caption);
			button.addClickListener(new ClickListener() {
				@Override
				public void buttonClick(final ClickEvent event) {
					clearMenuSelection();
					event.getButton().addStyleName(STYLE_SELECTED);
					if (/*getCompositionRoot()*/MenuView.this.getStyleName().contains(STYLE_VISIBLE)) {
						/*getCompositionRoot()*/MenuView.this.removeStyleName(STYLE_VISIBLE);
					} else {
						/*getCompositionRoot()*/MenuView.this.addStyleName(STYLE_VISIBLE);
					}
					onClick();
				}
			});
			addComponent(button);
			setStyleName(ValoTheme.MENU_ITEM);
			addStyleName(STYLE_BADGEWRAPPER);
			setWidth(100.0f, Unit.PERCENTAGE);
			badgeLabel = new Label();
			badgeLabel.addStyleName(ValoTheme.MENU_BADGE);
			badgeLabel.setWidthUndefined();
			badgeLabel.setVisible(false);
			addComponent(badgeLabel);
		}

		public Button getButton() {
			return button;
		}

		public Label getBadgeLabel() {
			return badgeLabel;
		}
		
		protected abstract void onClick();

	}

}
