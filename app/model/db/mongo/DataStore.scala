package model.db.mongo

import org.mongodb.scala._
import utils.config.AppConfiguration



trait DataStore extends AppConfiguration {

  lazy val dbHost = config.getString("datastore.host")
  lazy val dbPort = config.getInt("datastore.port")
  lazy val dbUser = getConfigString("datastore.user", "")
  lazy val dbName =  getConfigString("datastore.dbname", "")
  lazy val dbPasswd = getConfigString("datastore.password", "")
 
   //MongoDB Atlas Method (Localhost if DB User is empty)
   val uri: String = s"mongodb+srv://$dbUser:$dbPasswd@$dbHost/$dbName?retryWrites=true&w=majority"
   System.setProperty("org.mongodb.async.type", "netty")
   val mongoClient: MongoClient = if (getConfigString("datastore.user", "").isEmpty()) MongoClient() else MongoClient(uri)
   val database: MongoDatabase = mongoClient.getDatabase(dbName)
   def close = mongoClient.close() //Do this when logging out

}

