package sd.Actor;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.pattern.Patterns;
import akka.actor.ActorRef;

import java.time.Duration;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;


public class ClientActor extends AbstractActor {

	private ActorRef banque;
	private Compte compte; 
	
	public ClientActor(ActorRef banque) {
		this.banque = banque;
		this.compte = new Compte(1500);
	}
	
	// Méthode servant à déterminer le comportement de l'acteur lorsqu'il reçoit un message
		@Override
		public Receive createReceive() {
			return receiveBuilder()
					.match(Connexion.class, message -> Start())
					.match(Start.class, message -> Start())
					.match(Ajout.class, message -> Ajout(message))
					.match(Retrait.class, message -> Retrait(message))
					.match(BanquierActor.SommeInssufisante.class, message -> SommeInssufisante(message))
					.build();
		}
		
		private void Connexion() {
			//CompletionStage<Object> result = banque.ask(arbitreActor, new ArbitreActor.StartGame(), Duration.ofSeconds(10));
			//this.banque.tell(new ClientActor.Connexion(), ActorRef.noSender());
		}
		
		private void Start() {
			this.banque.tell(new ClientActor.Ajout(500,this.compte), getSender());
			this.banque.tell(new ClientActor.Retrait(5000,this.compte), getSender());
		}
		
		private void Ajout(final Ajout message) {
			System.out.println("Hello World to ");
		}

		private void Retrait(final Retrait message) {
			System.out.println("Bye World");
		}
		
		private void SommeInssufisante(final BanquierActor.SommeInssufisante message) {
			System.out.println(message.message);
		}
		
		
		// Méthode servant à la création d'un acteur
		public static Props props() {
			return Props.create(ClientActor.class);
		} 
		
		
		// Définition des messages en inner classes
		public interface Message {}
		
		public static class Connexion implements Message {
			public Connexion() {}
		}	
		
		public static class Start implements Message {
			public Start() {}
		}
		
		public static class Ajout implements Message {
			public Compte compte;
			public final int montantAjout;

			public Ajout(int montantAjout,Compte compte) {
				this.compte = compte;
				this.montantAjout = montantAjout;
			}
		}	
		
		public static class Retrait implements Message {
			public Compte compte;
			public final int montantRetrait;

			public Retrait(int montantRetrait, Compte compte) {
				this.compte = compte;
				this.montantRetrait = montantRetrait;
			}
		}
	
}
