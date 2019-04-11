package com.capgemini.bankapp.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.capgemini.bankapp.client.BankAccount;
import com.capgemini.bankapp.dao.BankAccountDao;
import com.capgemini.bankapp.exception.AccountNotFoundException;
//import com.capgemini.bankapp.util.DbUtil;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class BankAccountDaoImpl implements BankAccountDao {

	private JdbcTemplate jdbcTemplate;

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate){
		this.jdbcTemplate=jdbcTemplate;
	}

	@Override
	public boolean addNewBankAccount(BankAccount account) {

		String query = "INSERT INTO bankaccounts (customer_name,account_type,account_balance) VALUES  ('"+account.getAccountHolderName()+"','"+account.getAccountType()+"','"+account.getAccountBalance()+"')";
		int result = jdbcTemplate.update(query);

		if(result > 0)
			return true;
		else
			return false;

		}
		
	
	@Override
	public boolean deleteBankAccount(long accountId) {
		String query = "DELETE FROM bankaccounts WHERE account_id = '"+ accountId +"' ";  
		int result = jdbcTemplate.update(query);

		if(result > 0)
			return true;
		else
			return false;

		
	}

	@Override
	public boolean updateBankAccountDetails(long accountId, String accountHolderName, String accountType) {

		String query = "UPDATE bankaccounts SET customer_name= '"+ accountHolderName +"' ,account_type= '"+ accountType+"'  WHERE account_id= '"+ accountId+"' ";
		int result = jdbcTemplate.update(query);

		if(result > 0)
			return true;
		else
			return false;

	}

	@Override
	public List<BankAccount> findAllBankAccountsDetails() {
		String query1 = "SELECT * FROM bankaccounts";
		
		List<BankAccount> accounts = jdbcTemplate.query(query1,(resultSet,row)->{
						BankAccount account= new BankAccount(resultSet.getLong(1), 
						resultSet.getString(2), resultSet.getString(3),resultSet.getDouble(4));
						return account;
			});

		return accounts;
	}
		
	@Override
	public BankAccount searchAccountDetails(long accountId) throws AccountNotFoundException {
		String query1 = "SELECT * FROM bankaccounts WHERE account_id=" + accountId;
		BankAccount account = jdbcTemplate.queryForObject(query1,(resultSet,row)->{
						BankAccount accounts= new BankAccount(resultSet.getLong(1), 
						resultSet.getString(2), resultSet.getString(3),resultSet.getDouble(4));
						
						return accounts;
			});

		return account;
	}

	@Override
	public double getBalance(long accountId) {
		
		double balance = -1;
		balance = jdbcTemplate.queryForObject("SELECT account_balance FROM bankaccounts WHERE account_id=" + accountId,Double.class);
		return balance;	

	}
	
	@Override
	public void updateBalance(long accountId, double newBalance) {
	jdbcTemplate.update("UPDATE bankaccounts SET account_balance=? WHERE account_id=?",new Object[]{newBalance,accountId});
	}


}
