package com.sksamuel.exts

import scala.concurrent.duration.Duration

object PrettyTime {
  def apply(duration: Duration): String = {
    val hours = duration.toHours
    val minutes = duration.toMinutes % 60
    val seconds = duration.toSeconds % 60
    val secondsString = if (seconds < 10) s"0${seconds}s" else s"${seconds}s"
    if (hours > 0) {
      s"${hours}h:${minutes}m:$secondsString"
    } else {
      s"${minutes}m:$secondsString"
    }
  }
}
