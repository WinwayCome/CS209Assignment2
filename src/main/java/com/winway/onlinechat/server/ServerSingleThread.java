package com.winway.onlinechat.server;

import com.winway.onlinechat.common.ChatMessage;
import com.winway.onlinechat.common.DataType;
import com.winway.onlinechat.common.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

public class ServerSingleThread extends Thread
{
    private final Socket singleSocket;
    private final ArrayBlockingQueue<Object> queue = new ArrayBlockingQueue<>(5000);
    boolean connect = true;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    
    public ServerSingleThread(Socket linkSocket) throws IOException
    {
        this.singleSocket = linkSocket;
        System.out.println("Thread given");
    }
    
    private void setStream()
    {
        try
        {
            outputStream = new ObjectOutputStream(singleSocket.getOutputStream());
            inputStream = new ObjectInputStream(singleSocket.getInputStream());
            System.out.println("Connect.");
        }
        catch(IOException e)
        {
            throw new RuntimeException(e);
        }
    
    }
    
    private void listen()
    {
        DataType currentData;
        try
        {
            if(connect && singleSocket.isConnected() && (currentData = (DataType) inputStream.readObject()) != null)
            {
                switch(currentData)
                {
                    case CHECK ->
                    {
                        User cur_check = (User) inputStream.readObject();
                        User user = ServerCore.userCheckMap.get(cur_check.getName());
                        queue.add(DataType.CHECK);
                        if(user.getPassword().equals(cur_check.getPassword()))
                        {
                            queue.add(true);
                            queue.add(user);
                            this.setName(user.getName());
                            ServerCore.currentUsers.put(user.getName(), user);
                        } else
                        {
                            queue.add(false);
                        }
                    }
                    case MESSAGE ->
                    {
                        ChatMessage chatMessage = (ChatMessage) inputStream.readObject();
                        for(String receive : chatMessage.getTo())
                        {
                            User recUser;
                            if((recUser = ServerCore.userCheckMap.get(receive)) != null)
                            {
                                recUser.chatHistory.add(chatMessage);
                                ServerCore.userCheckMap.put(receive, recUser);
                                ServerCore.actions.add(new ChatMessage(chatMessage, receive));
                            }
                        }
                        
                    }
                    case SHUTDOWN ->
                    {
                        System.out.println("Shutdown " + this.getName());
                        ServerCore.currentUsers.remove(this.getName());
                        connect = false;
                    }
                    case ASK ->
                    {
                        System.out.println("Ask Users");
                        queue.add(DataType.ASK);
                    }
                }
            }
        }
        catch(IOException | ClassNotFoundException e)
        {
            e.printStackTrace();
        }
    }
    
    private void sendAll()
    {
        try
        {
            for(ChatMessage action : ServerCore.actions)
            {
                if(action.getTo().get(0).equals(this.getName()))
                {
                    outputStream.writeObject(DataType.MESSAGE);
                    outputStream.writeObject(action);
                    ServerCore.actions.remove(action);
                    outputStream.flush();
                }
            }
            while(!queue.isEmpty())
            {
                DataType link = (DataType) queue.poll(5000, TimeUnit.SECONDS);
                if(link != null)
                {
                    switch(link)
                    {
                        case ASK ->
                        {
                            outputStream.writeObject(DataType.ASK);
                            outputStream.flush();
                            outputStream.writeObject(new HashMap<>(ServerCore.currentUsers));
                            outputStream.flush();
                        }
                        case CHECK ->
                        {
                            outputStream.writeObject(DataType.CHECK);
                            if((Boolean) queue.poll(5000, TimeUnit.SECONDS))
                            {
                                outputStream.writeBoolean(true);
                                outputStream.writeObject(queue.poll(5000, TimeUnit.SECONDS));
                            } else outputStream.writeBoolean(false);
                            outputStream.flush();
                        }
                    }
                }
            }
        }
        catch(IOException | InterruptedException e)
        {
            throw new RuntimeException(e);
        }
    }
    
    public void run()
    {
        System.out.println("Start");
        setStream();
        System.out.println("Start1");
        new Thread(() ->
        {
            while(connect) listen();
        }).start();
        new Thread(() ->
        {
            while(connect) sendAll();
        }).start();
        System.out.println("Start2");
    }
}
