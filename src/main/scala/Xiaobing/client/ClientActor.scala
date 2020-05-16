package Xiaobing.client

import Xiaobing.common.{ClientProtocol, ServerProtocol}
import akka.actor.{Actor, ActorRef, ActorSelection, ActorSystem, Props}
import com.typesafe.config.ConfigFactory

import scala.io.StdIn

class ClientActor(serverHost: String, serverPort: Int) extends Actor {
  var serviceActorRef: ActorSelection = _

  //1.在这个ClientActor被构建出来之前，应该先向服务器中申请XiaoBingActor并绑定。
  //2.这个工作需要在preStart()方法内完成。
  override def preStart(): Unit = {
    //在目标akka服务的 /user "目录"下请求对应的Actor的name。
    serviceActorRef = context.actorSelection(s"akka.tcp://server@$serverHost:$serverPort/user/xiaobing")
    println("[successful] 获取远端Actor成功:"  + serviceActorRef)
  }

  override def receive: Receive = {
    case "start" => println("准备发送消息~")
    case "none" =>
      println("退出咨询，程序终止。")
      context.stop(self)
      context.system.terminate()
    case msg : String =>
      //网络间相互传输数据不能直接传输字符串。
      //所以要使用可被序列化的对象进行包装。
      println("消息已发送：")
      serviceActorRef ! ClientProtocol(msg)
    case ServerProtocol(msg)=>
      println(s"收到了来自${serverHost}的消息:$msg")

  }

}

object ClientActor extends App {

  //1.由于我们要从远端的服务器中获取XiaoBingActor，因此还需要配置服务器akka的对应端口。
  val (clientHost, clientPort, serverHost, serverPort) = (
    "127.0.0.1",
    9990,
    "127.0.0.1",
    9999
  )

  //2.绑定配置文件
  val config = ConfigFactory.parseString(
    s"""
        akka{
          actor{
            provider = "akka.remote.RemoteActorRefProvider"
          }

          remote{
            enabled-transports=["akka.remote.netty.tcp"]
            netty.tcp{
              hostname="$clientHost"
              port=$clientPort
            }
          }
        }
     """)

  //3.客服端的ActorSystem名称为client。
   val clientActorSystem = ActorSystem("client", config)

  //4.创建出这个Client的引用
  val client: ActorRef = clientActorSystem.actorOf(Props(new ClientActor(serverHost,serverPort)),"client")

  //启动customerRef
  client ! "start"

  while (true){

    println("咨询什么问题？none退出。")

    client !  StdIn.readLine()
  }

}