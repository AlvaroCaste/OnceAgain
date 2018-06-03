package onceagain.modules.scheduling.services

import java.time.Duration

import onceagain.modules.scheduling.model.Response.Normal
import onceagain.modules.scheduling.model.{EasinessFactor, Review}
import org.scalacheck.Gen
import org.scalactic.TypeCheckedTripleEquals
import org.scalatest.prop.PropertyChecks
import org.scalatest.{FlatSpec, Matchers}

class Sm2SchedulerTest
    extends FlatSpec
    with Matchers
    with TypeCheckedTripleEquals
    with PropertyChecks {

  private val efGen =
    Gen
      .chooseNum(EasinessFactor.Min, EasinessFactor.Max, specials = EasinessFactor.Default)
      .suchThat(ef ⇒ ef >= EasinessFactor.Min && ef <= EasinessFactor.Max)

  "The SM2 Scheduler" should "schedule the first review in one day when response is not Failed" in {
    forAll(efGen) { ef ⇒
      Sm2Scheduler.nextReview(Review(repetition = 1, ef = ef), Normal) should ===(
        Review(repetition = 2, ef = ef, interval = Duration.ofDays(1)))
    }
  }

  it should "schedule the second review in six days when response is not Failed" in {
    forAll(efGen) { ef ⇒
      Sm2Scheduler.nextReview(Review(repetition = 2, ef = ef), Normal) should ===(
        Review(repetition = 3, ef = ef, interval = Duration.ofDays(6)))
    }
  }

}
