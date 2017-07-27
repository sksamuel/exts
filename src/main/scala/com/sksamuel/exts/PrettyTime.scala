package com.sksamuel.exts

import scala.concurrent.duration.Duration

object PrettyTime {
  def apply(duration: Duration): String = {
    val abs = if (duration.toNanos < 0) duration.neg() else duration
    val hours = abs.toHours
    val minutes = abs.toMinutes % 60
    val seconds = abs.toSeconds % 60
    val secondsString = if (seconds < 10) s"0${seconds}s" else s"${seconds}s"
    val time = if (hours > 0) {
      s"${hours}h:${if (minutes < 10) "0" + minutes else minutes}m:$secondsString"
    } else {
      s"${minutes}m:$secondsString"
    }
    if (duration.toNanos < 0) "-" + time else time
  }
}
