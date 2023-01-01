package db.models

case class Location(
                     val ref: Option[String] = None,
                     val country: Option[String] = None,
                     val id: Option[Long] = None,
                     val address: Option[String] = None,
                     val postcode: Option[String] = None,
                     val city: Option[String] = None,
                     val longitude: Option[Double] = None,
                     val latitude: Option[Double] = None
                   )
