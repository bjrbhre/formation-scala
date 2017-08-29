package exercices.web.controllers

import com.twitter.finagle.http.Request
import com.twitter.finatra.http.Controller
import com.twitter.finatra.json.FinatraObjectMapper
import exercices.web.database.KeyValueStore
import exercices.web.domain.{User, UserNoId}
import exercices.web.utils.Extensions._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.Try

class UserController(store: KeyValueStore[User.Id, User], mapper: FinatraObjectMapper) extends Controller {
  /**
    * Endpoint to retrieve all users
    *
    * Tip: look at 'store' available methods
    * Tip2: finatra controllers want twitter Future instead of scala Future, you should transform them (see utils.Extensions)
    */
  get("/api/users") { _: Request =>
    (for {
      ids <- store.keys()
      users <- Future.sequence(ids.map(k => store.read(k)))
    } yield users.flatten).map(response.ok.json).toTwitter.rescue(handleError)
  }

  /**
    * Endpoint to create a new user
    *
    * Tip: FinatraObjectMapper is able to parse String into case classes
    */
  post("/api/users") { req: Request =>
    (for {
      userNoId <- Try(mapper.parse[UserNoId](req.contentString)).toFuture
      user = userNoId.generate
      _ <- store.create(user.id, user)
    } yield user.id).map(response.ok.json).toTwitter.rescue(handleError)
  }

  /**
    * Endpoint to retrieve a user given its id
    *
    * Tip: look at 'Request' methods
    */
  get("/api/users/:id") { req: Request =>
    (for {
      id <- Try(User.Id(req.getParam("id"))).toFuture
      userOpt <- store.read(id)
    } yield userOpt).map(_.map(response.ok.json).getOrElse(response.notFound)).toTwitter.rescue(handleError)
  }

  /**
    * Endpoint to update an existing user
    */
  put("/api/users/:id") { req: Request =>
    (for {
      id <- Try(User.Id(req.getParam("id"))).toFuture
      userNoId <- Try(mapper.parse[UserNoId](req.contentString)).toFuture
      user = userNoId.withId(id)
      _ <- store.update(id, user)
    } yield user.id).map(response.ok.json).toTwitter.rescue(handleError)
  }

  /**
    * Endpoint to delete a user
    */
  delete("/api/users/:id") { req: Request =>
    (for {
      id <- Try(User.Id(req.getParam("id"))).toFuture
      res <- store.delete(id)
    } yield res).map(response.ok.json).toTwitter.rescue(handleError)
  }
}
