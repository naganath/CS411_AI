# CS411_AI
This repository contains various AI problems and algorithms implemented to mimic the real world problems and its solution. Different kind of real world problems are considered suiting different environments in AI. 

The real world problems that are implemented are 
  1. N-Puzzle Solver 
    This is actually a game where there is N * N size blocks with (N * N) -1  tiles from 1 to (N*N)-1. These tiles are randomly arranged. The goal of the puzzle is to arrange these in the ascending order starting from top left and moving towards the right. 
    It is a deterministic discrete environment where there is a single agent trying to find the goal state or the solved answer for the puzzle. Different Algorithms are implemented are for it to understand the pros and cons of each. BFS, Iterative Deepening Search, A-Star and Iterative A-Star are tried as each approach has some advantage over the other. The approach to the problem can be decided on other factors such as memory consumption, fastest available solution, knowledge of the problem etc. 
    
  2. Optimal Path Finding Bot
    Here a bot is moving inside a closed space with exits. For each movement of the bot there is a penalty and there are certain points inside the closed block that gives reward points. Actions of the bot is not deterministic. Each action has a probability that it is properly executed upon. The goal to the bot is take the right decision considering the conditions and try to maximise its performance. 
    This is a stochastic sequential environment. Bot is actually utility based agent. By implementing MDP ( Markov Decision Process ) with Bellman equation, the algorithm tries to figure out the optimal policy that can be used by the Bot to navigate through the closed space. Policy might decides the action of the bot in each its the state and tries to optimise the performance.
    
  3. Decision Tree
    Implemented a simple decision tree that takes as input a vector of attribute value pair  and returns a choice / decision  as a single output value. Set of examples are considered as a training example that helps in building the decision tree. Once the decision tree is built, for any new example it will be able to predict the output. Its a kind of supervised learning where the system learns from the exams. Once enough examplares are given to train the system, it would be able to predict the output of any new vector of atrribute value pairs. 
