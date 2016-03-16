package org.gaaroth.devlib.notifications;

import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.dialogs.ConfirmDialog.Listener;

import com.vaadin.ui.UI;

public class CustomDialogs {
	
	public static String CONFERMA = "CONFERMA";
	public static String CONFERMA_BUTTON = "Conferma";
	public static String ANNULLA_BUTTON = "Annulla";
	
	public static void showConfirm(String testo, String confirmButtonCaption, String cancelButtonCaption, Listener listener) {
		ConfirmDialog.show(UI.getCurrent(), CONFERMA, testo, confirmButtonCaption, cancelButtonCaption, listener);
	}
	
	public static void showConfirm(String testo, Listener listener) {
		ConfirmDialog.show(UI.getCurrent(), CONFERMA, testo, CONFERMA_BUTTON, ANNULLA_BUTTON, listener);
	}

}
