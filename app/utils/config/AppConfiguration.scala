package utils.config

import scala.util.Try
import com.typesafe.config.ConfigFactory

/**
 * Retrieve values from the application.conf file
 */
trait AppConfiguration {

  val config = ConfigFactory.load()
  
  //Get Strings
  def getConfigString(key : String, default : String)  = {
    Try(config.getString(key)).getOrElse(default)
  }
}



