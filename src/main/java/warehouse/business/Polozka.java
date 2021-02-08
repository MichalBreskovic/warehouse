package warehouse.business;

import warehouse.storage.Tovar;

public class Polozka {
	private Tovar druh;
	private int pocet;
	private double celkovaCena;

	public Polozka() {

	}

	public Polozka(Tovar druh, int pocet, double celkovaCena) {
		this.druh = druh;
		this.pocet = pocet;
		this.celkovaCena = celkovaCena;
	}

	public Tovar getDruh() {
		return druh;
	}

	public void setNazov(Tovar druh) {
		this.druh = druh;
	}

	public int getPocet() {
		return pocet;
	}

	public void setPocet(int pocet) {
		this.pocet = pocet;
	}

	public double getCelkovaCena() {
		return celkovaCena;
	}

	public void setCelkovaCena(double celkovaCena) {
		this.celkovaCena = celkovaCena;
	}

	@Override
	public String toString() {
		return "Polozka [nazov=" + druh.getNazov() + ", pocet=" + pocet + ", celkovaCena=" + celkovaCena + "]";
	}
}
