// Agent sample_agent in project jasontest

/* 
 * Adapted from Ferilli et al. (2013, aieiea) 
 * "Logic-based incremental process mining in smart environments":
 * 
 * ==== Describing cases (p.395)
 * 
 * 	activity(S, T)
 * 		at step S task T is executed;
 * 
 * 	next(S', S")
 * 		step S" follows step S'.
 * 
 * ==== Describing the structure of a workflow (p.395)
 * 
 * 	task(t, C)
 * 		task t occurs in cases C, where C is a multiset of case identifiers 
 * 		(because a task may be carried out several times in the same case);
 * 
 * 	transition(I, O, p, C)
 * 		transition1 p, that occurs in cases C (again a multiset), consists in 
 *		ending all tasks in I and starting all tasks in O.
 * 
 * ==== Refinement of workflow model according to a new case (p.396)
 * 
 * Require: W: workflow model
 * Require: c: case having FOL description D
 * 
 * 	for all activity(s, t) in c do
 * 		if exists task(t, C) in  W then
 * 			W <- (W \ task(t, C)) u { task(t, C u {c}) } // update statistics on task t
 * 		else
 * 			W <- W u { task(t,{c})) } // insert new task and initialize statistics
 * 		end if
 * 		refine precondition(W, t(s) :- D|s)
 * 		refine postcondition(W, t(s) :- D)
 * 	end for
 * 
 * 	for all next(s',s") in c do
 * 		I <- {t' | activity(s', t') in c}
 * 		O <- {t" | activity(s', t") in c}
 * 		if exists transition(I, O, p, C) in W then
 * 			W <- (W \ transition(I, O, t, C)) u { transition(I, O, t, C in {c}) } // update statistics on transition p
 * 		else
 * 			p <- generate fresh transition identifier()
 * 			W <- W u { transition(I, O, p, {c})) } // insert new transition and initialize statistics
 * 		end if
 * 	end for
 */
 

/* Initial beliefs and rules
 * 
 * ==== Refined "Morning" workflow according to 5 cases (p.397)
 */ 

task(stop,[1,2,3,4,5]).
task(door,[1,2,3,4,5]).
task(tv,[1,2,4,5]).
task(tea,[1,2,3,4,5]).
task(dress,[1,2,3,4,5]).
task(weight,[1,4,5]).
task(wth,[1,2,3,4,5]).
task(book,[1,2,4,5]).
task(radio,[1,2,3,4,5]).
task(toilet,[1,2,3,4,5]).
task(wake_up,[1,2,3,4,5]).
task(start,[1,2,3,4,5]).

transition([start],[wake_up],p1,[1,2,3,4,5]).
transition([wake_up],[radio,toilet],p2,[1,2,3,4,5]).
transition([radio],[book],p3[1,2,4,5]).
transition([book,toilet],[wth],p4,[1,2,4,5]).
transition([wth],[weight],p5,[1,4,5]).
transition([weight],[dress],p6,[1,4,5]).
transition([dress],[tea,tv],p7,[1,2,4,5]).
transition([tea,tv],[door],p8,[1,2,4,5]).
transition([door],[stop],p9,[1,2,3,4,5]).
transition([radio,toilet],[wth],p10,[3]).
transition([wth],[dress],p11,[2,3]).
transition([dress],[tea],p12,[3]).
transition([tea],[door],p13,[3]).

doing(1,radio).

/* Initial goals */

!start.

/* Plans */

+!start : true <- 
	?doing(T, A);
	?transition([A|I],O,_P,_C);
	T2 = T + 1;
	.print("Doing task ", A, " at time ", T, " with possibly next ", O, " at time ", T2).
	
