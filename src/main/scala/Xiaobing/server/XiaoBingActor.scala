package Xiaobing.server

import Xiaobing.common.{ClientProtocol, ServerProtocol}
import akka.actor.{Actor, ActorRef, ActorSystem, DeadLetter, Props}
import com.typesafe.config.ConfigFactory


class XiaoBingActor extends Actor {
  override def receive: Receive = {
    case DeadLetter(msg,from,_)=>{
      println(s"消息接收失败。源：$from,消息：$msg")
    }
    case "start" => println("客服小冰为您服务~")
    case ClientProtocol(msg) =>
      msg match{
        case "天气" => sender() ! ServerProtocol("天气真不错！");println("sas")
        case "语言" => sender() ! ServerProtocol("这个程式使用Scala语言编写而成！")
    }
  }
}

object XiaoBingActor extends App {

  //1.绑定本机地址和启动的端口号
  val host = "127.0.0.1" //绑定为本机地址
  val port = 9999 //绑定本机启动的端口号

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
              hostname="$host"
              port=$port
            }
          }
        }
     """)

  //3.客户端通过ActorSystem的名字匹配到服务器端的Actor.
  //这里和之前的构造方法相比，增加了一个配置文件的传入。
  //"server"的作用相当于url（统一资源定位).
  //因此服务启动时，akka监听的端口为：akka.tcp://server@127.0.0.1:9999。
  val server = ActorSystem("server", config)

  //4.创建小冰客服的Actor。
  //注意，name很重要。其它网络要通过 ../user/${name}来查找到这个Actor。
  val xiaobingRef: ActorRef = server.actorOf(Props[XiaoBingActor], "xiaobing")

  xiaobingRef ! "start"

}
