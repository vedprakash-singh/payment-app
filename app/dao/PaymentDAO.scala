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

  def all(): Future[Seq[Payment]] = db.run(Payments.result)

  def findById(id: Long): Future[Option[Payment]] = db.run(Payments.filter(p => p.id === id).result.headOption)

  def insert(payment: Payment): Future[Unit] = db.run(Payments += payment).map { _ => () }

  private class PaymentsTable(tag: Tag) extends Table[Payment](tag, "PAYMENTS") {

    def id: Rep[Long] = column[Long]("ID", O.PrimaryKey)

    def name: Rep[String] = column[String]("NAME")

    def amount: Rep[Double] = column[Double]("AMOUNT")

    def cardNo: Rep[Long] = column[Long]("CARD_NO")

    def expMonth: Rep[Int] = column[Int]("EXP_MONTH")

    def expYear: Rep[Int] = column[Int]("EXP_YEAR")

    def cvvNo: Rep[Int] = column[Int]("CVV_NO")

    def token: Rep[String] = column[String]("TOKEN")

    def status: Rep[String] = column[String]("STATUS")

    def date: Rep[String] = column[String]("P_DATE")

    def fee: Rep[Double] = column[Double]("FEE")

    def total: Rep[Double] = column[Double]("TOTAL")

    def transId: Rep[String] = column[String]("TRANS_ID")

    def * = (id, name, amount, cardNo, expMonth, expYear, cvvNo, token, status, date, fee, total, transId) <>
      (Payment.tupled, Payment.unapply)
  }

}
