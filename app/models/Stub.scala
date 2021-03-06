package models

import java.util.Date

object StubDAO extends DAO {
  def queryThreads: Seq[Thread] = Seq()
  def queryThread(utfEpoch: Long, shortTitle: String, random: Int): Option[Tuple2[Thread, List[Post]]] = None
  def createThread(thread: Thread): Either[String, Boolean] = Left("Not implemented")
  def createPost(post: Post): Either[String, Boolean] = Left("Not implemented")
  def createUser(userid: String): Either[String, Boolean] = Left("Not implemented")
}
