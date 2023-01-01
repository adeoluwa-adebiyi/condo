package repos

import anorm._
import db.models.{Apartment, ApartmentType, Location, Offer, User}
import play.api.db.Database
import play.api.libs.json._

import javax.inject.{Inject, Named, Singleton}
import scala.util.Failure

@Singleton
class ApartmentRepo @Inject() (val database: Database) {

  val parser: RowParser[Map[String, Any]] =
    SqlParser.folder(Map.empty[String, Any]) { (map, value, meta) =>
      Right(map + (meta.column.qualified -> value))
    }

  def create(apartmentData: Apartment): Option[Apartment] = {
    database.withConnection {
      implicit c =>{
        val response = SQL(
          """
            SELECT id FROM (INSERT into apartment(
                owner,
                name,
                rooms,
                type,
                location)
            VALUES(
                SELECT id FROM app_user WHERE ref::text={owner},
                {name},
                {rooms},
                {type},
                {location}
            ) RETURNING *)
            SELECT
              ap.name as name, ap.rooms as rooms, owner as au.ref, ap_tp.ref as type, loc.ref as location
            FROM
            apartment ap JOIN app_user au ON au.id = id
            JOIN
            location loc ON au.location = loc.id
            JOIN
            apartment_type ap_tp ON ap_tp.id = au.type
              """.stripMargin).on(
          "owner" -> apartmentData.owner.ref,
                "name" -> apartmentData.name,
                "rooms" -> apartmentData.rooms,
                "type" -> apartmentData._type.ref,
                "location" -> apartmentData.location.ref
        ).as(parser.+)
        if(response.isEmpty){
          throw new Exception("Operation failed")
        }
        response.map(map => {
          Some(Apartment(
            User(ref = map("owner").asInstanceOf[String]),
            map("name").asInstanceOf[String],
            map("rooms").asInstanceOf[Int],
            ApartmentType(
              None,
              map("type").asInstanceOf[Option[String]],
              None
            ),
            map("ref").asInstanceOf[String],
            Location(map("location").asInstanceOf[Option[String]]),
            Some(map("has_sale").asInstanceOf[Boolean]),
          ))
        }).head
      }
    }
  }
}
