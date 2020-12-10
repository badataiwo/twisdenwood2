package controllers

import javax.inject._
import scala.util.Try
import scala.collection.mutable.ListBuffer
import play.api._
import play.api.mvc._
import play.api.i18n._
import forms.RegisterForm
import forms.LoginForm
import play.api._
import play.api.mvc._
import play.api.i18n.{ Lang, Langs, I18nSupport, Messages, MessagesApi, MessagesProvider, MessagesImpl }
import play.api.i18n.Messages.Implicits._
import model.db.collections.Client

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's related to Login on the customer side.
 */
//@Singleton
class ClientController @Inject() (
    cc: ControllerComponents,
    config: play.api.Configuration,
    langs: Langs,
    messagesApi: MessagesApi) 
  
  extends AbstractController(cc) with I18nSupport {
  
  implicit val lang: Lang = langs.availables.head
  implicit val messagesProvider: MessagesProvider = { MessagesImpl(lang, messagesApi) }

  val logger: Logger = Logger("Client-controller")

  val defaultPicMap = Map[String, String] ("url" -> "", "public_id" -> "", "format" -> "")
  

  /**
   * READ all records from Mongo Collection
   */
  def register() = Action { 
    implicit request: Request[AnyContent] =>
     logger.debug("On Register")
    val action = Try(request.queryString.toList(0)._1).getOrElse("")
    Ok(views.html.register(RegisterForm.form, action, defaultPicMap, config))
  
  }
  
  def processPage() = Action { 
    implicit request =>
    logger.debug("Create")
    RegisterForm.form.bindFromRequest.fold(
        formWithErrors => BadRequest(views.html.register(formWithErrors, "new", defaultPicMap, config)),
        
        form => {
          logger.debug("Registering...")
          Client.create ( form.FirstName, form.LastName, form.Username, form.Password)
          Redirect("/login")
        })
        
  }
  
  def login() = Action { 
    implicit request: Request[AnyContent] =>
     logger.debug("On Login")
    val action = Try(request.queryString.toList(0)._1).getOrElse("")
    Ok(views.html.login(LoginForm.form, action, defaultPicMap, config))
  }
  
  def loginTry() = Action { 
    implicit request =>
    logger.debug("On LoginTry..")
    LoginForm.form.bindFromRequest.fold(
        formWithErrors => BadRequest(views.html.login(formWithErrors, "new", defaultPicMap, config)),
        
        form => {
          logger.debug("Loggin ins...")
          val record = Client.findRecord(form.Username)
          //val user = record.Username
          //val pass = record.Password
          val passFromForm = form.Password
          //val userFromForm = form.Username
          if (record != null && (record.Password).equals(passFromForm)) {
            logger.debug("Success on login")
            Redirect("/booking").withSession("username" -> record.Username)  
          }
          else {
            Redirect("/login").flashing("fail"-> "Incorrect login/password combination")
          }
          
        })
        
  }
  
  def logout() = Action {
    Redirect(routes.ClientController.login()).withNewSession
  }
 
  
}
    
    
   