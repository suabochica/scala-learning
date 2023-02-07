package real_oop

import common._
import scala.collection.mutable.ArrayBuffer

class Pizza {
  type Money = BigDecimal

  // private fields
  private var _crustSize: CrustSize = MediumCrustSize
  private var _crustType: CrustType = RegularCrustSize
  private var _toppings = ArrayBuffer[Topping]()

  def getCrustSize = _crustSize
  def setCrustSize(cs: CrustSize): Unit = {
    _crustSize = cs
  }

  def getCrustType = _crustType
  def setCrustType(ct: CrustType) {
    _crustType = ct
  }

  def removeTopping(t: Tooping) {
    _toppings -= t
  }

  def removeAllToppings(): Unit = {
    _toppings.clear
  }

  def getPrice(): Money = ???
}