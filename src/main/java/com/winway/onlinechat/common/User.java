package com.winway.onlinechat.common;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable
{
    private String name;
    private String password;
    private ArrayList<ChatMessage> chatHistory;
    
    public User(String name, String password, ArrayList<ChatMessage> chatHistory)
    {
        this.name = name;
        this.password = password;
        this.chatHistory = chatHistory;
    }
    
    public User(String name, String password)
    {
        this.name = name;
        this.password = password;
    }
    
    public String getPassword()
    {
        return this.password;
    }
    
    public String getName()
    {
        return this.name;
    }
    
    public ArrayList<ChatMessage> getChatHistory()
    {
        return this.chatHistory;
    }
}
