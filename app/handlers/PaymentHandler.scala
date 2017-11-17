package handlers

import javax.inject.Inject

import play.api.Logger
import play.api.libs.ws.{WSClient, WSResponse}

import scala.concurrent.Future

class PaymentHandler @Inject()(ws: WSClient) {

  def getCall(apiUrl: String): Future[WSResponse] = {
    Logger.info(s"URL: $apiUrl")
    ws.url(apiUrl).get()
  }

  def postCall(apiUrl: String, dataMap: Map[String, Seq[String]]): Future[WSResponse] = {
    Logger.info(s"URL: $apiUrl, data: $dataMap")
    ws.url(apiUrl).post(dataMap)
  }

}
