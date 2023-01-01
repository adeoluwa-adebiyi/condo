package db.models

import play.api.libs.json._

case class Apartment(
                      val owner: User,
                      val name: String,
                      val rooms: Int,
                      val _type: ApartmentType,
                      val ref: String,
                      val location: Location,
                      val hasSale: Option[Boolean]= Some(false),
                      val offer: Option[Offer] = None,
                      val id: Option[Long]=None,
                    ){
}
