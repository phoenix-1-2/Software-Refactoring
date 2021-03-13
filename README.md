# Software Refactoring

# jMetalMSA: a framework for solving Multiple Sequence Alignment problems with Multi-Objective metaheuristics

jMetalMSA is an Open source software tool aimed at solving multiple sequence alignment (MSA) problems by using multi-objective metaheuristics. It is based on the jMetal multi-objective framework, which is extended with an encoding for representing MSA solutions. 





## Architecture of jMetalMSA

![alt tag](https://github.com/jMetal/jMetalMSA/blob/master/architecture/jmetalmsaarchitecture.png)

The object-oriented architecture of jMetalMSA is shown in Figure above, is composed of four core classes 
(Java interfaces). Three of them (MSAProblem, MSAAlgorithm, and MSASolution) inherits from their
counterparts in [jMetal](https://github.com/jMetal/jMetal) (the inheritance relationships are omitted in the diagram), and there is a class Score to represent a
given MSA scoring function.


## OUTPUT ON EXECUTION  
![alt tag](https://github.com/phoenix-1-2/Software-Refactoring/blob/main/assets/Output2.png)
![alt tag](https://github.com/phoenix-1-2/Software-Refactoring/blob/main/assets/Output3.png)
![alt tag](https://github.com/phoenix-1-2/Software-Refactoring/blob/main/assets/Output1.png)

## Summary of features

##List of Algorithms
The list of metaheuristics currently available in jMetalMSA include the evolutionary algorithms 
* NSGA-II [1] 
* NSGA-III [2] 
* SMS-EMOA [3]
* SPEA2 [4]
* PAES [5]
* MOEA/D [6]
* MOCell [7]
* GWASF-GA [8].

## Crossover Operator
The crossover operator is the Single-Point Crossover adapted to alignments, randomly selects a position from the parent A
by splitting it into two blocks and the parent B is tailored so that the right piece can be joined to the left piece of 
the first parent (PA1) and vice versa. Selected blocks are crossed between these two parents

## Mutation Operators
The list of mutation operators included in jMetalMSA are:
* Shift-closed gaps: Closed gaps are randomly chosen and shifted to another position.
* Non-gap group splitting: a non-gap group is selected randomly, and it is split into two groups.
* One gap insertion: Inserts a gap in a random position for each sequence.
* Two adjacent gap groups merging: Selects a random group of gaps and merge with its nearest group of gaps.
* Multiple mutation 

## Scores
The scores that are currently available in jMetalMSA are:
* Sum of Pairs
* Weighted Sum of Pairs with Affine Gaps
* Single sTRucture Induced Evaluation (STRIKE).
* Percentage of Totally Conserved Columns.
* Percentage of Non-Gaps
 
## Requirements
To use jMetalMSA the following software packages are required:
* [Java SE Development Kit 8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html?ssSourceSiteId=otnes)
* [Apache Maven](https://maven.apache.org/)
* [Git](https://git-scm.com/)
* [Strike Contacts Generator](https://github.com/cristianzambrano/strikeContactGenerator)

