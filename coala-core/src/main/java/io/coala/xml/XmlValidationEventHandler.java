/* $Id$
 * $URL: https://dev.almende.com/svn/abms/coala-common/src/main/java/com/almende/coala/xml/XmlValidationEventHandler.java $
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
package io.coala.xml;

import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.Subject;

/**
 * {@link XmlValidationEventHandler}
 * 
 * @version $Revision: 335 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 *
 */
public class XmlValidationEventHandler implements ValidationEventHandler
{

	/** */
	private final Subject<ValidationEvent, ValidationEvent> events = PublishSubject
			.create();

	@Override
	public boolean handleEvent(final ValidationEvent event)
	{
		events.onNext(event);
		return true;
	}

	/** @return an {@link Observable} source of {@link ValidationEvent}s */
	public Observable<ValidationEvent> events()
	{
		return this.events.asObservable();
	}
}
