package sd.Actor;

import java.util.ArrayList;
import java.util.HashMap;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.actor.ActorRef;
import java.sql.*;


public class BanqueActor extends AbstractActor {

	private Compte compte;
	private ArrayList<ActorRef> banquierListe;
	private HashMap<Integer,ActorRef> clientConnecterListe;
	
	public BanqueActor() {
		this.compte = new Compte(1500);
		this.banquierListe = new ArrayList<>();
		this.banquierListe.add(getContext().actorOf(BanquierActor.props(), "banquier1"));
		this.banquierListe.add(getContext().actorOf(BanquierActor.props(), "banquier2"));
		this.clientConnecterListe = new HashMap<>();
	}

	// Méthode servant à déterminer le comportement de l'acteur lorsqu'il reçoit un message
		@Override
		public Receive createReceive() {
			return receiveBuilder()
					.match(ClientActor.Connexion.class, message -> Connexion(getSender()))
					.match(ClientActor.Ajout.class, message -> Ajout(message))
					.match(ClientActor.Retrait.class, message -> Retrait(message))
					.match(BanquierActor.AjoutBanquier.class, message -> AjoutBanquier(message,getSender()))
					.match(BanquierActor.RetraitBanquier.class, message -> RetraitBanquier(message,getSender()))
					.build();
		}
		
		private void Connexion(ActorRef client) {
			client.tell(this.compte, getSelf());
		}
		
		private void Ajout(final ClientActor.Ajout message) {
			
			this.banquierListe.get(0).forward(message, getContext());
		}

		private void Retrait(final ClientActor.Retrait message) {
			this.banquierListe.get(0).forward(message, getContext());
		}
		
		private void AjoutBanquier(final BanquierActor.AjoutBanquier message,ActorRef client) {
			this.compte.AjouterMontant(message.montantAjout);
			client.tell(this.compte,getSelf()); //On renvoie au client son compte mis a jour avec la nouvelle somme
		}

		private void RetraitBanquier(final BanquierActor.RetraitBanquier message,ActorRef client) {
			this.compte.RetraitMontant(message.montantRetrait);
			client.tell(this.compte,getSelf());	//On renvoie au client son compte mis a jour avec la nouvelle somme
		}
		
		
		// Méthode servant à la création d'un acteur
		public static Props props() {
			return Props.create(BanqueActor.class);
		} 
		
		
		// Définition des messages en inner classes
		public interface Message {}
		
	
	
}
