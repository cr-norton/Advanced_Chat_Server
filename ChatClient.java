package chatclient;
import java.awt.AlphaComposite;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ChatClient {
    
    int PORT = 5000;
    
    BufferedReader in;
    PrintWriter out;
    
    OutputStream outStream;
    DataOutputStream dosStream;
    InputStream inStream;
    DataInputStream disStream;
    
    JFrame frame = new JFrame("LetsTalk");
    
    JTextField textField = new JTextField(40);
    JTextArea users = new JTextArea(10,10);
    JTextArea messageArea = new JTextArea(20, 20);
    
    JPanel buttonFrame = new JPanel();
    JButton privateMessage = new JButton("Message");
    JButton chatRooms = new JButton("Chatrooms");
    JButton disconnect = new JButton("Disconnect");
    JButton liveStream = new JButton("Video");
    JButton uploadimage = new JButton("Upload");
    JButton status = new JButton("Status");
    JButton admin = new JButton("Admin");
    //JLabel label = new JLabel();
    JPanel panel = new JPanel ();
    
    JFileChooser fc = new JFileChooser();

    private HashSet<String> names = new HashSet<>();
    String myUsername;
    String thisServer;

    public ChatClient(final Socket socket) {

        // Layout GUI
        textField.setEditable(false);
        users.setEditable(false);
        messageArea.setEditable(false);
        frame.getContentPane().add(textField, "South");
        frame.getContentPane().add(users, "West");
        frame.getContentPane().add(new JScrollPane(messageArea), "Center");
        frame.getContentPane().add(buttonFrame, "North");
        //frame.getContentPane().add(label, "East");
        frame.getContentPane().add(panel, "East");
        
        buttonFrame.add(status);
        buttonFrame.add(admin);
        buttonFrame.add(privateMessage);
        buttonFrame.add(chatRooms);
        buttonFrame.add(liveStream);
        buttonFrame.add(uploadimage);
        buttonFrame.add(disconnect);
        
        //label.setPreferredSize(new Dimension(200,200));
        panel.setPreferredSize(new Dimension(100,100));
        frame.pack();
        
        //JScrollPane scroller = new JScrollPane(label, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        JScrollPane scroller = new JScrollPane(panel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        frame.add(scroller);
        
        // Add Listeners
        uploadimage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int returnVal = fc.showOpenDialog( frame );
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fc.getSelectedFile();
                    Image img;
                    try
                    {
                        img = ImageIO.read(file);
                        BufferedImage originalImage = ImageIO.read(file);

                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        ImageIO.write( originalImage, "jpg", baos );
                        baos.flush();
                        byte[] imageInByte = baos.toByteArray();
                        baos.close();
                        
                        out.println("IMAGE");
                        
                        dosStream.writeInt(imageInByte.length);
                        if (imageInByte.length > 0) {
                            dosStream.write(imageInByte, 0, imageInByte.length);
                        }

                    }
                    catch(IOException k)
                    {

                    }
                }
            }
            
        });
        
        
        
        textField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Date date = new Date();
                String reportDate = dateFormat.format(date);
                String full = reportDate + "      " + textField.getText();
                out.println(encryption(full));
                textField.setText("");
            }
        });
        
        disconnect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                out.println("DISCONNECT" + getName());
            }
        });
        
        status.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    saveToFile( "saved.txt", messageArea);
                } catch (Exception ex) {
                    Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        privateMessage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String connectTo = JOptionPane.showInputDialog(frame, "Enter user to chat with", "PM", JOptionPane.PLAIN_MESSAGE);
                out.println("NEWPM" + connectTo);
                out.println(myUsername);
                try {
                    privateChat(myUsername, connectTo, socket);
                } catch (IOException ex) {
                    Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }); 
    }

    private void privateChat(final String user_1, final String user_2, Socket socket) throws IOException{
        JFrame privateFrame = new JFrame( user_2 );
        final JTextField textField2 = new JTextField(40);
        JTextArea users2 = new JTextArea(10,10);
        JTextArea messageArea2 = new JTextArea(8, 40);
        JPanel buttonFrame2 = new JPanel();
        JButton privateMessage2 = new JButton("Send");
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
        
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
        
        textField2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                out.println("SENDTO" + user_2);
                out.println(textField.getText());
                textField2.setText("");
            }
        });
        
    }
    
    private void Disconnect(String line){
        String temp = line.substring(10);
        names.remove(temp);
        users.setText("");
        for (String s : names) {
            users.append(s + "\n");
        }
    }
    
    private void updateUsers(String line){
        if ( names.contains(line.substring(10)) == false){
            names.add(line.substring(10));
        }
        users.setText("");
        for (String s : names) {
            users.append(s + "\n");
        }
    }
    
    private String getServerAddress() {
        return JOptionPane.showInputDialog(frame, "Enter IP Address of the Server:", "Welcome to the Chatter", JOptionPane.QUESTION_MESSAGE);
    }

    private String getName() {
        return JOptionPane.showInputDialog(frame, "Choose a screen name:", "Screen name selection", JOptionPane.PLAIN_MESSAGE);
    }
    

    public synchronized void play(final String fileName) 
    {         
        final File file = new File(fileName);
        new Thread(new Runnable() { 
            public void run() {
                try {
                    Clip clip = AudioSystem.getClip();
                    AudioInputStream inputStream = AudioSystem.getAudioInputStream(file);
                    clip.open(inputStream);
                    clip.start(); 
                } catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
                    System.out.println("play sound error: " + e.getMessage() + " for " + fileName);
                }
            }
        }).start();
    }

    
    public void saveToFile(String fileName, JTextArea textField) throws Exception {
        FileOutputStream out = new FileOutputStream(fileName, true);
        out.write(textField.getText().getBytes());
    } 
    

    private void run(Socket socket) throws IOException {

        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);

        outStream = socket.getOutputStream(); 
        dosStream = new DataOutputStream(outStream);
        inStream = socket.getInputStream();
        disStream = new DataInputStream(inStream);
        
        while (true) {
            String line = in.readLine();
            
            if (line.startsWith("SUBMITNAME")) {
                String thisName = getName();
                myUsername = thisName;
                out.println(myUsername);
            }
            else if (line.startsWith("NAMEACCEPTED")) {
                textField.setEditable(true);
            }
            else if (line.startsWith("MESSAGE")) {
                play("boxing_bell.wav");
                messageArea.append(decryption(line.substring(7)) + "\n");
            }
            else if (line.startsWith("NEWPM")){
                String me = line.substring(5);
                if (myUsername.equals(me)){
                    String fromWho = in.readLine();
                    privateChat(me, fromWho, socket);
                }
            } 
            else if (line.startsWith("IMAGE")){

                int len = disStream.readInt();
                byte[] data = new byte[len];
                if (len > 0) {
                    disStream.readFully(data);
                }
                InputStream in = new ByteArrayInputStream(data);
                BufferedImage bImageFromConvert = ImageIO.read(in);
                BufferedImage bufferedImage = new BufferedImage(200, 100, BufferedImage.TYPE_INT_RGB);
                Graphics2D graphics2D = bufferedImage.createGraphics();
                graphics2D.setComposite(AlphaComposite.Src);
                graphics2D.drawImage(bImageFromConvert, 0, 0, 200, 100, null);
                graphics2D.dispose(); 
                panel.add (new JLabel (new ImageIcon (bufferedImage)));
            } 
            else if (line.startsWith("RESET")){
                names = null;
                names = new HashSet<String>();
                users.setText("");
            }
            else if (line.startsWith("GET")){
                String me = line.substring(3);
                if (me.equals(myUsername)){
                    String from = in.readLine();
                    if (from.startsWith("RECIEVE")){
                        String fromWho = from.substring(7);
                        String thisMessage = in.readLine();
                        messageArea.append(fromWho + ": " + thisMessage + "\n");
                    }
                }
            }   
            else if (line.startsWith("DISCONNECT")) {
                Disconnect(line);
            }
            else if (line.startsWith("UPDATEUSER")) {
                updateUsers(line);
            }
        }
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
            else if ( letter.equals("1") | letter.equals("2") | letter.equals("3") | letter.equals("4") | letter.equals("5") | letter.equals("6") | letter.equals("7") | letter.equals("8") | letter.equals("9") | letter.equals("0")){
                encryption = encryption + "0111";
                if ( letter.equals("1") ){
                    encryption = encryption + "0000";
                }
                else if ( letter.equals("2") ){
                    encryption = encryption + "0001";
                }
                else if ( letter.equals("3") ){
                    encryption = encryption + "0010";
                }
                else if ( letter.equals("4") ){
                    encryption = encryption + "0011";
                }
                else if ( letter.equals("5") ){
                    encryption = encryption + "0100";
                }
                else if ( letter.equals("6") ){
                    encryption = encryption + "0101";
                }
                else if ( letter.equals("7") ){
                    encryption = encryption + "0110";
                }
                else if ( letter.equals("8") ){
                    encryption = encryption + "0111";
                }
                else if ( letter.equals("9") ){
                    encryption = encryption + "1000";
                }
                else if ( letter.equals("0") ){
                    encryption = encryption + "1001";
                }
            }
            else if ( letter.equals(" ") ){
                encryption = encryption + "11111111";
            }
            else if ( letter.equals(":") ){
                encryption = encryption + "11100111";
            }
            else if ( letter.equals("/") ){
                encryption = encryption + "11110111";
            }
        }        
        return encryption;
    }
    
    private String decrypt_1(String message){
        String decryption = "";     
        for ( int i = 0; i < message.length() - 7; i += 8){
            
            String l_5 = Character.toString(message.charAt(i+4));
            String l_6 = Character.toString(message.charAt(i+5));
            String l_7 = Character.toString(message.charAt(i+6));
            String l_8 = Character.toString(message.charAt(i+7));
            String l_1 = Character.toString(message.charAt(i));
            String l_2 = Character.toString(message.charAt(i+1));
            String l_3 = Character.toString(message.charAt(i+2));
            String l_4 = Character.toString(message.charAt(i+3));
            
            String first = l_1 + l_2 + l_3 + l_4;
            String second = l_5 + l_6 + l_7 + l_8;
            
            if ( first.equals("0000") ){
                if ( second.equals("0000") ){
                    decryption = decryption + "a";
                }
                else if ( second.equals("0001") ){
                    decryption = decryption + "b";
                }
                else if ( second.equals("0010") ){
                    decryption = decryption + "c";
                }
                else if ( second.equals("0011") ){
                    decryption = decryption + "d";
                }
                else if ( second.equals("0100") ){
                    decryption = decryption + "A";
                }
                else if ( second.equals("0101") ){
                    decryption = decryption + "B";
                }
                else if ( second.equals("0110") ){
                    decryption = decryption + "C";
                }
                else if ( second.equals("0111") ){
                    decryption = decryption + "D";
                }
            }
            else if ( first.equals("0001") ){
                if ( second.equals("0000") ){
                    decryption = decryption + "e";
                }
                else if ( second.equals("0001") ){
                    decryption = decryption + "f";
                }
                else if ( second.equals("0010") ){
                    decryption = decryption + "g";
                }
                else if ( second.equals("0011") ){
                    decryption = decryption + "h";
                }
                else if ( second.equals("0100") ){
                    decryption = decryption + "E";
                }
                else if ( second.equals("0101") ){
                    decryption = decryption + "F";
                }
                else if ( second.equals("0110") ){
                    decryption = decryption + "G";
                }
                else if ( second.equals("0111") ){
                    decryption = decryption + "H";
                }
            }
            else if ( first.equals("0010") ){
                if ( second.equals("0000") ){
                    decryption = decryption + "i";
                }
                else if ( second.equals("0001") ){
                    decryption = decryption + "j";
                }
                else if ( second.equals("0010") ){
                    decryption = decryption + "k";
                }
                else if ( second.equals("0011") ){
                    decryption = decryption + "l";
                }
                else if ( second.equals("0100") ){
                    decryption = decryption + "I";
                }
                else if ( second.equals("0101") ){
                    decryption = decryption + "J";
                }
                else if ( second.equals("0110") ){
                    decryption = decryption + "K";
                }
                else if ( second.equals("0111") ){
                    decryption = decryption + "L";
                }
            }
            else if ( first.equals("0011") ){
                if ( second.equals("0000") ){
                    decryption = decryption + "m";
                }
                else if ( second.equals("0001") ){
                    decryption = decryption + "n";
                }
                else if ( second.equals("0010") ){
                    decryption = decryption + "o";
                }
                else if ( second.equals("0011") ){
                    decryption = decryption + "p";
                }
                else if ( second.equals("0100") ){
                    decryption = decryption + "M";
                }
                else if ( second.equals("0101") ){
                    decryption = decryption + "N";
                }
                else if ( second.equals("0110") ){
                    decryption = decryption + "O";
                }
                else if ( second.equals("0111") ){
                    decryption = decryption + "P";
                }
            }
            else if ( first.equals("0100") ){
                if ( second.equals("0000") ){
                    decryption = decryption + "q";
                }
                else if ( second.equals("0001") ){
                    decryption = decryption + "r";
                }
                else if ( second.equals("0010") ){
                    decryption = decryption + "s";
                }
                else if ( second.equals("0011") ){
                    decryption = decryption + "t";
                }
                else if ( second.equals("0100") ){
                    decryption = decryption + "Q";
                }
                else if ( second.equals("0101") ){
                    decryption = decryption + "R";
                }
                else if ( second.equals("0110") ){
                    decryption = decryption + "S";
                }
                else if ( second.equals("0111") ){
                    decryption = decryption + "T";
                }
            }
            else if ( first.equals("0101") ){
                if ( second.equals("0000") ){
                    decryption = decryption + "u";
                }
                else if ( second.equals("0001") ){
                    decryption = decryption + "v";
                }
                else if ( second.equals("0010") ){
                    decryption = decryption + "w";
                }
                else if ( second.equals("0011") ){
                    decryption = decryption + "x";
                }
                else if ( second.equals("0100") ){
                    decryption = decryption + "U";
                }
                else if ( second.equals("0101") ){
                    decryption = decryption + "V";
                }
                else if ( second.equals("0110") ){
                    decryption = decryption + "W";
                }
                else if ( second.equals("0111") ){
                    decryption = decryption + "X";
                }
            }
            else if ( first.equals("0110") ) {
                if ( second.equals("0000") ){
                    decryption = decryption + "y";
                }
                else if ( second.equals("0001") ){
                    decryption = decryption + "z";
                }
                else if ( second.equals("0010") ){
                    decryption = decryption + "Y";
                }
                else if ( second.equals("0011") ){
                    decryption = decryption + "Z";
                }
            }
            else if ( first.equals("0111") ) {
                if ( second.equals("0000") ){
                    decryption = decryption + "1";
                }
                else if ( second.equals("0001") ){
                    decryption = decryption + "2";
                }
                else if ( second.equals("0010") ){
                    decryption = decryption + "3";
                }
                else if ( second.equals("0011") ){
                    decryption = decryption + "4";
                }
                else if ( second.equals("0100") ){
                    decryption = decryption + "5";
                }
                else if ( second.equals("0101") ){
                    decryption = decryption + "6";
                }
                else if ( second.equals("0110") ){
                    decryption = decryption + "7";
                }
                else if ( second.equals("0111") ){
                    decryption = decryption + "8";
                }
                else if ( second.equals("1000") ){
                    decryption = decryption + "9";
                }
                else if ( second.equals("1001") ){
                    decryption = decryption + "0";
                }
            }
            else if ( first.equals("1111") && (second.equals("1111")) ){
                decryption = decryption + " ";
            }
            else if ( first.equals("1110") && (second.equals("0111")) ){
                decryption = decryption + ":";
            }
            else if ( first.equals("1111") && (second.equals("0111")) ){
                decryption = decryption + "/";
            }
        }
        
        return decryption;
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
    
    private String decrypt_2(String message){
        String decryption = "";
        String temp = new StringBuilder(message).reverse().toString();
        
        for (int i = 0; i < temp.length(); i++){
            String Decrypt_i = Character.toString(temp.charAt(i));
            if ( Decrypt_i.equals("1") ){
                decryption = decryption + "0";
            }
            else {
                decryption = decryption + "1";
            }
        }
        return decryption;
    }
    
    private String encryption(String message){
        String encryption;
        encryption = encrypt_1(message);
        encryption = encrypt_2(encryption);
        return encryption;
    }
    
    private String decryption(String message){
        String decryption;
        decryption = decrypt_2(message);
        decryption = decrypt_1(decryption);
        return decryption;
    }
    
    
    
    public static void main(String[] args) throws Exception {
        Socket socket = new Socket("10.254.4.17", 5000);
        ChatClient client = new ChatClient(socket);
        client.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        client.frame.setVisible(true);
        InetAddress ip;
        ip = InetAddress.getLocalHost();
        String address = ip.getHostAddress();
        client.run(socket);
    }
}
