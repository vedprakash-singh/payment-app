package dao

import scala.concurrent.{ExecutionContext, Future}
import javax.inject._

import domains.Payment
import play.api.db.slick.DatabaseConfigProvider
import play.api.db.slick.HasDatabaseConfigProvider
import slick.jdbc.JdbcProfile

@Singleton()
class PaymentDAO @Inject()
(protected val dbConfigProvider: DatabaseConfigProvider)
(implicit executionContext: ExecutionContext)
  extends HasDatabaseConfigProvider[JdbcProfile] {

  import profile.api._

  private val Payments = TableQuery[PaymentsTable]

  private val PaymentsInc = Payments.returning(Payments.map(_.id))

  def all(): Future[Seq[Payment]] = db.run(Payments.result)

  def findById(id: Int): Future[Option[Payment]] = db.run(Payments.filter(p => p.id === id).result.headOption)

  def insert(payment: Payment): Future[Int] = db.run(PaymentsInc += payment)

  private class PaymentsTable(tag: Tag) extends Table[Payment](tag, "payments") {

    def id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)

    def name: Rep[String] = column[String]("name")

    def amount: Rep[Double] = column[Double]("amount")

    def cardNo: Rep[Long] = column[Long]("card_no")

    def expMonth: Rep[Int] = column[Int]("exp_month")

    def expYear: Rep[Int] = column[Int]("exp_year")

    def cvvNo: Rep[Int] = column[Int]("cvv_no")

    def token: Rep[String] = column[String]("token")

    def status: Rep[String] = column[String]("status")

    def date: Rep[String] = column[String]("p_date")

    def fee: Rep[Double] = column[Double]("fee")

    def total: Rep[Double] = column[Double]("total")

    def transId: Rep[String] = column[String]("trans_id")

    def * = (id.?, name, amount, cardNo, expMonth, expYear, cvvNo, token, status, date, fee, total, transId) <>
      (Payment.tupled, Payment.unapply)
  }

}
