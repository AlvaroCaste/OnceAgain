package onceagain.modules.scheduling.services

import java.time.Duration

import onceagain.modules.scheduling.model.{EasinessFactor, Response, Review}

object Sm2Scheduler extends Scheduler {
  def nextReview(review: Review, response: Response): Review =
    if (response.successful)
      review.copy(repetition = review.repetition + 1,
        interval = incrementedInterval(review),
        ef = updateEf(response, review.ef))
    else Review(repetition = 1, ef = review.ef)

  private def incrementedInterval(review: Review) =
    review.repetition match {
      case 1 ⇒ Duration.ofDays(1)
      case 2 ⇒ Duration.ofDays(6)
      case _ ⇒ Duration.ofSeconds((review.interval.getSeconds * review.ef).toLong)
    }

  private def updateEf(response: Response, ef: EasinessFactor): EasinessFactor = response match {
    case Response.Hard ⇒ ef - 0.8d + 0.28d - 0.02d
    case _ ⇒ ef
  }
}
