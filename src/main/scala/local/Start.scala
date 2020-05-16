package local

import akka.actor.{ActorRef, ActorSystem, Props}

object Start {

  def main(args: Array[String]): Unit = {

    //在创建任何Actor之前都要先构造出actorSystem。
    val actorSystemFactory = ActorSystem("actorSystemFactory")

    //Girl的构造方法不需要传参，因此可以使用类型反射的方式令Props构造出一个Girl的代理。
    val girl: ActorRef = actorSystemFactory.actorOf(Props[Girl],"girl")

    //注意：由于Boy的构造方法需要传参，因此在这里不能简单地利用类型反射令Props构造一个Boy的代理出来。
    val boy: ActorRef = actorSystemFactory.actorOf(Props(new Boy(girl)),"boy")

    //给boy发送消息。
    boy ! "greet"


  }
}
