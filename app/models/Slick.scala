package models

import scala.language.implicitConversions

import java.util.Date
import java.sql.{Timestamp => DBDate}

import play.api.db._
import play.api.Play.current

import slick.session.Database.threadLocalSession

import scala.slick.driver.PostgresDriver.simple._
import org.postgresql.util.PSQLException

object SlickCustomTypes {
  implicit def date2sql(date: Date): DBDate = new DBDate(date.getTime())
  implicit def sql2date(date: DBDate): Date = new Date(date.getTime())
}

object SlickDAO extends DAO {
  import SlickCustomTypes._

  object UsersT extends Table[(String)]("users") {
    def userid = column[String]("userid", O.PrimaryKey)
    def * = userid
  }

  def threadApply(created: DBDate, shortTitle: String, random: Int, title: String, userid: String): Thread = {
    val identifier = ThreadIdentifier(created, shortTitle, random)
    Thread(created, title, userid, identifier)
  }

  def threadUnapply(thread: Thread): Option[Tuple5[DBDate, String, Int, String, String]] = {
    val Thread(_, title, userid, ThreadIdentifier(created, shortTitle, random)) = thread
    Some((created, shortTitle, random, title, userid))
  }

  object ThreadsT extends Table[Thread]("thread") {
    def created = column[DBDate]("created", O.NotNull)
    def shortTitle = column[String]("shorttitle", O.NotNull)
    def random = column[Int]("random", O.NotNull)
    def title = column[String]("title", O.NotNull)
    def userid = column[String]("userid", O.NotNull)
    def * = created ~ shortTitle ~ random ~ title ~ userid <> (threadApply _, threadUnapply _)
  }

  def postApply(thread_created: DBDate, thread_sTitle: String, thread_random: Int, posted: DBDate, body: Option[String], userid: String): Post = {
    Post(posted, body, userid, Some(ThreadIdentifier(thread_created, thread_sTitle, thread_random)))
  }

  def postUnapply(post: Post): Option[Tuple6[DBDate, String, Int, DBDate, Option[String], String]] = {
    val Post(posted, body, userid, threadID) = post
    threadID.map({ threadID =>
      val ThreadIdentifier(thread_created, thread_sTitle, thread_random) = threadID
      (thread_created, thread_sTitle, thread_random, posted, body, userid)
    })
  }

  object PostsT extends Table[Post]("post") {
    def thread_created = column[DBDate]("thread_created", O.NotNull)
    def thread_sTitle = column[String]("thread_stitle", O.NotNull)
    def thread_random = column[Int]("thread_random", O.NotNull)
    def posted = column[DBDate]("posted", O.NotNull)
    def body = column[String]("body")
    def userid = column[String]("userid", O.NotNull)
    def * = thread_created ~ thread_sTitle ~ thread_random ~ posted ~ body.? ~ userid <> (postApply _, postUnapply _)
  }

  lazy val db = Database.forDataSource(DB.getDataSource())

  def queryThreads: Seq[Thread] = {
    db.withSession {
      Query(ThreadsT).to[List]
    }
  }

  def queryThread(utfEpoch: Long, shortTitle: String, random: Int): Option[Tuple2[Thread, List[Post]]] = {
    db.withSession {
      val thread = Query(ThreadsT)
        .filter(_.random === random)
        .filter(_.shortTitle === shortTitle)
        .filter(_.created === (new Date(utfEpoch): DBDate))
        .firstOption

      for(t <- thread) yield {
        val posts = Query(PostsT)
          .filter(_.thread_random === t.threadID.random)
          .filter(_.thread_sTitle === t.threadID.shortTitle)
          .filter(_.thread_created === (t.threadID.created: DBDate))
          .to[List]

        (t, posts)
      }
    }
  }

  def createThread(thread: Thread): Either[String, Boolean] = {
    db.withSession {
      try {
        Right(ThreadsT.insert(thread) > 0)
      } catch {
        case e: PSQLException => { Left(e.getServerErrorMessage.getMessage) }
      }
    }
  }

  def createPost(post: Post): Either[String, Boolean] = {
    db.withSession {
      try {
        Right(PostsT.insert(post) > 0)
      } catch {
        case e: PSQLException => { Left(e.getServerErrorMessage.getMessage) }
      }
    }
  }

  def createUser(userid: String): Either[String, Boolean] = {
    db.withSession {
      try {
        Right(UsersT.insert(userid) > 0)
      } catch {
        case e: PSQLException => Left(e.getServerErrorMessage.getMessage)
      }
    }
  }
}
