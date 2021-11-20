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
				getContext().parent().tell(new AjoutBanquier(message.montantAjout), getContext().parent());
			}

			private void Retrait(final ClientActor.Retrait message) {
				int sommeCompte = message.compte.getSomme();
				if(sommeCompte>=message.montantRetrait) {
					getContext().parent().tell(new RetraitBanquier(message.montantRetrait), getContext().parent());
				}else {
					//getSender().tell(new SommeInssufisante(), getSender());
					System.out.println("Pas assez d'argent sur le compte pour un retrait d'une tel somme");
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
				public final int montantAjout;

				public AjoutBanquier(int montantAjout) {
					this.montantAjout = montantAjout;
				}
			}	
			
			public static class RetraitBanquier implements Message {
				public final int montantRetrait;

				public RetraitBanquier(int montantRetrait) {
					this.montantRetrait = montantRetrait;
				}
			}
	
}

