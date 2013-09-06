package forms

import play.api.data._
import play.api.data.Forms._

object UserForm {
  def NewUserForm = Form(
    mapping(
      "userid" -> nonEmptyText
    )({x: String => x})({x: String => Some(x)})
  )
}
