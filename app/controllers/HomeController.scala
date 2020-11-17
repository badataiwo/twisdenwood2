package controllers

import javax.inject.Inject
import play.api._
import play.api.mvc._
import play.api.i18n._
import model.db.collections.Horse
import model.db.collections.Scamp

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

    def booking() = Action { implicit request: Request[AnyContent] =>
     val horses = Horse.findAll().toList
      appLogger.info("Debug Loading booking()")
      Ok(views.html.booking("Booking", horses))
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
    //  case "booking"    => Ok(views.html.booking(page.capitalize))
      case "about"      => Ok(views.html.about(page.capitalize))
     // case "products" => Ok(views.html.products(page.capitalize))
      case "forlease" => Ok(views.html.forlease(page.capitalize))
      case "showing" => Ok(views.html.showing(page.capitalize))
      case "services" => Ok(views.html.services(page.capitalize))
      case _          => Ok(views.html.index("Welcome"))
    }
  }
  

}
