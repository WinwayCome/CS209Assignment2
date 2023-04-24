package com.winway.onlinechat.server;

import com.winway.onlinechat.common.User;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class ServerCore
{
    static ServerSocket server;
    static ArrayList<User> currentUsers;
    static ArrayList<User> logUsers;
    static HashMap<String, String> userCheckMap;
    
    public ServerCore() throws IOException, ClassNotFoundException
    {
        server = new ServerSocket(14889);
        currentUsers = new ArrayList<>();
        logUsers = new ArrayList<>();
        //ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream("Users.dat"));
        //logUsers = (ArrayList<UserCheck>) inputStream.readObject();
        userCheckMap = new HashMap<>();
        
    }
    
    public void serverCoreThread()
    {
        try
        {
            System.out.println("Server Listening...");
            while(true)
            {
                Socket cur_connection = server.accept();
                ServerSingleThread socketThread = new ServerSingleThread(cur_connection);
                socketThread.start();
                System.out.println("Connecting with client.");
            }
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
}
