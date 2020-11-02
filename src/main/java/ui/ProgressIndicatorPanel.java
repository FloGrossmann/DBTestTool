package ui;

import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import measure.DBTest;

public class ProgressIndicatorPanel extends JPanel {
	
	private static final long serialVersionUID = 909712826195923363L;
	int nrOfGlobalTestRepetitions = DBTest.MAXIMUM / DBTest.TESTEVERY;
	JLabel progressBarGlobal_label = new JLabel("Global Progress");
	JProgressBar progressBarGlobal = new JProgressBar(0, nrOfGlobalTestRepetitions);
	JLabel progressBarAdding_label = new JLabel("Inserting");
	JProgressBar progressBarAdding = new JProgressBar(0, DBTest.TESTEVERY);
	JLabel progressBarTestRepetitions_label = new JLabel("Test Progress");
	JProgressBar progressBarTestRepetitions = new JProgressBar(0, DBTest.REPETITIONS);
	JLabel status = new JLabel("Testing not started");
	JLabel dbUnderTest = new JLabel("");
	
	public ProgressIndicatorPanel() {
		setLayout(new GridLayout(4, 2));

		add(dbUnderTest);
		add(status);
		add(progressBarGlobal_label);
		add(progressBarGlobal);
		add(progressBarAdding_label);
		add(progressBarAdding);
		add(progressBarTestRepetitions_label);
		add(progressBarTestRepetitions);
	}
	
	public void setText(String statusText) {
		status.setText(statusText);
		update(getGraphics());
	}
	
	public void setDBUnderTestText(String statusText) {
		dbUnderTest.setText(statusText);
		update(getGraphics());
	}
	
	public void resetGlobalProgress() {
		progressBarGlobal.setValue(0);
		update(getGraphics());
	}
	
	public void resetAddingBarProgress() {
		progressBarAdding.setValue(0);
		update(getGraphics());
	}
	
	public void resetTestRepititionBarProgress() {
		progressBarTestRepetitions.setValue(0);
		update(getGraphics());
	}
	
	public void setGlobalProgress(int val) {
		progressBarGlobal.setValue(val);
		update(getGraphics());
	}
	
	public void setAddingProgress(int val) {
		progressBarAdding.setValue(val);
		update(getGraphics());
	}
	
	public void setTestRepititionProgress(int val) {
		progressBarTestRepetitions.setValue(val);
		update(getGraphics());
	}
}
