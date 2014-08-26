package io.coala.jsa;

/*****************************************************************
JADE - Java Agent DEvelopment Framework is a framework to develop 
multi-agent systems in compliance with the FIPA specifications.
JSA - JADE Semantics Add-on is a framework to develop cognitive
agents in compliance with the FIPA-ACL formal specifications.

Copyright (C) 2007 France Telecom

GNU Lesser General Public License

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation,
version 2.1 of the License.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the
Free Software Foundation, Inc., 59 Temple Place - Suite 330,
Boston, MA  02111-1307, USA.
*****************************************************************/

/**
* Modified 29 January 2008 by Carole Adam
* added modifications of toStrings from Vincent Louis
* (copied from the main branch of the JSA)
*/

import io.coala.log.LogUtil;
import jade.semantics.interpreter.Finder;
import jade.semantics.kbase.KBase;
import jade.semantics.kbase.QueryResult;
import jade.semantics.kbase.observers.Observer;
import jade.semantics.lang.sl.grammar.AndNode;
import jade.semantics.lang.sl.grammar.AnyNode;
import jade.semantics.lang.sl.grammar.AtomicFormula;
import jade.semantics.lang.sl.grammar.BelieveNode;
import jade.semantics.lang.sl.grammar.ExistsNode;
import jade.semantics.lang.sl.grammar.ForallNode;
import jade.semantics.lang.sl.grammar.Formula;
import jade.semantics.lang.sl.grammar.FunctionalTermNode;
import jade.semantics.lang.sl.grammar.IdentifyingExpression;
import jade.semantics.lang.sl.grammar.InstitutionalLogicFormula;
import jade.semantics.lang.sl.grammar.ListOfTerm;
import jade.semantics.lang.sl.grammar.MetaTermReferenceNode;
import jade.semantics.lang.sl.grammar.Node;
import jade.semantics.lang.sl.grammar.NotNode;
import jade.semantics.lang.sl.grammar.OrNode;
import jade.semantics.lang.sl.grammar.PredicateNode;
import jade.semantics.lang.sl.grammar.SymbolNode;
import jade.semantics.lang.sl.grammar.Term;
import jade.semantics.lang.sl.grammar.TermSetNode;
import jade.semantics.lang.sl.grammar.TrueNode;
import jade.semantics.lang.sl.grammar.UncertaintyNode;
import jade.semantics.lang.sl.grammar.Variable;
import jade.semantics.lang.sl.grammar.VariableNode;
import jade.semantics.lang.sl.tools.MatchResult;
import jade.semantics.lang.sl.tools.SL;
import jade.util.leap.ArrayList;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

/**
* Class that implements the belief base api. The data are stored in an
* <code>ArrayList</code>. The observers of the base are also
* stored in <code>ArrayList</code>s.
* @author Vincent Pautret - France Telecom
* @version Date: 2004/11/30 Revision: 1.0
* @version Date: 2006/12/21 Revision: 1.4 (Thierry Martinez)
*/
public class ArrayListKBaseImpl implements KBase {

   /**
    * The wrapping KBase
    */
   private KBase wrappingKBase;
   
   /**
    * All the beliefs of this KBase have the form (B agentName ...) 
    */
   Term agentName;

   /**
    * Storage of beliefs
    */
   private DataStorage dataStorage;

   /**
    * List of patterns which are closed
    */
   private ArrayList closedPredicateList;

   /**
    * List of observers
    */
   private ArrayList observers;

   /**
    * Patterns for assertion and query methods
    */
   private final Formula uPattern;
   private final Formula notUPattern;
   private final Formula iPattern;
   private final Formula notIPattern;
   private final Formula bPattern;
   private final Formula notBPattern;
   // CA added 3 new patterns 20/11/2007
   private final Formula belOPattern;
   private final Formula belNotOPattern;
   private final Formula notBelOPattern;
   // CA added new pattern 27/11/2007
   //private final Formula donePattern;
   private final Formula notURefPattern;
   private final Formula URefPattern;
   private final Formula ireFormula;

	/**
    * Logger
    */

   private Logger logger;

   /**
    * HashSet of Observations which contains the observers to trigger when a
    * formula is asserted
    */
   
	HashSet<?> observersToApplied;

	
   /*********************************************************************/
   /**                   PRIVATE CLASS DATASTORAGE                     **/
   /*********************************************************************/
   static private class DataStorage
   {
   	/*
   	 * The dataStorage is a HashMap the entries of which are
   	 * ArrayList of formulas
   	 */
   	private HashMap<Integer, ArrayList> dataStorage = new HashMap<Integer, ArrayList>();
   	
//   	private Term agentName;
   	
		public DataStorage(Term agentName) {
//			this.agentName = agentName;
		}
   	
		// added by CA 4/02/08
//		public Term getAgentNameAttribute() {
//			return agentName;
//		}
		
   	private int hashCode(Formula phi) {
    		if (phi instanceof PredicateNode) {
   			return ((SymbolNode)((PredicateNode)phi).as_symbol()).lx_value().hashCode();
   		}
   		//else {
   			return -1;
   		//}
   	}
   	
		private ArrayList getFacts(Formula phi) {
			int hc = hashCode(phi);
   		ArrayList facts = (ArrayList)dataStorage.get(hc);
   		if ( facts == null ) {
   			dataStorage.put(hc, facts = new ArrayList());
   		}
			return facts;
		}
		
   	/**
   	 * Add the formula phi to the DataStorage if not already in
   	 * @param phi
   	 */
   	protected void add(Formula phi) { 
   		ArrayList facts = getFacts(phi);
   		if ( !facts.contains(phi) ) {
   			facts.add(phi);
   		}
   	}
   	
   	/**
   	 * Return true is the DataStorage contains phi
   	 * @param phi
   	 * @return true is the DataStorage contains phi
   	 */
   	protected boolean contains(Formula phi) {
   		return getFacts(phi).contains(phi);
   	}
   	
   	/**
   	 * Remove phi from the DataStorage
   	 * @param phi
   	 */
   	protected void remove(Formula phi) {
   		getFacts(phi).remove(phi);
   	}
   	
       /**
        * Removes all the formulae that match the specified pattern.
        * @param pattern an SL pattern
        */
       private void removeAll(Formula pattern) {
       	ArrayList facts = getFacts(pattern);
           for (int i = facts.size()-1; i >= 0 ; i--) {
               MatchResult matchResult = SL.match(pattern, (Node)facts.get(i));
               if (matchResult != null) {
               	facts.remove(i);
               }
           }
       } 
       
       public void removeFormula(Finder finder) {
       	Object[] lists = dataStorage.values().toArray();
       	for (int i=0; i<lists.length; i++) {
       		finder.removeFromList((ArrayList)lists[i]);
       	}
       }

   	/**
   	 * Tries to match the formula on each data stored in the base. In case of
        * success, a QueryResult is created that contains all
        * the corresponding MatchResults.
        * @param pattern a pattern to test on each data of the base
        * @return a QueryResult, which match the given pattern, and null if
        * the pattern does not match with any data stored in the base.
        */
       private QueryResult query(Formula pattern) {
       	QueryResult result = QueryResult.UNKNOWN;
       	ArrayList facts = getFacts(pattern);
           for (int j = 0; j < facts.size(); j++) {
           	Formula fact = (Formula)facts.get(j);
               MatchResult matchResult = SL.match(pattern, fact);
               if (matchResult != null) {
                   if (result == QueryResult.UNKNOWN) result = new QueryResult();
                   if (matchResult.size() != 0) result.add(matchResult);
               }
           }
           return result;
       } 

    	
		/**
		 * Return the size of the DataStorage
		 */
		protected int size() {
       	Object[] lists = dataStorage.values().toArray();
			int size = 0;
       	for (int i=0; i<lists.length; i++) {
       		size += ((ArrayList)lists[i]).size();
       	}
       	return size;
		}

		/**
        * Return the content of the DataStorage as a array of strings
        * @return a String array that represent the content of the DataStorage
        */
       public ArrayList toStrings() {
           ArrayList result = new ArrayList(size());
           for (Iterator<?> it = dataStorage.values().iterator(); it.hasNext(); ) {
           	for (Iterator<?> jt = ((ArrayList)it.next()).iterator(); jt.hasNext(); ) {
           		result.add(jt.next().toString());
           	}
           }
           return result;
       }

   }
   

	
   /*********************************************************************/
   /**                         CONSTRUCTOR                             **/
   /*********************************************************************/

   /**
    * Creates a new belief base. 
    * @param agent the owner of the base
    */
   public ArrayListKBaseImpl(Term agentName) {
   	this(agentName, null);
   }

   private class Logger {
	   
	   private org.apache.log4j.Logger LOG = LogUtil.getLogger(FilterKBaseImpl.class);

	   private static final int FINE = 0;
	   private static final int FINEST = 0;
	   
	   private final String name;
	   
	   public Logger(final String name) {
		   this.name = name;
	   }
	   
	   public boolean isLoggable(int level) {
		   if (level < 0)
			   return false;
		   return true;
	   }
	   public void log(int level, String message) {
		   LOG.trace(name+" (ArrayListKBASE): "+message);
	   };
   }
   
   public <T extends Node> void assertFormula(T formula) {
	   if (formula.getClass().equals(Formula.class))
		   assertFormula((Formula)formula);
	   else
		   logger.log(5,"Expected a "+Formula.class.getCanonicalName()+" but got a "+formula.getClass().getCanonicalName());
   }
   	/**
    * Creates a new belief base. 
    * @param agent the owner of the base
    * @param wrappingKBase the kbase that wrap this one
    */
   public ArrayListKBaseImpl(Term agentName, KBase wrappingKBase) {
   	this.wrappingKBase = wrappingKBase == null ? this : wrappingKBase;
   	this.agentName = agentName;
   	if (wrappingKBase == null) {
   		this.logger = new Logger("SEMANTICS.ArrayListKBase@" + agentName);
   	}
   	else {
//   		this.logger = wrappingKBase.getLogger();
   	}
       this.dataStorage = new DataStorage(agentName);
       this.closedPredicateList = new ArrayList();
       this.observers = new ArrayList();

       uPattern       = SL.formula("(U "+agentName+" ??phi)");
       notUPattern    = SL.formula("(not (U "+agentName+" ??phi))");
       iPattern       = SL.formula("(I "+agentName+" ??phi)");
       notIPattern    = SL.formula("(not (I "+agentName+" ??phi))");
       // CA definitions of 3 new patterns
       belOPattern       = SL.formula("(B "+agentName+" (O ??phi))");
       belNotOPattern    = SL.formula("(B "+agentName+" (not (O ??phi)))");
       notBelOPattern 	  = SL.formula("(not (B "+agentName+" (O ??phi)))");
       // CA definition of 1 new pattern
       //donePattern    = SL.formula("(done (action ??agent ??act) ??psi)");
       bPattern       = SL.formula("(B "+agentName+" ??phi)");
       notBPattern    = SL.formula("(not (B "+agentName+" ??phi))");
       notURefPattern = SL.formula("(forall ??var (not (U "+agentName+" ??phi)))");
       URefPattern    = SL.formula("(exists ??var (U "+agentName+" ??phi))");
       ireFormula     = SL.formula("(B "+agentName+" (= ??ire ??Result))");
   } 

   /*********************************************************************/
   /**                         METHODS                                 **/
   /*********************************************************************/
   
   /* (non-Javadoc)
    * @see jade.semantics.kbase.KBase#getWrappingKBase()
    */
   public KBase getWrappingKBase() {
     	return wrappingKBase;
   }
   
   /* (non-Javadoc)
    * @see jade.semantics.kbase.KBase#setWrappingKBase(jade.semantics.kbase.KBase)
    */
   public void setWrappingKBase(KBase kbase) {
   	wrappingKBase = kbase;
   }
   
   /* (non-Javadoc)
    * @see jade.semantics.kbase.KBase#getAgentName()
    */
   public Term getAgentName() {
   	return agentName;
   }
   
   /* (non-Javadoc)
    * @see jade.semantics.kbase.KBase#setAgentName(jade.semantics.lang.sl.grammar.Term)
    */
   public void setAgentName(Term agent) {
   	agentName = agent;    	
   }
   
	/**
    * Tries to assert the formula. The assertion depends on the formula. 
    * If the formula matches the pattern:
    * <ul>
    * <li> "(I ??myself ??phi)", the formula is asserted.
    * <li> "(not (I ??myself ??phi))", the formula "(I ??myself ??phi)" is removed if
    * it is in the base
    * 
    * <li> "(B ??myself (O ??phi))", the formula is asserted.
    * <li> "(B ??myself (not (O ??phi)))", the formula "(O ??phi)" is removed if
    * it is in the base
    * 
    * <li> "(B ??myself ??phi)", the formula phi is asserted if it is an
    * <code>AtomicFormula</code>
    * <li> "(B ??myself (not ??phi)), if the formula phi is in the base, it is removed
    * </ul>
    * <i>??myself</i> represents the current semantic agent.<br>
    * If the formula matches another pattern nothing is done.
    * At the end of the assert, each boolean that corresponds to the
    * applicability of the observers is set to false.
    * @param formula the formula to assert in the belief base
    */
   public void assertFormula(Formula formula) {
       formula = new BelieveNode(agentName, formula).getSimplifiedFormula();
       if (logger.isLoggable(Logger.FINE)) logger.log(Logger.FINE, ">>>>>>>>>> ASSERTING: " + formula);
		try {
//           Formula formulaToAssert = formula;
           MatchResult matchResult = SL.match(bPattern,formula);
           if (matchResult != null) {
               Formula phi = matchResult.getFormula("phi");
               if ( ((phi instanceof AtomicFormula) && !(phi instanceof TrueNode)) ||
                       ((phi instanceof NotNode) && (((NotNode)phi).as_formula() instanceof AtomicFormula))) {
                   if (!dataStorage.contains(phi)) {
                       removeFormula(new NotNode(phi).getSimplifiedFormula());
                       dataStorage.add(phi);						
                       if (logger.isLoggable(Logger.FINEST)) logger.log(Logger.FINEST, "ADDED TO DATASTORAGE: " + phi);
						updateObservers(phi); 
                  }
               }
               // added assertion of obligations, CA 20 November 2007
               // institutional logic formula are asserted in the KBase
               else if (((phi instanceof InstitutionalLogicFormula) && !(phi instanceof TrueNode)) ||
               		((phi instanceof NotNode) && (((NotNode)phi).as_formula() instanceof InstitutionalLogicFormula))) {
               	if (!dataStorage.contains(phi)) {
               		removeFormula(new NotNode(phi).getSimplifiedFormula());
               		dataStorage.add(phi);
               		updateObservers(phi);
               	}
               }
               // CA
           }
           else if ((matchResult = SL.match(iPattern,formula)) != null) {
               dataStorage.add(formula);
               if (logger.isLoggable(Logger.FINEST)) logger.log(Logger.FINEST, "ADDED TO DATASTORAGE: " + formula);
				updateObservers(formula); 
           }
           else if ((matchResult = SL.match(notIPattern,formula)) != null) {
               dataStorage.remove(((NotNode)formula).as_formula());
               if (logger.isLoggable(Logger.FINEST)) logger.log(Logger.FINEST, "REMOVED FROM DATASTORAGE: " + ((NotNode)formula).as_formula());
				updateObservers(formula); 
           }
           
//        // added assertion and REMOVAL of obligations, CA 20 November 2007, CA 11 December 2007
           else if ((matchResult = SL.match(belOPattern,formula)) != null) {
               formula = ((BelieveNode)formula).as_formula();
               dataStorage.add(formula);
				updateObservers(formula); 
           }
           else if ((matchResult = SL.match(belNotOPattern,formula)) != null) {
           	formula = ((NotNode)((BelieveNode)formula).as_formula()).as_formula();
               dataStorage.remove(formula);
				updateObservers(formula); 
           }
           else if ((matchResult = SL.match(notBelOPattern,formula)) != null) {
           	formula = ((BelieveNode)((NotNode)formula).as_formula()).as_formula();
           	dataStorage.remove(formula);
				updateObservers(formula);
           }
//           // CA
           
//           else if (((matchResult = SL.match(uPattern,formula)) != null) ||
//                   ((matchResult = SL.match(notUPattern, formula)) != null) ) {
//           }
           else if ((matchResult = SL.match(notBPattern, formula)) != null) {
               Formula phi = matchResult.getFormula("phi");
               if ( ((phi instanceof AtomicFormula) && !(phi instanceof TrueNode)) ||
                       ((phi instanceof NotNode) && (((NotNode)phi).as_formula() instanceof AtomicFormula))) {
                   dataStorage.removeAll(matchResult.getFormula("phi"));
                   if (logger.isLoggable(Logger.FINEST)) logger.log(Logger.FINEST, "REMOVED FROM DATASTORAGE: " + matchResult.getFormula("phi"));
               }
           }
       } catch (Exception e) {
           e.printStackTrace();
       }
   } 
	
	/* (non-Javadoc)
	 * @see jade.semantics.kbase.KBase#updateObservers()
	 */
	public void updateObservers(Formula formula) {
		/* modified by CA and VL, 29/1/08
		 * scan the table backward instead of forward
		 * (otherwise when a OneShot observer is updated,
		 * it is removed and the next observer in the list 
		 * is shifted at its place; thus when the index is 
		 * incremented this shifted observer is NOT updated...) 
		 */
		
		for (int i=observers.size()-1; i>-1; i--) {
			// MODIF by CA 02 April 2008, added a test to prevent OutOfBounds exception
			// size may have changed if OneShot observers are removed... this test is NOT useless
			if (i<observers.size()) {
				((Observer)observers.get(i)).update(formula);
			}
		}
	}
	
   /**
    * Queries the belief base on the formula (B ??agent (= ??ire ??Result)), where
    * ??ire is instantiated with the identifying expression given in parameter.
    * The ListOfTerm corresponding to the solutions is the value of the meta
    * variable ??Result, which could be null.
    * @param expression the identifying expression on which the query relates.
    * @return a list of terms that corresponds to the question or null (meaning
    * the answer : "I do not know")
    * @deprecated Use {@link #queryRefSingleTerm(IdentifyingExpression)} or
    *             {@link #queryRef(IdentifyingExpression, ArrayList)} instead.
    */
   @Deprecated
	public ListOfTerm queryRef(IdentifyingExpression expression) {
   	Term term = queryRef(expression, new ArrayList());
   	if (term != null) {
   		ListOfTerm result = new ListOfTerm();
   		if (term instanceof TermSetNode) {
   			for (int j = 0; j < ((TermSetNode)term).as_terms().size(); j++) {
   				result.add( ((TermSetNode)term).as_terms().get(j));
   			}
   		} else {
   			result.add(term);
   		}
   		return result;
   	}
   	return null;
   }
   
   /* (non-Javadoc)
    * @see jade.semantics.kbase.KBase#queryRef(jade.semantics.lang.sl.grammar.IdentifyingExpression, jade.util.leap.ArrayList)
    */
   public Term queryRef(IdentifyingExpression expression, ArrayList falsityReasons) {
       try {
           Formula form = (Formula)SL.instantiate(ireFormula,
//                   "agent", agentName,
                   "ire", expression);
           QueryResult solutions = getWrappingKBase().query(form, falsityReasons);
           if (solutions != QueryResult.UNKNOWN && solutions.size() == 1) {
           	return solutions.getResult(0).term("Result");
//               Term result;
//               if (solutions.getResult(0).getTerm("Result") instanceof IdentifyingExpression) {
//                   result = solutions.getResult(0).getTerm("ire");
//               } else {
//                   result = solutions.getResult(0).getTerm("Result");
//               }
//               return result;
           }
           else if (solutions == QueryResult.UNKNOWN) {
               for (int i = falsityReasons.size()-1; i>=0; i--) {
               	Variable var = new VariableNode("Result");
               	falsityReasons.add(i, new ForallNode(
               			var,
               			((Formula)falsityReasons.remove(i)).instantiate("Result", var)));
               }            	
           }
       } catch (Exception e) {
           e.printStackTrace();
       }
       return null;
   }
   
   /**
    * Allways returns a single term depending on the IRE
    * and the result list returned by queryRef.
    * - if the IRE is iota and the result owns 1 single term : returns this term
    * - if the IRE is any and the result owns 1 term at least : returns the first term
    * - if the IRE is all or some : returns a set containing all results
    * - else return null
    */
   public Term queryRefSingleTerm(IdentifyingExpression expression) {
   	return queryRef(expression, new ArrayList());
   }
   
   /* (non-Javadoc)
    * @see jade.semantics.kbase.KBase#eval(jade.semantics.lang.sl.grammar.Term)
    */
   public Term eval(Term expression) {
		// Get the most "deep" Term expression or MetaTermReferenceNode refered to by the term
		while ((expression instanceof MetaTermReferenceNode)
				&& (((MetaTermReferenceNode)expression).sm_value() != null)) {
			expression = ((MetaTermReferenceNode)expression).sm_value();
		}

		if (expression instanceof FunctionalTermNode) {
			FunctionalTermNode function = (FunctionalTermNode)expression;
			Variable var = new VariableNode(Integer.toString(function.hashCode()));
			ListOfTerm params = function.as_terms();
			params.append(var);
			expression = queryRefSingleTerm(new AnyNode(
					var,
					new PredicateNode(function.as_symbol(),
							params)));
		}
		
		if (expression instanceof IdentifyingExpression) {
			expression = getWrappingKBase().queryRefSingleTerm((IdentifyingExpression)expression);
		}
		return expression;
   }
   
   /* (non-Javadoc)
    * @see jade.semantics.kbase.KBase#query(jade.semantics.lang.sl.grammar.Formula)
    */
   public QueryResult query(Formula formula) {
   	return getWrappingKBase().query(formula, new ArrayList());
   }
   
   /**
    * Queries directly the belief base.
    * @return a list of solutions to the query
    * @param formula a formula
    * @param reasons the list of formulas, which are believed by the agent and
    *                explain the result of the query is unknown
    */
   public QueryResult query(Formula formula, ArrayList reasons) {
   	QueryResult result = QueryResult.UNKNOWN;
       formula = new BelieveNode(agentName, formula).getSimplifiedFormula();
       
       if (logger.isLoggable(Logger.FINE)) logger.log(Logger.FINE, ">>>>>>>>>> QUERYING: " + formula);
       try {
           MatchResult matchResult;
           
           if ((matchResult = SL.match(bPattern,formula)) != null) {
           	Formula phi = matchResult.getFormula("phi");
           	Formula notPhi = (phi instanceof NotNode ?
           			((NotNode)phi).as_formula() : new NotNode(phi));
               result = dataStorage.query(phi);
               
               if (result == QueryResult.UNKNOWN) {
               	if (isClosed(phi, null)) {
               		reasons.add(notPhi);
               	}
               	else {
//               		QueryResult notResult = getWrappingKBase().query(notPhi);
               		QueryResult notResult = dataStorage.query(notPhi);
               		if (notResult == QueryResult.UNKNOWN && isClosed(notPhi, null)) {
               			// ??phi is unknown && (not ??phi) is unknown && (not ??phi) is closed
               			// => agent believes ??phi
               			result = QueryResult.KNOWN;
               		}
               		else if (notResult != QueryResult.UNKNOWN && notResult.size()==0) {
               			reasons.add(notPhi);
               		}
               		else {
               			reasons.add(new NotNode(formula));
               		}
               	}
               }
           }
           else if ((matchResult = SL.match(notBPattern,formula)) != null) {
           	// FIXME Check the getWrappingKBase().query(phi) does not involve infinite loops
           	Formula phi = matchResult.getFormula("phi");
           	QueryResult notResult = getWrappingKBase().query(phi);
           	
           	if (notResult == QueryResult.UNKNOWN) {
           		result = QueryResult.KNOWN;
           	}
           	else {
           		if (notResult.size() == 0) {
           			reasons.add(phi);
           		}
           		// else formula is likely to contain meta-reference, and
           		// this is currently not processed by this implementation
           	}
           }
           else if ((matchResult = SL.match(iPattern, formula)) != null) {
           	result = dataStorage.query(formula);
           	if (result == QueryResult.UNKNOWN) {
           		reasons.add(new NotNode(formula));
           	}
           }
           else if ((matchResult = SL.match(notIPattern, formula)) != null) {
               if (getWrappingKBase().query(((NotNode)formula).as_formula()) == QueryResult.UNKNOWN) {
               	result = QueryResult.KNOWN;
               }
               else {
               	reasons.add(((NotNode)formula).as_formula());
               }
           }
           // FIXME management of obligations, CA 20 November 2007
           else if ((matchResult = SL.match(belOPattern, formula)) != null) {
           	result = dataStorage.query(((BelieveNode)formula).as_formula());
           }
           else if ((matchResult = SL.match(belNotOPattern, formula)) != null) {
               if (dataStorage.query(((NotNode)formula).as_formula()) == QueryResult.UNKNOWN) {
               	result = QueryResult.KNOWN;
               }
           }
           // CA 
           else if (formula.equals(SL.TRUE)) {
           	result = QueryResult.KNOWN;
           }
           else if (((matchResult = SL.match(URefPattern,formula)) != null) ||
                   ((matchResult = SL.match(uPattern,formula)) != null)) {
           	result = QueryResult.UNKNOWN; // ONLY TO KEEP THIS CASE EXPLICIT
           	if (formula instanceof UncertaintyNode) {
           		reasons.add(new NotNode(formula));
           	}
           	else {
           		reasons.add(new ForallNode(matchResult.variable("var"),
           				new NotNode(((ExistsNode)formula).as_formula())));
           	}
           }
           else if (((matchResult = SL.match(notURefPattern,formula)) != null) ||
                   ((matchResult = SL.match(notUPattern, formula)) != null)) {
           	result = QueryResult.KNOWN;
           }
       } catch (Exception e) {
           e.printStackTrace();
       }
       if (logger.isLoggable(Logger.FINE)) {
       	if (result == null) {
           	logger.log(Logger.FINE, ">>>>>>>>>> RESULT: null, BECAUSE " + reasons + "\nON: " + formula);
       	}
       	else {
           	logger.log(Logger.FINE, ">>>>>>>>>> RESULT: " + result + "\nON: " + formula);
       	}
       }
       if ( result != QueryResult.UNKNOWN && result.isEmpty() ) {
       	// To ensure that an empty result always references QueryResult.KNOWN
       	return QueryResult.KNOWN;
       }
       //else {
       	return result;
       //}
   } 

//   private AtomicFormula evaluateTerms(AtomicFormula formula) {
//   	if (formula instanceof PredicateNode) {
//   		ListOfTerm params = ((PredicateNode)formula).as_terms();
//   		for (int i=0; i<params.size(); i++) {
//   			Term term = eval(params.element(i));
//   			if (term != null) {
//   				params.replace(i, term);
//   			}
//   		}
//   	}
//   	else if (formula instanceof EqualsNode) {
//   		Term term = eval(((EqualsNode)formula).as_left_term());
//   		if (term != null) {
//       		((EqualsNode)formula).as_left_term(term);    			
//   		}
//   		term = eval(((EqualsNode)formula).as_right_term());
//   		if (term != null) {
//       		((EqualsNode)formula).as_right_term(term);    			
//   		}
//   	}
//   	else if (formula instanceof ResultNode) {
//   		Term term = eval(((ResultNode)formula).as_term1());
//   		if (term != null) {
//       		((ResultNode)formula).as_term1(term);    			
//   		}
//   		term = eval(((ResultNode)formula).as_term2());
//   		if (term != null) {
//       		((ResultNode)formula).as_term2(term);    			
//   		}
//   	}
//   	return formula;
//   }


   

   /**
    * Removes the specified formula from the base.
    * @param formula a formula
    * @return true if the formula to remove was actually in the KBase
    */
//   public boolean removeFormula(Formula formula) {
//       boolean result = false;
//       for (int i = dataStorage.size() - 1; i >=0 ; i--) {
//           if (dataStorage.get(i).equals(formula)) {
//               dataStorage.remove(i);
//               result = true;
//           }
//       }
//       return result;
//   } 
   public boolean removeFormula(Formula formula) {
       boolean result = dataStorage.contains(formula);
       dataStorage.remove(formula);
       return result;
   } 


   /**
    * @inheritDoc
    * @deprecated use retractFormula(Formula) instead
    */
//   public void removeFormula(Finder finder) {
//       finder.removeFromList(dataStorage);
//   }
   @Deprecated
	public void removeFormula(Finder finder) {
       dataStorage.removeFormula(finder);
   }
   
   /**
    * It retracts the formula given in parameter from the belief base by
    * asserting (not (B ??myself ??formula)). The formula to retract may include
    * meta-variables.
    * @param formula the formula to retract from the belief base
    */
   public void retractFormula(Formula formula) {
       getWrappingKBase().assertFormula(new NotNode(new BelieveNode(agentName, formula)).getSimplifiedFormula());
   }

   /**
    * For debugging purpose only
    * @return an array that contains a string representation of each data in
    * the base
    */
   public ArrayList toStrings() {
       return dataStorage.toStrings();
   } 


   /**
    * Adds a new Observer in the observer table
    * @param o the observer to add
    */
   public void addObserver(final Observer obs) {
		observers.add(obs);
   } 

   /**
    * Removes from the KBase the observers detected by the finder.
    * @param finder the finder
    */
   public void removeObserver(Finder finder) {
       ArrayList toSupress = new ArrayList();
		for (int i=0; i<observers.size(); i++) {
           if (finder.identify(observers.get(i))) {
               toSupress.add(observers.get(i));
			}
		}
		for (int i = 0; i < toSupress.size(); i++) {
			observers.remove(toSupress.get(i));
		}
   }

   /**
    * Removes the given observer
    * @param obs the observer to be removed
    */
   public void removeObserver(Observer obs) {
		observers.remove(obs);
	}
	
   /* (non-Javadoc)
    * @see jade.semantics.kbase.KBase#addClosedPredicate(jade.semantics.lang.sl.grammar.Formula)
    */
   public void addClosedPredicate(Formula pattern) {
   	if (!isClosed(pattern, null)) {
   		if (pattern instanceof AndNode) {
   			addClosedPredicate(((AndNode)pattern).as_left_formula());
   			addClosedPredicate(((AndNode)pattern).as_right_formula());
   		} else if (pattern instanceof ForallNode) {
   			addClosedPredicate(new NotNode(pattern));
   		} else if (!pattern.isMentalAttitude(agentName)) {
   			closedPredicateList.add(pattern);
   		}
   	}
   }

   /* (non-Javadoc)
    * @see jade.semantics.kbase.KBase#removeClosedPredicate(jade.semantics.interpreter.Finder)
    */
   public void removeClosedPredicate(Finder finder) {
       finder.removeFromList(closedPredicateList);
   }

   /* (non-Javadoc)
    * @see jade.semantics.kbase.KBase#isClosed(jade.semantics.lang.sl.grammar.Formula, jade.semantics.kbase.QueryResult)
    */
   public boolean isClosed(Formula pattern, QueryResult b) {
       if (pattern.isMentalAttitude(agentName)) {
           return true;
       } else if (pattern instanceof AndNode) {
           return (isClosed(((AndNode)pattern).as_left_formula(), b) || isClosed(((AndNode)pattern).as_right_formula(), b));
       } else if (pattern instanceof OrNode) {
           return (isClosed(((OrNode)pattern).as_left_formula(), b) && isClosed(((OrNode)pattern).as_right_formula(), b));
       } else if (pattern instanceof ExistsNode) {
           return (isClosed(((ExistsNode)pattern).as_formula(),b));
       } else if (pattern instanceof ForallNode) {
           return (isClosed(((ForallNode)pattern).as_formula(),b));
       }
       else {
           try {
               for (int i = 0; i < closedPredicateList.size(); i++) {
                   if ((SL.match((Node)closedPredicateList.get(i),pattern) != null)) {
                       if (b != null) {
                       	return b.equals(getWrappingKBase().query(pattern));
                       }
                       return true;
                   }
               }
           } catch (Exception e) {
               e.printStackTrace();
           }
           return false;
       }
   }
   
   /* (non-Javadoc)
    * @see jade.semantics.kbase.KBase#getLogger()
    */
   public Logger getLogger() {
   	return this.logger;
   }
} 
