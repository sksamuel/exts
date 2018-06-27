package com.sksamuel.exts.io

import java.nio.file.{Files, Path}
import java.util.concurrent.{CountDownLatch, TimeUnit}

import org.scalatest.{FlatSpec, Matchers}

class FileWatcherTest extends FlatSpec with Matchers {

  "file watcher" should "notify on change" ignore {
    val dir = Files.createTempDirectory("filewatchertest")
    val latch1 = new CountDownLatch(1)
    val latch2 = new CountDownLatch(2)

    val watcher = FileWatcher(dir)
    watcher.addObserver(new PathObserver {
      override def onChange(path: Path): Unit = {
        latch1.countDown()
        latch2.countDown()
      }
    })

    latch1.getCount shouldBe 1
    latch2.getCount shouldBe 2

    dir.resolve("file1").toFile.createNewFile()
    latch1.await(5, TimeUnit.SECONDS) shouldBe true
    dir.resolve("file2").toFile.createNewFile()
    latch2.await(5, TimeUnit.SECONDS) shouldBe true

    watcher.close()
  }
}