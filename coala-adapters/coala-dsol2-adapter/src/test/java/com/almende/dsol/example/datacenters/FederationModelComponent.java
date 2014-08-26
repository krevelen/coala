package com.almende.dsol.example.datacenters;

import io.coala.dsol.util.DsolModelComponent;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;
import nl.tudelft.simulation.event.EventType;

public interface FederationModelComponent extends
		DsolModelComponent<DEVSSimulatorInterface, FederationModel>
{

	/** */
	String RNG_ID2 = "rng2";

	/** */
	EventType NEW_DEMAND = new EventType("demand");

	/** */
	EventType NEW_DC = new EventType("dc");

	/** */
	EventType SERVICE_STARTED = new EventType("service started");

	/** */
	EventType SERVICE_COMPLETED = new EventType("service completed");

	/** */
	EventType CONSUMPTION_CHANGED = new EventType("consumption changed");

	/** */
	EventType CASHFLOW_CHANGED = new EventType("cashflow changed");

}
