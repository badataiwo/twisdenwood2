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
class BookingController @Inject() (cc: ControllerComponents) extends AbstractController(cc) with I18nSupport {
     
  
  //Setup an application logger
  val appLogger: Logger = Logger("application")
   
  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  

    def booking() = Action { implicit request: Request[AnyContent] =>
     // val horses = Horse.findAll().toList
      val horses = Horse.findActiveHorses().toList
      appLogger.info("Debug Loading booking()")
      Ok(views.html.booking("Booking", horses))
    }
    
    def bookingtrainerstep(horseid :  String) = Action { implicit request: Request[AnyContent] =>
     // val horses = Horse.findAll().toList
      // val lst = request.queryString.toList
      // val keys = request.queryString.keys
       
     //   appLogger.info(s"Debug Loading QueryString list: ${lst}")
     //  appLogger.info(s"Debug Loading QueryString keys: ${keys}")
        val usernameOpt = request.session.get("username")
        usernameOpt.map { username =>
          val horse = Horse.findRecord(horseid)
      appLogger.info(s"Debug Loading bookingtrainerstep() ${horseid}")
      val trainers = Trainer.findAll().toList
      Ok(views.html.bookingtrainer("Booking", horse, trainers))
        }.getOrElse(Redirect(routes.ClientController.login()))
        
    }
    
    def bookingSelectime(horseid :  String, trainerid: String) = Action { implicit request: Request[AnyContent] =>
     
        
        val horse = Horse.findRecord(horseid)
      appLogger.info(s"Debug Loading bookingtrainerstep() ${horseid}")
      val trainer = Trainer.findRecord(trainerid)
      Ok(views.html.bookingselecttime("Booking", horse, trainer))
    }
    
    def bookingConfirm(FName: String, LName: String, Email: String, DayToBook:String,TimeToSelect:String,SubButton:String, horseid :  String, trainerid: String) = Action { implicit request: Request[AnyContent] =>
     
        
      appLogger.info(s"Debug Loading bookingConfirm() ${horseid}")
     
        
        val lessonBooking = LessonBooking.findBookingBasedOnDateTime(DayToBook, TimeToSelect)
        if (lessonBooking !=null && lessonBooking.size >0) {
           Ok(views.html.bookingerrmsg("Booking","Sorry, there is a lesson scheduled on " + DayToBook + " " + TimeToSelect))
        
        }else{
          LessonBooking.create(horseid, trainerid, FName, LName, Email, DayToBook, TimeToSelect, true)
          Ok(views.html.bookingconfirm("Booking", FName, DayToBook, TimeToSelect))
        }
      
    }
    
  
  def leasing() = Action { implicit request: Request[AnyContent] =>
     // val horses = Horse.findAll().toList
      val horses = Horse.findHorsesForLease().toList
      appLogger.info("Debug leasing()")
      Ok(views.html.forlease("Horses For Lease", horses))
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
    //  case "forlease" => Ok(views.html.forlease(page.capitalize))
      case "showing" => Ok(views.html.showing(page.capitalize))
      case "contactUs" => Ok(views.html.contactUs(page.capitalize))
      case "services" => Ok(views.html.services(page.capitalize))
      //case "login" => Ok(views.html.login(page.capitalize))
       //case "register" => Ok(views.html.register(page.capitalize))
      case _          => Ok(views.html.index("Welcome"))
    }
  }
  

}
