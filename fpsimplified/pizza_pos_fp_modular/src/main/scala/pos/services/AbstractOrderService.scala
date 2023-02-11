package pos.services

import Money
import pos.model.{ CrustSize, CrustType, Order, Topping }

trait AbstractOrderService extends OrderServiceInterface {
  // create a concrete implementation of the trait so we
  // can use its `calculatePizzaPrice` function
  object PizzaService extends PizzaService
  import PizzaService.calculatePizzaPrice

  // all implementation of this trait will use these functions
  private lazy val toppingPricesMap = database.getToppingPrices()
  private lazy val crustSizePricesMap = database.getCrustSizePrices()
  private lazy val crustTypePricesMap = database.getCrustTypePrices()

  def calculateOrderPrice(o: Order): Money =
    calculateOrderPriceInternal(o, toppingPricesMap, crustSizePricesMap, crustTypePricesMap)

  private def calculateOrderPriceInternal(
                                         o: Order,
                                         toppingPrices: Map[Topping, Money],
                                         crustSizePrices: Map[CrustSize, Money],
                                         crustTypePrices: Map[CrustType, Money],
                                         ): Money = {
    val pizzaPrices: Seq[Money] = for {
      pizza <- o.pizzas
    } yield {
      calculatePizzaPrice(
        pizza,
        toppingPrices,
        crustSizePrices,
        crustTypePrices
      )
    }
    pizzaPrices.sum
  }
}