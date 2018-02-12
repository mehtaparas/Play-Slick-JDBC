package models

import play.api.libs.json._

case class Customer(siteId: Int, accountNbr: Int, video: Int, voice: Int, data: Int)

object Customer {  
  implicit val customerFormat = Json.format[Customer]
}
