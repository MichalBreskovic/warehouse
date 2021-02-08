package warehouse;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import warehouse.storage.Jednotka;
import warehouse.storage.Tovar;

public class TovarFxModel {

	private Long id = null;
	private StringProperty nazov = new SimpleStringProperty();
	private ObjectProperty<Jednotka> mernaJednotka = new SimpleObjectProperty<>();
	private StringProperty jednotkovaCena = new SimpleStringProperty();
	private StringProperty obmedzenie = new SimpleStringProperty();
	private StringProperty popis = new SimpleStringProperty();

	public TovarFxModel() {
	}

	public TovarFxModel(Tovar tovar) {

		id = tovar.getId();
		setNazov(tovar.getNazov());
		setMernaJednotka(tovar.getMernaJednotka());
		setJednotkovaCena(String.valueOf(tovar.getJednotkovaCena()));
		setObmedzenie(String.valueOf(tovar.getObmedzenie()));
		setPopis(tovar.getPopis());
	}

	public Long getId() {
		return id;
	}

	public String getNazov() {
		return nazov.get();
	}

	public StringProperty nazovProperty() {
		return nazov;
	}

	public void setNazov(String nazov) {
		this.nazov.set(nazov);
	}

	public Jednotka getMernaJednotka() {
		return mernaJednotka.get();
	}

	public ObjectProperty<Jednotka> mernaJednotkaProperty() {
		return mernaJednotka;
	}

	public void setMernaJednotka(Jednotka mernaJednotka) {
		this.mernaJednotka.set(mernaJednotka);
	}

	public String getJednotkovaCena() {
		return jednotkovaCena.get();
	}

	public StringProperty jednotkovaCenaProperty() {
		return jednotkovaCena;
	}

	public void setJednotkovaCena(String jednotkovaCena) {
		this.jednotkovaCena.set(jednotkovaCena);
	}

	public String getObmedzenie() {
		return obmedzenie.get();
	}

	public StringProperty obmedzenieProperty() {
		return obmedzenie;
	}

	public void setObmedzenie(String obmedzenie) {
		this.obmedzenie.set(obmedzenie);
	}

	public String getPopis() {
		return popis.get();
	}

	public StringProperty popisProperty() {
		return popis;
	}

	public void setPopis(String popis) {
		this.popis.set(popis);
	}

	public Tovar getTovar() {
		return new Tovar(id, getNazov(), getMernaJednotka(), Double.parseDouble(getJednotkovaCena()),
				Integer.parseInt(getObmedzenie()), getPopis());
	}

	public String getError() {
		if (getNazov() == null || getNazov().isBlank())
			return "N·zov nesmie byù pr·zdny!";
		if (getMernaJednotka() == null)
			return "Mern· jednotka musÌ byù zadan·!";
		if (getJednotkovaCena() == null || getJednotkovaCena().isBlank())
			return "Jednotkov· cena musÌ byù zadan·!";
		if (getObmedzenie() == null || getObmedzenie().isBlank())
			return "Obmedzenie musÌ byù zdanÈ!";
		if (getPopis() == null || getPopis().isBlank())
			return "Popis nesmie byù pr·zdny!";
		return null;
	}

	@Override
	public String toString() {
		return "TovarFxModel [id=" + id + ", nazov=" + nazov + ", mernaJednotka=" + mernaJednotka + ", jednotkovaCena="
				+ jednotkovaCena + ", obmedzenie=" + obmedzenie + ", popis=" + popis + "]";
	}

}
