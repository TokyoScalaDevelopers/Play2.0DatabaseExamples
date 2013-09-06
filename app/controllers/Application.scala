package controllers

import play.api._
import play.api.mvc._

import models._

object Application extends Controller {
  val selectedDAO: String = scala.util.Properties.envOrElse("myDAO", "stub").toLowerCase
  val myDAO: DAO = selectedDAO match {
    case "stub" => StubDAO
    case "anorm" => AnormDAO
    case "slick" => SlickDAO
  }

  def index = Action {
    Redirect(routes.Application.threadList)
  }

  def threadList = Action {
    Ok(views.html.threadList(myDAO.queryThreads, forms.ThreadForm.NewThreadForm))
  }

  def threadView(utfEpoch: Long, shortTitle: String, random: Int) = Action {
    val filledForm = forms.PostForm.NewPostForm.bind(Map(
      "created" -> utfEpoch.toString,
      "shortTitle" -> shortTitle,
      "random" -> random.toString
    ))

    myDAO.queryThread(utfEpoch, shortTitle, random).map({ case (thread, replies) => Ok(views.html.threadView(thread, replies, filledForm)) })
      .getOrElse(Ok("404"))
  }

  def newThread = Action { implicit request =>
    val boundForm = forms.ThreadForm.NewThreadForm.bindFromRequest

    def badSubmission(message: String) = BadRequest(views.html.newThread("Error: " + message, boundForm))

    boundForm.fold(
      formWithErrors => badSubmission("Please ensure all fields are filled out"),
      thread => {
        val success: Either[String, Boolean] = myDAO.createThread(thread)

        if(success.isRight && success.right.get) {
          Redirect(routes.Application.threadList)
        } else {
          badSubmission(success.left.get)
        }
      }
    )
  }

  def newPost = Action { implicit request =>
    val boundForm = forms.PostForm.NewPostForm.bindFromRequest

    def badSubmission(message: String) = BadRequest(views.html.newPost("Error: " + message, boundForm))

    boundForm.fold(
      formWithErrors => badSubmission("Please ensure all fields are filled out correctly"),
      post => {
        val success: Either[String, Boolean] = myDAO.createPost(post)

        if(success.right.getOrElse(false) && post.threadID != None) {
          Redirect(helpers.threadIdentifier2URL(post.threadID.get))
        } else {
          badSubmission(success.left.get)
        }
      }
    )
  }

  def newUser = Action {
    Ok(views.html.newUser("Please fill out the following form to create a new user", forms.UserForm.NewUserForm))
  }

  def newUserAction = Action { implicit request =>
    val boundForm = forms.UserForm.NewUserForm.bindFromRequest

    def badSubmission(message: String) = BadRequest(views.html.newUser("Error: " + message, boundForm))

    boundForm.fold(
      formWithErrors => badSubmission("Please ensure all fields are filled out correctly"),
      user => {
        val success: Either[String, Boolean] = myDAO.createUser(user)
        if(success.right.getOrElse(false)) {
          Redirect(routes.Application.threadList)
        } else {
          badSubmission(success.left.get)
        }
      }
    )
  }
}
