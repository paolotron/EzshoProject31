# Project Estimation  
Authors:
Date:
Version:
# Contents
- [Estimate by product decomposition]
-Identify products         
-Estimate effort per product
-Aggregate


- [Estimate by activity decomposition ]
-Identify activities (decomposition of activities in subactivities) 
-Estimate effort per activity
-Aggregate


# Estimation approach
<Consider the EZGas  project as described in YOUR requirement document, assume that you are going to develop the project INDEPENDENT of the deadlines of the course>
# Estimate by product decomposition
### 
|             | Estimate                        |             
| ----------- | ------------------------------- |  
| NC =  Estimated number of classes to be developed   |           45                 |             
|  A = Estimated average size per class, in LOC       |           100                 | 
| S = Estimated size of project, in LOC (= NC * A) |        4500          |
| E = Estimated effort, in person hours (here use productivity 10 LOC per person hour)  |    450   |   
| C = Estimated cost, in euro (here use 1 person hour cost = 30 euro) |  13500  | 
| Estimated calendar time, in calendar weeks (Assume team of 4 people, 8 hours per day, 5 days per week ) |       3 weeks         |              
# Estimate by activity decomposition
### 
|         Activity name    | Estimated effort (person hours)   |             
| ----------- | ------------------------------- | 
| Requirement activities | 80  |
| Review Existing Architectures | 20 | 
| Design activities | 40 |
| Verification Activities| 30 |
| Coding | 100 |
| Software Testing | 100 |
| Hardware Testing | 40 |
| Project Management | 20 |
| Hardware installation at customer location | 20 |
###
Insert here Gantt chart with above activities

``` plantuml
@startgantt
scale 1.5
[Requirement activities] lasts 3 days 
then [Review existing architectures] lasts 1 days 
then [Design activities] lasts 2 days
then [Verification Activities] lasts 2 days
then [Coding] lasts 4 days
then [Software Testing] lasts 4 days
then [Hardware Testing] lasts 2 days
then [Hardware installation at customer location] lasts 1 days
[Project Management] lasts 21 days
@endgantt
```
