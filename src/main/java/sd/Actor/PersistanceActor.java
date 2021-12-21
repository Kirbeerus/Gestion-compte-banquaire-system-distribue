package sd.Actor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import akka.actor.AbstractActor;
import akka.actor.Props;
import sd.Actor.BanqueActor.Message;
import akka.actor.ActorRef;


public class PersistanceActor extends AbstractActor{
	
	//Le persistanceActor enregistre en bdd donc il a besoin des connexion
	private Connection connexion;
	private Statement statement;
	//private PreparedStatement preparedStmt;
	
	public PersistanceActor(Connection connexion, Statement statement) throws SQLException {
		this.connexion = connexion;
		this.statement = statement;
		//On pr�pare la requete en avance pour que le code soit plus rapide
		//this.preparedStmt = this.connexion.prepareStatement("UPDATE compte SET solde = ? WHERE id = ?");
	}
	
	// M�thode servant � d�terminer le comportement de l'acteur lorsqu'il re�oit un message
			@Override
			public Receive createReceive() {
				return receiveBuilder()
						.match(BanquierActor.Enregistrement.class, message -> Enregistrement(message))
						.build();
			}
			
			//On enregistre en bdd en passant les param�tre � la requ�te.
			private void Enregistrement(final BanquierActor.Enregistrement message) throws SQLException {
				PreparedStatement preparedStmt = this.connexion.prepareStatement("UPDATE compte SET solde = ? WHERE id = ?");
				preparedStmt.setInt(1,(message.compte.getSomme()));
			    preparedStmt.setInt(2, message.compte.getId());
			    preparedStmt.executeUpdate();
			    preparedStmt.close();
			}

			
			
			// M�thode servant � la cr�ation d'un acteur
			public static Props props(Connection connexion, Statement statement) {
				return Props.create(PersistanceActor.class,connexion,statement);
			}
			

			// D�finition des messages en inner classes
			public interface Message {}
			
	
}

