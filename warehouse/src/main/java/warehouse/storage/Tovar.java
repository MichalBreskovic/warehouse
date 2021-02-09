package warehouse.storage;

public class Tovar {
	private Long id;
	private String nazov;
	private Jednotka mernaJednotka;
	private double jednotkovaCena;
	private int obmedzenie;
	private String popis;

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

	public Jednotka getMernaJednotka() {
		return mernaJednotka;
	}

	public void setMernaJednotka(Jednotka mernaJednotka) {
		this.mernaJednotka = mernaJednotka;
	}

	public double getJednotkovaCena() {
		return jednotkovaCena;
	}

	public void setJednotkovaCena(double jednotkovaCena) {
		this.jednotkovaCena = jednotkovaCena;
	}

	public int getObmedzenie() {
		return obmedzenie;
	}

	public void setObmedzenie(int obmedzenie) {
		this.obmedzenie = obmedzenie;
	}

	public String getPopis() {
		return popis;
	}

	public void setPopis(String popis) {
		this.popis = popis;
	}

	public Tovar() {

	}

	public Tovar(Long id, String nazov, Jednotka mernaJednotka, double jednotkovaCena, int obmedzenie, String popis) {
		this.id = id;
		this.nazov = nazov;
		this.mernaJednotka = mernaJednotka;
		this.jednotkovaCena = jednotkovaCena;
		this.obmedzenie = obmedzenie;
		this.popis = popis;
	}

	public Tovar(String nazov, Jednotka mernaJednotka, double jednotkovaCena, int obmedzenie, String popis) {
		super();
		this.nazov = nazov;
		this.mernaJednotka = mernaJednotka;
		this.jednotkovaCena = jednotkovaCena;
		this.obmedzenie = obmedzenie;
		this.popis = popis;
	}

	@Override
	public String toString() {
		return "#" + id + ": " + nazov + " " + jednotkovaCena + " € / " + mernaJednotka.getOznacenie();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		Tovar other = (Tovar) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
