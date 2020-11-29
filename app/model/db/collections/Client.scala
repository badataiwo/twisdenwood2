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

case class Client(ClientID: String, FirstName: String,  LastName: String, Username: String, Password : String)

object Client extends DataStore {

  val appLogger: Logger = Logger("application")

  //Required for using Case Classes
  val codecRegistry = fromRegistries(fromProviders(classOf[Client]), DEFAULT_CODEC_REGISTRY)

  //Using Case Class to get a collection - Test
  val coll: MongoCollection[Client] = database.withCodecRegistry(codecRegistry).getCollection("Client")

  //Using Document to get collection
  val listings: MongoCollection[Document] = database.getCollection("Client")

  // Insert a new record
  def create(ClientID: String, FirstName: String, LastName: String, Username: String, Password : String) = {

    val doc: Document = Document(
      "ClientID" -> UUID.randomUUID().toString(),
      "FirstName" -> FirstName,
      "LastName" -> LastName,
      "Username" -> Username,
      "Password" -> Password,
     )

    val observable: Observable[Completed] = listings.insertOne(doc)

    observable.subscribe(new Observer[Completed] {
      override def onNext(result: Completed): Unit = appLogger.debug(s"Inserted: $result")
      override def onError(e: Throwable): Unit = appLogger.error(s"Failed: $e")
      override def onComplete(): Unit = appLogger.info("Completed")
    })

  }
  
  //Update record
  def update(newClient: Client) = {

    coll.findOneAndReplace(equal("ClientID", newClient.ClientID), newClient).results()

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
  
  
  
  //Get all records
  def findAll() = { coll.find().results() }


}