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

public class AppMultiClient {
	public static void main(String[] args) throws InterruptedException{
		ActorSystem actorSystem = ActorSystem.create();

        ActorRef banque = actorSystem.actorOf(BanqueActor.props());

        ArrayList<ActorRef> clientListe = new ArrayList<ActorRef>();
        for(int i = 1;i<=10;i++) {
        	clientListe.add(actorSystem.actorOf(ClientActor.props(banque,i)));
        	clientListe.get(i-1).tell(new ClientActor.Connexion(0), ActorRef.noSender());
        }

        

        //10 000 demande de retrait-----------------------------------------------------
        for(int i = 0;i<10000;i++) {
        	for(int j=0;j<=9;j++) {
        		clientListe.get(j).tell(new ClientActor.StartAjout(), ActorRef.noSender());
        	} 
        }

        try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

        long startTime = System.currentTimeMillis();
        
        System.out.println("10 demandes de retrait");
        //10 demande de retrait-----------------------------------------------------
        	for(int j=0;j<=9;j++) {
        		clientListe.get(j).tell(new ClientActor.StartRetirer(), ActorRef.noSender());
        	} 

        try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

        clientListe.get(0).tell(new ClientActor.AfficherSoldeTemps(startTime), ActorRef.noSender());
        Thread.sleep(100);
        startTime = System.currentTimeMillis();

        System.out.println("10 demandes d'ajout");
        //10 demande d'ajout---------------------------------------------------
        	for(int j=0;j<=9;j++) {
        		clientListe.get(j).tell(new ClientActor.StartAjout(), ActorRef.noSender());
        	}      	

        try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

        clientListe.get(0).tell(new ClientActor.AfficherSoldeTemps(startTime), ActorRef.noSender());       
        Thread.sleep(100);
        startTime = System.currentTimeMillis();
        
        System.out.println("100 demandes de retrait");
        //100 demande de retrait-----------------------------------------------------
        for(int i = 0;i<10;i++) {
        	for(int j=0;j<=9;j++) {
        		clientListe.get(j).tell(new ClientActor.StartRetirer(), ActorRef.noSender());
        	} 
        }

        try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

        clientListe.get(0).tell(new ClientActor.AfficherSoldeTemps(startTime), ActorRef.noSender());
        Thread.sleep(100);
        startTime = System.currentTimeMillis();

        System.out.println("100 demandes d'ajout");
        //100 demande d'ajout---------------------------------------------------
        for(int i = 0;i<10;i++) {
        	for(int j=0;j<=9;j++) {
        		clientListe.get(j).tell(new ClientActor.StartAjout(), ActorRef.noSender());
        	}      	
        }

        try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

        clientListe.get(0).tell(new ClientActor.AfficherSoldeTemps(startTime), ActorRef.noSender());
        Thread.sleep(100);
        startTime = System.currentTimeMillis();
        
        System.out.println("1 000 demandes de retrait");
        //1000 demande de retrait-----------------------------------------------------
        for(int i = 0;i<100;i++) {
        	for(int j=0;j<=9;j++) {
        		clientListe.get(j).tell(new ClientActor.StartRetirer(), ActorRef.noSender());
        	} 
        }

        try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

        clientListe.get(0).tell(new ClientActor.AfficherSoldeTemps(startTime), ActorRef.noSender());
        Thread.sleep(100);
        startTime = System.currentTimeMillis();

        System.out.println("1 000 demandes d'ajout");
        //1000 demande d'ajout---------------------------------------------------
        for(int i = 0;i<100;i++) {
        	for(int j=0;j<=9;j++) {
        		clientListe.get(j).tell(new ClientActor.StartAjout(), ActorRef.noSender());
        	}      	
        }

        try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

        clientListe.get(0).tell(new ClientActor.AfficherSoldeTemps(startTime), ActorRef.noSender());
        Thread.sleep(100);
        startTime = System.currentTimeMillis();
        
        System.out.println("10 000 demandes de retrait");
        //10 000 demande de retrait-----------------------------------------------------
        for(int i = 0;i<1000;i++) {
        	for(int j=0;j<=9;j++) {
        		clientListe.get(j).tell(new ClientActor.StartRetirer(), ActorRef.noSender());
        	} 
        }

        try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

        clientListe.get(0).tell(new ClientActor.AfficherSoldeTemps(startTime), ActorRef.noSender());
        Thread.sleep(100);
        startTime = System.currentTimeMillis();

        System.out.println("10 000 demandes d'ajout");
        //10 000 demande d'ajout---------------------------------------------------
        for(int i = 0;i<1000;i++) {
        	for(int j=0;j<=9;j++) {
        		clientListe.get(j).tell(new ClientActor.StartAjout(), ActorRef.noSender());
        	}      	
        }

        try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

        clientListe.get(0).tell(new ClientActor.AfficherSoldeTemps(startTime), ActorRef.noSender());
        Thread.sleep(100);
       startTime = System.currentTimeMillis();  
        
       System.out.println("100 000 demandes de retrait");
       //100 000 demande de retrait--------------------------------------------------------------------
         for(int i = 0;i<10000;i++) {
         	for(int j=0;j<=9;j++) {
         		clientListe.get(j).tell(new ClientActor.StartRetirer(), ActorRef.noSender());
         	} 
         }

         try {
 			Thread.sleep(3000);
 		} catch (InterruptedException e) {
 			e.printStackTrace();
 		}

         clientListe.get(0).tell(new ClientActor.AfficherSoldeTemps(startTime), ActorRef.noSender());
         Thread.sleep(100);
       startTime = System.currentTimeMillis();     

        System.out.println("100 000 demandes d'ajout");
        //100 000 demande d'ajout----------------------------------------------------------------------
          startTime = System.currentTimeMillis();

          for(int i = 0;i<10000;i++) {
          	for(int j=0;j<=9;j++) {
          		clientListe.get(j).tell(new ClientActor.StartAjout(), ActorRef.noSender());
          	} 
          }

          try {
  			Thread.sleep(3000);
  		} catch (InterruptedException e) {
  			e.printStackTrace();
  		}

          clientListe.get(0).tell(new ClientActor.AfficherSoldeTemps(startTime), ActorRef.noSender());

          banque.tell(new BanqueActor.stopConnexion(), ActorRef.noSender());
          Thread.sleep(200);
        // Arrêt du système d'acteurs
        actorSystem.terminate();
        
	}
} 