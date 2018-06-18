package com.sksamuel.exts.io

import java.io.Closeable
import java.nio.file.StandardWatchEventKinds._
import java.nio.file.{ClosedWatchServiceException, FileSystems, Path, WatchService}
import java.util.Observable
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicBoolean

import scala.collection.JavaConverters._

class FileWatcher(watcher: WatchService) extends Observable with Closeable {

  private val running = new AtomicBoolean(true)
  private val executor = Executors.newSingleThreadExecutor()

  override def close(): Unit = {
    running.set(false)
    watcher.close()
  }

  def addObserver(o: PathObserver): Unit = super.addObserver(o)

  executor.submit(new Runnable {
    override def run(): Unit = {
      while (running.get) {
        try {
          val key = watcher.take()
          key.pollEvents().asScala.filterNot(_.kind == OVERFLOW).collect {
            case event if event.isInstanceOf[Path] => (event.context.asInstanceOf[Path], event.kind)
          }.foreach { case (path, kind) =>
            notifyObservers((path, kind))
          }
          key.reset()
        } catch {
          case _: ClosedWatchServiceException =>
          case other => other.printStackTrace()
        }
      }
    }
  })
}

trait PathObserver extends java.util.Observer {
  def onChange(path: Path): Unit
  final override def update(o: Observable, arg: scala.Any): Unit = onChange(arg.asInstanceOf[Path])
}

object FileWatcher {

  def apply(paths: Path*): FileWatcher = {
    val watcher = FileSystems.getDefault.newWatchService
    paths.foreach(_.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY))
    new FileWatcher(watcher)
  }
}