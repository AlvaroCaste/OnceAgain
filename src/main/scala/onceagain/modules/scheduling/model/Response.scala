package onceagain.modules.scheduling.model

import ca.mrvisser.sealerate

sealed trait Response {
  def successful: Boolean = true
}

object Response {
  case object Failed extends Response {
    override def successful: Boolean = false
  }
  case object Hard extends Response
  case object Normal extends Response
  case object Easy extends Response

  val All: Set[Response] = sealerate.values[Response]
}

