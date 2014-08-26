package io.coala.aglobe;

import io.coala.agent.AgentID;
import io.coala.agent.AgentStatusUpdate;
import io.coala.bind.BinderFactory;
import io.coala.capability.admin.CreatingCapability;
import io.coala.log.LogUtil;

import java.util.concurrent.CountDownLatch;

import org.apache.log4j.Logger;
import org.junit.Test;

import rx.Observer;

/**
 * {@link AGlobeWrapperAgentTest}
 * 
 * @version $Revision: 331 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 */
public class AGlobeWrapperAgentTest
{

	/** */
	private static final Logger LOG = LogUtil
			.getLogger(AGlobeWrapperAgentTest.class);

	/** */
	public static AgentID senderAgentID = null;

	/** */
	public static AgentID receiverAgentID = null;

	@Test
	public void sendMessage() throws Exception
	{
		final BinderFactory factory = BinderFactory.Builder.fromFile()
				.withModelName("bla" + System.currentTimeMillis()).build();

		final CreatingCapability booter = factory.create("bootAgent").inject(
				CreatingCapability.class);

		final CountDownLatch latch = new CountDownLatch(2);

		booter.createAgent("receiverAgent", TestAgent.class).subscribe(
				new Observer<AgentStatusUpdate>()
				{
					@Override
					public void onNext(final AgentStatusUpdate update)
					{
						if (receiverAgentID == null)
							receiverAgentID = update.getAgentID();

						LOG.trace("Observed status update: " + update);

						if (update.getStatus().isFinishedStatus()
								|| update.getStatus().isFailedStatus())
							latch.countDown();
					}

					@Override
					public void onCompleted()
					{
					}

					@Override
					public void onError(final Throwable e)
					{
						e.printStackTrace();
					}
				});

		booter.createAgent("senderAgent", TestAgent.class).subscribe(
				new Observer<AgentStatusUpdate>()
				{
					@Override
					public void onNext(final AgentStatusUpdate update)
					{
						if (senderAgentID == null)
							senderAgentID = update.getAgentID();

						LOG.trace("Observed status update: " + update);

						if (update.getStatus().isFinishedStatus()
								|| update.getStatus().isFailedStatus())
							latch.countDown();
					}

					@Override
					public void onCompleted()
					{
					}

					@Override
					public void onError(final Throwable e)
					{
						e.printStackTrace();
					}
				});

		latch.await();

	}

}
