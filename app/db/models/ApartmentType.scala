package db.models

case class ApartmentType(
                          val id: Option[BigInt] = None,
                          val _type: Option[String] = None,
                          val ref: Option[String] = None
                        )
