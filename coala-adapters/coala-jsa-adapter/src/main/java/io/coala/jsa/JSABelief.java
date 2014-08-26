package io.coala.jsa;

import io.coala.capability.know.ReasoningCapability.Belief;
import io.coala.jsa.sl.SLParsableSerializable;
import jade.semantics.lang.sl.grammar.Formula;
import jade.semantics.lang.sl.grammar.Node;
import jade.semantics.lang.sl.grammar.NotNode;

/**
 * {@link JSABelief}
 * 
 * @version $Revision$
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 */
public class JSABelief implements Belief {
	
	/** */
	private static final long serialVersionUID = 1L;
	
	protected final Node node;
	
	private String stringValue;
	
	public JSABelief(Node node) {
		super();
		this.node = node;
	}
	
	public final Node getNode() {
		return node;
	}
	
	@Override
	public String toString(){
		if (stringValue == null)
			stringValue = node.toString();
		return stringValue;
	}
	
	@Override
	public boolean equals(Object o) {
		return toString().equals(o.toString());
	}
	
	@Override
	public int hashCode() {
		return toString().hashCode();
	}

	@Override
	public Belief negate()
	{
		try {
			return new JSABelief(new NotNode((Formula)getNode()));
		} catch (ClassCastException e) {
			try
			{
				return new JSABelief(JSAReasoningCapability.getSLForObject(Node.class,new SLParsableSerializable("(not "+getNode()+")")));
			} catch (Exception e1)
			{
				throw new ClassCastException("Failed to negate "+getNode()+", cause:"+e1.getMessage());
			}
		}
	}
}