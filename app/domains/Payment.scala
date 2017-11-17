package domains

case class Payment
(
  id: Long,
  name: String,
  amount: Double,
  cardNo: Long,
  expMMYY: Int,
  cvvNo: Int,
  token: String,
  status: String,
  date: String,
  fee: Double,
  total: Double,
  transId: String
){

}
