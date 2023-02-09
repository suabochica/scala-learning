import pos.model.{CrustSize, CrustType, Topping}
import Money

case class ToppingPrices(
                          name: Topping,
                          price: Money
                        )

case class CrustTypePrices(
                            crustType: CrustType,
                            price: Money
                          )

case class CrustSizePrices(
                            crustSize: CrustSize,
                            price: Money
                          )
