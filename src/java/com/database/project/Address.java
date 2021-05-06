package com.database.project;

import lombok.AllArgsConstructor;
import lombok.Data;


@AllArgsConstructor
@Data
public class Address {

    private String address;
    private String city;
    private String state;
    private String country;
}
