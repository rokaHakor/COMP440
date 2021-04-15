package com.database.example;

import java.util.ArrayList;

public class Contact {

	private int id;
	private String firstName;
	private String lastName;
	private String gender;
	public final ArrayList<Integer> addresses = new ArrayList<>();
	public final ArrayList<Integer> phones = new ArrayList<>();

	public Contact(int id, String firstName, String lastName, String gender) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.gender = gender;
	}

	public Contact(String line) {
		String[] values = line.split(",");
		for (int x = 0; x < values.length; x++) {
			switch(x) {
				case 0:
					id = Integer.parseInt(values[x]);
					break;
				case 1:
					firstName = values[x];
					break;
				case 2:
					lastName = values[x];
					break;
				case 3:
					gender = values[x];
					break;
			}
		}
	}

	@Override
	public String toString() {
		return id + "," + firstName + "," + lastName + "," + gender;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
