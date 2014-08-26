package io.coala.jsa;

import io.coala.log.LogUtil;
import jade.semantics.interpreter.Finder;
import jade.semantics.kbase.KBase;
import jade.semantics.kbase.KBaseDecorator;
import jade.semantics.kbase.QueryResult;
import jade.semantics.kbase.filters.FilterKBase;
import jade.semantics.kbase.filters.FilterKBaseLoader;
import jade.semantics.kbase.filters.FiltersDefinition;
import jade.semantics.kbase.filters.KBAssertFilter;
import jade.semantics.kbase.filters.KBFilter;
import jade.semantics.kbase.filters.KBQueryFilter;
import jade.semantics.kbase.filters.std.builtins.NowFunction;
import jade.semantics.kbase.observers.Observer;
import jade.semantics.lang.sl.grammar.BelieveNode;
import jade.semantics.lang.sl.grammar.Formula;
import jade.semantics.lang.sl.grammar.MetaFormulaReferenceNode;
import jade.semantics.lang.sl.grammar.NotNode;
import jade.semantics.lang.sl.grammar.PredicateNode;
import jade.semantics.lang.sl.grammar.TrueNode;
import jade.semantics.lang.sl.tools.MatchResult;
import jade.semantics.lang.sl.tools.SL;
import jade.util.leap.ArrayList;
import jade.util.leap.Iterator;
import jade.util.leap.Set;
import jade.util.leap.SortedSetImpl;

import java.util.Arrays;
import java.util.List;

public class FilterKBaseImpl extends KBaseDecorator implements FilterKBase {
	
   /*********************************************************************/
   /**                         INNER CLASS     import jade.semantics.kbase.filters.FilterKBase;
                        **/
   /*********************************************************************/
	static class Cache {
		
		static int CACHE_SIZE = 5;
		
		static class CacheElement  {
			protected Formula formula = null;
			protected QueryResult result = null;
			protected ArrayList reasons = null;
			public CacheElement(Formula formula, QueryResult result, ArrayList reasons) {
				this.formula = formula;
				this.result = result;
				if (result == null) {
					this.reasons = reasons;
				}
				else {
					this.reasons = null;
				}
			}
		}
		
		CacheElement[] elements = new CacheElement[CACHE_SIZE];
		int filledSize = 0;
		
		protected CacheElement query(Formula formula) {
			for(int i=0; i<filledSize; i++) {
				if (elements[i].formula.equals(formula)) {
					return elements[i];
				}
			}
			return null;
		}
		
		protected void add(Formula formula, QueryResult result, ArrayList reasons) {
			if ( filledSize < elements.length ) {
				elements[filledSize++] = new CacheElement(formula, result, reasons);
			}
			else {
				for(int i=0; i<elements.length-1; i++) {
					elements[i] = elements[i+1];
				}				
				elements[filledSize-1] = new CacheElement(formula, result, reasons);
			}
		}
	
		protected void clear() {
			filledSize = 0;
		}
		
	}

   /*********************************************************************/
   /**                        PRIVATE FIELDS                           **/
   /*********************************************************************/
   private final Formula bPattern;

   /**
    * List of assertion filters
    */
   private ArrayList assertFilterList;

   /**
    * List of query filters
    */
   private ArrayList queryFilterList;

    /**
    * Logger
    */
   private Logger logger;

   /**
    * Cache
    */
   private Cache cache;
   
  
   /*********************************************************************/
   /**                         CONSTRUCTOR                             **/
   /*********************************************************************/

   /**
    * Creates a new belief base.
    * @param decorated the wrapped kbase used to store the facts, 
    *                  which should not be null.
    */
   public FilterKBaseImpl(KBase decorated) {
   	this(decorated, null);
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
		   LOG.trace(name+" (FilterKBASE): "+message);
	   };
   }

   /**
    * Creates a new belief base.
    * @param decorated the wrapped kbase used to store the facts, 
    *                  which should not be null.
    * @param loader a particular filters loader.
    */
   public FilterKBaseImpl(KBase decorated, FilterKBaseLoader loader) {
   	super(decorated);
		this.assertFilterList = new ArrayList();
       this.queryFilterList = new ArrayList();

     this.cache = new Cache();
   	this.logger = new Logger("SEMANTICS.FilterKBaseImpl@" + getAgentName());

       this.bPattern = new BelieveNode(getAgentName(), new MetaFormulaReferenceNode("phi"));
       
       if ( loader != null ) {loader.load(this);}
   } 
   
   
   /***************************
    * METHODS TO ACCESS KBASE *
    ***************************/
   
   
   /* (non-Javadoc)
    * @see jade.semantics.kbase.KBaseDecorator#assertFormula(jade.semantics.lang.sl.grammar.Formula)
    */
   @Override
	public void assertFormula(Formula formula) {
   	cache.clear();
      formula = new BelieveNode(getAgentName(), formula).getSimplifiedFormula();
       if (logger.isLoggable(Logger.FINE)) logger.log(Logger.FINE, "AAAAAAAAAA ASSERTING: " + formula);
		try {
			for (int i =0; i < assertFilterList.size(); i++) {
	            Formula formulaToAssert = formula;
               if (logger.isLoggable(Logger.FINEST)) logger.log(Logger.FINEST, "Applying assert filter BEFORE (" + i + "): " + assertFilterList.get(i));
               if (formula instanceof TrueNode) return;
//               System.err.println("Applying assert filter BEFORE (" + i + "): " + assertFilterList.get(i));
               formula = ((KBAssertFilter)assertFilterList.get(i)).apply(formula);
//               System.err.println("beforeAssert Filter (" + i + ") resulted in : " + formulaToAssert);
               if (logger.isLoggable(Logger.FINE) && formulaToAssert != formula)
               	logger.log(Logger.FINE, "APPLIED ASSERT-FILTER " + assertFilterList.get(i) + "\n" +
               			"INPUT= " + formulaToAssert + "\nOUTPUT=" + formula);
           }
			
			decorated.assertFormula(formula);

		} catch (Exception e) {
           e.printStackTrace();
       }
   } 

   /* (non-Javadoc)
    * @see jade.semantics.kbase.KBaseDecorator#query(jade.semantics.lang.sl.grammar.Formula)
    */
   @Override
	public QueryResult query(Formula formula, ArrayList reasons) {
   	QueryResult result = QueryResult.UNKNOWN;
		QueryResult.BoolWrapper goOn = new QueryResult.BoolWrapper(true);
   	ArrayList falsityReasons = new ArrayList();

		formula = new BelieveNode(getAgentName(), formula).getSimplifiedFormula();

   	Cache.CacheElement cacheResult = cache.query(formula);
//		Cache.CacheElement cacheResult = null;
   	if (cacheResult != null) {
   		QueryResult.addReasons(reasons, cacheResult.reasons);
   		return cacheResult.result;
   	}

		logger.log(Logger.FINEST, "QQQQQQQQQQ QUERYING: " + formula);
       for (int i =0; goOn.getBool() && i < queryFilterList.size(); i++) {
//     	System.err.println("Testing query filter (" + i + "): " + queryFilterList.get(i));
       	QueryResult filterResult = ((KBQueryFilter)queryFilterList.get(i)).apply(formula, falsityReasons, goOn);
       	if (filterResult != null) {
       		if (result == QueryResult.UNKNOWN) {
       			result = filterResult;
       		}
       		else {
       			result = result.union(filterResult);
       		}
       			logger.log(Logger.FINEST, "APPLIED " + queryFilterList.get(i) + " QUERY-FILTER(cont=" + goOn.getBool() + "): " + filterResult + "\n" +
       					"ON: " + formula);
       	}
       }
       
       if (goOn.getBool()) {
       	result = (result == QueryResult.UNKNOWN ?
       			decorated.query(formula, falsityReasons) : result.union(decorated.query(formula, falsityReasons)));
       }
//       System.err.println("FilterKBaseImpl.result = " + result);
       if (result == QueryResult.UNKNOWN) {
       	QueryResult.addReasons(reasons, falsityReasons);
       }
       else if (result.isEmpty()) {
          	// To ensure that an empty result always references QueryResult.KNOWN
       	result = QueryResult.KNOWN;
       }
   	if (result == null) {
       	logger.log(Logger.FINEST, "QQQQQQQQQQ RESULT: null\nBECAUSE: " + reasons + "\nON: " + formula);
   	}
   	else {
       	logger.log(Logger.FINEST, "QQQQQQQQQQ RESULT: " + result + "\nON: " + formula);
   	}
   	
   	if(formula instanceof BelieveNode && 
				((BelieveNode)formula).as_formula() instanceof PredicateNode &&
				nonCachingPredicates.contains(((PredicateNode)
						((BelieveNode)formula).as_formula()).as_symbol().toString()))
			logger.log(Logger.FINE, "NOT caching belief containing '" +
					nonCachingPredicates + "' predicate, e.g.: " + formula);
		else
			cache.add(formula, result, falsityReasons);
		
       return result;
  } 
   
   private static final List<String> nonCachingPredicates = Arrays.asList(
   		new String[]{
   				NowFunction.NOW_ID,
 //  				"random"
			});
   
   /******************************
    * METHODS TO ADD NEW FILTERS *
    ******************************/
   
    /* (non-Javadoc)
    * @see jade.semantics.kbase.FilterKBase#addFiltersDefinition(jade.semantics.kbase.FiltersDefinition)
    */
   public void addFiltersDefinition(FiltersDefinition filtersDefinition) {
       for (int i=0 ; i < filtersDefinition.size() ; i++) {
           addKBFilter(filtersDefinition.getFilterDefinition(i).getFilter(), filtersDefinition.getFilterDefinition(i).getIndex());
       }
   } 

   /**
    * Adds a new <code>KBFilter</code>.
    * @param filter a filter
    * @param index the index to add the filter
    */
   private void addKBFilter(KBFilter filter, int index) {
       filter.setMyKBase(this);
       if (filter instanceof KBAssertFilter) {
           if (index == END) {
                assertFilterList.add(filter);
           } else {
               assertFilterList.add((index<0 ? assertFilterList.size() : index), filter);
           }
       }
       else if (filter instanceof KBQueryFilter) {
           if (index == END) {
               queryFilterList.add(filter);
           } else {
               queryFilterList.add((index<0 ? queryFilterList.size() : index), filter);
           }
       }
   } 

   /* (non-Javadoc)
    * @see jade.semantics.kbase.FilterKBase#addKBAssertFilter(jade.semantics.kbase.filters.KBAssertFilter)
    */
   public void addKBAssertFilter(KBAssertFilter filter) {
	       addKBFilter(filter, -1);
	} 

   /* (non-Javadoc)
    * @see jade.semantics.kbase.filters.FilterKBase#addKBAssertFilter(jade.semantics.kbase.filters.KBAssertFilter, int)
    */
   public void addKBAssertFilter(KBAssertFilter filter, int index) {
       addKBFilter(filter, index);
   } 

   /* (non-Javadoc)
    * @see jade.semantics.kbase.filters.FilterKBase#addKBQueryFilter(jade.semantics.kbase.filters.KBQueryFilter)
    */
   public void addKBQueryFilter(KBQueryFilter filter) {
       addKBFilter(filter, -1);
   } 

   /* (non-Javadoc)
    * @see jade.semantics.kbase.filters.FilterKBase#addKBQueryFilter(jade.semantics.kbase.filters.KBQueryFilter, int)
    */
   public void addKBQueryFilter(KBQueryFilter filter, int index) {
       addKBFilter(filter, index);
   } 

   
   /*****************************
    * METHODS TO REMOVE FILTERS *
    *****************************/
   
   /* (non-Javadoc)
    * @see jade.semantics.kbase.filters.FilterKBase#removeKBAssertFilter(jade.semantics.interpreter.Finder)
    */
   public void removeKBAssertFilter(Finder filterIdentifier) {
       filterIdentifier.removeFromList(assertFilterList);
   } 

    /* (non-Javadoc)
    * @see jade.semantics.kbase.filters.FilterKBase#removeKBQueryFilter(jade.semantics.interpreter.Finder)
    */
   public void removeKBQueryFilter(Finder filterIdentifier) {
       filterIdentifier.removeFromList(queryFilterList);
   } 

   
   /*******************************
    * METHODS TO HANDLE OBSERVERS *
    *******************************/
   
   /* (non-Javadoc)
    * @see jade.semantics.kbase.KBaseDecorator#addObserver(jade.semantics.kbase.observers.Observer)
    */
   /**
    * Do not forget to initialise observers once added to the KBase
    * by using <code>observer.update(null);</code>
    * Only observers added in the <code>setupKBase</code> method are
    * automatically initialised. 
    **/
   @Override
	public void addObserver(final Observer obs) {
		decorated.addObserver(obs); 
		Formula[] formulas = obs.getObservedFormulas();
		for (int i=0; i<formulas.length; i++) {
	        Set result = new SortedSetImpl();
	        //System.err.println("add observer, i="+i);
	        //System.err.println("f=formula(i)="+formulas[i]);
	        //System.err.println("now call getObsTrigPatt(f,"+result+")");
	        getObserverTriggerPatterns(formulas[i], result);	
			for (Iterator it = result.iterator(); it.hasNext();) {
				Formula f = (Formula)it.next();
				obs.addFormula(f);
			}
		}
   } 

    /* (non-Javadoc)
    * @see jade.semantics.kbase.filters.FilterKBase#getObserverTriggerPatterns(jade.semantics.lang.sl.grammar.Formula, jade.util.leap.Set)
    */
   public void getObserverTriggerPatterns(Formula formula, Set result) {
		for (int i =0; i < queryFilterList.size(); i++) {
			((KBQueryFilter)queryFilterList.get(i)).getObserverTriggerPatterns(formula, result);
       }
       try {
       	MatchResult matchResult = SL.match(bPattern,formula);
           if (matchResult != null) {
				Formula phi = matchResult.getFormula("phi");
				result.add(phi);
			} 
           else {
				result.add(formula);
           }
       } catch (SL.WrongTypeException wte) {
           wte.printStackTrace();
       }
   }
   
  
   /*******************
    * DISPLAY METHODS *
    *******************/
   
   /* (non-Javadoc)
    * @see jade.semantics.kbase.KBaseDecorator#toStrings()
    */
   @Override
	public ArrayList toStrings() {
   	ArrayList result = new ArrayList(queryFilterList.size() * 5);
   	
   	// standard KBase
   	result.add("******* KBase *******");
   	toStrings(result, decorated.toStrings());
   	
   	// KBases of KBQueryFilters
   	for (Iterator it = queryFilterList.iterator(); it.hasNext(); ) {
   		toStrings(result, ((KBQueryFilter)it.next()).toStrings());
   	}
   	
   	// clean result from undesired predicates (only for internal use, not useful for user)
   	// scan backward because of removal and shifts (in order not to miss one element)
   	for (int i=result.size()-1;i>=0;i--) {
   		Formula stri = SL.formula(result.get(i).toString());
   		if (stri instanceof PredicateNode) {
   			String symbol = ((PredicateNode)stri).as_symbol().toString(); 
   			if ( symbol.equals("sanction")  || symbol.equals("is-institutional") || symbol.equals("is_observing"))  {
   				result.remove(i);
//   				System.err.println("do not print "+stri);
   			}
   		}
   		// FIXME : manual removal of not-formulas from the KBase ...?
   		if (stri instanceof NotNode) {
   			result.remove(i);
   		}
   	}
   	return result;
   }
   
   private void toStrings(ArrayList result, ArrayList strings) {
		if (strings != null) {
			for (Iterator j=strings.iterator(); j.hasNext(); ) {
				result.add(j.next());
			}
		}
   }
   
   
   /*********
    * DEBUG *
    *********/
   
   /**
    * For debugging purpose only
    */
   public void viewFilterQuery() {
       System.err.println("----------- Query Filter ---------");
       for(int i = 0; i < queryFilterList.size(); i++) {
           System.err.println(queryFilterList.get(i));
       }
       System.err.println("----------------------------------");
   } 

} 

