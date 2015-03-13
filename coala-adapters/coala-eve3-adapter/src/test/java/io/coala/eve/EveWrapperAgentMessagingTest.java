package io.coala.eve;

import static org.junit.Assert.assertTrue;
import io.coala.agent.AgentID;
import io.coala.agent.AgentStatusUpdate;
import io.coala.bind.Binder;
import io.coala.bind.BinderFactory;
import io.coala.capability.admin.CreatingCapability;
import io.coala.capability.replicate.ReplicationConfig;
import io.coala.log.LogUtil;
import io.coala.model.ModelComponentIDFactory;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.junit.Test;

import rx.functions.Action1;

/**
 * {@link EveWrapperAgentMessagingTest}
 * 
 * @version $Revision: 312 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 */
// @Ignore
public class EveWrapperAgentMessagingTest
{

	/** */
	private static final Logger LOG = LogUtil
			.getLogger(EveWrapperAgentMessagingTest.class);

	/** */
	public static AgentID senderAgentID = null;

	/** */
	public static AgentID receiverAgentID = null;

	@Test
	public void messagingTest() throws Exception
	{
		LOG.trace("Start eve messaging test");

		final Binder binder = BinderFactory.Builder
				.fromFile()
				.withProperty(ReplicationConfig.class,
						ReplicationConfig.MODEL_NAME_KEY,
						"testModel" + System.currentTimeMillis()).build()
				.create("_unittest_");

		senderAgentID = binder.inject(ModelComponentIDFactory.class)
				.createAgentID("senderAgent");

		receiverAgentID = binder.inject(ModelComponentIDFactory.class)
				.createAgentID("receiverAgent");

		final CreatingCapability booterSvc = binder
				.inject(CreatingCapability.class);
		final CountDownLatch latch = new CountDownLatch(2);

		booterSvc.createAgent(receiverAgentID, TestAgent.class).subscribe(
				new Action1<AgentStatusUpdate>()
				{
					@Override
					public void call(final AgentStatusUpdate update)
					{
						System.err.println("Observed status update: " + update);

						if (update.getStatus().isFinishedStatus()
								|| update.getStatus().isFailedStatus())
							latch.countDown();
					}
				});

		booterSvc.createAgent(senderAgentID, TestAgent.class).subscribe(
				new Action1<AgentStatusUpdate>()
				{
					@Override
					public void call(final AgentStatusUpdate update)
					{
						System.err.println("Observed status update: " + update);

						if (update.getStatus().isFinishedStatus()
								|| update.getStatus().isFailedStatus())
							latch.countDown();
					}
				});

		latch.await(20, TimeUnit.SECONDS);
		assertTrue("Agent(s) did not all finish or fail", latch.getCount() == 0);
	}
}
