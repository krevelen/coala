/* $Id$
 * $URL: https://dev.almende.com/svn/abms/coala-common/src/main/java/com/almende/coala/event/AbstractEventProducer.java $
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
package io.coala.event;

import io.coala.model.ModelComponentID;
import io.coala.name.AbstractIdentifiable;
import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.Subject;

/**
 * {@link AbstractEventProducer}
 * 
 * @date $Date: 2014-06-03 14:26:09 +0200 (Tue, 03 Jun 2014) $
 * @version $Revision: 296 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 */
public class AbstractEventProducer<ID extends ModelComponentID<?>, E extends Event<?>>
		extends AbstractIdentifiable<ID> implements EventProducer<ID, E>
{

	/** */
	private static final long serialVersionUID = 1L;

	private transient final Subject<E, E> events = PublishSubject.create();

	/**
	 * {@link AbstractEventProducer} constructor
	 */
	public AbstractEventProducer(final ID id)
	{
		super(id);
	}

	protected void fireEvent(final E event)
	{
		this.events.onNext(event);
	}

	/** @see EventProducer#getEvents() */
	@Override
	public Observable<E> getEvents()
	{
		return this.events.asObservable();
	}

}
