package serve;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Serve {
    
    public class personalKey{
        String myName;
        Key myKey;
        public personalKey( String name, Key key){
            this.myName = name;
            this.myKey = key;
        }
    }
    
    private static final HashSet<personalKey> listKeys = new HashSet<>();

    public class clientUser{
        String name;
        PrintWriter writer;
        DataOutputStream dosStream;
        String myStatus;
        public clientUser(String username, PrintWriter pwriter, DataOutputStream dstream, String status){
            name = username;
            writer = pwriter;
            dosStream = dstream;
            myStatus = status;
        }
    }
    

    private static final int PORT = 5000;

    private static final HashSet<String> names = new HashSet<>();
    private static final List<clientUser> clientList = new ArrayList<>();
    private static final HashSet<PrintWriter> writers = new HashSet<>();
    private static final HashSet<DataOutputStream> imageWriters = new HashSet<>();
    private static final HashSet<Key> keys = new HashSet<>();
    
    private static Cipher ecipher;
    private static Cipher dcipher;
    
    public static String keyString = "770A8A65DA156D24";

    final PrintWriter out = null;
    final DataOutputStream dout = null;
    static JFrame frame = new JFrame("Server");
    final static JTextField textField = new JTextField(40);
    static JTextArea messageArea = new JTextArea(8, 40);    
    static JButton kickUser = new JButton("Kick User");
    static JButton flushOutputs = new JButton("Flush");
    static String kicker;
    
   public static String tempKeySeed = "BT0N35Q5DA16YD84";
    
    public static void main(String[] args) throws Exception {
        System.out.println("The chat server is running.");
        ServerSocket listener = new ServerSocket(PORT);
        BufferedReader in;
    	
        textField.setEditable(true);
        messageArea.setEditable(false);
        frame.getContentPane().add(textField, "North");
        frame.getContentPane().add(new JScrollPane(messageArea), "Center");
        frame.getContentPane().add(kickUser, "South");
        frame.getContentPane().add(flushOutputs, "East");
        frame.pack();
        
        final SecretKeySpec keySpec1 = new SecretKeySpec(keyString.getBytes("UTF-8"), "AES");
        final Cipher cipher1 = Cipher.getInstance("AES");
        cipher1.init(Cipher.ENCRYPT_MODE, keySpec1);

        // Add Listeners
        textField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String serverMessage = textField.getText();
                    serverMessage = "SERVER: " + serverMessage;
                    byte[] encryptedTextBytes = cipher1.doFinal(serverMessage.getBytes("UTF-8"));
                    String msg = com.sun.org.apache.xerces.internal.impl.dv.util.Base64.encode(encryptedTextBytes);
                    for (PrintWriter writer : writers) {
                        writer.println(msg);
                    }
                    messageArea.append("Message from Server: " + serverMessage + "\n");
                    textField.setText("");
                } catch (        IllegalBlockSizeException | BadPaddingException | UnsupportedEncodingException ex) {
                    Logger.getLogger(Serve.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        });
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        
        messageArea.append("The Chat Server is Running" + "\n");
        InetAddress ip;
        ip = InetAddress.getLocalHost();
        String address = ip.getHostAddress();
        messageArea.append("IP Address: " + address + "\n");

        kickUser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    kicker = JOptionPane.showInputDialog(frame, "Enter User to Kick", "Moderation", JOptionPane.QUESTION_MESSAGE);
                    String kick = "KICK" + kicker;
                    messageArea.append("Kicked user: " + kicker + "\n");
                    names.remove(kicker);
                    byte[] encryptedTextBytes = cipher1.doFinal(kick.getBytes("UTF-8"));
                    String msg = com.sun.org.apache.xerces.internal.impl.dv.util.Base64.encode(encryptedTextBytes);
                    
                    String text = "DELETEUSER" + kicker;
                    byte[] encryptedTextBytes2 = cipher1.doFinal(kick.getBytes("UTF-8"));
                    String msg2 = com.sun.org.apache.xerces.internal.impl.dv.util.Base64.encode(encryptedTextBytes2);
                    
                    for (PrintWriter writer : writers){
                        writer.println(msg);
                        writer.println(msg2);
                    }
                } catch (        IllegalBlockSizeException | BadPaddingException | UnsupportedEncodingException ex) {
                    Logger.getLogger(Serve.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        flushOutputs.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                    for (PrintWriter writer : writers){
                        writer.flush();
                    }
                    for (DataOutputStream stream : imageWriters){
                        try {
                            stream.flush();
                        } catch (IOException ex) {
                            Logger.getLogger(Serve.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
            }
        });
        
        try {
            while (true) {
                new Handler(listener.accept()).start();
                System.out.println(names);
            }
        } finally {
            listener.close();
        }
    }

    private static class Handler extends Thread {
        private String name;
        private final Socket socket;
        private BufferedReader in;
        private PrintWriter out;
        private DataOutputStream dout;
        private String pass;
        private String status;
        private String myKey;
        private String mySecondKey;
        private DataInputStream dis;
        
        public static String generateString(Random rng, String characters, int length)
        {
            char[] text = new char[length];
            for (int i = 0; i < length; i++)
            {
                text[i] = characters.charAt(rng.nextInt(characters.length()));
            }
            String thisString = new String(text);
            tempKeySeed = thisString;
            return tempKeySeed;
        }

        public Handler(Socket socket) {
            this.socket = socket;
        }

        public boolean login(String user, String pass) throws FileNotFoundException{
            try (BufferedReader br = new BufferedReader(new FileReader("logins.txt"))) {
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
            }
            catch (IOException e){}
            return false;
        }
        
        public boolean signup(String user, String pass) throws FileNotFoundException{
            try (BufferedReader br = new BufferedReader(new FileReader("logins.txt"))) {
                String line;
                while ((line = br.readLine()) != null) {
                    if ( line.equals(user) )
                        return false;
                }
                br.close();
            }
            catch (IOException e){}
            try (FileWriter f0 = new FileWriter("logins.txt", true)) {
                f0.write("\r\n");
                f0.write(user);
                f0.write("\r\n");
                f0.write(pass);
                f0.close();
                out.println("NAMEACCEPTED");
                return true;
            }
            catch (IOException e){}
            return true;
        }
        
    private static String Decrypt(String encryptedText, String key) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException  {
     
        SecretKeySpec keySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

        // Instantiate the cipher
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, keySpec);
        byte[] encryptedTextBytes = com.sun.org.apache.xerces.internal.impl.dv.util.Base64.decode(encryptedText);
        byte[] decryptedTextBytes = cipher.doFinal(encryptedTextBytes);

        return new String(decryptedTextBytes);
    }
    
    private static String Encrypt(String plainText, String key) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
        SecretKeySpec keySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

        // Instantiate the cipher
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);

        byte[] encryptedTextBytes = cipher.doFinal(plainText.getBytes("UTF-8"));
        return com.sun.org.apache.xerces.internal.impl.dv.util.Base64.encode(encryptedTextBytes);
    }
        
        @Override
        public void run() {
            try {

                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);
                dout = new DataOutputStream(socket.getOutputStream());
                dis = new DataInputStream(socket.getInputStream());
                
                OUTER:                
                while (true) {
                    String cmd = Encrypt("SUBMITNAME", keyString);
                    out.println(cmd);
                    String choice = in.readLine();
                    choice = Decrypt(choice, keyString);
                    switch (choice) {
                        case "LOGIN":
                            name = in.readLine();
                            name = Decrypt(name, keyString);
                            pass = in.readLine();
                            pass = Decrypt(pass, keyString);
                            if (login(name, pass) == true) {
                                String cmd2 = Encrypt("NAMEACCEPTED", keyString);
                                out.println(cmd2);
                                messageArea.append("User " + name + " successfully logged in \n");
                                synchronized (names) {
                                    if (!names.contains(name)) {
                                        names.add(name);
                                        break OUTER;
                                    }
                                }
                            } else if ( login(name, pass) == false){
                                out.println(cmd);
                                messageArea.append("User " + name + " failed to logged in \n");
                            }   break;
                        case "SIGNUP":
                            name = in.readLine();
                            name = Decrypt(name, keyString);
                            pass = in.readLine();
                            pass = Decrypt(pass, keyString);
                            if (signup(name, pass) == true) {
                                String cmd2 = Encrypt("NAMEACCEPTED", keyString);
                                out.println(cmd2);
                                messageArea.append("User " + name + " successfully registered \n");
                                synchronized (names) {
                                    if (!names.contains(name)) {
                                        names.add(name);
                                        break OUTER;
                                    }
                                }
                            } else if ( signup(name, pass) == false){
                                messageArea.append("User " + name + " is already assigned \n");
                                out.println(cmd);
                            }   break;
                    }
                    if (name == null) {
                        return;
                    }
                }
                
                writers.add(out);
                imageWriters.add(dout);
                
                String seed = generateString(new Random(), tempKeySeed, 16);
                myKey = seed;
                for (PrintWriter writer : writers) {
                    String msg = name + seed;
                    msg = Encrypt(msg, keyString);
                    writer.println(msg);
                }
                
                
                messageArea.append("New client connected: " + name + "\n");
                
                for (PrintWriter writer : writers) {
                    for (String s : names) {
                        System.out.println(s);
                        String msg = "UPDATEUSER" + s;
                        msg = Encrypt(msg, keyString);
                    	writer.println(msg);
                    }
                }
                
                while (true) {
                    String input = in.readLine();
                    String decryptedText = Decrypt(input, myKey);
                    
                    if (decryptedText == null) {
                    }
                    else if (decryptedText.startsWith("DISCONNECT")){
                        names.remove(decryptedText.substring(10));
                        messageArea.append(decryptedText.substring(10) + " has disconnected \n");
                        String cmd = "DELETEUSER" + decryptedText.substring(10);
                        cmd = Encrypt(cmd, keyString);
                        if (out != null) {
                            writers.remove(out);
                        }
                        for (PrintWriter writer : writers) {
                            writer.println(cmd);
                        }    
                        socket.close();                        
                    }
                    else if (decryptedText.equals("IMAGE")){
                        int len = dis.readInt();
                        byte[] data = new byte[len];
                        
                        if (len > 0) {
                            dis.readFully(data);
                        }
                        
                        String cmd = Encrypt("IMAGE", keyString);
                        for (PrintWriter writer : writers) {
                            writer.println(cmd);
                        }
                        
                        messageArea.append(name + " posted an image" + "\n");
                        for (DataOutputStream stream : imageWriters) {
                            //stream.writeInt(data.length);
                            if (data.length > 0) {
                                stream.writeInt(data.length);
                                stream.write(data, 0, data.length);
                            }
                            stream.flush();
                        }
                        
                        data = null;
                    }
                    else if (decryptedText.startsWith("PM")){
                    	String you = decryptedText.substring(2);
                        String message = in.readLine();
                        message = Decrypt(message, myKey);
                        messageArea.append("From " + name + " to " + you + ": " + message + "\n");
                        for (PrintWriter writer : writers){
                            String cmd = "PM" + you;
                            String encryptedText = Encrypt(cmd, keyString);
                            writer.println(encryptedText);
                            encryptedText = Encrypt(name, keyString);
                            writer.println(encryptedText);
                            encryptedText = Encrypt(message, keyString);
                            writer.println(encryptedText);
                        }
                    }
                    else if (decryptedText.startsWith("NEWPM")){
                    	String to = decryptedText.substring(5);
                        for (PrintWriter writer : writers){
                            String cmd = "NEWPM" + to;
                            cmd = Encrypt(cmd, keyString);
                            writer.println(cmd);
                            String from = name;
                            from = Encrypt(from, keyString);
                            writer.println(from);
                        }
                    }
                    else if (decryptedText.startsWith("GROUPONE")) {
                        String message = decryptedText.substring(8);
                        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                        Date date = new Date();
                        String reportDate = dateFormat.format(date);
                        String full = ("GROUP" + "ONE" + "( " + reportDate + " ) " + name + ": " + message + "\n");
                        String encryptedText = Encrypt(full, keyString);
                        messageArea.append("Group one: " + "( " + reportDate + " ) " + name + ": " + message + "\n");
                        for (PrintWriter writer : writers) {
                            writer.println(encryptedText);
                        }
                    }
                    else if (decryptedText.startsWith("GROUPTWO")) {
                        String message = decryptedText.substring(8);
                        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                        Date date = new Date();
                        String reportDate = dateFormat.format(date);
                        String full = ("GROUP" + "TWO" + "( " + reportDate + " ) " + name + ": " + message + "\n");
                        String encryptedText = Encrypt(full, keyString);
                        messageArea.append("Group two: " + "( " + reportDate + " ) " + name + ": " + message + "\n");
                        for (PrintWriter writer : writers) {
                            writer.println(encryptedText);
                        }
                    }
                    else if (decryptedText.startsWith("GROUPTHREE")) {
                        String message = decryptedText.substring(10);
                        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                        Date date = new Date();
                        String reportDate = dateFormat.format(date);
                        String full = ("GROUP" + "THREE" + "( " + reportDate + " ) " + name + ": " + message + "\n");
                        String encryptedText = Encrypt(full, keyString);
                        messageArea.append("Group three: " + "( " + reportDate + " ) " + name + ": " + message + "\n");
                        for (PrintWriter writer : writers) {
                            writer.println(encryptedText);
                        }
                    }
                    else if (decryptedText.startsWith("GROUPFOUR")) {
                        String message = decryptedText.substring(9);
                        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                        Date date = new Date();
                        String reportDate = dateFormat.format(date);
                        String full = ("GROUP" + "FOUR" + "( " + reportDate + " ) " + name + ": " + message + "\n");
                        String encryptedText = Encrypt(full, keyString);
                        messageArea.append("Group four: " + "( " + reportDate + " ) " + name + ": " + message + "\n");
                        for (PrintWriter writer : writers) {
                            writer.println(encryptedText);
                        }
                    }
                    else if (decryptedText.startsWith("AWAY")) {
                        String user = decryptedText.substring(4);
                        String encryptedText = "AWAY" + name;
                        status = "AWAY";
                        messageArea.append( name + " is away" + "\n");
                        encryptedText = Encrypt(encryptedText, keyString);
                        for (PrintWriter writer : writers) {
                            writer.println(encryptedText);
                        }
                    }
                    else if (decryptedText.startsWith("BUSY")) {
                        String user = decryptedText.substring(4);
                        String encryptedText = "BUSY" + name;
                        status = "BUSY";
                        messageArea.append( name + " is busy" + "\n");
                        encryptedText = Encrypt(encryptedText, keyString);
                        for (PrintWriter writer : writers) {
                            writer.println(encryptedText);
                        }
                    }
                    else if (decryptedText.startsWith("AVAILABLE")) {
                        String user = decryptedText.substring(9);
                        String encryptedText = "AVAILABLE" + name;
                        status = "AVAILABLE";
                        messageArea.append( name + " is available" + "\n");
                        encryptedText = Encrypt(encryptedText, keyString);
                        for (PrintWriter writer : writers) {
                            writer.println(encryptedText);
                        }
                    }
                }
            } catch (IOException e) {
                System.out.println(e);
            } catch (    NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException ex) {
                Logger.getLogger(Serve.class.getName()).log(Level.SEVERE, null, ex);
            }/* finally {
                if (name != null) {
                    try {
                        names.remove(name);
                        String text = "DELETEUSER" + name;
                        String encryptedText = Encrypt(text, keyString);
                        for (PrintWriter writer : writers) {
                            writer.println(encryptedText);
                        }
                    } catch (            NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException | UnsupportedEncodingException ex) {
                        Logger.getLogger(Serve.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                if (out != null) {
                    writers.remove(out);
                }
                try {
                    socket.close();
                } catch (IOException e) {
                }
            }*/
        }
    }
}
