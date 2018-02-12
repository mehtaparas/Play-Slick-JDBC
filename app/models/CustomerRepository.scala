package models

import javax.inject.{ Inject, Singleton }
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.{ Future, ExecutionContext }

/**
 * A repository for people.
 *
 * @param dbConfigProvider The Play db config provider. Play will inject this for you.
 */
@Singleton
class customerRepository @Inject() (dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  
  // We want the JdbcProfile for this provider
  
  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  // These imports are important, the first one brings db into scope, which will let you do the actual db operations.
  // The second one brings the Slick DSL into scope, which lets you define the table and other queries.
  
  import dbConfig._
  import profile.api._

  /**
   * Here we define the table. It will have a name of people
   */
  
  private class CustomerTable(tag: Tag) extends Table[Customer](tag, "customer") {

    def accountNbr = column[Int]("accountNbr")

    def video = column[Int]("video")

    def voice = column[Int]("voice")

    def data = column[Int]("data")

    /**
     * This is the tables default "projection".
     *
     * It defines how the columns are converted to and from the Person object.
     *
     * In this case, we are simply passing the id, name and page parameters to the Person case classes
     * apply and unapply methods.
     */
    
    def * = (accountNbr, video, voice, data) <> ((Customer.apply _).tupled, Customer.unapply)
  }

  /**
   * The starting point for all queries on the people table.
   */

private val customer = TableQuery[CustomerTable]

  /**
   * List all the people in the database.
   */
  
  def getCustomer(accountNbr: Int): Future[Option[Customer]] = db.run {
    customer.filter(_.accountNbr === accountNbr).result
  }

}
