package com.almende.dsol.example.datacenters;

import io.coala.dsol.util.DsolAccumulator;
import nl.tudelft.simulation.dsol.experiment.TimeUnitInterface;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;

public class Accumulator extends
		DsolAccumulator<DEVSSimulatorInterface, FederationModel, Accumulator>
{

	/** */
	private static final long serialVersionUID = 1L;

	public Accumulator(final FederationModel model, final String title,
			final String unit, final String name, final Number initialRate,
			final TimeUnitInterface rateTimeUnit) throws Exception
	{
		this(null, model, title, unit, name, initialRate, rateTimeUnit);
	}

	public Accumulator(final Number initialValue, final FederationModel model,
			final String title, final String unit, final String name,
			final Number initialRate, final TimeUnitInterface rateTimeUnit)
			throws Exception
	{
		this(model, name, title, unit, initialValue, title, unit, initialRate,
				rateTimeUnit);
	}

	/**
	 * {@link Accumulator} constructor
	 * 
	 * @param model
	 * @param name
	 * @param valueTitle
	 * @param valueUnitName
	 * @param initialValue
	 * @param rateTitle
	 * @param rateUnitName
	 * @param initialRate
	 * @param timeUnit
	 * @throws Exception
	 */
	public Accumulator(final FederationModel model, final String name,
			final String valueTitle, final String valueUnitName,
			final Number initialValue, final String rateTitle,
			final String rateUnitName, final Number initialRate,
			final TimeUnitInterface timeUnit) throws Exception
	{
		super(model, name, valueTitle, valueUnitName, initialValue, rateTitle,
				rateUnitName, initialRate, timeUnit);
	}
}
