package sd.Actor;

import java.util.HashMap;

import akka.routing.BalancingPool;
import oracle.jdbc.pool.OracleDataSource;
import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.actor.ActorRef;
import java.sql.*;

public class BanqueActor extends AbstractActor {

	private HashMap<Integer,ActorRef> banquierListe; //Liste de des banquier avec leurs id en clé
	private ActorRef routerPersistance; //pool d'acteurs de persitances
	private Connection connexion; //Connexion à la bdd
	private Statement statement; //Permet d'éxécuter les query
	
	public BanqueActor() throws SQLException, ClassNotFoundException {
		//this.connexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/gestionbancaire","root","");  
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver").newInstance(); //On vérifie que l'on trouve le driver oracle
		}
		catch(ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
			System.out.println("Error: unable to load driver class!");
			System.exit(1);
		}
		Connection connexion = null;

		//On créer la source de connexion à oracle
		OracleDataSource ods = new OracleDataSource();
		ods.setDriverType("thin");
		ods.setServerName("butor");
		ods.setNetworkProtocol("tcp");
		ods.setDatabaseName("ENSB2021");
		ods.setPortNumber(1521);
		ods.setUser("cb778525");
		ods.setPassword("cb778525");
		
		try {
			this.connexion = ods.getConnection(); //On obtient la connexion
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		this.statement = this.connexion.createStatement();
		
		//On créer les acteurs de persistances à qui on passe la connexion
		//et le statement car ils en auront besoin pour l'enregistrement dans la bdd
		this.routerPersistance = getContext().actorOf(new BalancingPool(50).props(PersistanceActor.props(this.connexion,this.statement)));		
		
		//On créer la liste des banquier en allant la chercher dans la base de données
		this.banquierListe = new HashMap<Integer,ActorRef>();
		ResultSet res = statement.executeQuery("select * from banquier");
		while(res.next()) {
			//Chaque banquier à accès à la au pool d'acteur de persistances pour leurs envoyer des demande d'enregistrement en bdd
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
					.match(stopConnexion.class, message -> stopConnexion())
					.build();
		}
		
		//Récupère le compte du client et lui renvoie
		private void Connexion(ActorRef client,final ClientActor.Connexion message) throws SQLException {
			ResultSet res = statement.executeQuery("select * from compte where client="+message.id+"");
			res.next();
			client.tell(new Compte((Integer.parseInt(res.getString("solde"))),(Integer.parseInt(res.getString("id"))),(Integer.parseInt(res.getString("banquier")))), getSelf());
		}
		
		//Passe le message d'ajout du client au banquier responsable du compte
		private void Ajout(final ClientActor.Ajout message) {		
			this.banquierListe.get(message.compte.getBanquier()).forward(message, getContext());
		}
		
		//Passe le message de retrait du client au banquier responsable du compte
		private void Retrait(final ClientActor.Retrait message) {
			this.banquierListe.get(message.compte.getBanquier()).forward(message, getContext());
		}
		
		private void stopConnexion() throws SQLException {
			this.banquierListe = null;
			this.routerPersistance = null;
			this.statement.close();
			this.connexion.close();		
		}
		
		
		// Mï¿½thode servant ï¿½ la crï¿½ation d'un acteur
		public static Props props() {
			return Props.create(BanqueActor.class);
		} 
		
		
		// Dï¿½finition des messages en inner classes
		public interface Message {}
		
		public static class stopConnexion implements Message{
			public stopConnexion() {}
		}
		
}
