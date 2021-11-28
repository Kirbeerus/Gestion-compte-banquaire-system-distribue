package sd;

import java.io.IOException;
import java.time.Duration;
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
	public static void main(String[] args){
		ActorSystem actorSystem = ActorSystem.create();
     
        ActorRef banque = actorSystem.actorOf(BanqueActor.props());
        ActorRef client = actorSystem.actorOf(ClientActor.props(banque,1));
        
        client.tell(new ClientActor.Connexion(0), ActorRef.noSender());
        
        long startTime = System.currentTimeMillis();
        
        for(int i = 0;i<100000;i++) {
        	client.tell(new ClientActor.StartAjout(), ActorRef.noSender());
        }
        
        Scanner sc= new Scanner(System.in);    //System.in is a standard input stream  
        String choix = "";
        
        while(!choix.equals("exit")) {
        	System.out.println("Que voulez vous faire : ");
        	choix = sc.nextLine();
        }
        
        /*try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}*/
        
        client.tell(new ClientActor.AfficherSolde(startTime), ActorRef.noSender());
                
        // Arrêt du système d'acteurs
        actorSystem.terminate();
	}
}