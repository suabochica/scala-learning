# Wikigraph
In this assignment we will implement an algorithm and all the tools to compute the distance between wikipedia articles

## Introduction

But what is the **distance** between two pages? We can consider each Wikipedia article (represented by its id) to be a node, of the graph. The links from one page to another one can be interpreted as the edges of this directed graph. Therefore the distance between page A and page B is the number of links that a user needs to click on to reach page B from page A.

## Overview
The first part of the assignment consists in finishing the implementation of the WikiResult[A] type in the WikiResult.scala file. This type used to describe the result of an asynchronous computation which will produce a value of type A or fail with an error of type WikiError.

Note however that this type is based on Future[Either[Seq[WikiError], A]] which means that you have to take into consideration two kinds of errors:

- _domain errors_ which are represented by the Seq[WikiError] type. The future will be a Success(Left(error: Seq[WikiError]))
- *system failure* which are represented by the type WikiException (which is a subclass of Throwable). The future will be a Failure(failure: WikiException)

The Left variant contains a Seq because methods such as zip and traverse collect all the errors from the multiple WikiResult which they combine.

To complete this part you will need to use the common operators on Either and Future such as map and flatMap as well as combinators on Future such as traverse.

The second part of the assignment uses what was built in the first one. In particular, you are asked to implement three functions which consume and produce WikiResult in the Wikigraph.scala file. Note that client: Wikipedia is in scope as argument of the Wikigraph constructor. This object allows you to query the Wikipedia dataset using three simple methods.

The implementation of breadthFirstSearch requires some more details. This method uses a [breadth first search (BFS)](https://en.wikipedia.org/wiki/Breadth-first_search) on the graph of articles to compute the distance between two pages. The interaction with the article store will be asynchronous and concurrent.

### BFS Algorithm recap

The skeleton of the recursive method iter is provided to you. To fill in the rest, remember that BFS iteration first explores all the neighbors of a node and after that, it moves on to the neighbors of each neighbor.

To do so the algorithm relies on a set of visited nodes to remember which nodes where already explored and therefore avoid being stuck in infinite loops around graph cycles (for example A -> B -> C -> A). It also maintains a (priority) queue which contains the nodes to visit next. In this case, we modified this queue to contain not only the ID of the next nodes to analyze, but also the distance of the node from the start of the search. Finally, the implementation of breadthFirstSearch in this assignment also receives a maxDepth argument which describes the threshold to respect for the exploration.

The algorithm proceeds as follows:

When no node is left to visit or when we exceed maxDepth, the search is considered failed and completes with a None

Otherwise, we retrieve the next node to visit (and its distance from the start of the search) and its neighbors. If the destination is amongst the neighbors, our search is complete, and we can return the distance. Otherwise, we add the neighbors to the queue with the associated distance and we perform a recursive call.

## Download the dataset

To be able to test your code without Internet access, we have assembled for you a small dataset. This small graph contains all the pages that are at distance 2 from the Scala (programming language) article. The dataset might be incorrect or out of date and should be used only for testing.

You can download the file from [here](https://moocs.scala-lang.org/~dockermoocs/effective-scala/smallwiki.db)

Please put the file inside a directory named `enwiki-datase`t, in the project root directory. The path to the dataset (relative to the project root directory) should be `enwiki-dataset/smallwiki.d`b so that it matches the filename used in `src/main/resources/application.conf`.

