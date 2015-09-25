package SecPass.logica;

/**
* @author Elanne Melilo de Souza 10101180
* @author Ricardo Maurici Ferreira 10201015
* Date 24/09/2015
*/

public class Tabela {
	private String chaveCifrada;
	private String valorCifrado;
	private String hmac;
	
	public String getChaveCifrada() {
		return chaveCifrada;
	}
	public void setChaveCifrada(String chaveCifrada) {
		this.chaveCifrada = chaveCifrada;
	}
	public String getValorCifrado() {
		return valorCifrado;
	}
	public void setValorCifrado(String valorCifrado) {
		this.valorCifrado = valorCifrado;
	}
	public String getHmac() {
		return hmac;
	}
	public void setHmac(String hmac) {
		this.hmac = hmac;
	}
}
