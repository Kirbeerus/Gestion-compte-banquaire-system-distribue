package sd.Actor;

public class Compte {
	private int somme;
	private int id;
	private int banquier;
	
	public Compte(int somme,int id,int banquier) {
		this.somme = somme;
		this.id = id;
		this.banquier = banquier;
	}
	
	public int getSomme() {
		return this.somme;
	}
	
	public int getBanquier() {
		return this.banquier;
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
