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
	
	private Connection connexion;
	private Statement statement;
	private PreparedStatement preparedStmt;
	
	public PersistanceActor(Connection connexion, Statement statement,PreparedStatement preparedStmt) throws SQLException {
		//this.connexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/gestionbancaire","root","");
		//this.statement = connexion.createStatement();
		//this.preparedStmt = this.connexion.prepareStatement("UPDATE compte SET solde = ? WHERE id = ?");
		this.connexion = connexion;
		this.statement = statement;
		this.preparedStmt = preparedStmt;
	}
	
	// Méthode servant à déterminer le comportement de l'acteur lorsqu'il reçoit un message
			@Override
			public Receive createReceive() {
				return receiveBuilder()
						.match(BanqueActor.Enregistrement.class, message -> Enregistrement(message))
						.build();
			}
			

			private void Enregistrement(final BanqueActor.Enregistrement message) throws SQLException {
				preparedStmt.setInt(1,(message.compte.getSomme()));
			    preparedStmt.setInt(2, message.compte.getId());
			    preparedStmt.executeUpdate();
			}

			
			
			// Méthode servant à la création d'un acteur
			public static Props props(Connection connexion, Statement statement,PreparedStatement preparedStmt) {
				return Props.create(PersistanceActor.class,connexion,statement,preparedStmt);
			}
			

			// Définition des messages en inner classes
			public interface Message {}
			
	
}

