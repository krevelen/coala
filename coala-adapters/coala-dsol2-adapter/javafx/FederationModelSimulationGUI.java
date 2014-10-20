/* $Id: 24d70e5b07406f7a57f57e8f825fc27c992540c6 $
 * $URL: https://dev.almende.com/svn/abms/dsol-util/src/test/java/com/almende/dsol/example/datacenters/FederationModelSimulationGUI.java $
 * 
 * Part of the EU project All4Green, see http://www.all4green-project.eu/
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
 * Copyright � 2010-2012 Almende B.V. 
 */
package com.almende.dsol.example.datacenters;

import io.coala.dsol.util.DsolAccumulator;
import io.coala.dsol.util.DsolIndicator;
import io.coala.dsol.util.DsolUtil;
import io.coala.log.LogUtil;

import java.io.FileOutputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedAreaChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.Reflection;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import javax.naming.Context;
import javax.naming.InitialContext;

import nl.tudelft.simulation.dsol.SimRuntimeException;
import nl.tudelft.simulation.dsol.eventlists.RedBlackTree;
import nl.tudelft.simulation.dsol.experiment.Experiment;
import nl.tudelft.simulation.dsol.experiment.Replication;
import nl.tudelft.simulation.dsol.experiment.TimeUnitInterface;
import nl.tudelft.simulation.dsol.experiment.Treatment;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulator;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;
import nl.tudelft.simulation.dsol.simulators.Simulator;
import nl.tudelft.simulation.event.EventInterface;
import nl.tudelft.simulation.event.EventListenerInterface;
import nl.tudelft.simulation.jstats.streams.MersenneTwister;
import nl.tudelft.simulation.jstats.streams.StreamInterface;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Duration;

/**
 * {@link FederationModelSimulationGUI}
 * 
 * @date $Date: 2014-05-07 11:59:26 +0200 (Wed, 07 May 2014) $
 * @version $Revision: 258 $
 * @author <a href="mailto:rick@almende.org">Rick van Krevelen</a>
 */
//@SuppressWarnings("restriction")
public class FederationModelSimulationGUI extends Application
{

	/** */
	private static final Logger LOG = LogUtil.getLogger(FederationModelSimulationGUI.class);

	/** */
	private static final long SEED_COUNT = 0;

	/** */
	private static final long END_SEED = SEED_COUNT + 1;

	/** */
	private static final Duration RUN_LENGTH = Duration.standardDays(8);

	/** */
	private static final Duration WARMUP_PERIOD = Duration.standardDays(1);

	/** */
	private static final Duration STATS_INTERVAL = Duration.standardHours(1);

	/** */
	private static final Boolean STATS_ACCUMULATED = false;

	/** */
	private static final int FEDERATION_SIZE = 10;

	/** */
	private static final AllocationPolicy POLICY = AllocationPolicy.LOCAL;

	/** */
	private static final ChartIndicator INDICATOR = ChartIndicator.CURRENT_ENERGY_CONSUMPTION;

	/** */
	private static final String CSV_FILE_NAME = "test.csv";

	/** */
	private static final boolean CSV_APPEND = true;

	/** */
	private final String xAxisTitle = "Day";

	/** */
	public enum ChartIndicator
	{
		/** */
		TOTAL_EXCHANGES,
		/** */
		TOTAL_ADAPTION_HOURS,
		/** */
		TOTAL_WORKLOAD_HOURS,
		/** */
		CURRENT_EMISSION_RATE,
		/** */
		EMISSION_FACTOR,
		/** */
		CURRENT_ENERGY_CONSUMPTION,
		/** */
		CURRENT_CONSUMPTION_FACTOR,
		/** */
		CURRENT_CASH_FLOW,
		/** */
		TOTAL_SERVICES,
		/** */
		CURRENT_SERVICES,
	}

	/** */
	public enum AllocationPolicy
	{
		/** */
		LOCAL,
		/** */
		FEDERAL,
		/** */
		BROKERED,
	}

	/** */
	public static class MyData
	{
		private final int seriesId;

		private final Data<Number, Number> point;

		public MyData(final int seriesId, final Data<Number, Number> point)
		{
			this.seriesId = seriesId;
			this.point = point;
		}
	}

	/** */
	private FederationModel model = null;

	/** */
	private final Map<DsolIndicator<?, ?, ?>, Integer> indicators = new HashMap<>();

	/** data point buffer for chart draw events */
	private final List<MyData> newData = new LinkedList<>();

	/** */
	private final Button startButton = new Button("Start");

	/** */
	private final TextField lengthField = new TextField(
			Long.toString(RUN_LENGTH.getStandardDays()));

	/** */
	private final TextField warmUpField = new TextField(
			Long.toString(WARMUP_PERIOD.getStandardDays()));

	/** */
	private final TextField seedStartField = new TextField(
			Long.toString(SEED_COUNT));

	/** */
	private final TextField seedEndField = new TextField(
			Long.toString(END_SEED));

	/** */
	private final ChoiceBox<Duration> statsIntervalField = new ChoiceBox<>(
			FXCollections.observableArrayList(Duration.standardMinutes(30),
					Duration.standardHours(1), Duration.standardHours(3),
					Duration.standardHours(24)));

	/** */
	private final ChoiceBox<Boolean> accumulatedSelect = new ChoiceBox<>(
			FXCollections.observableArrayList(Boolean.FALSE, Boolean.TRUE));

	/** */
	private final ChoiceBox<Integer> fedSizeField = new ChoiceBox<>(
			FXCollections.observableArrayList(10, 20, 30, 40, 50));

	/** */
	private final ChoiceBox<AllocationPolicy> policySelect = new ChoiceBox<>(
			FXCollections.observableArrayList(AllocationPolicy.values()));

	/** */
	private final TextField csvFileNameField = new TextField(CSV_FILE_NAME);

	/** */
	private final CheckBox csvAppendField = new CheckBox("Append");

	/** */
	private final ChoiceBox<ChartIndicator> indicatorSelect = new ChoiceBox<>(
			FXCollections.observableArrayList(ChartIndicator.values()));

	/** */
	private StackedAreaChart<Number, Number> sac = new StackedAreaChart<Number, Number>(
			new NumberAxis(), new NumberAxis());

	@Override
	public void start(final Stage stage)
	{
		Platform.setImplicitExit(true);

		stage.setTitle("All4Green WP5 Federation Cash Flow");
		stage.setOnCloseRequest(new EventHandler<WindowEvent>()
		{
			@Override
			public void handle(WindowEvent t)
			{
				Platform.exit();
				System.exit(0);
			}
		});

		((NumberAxis) this.sac.getXAxis()).setTickUnit(1. / 6);
		((NumberAxis) this.sac.getXAxis()).setLowerBound(0);
		((NumberAxis) this.sac.getXAxis()).setUpperBound(1);
		((NumberAxis) this.sac.getXAxis()).setAutoRanging(false);
		((NumberAxis) this.sac.getXAxis()).setLabel(xAxisTitle);
		((NumberAxis) this.sac.getYAxis()).setAutoRanging(true);

		final HBox hbox = new HBox();
		hbox.setPadding(new Insets(15, 12, 15, 12));
		hbox.setSpacing(10); // Gap between nodes
		// hbox.setStyle("-fx-background-color: #336699;");

		this.lengthField.setTooltip(new Tooltip("Run length (days)"));
		this.lengthField.setPrefColumnCount(3);
		this.warmUpField.setTooltip(new Tooltip("Warm-up period (days)"));
		this.warmUpField.setPrefColumnCount(3);
		this.policySelect.setTooltip(new Tooltip("Allocation policy"));
		this.policySelect.setValue(POLICY);
		this.indicatorSelect.setTooltip(new Tooltip("Indicator to chart"));
		this.indicatorSelect.setValue(INDICATOR);
		this.statsIntervalField.setTooltip(new Tooltip("Statistics interval"));
		this.statsIntervalField.setValue(STATS_INTERVAL);
		this.accumulatedSelect.setTooltip(new Tooltip(
				"Accumulated (false for rates)"));
		this.accumulatedSelect.setValue(STATS_ACCUMULATED);
		this.seedStartField.setTooltip(new Tooltip("RNG seed start"));
		this.seedStartField.setPrefColumnCount(5);
		this.seedStartField.textProperty().addListener(
				new ChangeListener<String>()
				{
					@Override
					public void changed(
							final ObservableValue<? extends String> observable,
							final String oldValue, final String newValue)
					{
						try
						{
							final long start = Long.valueOf(newValue), end = Long
									.valueOf(seedEndField.getText()), min = Math
									.min(start, end), max = Math
									.max(start, end);
							seedStartField.setText(Long.toString(min));
							seedEndField.setText(Long.toString(max));
						} catch (final Exception ignore)
						{
							//
						}
					}
				});
		this.seedEndField.setTooltip(new Tooltip("RNG seed end"));
		this.seedEndField.setPrefColumnCount(5);
		this.seedEndField.textProperty().addListener(
				new ChangeListener<String>()
				{
					@Override
					public void changed(
							final ObservableValue<? extends String> observable,
							final String oldValue, final String newValue)
					{
						try
						{
							final long start = Long.valueOf(seedStartField
									.getText()), end = Long.valueOf(newValue), min = Math
									.min(start, end), max = Math
									.max(start, end);
							seedStartField.setText(Long.toString(min));
							seedEndField.setText(Long.toString(max));
						} catch (final Exception ignore)
						{
							//
						}
					}
				});
		this.fedSizeField.setTooltip(new Tooltip("Federation size (#DCs)"));
		this.fedSizeField.setValue(FEDERATION_SIZE);
		this.csvFileNameField.setTooltip(new Tooltip("CSV output file name"));
		this.csvFileNameField.setPrefColumnCount(10);
		this.csvAppendField.setTooltip(new Tooltip("CSV output append"));
		this.csvAppendField.setSelected(CSV_APPEND);
		hbox.getChildren().addAll(this.warmUpField, this.lengthField,
				this.policySelect, this.fedSizeField, this.statsIntervalField,
				this.indicatorSelect, this.accumulatedSelect,
				this.seedStartField, this.seedEndField, this.csvFileNameField,
				this.csvAppendField, this.startButton);
		this.startButton.setFont(new Font("Tahoma", 12));
		this.startButton.setEffect(new Reflection());
		this.startButton.setPrefSize(200, 30);
		this.startButton.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(final ActionEvent event)
			{
				Platform.runLater(new Runnable()
				{
					@Override
					public void run()
					{
						simStart();
					}
				});
			}
		});

		// Use a border pane as the root for scene
		final BorderPane border = new BorderPane();
		border.setTop(hbox);
		border.setCenter(this.sac);

		final Scene scene = new Scene(border);
		stage.setScene(scene);
		stage.show();
	}

	private final Executor exec = Executors.newSingleThreadExecutor();

	private void doChartUpdate(final ObservableList<Series<Number, Number>> data)
	{

		this.exec.execute(new Runnable()
		{
			@Override
			public void run()
			{

				// wait for new data if none is these yet
				while (newData.isEmpty())
				{
					// LOG.trace("Waiting for data...");
					try
					{
						synchronized (newData)
						{
							newData.wait(100L);
						}
					} catch (final InterruptedException ignore)
					{
						//
					}
				}

				// move new data points to local list
				// LOG.trace(String.format("Got %d new data", newData.size()));
				final List<MyData> plotData = new ArrayList<>();
				synchronized (newData)
				{
					plotData.addAll(newData);
					newData.clear();
				}

				Platform.runLater(new Runnable()
				{

					@Override
					public void run()
					{
						synchronized (data)
						{
							for (MyData datum : plotData)
								if (data.size() > datum.seriesId)
									data.get(datum.seriesId).getData()
											.add(datum.point);
							// else
							// LOG.warn("Race condition: series already removed for new run?");
						}
					}
				});
				// repeat
				// LOG.trace("Data plotted, repeating...");
				doChartUpdate(data);
			}
		});
	}

	private void disableGUI(final boolean disabled)
	{
		this.startButton.setDisable(disabled);
		this.seedStartField.setDisable(disabled);
		this.seedEndField.setDisable(disabled);
		this.lengthField.setDisable(disabled);
		this.warmUpField.setDisable(disabled);
		this.fedSizeField.setDisable(disabled);
		this.policySelect.setDisable(disabled);
		this.indicatorSelect.setDisable(disabled);
		this.csvFileNameField.setDisable(disabled);
		this.csvAppendField.setDisable(disabled);
		this.statsIntervalField.setDisable(disabled);
		this.accumulatedSelect.setDisable(disabled);
	}

	private void simStart()
	{
		this.startButton.setText("Running");

		disableGUI(true);
		doChartUpdate(this.sac.getData());

		// clear current indicators and chart series data
		Datacenter.ID_COUNT = 0;
		synchronized (this.sac.getData())
		{
			this.sac.getData().clear();
			this.indicators.clear();
		}

		this.model = new FederationModel(FEDERATION_SIZE,
				!AllocationPolicy.LOCAL.equals(policySelect.getValue()),
				AllocationPolicy.BROKERED.equals(policySelect.getValue()));
		
		// listen for new data centers to synchronize update graph time series
		this.model.addListener(new EventListenerInterface()
		{

			@Override
			public void notify(final EventInterface event)
					throws RemoteException
			{
				final Datacenter dc = (Datacenter) event.getContent();

				setupSeries(chooseIndicator(dc));
			}
		}, FederationModelComponent.NEW_DC);

		this.endReport = false;
		try
		{

			LOG.trace("Replication initializing...");
			final DEVSSimulatorInterface sim = new DEVSSimulator();
			final Context ctx = new InitialContext();
			final short mode = Treatment.REPLICATION_MODE_TERMINATING;
			final Experiment exp = new Experiment(ctx.createSubcontext("/exp"));
			final TimeUnitInterface timeUnit = TimeUnitInterface.DAY;

			exp.setSimulator(sim);
			exp.setModel(this.model);
			exp.setAnalyst("A4G");
			exp.setDescription("WP5 Phase 2");
			exp.setTreatment(new Treatment(exp, mode));
			exp.getTreatment().setStartTime(
					DateTime.now().withDayOfMonth(1).withTimeAtStartOfDay()
							.getMillis());
			exp.getTreatment().setTimeUnit(timeUnit);
			exp.getTreatment().setWarmupPeriod(
					Double.valueOf(this.warmUpField.getText()));
			exp.getTreatment().setRunLength(
					Double.valueOf(this.lengthField.getText()) + .000001);
			final Replication repl = new Replication(exp.getContext()
					.createSubcontext("/rep"), exp);
			final long seed = Long.valueOf(this.seedStartField.getText());
			this.seedStartField.setText(Long.toString(seed + 1));
			repl.setStreams(Collections.singletonMap(FederationModel.RNG_ID,
					(StreamInterface) new MersenneTwister()));
			sim.initialize(repl, mode);

			// start statistics flow to listen and draw
			scheduleStats(Duration.ZERO);

			((NumberAxis) this.sac.getXAxis()).setLowerBound(sim
					.getReplication().getTreatment().getWarmupPeriod());
			((NumberAxis) this.sac.getXAxis()).setUpperBound(sim
					.getReplication().getTreatment().getRunLength());

			// listen for simulation start/resume
			sim.addListener(new EventListenerInterface()
			{
				@Override
				public void notify(final EventInterface event)
				{
					LOG.trace("Sim started/resumed, t= " + model.getDateTime());
				}
			}, Simulator.START_EVENT);

			// listen for simulation ended
			sim.addListener(new EventListenerInterface()
			{
				@Override
				public void notify(final EventInterface event)
				{
					simEnded();
				}
			}, Simulator.END_OF_REPLICATION_EVENT);

			LOG.trace("Replication initialized, starting...");
			sim.start();
		} catch (final Exception e)
		{
			LOG.error("Problem reaching/starting sim", e);
		}
	}

	/** choose the charted indicator */
	private DsolIndicator<?, ?, ?> chooseIndicator(final Datacenter dc)
	{
		switch (indicatorSelect.getValue())
		{
		case CURRENT_CONSUMPTION_FACTOR:
			return dc.consumptionFactor;
		case CURRENT_SERVICES:
			return dc.getCurrentServiceCount();
		case EMISSION_FACTOR:
			return dc.emissionFactor;
		case CURRENT_CASH_FLOW:
			return dc.getCashflow();
		case CURRENT_ENERGY_CONSUMPTION:
			return dc.getConsumptionKWh();
		case CURRENT_EMISSION_RATE:
			return dc.getEmissionsKgCO2();
		case TOTAL_SERVICES:
			return dc.getTotalServiceCount();
		case TOTAL_ADAPTION_HOURS:
			return dc.adaptionHours;
		case TOTAL_EXCHANGES:
			return dc.exchangedServiceCount;
		case TOTAL_WORKLOAD_HOURS:
			return dc.workloadHours;
		default:
			return null;
		}
	}

	/** */
	private boolean endReport = false;

	private void simEnded()
	{
		if (this.endReport)
			return;
		this.endReport = true;

		Platform.runLater(new Runnable()
		{
			@Override
			public void run()
			{
				startButton.setText("Complete");
			}
		});

		// trigger chart update to empty current data point cache
		// synchronized (this.newData) {
		// this.newData.notifyAll();
		// }

		LOG.trace("Sim ended, t= " + this.model.getDateTime());
		double fedCash = 0.0d, fedConsKWh = 0.0d, fedEmitCO2 = 0.0d;
		int fedAdapt = 0, fedSvcCount = 0, fedXchCount = 0;
		for (Datacenter dc : this.model.getDatacenters())
		{
			fedAdapt += dc.adaptionHours.getValue().intValue();
			fedCash += dc.getCashflow().getValue().doubleValue();
			fedConsKWh += dc.getConsumptionKWh().getValue().doubleValue();
			fedEmitCO2 += dc.getEmissionsKgCO2().getValue().doubleValue();
			fedSvcCount += dc.getTotalServiceCount().getValue().intValue();
			fedXchCount += dc.exchangedServiceCount.getValue().intValue();
		}
		LOG.trace(String.format("Federation report:%n"
				+ "\tAllocation:                 \t%7s%n"
				+ "\tSimulation seed:            \t%7d%n"
				+ "\tDuration (days):            \t%7d%n"
				+ "\tFederated datacenters:      \t%7d%n"
				+ "\tAdaptions (hours):          \t%7d%n"
				+ "\tTotal cash generated (EUR): \t%10.2f%n"
				+ "\tTotal energy consumed (MWh):\t%11.3f%n"
				+ "\tTotal CO2 emitted (kg):     \t%11.3f%n"
				+ "\tTotal IT services completed:\t%7d%n"
				+ "\tTotal IT services exchanged:\t%7d%n"
				+ "\tTotal federation efficiency:\t%10.2f%%",
				policySelect.getValue(),
				Long.valueOf(seedStartField.getText()), DsolUtil
						.getRunInterval(this.model.getTreatment()).toDuration()
						.getStandardDays(), fedSizeField.getValue(), fedAdapt,
				fedCash, fedConsKWh / 1000, fedEmitCO2 / 1000, fedSvcCount,
				fedXchCount, 100.0 * fedXchCount / fedSvcCount));

		final String fileName = this.csvFileNameField.getText();
		try (final FileOutputStream out = new FileOutputStream(fileName,
				this.csvAppendField.isSelected()))
		{
			out.write(String.format(
					"%s,%s,%d,%d,%d,%d,%f,%f,%f,%d,%d,%f%n",
					DateTime.now().toString(),
					policySelect.getValue(),
					Long.valueOf(seedStartField.getText()),
					DsolUtil.getRunInterval(this.model.getTreatment())
							.toDuration().getStandardDays(),
					fedSizeField.getValue(), fedAdapt, fedCash,
					fedConsKWh / 1000, fedEmitCO2 / 1000, fedSvcCount,
					fedXchCount, 100.0 * fedXchCount / fedSvcCount).getBytes());
		} catch (final IOException e)
		{
			LOG.error("Problem writing to file: " + fileName, e);
		}

		Platform.runLater(new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					// reset simulator's pending event list
					model.getSimulator().setEventList(new RedBlackTree());
				} catch (final RemoteException | SimRuntimeException e)
				{
					LOG.warn("Problem resetting event list", e);
				}
				model = null;
				System.gc();

				if (Long.valueOf(seedStartField.getText()) < Long
						.valueOf(seedEndField.getText()))
					simStart();
				else
				{
					startButton.setText("Start");
					disableGUI(false);
				}
				// startButton.fire();
			}
		});
	}

	private void setupSeries(final DsolIndicator<?, ?, ?> ind)
			throws RemoteException
	{
		final Series<Number, Number> series = new Series<>();
		series.getData().add(new Data<Number, Number>(0, 0));
		series.setName(ind.getName());
		Platform.runLater(new Runnable()
		{
			@Override
			public void run()
			{
				// LOG.trace("New series for dc %d" + id);
				final int seriesId = sac.getData().size();
				sac.getData().add(series);
				if (accumulatedSelect.getValue() && ind instanceof Accumulator)
				{
					((NumberAxis) sac.getYAxis()).setLabel(((Accumulator) ind)
							.getRateUnitName());
					sac.setTitle(((Accumulator) ind).getRateTitle());
				} else
				{
					((NumberAxis) sac.getYAxis()).setLabel(ind
							.getValueUnitName());
					sac.setTitle(ind.getValueTitle());
				}
				synchronized (indicators)
				{
					indicators.put(ind, seriesId);
					indicators.notifyAll();
				}
			}
		});
	}

	private void scheduleStats(final Duration delay)
	{
		try
		{
			this.model.getSimulator().scheduleEvent(this.model.simTime(delay),
					this, this, DO_STATS_METHOD_ID,
					FederationModelComponent.NO_ARGS);
		} catch (final RemoteException | SimRuntimeException e)
		{
			LOG.warn("Problem repeating stats", e);
		}
		final String timeText = String.format("%s: %.1f", this.sac.getXAxis()
				.getLabel(), this.model.simTime());
		Platform.runLater(new Runnable()
		{
			@Override
			public void run()
			{
				startButton.setText(timeText);
			}
		});
	}

	private static final String DO_STATS_METHOD_ID = "doStats";

	protected void doStats() throws RemoteException
	{
		final Map<DsolIndicator<?, ?, ?>, Integer> currentIndicators = new HashMap<>();
		synchronized (indicators)
		{
			while (indicators.size() != model.getDatacenters().size())
				try
				{
					indicators.wait(100L);
				} catch (final InterruptedException ignore)
				{
					//
				}
			currentIndicators.putAll(this.indicators);
		}

		final double simDays = this.model.simTime(TimeUnitInterface.DAY);
		synchronized (this.newData)
		{
			for (Map.Entry<DsolIndicator<?, ?, ?>, Integer> entry : currentIndicators
					.entrySet())
			{
				final DsolIndicator<?, ?, ?> ind = entry.getKey();
				// LOG.trace(String.format("t=%.4f (%s) Adding stats for %s",
				// simDays, this.model.getDateTime(), ind.getName()));
				final Number value = ind instanceof DsolAccumulator
						&& !this.accumulatedSelect.getValue() ? ((DsolAccumulator<?, ?, ?>) ind)
						.getRate() : ind.getValue();
				if (value != null)
				{
					final Data<Number, Number> datum = new Data<Number, Number>(
							simDays, value);
					this.newData.add(new MyData(entry.getValue(), datum));
				}
			}
			// this.newData.notify();
		}

		// repeat
		scheduleStats(this.statsIntervalField.getValue());
	}

	/**
	 * @param args the main arguments
	 */
	public static void main(final String[] args)
	{
		launch(args);
	}
}