package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Message;
import com.example.exception.CustomClientException;
import com.example.repository.MessageRepository;

@Service
public class MessageService {
    MessageRepository messageRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public Message createMessage(Message message) throws CustomClientException {
        if ((message.getMessageText().length() == 0) || (message.getMessageText().length() > 255)) {
            throw new CustomClientException();
        }
        return this.messageRepository.save(message);
    }

    public List<Message> getAllMessages() {
        return this.messageRepository.findAll();
    }

    public Message getMessageById(int id) {
        return this.messageRepository.findById(id).orElse(null);
    }

    // returns number of rows affected; 1 if message deleted, null if message id not found
    public Integer deleteMessageById(int id) {
        Message foundMessage = this.getMessageById(id);
        if (foundMessage == null) {
            return null;
        }
        this.messageRepository.deleteById(id);
        return 1;
    }
}
