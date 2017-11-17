package domains

case class PaymentForm
(
  amount: String,
  name: String,
  cardNo: Long,
  expMMYY: Int,
  cvvNo: Int
) {

}
