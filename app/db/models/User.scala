package db.models

import java.util.Date

case class User(
                 val id: Option[Long] = None,
                 val firstname: Option[String] = None,
                 val lastname: Option[String] = None,
                 val dob: Option[String] = None,
                 val ref: String
               )
