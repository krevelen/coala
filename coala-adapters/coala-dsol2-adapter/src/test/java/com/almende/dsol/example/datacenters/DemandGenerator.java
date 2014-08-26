package com.almende.dsol.example.datacenters;

import io.coala.log.LogUtil;

import java.rmi.RemoteException;

import nl.tudelft.simulation.dsol.SimRuntimeException;
import nl.tudelft.simulation.jstats.distributions.DistContinuous;
import nl.tudelft.simulation.jstats.distributions.DistTriangular;
import nl.tudelft.simulation.jstats.streams.StreamInterface;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Interval;

public class DemandGenerator extends AbstractFederationModelComponent
{

	/** */
	private static final Logger LOG = LogUtil.getLogger(DemandGenerator.class);

	/** */
	private static final long serialVersionUID = 1L;

	/** */
	private static int ID_COUNT = 1;

	/** the number of new services added per morning */
	private DistContinuous morningCount;

	/** the hour at which the service starts */
	private DistContinuous morningStartHour;

	/** the duration of a new service */
	private DistContinuous morningDurationHours;

	/** the number of new services added per evening */
	private DistContinuous eveningCount;

	/** the hour at which the service starts */
	private DistContinuous eveningStartHour;

	/** the duration of a new service */
	private DistContinuous eveningDurationHours;

	public DemandGenerator(final FederationModel model) throws RemoteException,
			SimRuntimeException
	{
		super(model, "GEN"+ID_COUNT++);

		final StreamInterface stream = model.getSimulator().getReplication()
				.getStream(FederationModel.RNG_ID);

		this.morningCount = new DistTriangular(stream, 10, 30, 50);
		this.morningStartHour = new DistTriangular(stream, 0.0, 8.0, 12.0);
		this.morningDurationHours = new DistTriangular(stream, 0.5, 3.0, 12.0);

		this.eveningCount = new DistTriangular(stream, 10, 20, 30);
		this.eveningStartHour = new DistTriangular(stream, 12.0, 18.0, 24.0);
		this.eveningDurationHours = new DistTriangular(stream, 0.5, 3.0, 12.0);

		getSimulator().scheduleEvent(0, this, this,
				GENERATE_DEMAND_METHOD_ID, FederationModel.NO_ARGS);

		// LOG.trace("New demand generator created");
	}

	private static final String GENERATE_DEMAND_METHOD_ID = "generateDemand";

	protected void generateDemand()
	{
		final DateTime now = getDateTime();
//		LOG.trace(String.format("t=%.4f (%s) Generating demand", getModel()
//				.getSimTime(), now));
		
		final long morningCount = (long) this.morningCount.draw();
		for (int i = 0; i < morningCount; i++)
			newDemand(now, this.morningStartHour, this.morningDurationHours);
		final long eveningCount = (long) this.eveningCount.draw();
		for (int i = 0; i < eveningCount; i++)
			newDemand(now, this.eveningStartHour, this.eveningDurationHours);

		// fire event

		// repeat
		final double delay = simTime(Duration.standardDays(1));
		// LOG.trace(String.format("Repeat demand generation in %.2f %s", delay,
		// this.model.getTimeUnit()));
		try
		{
			getSimulator().scheduleEvent(delay, this, this,
					GENERATE_DEMAND_METHOD_ID, FederationModel.NO_ARGS);
		} catch (final RemoteException | SimRuntimeException e)
		{
			LOG.error("Problem scheduling new demand with delay: " + delay, e);
		}
	}

	private void newDemand(final DateTime now, final DistContinuous startHour,
			final DistContinuous durationHours)
	{
		double startHours = startHour.draw(), duration = durationHours.draw();
		if (startHours < now.getHourOfDay())
			startHours += 24;
		final DateTime start = now
				.plusMillis((int) (startHours * 60 * 60 * 1000));
		final Interval interval = new Interval(start,
				start.plusMillis((int) (duration * 60 * 60 * 1000)));
		// LOG.trace(String.format("t=%.4f (%s) new demand "
		// + "start %.2fh duration %.2fh >> interval %s", getModel()
		// .getSimTime(), now, startHours, duration, interval));
		fireEvent(NEW_DEMAND, interval);
	}
}
