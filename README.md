# Greed Puzzle Solver

## What is Greed?

Greed is a puzzle where you are given a grid of numbers between 1 through 9 and a marker.  Given
the initial placement of the marker on the grid you can move north, east, south, or west.
In the chosen direction, the number directly adjacent to the marker is the number of spaces
the marker will move.  The number of spaces you travelled will then accumulate to your score.
You continue picking directions until finally you reach a dead end.
Once you have hit a dead end, the game is over.  You can not move backwards, nor can you travel across a path that was taken previously.  The objective of the game is to get the maximum score.  Hence, the name of the game is Greed.

## What is the Greed Puzzle Solver?

The Greed Puzzle Solver will solve the greed puzzle, that is, tell you the maximum score that can be
obtained and tell you the directions to take in order to obtain the maximum score.  You can solve
the puzzle using a single threaded solution or a multi-threaded solution.  The multi-threaded solution
is roughly 2x faster than the single threaded solution.  Be weary of larger grids, because the time to solve
is at worst 4+3^(n-1), which is O(3^n) and therefore non-polynomial a.k.a exponential. Where n = r*c, r = grid rows,
c = grid columns.

## How to use Greed Puzzle Solver

### Build

Once downloaded, you can build the puzzle solver by running

```
mvn compile assembly:single
```

The above will generate the following file: greed-puzzle-solver-0.0.1-SNAPSHOT-with-dependencies.jar.  

### Run

If you have a greed data file (a sample is given), then you can run the puzzle solver with the following command:

```
java -jar greed-puzzle-solver-0.0.1-SNAPSHOT-jar-with-dependencies.jar <solve type> <file name> <# rows> <# cols>
```

There are currently three solve types, which are "greed.solver.SimpleGreedSolver", "greed.solver.ThreadedGreedSolver", and "greed.solver.GreedyGreedSolver".  The simple greed solver is a single threaded solver. The threaded greed solver is a multi-threaded puzzle solver.  The greedy greed solver is a greedy algorithm implementation, which chooses the local maximum score yielding direction.  Note: Greed is not applicable to a greedy algorithm,
which implies that it will not yield the correct maximum score.  It has been added merely for amusement and observation.

File name is the path of the file containing the greed data.  The following is a sample of what a greed data file contains:

```
166352597853863
99855492441@778
831662622557818
386269683764736
988979117859264
676747194241555
166992128281412
223769239853448
917317149319135
926913427444698
```

You can see the marker is denoted by an @ symbol.

Rows is the number of rows of the greed grid in the file whereas cols is the number of columns of the greed grid in the file.

### Sample Output

The following is the sample output for a 10x15 Greed Grid using the multi-threaded puzzle solver.
```
Solved Grid:
166352597853863
99855492441@778
831662622557818
386269683764736
988979117859264
676747194241555
166992128281412
223769239853448
917317149319135
926913427444698

Time: 0.013 seconds
Max Score: 64
Directions: [SOUTH, WEST, WEST, NORTH, NORTH, WEST, SOUTH, SOUTH, WEST, NORTH, EAST, EAST, SOUTH, EAST, NORTH, WEST, SOUTH, WEST, 
NORTH, WEST, NORTH]
```



