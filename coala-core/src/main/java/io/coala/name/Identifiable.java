/* $Id$
 * $URL: https://dev.almende.com/svn/abms/coala-common/src/main/java/com/almende/coala/identity/Identifiable.java $
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
package io.coala.name;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;

/**
 * {@link Identifiable} owns an {@link AbstractIdentifier}
 * 
 * @date $Date: 2014-06-03 14:26:09 +0200 (Tue, 03 Jun 2014) $
 * @version $Revision: 296 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 * 
 * @param <ID> the type of {@link AbstractIdentifier} value
 * @param <THIS> the (sub)type of {@link Identifiable} to build
 */
public interface Identifiable<ID extends Identifier<?, ?>>
		extends Comparable<Identifiable<ID>>
{

	/** @return the identifier */
	@JsonTypeInfo(use=JsonTypeInfo.Id.CLASS, include=As.PROPERTY, property="class")
	ID getID();

	/**
	 * {@link Builder}
	 * 
	 * @version $Revision: 296 $
	 * @author <a href="mailto:Rick@almende.org">Rick</a>
	 * 
	 * @param <ID>
	 * @param <T>
	 * @param <THIS>
	 */
	public interface Builder<ID extends Identifier<?, ?>, T extends Identifiable<ID>, THIS extends Builder<ID, T, THIS>>
	{
		/**
		 * @param id the {@link Identifier} to set
		 * @return this {@link Builder}
		 */
		THIS withID(ID id);
		
		/** @return the built {@link Identifiable} result */
		T build();
	}

}
