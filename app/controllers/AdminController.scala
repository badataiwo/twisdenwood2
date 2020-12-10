package controllers

import javax.inject.Inject
import scala.util.Try
import play.api._
import play.api.mvc._
import play.api.i18n._
import model.db.collections._


/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's Backend, to manage Horses, Trainers, and Scheduling records.
 */
//@Singleton
class AdminController @Inject() (cc: ControllerComponents) extends AbstractController(cc) with I18nSupport {
     
  
  //Setup an application logger
  val appLogger: Logger = Logger("application")
   
    
  def adminEdit() = Action { implicit request: Request[AnyContent] =>
    //val messages: Messages = request.messages
    //val title: String = messages("home.title")  
 
    val horses = Horse.findAll().toList
     val trainers = Trainer.findAll().toList
    appLogger.info("Loading admin Edit page")    
    Ok(views.html.edit("Admin Edit", horses, trainers))
  }
  
    
  
  def processAdminLogin(username: String, pwd: String) = Action { implicit request: Request[AnyContent] =>
 
    val trainer = Trainer.findLoginTrainer(username, pwd)
    if (trainer !=null) {
     
      appLogger.info("Loading admin home page")    
      Ok(views.html.adminHome("Admin Home"))
    
    }else {
      appLogger.info("Trainer login was not found")
       Ok(views.html.bookingerrmsg("Admin Login error","Sorry, You entered an invalid login for the Trainer, Please press back and try again"))
    }
    
  }
  
  
   def addHorseRecord(horseid: String, horsename: String, horsesize: String, horselevel: String, horsecolor : String,horsegender : String, lessonactive : Option[String], horseImgUrl : String, leaseactive : Boolean, horseprice : Double, But : Option[String]) = Action { implicit request: Request[AnyContent] =>
        appLogger.info(s"Horse Record id $horseid deleted")
      appLogger.info(s"parameter leaseactive value is $leaseactive")
      appLogger.info(s"parameter lessonactive value: $lessonactive")
      val isActive = lessonactive.getOrElse("false").toBoolean
      appLogger.info(s"isActive getOrElse returns: $isActive")
      
      
       Horse.create(horsename,horsesize, horselevel, horsecolor, horsegender,isActive, horseImgUrl, leaseactive, horseprice)
            appLogger.info("Horse Record Added")
            appLogger.info("Loading admin Edit page")  
      val horses = Horse.findAll().toList
      val trainers = Trainer.findAll().toList
            Ok(views.html.edit("Admin Edit", horses, trainers))
      
  }
  
  
  
  def updateHorseRecord(horseid: String, horsename: String, horsesize: String, horselevel: String, horsecolor : String,horsegender : String, lessonactive : Option[String], horseImgUrl : String, leaseactive : Boolean, horseprice : Double, But : Option[String]) = Action { implicit request: Request[AnyContent] =>
   
    val horse = Horse.findRecord(horseid)
    if (horse !=null){
      //Horse.delete(horseid)
      appLogger.info(s"Horse Record id $horseid deleted")
      appLogger.info(s"parameter leaseactive value is $leaseactive")
      appLogger.info(s"parameter lessonactive value: $lessonactive")
      val isActive = lessonactive.getOrElse("false").toBoolean
      appLogger.info(s"isActive getOrElse returns: $isActive")
      //Horse.create(horsename,horsesize, horselevel, horsecolor, horsegender,isActive, horseImgUrl, leaseactive, horseprice)
      Horse.createUpdate(horseid, horsename,horsesize, horselevel, horsecolor, horsegender,isActive, horseImgUrl, leaseactive, horseprice)
      appLogger.info("Horse Record updated")
      val horses = Horse.findAll().toList
     val trainers = Trainer.findAll().toList
     appLogger.info("Loading admin Edit page")    
    Ok(views.html.edit("Admin Edit", horses, trainers))
    
    
    }else{
       Ok(views.html.bookingerrmsg("Admin Horse Edit error",s"Sorry, The horse record $horseid could not be located. This horse record cannot be updated."))
    }
  }
  
  
  def addTrainerRecord( tname: String, traineractive: Option[String], TrainerSkill: String, TrainerRating: Int, timageurl: String, but: Option[String]) = Action { implicit request: Request[AnyContent] =>
    
      val username=tname
      val pwd="12345"
       val isActive = traineractive.getOrElse("false").toBoolean
      Trainer.create(tname, isActive, username, pwd, TrainerSkill, TrainerRating, timageurl)
      appLogger.info(s"Trainer $tname Record added")
      val horses = Horse.findAll().toList
     val trainers = Trainer.findAll().toList
     appLogger.info("Loading admin Edit page") 
     Ok(views.html.edit("Admin Edit", horses, trainers))
    
  }
  
  def updateTrainerRecord(trainerid : String, tname: String, traineractive: Option[String], TrainerSkill: String, TrainerRating: Int, timageurl: String, but: String) = Action { implicit request: Request[AnyContent] =>
    
    val trainer = Trainer.findRecord(trainerid)
    
    if (trainer !=null) {
      val isActive = traineractive.getOrElse("false").toBoolean
      Trainer.createUpdate(trainerid, tname, isActive, trainer.Username, trainer.Password, TrainerSkill, TrainerRating, timageurl)
       appLogger.info(s"Trainer with id: $trainerid Record updated")
      val horses = Horse.findAll().toList
     val trainers = Trainer.findAll().toList
     appLogger.info("Loading admin Edit page") 
      Ok(views.html.edit("Admin Edit", horses, trainers))
    }else{
      Ok(views.html.bookingerrmsg("Admin Horse Edit error",s"Sorry, The Trainer record $trainerid could not be located. This Trainer record cannot be updated."))
    }
  }
  
  
  def deleteTrainerRecord(trainerid : String) = Action { implicit request: Request[AnyContent] =>
    //val messages: Messages = request.messages
    //val title: String = messages("home.title")  
 
    Trainer.delete(trainerid)
    appLogger.info(s"Trainer Record id $trainerid deleted")
    val horses = Horse.findAll().toList
     val trainers = Trainer.findAll().toList
    appLogger.info("Loading admin Edit page")    
    Ok(views.html.edit("Admin Edit", horses, trainers))
  }
  
  def deleteHorseRecord(horseid : String) = Action { implicit request: Request[AnyContent] =>
    //val messages: Messages = request.messages
    //val title: String = messages("home.title")  
 
    Horse.delete(horseid)
    appLogger.info(s"Horse Record id $horseid deleted")
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
 
    
     val lessonBooking = LessonBooking.findAll().toList
    appLogger.info("Loading Schedule page")    
    Ok(views.html.viewbookings("View Bookings", lessonBooking))
  }  

    
  def deleteLessonSchedule(bookingid: String) = Action { implicit request: Request[AnyContent] =>
     
      LessonBooking.delete(bookingid)
      val lessonBooking = LessonBooking.findAll().toList
    appLogger.info("Loading Schedule page")    
    Ok(views.html.viewbookings("View Bookings", lessonBooking))      

    
  }
    
  
 

}
