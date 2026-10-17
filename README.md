# Pioneer Delivery Algorithm Portfolio - Part I

## Language

Java 17 or newer. The project uses only the Java standard library.

## Compile and run
Compile and run Main.java
console terminal will show results
## Input format

D01 10 13

formatted by id, start, finish
## Organization

`src/Main.java` contains input parsing and separate methods for Tasks A-D. The source code includes comments explaining the main data structures and greedy choices. `data/deliveries.txt` is the sample data file. 

## Algorithms

### Task A
The greedy choice is to fit the most ammount of compatible deliveries by sorting the deliverys. This is optimal as it gives us the most amount of time for more deliveries Sorting dominates the running time, so the complexity is O(n log n).

### Task B

Sort deliveries by start time. a min heap is there to show when a driver becomes available. For each delivery reuse the driver with the earliest availability when that time is no later than the delivery's start, otherwise create a new driver. The heap's earliest availability only introduces a new driver when needed. The complexity is O(n log n).

### Task C

This uses the same logic as Task A. If the maximum compatible schedule contains k deliveries, then all package - k deliveries must be rejected. It uses Task A and therefore runs in O(n log n).

### Task D

Counterexample:

| Delivery | Ready time | Profit |
|---|---:|---:|
| A | 0 | 3 | 
| B | 0 | 4 |
| C | 1 | 2 |

The proposed greedy order is B, A, C. It schedules B at time 0, A at time 1, and C at time 2, giving V(G)=4/1 + 3/2 + 2/2 = 6.5. A better schedule is B at time 0, C at time 1, and A at time 2, giving V(S)=4/1 + 2/1 + 3/3 = 7.0. Therefore, the proposed greedy strategy is not always optimal.

## Required test cases

## Task A expected results

| File | Expected maximum completed |
|---|---:|
| `taskA_case1.txt` | 0 |
| `taskA_case2.txt` | 1 |
| `taskA_case3.txt` | 3 |
| `taskA_case4.txt` | 1 |
| `taskA_case5.txt` | 3 |
| `taskA_case6.txt` | 2 |

## Task B expected results

| File | Expected minimum drivers |
|---|---:|
| `taskB_case1.txt` | 0 |
| `taskB_case2.txt` | 1 |
| `taskB_case3.txt` | 1 |
| `taskB_case4.txt` | 3 |
| `taskB_case5.txt` | 2 |
| `taskB_case6.txt` | 2 |

## Task C expected results

| File | Expected minimum rejected |
|---|---:|
| `taskC_case1.txt` | 0 |
| `taskC_case2.txt` | 0 |
| `taskC_case3.txt` | 0 |
| `taskC_case4.txt` | 2 |
| `taskC_case5.txt` | 1 |
| `taskC_case6.txt` | 1 |
