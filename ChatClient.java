package chatclient;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.HashSet;
import java.util.Iterator;
import javax.swing.JButton;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ChatClient {
    
    int PORT = 5000;
    int privatePORT = 5001;

    BufferedReader in;
    PrintWriter out;
    JFrame frame = new JFrame("LetsTalk");
    JTextField textField = new JTextField(40);
    JTextArea users = new JTextArea(10,10);
    JTextArea messageArea = new JTextArea(8, 40);
    
    JPanel buttonFrame = new JPanel();
    
    JButton privateMessage = new JButton("Message");
    JButton chatRooms = new JButton("Chatrooms");
    JButton disconnect = new JButton("Disconnect");
    JButton liveStream = new JButton("Video");
    JButton uploadimage = new JButton("Upload");
    JButton status = new JButton("Status");
    JButton admin = new JButton("Admin");
    
    private HashSet<String> names = new HashSet<String>();

    public ChatClient() {

        // Layout GUI
        textField.setEditable(false);
        users.setEditable(false);
        messageArea.setEditable(false);
        frame.getContentPane().add(textField, "South");
        frame.getContentPane().add(users, "West");
        frame.getContentPane().add(new JScrollPane(messageArea), "Center");
        
        frame.getContentPane().add(buttonFrame, "North");
        
        buttonFrame.add(status);
        buttonFrame.add(admin);
        buttonFrame.add(privateMessage);
        buttonFrame.add(chatRooms);
        buttonFrame.add(liveStream);
        buttonFrame.add(uploadimage);
        buttonFrame.add(disconnect);
        
        
        frame.pack();
        
        users.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                users.setText("");
            }
        });

        // Add Listeners
        textField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                out.println(textField.getText());
                textField.setText("");
            }
        });
        
        disconnect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                out.println("DISCONNECT" + getName());
            }
        });
        
        privateMessage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String connectTo = JOptionPane.showInputDialog(frame, "Enter user to chat with", "PM", JOptionPane.PLAIN_MESSAGE);
                        JFrame privateFrame = new JFrame( connectTo );
                        JTextField textField2 = new JTextField(40);
                        JTextArea users2 = new JTextArea(10,10);
                        JTextArea messageArea2 = new JTextArea(8, 40);

                        JPanel buttonFrame2 = new JPanel();

                        JButton privateMessage2 = new JButton("Private Message");
                        JButton chatRooms2 = new JButton("Chatrooms");
                        JButton disconnect2 = new JButton("Disconnect");
                        JButton liveStream2 = new JButton("Video Stream");
                        
                        textField2.setEditable(true);
                        users2.setEditable(false);
                        messageArea2.setEditable(false);
                        privateFrame.getContentPane().add(textField2, "North");
                        privateFrame.getContentPane().add(users2, "West");
                        privateFrame.getContentPane().add(new JScrollPane(messageArea2), "Center");

                        privateFrame.getContentPane().add(buttonFrame2, "South");
                        buttonFrame2.add(privateMessage2);
                        buttonFrame2.add(chatRooms2);
                        buttonFrame2.add(disconnect2);
                        buttonFrame2.add(liveStream2);


                        privateFrame.pack();
                        
                        privateFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                        privateFrame.setVisible(true);
            }
        }); 
    }

    private void privateChat(String user_1, String user_2){
        
    }
    
    private void sendPrivateMessage(String user, String message){
        out.println("PM");
        out.println(user);
        out.println(message);
    }
    
    private String getServerAddress() {
        return JOptionPane.showInputDialog(frame, "Enter IP Address of the Server:", "Welcome to the Chatter", JOptionPane.QUESTION_MESSAGE);
    }

    private String getName() {
        return JOptionPane.showInputDialog(frame, "Choose a screen name:", "Screen name selection", JOptionPane.PLAIN_MESSAGE);
    }

    private void run() throws IOException {

        String serverAddress = getServerAddress();
        Socket socket = new Socket(serverAddress, PORT);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);

        while (true) {
            String line = in.readLine();
            if (line.startsWith("SUBMITNAME")) {
                out.println(getName());
            } else if (line.startsWith("NAMEACCEPTED")) {
                textField.setEditable(true);
            } else if (line.startsWith("MESSAGE")) {
                messageArea.append(line.substring(8) + "\n");
            } else if (line.startsWith("RESET")){
                names = null;
                names = new HashSet<String>();
                users.setText("");
            } else if (line.startsWith("UPDATEUSER")) {
                if ( names.contains(line.substring(10)) == false){
                    names.add(line.substring(10));
                }
                users.setText("");
                for (String s : names) {
                    users.append(s + "\n");
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {
        ChatClient client = new ChatClient();
        client.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        client.frame.setVisible(true);
        InetAddress ip;
        ip = InetAddress.getLocalHost();
        String address = ip.getHostAddress();
        client.run();
    }
}