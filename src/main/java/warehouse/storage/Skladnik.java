package warehouse.storage;

public class Skladnik {
	private Long id;
	private String meno;
	private String priezvisko;
	private Sklad sklad;
	private boolean veduci;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMeno() {
		return meno;
	}

	public void setMeno(String meno) {
		this.meno = meno;
	}

	public String getPriezvisko() {
		return priezvisko;
	}

	public void setPriezvisko(String priezvisko) {
		this.priezvisko = priezvisko;
	}

	public Sklad getSklad() {
		return sklad;
	}

	public void setSklad(Sklad sklad) {
		this.sklad = sklad;
	}

	public boolean isVeduci() {
		return veduci;
	}

	public void setVeduci(boolean veduci) {
		this.veduci = veduci;
	}

	public Skladnik() {
	}

	public Skladnik(Long id, String meno, String priezvisko, Sklad sklad, boolean veduci) {
		this.id = id;
		this.meno = meno;
		this.priezvisko = priezvisko;
		this.sklad = sklad;
		this.veduci = veduci;
	}

	public Skladnik(String meno, String priezvisko, Sklad sklad, boolean veduci) {
		this.meno = meno;
		this.priezvisko = priezvisko;
		this.sklad = sklad;
		this.veduci = veduci;
	}

	@Override
	public String toString() {
		return "Skladnik [id=" + id + ", meno=" + meno + ", priezvisko=" + priezvisko + ", sklad=" + sklad.getNazov()
				+ ", veduci=" + veduci + "]";
	}

}
