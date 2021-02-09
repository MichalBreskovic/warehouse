package warehouse.storage;

public class Sklad {
	private Long id;
	private String nazov;
	private String adresa;
	private int pocetRegalov;
	private int pocetPoliciek;

	public Sklad(String nazov, String adresa, int pocetRegalov, int pocetPoliciek) {
		this.nazov = nazov;
		this.adresa = adresa;
		this.pocetRegalov = pocetRegalov;
		this.pocetPoliciek = pocetPoliciek;
	}

	public Sklad(Long id, String nazov, String adresa, int pocetRegalov, int pocetPoliciek) {
		this.id = id;
		this.nazov = nazov;
		this.adresa = adresa;
		this.pocetRegalov = pocetRegalov;
		this.pocetPoliciek = pocetPoliciek;
	}

	public Sklad() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNazov() {
		return nazov;
	}

	public void setNazov(String nazov) {
		this.nazov = nazov;
	}

	public String getAdresa() {
		return adresa;
	}

	public void setAdresa(String adresa) {
		this.adresa = adresa;
	}

	public int getPocetRegalov() {
		return pocetRegalov;
	}

	public void setPocetRegalov(int pocetRegalov) {
		this.pocetRegalov = pocetRegalov;
	}

	public int getPocetPoliciek() {
		return pocetPoliciek;
	}

	public void setPocetPoliciek(int pocetPoliciek) {
		this.pocetPoliciek = pocetPoliciek;
	}

	@Override
	public String toString() {
		return "#" + id + ": " + nazov + " (" + adresa+")";
		
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((adresa == null) ? 0 : adresa.hashCode());
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
		Sklad other = (Sklad) obj;
		if (adresa == null) {
			if (other.adresa != null)
				return false;
		} else if (!adresa.equals(other.adresa))
			return false;
		return true;
	}

}
