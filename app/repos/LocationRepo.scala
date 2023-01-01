package repos

import akka.http.scaladsl.model.headers.Connection
import anorm._
import db.models.Location
import models.{Location => DLocation}
import play.api.db.Database

import javax.inject.{Inject, Singleton}

@Singleton
class LocationRepo @Inject() (val database: Database){

  val parser: RowParser[Map[String, Any]] =
    SqlParser.folder(Map.empty[String, Any]) { (map, value, meta) =>
      Right(map + (meta.column.qualified -> value))
    }

  def create(locationData: DLocation): Option[Location] = {
      database.withConnection {
        implicit  c =>
        val locationResult = SQL(
          """
          INSERT INTO location(address, city, postcode, country) VALUES({address},{city},{postcode},{country}) RETURNING *
          """.stripMargin).on(
          "address" -> locationData.address.get,
                "city" -> locationData.city.get,
                "postcode" -> locationData.postcode.get,
                "country" -> locationData.country
        ).as(parser.+).head
        Some(
          Location(
          ref = Some(locationResult("location.ref").toString()),
          country = Some(locationResult("location.country").toString()),
          id = Some(locationResult("location.id").asInstanceOf[Long]),
          address = Some(locationResult("location.address").toString()),
          postcode = Some(locationResult("location.postcode").toString()),
          city = Some(locationResult("location.city").toString()),
          longitude = Some(locationResult("location.longitude").asInstanceOf[Double]),
          latitude = Some(locationResult("location.latitude").asInstanceOf[Double])
        ))
      }
  }

  def getAll(): Option[List[Location]] = {
    database.withConnection {
      implicit c =>
      val allLocations = SQL(
        """
          SELECT * FROM location
          """.stripMargin).as(parser.*)
        Some(allLocations.map(json => Location(
          ref = Some(json("location.ref").toString),
          country = Some(json("location.country").toString),
          id = Some(json("location.id").asInstanceOf[Long]),
          address = Some(json("location.address").toString),
          postcode = Some(json("location.postcode").toString),
          city = Some(json("location.city").toString),
          longitude = Some(json("location.longitude").asInstanceOf[Double]),
          latitude = Some(json("location.ref").asInstanceOf[Double])
        )))
    }
  }
}
