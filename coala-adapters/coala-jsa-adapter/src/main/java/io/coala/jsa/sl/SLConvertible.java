package io.coala.jsa.sl;

import jade.semantics.lang.sl.grammar.Node;


public interface SLConvertible<T extends SLConvertible<?>>
{

	/** @return the (Jade) semantic {@link Node} equivalent */
	<N extends Node> N toSL();
	
	/**
	 * @param node the semantic {@link Node} to interpret
	 * @return the interpreted bean
	 */
	<N extends Node> T fromSL(N node);
}