package pojos;

public class Messages {
	private byte[] contenu;
	private Personne expediteur;
	public Messages(byte[] contenu, Personne expediteur) {
		super();
		this.contenu = contenu;
		this.expediteur = expediteur;
	}
	public byte[] getContenu() {
		return contenu;
	}
	public Personne getExpediteur() {
		return expediteur;
	}
}
