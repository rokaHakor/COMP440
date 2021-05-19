package com.database.project;

import lombok.AllArgsConstructor;
import lombok.Data;


@AllArgsConstructor
@Data
public class BankAccount {

	private int bankAccountId;
	private int accountNumber;
	private Bank bank;

	@Override
	public String toString() {
		return bank.getName() + " - " + accountNumber;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof BankAccount)) {
			return false;
		}
		return this.getBank().getName().equals(((BankAccount) obj).getBank().getName()) && this.getAccountNumber() == ((BankAccount) obj).getAccountNumber();
	}
}
