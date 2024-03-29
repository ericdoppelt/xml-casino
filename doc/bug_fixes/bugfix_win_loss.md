## Description

At the end of each round, to easily communicate to the reader the results of the hand, all wagers are colored according
to their status (winners green, losers red - as configured in CSS).

## Expected Behavior

Normally, all winners and losers would get colored as expected per their status in the round.

## Current Behavior

The feature's bug is at the end of a round, hand winners and losers are colored with CSS. However, currently the 
coloring is not always correct (sometimes winners get colored as "losers", and "losers" get colored as "winners").

## Steps to Reproduce

Provide detailed steps for reproducing the issue.

 1. Play a game of "blackjack", checking all the way through
 2. Keep playing until a round where the dealer busts
 3. Notice that some hands are colored red (indicating loser) when they actually won.

## Failure Logs

See attached bug image (insert after merge with Eric).

## Hypothesis for Fixing the Bug

I believe to verify the bug, I should create an integration test and evaluate a cluster of hands with an adversary who has busted, given an appropriate Hands file for classification.

## Bug Resolution

In refactoring the code, I had mistakenly reordered the order of operations for adversary operations at the end of the game. In a haste, I had placed bet evaluation before distributing adversary cards. Under the hood, the evaluation was not consistent with what was expected (e.g. only the first two adversary cards factored into its evaluated hand), and the view showed things as expected. This bug has been since been fixed by reordering the operation of code. All tests now pass.