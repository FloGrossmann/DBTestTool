package dbinterface;

import org.bson.codecs.pojo.annotations.BsonProperty;

public class Adresse {

	@BsonProperty("ortschaft")
	String ortschaft;
	@BsonProperty("hausnummer")
	String hausnummer;
	@BsonProperty("strasse")
	String strasse;
	@BsonProperty("plz")
	String plz;

	public Adresse() {

	}

	public Adresse(@BsonProperty("ortschaft") String ortschaft, @BsonProperty("hausnummer") String hausnummer,
			@BsonProperty("strasse") String strasse, @BsonProperty("plz") String plz) {
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

}
