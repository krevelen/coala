/* $Id$
 * $URL: https://dev.almende.com/svn/abms/coala-common/src/main/java/com/almende/coala/restore/Restorable.java $
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
 * Copyright (c) 2010-2014 Almende B.V. 
 */
package io.coala.experimental.restore;

import io.coala.agent.Agent;
import io.coala.capability.Capability;

/**
 * {@link Restorable} is a marker interface to indicate that an originator (e.g.
 * {@link Agent} or {@link Capability}) may be rolled back based on any
 * {@link Checkpoint} it may have generated earlier
 * <p>
 * Should be based on the <a
 * href="http://en.wikipedia.org/wiki/Visitor_pattern">Visitor pattern</a>
 * <p>
 * Compare the <a href="http://en.wikipedia.org/wiki/Undo">undo</a> <a
 * href="http://en.wikipedia.org/wiki/Command_pattern">command pattern</a>, <a
 * href="http://en.wikipedia.org/wiki/Copy-on-write">copy-on-write</a>
 * optimization, and the <a
 * href="http://en.wikipedia.org/wiki/Memento_pattern">memento pattern</a> (with
 * alternative implementation at <a href=
 * "http://stackoverflow.com/questions/14082892">stackoverflow</a> and
 * application in <a
 * href="http://cs.gmu.edu/~eclab/projects/ecj/docs/tutorials/tutorial3/"
 * >ECJ</a>)
 * 
 * <p>
 * TODO implement with e.g. <a href="http://xstream.codehaus.org/">Xstream</a>?
 * 
 * @version $Revision: 332 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 *
 */
public interface Restorable<T extends Checkpoint, THIS extends Restorable<T, THIS>>
{

	/**
	 * @return a current {@link Checkpoint} from the originator, e.g. via
	 *         serialization
	 */
	void accept(CheckpointVisitor<? extends T> checkpointVisitor);

	/**
	 * @param the {@link Checkpoint} to rebuild the originator from, e.g. a
	 *            serialization
	 */
	THIS fromCheckpoint(T checkpoint) throws Exception;

}
