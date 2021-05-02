package database.com.project;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Inventory {

	private final HashMap<Integer, Item> cartItems = new HashMap<>();

	public Inventory(Item[] items) {
		for (Item item : items) {
			cartItems.put(item.getItemID(), item);
		}
	}

	public List<Item> getAllItems() {
		return new ArrayList<>(cartItems.values());
	}

	public Item getItem(int itemID) {
		return cartItems.getOrDefault(itemID, null);
	}

	public void addItem(Item item) {
		cartItems.put(item.getItemID(), item);
	}
}
