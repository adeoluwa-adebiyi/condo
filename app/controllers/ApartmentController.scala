package controllers

import db.models.{Apartment, ApartmentType, Location, Offer, User}
import play.api.mvc.{Action, AnyContent, BaseController, ControllerComponents}
import repos.ApartmentRepo
import play.api.libs.json._
import play.filters.csrf.RequireCSRFCheckAction
import play.mvc.With

import javax.inject.Inject

class ApartmentController @Inject() (val controllerComponents: ControllerComponents, val apartmentRepo: ApartmentRepo) extends BaseController{

  implicit val apartmentToJson: OFormat[Apartment] = Json.format[Apartment]
  implicit val userToJson: OFormat[User] = Json.format[User]
  implicit val apartmentTypeToJson: OFormat[ApartmentType] = Json.format[ApartmentType]
  implicit val locationToJson: OFormat[Location] = Json.format[Location]
  implicit val offerToJson: OFormat[Offer] = Json.format[Offer]

  def create(): Action[AnyContent] = Action {
    implicit request => {
      request.body.asJson.map(body => {
        Ok(Json.toJson(apartmentRepo.create(
          Apartment(
            owner = User(
              ref = request.headers.get(AUTHORIZATION).get
            ),
            name = body("name").toString(),
            rooms = body("rooms").asInstanceOf[Int],
            _type = ApartmentType(
              ref = Some(body("type").toString()),
            ),
            location = Location(ref = Some(body("location").toString())),
            ref = ""
          )).get)
        )
      }).head
    }
  }
}
