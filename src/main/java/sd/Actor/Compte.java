package sd.Actor;

public class Compte {
	private int somme;
	
	public Compte(int somme) {
		this.somme = somme;
	}
	
	public int getSomme() {
		return this.somme;
	}
	
	public void AjouterMontant(int montant) {
		this.somme = this.somme+montant;
	}
	
	public void RetraitMontant(int montant) {
		this.somme = this.somme-montant;
	}
}
