/* $Id$
 * $URL: https://dev.almende.com/svn/abms/dsol-util/src/main/java/io/coala/dsol/util/ExperimentBuilder.java $
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
 * Copyright (c) 2010-2013 Almende B.V. 
 */
package io.coala.dsol.util;

import io.coala.json.JsonUtil;
import io.coala.log.LogUtil;

import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import nl.tudelft.simulation.dsol.ModelInterface;
import nl.tudelft.simulation.dsol.experiment.Experiment;
import nl.tudelft.simulation.dsol.experiment.Replication;
import nl.tudelft.simulation.dsol.experiment.Treatment;
import nl.tudelft.simulation.dsol.simulators.SimulatorInterface;
import nl.tudelft.simulation.event.EventType;

import org.apache.log4j.Logger;

import com.eaio.uuid.UUID;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * {@link ExperimentBuilder}
 * 
 * @date $Date: 2014-05-07 11:59:26 +0200 (Wed, 07 May 2014) $
 * @version $Revision: 258 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 */
@SuppressWarnings("serial")
public class ExperimentBuilder extends Experiment
{
	/** */
	private static final String EXPERIMENT_CONTEXT_PREFIX = "/exp=";

	/** */
	private static final Logger LOG = LogUtil.getLogger(ExperimentBuilder.class);

	/**
	 * {@link ExperimentBuilder} constructor
	 * 
	 * @throws NamingException
	 */
	public ExperimentBuilder() throws NamingException
	{
		this(new UUID().toString());
	}

	/**
	 * {@link ExperimentBuilder} constructor
	 * 
	 * @param context
	 * @throws NamingException
	 */
	public ExperimentBuilder(final String context) throws NamingException
	{
		super(new InitialContext().createSubcontext(EXPERIMENT_CONTEXT_PREFIX
				+ context));
	}

	@Override
	@JsonIgnore
	public Context getContext()
	{
		return super.getContext();
	}

	public ExperimentBuilder withAnalyst(final String analyst)
	{
		setAnalyst(analyst);
		return this;
	}

	public ExperimentBuilder withDescription(final String description)
	{
		setDescription(description);
		return this;
	}

	public ExperimentBuilder withModel(final ModelInterface model)
	{
		setModel(model);
		return this;
	}

	@Override
	@JsonIgnore
	public ModelInterface getModel()
	{
		return super.getModel();
	}

	public ExperimentBuilder withSimulator(final SimulatorInterface simulator)
	{
		setSimulator(simulator);
		return this;
	}

	@Override
	@JsonIgnore
	public SimulatorInterface getSimulator()
	{
		return super.getSimulator();
	}

	/** @param result */
	public ExperimentBuilder withReplications(final Replication... replications)
	{
		List<Replication> list = getReplications();
		if (list == null)
		{
			list = new ArrayList<Replication>();
			setReplications(list);
		}
		if (replications == null || replications.length == 0)
			LOG.warn("No replications to add", new IllegalArgumentException());
		else
			for (Replication replication : replications)
				list.add(replication);
		return this;
	}

	@Override
	@JsonIgnore
	public List<Replication> getReplications()
	{
		return super.getReplications();
	}

	@Override
	@JsonIgnore
	public EventType[] getEventTypes()
	{
		return super.getEventTypes();
	}

	protected ExperimentBuilder withTreatment(final Treatment treatment)
	{
		if (treatment.getExperiment() != null)
			LOG.warn("Replacing treatment's current experiment: "
					+ treatment.getExperiment().getContext());
		setTreatment(treatment);
		// treatment.setExperiment(this); // redundant
		return this;
	}

	@Override
	@JsonIgnore
	public TreatmentBuilder getTreatment()
	{
		return (TreatmentBuilder) super.getTreatment();
	}

	public TreatmentBuilder newTreatment()
	{
		return withTreatment(new TreatmentBuilder(this)).getTreatment();
	}

	@Override
	public String toString()
	{
		try
		{
			// final JsonNode node = JsonUtil.getJOM().valueToTree(this);
			return JsonUtil.getJOM().writerWithDefaultPrettyPrinter()
					.writeValueAsString(this);
		} catch (final JsonProcessingException e)
		{
			LOG.warn("Problem marshalling " + getClass().getName(), e);
			try
			{
				return getContext().getNameInNamespace();
			} catch (final NamingException e1)
			{
				return super.toString();
			}
		}
	}

}
