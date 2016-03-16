package org.gaaroth.devlib.broadcast;

import java.util.List;

public abstract class BroadcastListener {
	
	private List<String> listaIdGruppi;
	
	public List<String> getListaIdGruppi() {
		return listaIdGruppi;
	}

	public BroadcastListener(List<String> listaIdGruppi) {
		this.listaIdGruppi = listaIdGruppi;
	}
	
    public abstract void receiveBroadcast(String message);
}
