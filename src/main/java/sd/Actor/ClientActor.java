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
	private long time; //Ne sert que pour le test des performances. permet de savoir quand le dernier message � �t� consommer.
	
	public ClientActor(ActorRef banque,int id) {
		this.banque = banque;
		this.compte = null;
		this.id = id;
	}
	
	// M�thode servant � d�terminer le comportement de l'acteur lorsqu'il re�oit un message
		@Override
		public Receive createReceive() {
			return receiveBuilder()
					.match(Connexion.class, message -> Connexion()) //On envoie une demande de connexion � la banque pour r�cup�rer son compte.
					.match(StartAjout.class, message -> StartAjout()) //On envoie une demande d'ajout � la banque, utile pour les test
					.match(StartRetirer.class, message -> StartRetirer()) //On envoie une demande de retrait � la banque, utile pour les test
					.match(AfficherSolde.class, message -> AfficherSolde(message)) //On demande au client d'afficher le solde de son compte
					.match(AfficherSoldeTemps.class, message -> AfficherSolde(message)) //On affiche le solde du compte et le temps d'�x�cution.
					.build();
		}
		
		//On demande � la banque de nous envoyer le compte du client pour pouvoir faire des demandes
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
		
		
		//Le client fait une demande � la banque
		private void StartAjout() {

				//On demande � la banque d'ajouter de l'argent au compte et on attend le nouveau solde du compte en retour
				CompletionStage<Object> resultatAjout = Patterns.ask(this.banque, new ClientActor.Ajout(500,this.compte), Duration.ofSeconds(100));
				 try {
					 	//On r�cup�re le compte avec le nouveau solde
					 	this.compte = (Compte) resultatAjout.toCompletableFuture().get();
					 	time = System.currentTimeMillis(); //On r�cup�re le temps de quand on � r�cup�rer la r�ponse pour connaitre les performances
					} catch (InterruptedException e) {
						e.printStackTrace();
					} catch (ExecutionException e) {
						e.printStackTrace();
					}
			}
		
		private void StartRetirer() {
			//On demande � la banque de retirer de l'argent au compte et on attend le nouveau solde du compte en retour
			 CompletionStage<Object> resultatRetrait = Patterns.ask(this.banque, new ClientActor.Retrait(200,this.compte), Duration.ofSeconds(100));
			 try {
				//On r�cup�re le compte avec le nouveau solde
					this.compte = (Compte) resultatRetrait.toCompletableFuture().get();
					time = System.currentTimeMillis();  //On r�cup�re le temps de quand on � r�cup�rer la r�ponse pour connaitre les performances
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
		
		//On affiche le solde du compte et le temps d'�x�cution
		private void AfficherSolde(final AfficherSoldeTemps message) {
			System.out.println("Solde du compte "+this.compte.getId()+" : "+this.compte.getSomme());
			System.out.println(" Temps d'�x�cution "+(this.time-message.temps));
			
		}
		
		
		
		// M�thode servant � la cr�ation d'un acteur
		public static Props props(ActorRef banque, int id) {
			return Props.create(ClientActor.class,banque,id);
		} 
		
		
		// D�finition des messages en inner classes
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
		
		//Message d'ajout envoyer � la banque contenant le montant � ajouter ainsi que le compte du client � affecter
		public static class Ajout implements Message {
			public Compte compte;
			public final int montantAjout;

			public Ajout(int montantAjout,Compte compte) {
				this.compte = compte;
				this.montantAjout = montantAjout;
			}
		}	
		
		//Message d'ajout envoyer � la banque contenant le montant � retirer ainsi que le compte du client � affecter
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
		
		//Message permettant d'afficher le  solde et le temps d'�x�cution, on passe en param�tre le temps de d�but ou on � lancer le test.
		public static class AfficherSoldeTemps implements Message {
			public long temps;
			public AfficherSoldeTemps(long temps) {
				this.temps = temps;
			}
		}	
	
}
