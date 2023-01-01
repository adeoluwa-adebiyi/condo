package controllers

import models.TodoListItem
import play.api.mvc.{Action, AnyContent, BaseController, ControllerComponents}

import javax.inject.{Inject, Singleton}
import scala.collection.mutable.ListBuffer
import play.api.libs.json._

@Singleton
class TodoListController @Inject() (val controllerComponents: ControllerComponents) extends BaseController{
  private val todoListItems = ListBuffer[TodoListItem]()

  todoListItems += TodoListItem(1, "Travel to Frankfurt",false)
  todoListItems += TodoListItem(1, "Eat Spaghetti Bolognese",true)

  implicit val todoListJson = Json.format[TodoListItem]

  def getAll(): Action[AnyContent] = Action {
    if (todoListItems.isEmpty) {
      NoContent
    }
      else{
      Ok(Json.toJson(todoListItems)).withHeaders({
        CONTENT_TYPE->JSON
      })
    }
  }
}
