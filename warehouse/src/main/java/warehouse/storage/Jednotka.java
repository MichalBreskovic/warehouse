package warehouse.storage;

public class Jednotka {

	private int id;
	private String Oznacenie;

	public Jednotka() {
	}
	
	public Jednotka(int id, String oznacenie) {
		this.id = id;
		Oznacenie = oznacenie;
	}

	public Jednotka(String oznacenie) {
		Oznacenie = oznacenie;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getOznacenie() {
		return Oznacenie;
	}

	public void setOznacenie(String oznacenie) {
		Oznacenie = oznacenie;
	}

	@Override
	public String toString() {
		return Oznacenie;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
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
		Jednotka other = (Jednotka) obj;
		if (id != other.id)
			return false;
		return true;
	}

}
