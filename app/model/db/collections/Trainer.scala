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
import org.mongodb.scala.model.Filters.equal

//import utils.config.CloudinaryCDN
import model.db.mongo.DataHelpers.GenericObservable
import model.db.mongo.DataStore

import play.api.Logger

case class Trainer(TrainerID: String, Name: String, Active: Boolean, Username: String, Password: String, SkillLevel : String, Rating: Int, img: String)

object Trainer extends DataStore {

  val appLogger: Logger = Logger("application")

  //Required for using Case Classes
  val codecRegistry = fromRegistries(fromProviders(classOf[Trainer]), DEFAULT_CODEC_REGISTRY)

  //Using Case Class to get a collection - Test
  val coll: MongoCollection[Trainer] = database.withCodecRegistry(codecRegistry).getCollection("Trainer")

  //Using Document to get collection
  val listings: MongoCollection[Document] = database.getCollection("Trainer")

  // Insert a new record
  def create(Name: String, Active: Boolean, Username: String, Password: String, SkillLevel : String, Rating: Int, img: String) = {

    val doc: Document = Document(
      "HorseID" -> UUID.randomUUID().toString(),
      "Name" -> Name,
      "Active" -> Active,
      "Username" -> Username,
      "Password" -> Password,
      "SkillLevel" -> SkillLevel,
      "Rating" -> Rating,
      "img" -> img)

    val observable: Observable[Completed] = listings.insertOne(doc)

    observable.subscribe(new Observer[Completed] {
      override def onNext(result: Completed): Unit = appLogger.debug(s"Inserted: $result")
      override def onError(e: Throwable): Unit = appLogger.error(s"Failed: $e")
      override def onComplete(): Unit = appLogger.info("Completed")
    })

  }
  
  //Update record
  def update(newTrainer: Trainer) = {

    coll.findOneAndReplace(equal("TrainerID", newTrainer.TrainerID), newTrainer).results()

  }
  
  //Delete record
  def delete(recId: String) = {
    coll.deleteOne(equal("TrainerID", recId)).printHeadResult("Delete Result: ")
  }
  
  //Find Record by Id
  def findRecord(recId: String) = {

    val rec = coll.find(equal("TrainerID", recId)).first().headResult()
    appLogger.info("Result  is: " + rec)
    rec
    /** Todo: What if findRecord returns no results. This should return an option **/
  }
  
  def findActiveTrainers() = {
     val rec = coll.find(equal("Active", true)).results()
     appLogger.info("Active Horses records result is: " + rec)
     
     rec   
  }
  
  
  
  //Get all records
  def findAll() = { coll.find().results() }


}