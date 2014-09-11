/* $Id$
 * $URL: https://dev.almende.com/svn/abms/coala-examples/src/test/java/io/coala/example/deliver/TimeStamp.java $
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
package io.coala.example.deliver;

import io.coala.json.JsonUtil;

import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * {@link TimeStamp}
 * 
 * @version $Revision: 327 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 *
 */
public class TimeStamp
{

	/**
	 * INITIAL = {
	 * 
	 * 5483504636788736-20140620=[4832307315736576, 6019178444095488,
	 * 5171346598264832, 5127748318134272, 6045169774231552, 4832307315736576,
	 * 6019178444095488, 5171346598264832, 5127748318134272, 6045169774231552],
	 * 
	 * 5405593862930432-20140620=[4575480418140160, 4946641358422016,
	 * 4608396644843520, 6006763102928896, 5958207222579200, 4835621822529536,
	 * 4575480418140160, 4946641358422016, 4608396644843520, 6006763102928896,
	 * 5958207222579200, 4835621822529536],
	 * 
	 * 4903936582483968-20140620=[5117096799240192, 6409519097708544,
	 * 5319788117098496, 5556106948509696, 5113782292447232, 6041884459794432,
	 * 6053224045871104, 5707358919458816, 4919269867388928, 5197782189080576,
	 * 5616283802402816, 5266114581889024, 4730996016545792, 5491035509620736,
	 * 4880863196086272, 5635547066269696, 5117096799240192, 6409519097708544,
	 * 5319788117098496, 5556106948509696, 5113782292447232, 6041884459794432,
	 * 6053224045871104, 5707358919458816, 4919269867388928, 5197782189080576,
	 * 5616283802402816, 5266114581889024, 4730996016545792, 5491035509620736,
	 * 4880863196086272, 5635547066269696],
	 * 
	 * 5064079051849728-20140620=[6434273141719040, 6368686306754560,
	 * 5144408966037504, 6068510237130752, 5683624326201344, 6323682095923200,
	 * 6239682199289856, 4961311423201280, 5478934506373120, 6434273141719040,
	 * 6368686306754560, 5144408966037504, 6068510237130752, 5683624326201344,
	 * 6323682095923200, 6239682199289856, 4961311423201280, 5478934506373120],
	 * 
	 * 4562842627866624-20140620=[5511997969924096, 5200744844099584,
	 * 5651323689107456, 4942610330288128, 6690190378663936, 6288238079639552,
	 * 5207697657954304, 6326644750942208, 4795936928694272, 4916142392999936,
	 * 5471829959376896, 6562083315384320, 5511997969924096, 5200744844099584,
	 * 5651323689107456, 4942610330288128, 6690190378663936, 6288238079639552,
	 * 5207697657954304, 6326644750942208, 4795936928694272, 4916142392999936,
	 * 5471829959376896, 6562083315384320]}";
	 */
	private final String[] VEHICLES = {

			// 5483504636788736-20140620
			"Thomas",

			// 5405593862930432-20140620
			"Stephanie",

			// 4903936582483968-20140620
			"Dave",

			// 5064079051849728-20140620
			"Steven",

			// 4562842627866624-20140620
			"Brett", };

	@SuppressWarnings("unused")
	private final String[] SCHEDULE_LABELS = {
			// INITIAL @ 12:20
			"INITIAL @ 12:20",

			// ALARM 1
			"ALARM 1 @ 16:10",

			// ALARM 2
			"ALARM 2 @ 17:18",

			// ALARM 3
			"ALARM 3 @ 18:21",

			// ALARM 4
			"ALARM 4 @ 18:37",

			// ALARM 5
			"ALARM 5 @ 19:42",

			// ALARM 6
			"ALARM 6 @ 19:48",

			// ALARM 7
			"ALARM 7 @ 20:08",

			// ALARM 8
			"ALARM 8 @ 21:11",

			// ALARM 9
			"ALARM 9 @ 21:25",

	};

	private final String[][] SCHEDULES = {
			// 5483504636788736-20140620
			{
			// INITIAL 
			"[6019178444095488, 5171346598264832, 5127748318134272, 6045169774231552, 4832307315736576]", },

			// 5405593862930432-20140620
			{
			// INITIAL
			"[4575480418140160, 4946641358422016, 4608396644843520, 6006763102928896, 5958207222579200, 4835621822529536]", },

			// 4903936582483968-20140620
			{
			// INITIAL
			"[5117096799240192, 6409519097708544, 5319788117098496, 5556106948509696, 5113782292447232, 6041884459794432, 6053224045871104, 5707358919458816, 4919269867388928, 5197782189080576, 5616283802402816, 5266114581889024, 4730996016545792, 5491035509620736, 4880863196086272, 5635547066269696]", },

			// 5064079051849728-20140620
			{
			// INITIAL
			"[6434273141719040, 6368686306754560, 5144408966037504, 6068510237130752, 5683624326201344, 6323682095923200, 6239682199289856, 4961311423201280, 5478934506373120]", },

			// 4562842627866624-20140620
			{
			// INITIAL
			"[5511997969924096, 5200744844099584, 5651323689107456, 4942610330288128, 6690190378663936, 6288238079639552, 5207697657954304, 6326644750942208, 4795936928694272, 4916142392999936, 5471829959376896, 6562083315384320]",
			//
			},
	//
	};

	@SuppressWarnings("deprecation")
	@Test
	public void doTimestamp()
	{
		final ArrayNode nodes = JsonUtil.getJOM().createArrayNode();
		final ArrayNode edges = JsonUtil.getJOM().createArrayNode();
		final ObjectNode data = JsonUtil.getJOM().createObjectNode();
		data.put("nodes", nodes);
		data.put("edges", edges);
		for (int i = 0; i < SCHEDULES.length; i++)
		{
			final ObjectNode vehicle = JsonUtil.getJOM().createObjectNode();
			vehicle.put("id", VEHICLES[i]);
			vehicle.put("name", VEHICLES[i]);
			vehicle.put("shape", "box");
			vehicle.put("label", VEHICLES[i]);
			vehicle.put("mass", 4.0);
			nodes.add(vehicle);
			for (int j = 0; j < SCHEDULES[i].length; j++)
			{
				String last = VEHICLES[i];
				for (Object o : JsonUtil.fromJSONString(SCHEDULES[i][j],
						List.class))
				{
					final ObjectNode order = JsonUtil.getJOM()
							.createObjectNode();
					order.put("id", "" + o);
					order.put("name", "" + o);
					order.put("shape", "circle");
					order.put("label", "" + o);
					order.put("mass", 2.0);
					nodes.add(order);
//					final ObjectNode edge1 = JsonUtil.getJOM()
//							.createObjectNode();
//					edge1.put("from", VEHICLES[i]);
//					edge1.put("to", "" + o);
//					edge1.put("style", "arrow");
//					edges.add(edge1);
					final ObjectNode edge2 = JsonUtil.getJOM()
							.createObjectNode();
					edge2.put("from", last);
					edge2.put("to", "" + o);
					edge2.put("style", "arrow");
					edges.add(edge2);
					last = "" + o;
				}
			}
		}
		System.err.println("var data=" + JsonUtil.toJSONString(data));
	}
}
