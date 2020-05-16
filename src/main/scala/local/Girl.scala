package local

import akka.actor.Actor

class Girl extends Actor {

  override def receive: Receive = {
    case "hi" =>
      //sender()指将消息原路返回出去。
      println("girl:Bye!")
      sender() ! "Bye!"
    case _ =>
      context.stop(self)
      context.system.terminate()
  }
}
