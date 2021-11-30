package sd.Actor;

import java.sql.*;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.routing.BalancingPool;
import sd.Actor.BanqueActor.Message;
import akka.actor.ActorRef;


public class BanquierActor extends AbstractActor{

	private static ActorRef routerPersistance = null;
	
	public BanquierActor(ActorRef routerPersistance){
		if(BanquierActor.routerPersistance == null) {
			BanquierActor.routerPersistance = routerPersistance;
		}		
	}
	
	// Méthode servant à déterminer le comportement de l'acteur lorsqu'il reçoit un message
			@Override
			public Receive createReceive() {
				return receiveBuilder()
						.match(ClientActor.Ajout.class, message -> Ajout(message,getSender()))
						.match(ClientActor.Retrait.class, message -> Retrait(message,getSender()))
						.build();
			}
			

			private void Ajout(final ClientActor.Ajout message,ActorRef client) {
				message.compte.AjouterMontant(message.montantAjout);
				BanquierActor.routerPersistance.tell(new Enregistrement(message.compte), ActorRef.noSender());
				client.tell(message.compte,getSelf()); //On renvoie au client son compte mis a jour avec la nouvelle somme
			}

			private void Retrait(final ClientActor.Retrait message,ActorRef client) {
				int sommeCompte = message.compte.getSomme();	//On récupère la somme sur le compte
				if(sommeCompte>=message.montantRetrait) {	//Si il n'y a pas assez d'argent sur le compte on annule le retrait
					//Le banquier accepte le message de retrait du client
					message.compte.RetraitMontant(message.montantRetrait);
					BanquierActor.routerPersistance.tell(new Enregistrement(message.compte), client);
					client.tell(message.compte,getSelf());	//On renvoie au client son compte mis a jour avec la nouvelle somme				
				}else {
					System.out.println("Pas assez d'argent sur le compte pour un retrait d'une telle somme");
				}
				
			}
			
			
			// Méthode servant à la création d'un acteur
			public static Props props(ActorRef routerPersistance) {
				return Props.create(BanquierActor.class,routerPersistance);
			}
			

			// Définition des messages en inner classes
			public interface Message {}
				
			
			public static class Enregistrement implements Message {
				public Compte compte;
				
				public Enregistrement(Compte compte) {
					this.compte = compte;
				}
			}
	
}

