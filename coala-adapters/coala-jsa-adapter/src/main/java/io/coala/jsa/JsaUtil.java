package io.coala.jsa;

import io.coala.agent.Agent;
import io.coala.agent.AgentID;
import io.coala.exception.CoalaExceptionFactory;
import io.coala.log.LogUtil;
import io.coala.util.Util;
import jade.semantics.kbase.filters.FiltersDefinition;
import jade.semantics.kbase.observers.Observer;
import jade.semantics.lang.sl.grammar.Formula;
import jade.semantics.lang.sl.grammar.Node;
import jade.semantics.lang.sl.grammar.Term;
import jade.semantics.lang.sl.parser.SLParser;
import jade.semantics.lang.sl.tools.SL;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

public class JsaUtil implements Util
{
	
	private static final Map<Object,Node> cachedSLNode = Collections
			.synchronizedMap(new HashMap<Object,Node>());
	
	private static JsaUtil INSTANCE;
	
	private Logger LOG = LogUtil.getLogger(JsaUtil.class);
	

	private JsaUtil()
	{
		// utility methods only
	}
	
	public static synchronized JsaUtil getInstance() {
		if (INSTANCE == null)
			INSTANCE = new JsaUtil();
		return INSTANCE;
	}

	public static FiltersDefinition getFilterDefinitions(final Agent agent)
	{
		// TODO get from (BAAL/hardcoded)Config

		// switch agent type

		// get filters from config

		return null;
	}
	
	/**
	 * 
	 * {@link Converter} that converts the object to a SL node.
	 * 
	 * @date $Date: 2014-07-10 08:43:55 +0200 (Thu, 10 Jul 2014) $
	 * @version $Revision: 326 $
	 * @author <a href="mailto:suki@almende.org">suki</a>
	 *
	 * @param <T>
	 */
	public interface Converter<T extends Node> {
		T convert(Object node);
	}
	
	@SuppressWarnings("unchecked")
	<T extends Node> T getCachedSLNode(final Object node, final Converter<T> converter) {
		T result = (T) cachedSLNode.get(node);
		if (result == null) {
			result = converter.convert(node);
			cachedSLNode.put(node, result);
		}
		return (T) result;
	}
	
	
	@SuppressWarnings("unchecked")
	<T extends Node> T instantiateAsSL(Class<T> slNodeType, Object javaObject){
		T result = null;
		if (slNodeType.equals(Term.class)) {
			if (javaObject instanceof AgentID)
				result = (T) getCachedSLNode(javaObject, new Converter<Term>()
						{

					@Override
					public Term convert(Object node)
					{
						return SL.string(node.toString());
					}
				});
			else
				result = (T) getCachedSLNode(javaObject, new Converter<Term>()
						{

					@Override
					public Term convert(Object node)
					{
						return SL.term(node.toString());
					}
				});
		} else if (slNodeType.equals(Formula.class)) {
			
				result = (T) getCachedSLNode(javaObject, new Converter<Formula>()
						{

					@Override
					public Formula convert(Object node)
					{
						return SL.formula(node.toString());
					}
				});
		}  else if (slNodeType.equals(Node.class)) {
			
			result = (T) getCachedSLNode(javaObject, new Converter<Node>()
					{

				@Override
				public Node convert(Object node)
				{
					Node slNode = null;
					try
					{
						slNode = SLParser.getParser().parseTerm(node.toString(),true);
					} catch (jade.semantics.lang.sl.parser.ParseException e)
					{
						try
						{
							slNode = SLParser.getParser().parseFormula(node.toString(),true);
						} catch (jade.semantics.lang.sl.parser.ParseException e1)
						{
							LOG.error("Failed to create SL term",e);
							LOG.error("Failed to create SL formula",e1);
							return null;
						}
					}
					return slNode;
				}
			});
	   }  else
			CoalaExceptionFactory.VALUE_NOT_ALLOWED.create(javaObject, "Failed to convert JavaType to SL Node of type: "+slNodeType.getSimpleName());
		return (T)result;
	}
	
	public Term getTermForAgentID(final AgentID agentID){
		return getCachedSLNode(agentID, new Converter<Term>()
		{

			@Override
			public Term convert(Object node)
			{
				return SL.string(node.toString());
			}
		});
	}

	public static List<Observer> getObservers(final Agent agent)
	{
		// TODO get from (BAAL/hardcoded)Config

		return null;
	}

}
