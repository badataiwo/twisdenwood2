package controllers

import javax.inject.Inject
import scala.util.Try
import play.api._
import play.api.mvc._
import play.api.i18n._
import model.db.collections._



/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's booking pages.
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
  
   def clientSchedule() = Action { implicit request: Request[AnyContent] =>
    //val messages: Messages = request.messages
    //val title: String = messages("home.title")  
 
      val usernameOpt = request.session.get("username")
         
         usernameOpt.map { username =>
           val client = Client.findRecord(username)
           
      val myBookings = LessonBooking.findBookingBasedOnCustomer(client.FirstName,client.LastName).toList
     appLogger.info("Loading Schedule page")    
     Ok(views.html.mybookings("My Bookings", myBookings))
           
      }.getOrElse(Redirect(routes.ClientController.login()))
    
  } 
  
   def deleteLessonSchedule(bookingid: String) = Action { implicit request: Request[AnyContent] =>
     
     
     val usernameOpt = request.session.get("username")
         
         usernameOpt.map { username =>
            LessonBooking.delete(bookingid)
           val client = Client.findRecord(username)
           
      val myBookings = LessonBooking.findBookingBasedOnCustomer(client.FirstName,client.LastName).toList
     appLogger.info("Loading Schedule page")    
     Ok(views.html.mybookings("My Bookings", myBookings))
           
      }.getOrElse(Redirect(routes.ClientController.login())) 

    
  }
  
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
     
         val usernameOpt = request.session.get("username")
         
         usernameOpt.map { username =>
           val horse = Horse.findRecord(horseid)
           val client = Client.findRecord(username)
      appLogger.info(s"Debug Loading bookingtrainerstep() ${horseid}")
      val trainer = Trainer.findRecord(trainerid)
      Ok(views.html.bookingselecttime("Booking", horse, trainer, client))
           
         }.getOrElse(Redirect(routes.ClientController.login()))
         
    }
    
    def bookingConfirm(FName: String, LName: String, Email: Option[String], DayToBook:String,TimeToSelect:String,SubButton:String, horseid :  String, trainerid: String) = Action { implicit request: Request[AnyContent] =>
     
        
      appLogger.info(s"Debug Loading bookingConfirm() ${horseid}")
     
        
        val lessonBooking = LessonBooking.findBookingBasedOnDateTime(DayToBook, TimeToSelect)
        if (lessonBooking !=null && lessonBooking.size >0) {
           Ok(views.html.bookingerrmsg("Booking","Sorry, there is a lesson scheduled on " + DayToBook + " " + TimeToSelect))
        
        }else{
            val aEmail = Email.getOrElse("noEmail@example.com")
          LessonBooking.create(horseid, trainerid, FName, LName, aEmail, DayToBook, TimeToSelect, true)
          Ok(views.html.bookingconfirm("Booking", FName, DayToBook, TimeToSelect))
        }
      
    }
    
   
}
