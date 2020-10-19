package dbinterface;

public class Adresse {

	String ortschaft;
	String hausnummer;
	String strasse;
	String plz;

	public Adresse() {

	}

	public Adresse(String ortschaft, String hausnummer, String strasse, String plz) {
		super();
		this.ortschaft = ortschaft;
		this.hausnummer = hausnummer;
		this.strasse = strasse;
		this.plz = plz;
	}

	public String getOrtschaft() {
		return ortschaft;
	}

	public void setOrtschaft(String ortschaft) {
		this.ortschaft = ortschaft;
	}

	public String getHausnummer() {
		return hausnummer;
	}

	public void setHausnummer(String hausnummer) {
		this.hausnummer = hausnummer;
	}

	public String getStrasse() {
		return strasse;
	}

	public void setStrasse(String strasse) {
		this.strasse = strasse;
	}

	public String getPlz() {
		return plz;
	}

	public void setPlz(String plz) {
		this.plz = plz;
	}
	
	@Override
	public String toString() {
		return "Adresse: \n"+strasse+" "+hausnummer+"\n"+ortschaft+"\n"+plz;
		
	}

}
