///*
// * $Id: 06ea05ac557971cc8566cf182ae6b4609163b843 $
// * $URL:
// * https://redmine.almende.com/svn/a4eesim/trunk/adapt4ee-gui/src/main/java
// * /eu/a4ee/gui/rest/SimulationManagementServlet.java $
// * 
// * Part of the EU project Adapt4EE, see http://www.adapt4ee.eu/
// * 
// * @license
// * Licensed under the Apache License, Version 2.0 (the "License"); you may not
// * use this file except in compliance with the License. You may obtain a copy
// * of the License at
// * 
// * http://www.apache.org/licenses/LICENSE-2.0
// * 
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
// * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
// * License for the specific language governing permissions and limitations under
// * the License.
// * 
// * Copyright (c) 2010-2013 Almende B.V.
// */
//package io.coala.nodyn.servlet;
//
//import io.coala.log.LogUtil;
//import io.coala.resource.FileUtil;
//
//import java.io.InputStreamReader;
//import java.util.ArrayList;
//import java.util.List;
//
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServlet;
//
//import org.apache.commons.io.IOUtils;
//import org.apache.log4j.Logger;
//
///**
// * Adapt4ee Calibration {@link NodynServlet} usage:
// * 
// * 
// * @date $Date: 2014-08-08 07:10:37 +0200 (Fri, 08 Aug 2014) $
// * @version $Revision: 350 $
// * @author <a href="mailto:Rick@almende.org">Rick</a>
// */
//public class NodynServlet extends HttpServlet
//{
//
//	/** */
//	private static final long serialVersionUID = 1L;
//
//	/** */
//	private static final Logger LOG = LogUtil.getLogger(NodynServlet.class);
//
//	@Override
//	public void init() throws ServletException
//	{
//		final String path = getInitParameter("scriptPath");// "js/inertiaViss.js";
//		LOG.info("Script path: " + path);
//		System.err.println("Script path: " + path);
//		try
//		{
//			final String script = IOUtils.toString(new InputStreamReader(
//					FileUtil.getFileAsInputStream(path)));
//			LOG.trace("Got script:\n" + script);
//
//			final List<String> args = new ArrayList<>();
//			args.add("--clustered");
//			// args.add("--classpath");
//			// args.add("node_modules/evejs");
//			// args.add("--console");
//			args.add("--eval");
//			args.add(script);
//			io.nodyn.cli.Main.main(args.toArray(new String[args.size()]));
//		} catch (final Exception e)
//		{
//			LOG.error("Problem running script: " + path, e);
//			e.printStackTrace();
//		}
//	}
//
//}
