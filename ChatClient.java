package JavaClient;

import static JavaClient.Client.buttonFrame;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import org.apache.commons.codec.binary.Base64;

public class Client {

    //Server Info
    static int PORT = 5000;
    static String IP = "10.10.108.200";

    //Account Information
    static String username;
    static String password;
    static String privateKey;
    static String serverKey;
    static boolean isNewUser = false;
    
    //Encryption/Decryption variables
    public static String randomKey = "4nV8cc9OM63Y0c4e";
    private static Cipher ecipher;
    private static Cipher dcipher;

    //Current Group
    static String group = "home";

    //Login Window
    static JFrame login_frame = new JFrame("Welcome to WeTalk");
    static JFrame login_fail = new JFrame("Error");
    static JPanel login_fail_buttons = new JPanel();
    static JButton login_fail_close = new JButton("Close");
    static JTextField login_fail_username = new JTextField("Invalid Login. Username can be no larger than 16 characters. Or the username is taken, or password is invalid");
    static JPanel login_buttons = new JPanel();
    static JButton login_button = new JButton("Login");
    static JButton register_button = new JButton("Sign Up");
    static JTextField login_username = new JTextField(10);
    static JTextField login_password = new JTextField(10);

    //Signup Window
    static JFrame signup_frame = new JFrame("Create New Account");
    static JPanel signup_buttons = new JPanel();
    static JButton signup_back_button = new JButton("Back");
    static JButton signup_confirm_button = new JButton("Confirm");
    static JTextField signup_username = new JTextField(10);
    static JTextField signup_password = new JTextField(10);

    //Group Windows
    static JFrame group_frame = new JFrame("Group Chat");
    static JPanel group_buttons = new JPanel();
    static JButton group_switch_button = new JButton("switch");
    static JTextField group_message = new JTextField(10);
    static JTextArea group_messageArea = new JTextArea(20, 20);
    static JTextArea group_users = new JTextArea(10, 10);

    //Main Chat Window
    static JFrame frame = new JFrame("WeTalk");
    final static JTextArea friends = new JTextArea(20, 10);
    static JPanel buttonFrame = new JPanel();
    static JPanel buttonFrame_2 = new JPanel();
    static JButton privateMessage = new JButton("Message");
    static JButton signOut = new JButton("Log Out");
    static JButton add_friend = new JButton("Add");
    static JButton delete_friend = new JButton("Remove");
    
    static JScrollPane jp = new JScrollPane(friends);

    //File Chooser
    static JFileChooser fc = new JFileChooser();

    //Messaging
    static BufferedReader in;
    static PrintWriter out;

    //List containers
    private static final List<privateChat> privateChats = new ArrayList<>();
    private static final List<String> usersOnline = new ArrayList<>();
    private static final List<String> friendsList = new ArrayList<>();
    
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
    
    public static String encodeImage(byte[] imageByteArray) {
        return Base64.encodeBase64URLSafeString(imageByteArray);
    }
     
    public static byte[] decodeImage(String imageDataString) {
        return Base64.decodeBase64(imageDataString);
    }

    //Resize an Image
    public static BufferedImage resize(BufferedImage img, int newW, int newH) {
        int w = img.getWidth();
        int h = img.getHeight();
        BufferedImage dimg = new BufferedImage(newW, newH, img.getType());
        Graphics2D g = dimg.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(img, 0, 0, newW, newH, 0, 0, w, h, null);
        g.dispose();
        return dimg;
    }

    //Add padding to a string to make length sixteen
    public static String addPadding(String word) {
        String padded = word;
        for (int i = 0; i < 16 - word.length(); i++) {
            padded = padded + " ";
        }
        return padded;
    }

    //Update group's online user list
    public static void updateUsers() {
        group_users.setText("");
        for (String user : usersOnline) {
            group_users.append(user + "\n");
        }
    }

    //Change current chat group
    public static void changeGroup(String group_name) {
        group = group_name;
        group_messageArea.setText("");
    }

    //Private Chat Class
    public static class privateChat {
        String reciever;
        JFrame frame;
        JFrame picture_frame;
        JTextField textField;
        JTextArea messageArea;
        JPanel buttonFrame;
        JPanel picture_panel;
        JButton close_frame;
        JButton view_picture;
        JButton send_picture;
        DefaultListModel listModel;
        int numImages;
    }

    //Create New Private Chat
    public static void newchat(String send_to) {
        final privateChat chat = new privateChat();
        chat.reciever = send_to;

        chat.frame = new JFrame(send_to);
        chat.picture_frame = new JFrame("Image Share");
        chat.textField = new JTextField(40);
        chat.messageArea = new JTextArea(20, 20);
        chat.buttonFrame = new JPanel();
        chat.picture_panel = new JPanel();
        chat.close_frame = new JButton("Close");
        chat.view_picture = new JButton("Image(0)");
        chat.send_picture = new JButton("Send Image");
        
        chat.listModel = new DefaultListModel();
        chat.numImages = 0;

        chat.textField.setEditable(true);
        chat.messageArea.setEditable(false);
        chat.frame.getContentPane().add(chat.textField, "South");
        chat.frame.getContentPane().add(new JScrollPane(chat.messageArea), "Center");
        chat.frame.getContentPane().add(chat.buttonFrame, "North");
        chat.picture_frame.getContentPane().add(chat.picture_panel);

        chat.buttonFrame.add(chat.close_frame);
        chat.buttonFrame.add(chat.view_picture);
        chat.buttonFrame.add(chat.send_picture);
        
        chat.picture_frame.getContentPane().setPreferredSize(new Dimension(420, 300));
        
        chat.picture_frame.setMaximumSize(new Dimension(420, Integer.MAX_VALUE));  
        chat.picture_frame.setMinimumSize(new Dimension(420, 300));
        
        chat.frame.pack();
        chat.picture_frame.pack();

        chat.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        chat.frame.setVisible(true);
        chat.picture_frame.setVisible(false);

        chat.textField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String full_message = "24" + addPadding(username) + addPadding(chat.reciever) + username + ": " + chat.textField.getText() + "\n";
                try {
                    out.println(Encrypt(full_message, serverKey));
                } catch (Exception ex) {}
                chat.messageArea.append(username + ": " + chat.textField.getText() + "\n");
                chat.textField.setText("");
            }
        });

        chat.view_picture.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chat.picture_frame.setVisible(true);
                chat.view_picture.setText("Image(0)");
            }
        });

        chat.send_picture.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int returnVal = fc.showOpenDialog( frame );
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    try {
                        File file = fc.getSelectedFile();
                        FileInputStream imageInFile = new FileInputStream(file);
                        byte imageData[] = new byte[(int) file.length()];
                        imageInFile.read(imageData);
                        String imageDataString = encodeImage(imageData);
                        String full_message = "26" + addPadding(username) + addPadding(chat.reciever) + imageDataString;
                        try {
                            out.println(Encrypt(full_message, serverKey));
                        } catch (Exception ex) {}
                        chat.view_picture.setText("Image(1)");
                        byte[] imageByteArray = decodeImage(imageDataString);
                        try (InputStream ins = new ByteArrayInputStream(imageByteArray)) {
                            BufferedImage imBuff = ImageIO.read(ins);
                            imBuff = resize(imBuff, 400, 200);
                            chat.picture_panel.add (new JLabel (new ImageIcon (imBuff)));
                            ins.close();
                        }
                    } catch (IOException ex) {} 
                }
            }
        });

        chat.close_frame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chat.frame.setVisible(false);
            }
        });

        privateChats.add(chat);
    }

    //Returns true and opens private chat if already started, otherwise creates a new private chat
    public static boolean reopenChat(String recv) {
        for (privateChat chat : privateChats) {
            if (chat.reciever.equals(recv)) {
                chat.frame.setVisible(true);
                return true;
            }
        }
        return false;
    }

    //Initialize main chat GUI
    public static void initializeFrame(final Socket socket) {
        group_message.setEditable(true);
        group_messageArea.setEditable(false);
        group_users.setEditable(false);
        friends.setEditable(false);

        group_frame.getContentPane().add(group_message, "South");
        group_frame.getContentPane().add(group_users, "West");
        group_frame.getContentPane().add(new JScrollPane(group_messageArea), "Center");
        group_frame.getContentPane().add(group_buttons, "North");

        frame.getContentPane().add(friends, "Center");
        frame.getContentPane().add(buttonFrame, "South");
        frame.getContentPane().add(buttonFrame_2, "North");
        frame.add(jp, "East");

        buttonFrame.setLayout(new GridBagLayout());
        group_buttons.add(group_switch_button);
        buttonFrame.add(add_friend);
        buttonFrame.add(delete_friend);
        buttonFrame_2.add(privateMessage);
        buttonFrame_2.add(signOut);

        frame.pack();
        group_frame.pack();
        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        group_frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        frame.setVisible(true);
        group_frame.setVisible(true);

        privateMessage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String user = JOptionPane.showInputDialog(frame, "enter username of person you want to chat", "Private Message", JOptionPane.QUESTION_MESSAGE);
                String full_message = "22" + addPadding(username) + addPadding(user);
                try {
                    out.println(Encrypt(full_message, serverKey));
                } catch (Exception ex) {}
                if (!reopenChat(user)) {
                    newchat(user);
                }
            }
        });
        
        add_friend.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String user = JOptionPane.showInputDialog(frame, "enter username of person you want to chat", "Private Message", JOptionPane.QUESTION_MESSAGE);
                friendsList.add(user);
                friends.setText("");
                for (String users : friendsList) {
                    if (usersOnline.contains(users))
                        friends.append(users + "(Online)" + "\n");
                    else
                        friends.append(users + "(Offline)" + "\n");
                }
            }
        });
        
        delete_friend.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String user = JOptionPane.showInputDialog(frame, "enter username of person you want to chat", "Private Message", JOptionPane.QUESTION_MESSAGE);
                friendsList.remove(user);
                friends.setText("");
                for (String users : friendsList) {
                    if (usersOnline.contains(users))
                        friends.append(users + "(Online)" + "\n");
                    else
                        friends.append(users + "(Offline)" + "\n");
                }
            }
        });
        
        signOut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FileWriter f0 = null;
                try {
                    PrintWriter writer = null;
                    try {
                        File file = new File(username + "friends.txt");
                        writer = new PrintWriter(file);
                        writer.print(""); 
                        writer.close();
                    } catch (FileNotFoundException ex) {}
                    finally {
                        writer.close();
                    }
                    f0 = new FileWriter(username + "friends.txt", true);
                    for (String friend:friendsList){
                        f0.write(friend);
                        f0.write("\r\n");
                    }
                    f0.close();
                } catch (IOException ex) {} 
                finally {
                    try {
                        f0.close();
                    } catch (IOException ex) {}
                }
                String signout = "12" + addPadding(username);
                try {
                    out.println(Encrypt(signout, serverKey));
                } catch (Exception ex) {}
                System.exit(0);
            }
        });
        
        group_message.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String full_message = "20" + addPadding(group) + username + ": " + group_message.getText() + "\n";
                try {
                    out.println(Encrypt(full_message, serverKey));
                } catch (Exception ex) {}
                group_message.setText("");
            }
        });
    }

    //Initialize login GUI
    public static void initializeLogin() {
        login_fail_username.setEditable(false);
        login_username.setEditable(true);
        login_password.setEditable(true);
        login_frame.getContentPane().add(login_buttons, "North");
        login_frame.getContentPane().add(login_username, "West");
        login_frame.getContentPane().add(login_password, "East");
        login_fail.getContentPane().add(login_fail_username, "North");
        login_fail.getContentPane().add(login_fail_buttons, "South");

        login_buttons.add(login_button);
        login_buttons.add(register_button);
        login_fail_buttons.add(login_fail_close);

        login_frame.pack();
        login_fail.pack();
        login_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        login_frame.setVisible(true);
        login_fail.setVisible(false);

        login_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                username = login_username.getText();
                password = login_password.getText();
                if (username.length() > 16)
                    login_fail.setVisible(true);
                else {
                    String full_message = "00" + addPadding(username) + addPadding(password);
                    try {
                        out.println(Encrypt(full_message, serverKey));
                    } catch (Exception ex) {}
                }
                login_username.setText("");
                login_password.setText("");
            }
        });

        register_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                username = login_username.getText();
                password = login_password.getText();
                if (username.length() > 16)
                    login_fail.setVisible(true);
                else {
                    String full_message = "02" + addPadding(username) + addPadding(password);
                    try {
                        out.println(Encrypt(full_message, serverKey));
                    } catch (Exception ex) {}
                    isNewUser = true;
                }
                login_username.setText("");
                login_password.setText("");
            }
        });
        
        login_fail_close.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                login_fail.setVisible(false);
            }
        });

    }

    public static void run(Socket socket) throws Exception {
        //Initialize reader, writer
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);

        privateKey = Decrypt(in.readLine(), randomKey);
        serverKey = Decrypt(in.readLine(), randomKey);

        //Initialize authentication
        initializeLogin();
        boolean valid_login = false;

        //Require user to login to continue
        while (valid_login == false) {
            String choice = Decrypt(in.readLine(), privateKey);
            String user = choice.substring(2, 18);
            String valid = choice.substring(18);
            if (choice.startsWith("00")) {
                if (username.equals(user.trim()) && valid.trim().equals("true")) {
                    valid_login = true;
                    login_frame.setVisible(false);
                    initializeFrame(socket);
                } else {
                    login_fail.setVisible(true);
                }
            }
        }
        
        File newFile;
        if (isNewUser == true)
            newFile = new File(username + "friends.txt");
        else {
            try (BufferedReader br = new BufferedReader(new FileReader(username + "friends.txt"))) {
                String person;
                while ((person = br.readLine()) != null) {
                    friendsList.add(person);
                }
            }
        }

        //Handle messages received by client
        while (true) {
            String line = Decrypt(in.readLine(), privateKey);

            //New user is online
            if (line.startsWith("10")) {
                String online_user = line.substring(2, 18);
                if (!usersOnline.contains(online_user.trim())) {
                    usersOnline.add(online_user.trim());
                    updateUsers();
                    for (String users : friendsList) {
                        if (usersOnline.contains(users))
                            friends.append(users + "(Online)" + "\n");
                        else
                            friends.append(users + "(Offline)" + "\n");
                    }
                }
            }
            
            //New user is online
            if (line.startsWith("12")) {
                String online_user = line.substring(2, 18);
                usersOnline.remove(online_user.trim());
                updateUsers();
                friends.setText("");
                for (String users : friendsList) {
                    if (usersOnline.contains(users))
                        friends.append(users + "(Online)" + "\n");
                    else
                        friends.append(users + "(Offline)" + "\n");
                }
            }

            //Receive group message
            if (line.startsWith("20")) {
                String recv_group = line.substring(2, 18);
                String full_group_message = line.substring(18);
                if (group.equals(recv_group.trim())) {
                    group_messageArea.append(full_group_message + "\n");
                }
            }

            //Request for a private message
            if (line.startsWith("22")) {
                String sender_name = line.substring(2, 18);
                String reciever_name = line.substring(18);
                if (username.equals(reciever_name.trim())) {
                    if (!reopenChat(sender_name.trim())) {
                        newchat(sender_name.trim());
                    }
                }
            }

            //Receive a private message
            if (line.startsWith("24")) {
                String sender_name = line.substring(2, 18);
                String reciever_name = line.substring(18, 34);
                String full_message = line.substring(34);
                if (username.equals(reciever_name.trim())) {
                    for (privateChat chat : privateChats) {
                        if (chat.reciever.equals(sender_name.trim())) {
                            chat.messageArea.append(full_message + "\n");
                        }
                    }
                }
            }
            
            //Receive a private image
            if (line.startsWith("26")) {
                String sender_name = line.substring(2, 18);
                String reciever_name = line.substring(18, 34);
                String full_image = line.substring(34);
                byte[] imageByteArray = decodeImage(full_image);
                if (username.equals(reciever_name.trim())) {
                    for (privateChat chat : privateChats) {
                        if (chat.reciever.equals(sender_name.trim())) {
                            chat.view_picture.setText("Image(1)");
                            try (InputStream ins = new ByteArrayInputStream(imageByteArray)) {
                                BufferedImage imBuff = ImageIO.read(ins);
                                imBuff = resize(imBuff, 400, 200);
                                chat.listModel.add(chat.numImages++, new ImageIcon(imBuff));
                                JList lsm=new JList(chat.listModel);
                                lsm.setVisibleRowCount(1);
                                chat.picture_frame.add(new JScrollPane(lsm));
                                ins.close();
                            }
                        }
                    }
                }
            }
        }
    }

    //Main client function
    public static void main(String args[]) throws Exception {
        final Socket socket = new Socket(IP, PORT);
        run(socket);
    }
}
