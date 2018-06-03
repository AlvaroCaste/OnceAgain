package onceagain.modules.scheduling.services

import java.time.Duration

import onceagain.modules.scheduling.model.Response.Normal
import onceagain.modules.scheduling.model.Review
import org.scalactic.TypeCheckedTripleEquals
import org.scalatest.{FlatSpec, Matchers}

class Sm2SchedulerTest extends FlatSpec with Matchers  with TypeCheckedTripleEquals {

  "The SM2 Scheduler" should "schedule the first review in one day when response is not Failed" in {
    Sm2Scheduler.nextReview(Review(1), Normal) should === (Review(repetition = 2, Duration.ofDays(1)))
  }

  it should "schedule the second review in six days when response is not Failed" in {
    Sm2Scheduler.nextReview(Review(2), Normal) should === (Review(repetition = 3, Duration.ofDays(6L)))
  }

}
