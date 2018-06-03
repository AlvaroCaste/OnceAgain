package onceagain.modules.scheduling.model

import java.time.Duration

final case class Review(repetition: Int,
                        interval: Duration = Duration.ZERO,
                        ef: EasinessFactor = EasinessFactor.Default) {
  require(repetition >= 1, s"Repetition must be strictly positive: $repetition given")
}
