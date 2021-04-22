package com.database.project;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class Coupon {

	@Getter
	private final int couponID;

	@Getter
	private final double value;
}
