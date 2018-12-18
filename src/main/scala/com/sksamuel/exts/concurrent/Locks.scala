package com.sksamuel.exts.concurrent

import java.util.concurrent.locks.Lock

trait Locks {

  def withLockInterruptibly[T](lock: Lock)(thunk: => T): T = {
    try {
      lock.lockInterruptibly()
      thunk
    } finally {
      lock.unlock()
    }
  }

}
