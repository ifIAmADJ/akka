package local

import akka.actor.{Actor, ActorRef}

class Boy(private val actorRef: ActorRef) extends Actor{

  override def receive: Receive = {
    case "greet" =>
      println("boy:hi!")
      actorRef ! "hi"
    case "Bye!" =>
      println("boy:bye!")
      context.stop(self)
      context.system.terminate()
    case _=>
      context.stop(self)
      context.system.terminate()
  }
}


