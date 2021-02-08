package warehouse.storage;

public class Umiestnenie {
	private Long id;
	private Tovar druh;
	private int mnozstvo;
	private Sklad sklad;
	private int regal;
	private int policka;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Tovar getDruh() {
		return druh;
	}

	public void setDruh(Tovar druh) {
		this.druh = druh;
	}

	public int getMnozstvo() {
		return mnozstvo;
	}

	public void setMnozstvo(int mnozstvo) {
		this.mnozstvo = mnozstvo;
	}

	public Sklad getSklad() {
		return sklad;
	}

	public void setSklad(Sklad sklad) {
		this.sklad = sklad;
	}

	public int getRegal() {
		return regal;
	}

	public void setRegal(int regal) {
		this.regal = regal;
	}

	public int getPolicka() {
		return policka;
	}

	public void setPolicka(int policka) {
		this.policka = policka;
	}

	public Umiestnenie() {
	}

	public Umiestnenie(Long id, Tovar druh, int mnozstvo, Sklad sklad, int regal, int policka) {
		super();
		this.id = id;
		this.druh = druh;
		this.mnozstvo = mnozstvo;
		this.sklad = sklad;
		this.regal = regal;
		this.policka = policka;
	}

	public Umiestnenie(Tovar druh, int mnozstvo, Sklad sklad, int regal, int policka) {
		super();
		this.druh = druh;
		this.mnozstvo = mnozstvo;
		this.sklad = sklad;
		this.regal = regal;
		this.policka = policka;
	}

	public Umiestnenie(Sklad sklad, int regal, int policka) {
		this.sklad = sklad;
		this.regal = regal;
		this.policka = policka;
	}

	@Override
	public String toString() {
		return "#" + id + " " + druh.getNazov() + " " + mnozstvo + " " + sklad.getNazov() + " " + regal + " " + policka;
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
		Umiestnenie other = (Umiestnenie) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
