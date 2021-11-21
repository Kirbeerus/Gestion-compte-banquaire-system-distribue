package sd.Actor;

import java.util.ArrayList;
import java.util.HashMap;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.actor.ActorRef;


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
					.match(ClientActor.Connexion.class, message -> Connexion())
					.match(ClientActor.Ajout.class, message -> Ajout(message))
					.match(ClientActor.Retrait.class, message -> Retrait(message))
					.match(BanquierActor.AjoutBanquier.class, message -> AjoutBanquier(message))
					.match(BanquierActor.RetraitBanquier.class, message -> RetraitBanquier(message))
					.build();
		}
		
		private void Connexion() {
			//getContext().getSender().tell(message, getContext());
		}
		
		private void Ajout(final ClientActor.Ajout message) {
			
			this.banquierListe.get(0).forward(message, getContext());
		}

		private void Retrait(final ClientActor.Retrait message) {
			this.banquierListe.get(0).forward(message, getContext());
		}
		
		private void AjoutBanquier(final BanquierActor.AjoutBanquier message) {
			this.compte.AjouterMontant(message.montantAjout);
			System.out.println(this.compte.getSomme());
		}

		private void RetraitBanquier(final BanquierActor.RetraitBanquier message) {
			this.compte.RetraitMontant(message.montantRetrait);
			System.out.println(this.compte.getSomme());
		}
		
		
		// Méthode servant à la création d'un acteur
		public static Props props() {
			return Props.create(BanqueActor.class);
		} 
		
		
		// Définition des messages en inner classes
		public interface Message {}
		
	
	
}
