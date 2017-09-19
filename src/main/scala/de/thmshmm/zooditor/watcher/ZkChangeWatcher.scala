package de.thmshmm.zooditor.watcher

import akka.actor.ActorRef
import org.apache.zookeeper.{WatchedEvent, Watcher}
import org.slf4j.LoggerFactory

/**
  * Created by Thomas Hamm on 19.09.17.
  */
class ZkChangeWatcher(managerActor: ActorRef, outputActor: ActorRef) extends Watcher {

  import de.thmshmm.zooditor.actor.Message._

  val log = LoggerFactory.getLogger(this.getClass)

  override def process(event: WatchedEvent): Unit = {
    managerActor ! ZkWatchMessage(event.getPath)
    outputActor ! ZkChangeMessage(event)
  }
}

object ZkChangeWatcher {
  def apply(managerActor: ActorRef, outputActor: ActorRef) = new ZkChangeWatcher(managerActor, outputActor)
}
