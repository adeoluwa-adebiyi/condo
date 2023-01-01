package controllers

import play.api.mvc.{Action, ActionBuilder, AnyContent, BaseController, ControllerComponents, Request}
import play.api.libs.json._
import repos.UserRepo
import models.{User => DUser}

import java.util.Date
import db.models.User

import java.time.{Instant, LocalDate, ZoneId, ZoneOffset}
import java.time.format.DateTimeFormatter
import javax.inject.{Inject, Singleton}

@Singleton
class UserController @Inject() (val controllerComponents: ControllerComponents, val userRepo: UserRepo) extends BaseController{
  implicit val userToJson = Json.format[DUser]

  def getUser(id: String): Action[AnyContent] = Action {
    implicit request => {
      val res = userRepo.getUserById(id)
      res match {
        case Nil => NotFound
        case _ => {
          val userMap = res.head
          Ok(
            Json.toJson(DUser(
              userMap("firstname").asInstanceOf[String],
              userMap("lastname").asInstanceOf[String],
              userMap("dob").asInstanceOf[String],
              userMap("ref").asInstanceOf[String])
            )
          )
        }
      }
    }
  }

  def createUser(): Action[AnyContent] = Action{
    implicit request => {
      request.body.asJson.map(json =>{
        val df = DateTimeFormatter.ofPattern("YYYY-MM-dd")
        val userForm = DUser(
          firstname = json("firstname").toString(),
          lastname = json("lastname").toString(),
          dob = json("dob").toString(),
          ref = json("firstname").toString())
        val user = userRepo.createUser(
          User(
            firstname = Some(userForm.firstname),
            lastname = Some(userForm.lastname),
            dob = Some(userForm.dob),
            ref="")
        ).get
        println("USER:")
        println(user)
        Ok(Json.toJson(user))

      }).getOrElse(InternalServerError)
    }
  }
}
