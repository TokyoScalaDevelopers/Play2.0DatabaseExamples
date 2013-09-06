package forms

import models.Thread
import models.ThreadIdentifier

import java.util.Date

import play.api.data._
import play.api.data.Forms._

object ThreadForm {
  // Custom apply and unapply methods to process data before creating the Thread
  def newThreadApply(title: String, userid: String): Thread = {
    val date = new Date()
    val random: Int = {
      val raw = util.Random.nextInt().abs
      raw % 10000 // We just want four numbers
    }

    val identifier = ThreadIdentifier(date, utils.cleanTitle(title), random)
    Thread(date, title, userid, identifier)
  }

  def newThreadUnapply(thread: Thread): Option[Tuple2[String, String]] = {
    Some((thread.title, thread.userid))
  }

  def NewThreadForm = Form(
    mapping(
      "title" -> nonEmptyText,
      "userid" -> nonEmptyText
    )(newThreadApply)(newThreadUnapply)
  )
}
