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

This how ths game works:

- The computer is going to flip a virtual coin.
- You're going to guess whether that result is heads or tails.
- You can play the game for as manu flips as you want.
- After each flip the output will look like this:

```
Flip was Tails. #Flips: 3, #Correct: 2
```

So, we have the next things about the game state:

- We need to track how many coin flips there are.
- We need to track how many guesses the player made correctly.

So, let's start working on the game code.

## Game pseudocode

We will need some sort of main loop, and in the imperative world we could have the next approach:

```
var input = ""
while (input != "q")
    // 1. Print the player to select heads, tails, or quit
    // 2. Get the player's input
    if (input == "q")
        // a. Print the game summary
        // b. Quit
    // 3. Flip the coin
    // 4. See if the player guessed correctly
    // 5. Print the #flips and #correct
```

## I/O functions

Thanks for the pseudocode, below are the I/O functions we are going to need.
- A "show prompt" function
- A "get user input" function
- A function to print the number of lips and correct answers
