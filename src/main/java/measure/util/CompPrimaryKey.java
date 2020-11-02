package measure.util;

public class CompPrimaryKey {

	private String artikelNummer;
	private String kundenNummer;

	public CompPrimaryKey(String artikelNummer, String kundenNummer) {
		super();
		this.artikelNummer = artikelNummer;
		this.kundenNummer = kundenNummer;
	}

	public String getArtikelNummer() {
		return artikelNummer;
	}

	public void setArtikelNummer(String artikelNummer) {
		this.artikelNummer = artikelNummer;
	}

	public String getKundenNummer() {
		return kundenNummer;
	}

	public void setKundenNummer(String kundenNummer) {
		this.kundenNummer = kundenNummer;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((artikelNummer == null) ? 0 : artikelNummer.hashCode());
		result = prime * result + ((kundenNummer == null) ? 0 : kundenNummer.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CompPrimaryKey other = (CompPrimaryKey) obj;
		if (artikelNummer == null) {
			if (other.artikelNummer != null)
				return false;
		} else if (!artikelNummer.equals(other.artikelNummer))
			return false;
		if (kundenNummer == null) {
			if (other.kundenNummer != null)
				return false;
		} else if (!kundenNummer.equals(other.kundenNummer))
			return false;
		return true;
	}
}
