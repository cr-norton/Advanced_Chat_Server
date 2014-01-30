import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

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
                    else if (input.startsWith("DISCONNECT")){
                        names.remove(input.substring(10));
                        for (PrintWriter writer : writers) {
                        	writer.println("RESET");
                            for (String s : names) {
                            	writer.println("UPDATEUSER " + s);
                            }
    	                    writer.println("MESSAGE " + name + " has disconnected.\n");
                        }
                        if (out != null) {
                            writers.remove(out);
                        }
                        try {
                            socket.close();
                        } catch (IOException e) {
                        }
                    	
                    } else if (input.startsWith("PM")){
                    	String newChat = input.substring(2);
                    	if (names.contains(newChat)){
                    		for (PrintWriter writer : writers){
                    			if (writer.equals(newChat)){
                    				System.out.println(name + " " + writer);
                    				writer.println("MESSAGE " + name + "wants to chat");
                    			}
                    		}
                    	}
                    }
                    else {
	                    for (PrintWriter writer : writers) {
	                        writer.println("MESSAGE " + name + ": " + input);
	                        System.out.println("Message from " + name + ": " + input);
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