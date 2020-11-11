package measure;

import com.opencsv.bean.CsvBindByName;

public class AccessTime extends CsvBean {

	@CsvBindByName(column = "Kategorie", required = true)
	CRUDoperation kategorie;

	@CsvBindByName(column = "Objekt", required = true)
	ObjectCategory objektKategorie;

	@CsvBindByName(column = "Methode", required = true)
	MethodType methode;

	@CsvBindByName(column = "Durchschnitts Zeit in MilliSekunden", required = true)
	double durchschnittsZugriffsZeit;
	
	@CsvBindByName(column="Durchschnitts Zeit in Nanosekunden", required = true)
	double durchschnittsZugriffsZeitinNs;

	@CsvBindByName(column = "Varianz der Messpunkte in Millisekunden", required = true)
	double varianz;

	@CsvBindByName(column = "Varianz der Messpunkte in Nanosekunden", required = true)
	double varianzNs;

	@CsvBindByName(column = "Standardabweichung der Messpunkte", required = true)
	double standardAbweichung;

	@CsvBindByName(column = "Anzahl der durchgeführten Messungen", required = true)
	double averaged_over;

	@CsvBindByName(column = "Anzahl der sich in der Datenbank befindlichen Einträge", required = true)
	int testedAt;

	public AccessTime(CRUDoperation crudCategory, ObjectCategory objectCategory, MethodType methodType,
			double average_time, double varianz, double standardAbweichung, int averaged_over, int testedAt) {
		this.methode = methodType;
		this.durchschnittsZugriffsZeit = average_time/1000000;
		this.durchschnittsZugriffsZeitinNs=average_time;
		this.kategorie = crudCategory;
		this.objektKategorie = objectCategory;
		this.varianzNs = varianz;
		this.varianz=varianz/ 1000000;
		this.standardAbweichung = standardAbweichung;
		this.averaged_over = averaged_over;
		this.testedAt = testedAt;
	}

	public AccessTime(CRUDoperation curdCategory, ObjectCategory objectCategory, MethodType methodType,
			Messreihe messreihe, int testedAt) {
		this(curdCategory, objectCategory, methodType, messreihe.getDurchschnitt(),  messreihe.getVarianz(),
				messreihe.getStandardAbweichung(), messreihe.getMessungen().size(), testedAt);
	}

	public ObjectCategory getObjektKategorie() {
		return objektKategorie;
	}

	public void setObjektKategorie(ObjectCategory objektKategorie) {
		this.objektKategorie = objektKategorie;
	}

	public MethodType getMethode() {
		return methode;
	}

	public void setMethode(MethodType methode) {
		this.methode = methode;
	}

	public double getZugriffszeitZeit() {
		return durchschnittsZugriffsZeit;
	}

	public void setZugriffszeitZeit(long zugriffszeitZeit) {
		this.durchschnittsZugriffsZeit = zugriffszeitZeit;
	}

	public CRUDoperation getKategorie() {
		return kategorie;
	}

	public void setKategorie(CRUDoperation kategorie) {
		this.kategorie = kategorie;
	}

	public double getDurchschnittsZugriffsZeit() {
		return durchschnittsZugriffsZeit;
	}

	public void setDurchschnittsZugriffsZeit(double durchschnittsZugriffsZeit) {
		this.durchschnittsZugriffsZeit = durchschnittsZugriffsZeit;
	}

	public double getVarianz() {
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

	public double getAveraged_over() {
		return averaged_over;
	}

	public void setAveraged_over(double averaged_over) {
		this.averaged_over = averaged_over;
	}

	public int getTestedAt() {
		return testedAt;
	}

	public void setTestedAt(int testedAt) {
		this.testedAt = testedAt;
	}

}
