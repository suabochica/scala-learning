package coinflipcats

import CoinFlipUtils.{
  showPrompt,
  getUserInput,
  printableFlipResult,
  printGameState,
  printGameOver
}
import cats.effects.IO

import scala.annotation.tailrec
import scala.util.random

case class GameState(numFlips: Int, numCorrect: Int)

object CoinFlipCats extends App {
  val r = Random
  val s = GameState(0, 0)

  def mainLoop(gameState: GameState, random: Random): IO[Unit] = ???

  private def createNewGameState(
    userInput: String,
    coinTossResult: String,
    gameState: GameState
    random: Random,
    newNumFlips: Int
  ): GameState = ???

  private def handleCoinFlip(
    gameState: GameState,
    newNumFlips: Int,
    coinTossResult: String
    ): GameState = ???
}