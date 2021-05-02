package database.com.project;

import lombok.AllArgsConstructor;
import lombok.Value;

@AllArgsConstructor
@Value
public class Coupon {

	int couponID;
	String code;
	String description;
	String status;

	float value;
}
