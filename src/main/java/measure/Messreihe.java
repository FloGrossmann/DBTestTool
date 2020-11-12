package measure;

import java.util.Iterator;
import java.util.List;

public class Messreihe {

	List<Long> messungen;
	private double durchschnitt = -1;
	private double durchschnittMs = -1;
	private double varianz = -1;
	private double varianzMs = -1;
	private double standardAbweichung = -1;

	public Messreihe(List<Long> messungen) {
		this.messungen = messungen;
		berechneDurchschnitt();
		berechneVarianz();
		berechneStandardAbweichung();
	}
	
	private double rint(double value, int decimalPoints) {
		   double d = Math.pow(10, decimalPoints);
		   return Math.rint(value * d) / d;
	}

	public void berechneDurchschnitt() {
		Iterator<Long> iterator = this.messungen.iterator();
		long durchschnitt = 0;
		while (iterator.hasNext()) {
			long value=iterator.next();
			durchschnitt = durchschnitt +value; 
		}
		this.durchschnitt = durchschnitt / this.messungen.size();
		
		this.durchschnittMs=this.rint((this.durchschnitt/1000000), 4);
	}

	public void berechneVarianz() {
		if (durchschnitt == -1 || durchschnittMs == -1) {
			berechneDurchschnitt();
		}

		Iterator<Long> iterator = this.messungen.iterator();

		Double varianz = 0d;
		Double varianzMs = 0d;
		while (iterator.hasNext()) {
			long value=iterator.next();
			double valueMs= value / 1000000;
			varianz = varianz + Math.pow((value - this.durchschnitt), 2);
			varianzMs = varianzMs + Math.pow((valueMs - this.durchschnittMs), 2);
		}
		this.varianz = varianz / this.messungen.size();
		this.varianzMs=this.rint((varianzMs/this.messungen.size()),4);
	}

	public void berechneStandardAbweichung() {
		if (varianz == -1 || varianzMs == -1) {
			berechneVarianz();
		}
		standardAbweichung = Math.round(Math.sqrt(varianzMs) * 10000.0) / 10000.0;
	}

	public List<Long> getMessungen() {
		return messungen;
	}

	public void setMessungen(List<Long> messungen) {
		this.messungen = messungen;
	}

	public double getDurchschnitt() {
		if (durchschnitt == -1) {
			berechneDurchschnitt();
		}
		return durchschnitt;
	}

	public void setDurchschnitt(double durchschnitt) {
		this.durchschnitt = durchschnitt;
	}

	public double getVarianz() {
		if (varianz == -1) {
			berechneVarianz();
		}
		return varianz;
	}

	public void setVarianz(double varianz) {
		this.varianz = varianz;
	}

	public double getStandardAbweichung() {
		return standardAbweichung;
	}

	public void setStandardAbweichung(double standardAbweichung) {
		this.standardAbweichung = standardAbweichung;
	}

	public double getDurchschnittMs() {
		if (durchschnittMs == -1) {
			berechneDurchschnitt();
		}
		return durchschnittMs;
	}

	public void setDurchschnittMs(double durchschnittMs) {
		this.durchschnittMs = durchschnittMs;
	}

	public double getVarianzMs() {
		if (varianzMs == -1) {
			berechneVarianz();
		}
		return varianzMs;
	}

	public void setVarianzMs(double varianzMs) {
		this.varianzMs = varianzMs;
	}

}
