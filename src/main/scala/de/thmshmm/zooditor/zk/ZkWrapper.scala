package de.thmshmm.zooditor.zk

import org.apache.zookeeper.data.Stat
import org.apache.zookeeper.{Watcher, ZooKeeper}
import org.slf4j.LoggerFactory

/**
  * Created by Thomas Hamm on 19.09.17.
  */
class ZkWrapper(connect: String, timeout: Int) {
  val log = LoggerFactory.getLogger(this.getClass)

  val zk = new ZooKeeper(connect, timeout, null)

  var timeoutCount = 0

  log.info("Connecting to ZooKeeper")

  while (!zk.getState.isConnected && timeoutCount < timeout) {
    Thread.sleep(1000)
    timeoutCount += 1000
  }

  zk.getState.isConnected match {
    case true => {
      log.info("ZooKeeper connection established")
    }
    case false => {
      log.error("Connection to ZooKeeper on '{}' could not be established", connect)
      log.error("Exit application")
      sys.exit(1)
    }
  }

  log.info(zk.getState.toString)

  def exists(path: String, watcher: Watcher): Option[Stat] = {
    try {
      val stat = zk.exists(path, watcher)
      if (stat == null) None else Some(stat)
    } catch {
      case e: Exception => {
        log.error(e.getMessage)
        None
      }
    }
  }

  def getData(path: String, watch: Boolean, stat: Option[Stat]): Option[Array[Byte]] = {
    try {
      Some(zk.getData(path, watch, stat.getOrElse(null)))
    } catch {
      case e: Exception => {
        log.error(e.getMessage)
        None
      }
    }
  }
}

object ZkWrapper {
  def apply(connect: String, timeout: Int) = new ZkWrapper(connect, timeout)
}
