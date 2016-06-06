package dao;

import dto.Weight;

public class WeightHandler {

	private Weight weight;
	
	public WeightHandler(){
		this.weight = new Weight();
	}
	
	public void add(double weight){
		this.weight.setNetto(this.weight.getNetto()+weight);
	}
	
	public void tarer(){
		weight.setTara(weight.getNetto());
		weight.setNetto(0);
	}
	
	public void reset(){
		weight.setTara(0);
		weight.setNetto(0);
	}
}