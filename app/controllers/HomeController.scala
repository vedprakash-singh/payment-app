package controllers

import javax.inject._

import domains.PaymentForm
import play.api.Logger
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc._
import services.PaymentService

import scala.concurrent.{ExecutionContext, Future}

/**
  * This controller creates an `Action` to handle HTTP requests to the
  * application's home page.
  */
@Singleton
class HomeController @Inject()
(cc: ControllerComponents, implicit val ec: ExecutionContext)
(paymentService: PaymentService) extends AbstractController(cc) {

  /**
    * Create an Action to render an HTML page.
    *
    * The configuration in the `routes` file means that this method
    * will be called when the application receives a `GET` request with
    * a path of `/`.
    */
  def index() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index("Primoris | Payment | APP"))
  }

  def doPayment = Action.async { implicit request: Request[AnyContent] =>
    paymentForm.bindFromRequest.fold(
      formWithErrors => {
        // binding failure, you retrieve the form containing errors:
        Logger.error("form containing errors" + formWithErrors)
        Future.successful {
          Redirect(routes.HomeController.index()).flashing("ERROR" -> "Please check the required details below!")
        }
      },
      paymentFormData => {
        /* binding success, you get the actual value. */
        Logger.info("binding success : " + paymentFormData)
        paymentService.processPaymentRequest(paymentFormData).map { wsResponse =>
          Logger.info("wsResponse : " + wsResponse)
          wsResponse match {
            case Right(payment) => Ok(views.html.reciept("Primoris | Payment | APP", payment))
            case Left(errors) => Redirect(routes.HomeController.index()).flashing("ERROR" -> errors.mkString(","))

          }
        }
      }
    )
  }

  def histories() = Action.async { implicit request: Request[AnyContent] =>
    paymentService.paymentHistory().map { payments =>
      Ok(views.html.histories("Primoris | Payment | APP", payments))
    }
  }

  val paymentForm = Form(mapping(
    "amount" -> nonEmptyText,
    "name" -> nonEmptyText,
    "cardNo" -> longNumber,
    "expM" -> number,
    "expY" -> number,
    "cvvNo" -> number)
  (PaymentForm.apply)(PaymentForm.unapply))

}
