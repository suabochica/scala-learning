# Coin Flip Game

This is a functional fame with a bit of state. The goals with this example are:

- To write a functional application
- Show an example of how to handle state in a Scala/FP application

To get starting using state in a Scala application, let's build a game that you can play at the command line. The application will flip a coin (virtual coin), and as the player, you goal is to guess whether the result is heads or tails. The computer will keep track of the total number of flops and the number of correct guesses.

When you start the game, you will see this command-line prompt:

```bash
(h)eads, (t)ails, or (q)uit: _
```

Then the application prompts you for your guess. Enter `h` for heads, `t` for tails, or `q` to quit the game. If you enter `h` or `t`, the application will flip a virtual coin, then let you know if your guess was correct or not.

As an example of how it works, Lets play the game and made three guesses. The input, and the output of that session looks like this:

```
(h)eads, (t)ails, or (q)uit:  h
Flip was Heads. #Flips: 1, #Correct: 1

(h)eads, (t)ails, or (q)uit:  h
Flip was Tails. #Flips: 2, #Correct: 1

(h)eads, (t)ails, or (q)uit:  t
Flip was Tails. #Flips: 3, #Correct: 2

(h)eads, (t)ails, or (q)uit:  q
=== Game Over ===
#Flip: 3, #Correct: 3
```
Admittedly this is not the most exciting game in the world, but it turns out to be a nice way to learn how to handle immutable state in a Scala/FP application.

> Note: The input/output in this game will not be handled in a functional way. Let's will that in the `coin_flip_cats` example

## Coin Flip game state