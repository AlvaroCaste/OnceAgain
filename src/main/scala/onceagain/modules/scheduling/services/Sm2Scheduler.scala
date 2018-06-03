package onceagain.modules.scheduling.services

import java.time.Duration

import onceagain.modules.scheduling.model.{Response, Review}

object Sm2Scheduler extends Scheduler {
  def nextReview(review: Review, response: Response): Review = review.repetition match {
    case 1 ⇒ Review(repetition = 2, Duration.ofDays(1))
    case _ ⇒ Review(repetition = 3, Duration.ofDays(6))
  }
}
