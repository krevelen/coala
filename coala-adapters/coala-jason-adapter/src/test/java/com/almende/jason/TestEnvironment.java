/* $Id$
 * $URL: https://dev.almende.com/svn/abms/jason-util/src/test/java/com/almende/jason/TestEnvironment.java $
 * 
 * Part of the EU project Adapt4EE, see http://www.adapt4ee.eu/
 * 
 * @license
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy
 * of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 * 
 * Copyright (c) 2010-2013 Almende B.V. 
 */
package com.almende.jason;

import io.coala.log.LogUtil;
import jason.NoValueException;
import jason.asSyntax.Atom;
import jason.asSyntax.ListTerm;
import jason.asSyntax.Literal;
import jason.asSyntax.NumberTerm;
import jason.asSyntax.Structure;
import jason.asSyntax.Term;
import jason.asSyntax.VarTerm;
import jason.environment.Environment;

import org.apache.log4j.Logger;

/**
 * {@link TestEnvironment}
 * 
 * @date $Date: 2014-04-18 16:38:34 +0200 (Fri, 18 Apr 2014) $
 * @version $Revision: 235 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 *
 */
public class TestEnvironment extends Environment {

	/** */
	private static final Logger LOG = LogUtil.getLogger(TestEnvironment.class);

	/**
	 * Executes an action on the environment. This method is probably overridden
	 * in the user environment class.
	 */
	public boolean executeAction(final String agName, final Structure act) {
		if (act.getFunctor().equals("show")) {
			try {
				final Number generation = ((NumberTerm) ((VarTerm) act
						.getTerm(0)).getTerm()).solve();
				final Number width = ((NumberTerm) ((VarTerm) act.getTerm(1))
						.getTerm()).solve();
				final Number height = ((NumberTerm) ((VarTerm) act.getTerm(2))
						.getTerm()).solve();
				final String[] states = new String[width.intValue()
						* height.intValue()];
				for (Term item : ((ListTerm) ((VarTerm) act.getTerm(3))
						.getTerm()).getAsList())
					states[Integer.valueOf(((Atom) ((Literal) item).getTerm(0))
							.getFunctor().substring(4))] = ((Atom) ((Literal) item)
							.getTerm(1)).getFunctor();
				final StringBuilder lattice = new StringBuilder(String.format(
						"generation %d", generation.intValue()));
				for (int y = 0; y < height.intValue(); y++) {
					lattice.append("\n\t[");
					for (int x = 0; x < width.intValue(); x++) {
						String state = states[y * height.intValue() + x];
						lattice.append(' ').append(
								state == null ? ' '
										: state.equals("dead") ? '.' : 'X');
					}
					lattice.append(" ]");
				}
				LOG.info(lattice.toString());
			} catch (final NoValueException e) {
				LOG.warn("Problem obtaining values from term", e);
			}
		} else
			LOG.info("Got action " + act.getFunctor() + "/" + act.getArity()
					+ " from " + agName);
		return true;
	}

}
