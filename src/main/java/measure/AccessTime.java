package measure;

import com.opencsv.bean.CsvBindByName;

public class AccessTime extends CsvBean{

	
	@CsvBindByName(column = "Kategorie", required = true)
	CRUDoperation kategorie;
	
	@CsvBindByName(column = "Objekt", required = true)
	ObjectCategory objektKategorie;
	
	@CsvBindByName(column = "Methode", required = true)
	MethodType methode;
	
	@CsvBindByName(column = "Zeit in Nanosekunden", required = true)
	long zugriffszeitZeit;
	
	public AccessTime(CRUDoperation crudCategory, ObjectCategory objectCategory, long time, MethodType methodType) {
		this.methode = methodType;
		this.zugriffszeitZeit = time;
		this.kategorie=crudCategory;
		this.objektKategorie = objectCategory;
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
