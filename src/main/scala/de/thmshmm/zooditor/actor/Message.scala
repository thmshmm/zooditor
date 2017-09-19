package de.thmshmm.zooditor.actor

import org.apache.zookeeper.WatchedEvent

/**
  * Created by Thomas Hamm on 19.09.17.
  */
case object Message {

  final case class ZkChangeMessage(event: WatchedEvent)

  final case class ZkWatchMessage(path: String)

}
