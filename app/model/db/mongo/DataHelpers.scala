package model.db.mongo

import java.util.concurrent.TimeUnit

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import play.api._
import org.mongodb.scala._

object DataHelpers {

  val logger: Logger = Logger("data-helper")
  
  implicit class DocumentObservable[C](val observable: Observable[Document]) extends ImplicitObservable[Document] {
    override val converter: (Document) => String = (doc) => doc.toJson
  }

  implicit class GenericObservable[C](val observable: Observable[C]) extends ImplicitObservable[C] {
    override val converter: (C) => String = (doc) => doc.toString
  }

  trait ImplicitObservable[C] {
    val observable: Observable[C]
    val converter: (C) => String

    def results(): Seq[C] = Await.result(observable.toFuture(), Duration(30, TimeUnit.SECONDS))
    def headResult() = Await.result(observable.head(), Duration(30, TimeUnit.SECONDS))
    def printResults(initial: String = ""): Unit = {
      if (initial.length > 0) logger.debug(initial)
      results().foreach(res => logger.debug(converter(res)))
    }
    def printHeadResult(initial: String = ""): Unit = logger.debug(s"${initial}${converter(headResult())}")
  }

}