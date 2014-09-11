/* $Id$
 * $URL: https://dev.almende.com/svn/abms/aglobe-util/src/main/java/com/almende/coala/aglobe/AGlobeUtil.java $
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
package io.coala.aglobe;

import io.coala.agent.AgentID;
import io.coala.exception.CoalaException;
import io.coala.exception.CoalaExceptionFactory;
import io.coala.log.LogUtil;
import io.coala.model.ModelID;
import io.coala.util.Util;
import io.coala.web.WebUtil;

import java.io.Externalizable;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.CountDownLatch;

import org.apache.log4j.Logger;

import rx.Observable;
import rx.functions.Action1;
import rx.subjects.BehaviorSubject;
import rx.subjects.Subject;
import aglobe.container.AgentContainer;
import aglobe.container.agent.Agent;
import aglobe.container.gui.ListListener;
import aglobe.container.transport.Address;
import aglobe.ontology.AgentInfo;
import aglobe.ontology.AgentList;
import aglobe.ontology.Libraries;
import aglobe.ontology.Message;
import aglobe.ontology.ServiceList;
import aglobe.platform.ContainerMonitor;
import aglobe.platform.Platform;
import aglobe.platform.PlatformMonitor;
import aglobe.platform.PlatformMonitor.PlatformState;

import com.eaio.uuid.UUID;

/**
 * {@link AGlobeUtil}
 * 
 * @version $Revision: 299 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 * 
 */
public class AGlobeUtil implements Util
{

	/** */
	private static final Logger LOG = LogUtil.getLogger(AGlobeUtil.class);

	/** */
	private static final String PLATFORM_NAME = "coala"; // FIXME from Config

	/** */
	private static PlatformState PLATFORM_STATE = null;

	/** */
	private static Subject<PlatformState, PlatformState> PLATFORM_STATE_SUBJECT = BehaviorSubject
			.create((PlatformState) PlatformState.STOPPED);

	/** */
	private static Map<String, Subject<Boolean, Boolean>> CONTAINER_STATE_SUBJECT = Collections
			.synchronizedSortedMap(new TreeMap<String, Subject<Boolean, Boolean>>());
	/** */
	private static Map<String, Subject<AGlobeAgentState, AGlobeAgentState>> AGENT_STATE_SUBJECT = Collections
			.synchronizedSortedMap(new TreeMap<String, Subject<AGlobeAgentState, AGlobeAgentState>>());

	/** start this JVM's AGlobe platform */
	protected static void startPlatform()
	{
		if (PLATFORM_STATE != null && PLATFORM_STATE != PlatformState.STOPPED)
		{
			LOG.warn("Already started AGlobe platform on port: "
					+ Platform.BASE_PLATFORM_PORT);
			return;
		}

		final String[] args = { "-name", PLATFORM_NAME };
		new Thread()
		{
			@Override
			public void run()
			{
				LOG.trace("Registering AGlobe platform monitor...");
				registerPlatformMonitor();

				LOG.trace("Registering AGlobe container monitor...");
				registerContainerMonitor();

				LOG.trace(String.format("Starting AGlobe platform v%d.%d, "
						+ "args: %s", Platform.MAJOR_VERSION,
						Platform.MINOR_VERSION, Arrays.asList(args)));
				Platform.internalMain(args);
			}
		}.start();
	}

	/** monitor this JVM's AGlobe platform status */
	protected static void registerPlatformMonitor()
	{
		Platform.registerPlatformMonitor(new PlatformMonitor()
		{
			@Override
			public void platformStateChanged(final PlatformState currentState)
			{
				setPlatformState(currentState);
			}
		});
	}

	/** monitor this JVM's AGlobe containers */
	protected static void registerContainerMonitor()
	{
		Platform.registerContainerMonitor(new ContainerMonitor()
		{

			@Override
			public void containerStarted(final AgentContainer container)
			{
				final String name = container.getContainerName();
				synchronized (CONTAINER_STATE_SUBJECT)
				{
					if (!CONTAINER_STATE_SUBJECT.containsKey(name))
						CONTAINER_STATE_SUBJECT.put(name,
								BehaviorSubject.create(false));
					CONTAINER_STATE_SUBJECT.get(name).onNext(true);
				}

				container.getAgentManager().addListListener(new ListListener()
				{
					@Override
					public void ValueChanged(final ListType where,
							final Object what)
					{
						// empty
					}

					@Override
					public void ListChanged(final ListType which,
							final Set<String> values)
					{
						if (which != ListType.AGENTS)
							return;
						synchronized (AGENT_STATE_SUBJECT)
						{
							for (String agentName : AGENT_STATE_SUBJECT
									.keySet())
							{
								if (!values.contains(agentName))
								{
									LOG.warn("Agent died? " + agentName);
									// FIXME update observers?
									continue;
								}

								final Agent agent = container.getAgentManager()
										.getAgentInstance(agentName);
								final AGlobeAgentState agentState = AGlobeAgentState
										.valueOf(agent.getState());
								setAgentState(agentName, agentState);

								/*
								if (wrapper.getAddress().getName() != null && 
								wrapper.getState() == ElementaryEntity.RUNNING)
								*/
							}
						}
					}
				});
			}

			@Override
			public void containerRemoved(final String name)
			{
				synchronized (CONTAINER_STATE_SUBJECT)
				{
					if (!CONTAINER_STATE_SUBJECT.containsKey(name))
					{
						LOG.warn("Container removed that was never started?");
						CONTAINER_STATE_SUBJECT.put(name,
								BehaviorSubject.create(false));
					} else
						CONTAINER_STATE_SUBJECT.get(name).onNext(true);
				}
			}
		});
	}

	/** @param state the AGlobe platform's current {@link PlatformState} */
	protected static synchronized void setPlatformState(
			final PlatformState state)
	{
		PLATFORM_STATE = state;
		PLATFORM_STATE_SUBJECT.onNext(state);
	}

	/** @return the AGlobe platform's current {@link PlatformState} */
	public static synchronized PlatformState getPlatformState()
	{
		return PLATFORM_STATE;
	}

	/** */
	public static Observable<PlatformState> getPlatformStates()
	{
		return PLATFORM_STATE_SUBJECT.asObservable();
	}

	/**
	 * @param containerName the containerName
	 * @return the {@link AgentContainer} belonging to specified model
	 */
	public static AgentContainer getContainer(final String containerName)
	{
		return Platform.getContainer(containerName);
	}

	/**
	 * @param modelID the model identifier to which the container is dedicated
	 * @return the {@link AgentContainer} belonging to specified model
	 */
	public static Observable<Boolean> getContainerStates(
			final String containerName)
	{
		synchronized (CONTAINER_STATE_SUBJECT)
		{
			if (!CONTAINER_STATE_SUBJECT.containsKey(containerName))
				CONTAINER_STATE_SUBJECT.put(containerName,
						BehaviorSubject.create(false));
			return CONTAINER_STATE_SUBJECT.get(containerName).asObservable();
		}
	}

	/**
	 * @param agentName
	 * @return
	 */
	public static void setAgentState(final String agentName,
			AGlobeAgentState agentState)
	{
		synchronized (AGENT_STATE_SUBJECT)
		{
			if (!AGENT_STATE_SUBJECT.containsKey(agentName))
				AGENT_STATE_SUBJECT.put(agentName,
						BehaviorSubject.create(AGlobeAgentState.INIT));
			AGENT_STATE_SUBJECT.get(agentName).onNext(agentState);
		}
	}

	/**
	 * @param agentName
	 * @return
	 */
	public static Observable<AGlobeAgentState> getAgentStates(
			final String agentName)
	{
		synchronized (AGENT_STATE_SUBJECT)
		{
			if (!AGENT_STATE_SUBJECT.containsKey(agentName))
				AGENT_STATE_SUBJECT.put(agentName,
						BehaviorSubject.create(AGlobeAgentState.INIT));
			return AGENT_STATE_SUBJECT.get(agentName).asObservable();
		}
	}

	/**
	 * @param agentID the identifier of the (wrapped) agent
	 * @return the specified AGlobe (wrapper) {@link Agent} or {@code null} if
	 *         not exists (locally)
	 */
	public static <T extends Agent> T getAgent(final AgentID agentID,
			final Class<T> agentType)
	{
		if (agentID == null)
			return null;

		final AgentContainer container = Platform
				.getContainer(toContainerName(agentID.getModelID()));

		if (container == null)
			return null;

		return agentType.cast(container.getAgentManager().getAgentInstance(
				toAgentName(agentID)));
	}

	/**
	 * @param agentID
	 * @return
	 */
	public static boolean isWrapperAgentCreated(final AgentID agentID)
	{
		return getAgent(agentID, Agent.class) != null;
	}

	/**
	 * @param modelID
	 * @return
	 */
	public static String toContainerName(final ModelID modelID)
	{
		return WebUtil.urlEncode(modelID.getValue());
	}

	/** */
	private static final Map<String, AgentID> AGENT_ID_CACHE = new HashMap<>();

	/**
	 * @param id
	 * @return
	 */
	public static AgentID toAgentID(final String id)
	{
		// FIXME create/employ global lookup service/agent

		final AgentID result = AGENT_ID_CACHE.get(id);
		if (result == null)
			LOG.warn("Unknown wrapped agentID for Eve agent Id: " + id);

		return AGENT_ID_CACHE.get(id);
	}

	/**
	 * @param id
	 * @return
	 */
	public static String toAgentName(final AgentID agentID)
	{
		final String result = WebUtil.urlEncode(agentID.getValue());
		AGENT_ID_CACHE.put(result, agentID);
		return result;
	}

	/**
	 * @param agentID
	 * @param agentType
	 * @throws CoalaException
	 */
	public static <T extends Agent> T createAgent(final AgentID agentID,
			final Class<T> agentType) throws CoalaException
	{
		T agent = getAgent(agentID, agentType);
		if (agent != null)
		{
			LOG.warn("Already created agent: " + agentID);
			return agent;
		}

		final String agentName = toAgentName(agentID);
		// final CountDownLatch latch = new CountDownLatch(1);

		final AgentInfo agentInfo = new AgentInfo();
		agentInfo.setName(agentName);
		agentInfo.setAutoRun(true);
		agentInfo.setMainClass(agentType.getName());
		agentInfo.setLibraries(new Libraries());

		final String containerName = toContainerName(agentID.getModelID());
		AgentContainer container = getContainer(containerName);
		try
		{
			if (container == null)
				container = createContainer(containerName, true);

			container.getAgentManager().createAgent(agentInfo);

			// getAgentStates(agentName).subscribe(new
			// Action1<AGlobeAgentState>()
			// {
			// @Override
			// public void call(final AGlobeAgentState agentState)
			// {
			//
			// if (agentState != AGlobeAgentState.INIT)
			// latch.countDown();
			// else
			// {
			// LOG.trace("Waiting for agent, current state: "
			// + agentState);
			// }
			// }
			// });
			T result = agentType.cast(container.getAgentManager()
					.getAgentInstance(agentName));

			// while (result == null)
			// {
			// try
			// {
			// Thread.sleep(10);
			// } catch (final InterruptedException ignore)
			// {
			// }
			// LOG.trace("Waiting for instantiation of agent: " + agentName);
			// result = agentType.cast(container.getAgentManager()
			// .getAgentInstance(agentName));
			// }

			// while (latch.getCount() > 0)
			// {
			// try
			// {
			// latch.await();
			// } catch (final InterruptedException ignore)
			// {
			// }
			// }

			return result;
		} catch (final CoalaException e)
		{
			throw e;
		} catch (final Throwable t)
		{
			throw CoalaExceptionFactory.AGENT_CREATION_FAILED
					.create(t, agentID);
		}
	}

	/**
	 * @param containerName
	 * @param b
	 * @return
	 * @throws Exception
	 */
	public static AgentContainer createContainer(final String containerName,
			final boolean waitUntilRunning) throws Exception
	{
		AgentContainer result = Platform.getContainer(containerName);
		if (result != null)
		{
			LOG.warn("AGlobe container already created: " + containerName);
			return result;
		}
		if (containerName == null)
			throw CoalaExceptionFactory.VALUE_NOT_SET.create("containerName");

		final CountDownLatch latch = new CountDownLatch(1);
		if (waitUntilRunning)
			getContainerStates(containerName).subscribe(new Action1<Boolean>()
			{
				@Override
				public void call(final Boolean isRunning)
				{
					if (isRunning)
						latch.countDown();
				}
			});

		final String[] containerArgs = { "-name", containerName };
		final AgentList agentList = new AgentList();
		final ServiceList serviceList = new ServiceList();
		final Address librarySourceContainer = Address.getAddress("libSrc");
		final boolean waitForPreviousFinish = true;
		try
		{
			if (Platform.getPlatformThreadGroup() == null) // boot first?
			{
				final String bootContainerName = new UUID().toString();
				final String[] bootArgs = { "-name", bootContainerName };
				Platform.internalMain(bootArgs);
			}
			Platform.startNewContainer(containerArgs, agentList, serviceList,
					librarySourceContainer, waitForPreviousFinish);
		} catch (final Throwable t)
		{
			throw CoalaExceptionFactory.VALUE_NOT_ALLOWED.create(t,
					"containerName", containerName, "AGlobe platform "
							+ containerName + "already running?");
		}

		result = Platform.getContainer(containerName);
		if (waitUntilRunning)
			while (result == null || latch.getCount() > 0)
			{
				try
				{
					latch.await();
				} catch (final InterruptedException ignore)
				{
				}
				result = Platform.getContainer(containerName);
			}
		return result;
	}

	/**
	 * @param senderID
	 * @param receiverID
	 * @param payload
	 * @return
	 * @throws CoalaException
	 */
	public static Message createMessage(final byte[] payload,
			final AgentID senderID, final AgentID receiverID)
			throws CoalaException
	{
		return createMessage(payload, getAddress(senderID),
				getAddress(receiverID));
	}

	/**
	 * @param senderID
	 * @param receiverID
	 * @param payload
	 * @return
	 * @throws CoalaException
	 */
	public static Message createMessage(final Externalizable payload,
			final AgentID senderID, final AgentID receiverID)
			throws CoalaException
	{
		return createMessage(payload, getAddress(senderID),
				getAddress(receiverID));
	}

	/**
	 * @param senderID
	 * @param receiverID
	 * @param payload
	 * @return
	 * @throws CoalaException
	 */
	public static Message createMessage(final Serializable payload,
			final AgentID senderID, final AgentID receiverID)
	{
		if (senderID == null)
			throw CoalaExceptionFactory.VALUE_NOT_SET.createRuntime("senderID");
		if (receiverID == null)
			throw CoalaExceptionFactory.VALUE_NOT_SET
					.createRuntime("receiverID");
		return createMessage(payload, getAddress(senderID),
				getAddress(receiverID));
	}

	/**
	 * @param senderID
	 * @return
	 * @throws CoalaException
	 */
	public static Address getAddress(final AgentID agentID)
	{
		final Agent agent = getAgent(agentID, Agent.class);
		if (agent == null)
			throw CoalaExceptionFactory.VALUE_NOT_SET
					.createRuntime("wrapped agent for " + agentID);
		return agent.getAddress();
	}

	/**
	 * @param senderAddress
	 * @param receiverAddress
	 * @param content
	 * @return
	 * @throws CoalaException
	 */
	protected static Message createMessage(final Object content,
			final Address senderAddress, final Address... receiverAddresses)
	{
		// sanity check
		if (receiverAddresses == null || receiverAddresses.length == 0)
			throw CoalaExceptionFactory.VALUE_NOT_SET
					.createRuntime("receiverAddresses");

		final Message result = Message.newInstance();
		result.setContent(content);
		result.setSender(senderAddress);
		if (receiverAddresses.length == 1)
			result.setReceiver(receiverAddresses[0]);
		else
			result.setReceivers(Arrays.asList(receiverAddresses));

		return result;
	}

	/**
	 * {@link AGlobeUtil} constructor
	 */
	private AGlobeUtil()
	{
		// should not produce protected/public instances
	}

	/**
	 * @param id
	 * @return
	 */
	public static AgentID killAgent(final AgentID id)
	{
		setAgentState(id.getValue(), AGlobeAgentState.DONE);
		final AgentContainer container = getContainer(id.getModelID()
				.getValue());
		container.getAgentManager().killAgent(id.getValue());
		setAgentState(id.getValue(), AGlobeAgentState.DEAD);
		synchronized (AGENT_STATE_SUBJECT)
		{
			AGENT_STATE_SUBJECT.remove(id.getValue());
			if (AGENT_STATE_SUBJECT.size() == 0)
			{
				LOG.trace("Last one out closes the door: "
						+ "Shutting down A-Globe container");
				container.shutdown();
				Platform.killPlatform();
			}
		}
		return id;
	}

}
