package org.gaaroth.devlib.broadcast;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Listener con lista null -> Listener generale
 * Broadcast con lista nulla -> Broadcast generale
 * Negli altri casi -> Broadcast solo ai gruppi definiti
 */
public class Broadcaster implements Serializable {

	private static final long serialVersionUID = 1L;
	
	static ExecutorService executorService = Executors.newSingleThreadExecutor();
    
    private static LinkedList<BroadcastListener> listeners = new LinkedList<BroadcastListener>();
    
    public static synchronized void register(BroadcastListener listener) {
        listeners.add(listener);
    }
    
    public static synchronized void unregister(BroadcastListener listener) {
        listeners.remove(listener);
    }
    
    public static synchronized void broadcast(final String message, final List<String> listaIdGruppi) {
        for (final BroadcastListener listener: listeners) {
        	if (listenerAppartieneAiGruppi(listener, listaIdGruppi)) {
	            executorService.execute(new Runnable() {
	                @Override
	                public void run() {
	                    listener.receiveBroadcast(message);
	                }
	            });
        	}
        }
    }

	private static boolean listenerAppartieneAiGruppi(BroadcastListener listener, List<String> listaIdGruppi) {
		if (listaIdGruppi == null) {
			return true;
		}
		if (listener.getListaIdGruppi() == null) {
			return false;
		}
		for (String idGruppo : listener.getListaIdGruppi()) {
			for (String idGruppoCheck : listaIdGruppi) {
				if (idGruppo.equals(idGruppoCheck)) {
					return true;
				}
			}
		}
		return false;
	}
}
