package com.example.service;

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
}
