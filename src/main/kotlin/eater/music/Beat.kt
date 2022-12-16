package eater.music

import eater.messaging.IMessage

data class Beat(val timeBars: Float, val sixteenth: Int, val hitTime: Float): IMessage