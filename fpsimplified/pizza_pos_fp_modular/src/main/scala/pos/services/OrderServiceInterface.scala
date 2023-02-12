package pos.services

import Money
import pos.database.PizzaDaoInterface
import pos.mode.{CrustSize, CrustType, Order, Topping}

trait OrderServiceInterface {
  /**
   * Implementing classes should provide their own database
   * that is an instance of the PizzaDaoInterface, such as
   * MockPizzaDao, TestPizzaDao, or PorductPizzaDao
   * @return
   */
  protected def database: PizzaDaoInterface

  def calculatorOrderPrice(o: Order): Money
}