package com.database.project;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public class Item {

	@Getter
	@Setter
	private int itemID;

	@Getter
	@Setter
	private String name;

	@Getter
	@Setter
	private int quantity;

	@Getter
	@Setter
	private double price;

	@Getter
	@Setter
	private String description;

	public Item(Item other, int quantity) {
		this.itemID = other.getItemID();
		this.name = other.getName();
		this.quantity = quantity;
		this.price = other.getPrice();
		this.description = other.getDescription();
	}

	public void addQuantity(int amount) {
		quantity += amount;
	}
}
