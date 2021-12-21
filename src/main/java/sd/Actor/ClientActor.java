package sd.Actor;


import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.pattern.Patterns;
import akka.actor.ActorRef;

import java.time.Duration;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;


public class ClientActor extends AbstractActor {

	private int id; //L'id du client
	private ActorRef banque; //La banque
	private Compte compte; //Le compte du client
	private long time; //Ne sert que pour le test des performances. permet de savoir quand le dernier message à été consommer.
	
	public ClientActor(ActorRef banque,int id) {
		this.banque = banque;
		this.compte = null;
		this.id = id;
	}
	
	// Méthode servant à déterminer le comportement de l'acteur lorsqu'il reçoit un message
		@Override
		public Receive createReceive() {
			return receiveBuilder()
					.match(Connexion.class, message -> Connexion()) //On envoie une demande de connexion à la banque pour récupérer son compte.
					.match(StartAjout.class, message -> StartAjout()) //On envoie une demande d'ajout à la banque, utile pour les test
					.match(StartRetirer.class, message -> StartRetirer()) //On envoie une demande de retrait à la banque, utile pour les test
					.match(AfficherSolde.class, message -> AfficherSolde(message)) //On demande au client d'afficher le solde de son compte
					.match(AfficherSoldeTemps.class, message -> AfficherSolde(message)) //On affiche le solde du compte et le temps d'éxécution.
					.build();
		}
		
		//On demande à la banque de nous envoyer le compte du client pour pouvoir faire des demandes
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
		
		
		//Le client fait une demande à la banque
		private void StartAjout() {

				//On demande à la banque d'ajouter de l'argent au compte et on attend le nouveau solde du compte en retour
				CompletionStage<Object> resultatAjout = Patterns.ask(this.banque, new ClientActor.Ajout(500,this.compte), Duration.ofSeconds(100));
				 try {
					 	//On récupère le compte avec le nouveau solde
					 	this.compte = (Compte) resultatAjout.toCompletableFuture().get();
					 	time = System.currentTimeMillis(); //On récupère le temps de quand on à récupérer la réponse pour connaitre les performances
					} catch (InterruptedException e) {
						e.printStackTrace();
					} catch (ExecutionException e) {
						e.printStackTrace();
					}
			}
		
		private void StartRetirer() {
			//On demande à la banque de retirer de l'argent au compte et on attend le nouveau solde du compte en retour
			 CompletionStage<Object> resultatRetrait = Patterns.ask(this.banque, new ClientActor.Retrait(200,this.compte), Duration.ofSeconds(100));
			 try {
				//On récupère le compte avec le nouveau solde
					this.compte = (Compte) resultatRetrait.toCompletableFuture().get();
					time = System.currentTimeMillis();  //On récupère le temps de quand on à récupérer la réponse pour connaitre les performances
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				}

			 //System.out.println(this.compte.getSomme());
		}
			
		//On affiche le solde du compte
		private void AfficherSolde(final AfficherSolde message) {
			System.out.println("Solde du compte "+this.compte.getId()+" : "+this.compte.getSomme());
			
		}
		
		//On affiche le solde du compte et le temps d'éxécution
		private void AfficherSolde(final AfficherSoldeTemps message) {
			System.out.println("Solde du compte "+this.compte.getId()+" : "+this.compte.getSomme());
			System.out.println(" Temps d'éxécution "+(this.time-message.temps));
			
		}
		
		
		
		// Méthode servant à la création d'un acteur
		public static Props props(ActorRef banque, int id) {
			return Props.create(ClientActor.class,banque,id);
		} 
		
		
		// Définition des messages en inner classes
		public interface Message {}
		
		//Message de connexio contenant l'id du client
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
		
		//Message d'ajout envoyer à la banque contenant le montant à ajouter ainsi que le compte du client à affecter
		public static class Ajout implements Message {
			public Compte compte;
			public final int montantAjout;

			public Ajout(int montantAjout,Compte compte) {
				this.compte = compte;
				this.montantAjout = montantAjout;
			}
		}	
		
		//Message d'ajout envoyer à la banque contenant le montant à retirer ainsi que le compte du client à affecter
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
		
		//Message permettant d'afficher le  solde et le temps d'éxécution, on passe en paramètre le temps de début ou on à lancer le test.
		public static class AfficherSoldeTemps implements Message {
			public long temps;
			public AfficherSoldeTemps(long temps) {
				this.temps = temps;
			}
		}	
	
}
