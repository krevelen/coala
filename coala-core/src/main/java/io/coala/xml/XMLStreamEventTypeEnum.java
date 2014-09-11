/* $Id$
 * $URL: https://dev.almende.com/svn/abms/coala-common/src/main/java/com/almende/coala/xml/XMLStreamEventTypeEnum.java $
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

/**
 * {@link XMLStreamEventTypeEnum}
 * 
 * @version $Revision: 354 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 *
 */
public enum XMLStreamEventTypeEnum
{
	/**
	 * Indicates an event is a start element
	 * 
	 * @see javax.xml.stream.events.StartElement
	 */
	START_ELEMENT,

	/**
	 * Indicates an event is an end element
	 * 
	 * @see javax.xml.stream.events.EndElement
	 */
	END_ELEMENT,

	/**
	 * Indicates an event is a processing instruction
	 * 
	 * @see javax.xml.stream.events.ProcessingInstruction
	 */
	PROCESSING_INSTRUCTION,

	/**
	 * Indicates an event is characters
	 * 
	 * @see javax.xml.stream.events.Characters
	 */
	CHARACTERS,

	/**
	 * Indicates an event is a comment
	 * 
	 * @see javax.xml.stream.events.Comment
	 */
	COMMENT,

	/**
	 * The characters are white space (see [XML], 2.10 "White Space Handling").
	 * Events are only reported as SPACE if they are ignorable white space.
	 * Otherwise they are reported as CHARACTERS.
	 * 
	 * @see javax.xml.stream.events.Characters
	 */
	SPACE,

	/**
	 * Indicates an event is a start document
	 * 
	 * @see javax.xml.stream.events.StartDocument
	 */
	START_DOCUMENT,

	/**
	 * Indicates an event is an end document
	 * 
	 * @see javax.xml.stream.events.EndDocument
	 */
	END_DOCUMENT,

	/**
	 * Indicates an event is an entity reference
	 * 
	 * @see javax.xml.stream.events.EntityReference
	 */
	ENTITY_REFERENCE,

	/**
	 * Indicates an event is an attribute
	 * 
	 * @see javax.xml.stream.events.Attribute
	 */
	ATTRIBUTE,

	/**
	 * Indicates an event is a DTD
	 * 
	 * @see javax.xml.stream.events.DTD
	 */
	DTD,

	/**
	 * Indicates an event is a CDATA section
	 * 
	 * @see javax.xml.stream.events.Characters
	 */
	CDATA,

	/**
	 * Indicates the event is a namespace declaration
	 * 
	 * @see javax.xml.stream.events.Namespace
	 */
	NAMESPACE,

	/**
	 * Indicates a Notation
	 * 
	 * @see javax.xml.stream.events.NotationDeclaration
	 */
	NOTATION_DECLARATION,

	/**
	 * Indicates a Entity Declaration
	 * 
	 * @see javax.xml.stream.events.NotationDeclaration
	 */
	ENTITY_DECLARATION,

	//
	;
}