package com.database.project;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;

@AllArgsConstructor
public class SoldItem {

	@Getter
	private final Item item;

	@Getter
	private final Date deliveryDate;

	@Getter
	private final String status;
}
