package measure;

import java.util.Iterator;
import java.util.List;

public class Messreihe {

	List<Long> messungen;
	private double durchschnitt = -1;
	private double varianz = -1;
	private double standardAbweichung = -1;

	public Messreihe(List<Long> messungen) {
		this.messungen = messungen;
		berechneDurchschnitt();
		berechneVarianz();
		berechneStandardAbweichung();
	}

	public void berechneDurchschnitt() {
		Iterator<Long> iterator = this.messungen.iterator();
		long durchschnitt = 0;
		while (iterator.hasNext()) {
			durchschnitt = durchschnitt + iterator.next();
		}
		this.durchschnitt = durchschnitt / this.messungen.size();
	}

	public void berechneVarianz() {
		if (durchschnitt == -1) {
			berechneDurchschnitt();
		}

		Iterator<Long> iterator = this.messungen.iterator();

		Double varianz = 0d;
		while (iterator.hasNext()) {
			varianz = varianz + Math.pow((iterator.next() - this.durchschnitt), 2);
		}
		this.varianz = varianz / this.messungen.size();
	}

	public void berechneStandardAbweichung() {
		if (varianz == -1) {
			berechneVarianz();
		}
		standardAbweichung = Math.round(Math.sqrt(varianz) * 10000.0) / 10000.0;
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

}
