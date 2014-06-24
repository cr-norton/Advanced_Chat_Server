package JavaServer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class Server {
    //Client
    public static class Client {
        String name;
        String clientKey;
        String serverKey;
        PrintWriter clientWriter;
    }
    
    //Create a Client
    public static Client makeClient(String nm, String ck, String sk, PrintWriter cw) {
        Client thisClient = new Client();
        thisClient.name = nm;
        thisClient.clientKey = ck;
        thisClient.serverKey = sk;
        thisClient.clientWriter = cw;
        return thisClient;
    }
   
    //Encryption/Decryption variables
    public static String serverKey = "X9H20m6K7eeBQT65";
    public static String seedKey = "8sY2nQ520Rt2Qm3d";
    
    
    public static String randomKey = "4nV8cc9OM63Y0c4e";
    public static String tempKeySeed = "vP32Qm3U9xY3n1Rr";
    
    //Generate random string
    public static String generateString(Random rng, String characters, int length) {
        char[] text = new char[length];
        for (int i = 0; i < length; i++)
            text[i] = characters.charAt(rng.nextInt(characters.length()));
        String thisString = new String(text);
        return thisString;
    }
 
    private static String Decrypt(String encryptedText, String key) throws Exception {
        SecretKeySpec keySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, keySpec);
        byte[] encryptedTextBytes = com.sun.org.apache.xerces.internal.impl.dv.util.Base64.decode(encryptedText);
        byte[] decryptedTextBytes = cipher.doFinal(encryptedTextBytes);
        return new String(decryptedTextBytes);
    }
    
    private static String Encrypt(String plainText, String key) throws Exception {
        SecretKeySpec keySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);
        byte[] encryptedTextBytes = cipher.doFinal(plainText.getBytes("UTF-8"));
        return com.sun.org.apache.xerces.internal.impl.dv.util.Base64.encode(encryptedTextBytes);
    }
    
    //Server Information
    private static final int PORT = 5000;

    //Server GUI
    static JFrame frame = new JFrame("Server");
    static JTextArea messageArea = new JTextArea(8, 40);
	
    //Writers and Streams
    final PrintWriter out = null;

    //HashSet for usernames, printwriters, outputsreams
    private static final List<String> names = new ArrayList<>();
    private static final List<Client> clients = new ArrayList<>();

    //Add padding to a string to make length sixteen
    public static String addPadding(String word) {
        String padded = word;
        for (int i = 0; i < 16 - word.length(); i++)
            padded = padded + " ";
        return padded;
    }

    //Return true if valid login information, return false otherwise
    public static boolean login(String user, String pass) throws Exception{
        BufferedReader br = new BufferedReader(new FileReader("logins.txt"));
        String line;
        String password;
        while ((line = br.readLine()) != null) {
            if ( line.equals(user) ){
                password = br.readLine();
                if ( password.equals(pass) ) {
                    br.close();
                    return true;
                }
            }
        }
        return false;
    }
	
    //Returns true if username is available, false otherwise
    public static boolean isUsernameAvailable(String user) throws Exception {
        BufferedReader br = new BufferedReader(new FileReader("logins.txt"));
        String line;
        while ((line = br.readLine()) != null) {
            if ( line.equals(user) ){
                br.close();
                return false;
            }
        }
        return true;
    }
	
    //Saves new account to login file
    public static boolean signup(String user, String pass) throws Exception {
        if (isUsernameAvailable(user)) {
            try (FileWriter f0 = new FileWriter("logins.txt", true)) {
                f0.write("\r\n");
                f0.write(user);
                f0.write("\r\n");
                f0.write(pass);
            }
            return true;
        }
        else
            return false;
    }

    //Sends client true if login is valid, false otherwise
    public static void sendLoginValid(PrintWriter writer, String user, String valid, String serverKey) throws Exception {
        String username = addPadding(user);
        String full_message = "00" + username + valid;
        writer.println(Encrypt(full_message, serverKey));
    }

    //Sends broadcast to clients when a new user signs online
    public static void userOnline(PrintWriter writer, String user, String serverKey) throws Exception {
        String username = addPadding(user);
        String full_message = "10" + username;
        writer.println(Encrypt(full_message, serverKey));
    }

    //Handler for each connected client
    public static class Handler extends Thread {
        //Add variables
        private final Socket socket;
        private String name;
        private BufferedReader in;
        private PrintWriter out;
        
        
        private String privateKey;
        private String privateServer;

        //Initialize client socket
        public Handler(Socket socket) {
            this.socket = socket;
        }
        
        @Override
        public void run() {
            try {
                //Generate readers, writers, input streams, output streams
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);
                
                //Generate Key
                tempKeySeed = generateString(new Random(), tempKeySeed, 16);
                privateKey = tempKeySeed;
                
                serverKey = generateString(new Random(), serverKey, 16);
                privateServer = serverKey;
                
                String send_private_key = Encrypt(privateKey, randomKey);
                String send_server_key = Encrypt(privateServer, randomKey);
                
                out.println(send_private_key);
                out.println(send_server_key);
                
                //Handle user login
                while (true) {
                    //Get login information
                    String choice = Decrypt(in.readLine(), serverKey);
                    String user = choice.substring(2, 18);
                    String pass = choice.substring(18);
                    
                    //User wants to login
                    if (choice.startsWith("00")) {
                        if (login(user.trim(), pass.trim())) {
                            sendLoginValid(out, user.trim(), "true", privateKey);
                            messageArea.append("User: " + user.trim() + " successfully logged in\n");
                            name = user;
                            names.add(user);
                            clients.add(makeClient(name, privateKey, serverKey, out));
                            break;
                        }
                        else {
                            sendLoginValid(out, user.trim(), "false", privateKey);
                            messageArea.append("User: " + user.trim() + " failed to login\n");
                        }
                    }
                    //User wants to signup
                    else if (choice.startsWith("02")) {
                        if (signup(user.trim(), pass.trim())) {
                            sendLoginValid(out, user.trim(), "true", privateKey);
                            messageArea.append("User: " + user.trim() + " successfully signed up\n");
                            name = user;
                            names.add(user);
                            clients.add(makeClient(name, privateKey, serverKey, out));
                            break;
                        }
                        else {
                            sendLoginValid(out, user.trim(), "false", privateKey);
                            messageArea.append("User: " + user.trim() + " failed to sign up\n");
                        }
                    }
                }
                
                //Log to server GUI
                messageArea.append("New client connected: " + name + "\n");
                
                //Alert all clients that a user has signed in
                for (Client client : clients){
                    for (String users : names){
                        userOnline(client.clientWriter, users, client.clientKey);
                    }
                }

                //Handle all messages
                while (true) {
                    String input = Decrypt(in.readLine(), privateServer);
                    
                    if (input.startsWith("12")) {
                        names.remove(name);
                        for (Client client : clients) {
                            if (client.name.equals(name))
                                clients.remove(client);
                            client.clientWriter.println(Encrypt(input, client.clientKey));
                        }
                    }
                    
                    //Handle a group message
                    else if (input.startsWith("20")) {
                        String group = input.substring(2, 18);
                        String message = input.substring(18);
                        messageArea.append("In group (" + group.trim() + ") : " + message + "\n");
                        for (Client client : clients)
                            client.clientWriter.println(Encrypt(input, client.clientKey));                 
                    }
                    
                    //Broadcast message
                    else {
                        for (Client client : clients)
                            client.clientWriter.println(Encrypt(input, client.clientKey));  
                    }
                }
            } catch (IOException ex) {} catch (Exception ex) {}
        }
    }
	
    //Main server function
    public static void main(String[] args) throws Exception {
	//Initialize GUI
	messageArea.setEditable(false);
        frame.getContentPane().add(new JScrollPane(messageArea), "Center");
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
	frame.setVisible(true);
        
        final SecretKeySpec keySpec1 = new SecretKeySpec(serverKey.getBytes("UTF-8"), "AES");
        final Cipher cipher1 = Cipher.getInstance("AES");
        cipher1.init(Cipher.ENCRYPT_MODE, keySpec1);
		
        //Set up server
	ServerSocket listener = new ServerSocket(PORT);
        String address = InetAddress.getLocalHost().getHostAddress();
	messageArea.append("The Chat Server is Running at " + address + " on port " + PORT + "\n");

	//Create new thread for each client that connects
        try {
            while (true)
                new Handler(listener.accept()).start();
        }
	finally {
            listener.close();
        }

    }
}
