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

case class Horse(HorseID: String, Name: String, Size: String, Level: String, Color: String, Gender : String, Active: Boolean, ImgUrl: String, ForLease: Boolean, Price: Double)

object Horse extends DataStore {

  val appLogger: Logger = Logger("application")

  //Required for using Case Classes
  val codecRegistry = fromRegistries(fromProviders(classOf[Horse]), DEFAULT_CODEC_REGISTRY)

  //Using Case Class to get a collection - Test
  val coll: MongoCollection[Horse] = database.withCodecRegistry(codecRegistry).getCollection("Horse")

  //Using Document to get collection
  val listings: MongoCollection[Document] = database.getCollection("Horse")

  // Insert a new record
  def create(Name: String, Size: String, Level: String, Color: String, Gender : String, Active: Boolean, ImgUrl: String, ForLease: Boolean, Price: Double) = {

    val doc: Document = Document(
      "HorseID" -> UUID.randomUUID().toString(),
      "Name" -> Name,
      "Size" -> Size,
      "Level" -> Level,
      "Color" -> Color,
      "Gender" -> Gender,
      "Active" -> Active,
      "ImgUrl" -> ImgUrl,
      "ForLease" -> ForLease,
      "Price" -> Price)

    val observable: Observable[Completed] = listings.insertOne(doc)

    observable.subscribe(new Observer[Completed] {
      override def onNext(result: Completed): Unit = appLogger.debug(s"Inserted: $result")
      override def onError(e: Throwable): Unit = appLogger.error(s"Failed: $e")
      override def onComplete(): Unit = appLogger.info("Completed")
    })

  }
  
  //Update record
  def update(newHorse: Horse) = {

    coll.findOneAndReplace(equal("HorseID", newHorse.HorseID), newHorse).results()

  }
  
  //Delete record
  def delete(recId: String) = {
    coll.deleteOne(equal("HorseID", recId)).printHeadResult("Delete Result: ")
  }
  
  //Find Record by Id
  def findRecord(recId: String) = {

    val rec = coll.find(equal("HorseID", recId)).first().headResult()
    appLogger.info("Result  is: " + rec)
    rec
    /** Todo: What if findRecord returns no results. This should return an option **/
  }
  
  def findActiveHorses() = {
    val rec = coll.find(and(equal("Active",true), equal("ForLease",false))).results()
     
    appLogger.info("Active Horses records result is: " + rec)
     
     rec
    
  }
  
   def findHorsesForLease() = {
     val rec = coll.find(equal("ForLease", true)).results()
     appLogger.info("Horses For Lease records result is: " + rec)
     
     rec
    
  }
  
  //Get all records
  def findAll() = { coll.find().results() }


}