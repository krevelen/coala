/* $Id: 42bb4acbb5472a92611352ec4ede2c5032cf0e40 $
 * $URL$
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
package io.coala.eve3;

import com.almende.eve.protocol.jsonrpc.annotation.Access;
import com.almende.eve.protocol.jsonrpc.annotation.AccessType;
import com.almende.eve.protocol.jsonrpc.annotation.Namespace;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * {@link EveExposingAgent}
 * 
 * @version $Revision$
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 *
 */
public interface EveExposingAgent
{

	/** */
	String NAMESPACE = "exposed";

	/**
	 * @param exposed
	 */
	@JsonIgnore
	@Access(AccessType.SELF)
	void setExposed(Object exposed);

	/** @return the exposed object */
	@JsonIgnore
	@Access(AccessType.SELF)
	@Namespace(NAMESPACE)
	Object getExposed();

}
