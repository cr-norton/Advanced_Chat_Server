import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import javax.imageio.ImageIO;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ChatServer {

	public class clientUser{
		String name;
		PrintWriter writer;
		public clientUser(String username, PrintWriter pwriter){
			name = username;
			writer = pwriter;
		}
	}

	private static final int PORT = 5000;

    private static HashSet<String> names = new HashSet<String>();
    List<clientUser> clientList = new ArrayList<clientUser>();

    private static HashSet<PrintWriter> writers = new HashSet<PrintWriter>();

    
    public static void main(String[] args) throws Exception {
        System.out.println("The chat server is running.");
        ServerSocket listener = new ServerSocket(PORT);
        
    	/*Setup UI*/
        BufferedReader in;
        final PrintWriter out = null;
        JFrame frame = new JFrame("Server");
        final JTextField textField = new JTextField(40);
        JTextArea messageArea = new JTextArea(8, 40);
        
        textField.setEditable(false);
        messageArea.setEditable(false);
        frame.getContentPane().add(textField, "North");
        frame.getContentPane().add(new JScrollPane(messageArea), "Center");
        frame.pack();

        // Add Listeners
        textField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                out.println(textField.getText());
                textField.setText("");
            }
        });
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        
        messageArea.append("The Chat Server is Running" + "\n");
        InetAddress ip;
        ip = InetAddress.getLocalHost();
        String address = ip.getHostAddress();
        messageArea.append("IP Address: " + address + "\n");
        
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
        private Socket socket;
        private BufferedReader in;
        private PrintWriter out;

        public Handler(Socket socket) {
            this.socket = socket;
        }

                private String encrypt_1(String message){
        String encryption = "";
        for (int i = 0; i < message.length(); i++) {   
            String letter = Character.toString(message.charAt(i));
         
            if ( letter.equals("a") | letter.equals("b") | letter.equals("c") | letter.equals("d") | letter.equals("A") | letter.equals("B") | letter.equals("C") | letter.equals("D")) {                
                encryption = encryption + "0000";
                if ( letter.equals("a") ){
                    encryption = encryption + "0000";
                }
                else if ( letter.equals("b") ){
                    encryption = encryption + "0001";
                }
                else if ( letter.equals("c") ){
                    encryption = encryption + "0010";
                }
                else if ( letter.equals("d") ){
                    encryption = encryption + "0011";
                }
                else if ( letter.equals("A") ){
                    encryption = encryption + "0100";
                }
                else if ( letter.equals("B") ){
                    encryption = encryption + "0101";
                }
                else if ( letter.equals("C") ){
                    encryption = encryption + "0110";
                }
                else if ( letter.equals("D") ){
                    encryption = encryption + "0111";
                }
            }
            else if ( letter.equals("e") | letter.equals("f") | letter.equals("g") | letter.equals("h") | letter.equals("E") | letter.equals("F") | letter.equals("G") | letter.equals("H") ) {                
                encryption = encryption + "0001";
                if ( letter.equals("e") ){
                    encryption = encryption + "0000";
                }
                else if ( letter.equals("f") ){
                    encryption = encryption + "0001";
                }
                else if ( letter.equals("g") ){
                    encryption = encryption + "0010";
                }
                else if ( letter.equals("h") ){
                    encryption = encryption + "0011";
                }
                else if ( letter.equals("E") ){
                    encryption = encryption + "0100";
                }
                else if ( letter.equals("F") ){
                    encryption = encryption + "0101";
                }
                else if ( letter.equals("G") ){
                    encryption = encryption + "0110";
                }
                else if ( letter.equals("H") ){
                    encryption = encryption + "0111";
                }
            }
            else if ( letter.equals("i") | letter.equals("j") | letter.equals("k") | letter.equals("l") | letter.equals("I") | letter.equals("J") | letter.equals("K") | letter.equals("L") ) {                
                encryption = encryption + "0010";
                if ( letter.equals("i") ){
                    encryption = encryption + "0000";
                }
                else if ( letter.equals("j") ){
                    encryption = encryption + "0001";
                }
                else if ( letter.equals("k") ){
                    encryption = encryption + "0010";
                }
                else if ( letter.equals("l") ){
                    encryption = encryption + "0011";
                }
                else if ( letter.equals("I") ){
                    encryption = encryption + "0100";
                }
                else if ( letter.equals("J") ){
                    encryption = encryption + "0101";
                }
                else if ( letter.equals("K") ){
                    encryption = encryption + "0110";
                }
                else if ( letter.equals("L") ){
                    encryption = encryption + "0111";
                }
            }
            else if ( letter.equals("m") | letter.equals("n") | letter.equals("o") | letter.equals("p") | letter.equals("M") | letter.equals("N") | letter.equals("O") | letter.equals("P") ) {                
                encryption = encryption + "0011";
                if ( letter.equals("m") ){
                    encryption = encryption + "0000";
                }
                else if ( letter.equals("n") ){
                    encryption = encryption + "0001";
                }
                else if ( letter.equals("o") ){
                    encryption = encryption + "0010";
                }
                else if ( letter.equals("p") ){
                    encryption = encryption + "0011";
                }
                else if ( letter.equals("M") ){
                    encryption = encryption + "0100";
                }
                else if ( letter.equals("N") ){
                    encryption = encryption + "0101";
                }
                else if ( letter.equals("O") ){
                    encryption = encryption + "0110";
                }
                else if ( letter.equals("P") ){
                    encryption = encryption + "0111";
                }
            }
            else if ( letter.equals("q") | letter.equals("r") | letter.equals("s") | letter.equals("t") | letter.equals("Q") | letter.equals("R") | letter.equals("S") | letter.equals("T") ) {                
                encryption = encryption + "0100";
                if ( letter.equals("q") ){
                    encryption = encryption + "0000";
                }
                else if ( letter.equals("r") ){
                    encryption = encryption + "0001";
                }
                else if ( letter.equals("s") ){
                    encryption = encryption + "0010";
                }
                else if ( letter.equals("t") ){
                    encryption = encryption + "0011";
                }
                else if ( letter.equals("Q") ){
                    encryption = encryption + "0100";
                }
                else if ( letter.equals("R") ){
                    encryption = encryption + "0101";
                }
                else if ( letter.equals("S") ){
                    encryption = encryption + "0110";
                }
                else if ( letter.equals("T") ){
                    encryption = encryption + "0111";
                }
            }
            else if ( letter.equals("u") | letter.equals("v") | letter.equals("w") | letter.equals("x") | letter.equals("U") | letter.equals("V") | letter.equals("W") | letter.equals("X") ) {                
                encryption = encryption + "0101";
                if ( letter.equals("u") ){
                    encryption = encryption + "0000";
                }
                else if ( letter.equals("v") ){
                    encryption = encryption + "0001";
                }
                else if ( letter.equals("w") ){
                    encryption = encryption + "0010";
                }
                else if ( letter.equals("x") ){
                    encryption = encryption + "0011";
                }
                else if ( letter.equals("U") ){
                    encryption = encryption + "0100";
                }
                else if ( letter.equals("V") ){
                    encryption = encryption + "0101";
                }
                else if ( letter.equals("W") ){
                    encryption = encryption + "0110";
                }
                else if ( letter.equals("X") ){
                    encryption = encryption + "0111";
                }
            }
            else if ( letter.equals("y") | letter.equals("z") | letter.equals("Y") | letter.equals("Z") ) {                
                encryption = encryption + "0110";
                if ( letter.equals("y") ){
                    encryption = encryption + "0000";
                }
                else if ( letter.equals("z") ){
                    encryption = encryption + "0001";
                }
                else if ( letter.equals("Y") ){
                    encryption = encryption + "0010";
                }
                else if ( letter.equals("Z") ){
                    encryption = encryption + "0011";
                }
            }
            else if ( letter.equals(" ") ){
                encryption = encryption + "11111111";
            }
            else if ( letter.equals(":") ){
                encryption = encryption + "11100111";
            }
        }        
        return encryption;
    }
    
        private String encrypt_2(String message){
        
        String temp = new StringBuilder(message).reverse().toString();
        String encryption = "";
        
        for (int i = 0; i < temp.length(); i++){
            String Encrypt_i = Character.toString(temp.charAt(i));
            if ( Encrypt_i.equals("1")){
                encryption = encryption + "0";
            }
            else {
                encryption = encryption + "1";
            }
        }
        
        return encryption;
    }
        
        
        public void run() {
            try {

                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);
                
                while (true) {
                    out.println("SUBMITNAME");
                    name = in.readLine();
                    if (name == null) {
                        return;
                    }
                    synchronized (names) {
                        if (!names.contains(name)) {
                            names.add(name);
                            break;
                         }
                    }
                }

                out.println("NAMEACCEPTED");
                writers.add(out);
                System.out.println("New client connected: " + name);
                
                for (PrintWriter writer : writers) {
                    for (String s : names) {
                    	writer.println("UPDATEUSER " + s);
                    }
                }
                
                System.out.println(names);
                System.out.println(writers);
                
                while (true) {
                    String input = in.readLine();
                    if (input == null) {
                        return;
                    }
                    else if (input.startsWith("IMAGE")){
                        InputStream in = socket.getInputStream();
                        DataInputStream dis = new DataInputStream(in);

                        int len = dis.readInt();
                        byte[] data = new byte[len];
                        if (len > 0) {
                            dis.readFully(data);
                        }
                        for (PrintWriter writer : writers) {
                            writer.println("IMAGE");
                            OutputStream out = socket.getOutputStream(); 
                            DataOutputStream dos = new DataOutputStream(out);

                            dos.writeInt(data.length);
                            if (data.length > 0) {
                                dos.write(data, 0, data.length);
                            }
                            System.out.println(data);
                        }
                    }
                    else if (input.startsWith("DISCONNECT")){
                    	String temp = input.substring(10);
                        names.remove(temp);
                        for (PrintWriter writer : writers) {
                            writer.println("DISCONNECT" + temp);
    	                    writer.println("MESSAGE " + name + " has disconnected.\n");
                        }
                        if (out != null) {
                            writers.remove(out);
                        }
                        try {
                            socket.close();
                        } catch (IOException e) {
                        }
                    	
                    } else if (input.startsWith("NEWPM")){
                    	String newChat = input.substring(5);
                    	System.out.println("PM to " + newChat + "\n");
                    	String fromWho = in.readLine();
                    	System.out.println("PM from " + fromWho + "\n");
                    	if (names.contains(newChat)){
                    		System.out.println("Recognizes " + newChat + "\n");
                    		for (PrintWriter writer : writers){
                    			writer.println("NEWPM" + newChat);
                    			writer.println(fromWho);
                    		}
                    	}
                    }
                    else if (input.startsWith("SENDTO")){
                    	String newChat = input.substring(6);
                    	System.out.println("PM to " + newChat + "\n");
                    	System.out.println("PM from " + name + "\n");
                    	String currentMessage = in.readLine();
                    	if (names.contains(newChat)){
                    		System.out.println("Recognizes " + newChat + "\n");
                    		System.out.println("Message is: " + currentMessage + "\n");
                    		for (PrintWriter writer : writers){
                    			writer.println("GET" + newChat);
                    			writer.println("RECIEVE" + name);
                    			writer.println( currentMessage );
                    		}
                    	}
                    }
                    else {
	                    for (PrintWriter writer : writers) {
                                String thisMsg = name + ":" + "  ";
                                thisMsg = encrypt_1(thisMsg);
                                thisMsg = encrypt_2(thisMsg);
                                String space = encrypt_2(encrypt_1(""));
	                        writer.println("MESSAGE" + input + space + thisMsg);
	                        System.out.println("Message from " + name + ": " + input);
                                
                                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                                Date date = new Date();
                                String reportDate = dateFormat.format(date);
                                
                                System.out.println(dateFormat.format(date));
	                    }
                    }
                }
            } catch (IOException e) {
                System.out.println(e);
            } finally {
                if (name != null) {
                    names.remove(name);
                    for (PrintWriter writer : writers) {
                    	writer.println("DELETEUSER" + name);
                    }
                }
                if (out != null) {
                    writers.remove(out);
                }
                try {
                    socket.close();
                } catch (IOException e) {
                }
            }
        }
    }
}
