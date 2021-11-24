package sd;

import java.io.IOException;
import java.time.Duration;
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
        ActorRef client = actorSystem.actorOf(Props.create(ClientActor.class,banque));
        
        client.tell(new ClientActor.Connexion(), ActorRef.noSender());
        client.tell(new ClientActor.Start(), ActorRef.noSender());
                
        // Arrêt du système d'acteurs
        actorSystem.terminate();
	}
}