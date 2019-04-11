package com.capgemini.bankapp.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;

import com.capgemini.bankapp.exception.AccountNotFoundException;
import com.capgemini.bankapp.exception.LowBalanceException;
import com.capgemini.bankapp.service.BankAccountService;
import com.capgemini.bankapp.service.impl.BankAccountServiceImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import javax.sql.DataSource;

public class BankAccountClient {


	public static void main(String[] args) {
	
		ApplicationContext context=new ClassPathXmlApplicationContext("context.xml");
		BankAccountService accountService=(BankAccountService)context.getBean("bankAccountServiceImpl");

		int choice;
		String accountHolderName;
		String accountType;
		double accountBalance;
		long accountId;
		long reciverAccountId;
		double amount;
		

		try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {

			while (true) {
				System.out.println(
						"1. Add New BankAccount\n2. Withdraw\n3. Deposit\n4. Fund Transfer\n5. Delete BankAccount\n6. Display All BankAccount Details\n7. Search BankAccount\n8. Check Balance\n9.Update Bank Account Details\n10. Exit\n");

				System.out.print("Enter your choice :");
				choice = Integer.parseInt(reader.readLine());

				switch (choice) {

				case 1:
					System.out.println("Enter your name:");
					accountHolderName = reader.readLine();
					System.out.println("Enter account type:");
					accountType = reader.readLine();
					System.out.println("Enter account Initial Balance:");
					accountBalance = Double.parseDouble(reader.readLine());
					BankAccount account = new BankAccount(accountHolderName, accountType, accountBalance);
					if (accountService.addNewBankAccount(account)) {
						System.out.println("Account created successfully");
					} else {
						System.out.println("Account can't created");
					}
					break;

				case 2:
					System.out.println("Enter your account Id:");
					accountId = Long.parseLong(reader.readLine());
					System.out.println("Enter amount you want to withdraw:");
					amount = Double.parseDouble(reader.readLine());

					try {
						System.out.println("Your Current Balance is:" + accountService.withdraw(accountId, amount));
					} catch (LowBalanceException e) {
						System.out.println(e.getMessage());
						e.printStackTrace();
					}

					break;
				case 3:
					System.out.println("Enter your account Id:");
					accountId = Long.parseLong(reader.readLine());
					System.out.println("Enter amount you want to Deposit:");
					amount = Double.parseDouble(reader.readLine());
					System.out.println("Your Current Balance is:" + accountService.deposit(accountId, amount));

					break;
				case 4:
					System.out.println("Enter Sender account Id:");
					accountId = Long.parseLong(reader.readLine());
					System.out.println("Enter Reciver Account Id:");
					reciverAccountId = Long.parseLong(reader.readLine());
					System.out.println("Enter amount you want to transfer:");
					amount = Double.parseDouble(reader.readLine());

					try {
						System.out.println("Your Current Balance is:"
								+ accountService.fundTransfer(accountId, reciverAccountId, amount));
					} catch (LowBalanceException | AccountNotFoundException e) {
						e.printStackTrace();
					}
					break;


				case 5:
					System.out.println("Enter your Account Id:");
					accountId = Long.parseLong(reader.readLine());

					if (accountService.deleteBankAccount(accountId)) {
						System.out.println("Account Deleted successfully..");
					} else {
						System.out.println("Account can't delete..");
						System.out.println();
					}
					break;


				case 6:
					List<BankAccount> accounts = accountService.findAllBankAccountsDetails();
					Iterator<BankAccount> iterator = accounts.iterator();

					while (iterator.hasNext()) {
						BankAccount account2 = iterator.next();
						System.out.println("Account Id :" + account2.getAccountId());
						System.out.println("AccountHolder Name :" + account2.getAccountHolderName());
						System.out.println("AccountType :" + account2.getAccountType());
						System.out.println("Account Balance :" + account2.getAccountBalance());
						System.out.println();

					}
					break;


				case 7:
					System.out.println("Enter your Account Id:");
					accountId = Long.parseLong(reader.readLine());
					try {
						BankAccount account2 = accountService.searchAccountDetails(accountId);

						System.out.println("Account Id :" + account2.getAccountId());
						System.out.println("AccountHolder Name :" + account2.getAccountHolderName());
						System.out.println("AccountType :" + account2.getAccountType());
						System.out.println("Account Balance :" + account2.getAccountBalance());
						System.out.println();
					} catch (AccountNotFoundException e) {
						System.out.println(e.getMessage());
						e.printStackTrace();
					}
					break;

				case 8:
					System.out.println("Enter your Account Id:");
					accountId = Long.parseLong(reader.readLine());
					System.out.println("Your current balance is :" + accountService.checkBalance(accountId));

					break;


				case 9:
					System.out.println("Enter your Account Id:");
					accountId = Long.parseLong(reader.readLine());
					System.out.println("Update your name:");
					accountHolderName = reader.readLine();
					System.out.println("Update account type:");
					accountType = reader.readLine();
					if (accountService.updateBankAccountDetails(accountId, accountHolderName, accountType)) {
						System.out.println("Bank Detail Updated Successfully....");
					}

					break;

				case 10:
					System.out.println("Thank you for Banking with us.");
					System.exit(0);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			
		}
	}

}
