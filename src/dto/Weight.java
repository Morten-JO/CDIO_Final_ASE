package dto;

public class Weight {

	private double tara; //emballage af varen
	private double netto; //indholdet af varen
	
	public Weight(){
		tara = 0;
		netto = 0;
	}
	
	public double getNetto(){
		return netto;
	}
	
	public double getTara(){
		return tara;
	}
	
	public void setNetto(double netto){
		this.netto = netto;
	}
	
	public void setTara(double tara){
		this.tara = tara;
	}
	
	public double getBrutto(){
		return tara + netto;
	}
}