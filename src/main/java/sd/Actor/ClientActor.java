package sd.Actor;


import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.pattern.Patterns;
import akka.actor.ActorRef;

import java.time.Duration;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;


public class ClientActor extends AbstractActor {

	private int id;
	private ActorRef banque;
	private Compte compte; 
	private long time;
	
	public ClientActor(ActorRef banque,int id) {
		this.banque = banque;
		this.compte = null;
		this.id = id;
	}
	
	// Méthode servant à déterminer le comportement de l'acteur lorsqu'il reçoit un message
		@Override
		public Receive createReceive() {
			return receiveBuilder()
					.match(Connexion.class, message -> Connexion())
					.match(StartAjout.class, message -> StartAjout())
					.match(StartRetirer.class, message -> StartRetirer())
					.match(AfficherSolde.class, message -> AfficherSolde(message))
					.build();
		}
		
		
		private void Connexion() {
			CompletionStage<Object> result = Patterns.ask(this.banque, new ClientActor.Connexion(this.id), Duration.ofSeconds(100));
			 try {
					this.compte = (Compte) result.toCompletableFuture().get();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				}
		}
		
		private void StartAjout() {

				//On demande à la banque d'ajouter de l'argent au compte et on attend le nouveau solde du compte en retour
				CompletionStage<Object> resultatAjout = Patterns.ask(this.banque, new ClientActor.Ajout(500,this.compte), Duration.ofSeconds(100));
				 try {
					 	//On récupère le compte avec le nouveau solde
					 	this.compte = (Compte) resultatAjout.toCompletableFuture().get();
					 	time = System.currentTimeMillis();
					} catch (InterruptedException e) {
						e.printStackTrace();
					} catch (ExecutionException e) {
						e.printStackTrace();
					}
				//banque.tell(new ClientActor.Ajout(500,this.compte),getSelf());
				 //System.out.println(this.compte.getSomme());
				
			}
		
		private void StartRetirer() {
			//On demande à la banque de retirer de l'argent au compte et on attend le nouveau solde du compte en retour
			 CompletionStage<Object> resultatRetrait = Patterns.ask(this.banque, new ClientActor.Retrait(200,this.compte), Duration.ofSeconds(100));
			 try {
				//On récupère le compte avec le nouveau solde
					this.compte = (Compte) resultatRetrait.toCompletableFuture().get();
					time = System.currentTimeMillis();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				}

			 //System.out.println(this.compte.getSomme());
		}
			
		
		private void AfficherSolde(final AfficherSolde message) {
			System.out.println("Solde du compte "+this.compte.getId()+" : "+this.compte.getSomme());
			
		}
		
		
		
		// Méthode servant à la création d'un acteur
		public static Props props(ActorRef banque, int id) {
			return Props.create(ClientActor.class,banque,id);
		} 
		
		
		// Définition des messages en inner classes
		public interface Message {}
		
		public static class Connexion implements Message {
			public int id;
			
			public Connexion(int id) {
				this.id = id;
			}
		}	
		
		public static class StartAjout implements Message {
			public StartAjout() {}
		}
		
		public static class StartRetirer implements Message {
			public StartRetirer() {}
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
		
		public static class AfficherSolde implements Message {
			public AfficherSolde() {
			}
		}
		
	
}
