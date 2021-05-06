package com.database.example;

public class Address {

	private int id;
	private String addressOne;
	private String addressTwo;
	private int apt;
	private String city;
	private String state;
	private String country;
	private int contactId;

	public Address(int id, String addressOne, String addressTwo, int apt, String city, String state, String country, int contactId) {
		this.id = id;
		this.addressOne = addressOne;
		this.addressTwo = addressTwo;
		this.apt = apt;
		this.city = city;
		this.state = state;
		this.country = country;
		this.contactId = contactId;
	}

	public Address(String line) {
		String[] values = line.split(",");
		for (int x = 0; x < values.length; x++) {
			switch(x){
				case 0:
					id = Integer.parseInt(values[x]);
					break;
				case 1:
					addressOne = values[x];
					break;
				case 2:
					addressTwo = values[x];
					break;
				case 3:
					apt = Integer.parseInt(values[x]);
					break;
				case 4:
					city = values[x];
					break;
				case 5:
					state = values[x];
					break;
				case 6:
					country = values[x];
					break;
				case 7:
					contactId = Integer.parseInt(values[x]);
					break;
			}
		}
	}

	@Override
	public String toString() {
		return id + "," + addressOne + "," + addressTwo + "," + apt + "," + city + "," + state + "," + country + "," + contactId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAddressOne() {
		return addressOne;
	}

	public void setAddressOne(String addressOne) {
		this.addressOne = addressOne;
	}

	public String getAddressTwo() {
		return addressTwo;
	}

	public void setAddressTwo(String addressTwo) {
		this.addressTwo = addressTwo;
	}

	public int getApt() {
		return apt;
	}

	public void setApt(int apt) {
		this.apt = apt;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public int getContactId(){
		return contactId;
	}

	public void setContactId(int id){
		this.contactId = id;
	}
}
