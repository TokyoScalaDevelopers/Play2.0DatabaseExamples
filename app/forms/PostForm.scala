package forms

import models.Post
import models.Thread
import models.ThreadIdentifier

import java.util.Date

import play.api.data._
import play.api.data.Forms._
import play.api.data.format.Formats._

object PostForm {
  // Custom apply and unapply methods to process data before creating the Thread
  def newPostApply(created: Long, shortTitle: String, random: Int, userid: String, body: String): Post = {
    val threadID = ThreadIdentifier(new Date(created), shortTitle, random)
    val date = new Date()

    Post(date, Some(body), userid, Some(threadID))
  }

  def newPostUnapply(post: Post): Option[Tuple5[Long, String, Int, String, String]] = {
    for(
      body <- post.body;
      threadid <- post.threadID
    ) yield {
      (threadid.created.getTime(), threadid.shortTitle, threadid.random, post.userid, body)
    }
  }

  def NewPostForm = Form(
    mapping(
      "created" -> of[Long],
      "shortTitle" -> nonEmptyText,
      "random" -> of[Int],
      "userid" -> nonEmptyText,
      "body" -> nonEmptyText
    )(newPostApply)(newPostUnapply)
  )
}
