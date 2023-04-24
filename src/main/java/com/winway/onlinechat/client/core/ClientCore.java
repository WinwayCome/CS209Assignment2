package com.winway.onlinechat.client.core;

import com.winway.onlinechat.common.ChatMessage;
import com.winway.onlinechat.common.DataType;
import com.winway.onlinechat.common.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Date;

public class ClientCore
{
    private Socket client;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    
    private User user;
    
    public ClientCore()
    {
        System.out.println("1");
        while(true)
        {
            System.out.println("2");
            try
            {
                this.client = new Socket("127.0.0.1", 14889);
                this.outputStream = new ObjectOutputStream(this.client.getOutputStream());
                this.inputStream = new ObjectInputStream(this.client.getInputStream());
                System.out.println("Server connecting.");
                break;
            }
            catch(IOException IOe)
            {
                System.out.println("Cannot connect to server, reconnect in 2 sec...");
                IOe.printStackTrace();
                try
                {
                    Thread.sleep(2000);
                }
                catch(InterruptedException Ine)
                {
                    System.out.println("Reconnected Thread Failed.");
                    Ine.printStackTrace();
                    System.exit(1);
                }
            }
        }
    }
    
    
    public void close()
    {
        try
        {
            outputStream.writeObject(DataType.SHUTDOWN);
            client.close();
            outputStream.close();
            inputStream.close();
        }
        catch(IOException IOe)
        {
            System.out.println("Client Close failed.");
            IOe.printStackTrace();
        }
    }
    
    public boolean checkID(String name, String password) throws IOException, ClassNotFoundException
    {
        outputStream.writeObject(DataType.CHECK);
        User loginUser = new User(name, password);
        outputStream.writeObject(loginUser);
        if(inputStream.readBoolean())
        {
            User user = (User) inputStream.readObject();
            return true;
        }
        return false;
    }
    
    public void sendMessage(String text, String to) throws IOException
    {
        outputStream.writeObject(DataType.MESSAGE);
        outputStream.writeObject(new ChatMessage(new Date(), text, user.getName(), to));
    }
}
