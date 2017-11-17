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

  def doLogin = Action.async { implicit request: Request[AnyContent] =>
    paymentForm.bindFromRequest.fold(
      formWithErrors => {
        // binding failure, you retrieve the form containing errors:
        Logger.error("form containing errors" + formWithErrors.errors)
        Future.successful(BadRequest(views.html.index("Primoris | Payment | APP")))
      },
      paymentFormData => {
        /* binding success, you get the actual value. */
        Logger.info("binding success : " + paymentFormData)
        paymentService.processPaymentRequest(paymentFormData).map { wsResponse =>
        Logger.info("wsResponse : " + wsResponse)
          wsResponse match {
            case Right(payment) => Ok(payment.toString)
            case Left(error) => Ok(error.mkString)
          }
        }
      }
    )
  }

  val paymentForm = Form(mapping(
    "amount" -> nonEmptyText,
    "name" -> nonEmptyText,
    "cardNo" -> longNumber,
    "expMMYY" -> number,
    "cvvNo" -> number)
  (PaymentForm.apply)(PaymentForm.unapply))

}
