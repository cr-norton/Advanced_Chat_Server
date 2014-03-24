package cli;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import java.awt.AlphaComposite;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
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
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
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
import javax.swing.SwingConstants;

public class Cli {
    
    static int PORT = 5000;

    //Messaging
    BufferedReader in;
    PrintWriter out;
    
    //Image Transfer
    OutputStream outStream;
    DataOutputStream dosStream;
    InputStream inStream;
    DataInputStream disStream;
    
    //Login Window
    JFrame loginFrame = new JFrame("Welcome to LetsTalk");
    JButton loginButton = new JButton("Login");
    JButton signupButton = new JButton("Sign Up");
    
    //Chat Room Selector
    JFrame roomSelection = new JFrame("Pick a chat room");
    JButton defaultRoom = new JButton("Room One");
    JButton secondRoom = new JButton("Room Two");
    JButton thirdRoom = new JButton("Room Three");
    JButton fouthRoom = new JButton("Room Four");
    JPanel roomButtonFrame = new JPanel();
    
    //Status
    JFrame statusSelection = new JFrame("Update your status");
    JButton available = new JButton("Available");
    JButton away = new JButton("Away");
    JButton busy = new JButton("Busy");
    JPanel statusFrame = new JPanel();
    
    //Main Chat Window
    static JFrame frame = new JFrame("LetsTalk");
    JTextField textField = new JTextField(40);
    JTextArea users = new JTextArea(10,10);
    JTextArea messageArea = new JTextArea(20, 20);
    
    //Button Panel
    JPanel buttonFrame = new JPanel();
    JButton privateMessage = new JButton("Message");
    JButton chatRooms = new JButton("Chatrooms");
    JButton disconnect = new JButton("Disconnect");
    JButton liveStream = new JButton("Video");
    JButton uploadimage = new JButton("Upload");
    JButton status = new JButton("Save");
    JButton admin = new JButton("Status");
    
    //Image Panel and File Choser
    JPanel panel = new JPanel ();
    //JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    JFileChooser fc = new JFileChooser();
    
    static JFrame privateFrame1 = new JFrame();
    final JTextField textField1 = new JTextField(40);
    JTextArea messageArea1 = new JTextArea(8, 40);
    JPanel buttonFrame1 = new JPanel();
    JButton close1 = new JButton("Close");
    
    static JFrame privateFrame2 = new JFrame();
    final JTextField textField2 = new JTextField(40);
    JTextArea messageArea2 = new JTextArea(8, 40);
    JPanel buttonFrame2 = new JPanel();
    JButton close2 = new JButton("Close");
    
    static JFrame privateFrame3 = new JFrame();
    final JTextField textField3 = new JTextField(40);
    JTextArea messageArea3 = new JTextArea(8, 40);
    JPanel buttonFrame3 = new JPanel();
    JButton close3 = new JButton("Close");
    
    static JFrame privateFrame4 = new JFrame();
    final JTextField textField4 = new JTextField(40);
    JTextArea messageArea4 = new JTextArea(8, 40);
    JPanel buttonFrame4 = new JPanel();
    JButton close4 = new JButton("Close");
    
    static JFrame privateFrame5 = new JFrame();
    final JTextField textField5 = new JTextField(40);
    JTextArea messageArea5 = new JTextArea(8, 40);
    JPanel buttonFrame5 = new JPanel();
    JButton close5 = new JButton("Close");
    
    static JFrame privateFrame6 = new JFrame();
    final JTextField textField6 = new JTextField(40);
    JTextArea messageArea6 = new JTextArea(8, 40);
    JPanel buttonFrame6 = new JPanel();
    JButton close6 = new JButton("Close");
    
    static JFrame privateFrame7 = new JFrame();
    final JTextField textField7 = new JTextField(40);
    JTextArea messageArea7 = new JTextArea(8, 40);
    JPanel buttonFrame7 = new JPanel();
    JButton close7 = new JButton("Close");
        
    static JFrame privateFrame8 = new JFrame();
    final JTextField textField8 = new JTextField(40);
    JTextArea messageArea8 = new JTextArea(8, 40);
    JPanel buttonFrame8 = new JPanel();
    JButton close8 = new JButton("Close");
    
    static JFrame privateFrame9 = new JFrame();
    final JTextField textField9 = new JTextField(40);
    JTextArea messageArea9 = new JTextArea(8, 40);
    JPanel buttonFrame9 = new JPanel();
    JButton close9 = new JButton("Close");
    
    static JFrame privateFrame10 = new JFrame();
    final JTextField textField10 = new JTextField(40);
    JTextArea messageArea10 = new JTextArea(8, 40);
    JPanel buttonFrame10 = new JPanel();
    JButton close10 = new JButton("Close");
    
    String myUsername;
    String thisServer;
    String currentChatRoom;
    String myKey;

    String keyString = "770A8A65DA156D24";
    String Status = "AVAILABLE";

    
    private HashSet<String> names = new HashSet<>();
    private HashSet<clients> Users = new HashSet<>();
    
    public class clients{
        public String uName;
        public String uStatus;
        
        clients(String name, String status){
            this.uName = name;
            this.uStatus = status;
        }
    }
    
    private boolean inUse_1 = false;
    private boolean inUse_2 = false;
    private boolean inUse_3 = false;
    private boolean inUse_4 = false;
    private boolean inUse_5 = false;
    private boolean inUse_6 = false;
    private boolean inUse_7 = false;
    private boolean inUse_8 = false;
    private boolean inUse_9 = false;
    private boolean inUse_10 = false;  
    
    private String PM_1 = null;
    private String PM_2 = null;
    private String PM_3 = null;
    private String PM_4 = null;
    private String PM_5 = null;
    private String PM_6 = null;
    private String PM_7 = null;
    private String PM_8 = null;
    private String PM_9 = null;
    private String PM_10 = null;
    
    static private String serverIP;
    boolean isRunning = true;
    
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
    
    public Cli(final Socket socket) {
        
        // Layout GUI
        textField.setEditable(false);
        users.setEditable(false);
        messageArea.setEditable(false);
        frame.getContentPane().add(textField, "South");
        frame.getContentPane().add(users, "West");
        frame.getContentPane().add(new JScrollPane(messageArea), "Center");
        frame.getContentPane().add(buttonFrame, "North");
        frame.getContentPane().add(panel, "East");
        
        //Add Buttons
        buttonFrame.add(status);
        buttonFrame.add(admin);
        buttonFrame.add(privateMessage);
        buttonFrame.add(chatRooms);
        buttonFrame.add(liveStream);
        buttonFrame.add(uploadimage);
        buttonFrame.add(disconnect);

        //Size and Scroll
        panel.setPreferredSize(new Dimension(100, 100));
        frame.pack();
        JScrollPane scroller = new JScrollPane(panel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        frame.add(scroller);
        
        textField1.setEditable(true);
        messageArea1.setEditable(false);  
        privateFrame1.getContentPane().add(textField1, "North");
        privateFrame1.getContentPane().add(new JScrollPane(messageArea1), "Center");
        privateFrame1.getContentPane().add(buttonFrame1, "South");
        buttonFrame1.add(close1);
        privateFrame1.pack();
        privateFrame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        privateFrame1.setVisible(false);        
        
        textField2.setEditable(true);
        messageArea2.setEditable(false);  
        privateFrame2.getContentPane().add(textField2, "North");
        privateFrame2.getContentPane().add(new JScrollPane(messageArea2), "Center");
        privateFrame2.getContentPane().add(buttonFrame2, "South");
        buttonFrame2.add(close2);
        privateFrame2.pack();
        privateFrame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        privateFrame2.setVisible(false);   
        
        textField3.setEditable(true);
        messageArea3.setEditable(false);  
        privateFrame3.getContentPane().add(textField3, "North");
        privateFrame3.getContentPane().add(new JScrollPane(messageArea3), "Center");
        privateFrame3.getContentPane().add(buttonFrame3, "South");
        buttonFrame3.add(close3);
        privateFrame3.pack();
        privateFrame3.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        privateFrame3.setVisible(false);   
        
        textField4.setEditable(true);
        messageArea4.setEditable(false);  
        privateFrame4.getContentPane().add(textField4, "North");
        privateFrame4.getContentPane().add(new JScrollPane(messageArea4), "Center");
        privateFrame4.getContentPane().add(buttonFrame4, "South");
        buttonFrame4.add(close4);
        privateFrame4.pack();
        privateFrame4.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        privateFrame4.setVisible(false);   
        
        textField5.setEditable(true);
        messageArea5.setEditable(false);  
        privateFrame5.getContentPane().add(textField5, "North");
        privateFrame5.getContentPane().add(new JScrollPane(messageArea5), "Center");
        privateFrame5.getContentPane().add(buttonFrame5, "South");
        buttonFrame5.add(close5);
        privateFrame5.pack();
        privateFrame5.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        privateFrame5.setVisible(false);   
        
        textField6.setEditable(true);
        messageArea6.setEditable(false);  
        privateFrame6.getContentPane().add(textField6, "North");
        privateFrame6.getContentPane().add(new JScrollPane(messageArea6), "Center");
        privateFrame6.getContentPane().add(buttonFrame6, "South");
        buttonFrame6.add(close6);
        privateFrame6.pack();
        privateFrame6.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        privateFrame6.setVisible(false);   
        
        textField7.setEditable(true);
        messageArea7.setEditable(false);  
        privateFrame7.getContentPane().add(textField7, "North");
        privateFrame7.getContentPane().add(new JScrollPane(messageArea7), "Center");
        privateFrame7.getContentPane().add(buttonFrame7, "South");
        buttonFrame7.add(close7);
        privateFrame7.pack();
        privateFrame7.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        privateFrame7.setVisible(false);           
        
        textField8.setEditable(true);
        messageArea8.setEditable(false);  
        privateFrame8.getContentPane().add(textField8, "North");
        privateFrame8.getContentPane().add(new JScrollPane(messageArea8), "Center");
        privateFrame8.getContentPane().add(buttonFrame8, "South");
        buttonFrame8.add(close8);
        privateFrame8.pack();
        privateFrame8.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        privateFrame8.setVisible(false);   
        
        textField9.setEditable(true);
        messageArea9.setEditable(false);  
        privateFrame9.getContentPane().add(textField9, "North");
        privateFrame9.getContentPane().add(new JScrollPane(messageArea9), "Center");
        privateFrame9.getContentPane().add(buttonFrame9, "South");
        buttonFrame9.add(close9);
        privateFrame9.pack();
        privateFrame9.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        privateFrame9.setVisible(false);   
        
        textField10.setEditable(true);
        messageArea10.setEditable(false);  
        privateFrame10.getContentPane().add(textField10, "North");
        privateFrame10.getContentPane().add(new JScrollPane(messageArea10), "Center");
        privateFrame10.getContentPane().add(buttonFrame10, "South");
        buttonFrame10.add(close10);
        privateFrame10.pack();
        privateFrame10.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        privateFrame10.setVisible(false);        
        
        
        currentChatRoom = "ONE";
        
            close1.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    privateFrame1.setVisible(false);
                    inUse_1 = false;
                }
            });   
            close2.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    privateFrame2.setVisible(false);
                    inUse_2 = false;
                }
            });   
            close3.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    privateFrame3.setVisible(false);
                    inUse_3 = false;
                }
            });   
            close4.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    privateFrame4.setVisible(false);
                    inUse_4 = false;
                }
            });   
            close5.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    privateFrame5.setVisible(false);
                    inUse_5 = false;
                }
            });   
            close6.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    privateFrame6.setVisible(false);
                    inUse_6 = false;
                }
            });   
            close7.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    privateFrame7.setVisible(false);
                    inUse_7 = false;
                }
            });   
            close8.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    privateFrame8.setVisible(false);
                    inUse_8 = false;
                }
            });   
            close9.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    privateFrame9.setVisible(false);
                    inUse_9 = false;
                }
            });   
            close10.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    privateFrame10.setVisible(false);
                    inUse_10 = false;
                }
            });  
            
        textField1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {            
                try {
                    String send = "PM" + PM_1;
                    send = Encrypt(send, myKey);
                    out.println(send);
                    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    Date date = new Date();
                    String reportDate = dateFormat.format(date);
                    String msg = textField1.getText();
                    String full = "( " + reportDate + " ) " + myUsername + ": " + msg;
                    String full2 = "( " + reportDate + " ) " + myUsername + ": " + msg + "\n";
                    messageArea1.append(full2);
                    full = Encrypt(full, myKey);
                    out.println(full);
                    textField1.setText("");
                } catch (        NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException | UnsupportedEncodingException ex) {
                    Logger.getLogger(Cli.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        textField2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {            
                try {
                    String send = "PM" + PM_2;
                    send = Encrypt(send, myKey);
                    out.println(send);
                    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    Date date = new Date();
                    String reportDate = dateFormat.format(date);
                    String msg = textField2.getText();
                    String full = "( " + reportDate + " ) " + myUsername + ": " + msg + "\n";
                    messageArea2.append(full);
                    full = Encrypt(full, myKey);
                    out.println(full);
                    textField2.setText("");
                } catch (        NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException | UnsupportedEncodingException ex) {
                    Logger.getLogger(Cli.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
  
        textField3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {            
                try {
                    String send = "PM" + PM_3;
                    send = Encrypt(send, myKey);
                    out.println(send);
                    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    Date date = new Date();
                    String reportDate = dateFormat.format(date);
                    String msg = textField3.getText();
                    String full = "( " + reportDate + " ) " + myUsername + ": " + msg + "\n";
                    messageArea3.append(full);
                    full = Encrypt(full, myKey);
                    out.println(full);
                    textField3.setText("");
                } catch (        NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException | UnsupportedEncodingException ex) {
                    Logger.getLogger(Cli.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        textField4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {            
                try {
                    String send = "PM" + PM_4;
                    send = Encrypt(send, myKey);
                    out.println(send);
                    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    Date date = new Date();
                    String reportDate = dateFormat.format(date);
                    String msg = textField4.getText();
                    String full = "( " + reportDate + " ) " + myUsername + ": " + msg + "\n";
                    messageArea4.append(full);
                    full = Encrypt(full, myKey);
                    out.println(full);
                    textField4.setText("");
                } catch (        NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException | UnsupportedEncodingException ex) {
                    Logger.getLogger(Cli.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        textField5.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {            
                try {
                    String send = "PM" + PM_5;
                    send = Encrypt(send, myKey);
                    out.println(send);
                    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    Date date = new Date();
                    String reportDate = dateFormat.format(date);
                    String msg = textField5.getText();
                    String full = "( " + reportDate + " ) " + myUsername + ": " + msg + "\n";
                    messageArea5.append(full);
                    full = Encrypt(full, myKey);
                    out.println(full);
                    textField5.setText("");
                } catch (        NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException | UnsupportedEncodingException ex) {
                    Logger.getLogger(Cli.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    
        textField6.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {            
                try {
                    String send = "PM" + PM_6;
                    send = Encrypt(send, myKey);
                    out.println(send);
                    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    Date date = new Date();
                    String reportDate = dateFormat.format(date);
                    String msg = textField6.getText();
                    String full = "( " + reportDate + " ) " + myUsername + ": " + msg + "\n";
                    messageArea6.append(full);
                    full = Encrypt(full, myKey);
                    out.println(full);
                    textField6.setText("");
                } catch (        NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException | UnsupportedEncodingException ex) {
                    Logger.getLogger(Cli.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        textField7.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {            
                try {
                    String send = "PM" + PM_7;
                    send = Encrypt(send, myKey);
                    out.println(send);
                    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    Date date = new Date();
                    String reportDate = dateFormat.format(date);
                    String msg = textField7.getText();
                    String full = "( " + reportDate + " ) " + myUsername + ": " + msg + "\n";
                    messageArea7.append(full);
                    full = Encrypt(full, myKey);
                    out.println(full);
                    textField7.setText("");
                } catch (        NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException | UnsupportedEncodingException ex) {
                    Logger.getLogger(Cli.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        textField8.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {            
                try {
                    String send = "PM" + PM_8;
                    send = Encrypt(send, myKey);
                    out.println(send);
                    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    Date date = new Date();
                    String reportDate = dateFormat.format(date);
                    String msg = textField8.getText();
                    String full = "( " + reportDate + " ) " + myUsername + ": " + msg + "\n";
                    messageArea8.append(full);
                    full = Encrypt(full, myKey);
                    out.println(full);
                    textField8.setText("");
                } catch (        NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException | UnsupportedEncodingException ex) {
                    Logger.getLogger(Cli.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });    
        
        textField9.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {            
                try {
                    String send = "PM" + PM_9;
                    send = Encrypt(send, myKey);
                    out.println(send);
                    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    Date date = new Date();
                    String reportDate = dateFormat.format(date);
                    String msg = textField9.getText();
                    String full = "( " + reportDate + " ) " + myUsername + ": " + msg + "\n";
                    messageArea9.append(full);
                    full = Encrypt(full, myKey);
                    out.println(full);
                    textField9.setText("");
                } catch (        NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException | UnsupportedEncodingException ex) {
                    Logger.getLogger(Cli.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }); 
        
        textField10.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {            
                try {
                    String send = "PM" + PM_10;
                    send = Encrypt(send, myKey);
                    out.println(send);
                    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    Date date = new Date();
                    String reportDate = dateFormat.format(date);
                    String msg = textField10.getText();
                    String full = "( " + reportDate + " ) " + myUsername + ": " + msg + "\n";
                    messageArea10.append(full);
                    full = Encrypt(full, myKey);
                    out.println(full);
                    textField10.setText("");
                } catch (        NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException | UnsupportedEncodingException ex) {
                    Logger.getLogger(Cli.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }); 
        
        
        
        // Add Listeners
        textField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {            
                try {
                    String group = "GROUP" + currentChatRoom;
                    String full = group + textField.getText();
                    String text = Encrypt(full, myKey);
                    out.println(text);
                    textField.setText("");
                } catch (        NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException | UnsupportedEncodingException ex) {
                    Logger.getLogger(Cli.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        disconnect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String text = "DISCONNECT" + myUsername;
                    String cmd = Encrypt(text, myKey);
                    out.println(cmd);
                    frame.setVisible(false);
                    frame.dispose();
                    isRunning = false;
                    System.exit(0);
                    //socket.close();
                } catch (        NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException | UnsupportedEncodingException ex) {
                    Logger.getLogger(Cli.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        uploadimage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int returnVal = fc.showOpenDialog( frame );
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fc.getSelectedFile();
                    try
                    {
                        BufferedImage originalImage = ImageIO.read(file);
                        originalImage = resize(originalImage, 200, 100);
                        byte[] imageInByte;
                        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                            System.out.println(originalImage.getType());
                            ImageIO.write( originalImage, "jpg", baos );
                            imageInByte = baos.toByteArray();
                            baos.flush();
                            baos.close();
                        }
                        String cmd = Encrypt("IMAGE", myKey);
                        out.println(cmd);
                        //dosStream.writeInt(imageInByte.length);
                        if (imageInByte.length > 0) {
                            dosStream.writeInt(imageInByte.length);
                            System.out.println(imageInByte.length + "is length \n");
                            dosStream.write(imageInByte, 0, imageInByte.length);
                        }
                        dosStream.flush();
                    }
                    catch(IOException k)
                    {

                    } catch (            NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException ex) {
                        Logger.getLogger(Cli.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            
        });
        
        chatRooms.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                roomSelection.getContentPane().add(roomButtonFrame);
                roomButtonFrame.add(defaultRoom);
                roomButtonFrame.add(secondRoom);
                roomButtonFrame.add(thirdRoom);
                roomButtonFrame.add(fouthRoom);
                roomSelection.setVisible(true);
                roomSelection.pack();
                
                defaultRoom.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        currentChatRoom = "ONE";
                        roomSelection.setVisible(false);
                        updateFrame();
                    }
                });

                secondRoom.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        currentChatRoom = "TWO";
                        roomSelection.setVisible(false);
                        updateFrame();
                    }
                });

                thirdRoom.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        currentChatRoom = "THREE";
                        roomSelection.setVisible(false);
                        updateFrame();
                    }
                });

                fouthRoom.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        currentChatRoom = "FOUR";
                        roomSelection.setVisible(false);
                        updateFrame();
                    }
                });
                
            }
        });      
        
        admin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                statusSelection.getContentPane().add(statusFrame);
                statusFrame.add(available);
                statusFrame.add(away);
                statusFrame.add(busy);
                statusSelection.setVisible(true);
                statusSelection.pack();
                
                available.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            Status = "AVAILABLE";
                            String cmd = Status + myUsername;
                            cmd = Encrypt(cmd, myKey);
                            out.println(cmd);
                        } catch (                NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException | UnsupportedEncodingException ex) {
                            Logger.getLogger(Cli.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                });

                away.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            Status = "AWAY";
                            String cmd = Status + myUsername;
                            cmd = Encrypt(cmd, myKey);
                            out.println(cmd);
                        } catch (                NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException | UnsupportedEncodingException ex) {
                            Logger.getLogger(Cli.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                });

                busy.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            Status = "BUSY";
                            String cmd = Status + myUsername;
                            cmd = Encrypt(cmd, myKey);
                            out.println(cmd);
                        } catch (                NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException | UnsupportedEncodingException ex) {
                            Logger.getLogger(Cli.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                });
                
            }
        }); 
        
        status.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    saveToFile( "saved.txt", messageArea);
                } catch (Exception ex) {}
            }
        });        
        
        privateMessage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    final String connectTo = JOptionPane.showInputDialog(frame, "Enter user to chat with", "PM", JOptionPane.PLAIN_MESSAGE);
                    String cmd = "NEWPM" + connectTo;
                    cmd = Encrypt(cmd, myKey);
                    if (PM_1 == null)
                        PM_1 = connectTo;
                    else if (PM_2 == null)
                        PM_2 = connectTo;
                    else if (PM_3 == null)
                        PM_3 = connectTo;
                    else if (PM_4 == null)
                        PM_4 = connectTo;
                    else if (PM_5 == null)
                        PM_5 = connectTo;
                    else if (PM_6 == null)
                        PM_6 = connectTo;
                    else if (PM_7 == null)
                        PM_7 = connectTo;
                    else if (PM_8 == null)
                        PM_8 = connectTo;
                    else if (PM_9 == null)
                        PM_9 = connectTo;
                    else if (PM_10 == null)
                        PM_10 = connectTo;
                    if ( inUse_1 == false) {
                        privateFrame1.setVisible(true);
                        out.println(cmd);
                        inUse_1 = true;
                        PM_1 = connectTo;
                        privateFrame1.setTitle("You are "+ myUsername + " and you are chatting with  " + connectTo);
                    }
                    else if ( inUse_2 == false) {
                        privateFrame2.setVisible(true);
                        out.println(cmd);
                        inUse_2 = true;
                        PM_2 = connectTo;
                        privateFrame2.setTitle("You are "+ myUsername + " and you are chatting with  " + connectTo);
                    }
                    else if ( inUse_3 == false) {
                        privateFrame3.setVisible(true);
                        out.println(cmd);
                        inUse_3 = true;
                        PM_3 = connectTo;
                        privateFrame3.setTitle("You are "+ myUsername + " and you are chatting with  " + connectTo);
                    }
                    else if ( inUse_4 == false) {
                        privateFrame4.setVisible(true);
                        out.println(cmd);
                        inUse_4 = true;
                        PM_4 = connectTo;
                        privateFrame4.setTitle("You are "+ myUsername + " and you are chatting with  " + connectTo);
                    }
                    else if ( inUse_5 == false) {
                        privateFrame5.setVisible(true);
                        out.println(cmd);
                        inUse_5 = true;
                        PM_5 = connectTo;
                        privateFrame5.setTitle("You are "+ myUsername + " and you are chatting with  " + connectTo);
                    }
                    else if ( inUse_6 == false) {
                        privateFrame6.setVisible(true);
                        out.println(cmd);
                        inUse_6 = true;
                        PM_6 = connectTo;
                        privateFrame6.setTitle("You are "+ myUsername + " and you are chatting with  " + connectTo);
                    }
                    else if ( inUse_7 == false) {
                        privateFrame7.setVisible(true);
                        out.println(cmd);
                        inUse_7 = true;
                        PM_7 = connectTo;
                        privateFrame7.setTitle("You are "+ myUsername + " and you are chatting with  " + connectTo);
                    }
                    else if ( inUse_8 == false) {
                        privateFrame8.setVisible(true);
                        out.println(cmd);
                        inUse_8 = true;
                        PM_8 = connectTo;
                        privateFrame8.setTitle("You are "+ myUsername + " and you are chatting with  " + connectTo);
                    }
                    else if ( inUse_9 == false) {
                        privateFrame9.setVisible(true);
                        out.println(cmd);
                        inUse_9 = true;
                        PM_9 = connectTo;
                        privateFrame9.setTitle("You are "+ myUsername + " and you are chatting with  " + connectTo);
                    }
                    else if ( inUse_10 == false) {
                        privateFrame10.setVisible(true);
                        out.println(cmd);
                        inUse_10 = true;
                        PM_10 = connectTo;
                        privateFrame10.setTitle("You are "+ myUsername + " and you are chatting with  " + connectTo);
                    }
                } catch (        NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException | UnsupportedEncodingException ex) {
                    Logger.getLogger(Cli.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }); 
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
    
    private void Disconnect(String line){
        if (line.equals(myUsername)){
            frame.dispose();
        }
        for ( clients u : Users){
            if ( u.uName.equals(line) ){
                u.uStatus = null;
                u.uName = null;
            }
        }
        users.setText("");
        for ( clients re : Users ){
            if ( re.uName != null)
                users.append( re.uName + " (" + re.uStatus + ") \n");
        }        
    }    
    
    public void saveToFile(String fileName, JTextArea textField) throws Exception {
        try (FileOutputStream outFile = new FileOutputStream(fileName, true)) {
            outFile.write(textField.getText().getBytes());
            outFile.close();
        }
    }   
    
    public synchronized void play(final String fileName) 
    {         
        final File file = new File(fileName);
        new Thread(new Runnable() { 
            @Override
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

    private void updateFrame(){
        messageArea.setText("");
        panel.removeAll();
    }
    
    private String getServerAddress() {
        return JOptionPane.showInputDialog(frame, "Enter IP Address of the Server:", "Welcome to the Chatter", JOptionPane.QUESTION_MESSAGE);
    }

    private String getName() {
        return JOptionPane.showInputDialog(frame, "Choose a screen name:", "Screen name selection", JOptionPane.PLAIN_MESSAGE);
    }
    
    private String getPass() {
        return JOptionPane.showInputDialog(frame, "Enter Password", "Screen name selection", JOptionPane.PLAIN_MESSAGE);
    }

    private static String Encrypt(String plainText, String key) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
        SecretKeySpec keySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);

        byte[] encryptedTextBytes = cipher.doFinal(plainText.getBytes("UTF-8"));
        return com.sun.org.apache.xerces.internal.impl.dv.util.Base64.encode(encryptedTextBytes);
    }
    
    private static String Decrypt(String encryptedText, String key) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException  {
        SecretKeySpec keySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, keySpec);
        byte[] encryptedTextBytes = com.sun.org.apache.xerces.internal.impl.dv.util.Base64.decode(encryptedText);
        byte[] decryptedTextBytes = cipher.doFinal(encryptedTextBytes);

        return new String(decryptedTextBytes);
    }
    
    private void run(Socket socket) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, UnsupportedAudioFileException, LineUnavailableException {
        AudioInputStream audio = AudioSystem.getAudioInputStream(new File("boxing_bell.wav"));
        Clip clip = AudioSystem.getClip();
        clip.open(audio);
        
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
        outStream = socket.getOutputStream();
        dosStream = new DataOutputStream(outStream);
        inStream = socket.getInputStream();
        disStream = new DataInputStream(inStream);
        
        while (isRunning) {
            String line = in.readLine();
            String decryptedText = Decrypt(line, keyString);
            
            if (socket.isInputShutdown() ){
                messageArea.append("input connection dropped \n");
            }
            if (socket.isOutputShutdown()){
                messageArea.append("out connection dropped \n");
            }
            
            //if ( socket.isClosed() || !socket.isConnected() ){
            //    SocketAddress remoteaddr = new InetSocketAddress(serverIP, PORT);
            //    socket.connect(remoteaddr);
            //    socket.isInputShutdown();
            //    messageArea.append("Reset connection \n");
            //}
                
            if (decryptedText.startsWith("SUBMITNAME")) {
                //Handle Login
                loginFrame.getContentPane().add(loginButton, "West");
                loginFrame.getContentPane().add(signupButton, "East");
                loginFrame.setVisible(true);
                loginFrame.pack();
                
                //Login Button
                loginButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            String thisName = getName();
                            myUsername = thisName;
                            String thisPass = getPass();
                            String cmd = Encrypt("LOGIN", keyString);
                            out.println(cmd);
                            String user = Encrypt(myUsername, keyString);
                            out.println(user);
                            String pass = Encrypt(thisPass, keyString);
                            out.println(pass);
                        } catch (                NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException | UnsupportedEncodingException ex) {
                            Logger.getLogger(Cli.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                });
                
                //Signup Button
                signupButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            String thisName = getName();
                            myUsername = thisName;
                            String thisPass = getPass();
                            String cmd = Encrypt("SIGNUP", keyString);
                            out.println(cmd);
                            String user = Encrypt(myUsername, keyString);
                            out.println(user);
                            String pass = Encrypt(thisPass, keyString);
                            out.println(pass);
                        } catch (                NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException | UnsupportedEncodingException ex) {
                            Logger.getLogger(Cli.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                });
            }
            else if (decryptedText.startsWith(myUsername)) {
                String seed = decryptedText.substring(myUsername.length());
                myKey = seed;
            }
            else if (decryptedText.startsWith("NAMEACCEPTED")) {
                textField.setEditable(true);
                loginFrame.setVisible(false);
                loginFrame.dispose();
                frame.setTitle("Logged in to LetsTalk as " + myUsername);
            }
            else if (decryptedText.startsWith("KICK")) {
                String kick = decryptedText.substring(4);
                if (myUsername.equals(kick)){
                    Disconnect(kick);
                    socket.close();
                }
            }
            else if (decryptedText.startsWith("SERVER")) {
                String message = decryptedText.substring(6);
                messageArea.append("SERVER" + message + "\n");
                clip.setFramePosition(0);
                clip.start();
            }
            else if (decryptedText.equals("IMAGE")){
                System.out.println("got command \n");
                int len = disStream.readInt();
                byte[] data;
                if (len > 0){
                    clip.setFramePosition(0);
                    clip.start();
                    data = new byte[len];
                    disStream.readFully(data);
                    System.out.println("read data \n");
                    try (InputStream ins = new ByteArrayInputStream(data)) {
                        BufferedImage imBuff = ImageIO.read(ins);
                        imBuff = resize(imBuff, 200, 100);
                        panel.add (new JLabel (new ImageIcon (imBuff)));
                        ins.close();
                    }
                    data = null;
                }
            }
            else if (decryptedText.startsWith("PM")) {
                String to = decryptedText.substring(2);
                String from = in.readLine();
                from = Decrypt(from, keyString);
                if ( to.equals(myUsername) ){
                    if ( from.equals(PM_1) ){
                        clip.setFramePosition(0);
                        clip.start();
                        String msg = in.readLine();
                        msg = Decrypt(msg, keyString);
                        messageArea1.append(msg + "\n");
                    }
                    else if ( from.equals(PM_2) ){
                        clip.setFramePosition(0);
                        clip.start();
                        String msg = in.readLine();
                        msg = Decrypt(msg, keyString);
                        messageArea2.append(msg + "\n");
                    }
                    else if ( from.equals(PM_3) ){
                        clip.setFramePosition(0);
                        clip.start();
                        String msg = in.readLine();
                        msg = Decrypt(msg, keyString);
                        messageArea3.append(msg + "\n");
                    }
                    else if ( from.equals(PM_4) ){
                        clip.setFramePosition(0);
                        clip.start();
                        String msg = in.readLine();
                        msg = Decrypt(msg, keyString);
                        messageArea4.append(msg + "\n");
                    }
                    else if ( from.equals(PM_5) ){
                        clip.setFramePosition(0);
                        clip.start();
                        String msg = in.readLine();
                        msg = Decrypt(msg, keyString);
                        messageArea5.append(msg + "\n");
                    }
                    else if ( from.equals(PM_6) ){
                        clip.setFramePosition(0);
                        clip.start();
                        String msg = in.readLine();
                        msg = Decrypt(msg, keyString);
                        messageArea6.append(msg + "\n");
                    }
                    else if ( from.equals(PM_7) ){
                        clip.setFramePosition(0);
                        clip.start();
                        String msg = in.readLine();
                        msg = Decrypt(msg, keyString);
                        messageArea7.append(msg + "\n");
                    }
                    else if ( from.equals(PM_8) ){
                        clip.setFramePosition(0);
                        clip.start();
                        String msg = in.readLine();
                        msg = Decrypt(msg, keyString);
                        messageArea8.append(msg + "\n");
                    }
                    else if ( from.equals(PM_9) ){
                        clip.setFramePosition(0);
                        clip.start();
                        String msg = in.readLine();
                        msg = Decrypt(msg, keyString);
                        messageArea9.append(msg + "\n");
                    }
                    else if ( from.equals(PM_10) ){
                        clip.setFramePosition(0);
                        clip.start();
                        String msg = in.readLine();
                        msg = Decrypt(msg, keyString);
                        messageArea10.append(msg + "\n");
                    }
                }
            }
            
            else if (decryptedText.startsWith("NEWPM")){
                String to = decryptedText.substring(5);
                String from = in.readLine();
                from = Decrypt(from, keyString);
                if ( to.equals(myUsername) ){
                    if (PM_1 == null)
                        PM_1 = from;
                    else if (PM_2 == null)
                        PM_2 = from;
                    else if (PM_3 == null)
                        PM_3 = from;
                    else if (PM_4 == null)
                        PM_4 = from;
                    else if (PM_5 == null)
                        PM_5 = from;
                    else if (PM_6 == null)
                        PM_6 = from;
                    else if (PM_7 == null)
                        PM_7 = from;
                    else if (PM_8 == null)
                        PM_8 = from;
                    else if (PM_9 == null)
                        PM_9 = from;
                    else if (PM_10 == null)
                        PM_10 = from;
                }
                if ( to.equals(myUsername) ){
                    if ( inUse_1 == false) {
                        privateFrame1.setVisible(true);
                        inUse_1 = true;
                        PM_1 = from;
                        clip.setFramePosition(0);
                        clip.start();
                        privateFrame1.setTitle("You are "+ myUsername + " and you are chatting with  " + from);
                    }
                    else if ( inUse_2 == false) {
                        privateFrame2.setVisible(true);
                        inUse_2 = true;
                        PM_2 = from;
                        clip.setFramePosition(0);
                        clip.start();
                        privateFrame2.setTitle("You are "+ myUsername + " and you are chatting with  " + from);
                    }
                    else if ( inUse_3 == false) {
                        privateFrame3.setVisible(true);
                        inUse_3 = true;
                        PM_3 = from;
                        clip.setFramePosition(0);
                        clip.start();
                        privateFrame3.setTitle("You are "+ myUsername + " and you are chatting with  " + from);
                    }
                    else if ( inUse_4 == false) {
                        privateFrame4.setVisible(true);
                        inUse_4 = true;
                        PM_4 = from;
                        clip.setFramePosition(0);
                        clip.start();
                        privateFrame4.setTitle("You are "+ myUsername + " and you are chatting with  " + from);
                    }
                    else if ( inUse_5 == false) {
                        privateFrame5.setVisible(true);
                        inUse_5 = true;
                        PM_5 = from;
                        clip.setFramePosition(0);
                        clip.start();
                        privateFrame5.setTitle("You are "+ myUsername + " and you are chatting with  " + from);
                    }
                    else if ( inUse_6 == false) {
                        privateFrame6.setVisible(true);
                        inUse_6 = true;
                        PM_6 = from;
                        clip.setFramePosition(0);
                        clip.start();
                        privateFrame6.setTitle("You are "+ myUsername + " and you are chatting with  " + from);
                    }
                    else if ( inUse_7 == false) {
                        privateFrame7.setVisible(true);
                        inUse_7 = true;
                        PM_7 = from;
                        clip.setFramePosition(0);
                        clip.start();
                        privateFrame7.setTitle("You are "+ myUsername + " and you are chatting with  " + from);
                    }
                    else if ( inUse_8 == false) {
                        privateFrame8.setVisible(true);
                        inUse_8 = true;
                        PM_8 = from;
                        clip.setFramePosition(0);
                        clip.start();
                        privateFrame8.setTitle("You are "+ myUsername + " and you are chatting with  " + from);
                    }
                    else if ( inUse_9 == false) {
                        privateFrame9.setVisible(true);
                        inUse_9 = true;
                        PM_9 = from;
                        clip.setFramePosition(0);
                        clip.start();
                        privateFrame9.setTitle("You are "+ myUsername + " and you are chatting with  " + from);
                    }
                    else if ( inUse_10 == false) {
                        privateFrame10.setVisible(true);
                        inUse_10 = true;
                        PM_10 = from;
                        clip.setFramePosition(0);
                        clip.start();
                        privateFrame10.setTitle("You are "+ myUsername + " and you are chatting with  " + from);
                    }
                }
            }
            else if (decryptedText.startsWith("GROUPONE")) {
                String message = decryptedText.substring(8);
                if ( currentChatRoom.equals("ONE")){
                    messageArea.append(message);
                    clip.setFramePosition(0);
                    clip.start();
                }
            }
            else if (decryptedText.startsWith("GROUPTWO")) {
                String message = decryptedText.substring(8);
                if ( currentChatRoom.equals("TWO")){
                    messageArea.append(message);
                    clip.setFramePosition(0);
                    clip.start();
                }
            }
            else if (decryptedText.startsWith("GROUPTHREE")) {
                String message = decryptedText.substring(10);
                if ( currentChatRoom.equals("THREE")){
                    messageArea.append(message);
                    clip.setFramePosition(0);
                    clip.start();
                }
            }
            else if (decryptedText.startsWith("GROUPFOUR")) {
                String message = decryptedText.substring(9);
                if ( currentChatRoom.equals("FOUR")){
                    messageArea.append(message);
                    clip.setFramePosition(0);
                    clip.start();
                }
            }
            else if (decryptedText.startsWith("RESET")){
                names = null;
                names = new HashSet<>();
                users.setText("");
            }
            else if (decryptedText.equals("DISCONNECT")) {
               // String name = decryptedText.substring(10);
                //name = Decrypt(name, keyString);
                //Disconnect(name);
            }
            else if (decryptedText.startsWith("UPDATEUSER")) {
                String name = decryptedText.substring(10);
                boolean add = true;
                if (Users.isEmpty())
                    Users.add( new clients(name, "AVAILABLE") );
                for ( clients u : Users){
                    if ( u.uName.equals(name) )
                        add = false;
                }
                if ( add == true )
                    Users.add( new clients(name, "AVAILABLE") );
                users.setText("");
                for ( clients re : Users ){
                    if ( re.uName != null)
                        users.append( re.uName + " (" + re.uStatus + ") \n");
                }
            }
            else if (decryptedText.startsWith("AWAY")) {
                String name = decryptedText.substring(4);
                for ( clients u : Users){
                    if ( u.uName.equals(name) )
                        u.uStatus = "AWAY";
                }
                users.setText("");
                for ( clients re : Users ){
                    if ( re.uName != null)
                        users.append( re.uName + " (" + re.uStatus + ") \n");
                }
            }
            else if (decryptedText.startsWith("BUSY")) {
                String name = decryptedText.substring(4);
                for ( clients u : Users){
                    if ( u.uName.equals(name) )
                        u.uStatus = "BUSY";
                }
                users.setText("");
                for ( clients re : Users ){
                    if ( re.uName != null)
                        users.append( re.uName + " (" + re.uStatus + ") \n");
                }
            }
            else if (decryptedText.startsWith("AVAILABLE")) {
                String name = decryptedText.substring(9);
                for ( clients u : Users){
                    if ( u.uName.equals(name) )
                        u.uStatus = "AVAILABLE";
                }
                users.setText("");
                for ( clients re : Users ){
                    if ( re.uName != null)
                        users.append( re.uName + " (" + re.uStatus + ") \n");
                }
            }
            else if (decryptedText.startsWith("DELETEUSER")) {
                String name = decryptedText.substring(10);
                for ( clients u : Users){
                    if ( u.uName.equals(name) ){
                        u.uName = null;
                        u.uStatus = null;
                    }
                }
                users.setText("");
                for ( clients re : Users ){
                    if ( re.uName != null)
                        users.append( re.uName + " (" + re.uStatus + ") \n");
                }
            }
        }
        //socket.close();
    }

    public static void main(String[] args) throws Exception {
        String serverAddress = JOptionPane.showInputDialog(frame, "Enter IP Address of the Server:", "Welcome to the Chatter", JOptionPane.QUESTION_MESSAGE);
        serverIP = serverAddress;
        Socket socket = new Socket(serverAddress, PORT);
        Cli client = new Cli(socket);
        client.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        client.frame.setVisible(true);
        client.run(socket);
    }
}
