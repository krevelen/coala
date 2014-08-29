// Agent sample_agent in project jasontest

/* Initial beliefs and rules */

/* Initial goals */

process(term,actor).
//terms
!task(lunch, x, y).

/* Plans */

+!task(TaskName, _S, _T) : activity(TaskName).
+!activity(TaskName) : .print(TaskName).
