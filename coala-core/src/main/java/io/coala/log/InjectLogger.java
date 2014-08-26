/* $Id: InjectLogger.java 300 2014-06-13 12:10:35Z krevelen $
 * $URL: https://dev.almende.com/svn/abms/coala-common/src/main/java/com/almende/coala/log/InjectLogger.java $
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
package io.coala.log;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * {@link InjectLogger} does not work on abstract supertypes, perhaps a solution
 * may be distilled from <a href=
 * "https://bitbucket.org/noctarius/guiceidentityinjection/src/014f6cb8fcc0/src/main/java/com/google/inject/identityinjection/IdentityProviderFactory.java"
 * >here</a>
 * 
 * @date $Date: 2014-06-13 14:10:35 +0200 (Fri, 13 Jun 2014) $
 * @version $Revision: 300 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface InjectLogger
{

}
