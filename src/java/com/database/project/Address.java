package com.database.project;

import lombok.AllArgsConstructor;
import lombok.Data;


@AllArgsConstructor
@Data
public class Address {

    private int userAddressId;
    private String address;
    private String city;
    private String state;
    private String country;

    @Override
    public String toString(){
        return address;
    }
}
