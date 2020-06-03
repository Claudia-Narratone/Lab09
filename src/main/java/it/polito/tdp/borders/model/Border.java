package it.polito.tdp.borders.model;

public class Border {
	
	private Country c1;
	private Country c2;
	
	public Border(Country c1, Country c2) {
		super();
		this.c1 = c1;
		this.c2 = c2;
	}

	public Country getC1() {
		return c1;
	}

	public Country getC2() {
		return c2;
	}

	@Override
	public String toString() {
		return "Border [c1=" + c1 + ", c2=" + c2 + "]";
	}
	
	

}
