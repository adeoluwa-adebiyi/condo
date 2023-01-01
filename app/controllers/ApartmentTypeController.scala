package controllers

import models.{ApartmentType => DApartmentType}
import play.api.libs.json._
import play.api.mvc.{Action, AnyContent, BaseController, ControllerComponents, Request}
import repos.ApartmentTypeRepo

import javax.inject.Inject

class ApartmentTypeController @Inject() (val controllerComponents: ControllerComponents, val apartmentTypeRepo: ApartmentTypeRepo) extends BaseController{

  implicit val apartmentTypeToJson = Json.format[DApartmentType]

  def create(): Action[AnyContent] = Action{
    implicit request => {
      val _type = request.body.asJson.map(body => {
        apartmentTypeRepo.create(
          DApartmentType(_type = body("type").toString(),ref = "")
        ).head
      }).head
      Ok(Json.toJson(DApartmentType(ref=_type.ref.get, _type=_type._type.get)))
    }
  }

  def getAll(): Action[AnyContent] = Action {
    val _types = apartmentTypeRepo.getAll().get.map(_type => {
      DApartmentType(ref = _type.ref.get, _type= _type._type.get)
    })
    Ok(Json.toJson(_types))
  }
}
