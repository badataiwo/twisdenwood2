package model.db.collections

import java.util.Date
import java.util.UUID

import org.bson.codecs.configuration.CodecRegistries.fromProviders
import org.bson.codecs.configuration.CodecRegistries.fromRegistries
import org.mongodb.scala.Completed
import org.mongodb.scala.Document
import org.mongodb.scala.MongoCollection
import org.mongodb.scala.Observable
import org.mongodb.scala.Observer
import org.mongodb.scala.MongoClient.DEFAULT_CODEC_REGISTRY
import org.mongodb.scala.bson.codecs.Macros.createCodecProvider
import org.mongodb.scala.model.Filters._

//import utils.config.CloudinaryCDN
import model.db.mongo.DataHelpers.GenericObservable
import model.db.mongo.DataStore

import play.api.Logger

case class LessonBooking(BookingID: String, HorseID: String, TrainerID: String, ClientFName: String, ClientLName: String, ClientEmail : String, BookingDate: String, TimeOfDay: String, Confirmed: Boolean)

object LessonBooking extends DataStore {

  val appLogger: Logger = Logger("application")

  //Required for using Case Classes
  val codecRegistry = fromRegistries(fromProviders(classOf[LessonBooking]), DEFAULT_CODEC_REGISTRY)

  //Using Case Class to get a collection - Test
  val coll: MongoCollection[LessonBooking] = database.withCodecRegistry(codecRegistry).getCollection("LessonBooking")

  //Using Document to get collection
  val listings: MongoCollection[Document] = database.getCollection("LessonBooking")

  // Insert a new record
  def create( HorseID: String, TrainerID: String, ClientFName: String, ClientLName: String, ClientEmail : String, BookingDate: String, TimeOfDay: String, Confirmed: Boolean)= {

    val doc: Document = Document(
       "BookindID" -> UUID.randomUUID().toString(),
      "HorseID" -> HorseID,
      "TrainerID" -> TrainerID,
      "ClientFName" -> ClientFName,
      "ClientLName" -> ClientLName,
      "ClientEmail" -> ClientEmail,
      "ClientEmail" -> ClientEmail,
      "BookingDate" -> BookingDate,
      "TimeOfDay" -> TimeOfDay,
      "Confirmed" -> Confirmed,
       "created" -> new Date())

    val observable: Observable[Completed] = listings.insertOne(doc)

    observable.subscribe(new Observer[Completed] {
      override def onNext(result: Completed): Unit = appLogger.debug(s"Inserted: $result")
      override def onError(e: Throwable): Unit = appLogger.error(s"Failed: $e")
      override def onComplete(): Unit = appLogger.info("Completed")
    })

  }
  
  //Update record
  def update(newLessonBooking: LessonBooking) = {

    coll.findOneAndReplace(equal("BookingID", newLessonBooking.BookingID), newLessonBooking).results()

  }
  
  //Delete record
  def delete(recId: String) = {
    coll.deleteOne(equal("BookingID", recId)).printHeadResult("Delete Result: ")
  }
  
  //Find Record by Id
  def findRecord(recId: String) = {

    val rec = coll.find(equal("BookingID", recId)).first().headResult()
    appLogger.info("Result  is: " + rec)
    rec
    /** Todo: What if findRecord returns no results. This should return an option **/
  }
  
  def findBookingBasedOnCustomer(clientFName: String, clientLName: String, BookingDate: String) = {
    
    val rec = coll.find(and(equal("ClientFName", clientFName), equal("ClientLName",clientLName),equal("BookingDate",BookingDate))).results()
     appLogger.info("Active Horses records result is: " + rec)
     rec
  }
  
  //Get all records
  def findAll() = { coll.find().results() }


}