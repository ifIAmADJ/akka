package Xiaobing.common

//1.使用样例类来使客服端在发送消息时遵守某种协议，因为样例类自动实现了序列化，以及apply方法。
case class ClientProtocol(mes :String)

//2.同样的，服务器端发送消息也会遵守一个协议，并且服务器端和客服端返回的消息未必是同一种类型的消息。【这里是特例】
case class ServerProtocol(mes :String)