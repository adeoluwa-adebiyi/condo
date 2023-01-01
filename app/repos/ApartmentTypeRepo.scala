package repos

import anorm._
import db.models.ApartmentType
import models.{ApartmentType => DApartmentType}
import play.api.db.Database

import javax.inject.{Inject, Singleton}

@Singleton
class ApartmentTypeRepo @Inject() (
                                  val database: Database
                                  ){

  val parser: RowParser[Map[String, Any]] =
    SqlParser.folder(Map.empty[String, Any]) { (map, value, meta) =>
      Right(map + (meta.column.qualified -> value))
    }

  def create(apartmentTypeData: DApartmentType): Option[ApartmentType] = {
    database.withConnection {
      implicit c => {
          val typeMap = SQL(
            """
              INSERT INTO apartment_type(type) VALUES({type}) RETURNING *
              """).on(
            "type" -> apartmentTypeData._type
          ).as(parser.+).head
        Some(ApartmentType(
          id = Some(typeMap("apartment_type.id").asInstanceOf[Int]),
          _type = Some(typeMap("apartment_type.type").toString()),
          ref = Some(typeMap("apartment_type.ref").toString())
        ))
      }
    }
  }

  def getAll(): Option[List[ApartmentType]] = {
    database.withConnection {
      implicit c => {
        val _types = SQL(
          """
            SELECT type,ref FROM apartment_type
            """.stripMargin).as(parser.*)
        println(_types)
        Some(_types.map(_type => {
          ApartmentType(
            _type = Some(_type("apartment_type.type").asInstanceOf[String]),
            ref = Some(_type("apartment_type.ref").toString)
          )
        }))
      }
    }
  }
}
