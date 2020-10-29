package measure;

import com.opencsv.bean.CsvBindByName;

public class AccessTime extends CsvBean{

	
	@CsvBindByName(column = "Kategorie", required = true)
	CRUDoperation kategorie;
	
	@CsvBindByName(column = "Beschreibung", required = true)
	String beschreibung;
	
	@CsvBindByName(column = "Zeit in Nanosekunden", required = true)
	long zugriffszeitZeit;
	
	public AccessTime(String beschreibung, long zeit, CRUDoperation kategorie) {
		this.beschreibung =beschreibung;
		this.zugriffszeitZeit=zeit;
		this.kategorie=kategorie;
	}

	public String getBeschreibung() {
		return beschreibung;
	}

	public void setBeschreibung(String beschreibung) {
		this.beschreibung = beschreibung;
	}

	public long getZugriffszeitZeit() {
		return zugriffszeitZeit;
	}

	public void setZugriffszeitZeit(long zugriffszeitZeit) {
		this.zugriffszeitZeit = zugriffszeitZeit;
	}

	public CRUDoperation getKategorie() {
		return kategorie;
	}

	public void setKategorie(CRUDoperation kategorie) {
		this.kategorie = kategorie;
	}
	
	 
	
}
