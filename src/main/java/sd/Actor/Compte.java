package sd.Actor;

public class Compte {
	private int somme;
	private int id;
	
	public Compte(int somme,int id) {
		this.somme = somme;
		this.id = id;
	}
	
	public int getSomme() {
		return this.somme;
	}
	
	public int getId() {
		return this.id;
	}
	
	public void AjouterMontant(int montant) {
		this.somme = this.somme+montant;
	}
	
	public void RetraitMontant(int montant) {
		this.somme = this.somme-montant;
	}
}
