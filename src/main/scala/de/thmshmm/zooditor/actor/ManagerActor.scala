package de.thmshmm.zooditor.actor

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import de.thmshmm.zooditor.watcher.ZkChangeWatcher
import de.thmshmm.zooditor.zk.ZkWrapper

/**
  * Created by Thomas Hamm on 19.09.17.
  */
class ManagerActor(zk: ZkWrapper, outputActor: ActorRef) extends Actor with ActorLogging {

  import Message._

  override def receive: Receive = {
    case ZkWatchMessage(path) => {
      log.debug("Trying to add watcher for path '{}'", path)

      val watcher = ZkChangeWatcher(self, outputActor)
      val znodeStat = zk.exists(path, watcher)

      znodeStat match {
        case Some(stat) => {
          log.debug("Watcher for path '{}' added", path)
        }
        case None => log.error("Could not add watcher to path '{}'", path)
      }
    }
  }
}

object ManagerActor {
  def props(zk: ZkWrapper, outputActor: ActorRef): Props = Props(new ManagerActor(zk, outputActor))
}
