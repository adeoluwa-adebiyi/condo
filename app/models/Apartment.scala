package models

import db.models.{ApartmentType, Location, Offer, User}

case class Apartment(
                      val owner: User,
                      val name: String,
                      val rooms: Int,
                      val _type: ApartmentType,
                      val ref: String,
                      val location: Location,
                      val hasSale: Boolean,
                      val offer: Offer
                    )
