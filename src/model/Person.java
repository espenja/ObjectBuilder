package model;

public class Person {
	
	private String navn;
	private String etternavn;
	private int alder;
	
	public Person(String navn, String etternavn, int alder) {
		this.navn = navn;
		this.etternavn = etternavn;
		this.alder = alder;
	}

	public String getNavn() {
		return navn;
	}

	public String getEtternavn() {
		return etternavn;
	}

	public int getAlder() {
		return alder;
	}
}
