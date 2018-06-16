package onceagain.modules.scheduling.services

import onceagain.modules.scheduling.model.{Response, Review}

trait Scheduler {
  def nextReview(review: Review, response: Response): Review
}
