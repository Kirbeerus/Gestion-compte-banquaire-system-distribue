package sd;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.pattern.Patterns;
import akka.routing.RoundRobinPool;
import sd.Actor.*;

public class App {
	public static void main(String[] args) throws InterruptedException{
		ActorSystem actorSystem = ActorSystem.create();
     
        ActorRef banque = actorSystem.actorOf(BanqueActor.props()); //Cr�ation de la banque
        
        //On initialise les diff�rents clients
        ArrayList<ActorRef> clientListe = new ArrayList<ActorRef>();
        System.out.println("Les client sont initialiser");
        for(int i = 1;i<=10;i++) {
        	clientListe.add(actorSystem.actorOf(ClientActor.props(banque,i)));
        	clientListe.get(i-1).tell(new ClientActor.Connexion(0), ActorRef.noSender());
        }
        //On attend pour �tre sur que les clients est tous re�u leurs compte.
        try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
                
        String commande = "";
        Scanner scanner = new Scanner(System.in);  // Create a Scanner object
        while(!commande.equals("exit")) {
        	System.out.println("Entre les valeurs suivantes pour teste le programme \n [1] : Faire un ajout de 500 \n [2] : Faire un retrait de 200 \n "
        			+ "[3] : 10 client font un ajout de 500 sur plusieurs banquier \n [4] : 10 client font un retrait de 200 sur plusieurs banquiers \n [exit] : Stopper le programme");
        	System.out.println("---------------------------------------------------------------------------------------------------------");
        	commande = scanner.nextLine();
        	//On envoie une demande d'ajout avec 1 client et on montre le solde.
        	if(commande.equals("1")) {
        		clientListe.get(0).tell(new ClientActor.StartAjout(), ActorRef.noSender());
        		clientListe.get(0).tell(new ClientActor.AfficherSolde(), ActorRef.noSender());  
        	}
        	//On envoie une demande de retrait avec 1 client et on montre le solde.
			if(commande.equals("2")) {
				clientListe.get(0).tell(new ClientActor.StartRetirer(), ActorRef.noSender()); 
				clientListe.get(0).tell(new ClientActor.AfficherSolde(), ActorRef.noSender());
			}
			//On envoie une demande d'ajout avec 10 client et on montre le solde.
			if(commande.equals("3")) {
		        	for(int j=0;j<=9;j++) {
		        		clientListe.get(j).tell(new ClientActor.StartAjout(), ActorRef.noSender());
		        		clientListe.get(j).tell(new ClientActor.AfficherSolde(), ActorRef.noSender());
		        	} 
			}
			//On envoie une demande de retrait avec 10 client et on montre le solde.
			if(commande.equals("4")) {
				for(int j=0;j<=9;j++) {
	        		clientListe.get(j).tell(new ClientActor.StartRetirer(), ActorRef.noSender());
	        		clientListe.get(j).tell(new ClientActor.AfficherSolde(), ActorRef.noSender());
	        	} 
			}
			//On attend pour que le message soit compl�tement traiter.
			//Cela sert surtout � �viter que l'on r�afiche le menu des option avant le r�sultat.
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
        }
        

        // Arr�t du syst�me d'acteurs
        actorSystem.terminate();
        
        System.exit(0);
	}
}