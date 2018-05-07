import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by yakov on 5/6/2018.
 */
import java.net.*;
import java.io.*;
import java.util.ArrayList;

public class ChatServer implements Runnable
{
    private String name = null;
    private ChatServerThread clients[] = new ChatServerThread[50];
    private ServerSocket server = null;
    private Thread       thread = null;
    private int clientCount = 0;
    ArrayList<String> pairs = new ArrayList<String>();


    public ChatServer(int port)
    {
        try {
            System.out.println("Binding to port " + port + ", please wait  ...");
            server = new ServerSocket(port);
            System.out.println("Server started: " + server);
            start();
        }
        catch(IOException ioe) {
            System.out.println("Can not bind to port " + port + ": " + ioe.getMessage()); }
        }

    public void run() {
        while (thread != null) {
            try {
                System.out.println("Waiting for a client ...");
                addThread(server.accept());
            }
            catch(IOException ioe) {
                System.out.println("Server accept error: " + ioe); stop();
            }
        }
    }
    public void start()  {
        if (thread == null) {
            thread = new Thread(this);
            thread.start();
        }
    }
    public void stop()   {
        if (thread != null) {
            thread.stop();
            thread = null;
        }
    }
    private int findClient(int ID)
    {  for (int i = 0; i < clientCount; i++)
        if (clients[i].getID() == ID)
            return i;
        return -1;
    }
    private ChatServerThread findClient2(int ID)
    {  for (int i = 0; i < clientCount; i++)
        if (clients[i].getID() == ID)
            return clients[i];
        return null;
    }
    public synchronized void handle(int ID, String input)
    {
        ArrayList<ChatServerThread> cst = new ArrayList<>();
        ChatServerThread cs = null;
        if(findClient(ID) != -1){
            cs = findClient2(ID);
            cst.add(cs);
        }
        if(input.toLowerCase().startsWith("register me as")){
            cs.setUserName(input.split(" ")[3]);
            cs.send("Registered as " + input.split(" ")[3]);
            return;
        }
        if(cs.getUserName() == null){
            cs.send("must register first");
            return;
        }
        if(input.toLowerCase().startsWith("i would like to talk with")){
            addPairs(ID, input.split(" ")[6]);
            cs.send("now chatting with "+ input.split(" ")[6]);
            return;
        }
        if (input.equals(".bye")) {
            clients[findClient(ID)].send(".bye");
            remove(ID);
        }
        else{
            send(ID,input);
        }
    }

    private void send(int ID, String msg){
        ArrayList<String> pairs2 = new ArrayList<>();
        for(String s : pairs){
            String[] s2 = s.split(" ");
            for(int i = 0; i < s2.length; i++){
                if(Integer.parseInt(s2[i]) == ID)pairs2.add(s);
            }
        }
        ArrayList<Integer> ints = new ArrayList<>();
        for(String s: pairs2){
            String[] s2 = s.split(" ");
            for(int i = 0; i < s2.length; i++){
                int j = Integer.parseInt(s2[i]);
                if(!ints.contains(j))ints.add(j);
            }
        }
        for(int i: ints){
            for(int j = 0 ; j < clientCount; j ++){
                if(clients[j].getID() == i){
                    clients[j].send(msg);
                }
            }
        }
    }

    private void addPairs(int id, String s) {
        if(findClientByName(s) != null){
            String str = ""+id+" "+findClientByName(s).getID();
            pairs.add(str);
        }
    }

    private ChatServerThread findClientByName(String s) {
        for(int i = 0; i < clientCount; i++){
            if(clients[i].getUserName().equals(s)) return clients[i];
        }
        return null;
    }

    public synchronized void remove(int ID)
    {
        int pos = findClient(ID);
        if (pos >= 0)
        {  ChatServerThread toTerminate = clients[pos];
            System.out.println("Removing client thread " + ID + " at " + pos);
            if (pos < clientCount-1)
                for (int i = pos+1; i < clientCount; i++)
                    clients[i-1] = clients[i];
            clientCount--;
            removePairs(ID);
            try
            {  toTerminate.close(); }
            catch(IOException ioe)
            {  System.out.println("Error closing thread: " + ioe); }
            toTerminate.stop(); }
    }

    private void removePairs(int id) {
        String toRemove = null;
        for(String s : pairs){
            String[] s2 = s.split(" ");
            for(int i = 0 ; i < s2.length; i ++){
                if(Integer.parseInt(s2[i]) == id) toRemove = s;
            }
        }
        if(toRemove == null) return;
        pairs.remove(toRemove);
        removePairs(id);
    }

    private void addThread(Socket socket)
    {  if (clientCount < clients.length)
    {  System.out.println("Client accepted: " + socket);
        clients[clientCount] = new ChatServerThread(this, socket);
        try
        {  clients[clientCount].open();
            clients[clientCount].start();
            clientCount++; }
        catch(IOException ioe)
        {  System.out.println("Error opening thread: " + ioe); } }
    else
        System.out.println("Client refused: maximum " + clients.length + " reached.");
    }
    public static void main(String args[]) {
        ChatServer server = null;
        new ChatServer(Integer.parseInt("8888"));
    }

}
