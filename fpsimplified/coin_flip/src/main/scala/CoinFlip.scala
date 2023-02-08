package scala.main

import CoinFlipUtils.{
  showPrompt,
  getUserInput,
  printableFlipResult,
  printGameState,
  printGameOver
}
import scala.annotation.tailrec
import scala.util.Random

case class GameState(numFlips: Int, numCorrect: Int)

object CoinFlip extends App {
  val r = Random
  val s = GameState(0, 0)

  mainLoop(s, r)

  @tailrec
  def mainLoop(gameState: GameState, random: Random) = {
    showPrompt()
    val userInput = getUserInput()

    // handle result
    userInput match {
      case "H" | "T" => {
        val coinTossResult = tossCoin(random)
        val newNumFlips = gameState.numFlips + 1

        if (user == coinTossResult) {
          val newNumCorrect = gameState.numCorrect + 1
          val newGameState = gameState.copy(numFlips = newNumFlips)

          if (userInput == coinTossResult) {
            val newNumCorrect = gameState.numCorrect + 1
            val newGameState = gameState.copy(numFlips = newNumFlips, numCorrect = newNumCorrect)

            printGameState(printableFlipResult(coinTossResult), newGameState)
            mainLoop(newGameState, random)
          } else {
            val newGameState = gameState.copy(numFlips = newNumFlips)

            printGameState(printableFlipResult(coinTossResult), newGameState)
            mainLoop(newGameState, random)
          }
        }
        case _ => {
          printGameOver()
          printGameState(gameState)
          // return out of the recursion here
        }
      }
    }
  }
}