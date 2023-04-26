package com.winway.onlinechat.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class ChatMessage implements Serializable
{
    private final Date time;
    private final ArrayList<String> to;
    private final String text;
    private final String from;
    private String chatName;
    
    public ChatMessage(Date time, String chatName, String text, String from, ArrayList<String> to)
    {
        this.time = time;
        this.chatName = chatName;
        this.text = text;
        this.from = from;
        this.to = to;
    }
    
    public ChatMessage(ChatMessage chatMessageAll, String chatName)
    {
        this.time = chatMessageAll.time;
        this.chatName = chatMessageAll.chatName;
        this.from = chatMessageAll.from;
        this.to = new ArrayList<>();
        this.to.add(chatName);
        this.text = chatMessageAll.text;
    }
    
    public Date getTime()
    {
        return time;
    }
    
    public String getChatName()
    {
        return chatName;
    }
    
    public String getText()
    {
        return text;
    }
    
    public String getFrom()
    {
        return from;
    }
    
    public void setChatName(String chatName)
    {
        this.chatName = chatName;
    }
    
    public ArrayList<String> getTo()
    {
        return to;
    }
}
