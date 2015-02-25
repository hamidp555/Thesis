package com.cheo.charts;

import java.awt.RenderingHints;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.FastScatterPlot;
import org.jfree.ui.ApplicationFrame;

public class ScateredPlot extends ApplicationFrame {

	private static final long serialVersionUID = -3731299414874345019L;

	public ScateredPlot(float[][] data, String title) {
		super(title);
		final NumberAxis domainAxis = new NumberAxis("EDUs");
		domainAxis.setAutoRangeIncludesZero(false);
		final NumberAxis rangeAxis = new NumberAxis("Sentiment");
		rangeAxis.setAutoRangeIncludesZero(false);

		final FastScatterPlot plot = new FastScatterPlot(data, domainAxis, rangeAxis);
		final JFreeChart chart = new JFreeChart("Fast Scatter Plot", plot);

		// force aliasing of the rendered content..
		chart.getRenderingHints().put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		final ChartPanel panel = new ChartPanel(chart, true);
		panel.setPreferredSize(new java.awt.Dimension(500, 270));

		panel.setMinimumDrawHeight(10);
		panel.setMaximumDrawHeight(2000);
		panel.setMinimumDrawWidth(20);
		panel.setMaximumDrawWidth(2000);

		setContentPane(panel);
	}

}
