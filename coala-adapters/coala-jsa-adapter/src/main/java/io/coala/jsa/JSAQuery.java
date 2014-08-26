package io.coala.jsa;

import io.coala.capability.know.ReasoningCapability.Query;
import jade.semantics.lang.sl.grammar.Formula;
import jade.semantics.lang.sl.grammar.Node;
import jade.semantics.lang.sl.grammar.NotNode;
import jade.semantics.lang.sl.grammar.StringConstantNode;
import jade.semantics.lang.sl.tools.SL;

/**
 * {@link JSAQuery}
 * 
 * @version $Revision: 237 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 *
 */
public class JSAQuery implements Query
{

	/** */
	private static final long serialVersionUID = 1L;

	protected final Node node;

	public JSAQuery(Node node)
	{
		super();
		if (node instanceof StringConstantNode)
		{
			this.node = SL.formula("(false)");
		} else
			this.node = node;
	}

	public final Node getNode()
	{
		return node;
	}

	@Override
	public String toString()
	{
		return node.toString();
	}

	/** @see com.almende.coala.service.reasoner.ReasonerService.Query#negate() */
	@Override
	public Query negate()
	{
		return new JSAQuery(new NotNode((Formula) getNode()));
	}

}