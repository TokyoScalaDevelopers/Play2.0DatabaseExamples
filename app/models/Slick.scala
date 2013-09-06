package models

import scala.language.implicitConversions

import java.util.Date
import java.sql.{Timestamp => DBDate}

import play.api.db._
import play.api.Play.current

import slick.session.Database.threadLocalSession

import scala.slick.driver.PostgresDriver.simple._
import org.postgresql.util.PSQLException

object SlickDAO extends DAO {
  def queryThreads: Seq[Thread] = Seq()
  def queryThread(utfEpoch: Long, shortTitle: String, random: Int): Option[Tuple2[Thread, List[Post]]] = None
  def createThread(thread: Thread): Either[String, Boolean] = Left("Not implemented")
  def createPost(post: Post): Either[String, Boolean] = Left("Not implemented")
  def createUser(userid: String): Either[String, Boolean] = Left("Not implemented")
}
