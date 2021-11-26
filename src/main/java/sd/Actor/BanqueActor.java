package sd.Actor;

import java.util.ArrayList;
import java.util.HashMap;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.actor.ActorRef;
import java.sql.*;


public class BanqueActor extends AbstractActor {

	private ArrayList<ActorRef> banquierListe;
	private Connection connexion;
	private Statement statement;
	
	public BanqueActor() throws SQLException {
		this.banquierListe = new ArrayList<>();
		this.banquierListe.add(getContext().actorOf(BanquierActor.props(), "banquier1"));
		this.banquierListe.add(getContext().actorOf(BanquierActor.props(), "banquier2"));
		this.connexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/gestionbancaire","root","");
		this.statement = connexion.createStatement();
		
	}

	// Méthode servant à déterminer le comportement de l'acteur lorsqu'il reçoit un message
		@Override
		public Receive createReceive() {
			return receiveBuilder()
					.match(ClientActor.Connexion.class, message -> Connexion(getSender(),message))
					.match(ClientActor.Ajout.class, message -> Ajout(message))
					.match(ClientActor.Retrait.class, message -> Retrait(message))
					.match(BanquierActor.AjoutBanquier.class, message -> AjoutBanquier(message,getSender()))
					.match(BanquierActor.RetraitBanquier.class, message -> RetraitBanquier(message,getSender()))
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
		
		private void AjoutBanquier(final BanquierActor.AjoutBanquier message,ActorRef client) throws SQLException {
			ResultSet res = this.statement.executeQuery("select * from compte where id=1;");
			res.next();
			
			PreparedStatement preparedStmt = this.connexion.prepareStatement("UPDATE compte SET solde = ? WHERE id = ?");
		    preparedStmt.setInt(1,(Integer.parseInt(res.getString("solde"))+message.montantAjout));
		    preparedStmt.setInt(2, message.compte.getId());

		    // execute the java preparedstatement
		    preparedStmt.executeUpdate();
		    
			message.compte.AjouterMontant(message.montantAjout);
			client.tell(message.compte,getSelf()); //On renvoie au client son compte mis a jour avec la nouvelle somme
		}

		private void RetraitBanquier(final BanquierActor.RetraitBanquier message,ActorRef client) throws SQLException {
			ResultSet res = this.statement.executeQuery("select * from compte where id=1;");
			res.next();
			
			PreparedStatement preparedStmt = this.connexion.prepareStatement("UPDATE compte SET solde = ? WHERE id = ?");
		    preparedStmt.setInt(1,(Integer.parseInt(res.getString("solde"))+message.montantRetrait));
		    preparedStmt.setInt(2, message.compte.getId());

		    // execute the java preparedstatement
		    preparedStmt.executeUpdate();
			
			message.compte.RetraitMontant(message.montantRetrait);
			client.tell(message.compte,getSelf());	//On renvoie au client son compte mis a jour avec la nouvelle somme
		}
		
		
		// Méthode servant à la création d'un acteur
		public static Props props() {
			return Props.create(BanqueActor.class);
		} 
		
		
		// Définition des messages en inner classes
		public interface Message {}
		
	
	
}
