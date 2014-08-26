package com.almende.dsol.example.datacenters;

import io.coala.dsol.util.DsolIndicator;

import java.rmi.RemoteException;

import nl.tudelft.simulation.dsol.SimRuntimeException;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;

public class Indicator extends
		DsolIndicator<DEVSSimulatorInterface, FederationModel, Indicator>
		implements FederationModelComponent
{

	/** */
	private static final long serialVersionUID = 1L;

	/**
	 * {@link Indicator} constructor
	 * 
	 * @param model
	 * @param name
	 * @param title
	 * @param unitName
	 * @param initialValue
	 * @throws SimRuntimeException
	 * @throws RemoteException
	 */
	public Indicator(final FederationModel model, final String name,
			final String title, final String unitName, final Number initialValue)
			throws RemoteException, SimRuntimeException
	{
		super(model, name, title, unitName, initialValue);
	}

}
