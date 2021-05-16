package com.database.project;

import lombok.AllArgsConstructor;
import lombok.Data;


@AllArgsConstructor
@Data
public class BankAccount {

    private int bankAccountId;
    private int accountNumber;
    Bank bank;

    @Override
    public String toString(){
        return bank.getName() + " - " + accountNumber;
    }
}
