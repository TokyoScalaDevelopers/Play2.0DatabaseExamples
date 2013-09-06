package models

// Database imports
import play.api.db._
import play.api.Play.current
import anorm._

import java.util.Date

import org.postgresql.util.PSQLException

object AnormDAO extends DAO {
  def queryThreads: Seq[Thread] = {
    DB.withConnection { implicit c =>
      SQL("""
        select created, shortTitle, random, title, userid
        from thread
      """).apply().collect({
        case Row(created: Date, shortTitle: String, random: Int, title: String, userid: String) => Thread(created, title, userid, ThreadIdentifier(created, shortTitle, random))
      }).toList
    }
  }

  def queryThread(utfEpoch: Long, shortTitle: String, random: Int): Option[Tuple2[Thread, List[Post]]] = None
  def createThread(thread: Thread): Either[String, Boolean] = Left("Not implemented")
  def createPost(post: Post): Either[String, Boolean] = Left("Not implemented")
  def createUser(userid: String): Either[String, Boolean] = Left("Not implemented")
}
