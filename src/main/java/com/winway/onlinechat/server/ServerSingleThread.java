package com.winway.onlinechat.server;

import com.winway.onlinechat.common.DataType;
import com.winway.onlinechat.common.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServerSingleThread extends Thread
{
    private Socket singleSocket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    
    public ServerSingleThread(Socket linkSocket)
    {
        this.singleSocket = linkSocket;
    }
    
    private void setStream()
    {
        try
        {
            inputStream = new ObjectInputStream(singleSocket.getInputStream());
            outputStream = new ObjectOutputStream(singleSocket.getOutputStream());
        }
        catch(IOException e)
        {
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public void run()
    {
        while(true)
        {
            DataType currentData = null;
            try
            {
                if((currentData = (DataType) inputStream.readObject()) != null)
                {
                    switch(currentData)
                    {
                        case CHECK ->
                        {
                            User cur_check = (User) inputStream.readObject();
                            String passwd = ServerCore.userCheckMap.get(cur_check.getName());
                            if(passwd != null && passwd.equals(cur_check.getPassword()))
                            {
                                outputStream.writeBoolean(true);
                                //补全 outputStream.writeObject(ServerCore.logUsers.);
                            } else
                            {
                                outputStream.writeBoolean(false);
                            }
                        }
                        case MESSAGE ->
                        {
                        
                        }
                        case SHUTDOWN ->
                        {
                            //ServerCore.currentUsers.remove();
                        }
                    }
                }
            }
            catch(IOException | ClassNotFoundException e)
            {
                throw new RuntimeException(e);
            }
        }
    }
}
