package onceagain.modules.scheduling.services

import java.time.Duration

import onceagain.modules.scheduling.model.Response.{Failed, Normal}
import onceagain.modules.scheduling.model.{EasinessFactor, Response, Review}
import org.scalacheck.{Arbitrary, Gen}
import org.scalacheck.Arbitrary.arbitrary
import org.scalactic.TypeCheckedTripleEquals
import org.scalatest.prop.PropertyChecks
import org.scalatest.{FlatSpec, Matchers}

class Sm2SchedulerTest
    extends FlatSpec
    with Matchers
    with TypeCheckedTripleEquals
    with PropertyChecks {

  private val repGen = Gen.posNum[Int]

  private val efGen =
    Gen
      .chooseNum(EasinessFactor.Min, EasinessFactor.Max, specials = EasinessFactor.Default)
      .suchThat(ef ⇒ ef >= EasinessFactor.Min && ef <= EasinessFactor.Max)

  private implicit val arbReview = Arbitrary(for {
    rep ← repGen
    interval ← Gen.posNum[Long].map(Duration.ofDays)
    ef ← efGen
  } yield Review(rep, interval, ef))

  private val successfulResponses = Gen.oneOf(Response.All.filter(_.successful).toSeq)

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

  it should "go back to the first review but keep EF after a failure" in {
    forAll { review: Review ⇒
      Sm2Scheduler.nextReview(review, Failed) should === (Review(repetition = 1, ef = review.ef))
    }
  }

  it should "increment the interval factor in the general case" in {
    forAll(arbitrary[Review], successfulResponses) { (review, response) ⇒
      whenever(review.repetition >= 3) {
        val nextReview = Sm2Scheduler.nextReview(review, response)
        val proportion = nextReview.interval.getSeconds.toDouble / review.interval.getSeconds
        proportion shouldBe review.ef +- 0.01d
      }
    }
  }

  it should "reduce EF when response is hard" in {
    forAll { review: Review ⇒
      Sm2Scheduler.nextReview(review, Response.Hard).ef should be <= review.ef
    }
  }
}
