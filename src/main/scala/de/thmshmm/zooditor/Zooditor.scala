package de.thmshmm.zooditor

import akka.actor.ActorSystem
import com.typesafe.config.ConfigFactory
import de.thmshmm.zooditor.actor.{ManagerActor, OutputActor}
import de.thmshmm.zooditor.zk.ZkWrapper
import org.apache.zookeeper.ZooKeeper
import org.slf4j.LoggerFactory

import scala.concurrent.Await
import scala.concurrent.duration.Duration

/**
  * Created by Thomas Hamm on 19.09.17.
  */
object Main extends App {

  import de.thmshmm.zooditor.actor.Message._

  val log = LoggerFactory.getLogger(this.getClass)

  val appConf = ConfigFactory.load

  val system = ActorSystem("zooditor")


  val zk = ZkWrapper(appConf.getString("app.zk.connect"), appConf.getInt("app.zk.timeout"))

  val outputActor = system.actorOf(OutputActor.props(zk, appConf.getString("app.out.file"), appConf.getString("app.out.format")), "output")
  val managerActor = system.actorOf(ManagerActor.props(zk, outputActor), "manager")

  appConf.getStringList("app.paths").forEach(path => managerActor ! ZkWatchMessage(path))

  Await.result(system.whenTerminated, Duration.Inf)
}
