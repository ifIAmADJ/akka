package test

import akka.actor.Actor

class PlainActor extends Actor {
  /**
    * 1.这个receive方法会被此Actor对象的MailBox(实现了Runnable接口)调用
    * 2.当Actor的MailBox接收到此消息的时候，就会调用receive方法。
    *
    * @return Receive是一个偏函数。如果忘记了该部分内容，可以参考之前的Scala之：函数高级部分。
    */
  override def receive: Receive = {
    case "hello" => println("nice to meet you too~")
    case "ok" => println("Okay.")
    case "exit" =>
      println("接收到exit指令，系统结束。")
      context.stop(self) //self 指代停用自身的ActorRef.
      context.system.terminate() //主程序关闭.
    case _ => println("Not matched.")
  }
}