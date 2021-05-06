package com.database.project;

import lombok.AllArgsConstructor;
import lombok.Data;


@AllArgsConstructor
@Data
public class CartItem {

    int cartItemId;
    int quantity;

    String name;
    double price;
    String description;
}
