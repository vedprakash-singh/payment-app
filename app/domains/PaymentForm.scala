package domains

case class PaymentForm
(
  amount: String,
  name: String,
  cardNo: Long,
  expM: Int,
  expY: Int,
  cvvNo: Int
) {

}
