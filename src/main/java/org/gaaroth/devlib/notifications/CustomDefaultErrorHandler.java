package org.gaaroth.devlib.notifications;

import com.vaadin.server.DefaultErrorHandler;

@SuppressWarnings("serial")
public class CustomDefaultErrorHandler extends DefaultErrorHandler {
	
    @Override
    public void error(com.vaadin.server.ErrorEvent event) {
    	
    	String cause = ""; //event.getThrowable().getMessage();
        for (Throwable t = event.getThrowable(); t != null; t = t.getCause()) {
            if (t.getCause() == null) {
                cause = t.getMessage();//getClass().getName();
            }
        }
        CustomNotifications.showError(cause);
        //doDefault(event);
    } 
    
}
