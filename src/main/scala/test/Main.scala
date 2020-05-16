package test

import akka.actor.{ActorRef, ActorSystem, Props}

object Main {

  //1.创建一个Actor Factory工厂。
  private val actorFactory = ActorSystem("actorFactory")

  //2.创建一个Actor的同时，返回此Actor的ActorRef.
  //  2.1.Props[PlainActor] 相当于创建了一个PlanActor的实例，利用反射去完成。
  //  2.2.后面的参数为该ActorRef的name，在一个环境下应当避免重复的name。
  private val plainActorRef: ActorRef = actorFactory.actorOf(Props[PlainActor],"plainActor")

  def main(args: Array[String]): Unit = {

      //3.向这个plainActor的Ref（代理人）发送消息："hello"
      //4.这条消息会发送给Message Dispatcher.
      //5.Message Dispatcher会将后面的消息发送给MailBox[plainActor]（它是一个Runnable线程）
      //6.这个MailBox一直处于监听状态，当它接收到主程序发送的"hello"消息之后，便会去调用plainActor所对应的receive方法。
      //7.注意，由于MailBox处于监听状态，所以目前为止主函数一直都没有退出。
      plainActorRef ! "hello"

      //8.无论执行多少次，消息接收的顺序一定是"hello","ok"
      plainActorRef ! "ok"

      //9.context.system.terminate()会退出主程序。
      plainActorRef ! "exit"

  }
}
