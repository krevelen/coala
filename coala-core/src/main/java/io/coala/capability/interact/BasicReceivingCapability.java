package io.coala.capability.interact;

import io.coala.bind.Binder;
import io.coala.capability.BasicCapability;
import io.coala.log.InjectLogger;
import io.coala.message.Message;
import io.coala.message.MessageHandler;

import java.util.Deque;
import java.util.LinkedList;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.inject.Inject;

import org.apache.log4j.Logger;

import rx.Observable;
import rx.subjects.ReplaySubject;
import rx.subjects.Subject;

/**
 * {@link BasicReceivingCapability}
 * 
 * @version $Revision: 316 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 * @author <a href="mailto:Suki@almende.org">Suki</a>
 */
public class BasicReceivingCapability extends BasicCapability implements
		ReceivingCapability, MessageHandler
{

	/** */
	private static final long serialVersionUID = 1L;

	/** */
	@InjectLogger
	private Logger LOG;

	/** */
	private Subject<Message<?>, Message<?>> incoming = ReplaySubject.create();

	/**
	 * {@link AGlobeMessengerService} constructor
	 * 
	 * @param binder
	 */
	@Inject
	protected <T extends Message<?>> BasicReceivingCapability(final Binder binder)
	{
		super(binder);
	}

	// private class InterruptibleDeliveryTask implements Runnable
	// {
	//
	// private final Message<?> message;
	//
	// private volatile boolean suspended = false;
	//
	// public InterruptibleDeliveryTask(final Message<?> message)
	// {
	// this.message = message;
	// }
	//
	// public void suspend()
	// {
	// this.suspended = true;
	// }
	//
	// public void resume()
	// {
	// this.suspended = false;
	// synchronized (this)
	// {
	// notifyAll();
	// }
	// }
	//
	// @Override
	// public void run()
	// {
	// while (!Thread.currentThread().isInterrupted())
	// {
	// if (!this.suspended)
	// {
	// incoming.onNext(this.message);
	// LOG.trace("Delivered: " + message);
	// deliveryTasks.remove(this);
	// } else
	// {
	// LOG.trace("Suspended delivery of " + this.message);
	// try
	// {
	// while (this.suspended)
	// {
	// synchronized (this)
	// {
	// wait();
	// }
	// }
	// } catch (final InterruptedException ignore)
	// {
	// }
	// }
	// }
	// LOG.trace("Cancelled delivery of " + this.message);
	// }
	//
	// }
	//
	// private static final ExecutorService MESSAGE_DELIVERY = Executors
	// .newCachedThreadPool();
	//
	// private SortedSet<InterruptibleDeliveryTask> deliveryTasks = Collections
	// .synchronizedSortedSet(new TreeSet<InterruptibleDeliveryTask>());

	/** */
	private boolean ownerReady = false;

	@Override
	public final void activate()
	{
		synchronized (this.backlog)
		{
			this.ownerReady = true;
			handleBacklog();
		}
	}

	@Override
	public final void finish()
	{
		// synchronized (this.backlog)
		// {
		// this.ownerReady = false;
		// }
	}

	private void handleBacklog()
	{
		synchronized (this.backlog)
		{
			final Deque<Message<?>> queue = new LinkedList<>(this.backlog);
			this.backlog.clear();
			while (!queue.isEmpty())
			{
				this.incoming.onNext(queue.removeFirst());
			}
		}
	}

	private final SortedSet<Message<?>> backlog = new TreeSet<Message<?>>();

	@Override
	public void onMessage(final Message<?> message)
	{
		synchronized (this.backlog)
		{
			if (this.ownerReady)
				try
				{
					// LOG.info("DELIVERING " + message);
					// final InterruptibleDeliveryTask deliveryTask = new
					// InterruptibleDeliveryTask(
					// message);
					// if (!getStatus().isStartedStatus())
					// deliveryTask.suspend();
					// MESSAGE_DELIVERY.submit(deliveryTask);
					// this.deliveryTasks.add(deliveryTask);

					// LOG.info("DELIVERING: " + message);
					this.incoming.onNext(message);
					// LOG.info("DELIVERED: " + message);
				} catch (final Throwable t)
				{
					t.printStackTrace();
					this.incoming.onError(t);
					LOG.error("Problem delivering: " + message, t);
				}
			else
			{
				System.err.println("BACKLOGGING MSG " + message);
				this.backlog.add(message);
			}
		}
	}

	@Override
	public Observable<Message<?>> getIncoming()
	{
		return this.incoming.asObservable();
	}
}