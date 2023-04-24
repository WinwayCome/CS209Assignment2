package com.winway.onlinechat.common;

import java.util.Date;

public class ChatMessage
{
    private final Date time;
    private final String text;
    private final String from;
    private final String to;
    
    public ChatMessage(Date time, String text, String from, String to)
    {
        this.time = time;
        this.text = text;
        this.from = from;
        this.to = to;
    }
    
    public Date getTime()
    {
        return time;
    }
    
    public String getText()
    {
        return text;
    }
    
    public String getFrom()
    {
        return from;
    }
    
    public String getTo()
    {
        return to;
    }
}
