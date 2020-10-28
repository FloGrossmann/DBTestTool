package measure;

import java.util.Iterator;
import java.util.List;

public class Messreihe {

	List<AccessTime> messungen;
	double durchschnitt;
	double varianz;
	// was für Werte brauchen wir noch?
	
	public Messreihe(List<AccessTime> messungen) {
		this.messungen = messungen;
		berechneDurchschnitt();	// wäre jetzt nur die allgemeine Berechnung
	}
	
	private void berechneDurchschnitt() {
		Iterator<AccessTime> iterator=this.messungen.iterator();
		while(iterator.hasNext()) {
			this.durchschnitt=this.durchschnitt + iterator.next().getZugriffszeitZeit();
		}
		this.durchschnitt=this.durchschnitt/this.messungen.size();
	}

	public List<AccessTime> getMessungen() {
		return messungen;
	}
	public void setMessungen(List<AccessTime> messungen) {
		this.messungen = messungen;
	}
	public double getDurchschnitt() {
		return durchschnitt;
	}
	public void setDurchschnitt(double durchschnitt) {
		this.durchschnitt = durchschnitt;
	}
	public double getVarianz() {
		return varianz;
	}
	public void setVarianz(double varianz) {
		this.varianz = varianz;
	}
}
