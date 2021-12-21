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
	public static void main(String[] args) throws InterruptedException{
		ActorSystem actorSystem = ActorSystem.create();

        ActorRef banque = actorSystem.actorOf(BanqueActor.props());

        ActorRef client = actorSystem.actorOf(ClientActor.props(banque,1));
        client.tell(new ClientActor.Connexion(0), ActorRef.noSender());


        //Initialisation du programme ---------------------------------------------------
        //On fait beaucoup demandes au debut pour avoir un solde assez grand et être sur que la connexion fonctionne bien 
        for(int i = 0;i<10000;i++) {
        	client.tell(new ClientActor.StartAjout(), ActorRef.noSender());
        }

        //On attend que tout les message soit bien consommer
        try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

        
        long startTime = System.currentTimeMillis();
        
        System.out.println("10 Demande de retrait");
        //10 demande de retrait-----------------------------------------------------
        for(int i = 0;i<10;i++) {
        	client.tell(new ClientActor.StartRetirer(), ActorRef.noSender());
        }
        //On attend que les demandes soit bien consommer avant de faire les autres test
        try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

        //On affiche le nouveau solde et le temps d'éxécution
        client.tell(new ClientActor.AfficherSoldeTemps(startTime), ActorRef.noSender());
        //On attend pour que l'affichage soit bien fait
		Thread.sleep(100);
		
        startTime = System.currentTimeMillis();

        System.out.println("10 Demande d'ajout");
        //10 demande d'ajout---------------------------------------------------
        for(int i = 0;i<10;i++) {
        	client.tell(new ClientActor.StartAjout(), ActorRef.noSender());
        }

        try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

        client.tell(new ClientActor.AfficherSoldeTemps(startTime), ActorRef.noSender());
        Thread.sleep(100);
        startTime = System.currentTimeMillis();

        System.out.println("100 Demande de retrait");
        //100 demande de retrait-----------------------------------------------------
        for(int i = 0;i<100;i++) {
        	client.tell(new ClientActor.StartRetirer(), ActorRef.noSender());
        }

        try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

        client.tell(new ClientActor.AfficherSoldeTemps(startTime), ActorRef.noSender());
        Thread.sleep(100);
        startTime = System.currentTimeMillis();

        System.out.println("100 Demande d'ajout");
        //100 demande d'ajout---------------------------------------------------
        for(int i = 0;i<100;i++) {
        	client.tell(new ClientActor.StartAjout(), ActorRef.noSender());
        }

        try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

        client.tell(new ClientActor.AfficherSoldeTemps(startTime), ActorRef.noSender());
        Thread.sleep(100);
        startTime = System.currentTimeMillis();

        System.out.println("1000 Demande de retrait");
        //1 000 demande de retrait-----------------------------------------------------
        for(int i = 0;i<1000;i++) {
        	client.tell(new ClientActor.StartRetirer(), ActorRef.noSender());
        }

        try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

        client.tell(new ClientActor.AfficherSoldeTemps(startTime), ActorRef.noSender());
        Thread.sleep(100);
        startTime = System.currentTimeMillis();

        System.out.println("1000 Demande d'ajout");
        //1 000 demande d'ajout---------------------------------------------------
        for(int i = 0;i<1000;i++) {
        	client.tell(new ClientActor.StartAjout(), ActorRef.noSender());
        }

        try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

        client.tell(new ClientActor.AfficherSoldeTemps(startTime), ActorRef.noSender());
        Thread.sleep(100);
        startTime = System.currentTimeMillis();
        System.out.println("10 000 Demande de retrait");
      //10 000 demande de retrait----------------------------------------------------------------------
        

        for(int i = 0;i<10000;i++) {
        	client.tell(new ClientActor.StartRetirer(), ActorRef.noSender());
        }

        try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

        client.tell(new ClientActor.AfficherSoldeTemps(startTime), ActorRef.noSender());
        Thread.sleep(100);
       startTime = System.currentTimeMillis();

       System.out.println("10 000 Demande d'ajout");
      //10 000 demande d'ajout--------------------------------------------------------------------
        for(int i = 0;i<10000;i++) {
        	client.tell(new ClientActor.StartAjout(), ActorRef.noSender());
        }

        try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

        client.tell(new ClientActor.AfficherSoldeTemps(startTime), ActorRef.noSender());
        Thread.sleep(100);
        startTime = System.currentTimeMillis();
        System.out.println("100 000 Demande de retrait");
        //100 000 demande de retrait----------------------------------------------------------------------
          

          for(int i = 0;i<100000;i++) {
          	client.tell(new ClientActor.StartRetirer(), ActorRef.noSender());
          }

          try {
  			Thread.sleep(5000);
  		} catch (InterruptedException e) {
  			e.printStackTrace();
  		}

          client.tell(new ClientActor.AfficherSoldeTemps(startTime), ActorRef.noSender());
          Thread.sleep(100);
         startTime = System.currentTimeMillis();

         System.out.println("100 000 Demande d'ajout");
        //100 000 demande d'ajout--------------------------------------------------------------------
          for(int i = 0;i<100000;i++) {
          	client.tell(new ClientActor.StartAjout(), ActorRef.noSender());
          }

          try {
  			Thread.sleep(5000);
  		} catch (InterruptedException e) {
  			e.printStackTrace();
  		}

          client.tell(new ClientActor.AfficherSoldeTemps(startTime), ActorRef.noSender());

          banque.tell(new BanqueActor.stopConnexion(), ActorRef.noSender());
          Thread.sleep(200);
        // Arrêt du système d'acteurs
        actorSystem.terminate();
        
        System.exit(0);
	}
} 