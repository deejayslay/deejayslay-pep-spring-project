package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.exception.DuplicateUsernameException;
import com.example.exception.AccountNotAuthorizedException;
import com.example.exception.CustomClientException;
import com.example.repository.AccountRepository;


@Service
public class AccountService {
    private AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    // helper method for registering and logging in accounts
    public Account getAccountByUsername(String username){
        return this.accountRepository.findByUsername(username).orElse(null);
    }

    public Account getAccountById(int id) throws CustomClientException {
        return this.accountRepository.findById(id).orElseThrow(() -> new CustomClientException());
    }

    public Account registerAccount(Account account) throws DuplicateUsernameException, CustomClientException {
        Account foundAccount = this.getAccountByUsername(account.getUsername());
        if (foundAccount != null) {
            throw new DuplicateUsernameException();
        }
        if ((account.getUsername().length() == 0) || (account.getPassword().length() < 4)) {
            throw new CustomClientException();
        }
        return this.accountRepository.save(account);
    }

    public Account loginAccount(Account account) throws AccountNotAuthorizedException {
        Account foundAccount = this.getAccountByUsername(account.getUsername());
        if (foundAccount == null) {
            throw new AccountNotAuthorizedException();
        }
        if (!foundAccount.getPassword().equals(account.getPassword())) {
            throw new AccountNotAuthorizedException();
        }
        return foundAccount;
    }

}
