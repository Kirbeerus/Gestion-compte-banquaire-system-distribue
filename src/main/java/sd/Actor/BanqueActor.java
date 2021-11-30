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

	private ArrayList<ActorRef> banquierListe;
	private ActorRef routerPersistance;
	private Connection connexion;
	private Statement statement;
	private PreparedStatement preparedStmt;
	
	public BanqueActor() throws SQLException {
		this.connexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/gestionbancaire","root","");
		this.statement = connexion.createStatement();
		this.preparedStmt = this.connexion.prepareStatement("UPDATE compte SET solde = ? WHERE id = ?");
		
		this.routerPersistance = getContext().actorOf(new BalancingPool(50).props(PersistanceActor.props(this.connexion,this.statement,this.preparedStmt)));		
		
		this.banquierListe = new ArrayList<>();
		ResultSet res = statement.executeQuery("select * from banquier;");
		while(res.next()) {
			this.banquierListe.add(getContext().actorOf(BanquierActor.props(routerPersistance)));
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
			client.tell(new Compte((Integer.parseInt(res.getString("solde"))),(Integer.parseInt(res.getString("id")))), getSelf());
		}
		
		private void Ajout(final ClientActor.Ajout message) {		
			this.banquierListe.get(0).forward(message, getContext());
		}

		private void Retrait(final ClientActor.Retrait message) {
			this.banquierListe.get(0).forward(message, getContext());
		}
		
		
		// Méthode servant à la création d'un acteur
		public static Props props() {
			return Props.create(BanqueActor.class);
		} 
		
		
		// Définition des messages en inner classes
		public interface Message {}
		
}
