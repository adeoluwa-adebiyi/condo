package actions

import play.api.mvc.{Action, BodyParser, Request, Result}

import scala.concurrent.{ExecutionContext, Future}

class Authentication[A] extends Action[A]{
  override def parser: BodyParser[A] = ???

  override def apply(request: Request[A]): Future[Result] = ???

  override def executionContext: ExecutionContext = ???
}
