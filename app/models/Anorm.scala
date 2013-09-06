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

  def queryThread(utfEpoch: Long, shortTitle: String, random: Int): Option[Tuple2[Thread, List[Post]]] = {
    DB.withConnection { implicit c =>
      val thread: Option[Thread] = SQL("""
        select created, shortTitle, random, title, userid
        from thread
        where created = {created} and shortTitle = {shortTitle} and random = {random}
      """).on('created -> new Date(utfEpoch), 'shortTitle -> shortTitle, 'random -> random).apply().collect({
        case Row(created: Date, shortTitle: String, random: Int, title: String, userid: String) => Thread(created, title, userid, ThreadIdentifier(created, shortTitle, random))
      }).headOption

      thread.flatMap({ _thread =>
        val replies = SQL("""
          select posted, body, userid
          from post
          where thread_created = {created} and
                thread_sTitle = {shortTitle} and
                thread_random = {random}
        """).on(
          'created -> new Date(utfEpoch),
          'shortTitle -> shortTitle,
          'random -> random
        ).apply().collect({
          case Row(posted: Date, body: Option[String]@unchecked, userid: String) => Post(posted, body, userid)
        }).toList

        Some(Tuple2(_thread, replies))
      })
    }
  }

  def createThread(thread: Thread): Either[String, Boolean] = Left("Not implemented")
  def createPost(post: Post): Either[String, Boolean] = Left("Not implemented")
  def createUser(userid: String): Either[String, Boolean] = Left("Not implemented")
}
