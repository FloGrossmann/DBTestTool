package measure;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;

public class AccessTime extends CsvBean{

	
	@CsvBindByName(column = "Kategorie", required = true)
//	@CsvBindByPosition(position = 0)
	CRUDoperation kategorie;
	
	@CsvBindByName(column = "Beschreibung", required = true)
//	@CsvBindByPosition(position = 1)
	String beschreibung;
	
	@CsvBindByName(column = "Zeit in ms", required = true)
//	@CsvBindByPosition(position = 2)
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
