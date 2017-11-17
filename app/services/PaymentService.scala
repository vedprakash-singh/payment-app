package services

import java.util.UUID
import javax.inject._

import dao.PaymentDAO
import domains.{Payment, PaymentForm}
import handlers.PaymentHandler
import play.api.{Configuration, Logger}
import play.api.http.Status
import play.api.libs.json.JsValue
import play.api.libs.ws.WSResponse

import scala.concurrent.{ExecutionContext, Future}

@Singleton()
class PaymentService @Inject()
(paymentDAO: PaymentDAO, paymentHandler: PaymentHandler, config: Configuration)
(implicit executionContext: ExecutionContext) {

  lazy val API_PUBLIC_KEY: String = config.get[String]("primoris.payment.api.public.key")
  lazy val API_PRIVATE_KEY: String = config.get[String]("primoris.payment.api.private.key")
  lazy val PAYMENTS_DIRECT_URL: String = config.get[String]("primoris.payment.api.direct")
  lazy val PAYMENTS_CONFIRMATION_URL: String = config.get[String]("primoris.payment.api.confirmation")

  def paymentHistory(): Future[Seq[Payment]] = paymentDAO.all()

  def processPaymentRequest(paymentForm: PaymentForm): Future[Either[List[String], Payment]] = {
    paymentHandler.postCall(PAYMENTS_DIRECT_URL, populateDataMap(paymentForm)).flatMap { apiResponse =>
      Logger.info(s"API Status: ${apiResponse.statusText}, body: ${apiResponse.body}")
      apiResponse.status match {
        case Status.OK => storePaymentHistory(paymentForm, apiResponse)
        case _ => populateErrorResponse(apiResponse)
      }
    }.recover {
      case ex: Exception =>
        Logger.error("Exception: ", ex)
        Left(List(ex.getMessage))
    }
  }

  private def storePaymentHistory(form: PaymentForm, apiResponse: WSResponse): Future[Either[List[String], Payment]] = {
    parsePaymentDataFromResponse(form, apiResponse) match {
      case Right(payment) => paymentDAO.insert(payment).map(id => Right(payment.copy(id = Some(id))))
      case Left(errors) => Future.successful(Left(errors))
    }

  }

  private def parsePaymentDataFromResponse(paymentForm: PaymentForm, apiResponse: WSResponse): Either[List[String], Payment] = {
    val data: JsValue = apiResponse.json("data")
    val errors = try {
      data("errors").asOpt[List[String]].getOrElse(Nil)
    } catch {
      case ex: Exception => data("error").asOpt[List[String]].getOrElse(Nil)
    }

    errors match {
      case Nil => Right {
        Payment(
          None,
          data("payment_name").as[String],
          data("amount").as[String].toDouble,
          paymentForm.cardNo,
          paymentForm.expM,
          paymentForm.expY,
          paymentForm.cvvNo,
          data("payment_token").as[String],
          data("payment_token_status").as[String],
          data("payment_date").as[String],
          data("primoris_fee").as[String].toDouble,
          data("amount_total").as[String].toDouble,
          data("trans_id").as[String]
        )
      }
      case _ => Left(errors)
    }

  }

  private def populateErrorResponse(apiResponse: WSResponse): Future[Either[List[String], Payment]] = {
    val data = apiResponse.json("data")
    Future.successful(Left(data("errors").as[List[String]]))
  }

  private def populateDataMap(paymentForm: PaymentForm): Map[String, Seq[String]] = {
    Map(
      "key" -> Seq(API_PUBLIC_KEY),
      "private_key" -> Seq(API_PRIVATE_KEY),
      "name" -> Seq(paymentForm.name),
      "address" -> Seq("123 Main St"),
      "state" -> Seq("UK"),
      "zip" -> Seq("55555"),
      "account_type" -> Seq("card"),
      "card_number" -> Seq(paymentForm.cardNo.toString),
      "expiration_date" -> Seq(s"${paymentForm.expM}${paymentForm.expY}"),
      "cvv2" -> Seq(paymentForm.cvvNo.toString),
      "amount" -> Seq(paymentForm.amount),
      "prepaid" -> Seq("no"),
      "customer_identifier" -> Seq(UUID.randomUUID().toString),
      "user_identifier" -> Seq(UUID.randomUUID().toString)
    )
  }

}
