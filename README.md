# AI-team26

Enums we have
Moveset (forward, turn, bash, demolish)
Heuristics (H1 - H6)

Classes we might need:
  Coordinate
    Fields
      X and Y (int)
    Methods
      Maybe a getNeighbor that takes one of {up, left, right, down}
World
  Board (represented with terrain numbers, and S and G are known)
    S: a coordinate or index where algorithm starts
    G: a coordinate or index where algorithm ends
Agent
  FIelds
    Score
    Time / Timer / Counter
      Do we need this?  Honestly i think the score can work as a timer (of sorts)
  Methods
    Movement
      Moveset: make it an enum? There’s only 4 moves
        Forward
        Turn
        Bash
        Demolish
    A-Star
      Load the heuristic of your choice
      Call the heuristic as a function
      came_from - HashMap<Coordinate, Moveset>
      Pair (Coordinate c, Integer heuristic)
      Frontier / queue - PriorityQueue
