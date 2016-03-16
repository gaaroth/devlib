package org.gaaroth.devlib.notifications;

import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;

public class CustomNotifications {
	
	public static String ERROR = "ERRORE";
	public static String INFO = "INFO";
	public static String WARNING = "ATTENZIONE";
	
	public static void showHeavyInfo(String title, String message) {
		showHeavyInfo(title, message, Position.MIDDLE_CENTER);
	}
	
	public static void showInfo(String message) {
		showInfo(INFO, message, Position.MIDDLE_CENTER);
	}
	
	public static void showLightInfo(String message) {
		showLighInfo(INFO, message, Position.BOTTOM_CENTER);
	}
	
	public static void showError(String message) {
		showError(ERROR, message, Position.MIDDLE_CENTER);
	}
	
	public static void showLightError(String message) {
		showLightError(ERROR, message, Position.BOTTOM_CENTER);
	}
	
	public static void showWarning(String message) {
		showWarning(WARNING, message, Position.MIDDLE_CENTER);
	}
	
	public static void showLightWarning(String message) {
		showLightWarning(WARNING, message, Position.BOTTOM_CENTER);
	}
	
	public static void showHeavyInfo(String title, String message, Position position) {
		Notification notification = new Notification(title);
		notification.setDescription("<span>"+message+"</span>");
		notification.setDelayMsec(-1);
		notification.setHtmlContentAllowed(true);
		notification.setStyleName("tray dark small closable"); // login-help
		notification.setPosition(position);
		notification.show(Page.getCurrent());
	}
	
	public static void showInfo(String title, String message, Position position) {
		Notification notification = new Notification(title);
		notification.setDescription("<span>"+message+"</span>");
		notification.setDelayMsec(3000);
		notification.setHtmlContentAllowed(true);
		notification.setStyleName("tray");
		notification.setPosition(position);
		notification.show(Page.getCurrent());
	}
	
	public static void showLighInfo(String title, String message, Position position) {
		Notification notification = new Notification(title, Type.TRAY_NOTIFICATION);
		notification.setDescription("<span>"+message+"</span>");
		notification.setDelayMsec(2000);
		notification.setHtmlContentAllowed(true);
		notification.setStyleName("tray");
		notification.setPosition(position);
		notification.show(Page.getCurrent());
	}
	
	public static void showError(String title, String message, Position position) {
		Notification notification = new Notification(title);
		notification.setDescription("<span>"+message+"</span>");
		notification.setDelayMsec(-1);
		notification.setHtmlContentAllowed(true);
		notification.setStyleName("error closable");
		notification.setPosition(position);
		notification.show(Page.getCurrent());
	}
	
	public static void showLightError(String title, String message, Position position) {
		Notification notification = new Notification(title);
		notification.setDescription("<span>"+message+"</span>");
		notification.setDelayMsec(2000);
		notification.setHtmlContentAllowed(true);
		notification.setStyleName("error");//closable
		notification.setPosition(position);
		notification.show(Page.getCurrent());
	}
	
	public static void showWarning(String title, String message, Position position) {
		Notification notification = new Notification(title, Type.WARNING_MESSAGE);
		notification.setDescription("<span>"+message+"</span>");
		notification.setDelayMsec(-1);
		notification.setHtmlContentAllowed(true);
		notification.setStyleName("warning closable");
		notification.setPosition(position);
		notification.show(Page.getCurrent());
	}
	
	public static void showLightWarning(String title, String message, Position position) {
		Notification notification = new Notification(title, Type.WARNING_MESSAGE);
		notification.setDescription("<span>"+message+"</span>");
		notification.setDelayMsec(2000);
		notification.setHtmlContentAllowed(true);
		notification.setStyleName("warning");
		notification.setPosition(position);
		notification.show(Page.getCurrent());
	}

}
