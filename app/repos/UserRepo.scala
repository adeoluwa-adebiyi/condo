package repos

import anorm._
import anorm.SqlParser._
import db.models.User
import play.api.db.Database

import java.util.Date
import javax.inject.{Inject, Singleton}
import models.{User => DUser}

import java.time.Instant
import java.time.temporal.Temporal

@Singleton
class UserRepo @Inject() (val database: Database){

  val parser: RowParser[Map[String, Any]] =
    SqlParser.folder(Map.empty[String, Any]) { (map, value, meta) =>
      Right(map + (meta.column.qualified -> value))
    }

  def getUserById(id: String): List[Map[String,Any]] = {
    database.withConnection { implicit c =>
      val user = SQL("SELECT * FROM app_user WHERE ref::text = {id}").on("id" -> id).as(parser.*)
      return user
    }
  }

  def createUser(userData: User): Option[DUser] = {
    database.withConnection {
      implicit  c => {
        try{
          val userMap: Map[String, Any] = SQL(
            """
              INSERT into app_user(firstname, lastname, dob) VALUES({firstname},{lastname}, DATE({dob})) RETURNING *
              """
          ).on(
            "firstname" -> userData.firstname,
            "lastname" -> userData.lastname,
            "dob" -> userData.dob
          ).as(parser.+).head
          println(userMap)
          return Some(DUser(
            firstname = userMap("app_user.firstname").toString,
            lastname = userMap("app_user.lastname").toString,
            dob = userMap("app_user.dob").toString,
            ref = userMap("app_user.ref").toString
          ))
        }catch {
          case e: Exception => {
            println("Exception:")
            println(e)
            None
          }
        }
      }
    }
  }

  def deleteUser(id: String): Boolean = {
    database.withConnection {
      implicit c => {
        SQL("DELETE FROM app_user WHERE ref::text = {id}").on("id" -> id).execute()
      }
    }
  }

}
