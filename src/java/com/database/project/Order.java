package com.database.project;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
public class Order {

	@Getter
	private final int orderID;

	@Getter
	private final Date orderDate;

	@Getter
	private final Coupon coupon;

	@Getter
	private final List<SoldItem> soldItems;

	public void addItem(SoldItem item){
		if(item != null){
			soldItems.add(item);
		}
	}
}
