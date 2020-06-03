package it.polito.tdp.borders.model;

public class Country implements Comparable<Country> {

	private int countryCode;
	private String countryName;
	private String countryAbb;
	
	public Country(int countryCode, String countryName, String countryAbb) {
		super();
		this.countryCode = countryCode;
		this.countryName = countryName;
		this.countryAbb = countryAbb;
	}

	public int getCountryCode() {
		return countryCode;
	}

	public String getCountryName() {
		return countryName;
	}

	public String getCountryAbb() {
		return countryAbb;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + countryCode;
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
		Country other = (Country) obj;
		if (countryCode != other.countryCode)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Country [countryCode=" + countryCode + ", countryName=" + countryName + ", countryAbb=" + countryAbb
				+ "]";
	}
	
	public int compareTo(Country o) {
		return this.getCountryName().compareTo(o.getCountryName());
	}

}
