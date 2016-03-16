package org.gaaroth.devlib.mail;

public interface EmailObject {
	
	public String getMittente();
	
	public void setMittente(String mittente);
	
	public String getDestinatario();
	
	public void setDestinatario(String destinatario);
	
	public String getCopiaCarbone();
	
	public void setCopiaCarbone(String copiaCarbone);
	
	public String getCopiaCarboneNascosta();
	
	public void setCopiaCarboneNascosta(String copiaCarboneNascosta);
	
	public String getOggetto();
	
	public void setOggetto(String oggetto);
	
	public String getTesto();
	
	public void setTesto(String testo);

}
