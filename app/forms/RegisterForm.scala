package forms

import play.api.data.Form
import play.api.data.Forms._
import play.api.data.format.Formats._
object RegisterForm {
  val form = Form[Data](
      mapping(
      "FirstName" -> nonEmptyText,
      "LastName" -> nonEmptyText,
      "Username" -> nonEmptyText,
      "Password" -> nonEmptyText)(Data.apply)(Data.unapply))
  
  case class Data(
        FirstName:String,
        LastName: String,
        Username: String,
        Password: String)

  
}