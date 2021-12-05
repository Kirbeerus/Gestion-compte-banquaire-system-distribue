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

public class AppSingleClient {
	public static void main(String[] args){
		ActorSystem actorSystem = ActorSystem.create();
     
        ActorRef banque = actorSystem.actorOf(BanqueActor.props());
        
        ActorRef client = actorSystem.actorOf(ClientActor.props(banque,1));
        client.tell(new ClientActor.Connexion(0), ActorRef.noSender());
        
        long startTime = System.currentTimeMillis();

        //10 000 demande d'ajout---------------------------------------------------
        for(int i = 0;i<10000;i++) {
        	client.tell(new ClientActor.StartAjout(), ActorRef.noSender());
        }
        
        try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
        
        client.tell(new ClientActor.AfficherSolde(startTime), ActorRef.noSender());
        
        long endtime = System.currentTimeMillis();
        
        startTime = System.currentTimeMillis();
        
        //10 000 demande de retrait-----------------------------------------------------
        for(int i = 0;i<10000;i++) {
        	client.tell(new ClientActor.StartRetirer(), ActorRef.noSender());
        }
        
        try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
        
        client.tell(new ClientActor.AfficherSolde(startTime), ActorRef.noSender());
              
      //100 000 demande d'ajout----------------------------------------------------------------------
        startTime = System.currentTimeMillis();
        
        for(int i = 0;i<100000;i++) {
        	client.tell(new ClientActor.StartAjout(), ActorRef.noSender());
        }
        
        try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
        
        client.tell(new ClientActor.AfficherSolde(startTime), ActorRef.noSender());
        
       startTime = System.currentTimeMillis();
        
      //100 000 demande de retrait--------------------------------------------------------------------
        for(int i = 0;i<100000;i++) {
        	client.tell(new ClientActor.StartRetirer(), ActorRef.noSender());
        }
        
        try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
        
        client.tell(new ClientActor.AfficherSolde(startTime), ActorRef.noSender());  
        
        // Arrêt du système d'acteurs
        actorSystem.terminate();
	}
}