package forms

import play.api.data.Form
import play.api.data.Forms._
import play.api.data.format.Formats._
object LoginForm {
  val form = Form[Data](
      mapping(
      "Username" -> nonEmptyText,
      "Password" -> nonEmptyText)(Data.apply)(Data.unapply))
  
  case class Data(
        Username: String,
        Password: String)

  
}