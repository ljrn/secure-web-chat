package pojos;

public class Messages {
	private String contenu;
	private Personne expediteur;
	public Messages(String contenu, Personne expediteur) {
		super();
		this.contenu = contenu;
		this.expediteur = expediteur;
	}
	public String getContenu() {
		return contenu;
	}
	public Personne getExpediteur() {
		return expediteur;
	}
	
	
}
