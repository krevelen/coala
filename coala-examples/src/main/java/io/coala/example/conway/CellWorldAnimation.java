package io.coala.example.conway;
///*
// * Copyright: Almende B.V. (2014), Rotterdam, The Netherlands
// * License: The Apache Software License, Version 2.0
// */
//package com.almende.eve.goldemo;
//
//import java.io.BufferedReader;
//import java.io.InputStreamReader;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import org.apache.log4j.Logger;
//import org.junit.Test;
//
//import com.almende.eve.agent.AgentHost;
//
///**
// * {@link Goldemo}
// * 
// * @date $Date: 2014-06-03 13:55:16 +0200 (Tue, 03 Jun 2014) $
// * @version $Revision: 295 $
// * @author <a href="mailto:Ludo@almende.org">Ludo</a>
// * @author <a href="mailto:Rick@almende.org">Rick</a>
// */
//public class Goldemo
//{
//
//	/** */
//	private static final Logger LOG = Logger.getLogger(Goldemo.class);
//
//	/**
//	 * The Constant PATH.
//	 */
//	// final static String BASE = "inproc://";
//	// final static String BASE = "ipc:///tmp/zmq-socket-";
//	// final static String PATH = "zmq:"+BASE;
//	// final static String PATH = "local:";
//	final static String PATH = "http://127.0.0.1:8081/agents/";
//
//	private static final int DURATION_SEC = 60;
//	//private static final String path = "eve.yaml";
//	private static final int N = 3;
//	private static final int M = 3;
//	private static final boolean annimate = false;
//	private static final boolean shortcut = true;
//
//	@Test
//	public void testGameOfLife() throws Exception
//	{
//		final AgentHost host = AgentHost.getInstance();
//		//host.loadConfig(new Config(path));
//		host.setDoesShortcut(shortcut);
//
//		boolean[][] grid = new boolean[N][M];
//		final BufferedReader br = new BufferedReader(new InputStreamReader(
//				System.in));
//
//		String input;
//
//		int cN = 0;
//		while ((input = br.readLine()) != null && cN < N)
//		{
//			String trimmedInput = input.trim();
//			if (trimmedInput.isEmpty())
//				break;
//			if (trimmedInput.length() != M)
//				throw new IllegalArgumentException(
//						"Incorrect input line detected:" + input);
//			for (int cM = 0; cM < M; cM++)
//			{
//				grid[cN][cM] = (trimmedInput.charAt(cM) == '+');
//				CellFactoryTest.createAgent(host, PATH, N, M, cN, cM,
//						(trimmedInput.charAt(cM) == '+'));
//			}
//			cN++;
//		}
//		for (cN = 0; cN < N; cN++)
//		{
//			for (int cM = 0; cM < M; cM++)
//			{
//				CellAgentEve cell = (CellAgentEve) host.getAgent("agent_" + cN + "_" + cM);
//				cell.register();
//			}
//		}
//		for (cN = 0; cN < N; cN++)
//		{
//			for (int cM = 0; cM < M; cM++)
//			{
//				CellAgentEve cell = (CellAgentEve) host.getAgent("agent_" + cN + "_" + cM);
//				cell.start();
//			}
//		}
//		LOG.trace("Started!");
//		try
//		{
//			Thread.sleep(DURATION_SEC * 1000);
//		} catch (final InterruptedException e)
//		{
//			LOG.trace("Early interrupt", e);
//		}
//		for (cN = 0; cN < N; cN++)
//		{
//			for (int cM = 0; cM < M; cM++)
//			{
//				CellAgentEve cell = (CellAgentEve) host.getAgent("agent_" + cN + "_" + cM);
//				cell.stop();
//			}
//		}
//		final Map<String, List<CycleState>> results = new HashMap<String, List<CycleState>>();
//		int max_full = 0;
//		for (cN = 0; cN < N; cN++)
//		{
//			for (int cM = 0; cM < M; cM++)
//			{
//				CellAgentEve cell = (CellAgentEve) host.getAgent("agent_" + cN + "_" + cM);
//				List<CycleState> res = cell.getAllCycleStates();
//				max_full = (max_full == 0 || max_full > res.size() ? res.size()
//						: max_full);
//				results.put(cell.getId(), res);
//			}
//		}
//		int cycle = 0;
//		for (int j = 0; j < max_full; j++)
//		{
//			final StringBuilder out = new StringBuilder("Cycle:" + cycle + "/"
//					+ (max_full - 1));
//			out.append("\n/");
//			for (int i = 0; i < M * 2; i++)
//				out.append('-');
//			out.append("-\\\n");
//			for (cN = 0; cN < N; cN++)
//			{
//				out.append("| ");
//				for (int cM = 0; cM < M; cM++)
//				{
//					final List<CycleState> states = results.get(CellFactoryTest.getName(
//							cN, cM));
//					if (states.size() <= cycle)
//						break;
//					out.append(states.get(cycle).isAlive() ? "# " : "- ");
//				}
//				out.append("|\n");
//			}
//			out.append("\\\n");
//			for (int i = 0; i < M * 2; i++)
//				out.append('-');
//			out.append("-/\n");
//			if (annimate)
//			{
//				final String ESC = "\033[";
//				System.out.print(ESC + "2J");
//			}
//			System.out.println(out.toString());
//			if (annimate)
//			{
//				try
//				{
//					Thread.sleep(500);
//				} catch (InterruptedException e)
//				{
//				}
//			}
//			cycle++;
//		}
//	}
//
//}
