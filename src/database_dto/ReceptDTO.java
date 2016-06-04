package database_dto;

/**
 * Recept Data Objekt
 * 
 * @author mn/tb
 * @version 1.2
 */

public class ReceptDTO {
	/** Recept nr i omraadet 1-99999999 */
	int receptId;
	/** Receptnavn min. 2 max. 20 karakterer */
	String receptNavn;

	/** liste af kompenenter i recepten */

	public ReceptDTO(String receptNavn) {
		this.receptNavn = receptNavn;
	}

	public int getReceptId() {
		return receptId;
	}

	public void setReceptId(int receptId) {
		this.receptId = receptId;
	}

	public String getReceptNavn() {
		return receptNavn;
	}

	public void setReceptNavn(String receptNavn) {
		this.receptNavn = receptNavn;
	}

	public String toString() {
		return receptId + "\t" + receptNavn;
	}
}
