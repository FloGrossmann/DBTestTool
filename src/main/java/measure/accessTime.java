package measure;

public class accessTime {

	String beschreibung;
	CRUDoperation kategorie;
	long zugriffszeitZeit;
	
	public accessTime(String beschreibung, long zeit, CRUDoperation kategorie) {
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
