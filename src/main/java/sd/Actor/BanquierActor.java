package sd.Actor;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.actor.ActorRef;


public class BanquierActor extends AbstractActor{

	/*ActorRef banque;
	
	public BanquierActor(ActorRef banque) {
		this.banque = banque;
	}*/
	
	// Méthode servant à déterminer le comportement de l'acteur lorsqu'il reçoit un message
			@Override
			public Receive createReceive() {
				return receiveBuilder()
						.match(ClientActor.Ajout.class, message -> Ajout(message))
						.match(ClientActor.Retrait.class, message -> Retrait(message))
						.build();
			}
			

			private void Ajout(final ClientActor.Ajout message) {
				//Le banquier accepte le message d'ajout du client et  dit à la banque d'ajouter l'argent sur le compte
				getContext().parent().forward(new AjoutBanquier(message.montantAjout,message.compte), getContext()); 
			}

			private void Retrait(final ClientActor.Retrait message) {
				int sommeCompte = message.compte.getSomme();	//On récupère la somme sur le compte
				if(sommeCompte>=message.montantRetrait) {	//Si il n'y a pas assez d'argent sur le compte on annule le retrait
					//Le banquier accepte le message de retrait du client et  dit à la banque de retirer l'argent du compte
					getContext().parent().forward(new RetraitBanquier(message.montantRetrait), getContext()); 				
				}else {
					System.out.println("Pas assez d'argent sur le compte pour un retrait d'une telle somme");
				}
				
			}
			
			
			// Méthode servant à la création d'un acteur
			public static Props props() {
				return Props.create(BanquierActor.class);
			}
			

			// Définition des messages en inner classes
			public interface Message {}
			
			public static class SommeInssufisante implements Message {
				public String message;
				
				public SommeInssufisante() {
					this.message = "Pas assez d'argent sur le compte pour un retrait d'une tel somme";
				}
			}	
			
			public static class AjoutBanquier implements Message {
				public int montantAjout;
				public Compte compte;

				public AjoutBanquier(int montantAjout,Compte compte) {
					this.montantAjout = montantAjout;
					this.compte = compte;
				}
			}	
			
			public static class RetraitBanquier implements Message {
				public final int montantRetrait;

				public RetraitBanquier(int montantRetrait) {
					this.montantRetrait = montantRetrait;
				}
			}
	
}

