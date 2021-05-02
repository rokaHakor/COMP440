package database.com.project;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Item {

	private int itemID;

	private String name;

	private int quantity;

	private double price;

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
