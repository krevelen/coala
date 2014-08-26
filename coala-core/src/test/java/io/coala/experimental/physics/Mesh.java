/* $Id: Mesh.java 296 2014-06-03 12:26:09Z krevelen $
 * $URL: https://dev.almende.com/svn/abms/coala-common/src/main/java/com/almende/coala/physics/Mesh.java $
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
package io.coala.experimental.physics;


/**
 * {@link Mesh} is a collection of vertices, edges and faces that define the
 * shape (e.g. surface) of a {@link Body}
 * 
 * <p>
 * See also Wikipedia's <a
 * href="http://en.wikipedia.org/wiki/Polygon_mesh">Polygon Mesh</a> description
 * 
 * @date $Date: 2014-06-03 14:26:09 +0200 (Tue, 03 Jun 2014) $
 * @version $Revision: 296 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 * 
 */
public interface Mesh extends Shape
{

	//List<Vertex> getPoints();

}
