/* $Id: 0b646eb8ad3f4ce0f439374cc2ca0cbee6eb3677 $
 * $URL: https://dev.almende.com/svn/abms/coala-common/src/main/java/com/almende/coala/agent/Agent.java $
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
package io.coala.actor;

import io.coala.Coala;

import org.aeonbits.owner.Config.LoadPolicy;
import org.aeonbits.owner.Config.LoadType;
import org.aeonbits.owner.Config.Separator;
import org.aeonbits.owner.Config.Sources;

/**
 * {@link Config}
 * 
 * @date $Date: 2014-06-03 14:26:09 +0200 (Tue, 03 Jun 2014) $
 * @version $Id: 0b646eb8ad3f4ce0f439374cc2ca0cbee6eb3677 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 */
@LoadPolicy(LoadType.MERGE)
@Sources({ "file:${" + Coala.CONFIG_FILE_PROPERTY + "}",
		"classpath:${" + Coala.CONFIG_FILE_PROPERTY + "}",
		"file:${user.dir}/" + Coala.CONFIG_FILE_DEFAULT,
		"file:~/" + Coala.CONFIG_FILE_DEFAULT,
		"classpath:" + Coala.CONFIG_FILE_DEFAULT })
@Separator(",")
public interface Config
{

}
