package com.database.project;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.database.project.Item;

public class Cart {

	private final HashMap<Integer, Item> cartItems = new HashMap<>();

	private double totalPrice;

	@Setter
	@Getter
	private Coupon coupon;

	public Cart() {
		totalPrice = 0;
		coupon = null;
	}

	public void add(Item item) {
		totalPrice += item.getPrice() * item.getQuantity();
		if (contains(item)) {
			cartItems.get(item.getItemID()).addQuantity(item.getQuantity());
			return;
		}
		cartItems.put(item.getItemID(), item);
	}

	public boolean contains(Item item) {
		return cartItems.containsKey(item.getItemID());
	}

	public void remove(Item item) {
		if (!contains(item)) {
			return;
		}
		Item inCartItem = cartItems.get(item.getItemID());
		int inCartQuantity = inCartItem.getQuantity();
		if (inCartQuantity > item.getQuantity()) {
			totalPrice -= item.getPrice() * item.getQuantity();
			inCartItem.addQuantity(-1 * item.getQuantity());
			return;
		}
		totalPrice -= item.getPrice() * inCartQuantity;
		cartItems.remove(item.getItemID());
	}

	public void clear() {
		totalPrice = 0;
		coupon = null;
		cartItems.clear();
	}

	public double getTotalPrice() {
		if (coupon != null) {
			return totalPrice * coupon.getValue();
		}
		return totalPrice;
	}

	public List<Item> items() {
		return new ArrayList<>(cartItems.values());
	}
}
