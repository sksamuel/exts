package com.sksamuel.exts.concurrent

import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.locks.{Condition, Lock}
import java.util.function.IntUnaryOperator

import com.sksamuel.exts.Logging

import scala.util.control.NonFatal

class Throttler(counter: AtomicInteger, lock: Lock, isExecuting: Condition) extends Logging {

  def down(): Unit = {
    if (counter.updateAndGet(new IntUnaryOperator {
      override def applyAsInt(operand: Int): Int = operand - 1
    }) == 0) {
      lock.lock()
      isExecuting.signalAll()
      lock.unlock()
    }
  }

  def up(): Unit = {
    counter.updateAndGet(new IntUnaryOperator {
      override def applyAsInt(operand: Int): Int = operand + 1
    })
  }

  def apply[T](fn: => T): T = {
    while (counter.get > 0) {
      try {
        logger.debug("Throttling...")
        lock.lock()
        isExecuting.await()
        lock.unlock()
      } catch {
        case _: InterruptedException =>
        case NonFatal(e) => logger.error("Error while throttling", e)
      }
    }
    fn
  }
}