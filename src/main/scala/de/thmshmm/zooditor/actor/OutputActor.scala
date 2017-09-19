package de.thmshmm.zooditor.actor

import java.io.{File, FileWriter}
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

import akka.actor.{Actor, ActorLogging, Props}
import de.thmshmm.zooditor.zk.ZkWrapper
import org.apache.zookeeper.data.Stat
import play.api.libs.json.Json

/**
  * Created by Thomas Hamm on 19.09.17.
  */
class OutputActor(zk: ZkWrapper, logPath: String, logFormat: String) extends Actor with ActorLogging {

  import Message._

  override def receive: Receive = {
    case ZkChangeMessage(event) =>
      val znodeStat = new Stat()
      val znodeData = zk.getData(event.getPath, false, Some(znodeStat))

      znodeData match {
        case Some(data) =>
          val fOut = new FileWriter(new File(logPath), true)

          try {
            fOut.write("\n" + getLogLine(logFormat, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss,SSS")), event.getPath, new String(data)))
          } finally {
            fOut.close()
          }
        case None => log.debug("No data from znode '{}' returned", event.getPath)
      }
  }


  private[this] def getLogLine(logFormat: String, timestamp: String, path: String, data: String): String = logFormat match {
    case "plain" => getPlainEvent(timestamp, path, data)
    case "json" => getJsonEvent(timestamp, path, data)
  }

  private[this] def getPlainEvent(timestamp: String, path: String, data: String): String = s"$timestamp $path $data"

  private[this] def getJsonEvent(timestamp: String, path: String, data: String): String = {
    Json.obj(
      "timestamp" -> timestamp,
      "path" -> path,
      "data" -> data
    ).toString
  }
}

object OutputActor {
  def props(zk: ZkWrapper, logPath: String, logFormat: String): Props = Props(new OutputActor(zk, logPath, logFormat))
}
