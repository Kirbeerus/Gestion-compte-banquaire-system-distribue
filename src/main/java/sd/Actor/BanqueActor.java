package sd.Actor;

import java.util.ArrayList;
import java.util.HashMap;

import akka.routing.BalancingPool;
import akka . routing . RoundRobinPool ;
import akka.actor.AbstractActor;
import akka.actor.Props;
import sd.Actor.ClientActor.Message;
import akka.actor.ActorRef;
import java.sql.*;


public class BanqueActor extends AbstractActor {

	private HashMap<Integer,ActorRef> banquierListe;
	private ActorRef routerPersistance;
	private Connection connexion;
	private Statement statement;
	
	public BanqueActor() throws SQLException, ClassNotFoundException {
		//this.connexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/gestionbancaire","root","");  
		this.connexion = DriverManager.getConnection("jdbc:mysql://db4free.net:3306/gestionbancaire","cb778525","cb778525");
		this.statement = connexion.createStatement();
		
		this.routerPersistance = getContext().actorOf(new BalancingPool(50).props(PersistanceActor.props(this.connexion,this.statement)));		
		
		this.banquierListe = new HashMap<Integer,ActorRef>();
		ResultSet res = statement.executeQuery("select * from banquier;");
		while(res.next()) {
			this.banquierListe.put(Integer.parseInt(res.getString("id")),getContext().actorOf(BanquierActor.props(routerPersistance)));
		}
		
	}

	// Méthode servant à déterminer le comportement de l'acteur lorsqu'il reçoit un message
		@Override
		public Receive createReceive() {
			return receiveBuilder()
					.match(ClientActor.Connexion.class, message -> Connexion(getSender(),message))
					.match(ClientActor.Ajout.class, message -> Ajout(message))
					.match(ClientActor.Retrait.class, message -> Retrait(message))
					.build();
		}
		
		private void Connexion(ActorRef client,final ClientActor.Connexion message) throws SQLException {
			ResultSet res = statement.executeQuery("select * from compte where client="+message.id+";");
			res.next();
			client.tell(new Compte((Integer.parseInt(res.getString("solde"))),(Integer.parseInt(res.getString("id"))),(Integer.parseInt(res.getString("banquier")))), getSelf());
		}
		
		private void Ajout(final ClientActor.Ajout message) {		
			this.banquierListe.get(message.compte.getBanquier()).forward(message, getContext());
		}

		private void Retrait(final ClientActor.Retrait message) {
			this.banquierListe.get(message.compte.getBanquier()).forward(message, getContext());
		}
		
		
		// Méthode servant à la création d'un acteur
		public static Props props() {
			return Props.create(BanqueActor.class);
		} 
		
		
		// Définition des messages en inner classes
		public interface Message {}
		
}
