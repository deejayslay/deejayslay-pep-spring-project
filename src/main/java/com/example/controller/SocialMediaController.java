package com.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.exception.DuplicateUsernameException;
import com.example.exception.AccountNotAuthorizedException;
import com.example.exception.CustomClientException;
import com.example.service.AccountService;
import com.example.service.MessageService;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */

@RestController
public class SocialMediaController {
    private AccountService accountService;
    private MessageService messageService;

    @Autowired
    public SocialMediaController(AccountService accountService, MessageService messageService) {
        this.accountService = accountService;
        this.messageService = messageService;
    }

    // exception handlers
    @ExceptionHandler(DuplicateUsernameException.class)
    public ResponseEntity<String> handleDuplicateUsernameException(DuplicateUsernameException ex) {
        return ResponseEntity.status(409).build();
    }

    @ExceptionHandler(AccountNotAuthorizedException.class)
    public ResponseEntity<String> handleAaccountNotAuthorizedException(AccountNotAuthorizedException ex) {
        return ResponseEntity.status(401).build();
    }

    @ExceptionHandler(CustomClientException.class)
    public ResponseEntity<String> customClientException(CustomClientException ex) {
        return ResponseEntity.status(400).build();
    }

    // REST mappings
    @PostMapping("/register")
    public ResponseEntity<Account> postAccount(@RequestBody Account account) throws DuplicateUsernameException, CustomClientException {
        Account newAccount = this.accountService.registerAccount(account);
        return ResponseEntity.status(200).body(newAccount);
    }

    @PostMapping("/login")
    public ResponseEntity<Account> loginAccount(@RequestBody Account account) throws AccountNotAuthorizedException {
        Account loggedInAccount = this.accountService.loginAccount(account);
        return ResponseEntity.status(200).body(loggedInAccount);
    }

    @PostMapping("/messages")
    public ResponseEntity<Message> postMessage(@RequestBody Message message) throws CustomClientException {
        // verify message's get_posted_by is a real account
        this.accountService.getAccountById(message.getPostedBy());
        Message newMessage = this.messageService.createMessage(message);
        return ResponseEntity.status(200).body(newMessage);
    }

    @GetMapping("/messages")
    public ResponseEntity<List<Message>> getAllMessages() {
        List<Message> messages = this.messageService.getAllMessages();
        return ResponseEntity.status(200).body(messages);
    }

    @GetMapping("/messages/{messageId}")
    public ResponseEntity<Message> getMessageById(@PathVariable int messageId) {
        Message foundMessage = this.messageService.getMessageById(messageId);
        return ResponseEntity.status(200).body(foundMessage);
    }
}
