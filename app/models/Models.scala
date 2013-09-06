package models

import java.util.Date

case class ThreadIdentifier(created: Date, shortTitle: String, random: Int)
case class Thread(created: Date, title: String, userid: String, threadID: ThreadIdentifier)
case class Post(posted: Date, body: Option[String], userid: String, threadID: Option[ThreadIdentifier] = None)

trait DAO {
  def queryThreads: Seq[Thread]
  def queryThread(utfEpoch: Long, shortTitle: String, random: Int): Option[Tuple2[Thread, List[Post]]]
  def createThread(thread: Thread): Either[String, Boolean]
  def createPost(post: Post): Either[String, Boolean]
  def createUser(userid: String): Either[String, Boolean]
}
