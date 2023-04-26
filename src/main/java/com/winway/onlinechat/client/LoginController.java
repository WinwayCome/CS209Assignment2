package com.winway.onlinechat.client;

import com.winway.onlinechat.common.ChatInf;
import com.winway.onlinechat.common.ChatMessage;
import com.winway.onlinechat.common.DataType;
import com.winway.onlinechat.common.User;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class LoginController
{
    public static ArrayList<User> currentList = new ArrayList<>();
    @FXML
    public AnchorPane LOGINPANE;
    @FXML
    public TextField NAMEFIELD;
    @FXML
    public PasswordField PASSFIELD;
    @FXML
    public Button LOGINPRESS;
    @FXML
    public AnchorPane MAINPANE;
    @FXML
    public Button FILEBUTTON;
    @FXML
    public Button SENDBUTTON;
    @FXML
    public Button EMOJIBUTTON;
    @FXML
    public TextArea WRITEAREA;
    @FXML
    public TextArea SHOWAREA;
    @FXML
    public ListView<String> CURRENTLIST;
    @FXML
    public Menu ONLINEUSER;
    @FXML
    public Button CREATECHAT;
    @FXML
    public CheckMenuItem select1, select2, select3, select4;
    @FXML
    public Button A, B, C, D;
    @FXML
    public TextField USERNAME;
    @FXML
    public TextField NEWMESSAGE;
    ClientCore client;
    ConcurrentHashMap<String, ChatInf> chatInfArrayList = new ConcurrentHashMap<>();
    String currentText = " ";
    
    public LoginController()
    {
        this.client = new ClientCore();
        this.client.start();
    }
    
    @FXML
    public void onLoginButton() throws IOException, ClassNotFoundException
    {
        if(NAMEFIELD.getText() != null && PASSFIELD.getText() != null && !NAMEFIELD.getText().isEmpty() && !PASSFIELD.getText().isEmpty())
        {
            if(client.checkID(NAMEFIELD.getText(), PASSFIELD.getText()))
            {
                LOGINPANE.setVisible(false);
                MAINPANE.setVisible(true);
                for(ChatMessage curCheck : client.getUser().getChatHistory())
                {
                    if(chatInfArrayList.containsKey(curCheck.getChatName()))
                    {
                        chatInfArrayList.get(curCheck.getChatName()).add(curCheck);
                    } else
                    {
                        chatInfArrayList.put(curCheck.getChatName(), new ChatInf(curCheck));
                    }
                }
            }
        }
    }
    
    @FXML
    public void onSendButton() throws IOException
    {
        if(WRITEAREA.getText().isEmpty()) return;
        System.out.println("Send begin.");
        ChatMessage message = new ChatMessage(new Date(), CURRENTLIST.getSelectionModel().getSelectedItem(), WRITEAREA.getText(), this.client.getUser().getName(), chatInfArrayList.get(CURRENTLIST.getSelectionModel().getSelectedItem()).getUsers());
        System.out.println("Send message.");
        client.sendMessage(message);
        WRITEAREA.clear();
    }
    
    @FXML
    public void onMenuButton()
    {
        client.askOnline();
        select1.setVisible(false);
        select2.setVisible(false);
        select3.setVisible(false);
        select4.setVisible(false);
        if(currentList.size() > 0)
        {
            select1.setText(currentList.get(0).getName());
            select1.setVisible(true);
        }
        if(currentList.size() > 1)
        {
            select2.setText(currentList.get(1).getName());
            select2.setVisible(true);
        }
        if(currentList.size() > 2)
        {
            select3.setText(currentList.get(2).getName());
            select3.setVisible(true);
        }
        if(currentList.size() > 3)
        {
            select4.setText(currentList.get(3).getName());
            select4.setVisible(true);
        }
    }
    
    @FXML
    public void onCreateButton()
    {
        ArrayList<User> arrayList = new ArrayList<>();
        if(select1.isSelected())
        {
            for(User user : currentList)
            {
                if(user.getName().equals(select1.getText())) arrayList.add(user);
            }
        }
        if(select2.isSelected())
        {
            for(User user : currentList)
            {
                if(user.getName().equals(select2.getText())) arrayList.add(user);
            }
        }
        if(select3.isSelected())
        {
            for(User user : currentList)
            {
                if(user.getName().equals(select3.getText())) arrayList.add(user);
            }
        }
        if(select4.isSelected())
        {
            for(User user : currentList)
            {
                if(user.getName().equals(select4.getText())) arrayList.add(user);
            }
        }
        for(User a : arrayList)
            if(a.getName().equals(this.client.getName())) arrayList.remove(a);
        
        if(arrayList.size() == 0) return;
        arrayList.add(this.client.getUser());
        arrayList.sort(Comparator.comparingInt(o -> o.getName().hashCode()));
        StringBuilder name = new StringBuilder();
        if(arrayList.size() <= 3) for(int i = 0; i < arrayList.size(); ++i)
        {
            if(i == 0) name.append(arrayList.get(i).getName());
            else name.append(", ").append(arrayList.get(i).getName());
        }
        else
        {
            name.append(arrayList.get(0).getName());
            for(int i = 1; i < 3; ++i)
            {
                name.append(", ").append(arrayList.get(i).getName());
            }
            name.append("...");
        }
        name.append("(").append(arrayList.size()).append(")");
        if(CURRENTLIST.getItems().contains(name.toString()))
        {
            show(name.toString());
        } else
        {
            chatInfArrayList.put(name.toString(), new ChatInf(name.toString(), arrayList));
            CURRENTLIST.getItems().clear();
            CURRENTLIST.setItems(FXCollections.observableList(chatInfArrayList.values().stream().map(ChatInf::getChatName).collect(Collectors.toList())));
            CURRENTLIST.refresh();
        }
    }
    
    public void show(String chatName)
    {
        if(chatName == null) return;
        ArrayList<ChatMessage> link = chatInfArrayList.get(chatName).getChatMessages();
        System.out.println("Message get");
        link.sort((a, b) ->
        {
            if(a.getTime().before(b.getTime())) return 0;
            return 1;
        });
        System.out.println("link.sort");
        StringBuilder stringBuilder = new StringBuilder();
        for(ChatMessage chatLink : link)
        {
            stringBuilder.append(chatLink.getFrom()).append(":\n").append(chatLink.getTime()).append("\n").append(chatLink.getText()).append("\n\n");
        }
        SHOWAREA.setText(stringBuilder.toString());
        currentText = stringBuilder.toString();
        System.out.println("Show Info");
        StringBuilder sb = new StringBuilder();
        chatInfArrayList.get(chatName).getUsers().forEach(x -> sb.append(x).append(" "));
        System.out.println("Find usernames");
        USERNAME.setText(sb.toString());
    }
    
    public void initialize()
    {
        System.out.println("Connecting.");
        MAINPANE.setVisible(false);
        LOGINPANE.setVisible(true);
        SHOWAREA.setEditable(false);
        CURRENTLIST.getItems().clear();
        CURRENTLIST.setItems(FXCollections.observableList(chatInfArrayList.values().stream().map(ChatInf::getChatName).collect(Collectors.toList())));
        CURRENTLIST.refresh();
        CURRENTLIST.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) ->
        {
            System.out.println("Select" + newValue);
            show(newValue);
        });
        USERNAME.setEditable(false);
    }
    
    public class ClientCore extends Thread
    {
        public ArrayBlockingQueue<ChatMessage> actions = new ArrayBlockingQueue<>(5000);
        ObjectInputStream inputStream;
        ObjectOutputStream outputStream;
        private Socket client;
        private User user;
        
        public synchronized void close()
        {
            try
            {
                outputStream.writeObject(DataType.SHUTDOWN);
                outputStream.flush();
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
        
        public synchronized boolean sendMessage(ChatMessage message) throws IOException
        {
            if(client == null || !client.isConnected())
            {
                System.out.println("Cannot connect to the server.");
                return false;
            }
            System.out.println("Send message in core.");
            try
            {
                outputStream.writeObject(DataType.MESSAGE);
                outputStream.writeObject(message);
                System.out.println("Output down.");
            }
            catch(IOException e)
            {
                throw new RuntimeException(e);
            }
            return true;
        }
        
        public User getUser()
        {
            return this.user;
        }
        
        public synchronized void askOnline()
        {
            if(client == null || !client.isConnected())
            {
                System.out.println("Cannot connect to the server.");
                return;
            }
            try
            {
                outputStream.writeObject(DataType.ASK);
                outputStream.flush();
            }
            catch(IOException e)
            {
                throw new RuntimeException(e);
            }
        }
        
        public synchronized boolean checkID(String name, String password) throws IOException, ClassNotFoundException
        {
            if(client == null || !client.isConnected())
            {
                System.out.println("Cannot connect to the server.");
                return false;
            }
            try
            {
                outputStream.writeObject(DataType.CHECK);
                User loginUser = new User(name, password, new ArrayList<>());
                outputStream.writeObject(loginUser);
                outputStream.flush();
                if(inputStream.readObject().equals(DataType.CHECK))
                {
                    if(inputStream.readBoolean()) this.user = (User) inputStream.readObject();
                } else throw new IOException();
                if(this.user != null)
                {
                    System.out.println("find user");
                    return true;
                }
            }
            catch(IOException | ClassNotFoundException e)
            {
                throw new RuntimeException(e);
            }
            return false;
        }
        
        @Override
        public void run()
        {
            
            while(true)
            {
                if(client == null || !client.isConnected())
                {
                    while(true)
                    {
                        try
                        {
                            client = new Socket("127.0.0.1", 14889);
                            outputStream = new ObjectOutputStream(client.getOutputStream());
                            inputStream = new ObjectInputStream(client.getInputStream());
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
                if(this.getUser() == null) continue;
                System.out.println("us");
                DataType dataType = null;
                try
                {
                    if((dataType = (DataType) inputStream.readObject()) != null)
                    {
                        switch(dataType)
                        {
                            case MESSAGE ->
                            {
                                System.out.println("Try Get Message");
                                ChatMessage chatMessage = (ChatMessage) inputStream.readObject();
                                System.out.println("Get Message");
                                System.out.println(chatMessage.getChatName());
                                System.out.println(chatMessage.getFrom());
                                System.out.println(chatMessage.getText());
                                System.out.println(chatMessage.getTime());
                                System.out.println();
                                System.out.println("Check local");
                                if(LoginController.this.chatInfArrayList.contains(chatMessage.getChatName()))
                                {
                                    System.out.println("Local yes");
                                    ChatInf link = LoginController.this.chatInfArrayList.get(chatMessage.getChatName());
                                    System.out.println("Find origin data");
                                    link.add(chatMessage);
                                    LoginController.this.chatInfArrayList.put(chatMessage.getChatName(), link);
                                    System.out.println("Update down");
                                } else
                                {
                                    System.out.println("Local no");
                                    LoginController.this.chatInfArrayList.put(chatMessage.getChatName(), new ChatInf(chatMessage));
                                    System.out.println("Add down");
                                }
                                Platform.runLater(() ->
                                {
                                    CURRENTLIST.getItems().clear();
                                    CURRENTLIST.setItems(FXCollections.observableList(chatInfArrayList.values().stream().map(ChatInf::getChatName).collect(Collectors.toList())));
                                    CURRENTLIST.refresh();
                                    
                                    System.out.println("Reoutput down");
                                    if(CURRENTLIST.getSelectionModel().getSelectedItem() != null && CURRENTLIST.getSelectionModel().getSelectedItem().equals(chatMessage.getChatName()))
                                    {
                                        System.out.println("Show begin");
                                        show(chatMessage.getChatName());
                                        System.out.println("Show down");
                                    } else
                                    {
                                        NEWMESSAGE.setText("You have a new message from " + chatMessage.getChatName());
                                    }
                                });
                            }
                            case ASK ->
                            {
                                System.out.println("ask find");
                                HashMap<String, User> concurrentHashMap = (HashMap<String, User>) inputStream.readObject();
                                System.out.println(concurrentHashMap);
                                if(concurrentHashMap != null)
                                {
                                    System.out.println("IsnotNull");
                                    ArrayList<User> arrayList = new ArrayList<>(concurrentHashMap.values());
                                    arrayList = (ArrayList<User>) arrayList.stream().filter(x -> !Objects.equals(x.getName(), this.user.getName())).collect(Collectors.toList());
                                    currentList = arrayList;
                                } else currentList = new ArrayList<>();
                            }
                        }
                    }
                }
                catch(IOException | ClassNotFoundException e)
                {
                    //do nothing
                }
                
            }
        }
    }
}

