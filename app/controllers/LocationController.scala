package controllers

import play.api.mvc.{Action, AnyContent, BaseController, ControllerComponents}
import repos.LocationRepo

import javax.inject.Inject
import models.Location
import play.api.libs.json._

class LocationController @Inject() (
                                     val controllerComponents: ControllerComponents,
                                     val locationRepo: LocationRepo
                                   ) extends BaseController{

  implicit val locationToJson = Json.format[Location]

  def create(): Action[AnyContent] = Action{
    implicit request =>
    val location = request.body.asJson.map( body => locationRepo.create(
      Location(
        country = Some(body("country").toString()),
        address = Some(body("address").toString()),
        postcode = Some(body("postcode").toString()),
        city = Some(body("city").toString()),
      )
    )).head.get
      Ok(Json.toJson(Location(
        ref = location.ref,
        country = location.country,
        address = location.address,
        postcode = location.postcode,
        city = location.city,
        longitude = location.longitude,
        latitude = location.latitude
      )))
  }

  def getAll(): Action[AnyContent] = Action {
    Ok(Json.toJson(locationRepo.getAll().get.map(
      location => Location(
        ref = location.ref,
        country = location.country,
        address = location.address,
        postcode = location.postcode,
        city = location.city,
        longitude = location.longitude,
        latitude = location.latitude
      )
    )))
  }
}
