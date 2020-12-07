package controllers

import javax.inject.Inject
import scala.util.Try
import play.api._
import play.api.mvc._
import play.api.i18n._
import model.db.collections._


/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
//@Singleton
class HomeController @Inject() (cc: ControllerComponents) extends AbstractController(cc) with I18nSupport {
     
  
  //Setup an application logger
  val appLogger: Logger = Logger("application")
   
  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index() = Action { implicit request: Request[AnyContent] =>
    val messages: Messages = request.messages
    val title: String = messages("home.title")  
 
    appLogger.info("Debug "+title)    
    Ok(views.html.index("Welcome"))
  }

  
  def adminEdit() = Action { implicit request: Request[AnyContent] =>
    //val messages: Messages = request.messages
    //val title: String = messages("home.title")  
 
    val horses = Horse.findAll().toList
     val trainers = Trainer.findAll().toList
    appLogger.info("Loading admin Edit page")    
    Ok(views.html.edit("Admin Edit", horses, trainers))
  }
  
  def leasing() = Action { implicit request: Request[AnyContent] =>
     // val horses = Horse.findAll().toList
      val horses = Horse.findHorsesForLease().toList
      appLogger.info("Debug leasing()")
      Ok(views.html.forlease("Horses For Lease", horses))
    }
  
  def viewSchedule() = Action { implicit request: Request[AnyContent] =>
    //val messages: Messages = request.messages
    //val title: String = messages("home.title")  
 
    val horses = Horse.findAll().toList
     val trainers = Trainer.findAll().toList
     val lessonBooking = LessonBooking.findAll().toList
    appLogger.info("Loading Schedule page")    
    Ok(views.html.viewbookings("View Bookings", horses, trainers, lessonBooking))
  }  
    
  //  def about() = Action { implicit request: Request[AnyContent] =>
  //    Ok(views.html.about("About"))
  //  }
  //
  //  def products() = Action { implicit request: Request[AnyContent] =>
  //    Ok(views.html.products("Products"))
  //  }

  /***
  *  We might need to use this function to load certain pages.
  */
  def loadPage(page: String) = Action { implicit request: Request[AnyContent] =>
    appLogger.debug(s"Loading page: $page")
    
    page match {
      case "about"      => Ok(views.html.about(page.capitalize))
      case "showing" => Ok(views.html.showing(page.capitalize))
      case "contactUs" => Ok(views.html.contactUs(page.capitalize))
      case "services" => Ok(views.html.services(page.capitalize))
       //case "viewbookings" => Ok(views.html.viewbookings(page.capitalize))
      //case "login" => Ok(views.html.login(page.capitalize))
      //case "register" => Ok(views.html.register(page.capitalize))
      case "adminlogin" => Ok(views.html.adminlogin(page.capitalize))
      case "adminHome" => Ok(views.html.adminHome(page.capitalize))
     
      //case "edit" => Ok(views.html.edit(page.capitalize))
      case _          => Ok(views.html.index("Welcome"))
    }
  }
  

}
