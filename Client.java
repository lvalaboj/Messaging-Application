import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.Timer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Client extends JComponent implements Runnable {
    private JButton signUp;
    private JButton logIn;
    private JButton newsign;
    private JButton toProfile;
    private JButton editProfile;
    private JButton viewMessages;
    private JButton logout;
    private JButton deleteAccount;
    private JButton changePass;
    private JButton home;
    private JButton save;
    private JButton requirement;
    private JButton create;
    private JButton note;
    private JButton viewChat;
    private JButton deleteChat;
    private JButton back;
    private ArrayList<JLabel> panels = new ArrayList<JLabel>();
    
    private JPanel panel;
    private JLabel validUser;
    
    private JFrame f = new JFrame("Messaging Application");
    private JFrame login = new JFrame("Messaging Application");
    private JFrame signup = new JFrame("Messaging Application");
    private JFrame profilePage;
    private JFrame edit;
    private JFrame pass;
    private JFrame view;
    private JFrame chat;
    private JFrame mesOption;
    private JFrame vwChat;
    
    private Socket socket;
    private ObjectOutputStream writer;
    private ObjectInputStream reader;
    private String username;
    
    private Timer chatTimer;
    private ChatUpdater chatUpdater;
    
    private Timer messageTimer;
    private MessageUpdater messageUpdater;
    
    public void run() {
        int count = 1;
        if (count == 1) {
            try {
                InetSocketAddress sA = new InetSocketAddress("localhost", 4242);
                socket = new Socket();
                socket.connect(sA, 2000);
                count = 2;
            } catch (ConnectException e) {
                JOptionPane.showMessageDialog(null, "Connection is not established", "Messaging",
                        JOptionPane.ERROR_MESSAGE);
            } catch (UnknownHostException e) {
                JOptionPane.showMessageDialog(null, "Connection is not established", "Messaging",
                        JOptionPane.ERROR_MESSAGE);
            } catch (IllegalArgumentException e) {
                JOptionPane.showMessageDialog(null, "Connection is not established", "Messaging",
                        JOptionPane.ERROR_MESSAGE);
            } catch (NoRouteToHostException e) {
                JOptionPane.showMessageDialog(null, "Connection is not established", "Messaging",
                        JOptionPane.ERROR_MESSAGE);
            } catch (SocketTimeoutException e) {
                JOptionPane.showMessageDialog(null, "Connection is not established", "Messaging",
                        JOptionPane.ERROR_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Connection is not established", "Messaging",
                        JOptionPane.ERROR_MESSAGE);
            }
            
        }
        if (count == 2) {
            welcome();
            try {
                writer = new ObjectOutputStream(socket.getOutputStream());
                reader = new ObjectInputStream(socket.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    public void welcome() {
        //Welcome page
        panel = new JPanel();
        panel.setLayout(new FlowLayout());
        JLabel label = new JLabel("Welcome");
        label.setFont(new Font("Ariel", Font.PLAIN, 25));
        label.setForeground(Color.BLUE);
        
        signUp = new JButton("Sign up");
        signUp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                f.dispose();
                signUp();
            }
        });
        
        logIn = new JButton("Log in");
        logIn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                f.dispose();
                logIn();
            }
        });
        panel.add(label);
        panel.add(signUp);
        panel.add(logIn);
        f.add(panel);
        
        panel.setLayout(null);
        
        label.setBounds(145, 50, 120, 50);
        signUp.setBounds(140, 100, 120, 50);
        logIn.setBounds(140, 150, 120, 50);
        
        f.setSize(400, 400);
        f.setLocationRelativeTo(null);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
    }
    
    public void signUp() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        JLabel label = new JLabel("Sign Up");
        label.setFont(new Font("Ariel", Font.PLAIN, 25));
        label.setForeground(Color.BLUE);
        
        JLabel username = new JLabel("Set a Username:");
        JLabel pass = new JLabel("Set a Password:");
        
        JTextField user = new JTextField("", 10);
        JPasswordField password = new JPasswordField("", 10);
        
        newsign = new JButton("Sign Up");
        newsign.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = user.getText();
                String pass = password.getText();
                String userCheck = null;
                String passCheck = null;
                try {
                    writer.writeObject("new user");
                    writer.writeObject(username);
                    writer.writeObject(pass);
                    writer.flush();
                    passCheck = (String) reader.readObject();
                } catch (IOException | ClassNotFoundException ioException) {
                    ioException.printStackTrace();
                }
                
                if (passCheck.equals("This Username Already Exist!")) {
                    JOptionPane.showMessageDialog(null, "This Username Already Exist!", "Messaging", JOptionPane.PLAIN_MESSAGE);
                } else if (passCheck.equals("Username can't have spaces")) {
                    JOptionPane.showMessageDialog(null, "Username can't have spaces", "Messaging", JOptionPane.PLAIN_MESSAGE);
                } else if (passCheck.equals("Password Doesn't Match Requirements")) {
                    JOptionPane.showMessageDialog(null, "Password Doesn't Match Requirements", "Messaging", JOptionPane.PLAIN_MESSAGE);
                } else if (passCheck.equals("Password Doesn't Match Requirements and Username Already Exists")) {
                    JOptionPane.showMessageDialog(null, "Password Doesn't Match Requirements and Username Already Exists", "Messaging", JOptionPane.PLAIN_MESSAGE);
                } else if (passCheck.equals("This is a deleted username which is registered in the application!")) {
                    JOptionPane.showMessageDialog(null, "This is a deleted username which is registered in the application!", "Messaging", JOptionPane.PLAIN_MESSAGE);
                } else if (passCheck.equals("accepted")) {
                    user.setText("");
                    password.setText("");
                    signup.dispose();
                    int yesno = JOptionPane.showConfirmDialog(null, "Would you like to log in?", "Messaging", JOptionPane.YES_NO_OPTION);
                    if (yesno == 1) {
                        signup.dispose();
                        welcome();
                    } else {
                        signup.dispose();
                        logIn();
                    }
                }
            }
        });
        
        requirement = new JButton("Requirement");
        requirement.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Password Requirements: " +
                        "  Upper Case \n" +
                        "  Number \n" +
                        "  Lower Case \n" +
                        "  8 Characters \n" +
                        "  No Spaces", "Messaging", JOptionPane.PLAIN_MESSAGE);
            }
        });
        
        back = new JButton("< Back");
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                user.setText("");
                password.setText("");
                signup.dispose();
                f.show();
            }
        });
        
        validUser = new JLabel("");
        validUser.setForeground(Color.red);
        validUser.setSize(500, 25);
        validUser.setLocation(142, 240);
        
        panel.add(validUser);
        panel.add(label);
        panel.add(newsign);
        panel.add(user);
        panel.add(pass);
        panel.add(username);
        panel.add(password);
        panel.add(back);
        panel.add(requirement);
        
        signup.add(panel);
        
        panel.setLayout(null);
        
        label.setBounds(150, 50, 120, 50);
        newsign.setBounds(112, 190, 180, 30);
        username.setBounds(90, 100, 200, 20);
        pass.setBounds(93, 150, 200, 20);
        user.setBounds(190, 100, 100, 20);
        password.setBounds(190, 150, 100, 20);
        back.setBounds(300, 5, 100, 30);
        requirement.setBounds(140, 250, 120, 40);
        
        signup.setSize(400, 400);
        signup.setLocationRelativeTo(null);
        signup.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        signup.setVisible(true);
    }
    
    public void logIn() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        JLabel label = new JLabel("Log in");
        label.setFont(new Font("Ariel", Font.PLAIN, 25));
        label.setForeground(Color.BLUE);
        
        JLabel username = new JLabel("Username:");
        JLabel pass = new JLabel("Password:");
        
        JTextField user = new JTextField(10);
        JPasswordField password = new JPasswordField(10);
        
        toProfile = new JButton("Log In");
        toProfile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = user.getText();
                String pass = password.getText();
                String check = null;
                try {
                    writer.writeObject("Log-in");
                    writer.writeObject(username);
                    writer.writeObject(pass);
                    check = (String) reader.readObject();
                } catch (IOException | ClassNotFoundException ioException) {
                    ioException.printStackTrace();
                }
                if (check.equals("wrong password")) {
                    JOptionPane.showMessageDialog(null, "You have entered the wrong password!", "Messaging", JOptionPane.PLAIN_MESSAGE);
                } else if (check.equals("user doesn't exist")) {
                    JOptionPane.showMessageDialog(null, "This username doesn't exist!", "Messaging", JOptionPane.PLAIN_MESSAGE);
                } else {
                    user.setText("");
                    password.setText("");
                    login.dispose();
                    onLogIn(username);
                    profilePage();
                }
            }
        });
        
        back = new JButton("< Back");
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                user.setText("");
                password.setText("");
                login.dispose();
                welcome();
            }
        });
        
        validUser = new JLabel("");
        validUser.setForeground(Color.red);
        validUser.setSize(500, 25);
        validUser.setLocation(142, 240);
        
        panel.add(validUser);
        panel.add(back);
        panel.add(label);
        panel.add(toProfile);
        panel.add(user);
        panel.add(pass);
        panel.add(username);
        panel.add(password);
        
        login.add(panel);
        
        panel.setLayout(null);
        
        label.setBounds(165, 50, 120, 50);
        toProfile.setBounds(112, 190, 180, 30);
        username.setBounds(110, 100, 200, 20);
        pass.setBounds(114, 150, 200, 20);
        user.setBounds(190, 100, 100, 20);
        password.setBounds(190, 150, 100, 20);
        back.setBounds(300, 5, 100, 30);
        
        login.setSize(400, 400);
        login.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        login.setLocationRelativeTo(null);
        login.setVisible(true);
    }
    
    public void onLogIn(String username) {
        this.username = username;
        
        profilePage = new JFrame("Logged in as " + username);
        edit = new JFrame("Logged in as " + username);
        pass = new JFrame("Logged in as " + username);
        view = new JFrame("Logged in as " + username);
        chat = new JFrame("Logged in as " + username);
        mesOption = new JFrame("Logged in as " + username);
        vwChat = new JFrame("Logged in as " + username);
    }
    
    public void profilePage() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        JLabel label = new JLabel("Your Profile");
        label.setFont(new Font("Ariel", Font.PLAIN, 20));
        label.setForeground(Color.BLUE);
        
        editProfile = new JButton("Edit Profile");
        editProfile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                profilePage.dispose();
                editProfile();
            }
        });
        
        viewMessages = new JButton("View Messages");
        viewMessages.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                profilePage.dispose();
                viewMessages();
            }
        });
        
        logout = new JButton("Log out");
        logout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                profilePage.dispose();
                JOptionPane.showMessageDialog(null, "Logged Out!", "Messaging", JOptionPane.PLAIN_MESSAGE);
                welcome();
            }
        });
        
        panel.add(label);
        panel.add(editProfile);
        panel.add(viewMessages);
        panel.add(logout);
        profilePage.add(panel);
        
        panel.setLayout(null);
        
        label.setBounds(135, 50, 120, 50);
        editProfile.setBounds(120, 100, 150, 50);
        viewMessages.setBounds(120, 150, 150, 50);
        logout.setBounds(120, 200, 150, 50);
        
        profilePage.setSize(400, 400);
        profilePage.setLocationRelativeTo(null);
        profilePage.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        profilePage.setVisible(true);
        
        try {
            writer.writeObject("has unread");
            if ((boolean) reader.readObject()) {
                int option = JOptionPane.showOptionDialog(null, "You have new unread messages!",
                        "New Messages", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE,
                        null, new Object[]{"Go to Messages", "Okay"}, null);
                
                if (option == 0) {
                    profilePage.dispose();
                    profilePage.remove(panel);
                    viewMessages();
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    public void editProfile() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        JLabel label = new JLabel("Edit Profile");
        label.setFont(new Font("Ariel", Font.PLAIN, 21));
        label.setForeground(Color.BLUE);
        
        deleteAccount = new JButton("Delete Account");
        deleteAccount.addActionListener(new ActionListener() {
            int yesno = -1;
            
            @Override
            public void actionPerformed(ActionEvent e) {
                yesno = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete your account?", "Messaging",
                        JOptionPane.YES_NO_OPTION);
                try {
                    if (yesno == 0) {
                        writer.writeObject("delete user");
                        JOptionPane.showMessageDialog(null, "Account Deleted!", "Messaging", JOptionPane.PLAIN_MESSAGE);
                        edit.dispose();
                        welcome();
                    } else {
                        editProfile();
                    }
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                
            }
        });
        
        changePass = new JButton("Change Password");
        changePass.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                edit.dispose();
                changePass();
            }
        });
        
        home = new JButton("Home");
        home.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                edit.dispose();
                profilePage();
            }
        });
        
        panel.add(label);
        panel.add(changePass);
        panel.add(deleteAccount);
        panel.add(home);
        edit.add(panel);
        
        panel.setLayout(null);
        
        label.setBounds(140, 50, 120, 50);
        changePass.setBounds(120, 100, 150, 50);
        deleteAccount.setBounds(120, 150, 150, 50);
        home.setBounds(120, 200, 150, 50);
        
        edit.setSize(400, 400);
        edit.setLocationRelativeTo(null);
        edit.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        edit.setVisible(true);
    }
    
    public void changePass() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        JLabel label = new JLabel("Change Password");
        label.setFont(new Font("Ariel", Font.PLAIN, 13));
        label.setForeground(Color.BLUE);
        
        JLabel passw = new JLabel("New Password:");
        
        JPasswordField newPass = new JPasswordField(10);
        
        save = new JButton("Save");
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String password = newPass.getText();
                String check = null;
                try {
                    writer.writeObject("change pass");
                    writer.writeObject(password);
                    check = (String) reader.readObject();
                    
                } catch (IOException | ClassNotFoundException ioException) {
                    ioException.printStackTrace();
                }
                if (check.equals("y")) {
                    pass.dispose();
                    editProfile();
                } else if (check.equals("same")) {
                    JOptionPane.showMessageDialog(null, "This was your old password ", "Messaging", JOptionPane.PLAIN_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Password Did Not Meet Requirements", "Messaging", JOptionPane.PLAIN_MESSAGE);
                }
            }
        });
        
        back = new JButton("< Back");
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pass.dispose();
                editProfile();
            }
        });
        
        requirement = new JButton("Requirement");
        requirement.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Password Requirements: " +
                        "  Upper Case \n" +
                        "  Number \n" +
                        "  Lower Case \n" +
                        "  8 Characters \n" +
                        "  No Spaces", "Messaging", JOptionPane.PLAIN_MESSAGE);
            }
        });
        
        panel.add(save);
        panel.add(back);
        panel.add(label);
        panel.add(passw);
        panel.add(requirement);
        panel.add(newPass);
        pass.add(panel);
        
        panel.setLayout(null);
        
        label.setBounds(140, 50, 120, 50);
        newPass.setBounds(140, 110, 200, 20);
        passw.setBounds(45, 110, 100, 20);
        save.setBounds(120, 150, 150, 40);
        back.setBounds(300, 5, 100, 30);
        requirement.setBounds(140, 250, 120, 40);
        
        pass.setSize(400, 400);
        pass.setLocationRelativeTo(null);
        pass.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        pass.setVisible(true);
    }
    
    public void viewMessages() {
        JLabel label = new JLabel("Messages");
        label.setFont(new Font("Ariel", Font.PLAIN, 25));
        label.setForeground(Color.BLUE);
        
        ArrayList<String> chatNames = new ArrayList<String>();
        
        JList list = new JList(chatNames.toArray());
        
        messageTimer = new Timer();
        messageUpdater = new MessageUpdater(list, chatNames);
        messageTimer.schedule(messageUpdater, 1000, 3000);
        messageUpdater.update();
        
        JScrollPane pane = new JScrollPane(list,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        pane.setPreferredSize(new Dimension(400, 350));
        list.setFont(new Font("Ariel", Font.PLAIN, 20));
        
        JButton createChat = new JButton("Create chat");
        JButton backmess = new JButton("< Back");
        
        Object[] options = {"View Chat", "Delete Chat"};
        
        list.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int chatIndex = list.getSelectedIndex();
                
                if (chatIndex < 0 || chatIndex >= chatNames.size()) {
                    return;
                }
                
                int option = JOptionPane.showOptionDialog(null, "", "Messaging",
                        JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE,
                        null, options, null);
                if (option == 1) {
                    try {
                        writer.writeObject("delete chat");
                        writer.writeObject(chatIndex);
                        messageUpdater.update();
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                } else if (option == 0) {
                    try {
                        writer.writeObject("get chat id");
                        writer.writeObject(chatIndex);
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                    view.dispose();
                    messageUpdater.cancel();
                    messageTimer.cancel();
                    view.remove(createChat);
                    view.remove(label);
                    view.remove(pane);
                    view.remove(backmess);
                    viewChatWindow();
                }
            }
        });
        
        createChat.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                view.dispose();
                messageUpdater.cancel();
                messageTimer.cancel();
                view.remove(createChat);
                view.remove(label);
                view.remove(pane);
                view.remove(backmess);
                createChat();
            }
        });
        
        backmess.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                view.dispose();
                messageUpdater.cancel();
                messageTimer.cancel();
                profilePage();
                view.remove(createChat);
                view.remove(label);
                view.remove(pane);
                view.remove(backmess);
            }
        });
        
        view.add(createChat, BorderLayout.SOUTH);
        view.add(label, BorderLayout.NORTH);
        view.add(pane);
        view.add(backmess, BorderLayout.EAST);
        //view.add(messOptions, BorderLayout.WEST);
        
        list.setSize(400, 300);
        list.setBounds(0, 100, 400, 300);
        
        view.setSize(400, 400);
        view.setLocationRelativeTo(null);
        view.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        view.setVisible(true);
    }
    
    public void viewChatWindow() {
        int chatId = -1;
        try {
            chatId = (int) reader.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        String name = null;
        try {
            name = (String) reader.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        
        JLabel label = new JLabel(name);
        label.setFont(new Font("Ariel", Font.PLAIN, 25));
        label.setForeground(Color.BLUE);
        
        ArrayList<String> messages = new ArrayList<String>();
        
        JList list = new JList(messages.toArray());
        
        JScrollPane pane = new JScrollPane(list,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        pane.setPreferredSize(new Dimension(400, 350));
        list.setFont(new Font("Ariel", Font.PLAIN, 20));
        
        chatTimer = new Timer();
        chatUpdater = new ChatUpdater(list, pane, messages);
        chatTimer.schedule(chatUpdater, 1000, 1000);
        
        chatUpdater.update();
        
        JTextField mess = new JTextField();
        JButton optionsButton = new JButton("Options");
        JButton backButton = new JButton("< Back");
        vwChat.add(mess, BorderLayout.SOUTH);
        vwChat.add(optionsButton, BorderLayout.NORTH);
        vwChat.add(backButton, BorderLayout.NORTH);
        vwChat.add(label, BorderLayout.NORTH);
        vwChat.add(pane, BorderLayout.CENTER);
        
        mess.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!mess.getText().equals("")) {
                    try {
                        writer.writeObject("send message");
                        writer.writeObject(mess.getText());
                        mess.setText("");
                        chatUpdater.update();
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
            }
        });
        
        Object[] delEdtOptions = {"Edit Message", "Delete Message"};
        
        try {
            writer.writeObject("get username");
            username = (String) reader.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        
        list.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int chatIndex = list.getSelectedIndex();
                
                if (chatIndex < 0 || chatIndex >= messages.size()) {
                    return;
                }
                
                if (!username.equals(messages.get(chatIndex).split(" ")[1])) {
                    JOptionPane.showMessageDialog(null, "You can only edit/delete your own messages");
                    return;
                }
                
                int option = JOptionPane.showOptionDialog(null, "", "Messaging",
                        JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE,
                        null, delEdtOptions, null);
                if (option == 0) {
                    try {
                        String newMessage = JOptionPane.showInputDialog("Enter new message"); // fill input dialog with old message
                        if (newMessage != null) {
                            System.out.println("Edited Message: " + newMessage);
                            writer.writeObject("edit message");
                            writer.writeObject(messages.get(chatIndex));
                            writer.writeObject(newMessage);
                            
                            chatUpdater.update();
                        }
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                } else if (option == 1) {
                    try {
                        writer.writeObject("delete message");
                        writer.writeObject(messages.get(chatIndex));
                        chatUpdater.update();
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
            }
        });
        
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                vwChat.dispose();
                
                vwChat.remove(backButton);
                vwChat.remove(label);
                vwChat.remove(pane);
                vwChat.remove(mess);
                vwChat.remove(optionsButton);
                
                chatUpdater.cancel();
                chatTimer.cancel();
                chatUpdater = null;
                chatTimer = null;
                viewMessages();
            }
        });
        
        Object[] optionMenuOptions = {"Export Chat", "Back"};
        optionsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int option = JOptionPane.showOptionDialog(null, "", "Options",
                        JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE,
                        null, optionMenuOptions, null);
                if (option == 0) {
                    JFileChooser fc = new JFileChooser();
                    FileNameExtensionFilter filter = new FileNameExtensionFilter("CSV File", "csv");
                    fc.setCurrentDirectory(new File("/"));
                    fc.addChoosableFileFilter(filter);
                    fc.removeChoosableFileFilter(fc.getAcceptAllFileFilter());
                    while (true) {
                        int val = fc.showSaveDialog(null);
                        
                        if (val == JFileChooser.APPROVE_OPTION) {
                            File selectedFile = fc.getSelectedFile();
                            String[] fileNameParts = selectedFile.getName().split("\\.");
                            int numPeriods = (int) selectedFile.getName().chars().filter(ch -> ch == 'e').count();
                            
                            if (numPeriods >= 2) {
                                JOptionPane.showMessageDialog(null, "Invalid file name");
                                continue;
                            } else if (fileNameParts.length >= 2 && !fileNameParts[1].equalsIgnoreCase("csv")) {
                                JOptionPane.showMessageDialog(null, "Invalid file type");
                                continue;
                            } else if (fileNameParts.length == 1) {
                                selectedFile = new File(selectedFile.getAbsolutePath() + ".csv");
                            }
                            System.out.println(selectedFile.getName());
                            
                            try {
                                writeCSV(selectedFile);
                                break;
                            } catch (Exception exception) {
                                JOptionPane.showMessageDialog(null, "Invalid file name or location");
                            }
                        } else {
                            break;
                        }
                    }
                }
            }
        });
        
        label.setBounds(10, 0, 120, 50);
        mess.setBounds(0, 350, 340, 30);
        optionsButton.setBounds(300, 4, 100, 30);
        backButton.setBounds(200, 4, 100, 30);
        list.setSize(400, 300);
        list.setBounds(0, 100, 400, 300);
        
        vwChat.setSize(400, 400);
        vwChat.setLocationRelativeTo(null);
        vwChat.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        vwChat.setVisible(true);
        
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JScrollBar scrollBar = pane.getVerticalScrollBar();
                scrollBar.setValue(scrollBar.getMaximum());
            }
        });
    }
    
    public void createChat() {
        JPanel panel = new JPanel();
        JLabel label = new JLabel("Create a Chat");
        label.setFont(new Font("Ariel", Font.PLAIN, 18));
        label.setForeground(Color.BLUE);
        
        
        JTextField chatnames = new JTextField();
        create = new JButton("Create");
        note = new JButton("How to Format");
        
        note.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "To create a group chat, separate usernames with spaces", "Messaging", JOptionPane.PLAIN_MESSAGE);
            }
        });
        
        create.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String users = chatnames.getText();
                String check = null;
                try {
                    writer.writeObject("create chat");
                    writer.writeObject(users);
                    
                    check = (String) reader.readObject();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                } catch (ClassNotFoundException classNotFoundException) {
                    classNotFoundException.printStackTrace();
                }
                
                if (check.equals("success")) {
                    chat.dispose();
                    JOptionPane.showMessageDialog(null, "Chat Created!", "Messaging", JOptionPane.PLAIN_MESSAGE);
                    viewMessages();
                } else if (check.equals("exists")) {
                    JOptionPane.showMessageDialog(null, "This chat already exists!", "Messaging", JOptionPane.PLAIN_MESSAGE);
                } else if (check.equals("same")) {
                    JOptionPane.showMessageDialog(null, "You cant create chat with yourself!", "Messaging", JOptionPane.PLAIN_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "The username/usernames you have entered don't have an account!", "Messaging", JOptionPane.PLAIN_MESSAGE);
                }
                
            }
        });
        
        back = new JButton("< Back");
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chat.dispose();
                viewMessages();
            }
        });
        
        panel.add(label);
        panel.add(chatnames);
        panel.add(create);
        panel.add(note);
        panel.add(back);
        chat.add(panel);
        
        panel.setLayout(null);
        
        label.setBounds(140, 50, 120, 50);
        note.setBounds(140, 100, 120, 40);
        chatnames.setBounds(100, 160, 200, 30);
        create.setBounds(140, 210, 120, 40);
        back.setBounds(300, 5, 100, 30);
        
        chat.setSize(400, 400);
        chat.setLocationRelativeTo(null);
        chat.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        chat.setVisible(true);
    }
    
    public void writeCSV(File dest)
            throws Exception {
        
        writer.writeObject("get members");
        String memberList = (String) reader.readObject();
        
        writer.writeObject("get messages");
        ArrayList<String> messageList = (ArrayList<String>) reader.readObject();
        
        if (!dest.exists()) {
            dest.createNewFile();
        }
        
        FileWriter fw = new FileWriter(dest, false);
        BufferedWriter bw = new BufferedWriter(fw);
        PrintWriter pw = new PrintWriter(bw);
        
        pw.println("Chat Members: " + memberList);
        
        ArrayList<String[]> formattedStrings = new ArrayList<>();
        for (String s : messageList) {
            String[] formattedString = s.split(" ", 2);
            formattedStrings.add(formattedString);
        }
        
        formattedStrings.stream()
                .map(this::convertToCSV)
                .forEach(pw::println);
        pw.flush();
        
        pw.close();
        bw.close();
        fw.close();
    }
    
    public String escapeSpecial(String input) {
        String newString = input.replaceAll("\\R", " ");
        if (input.contains(",") || input.contains("\"") || input.contains("'")) {
            input = input.replace("\"", "\"\"");
            newString = "\"" + input + "\"";
        }
        return newString;
    }
    
    public String convertToCSV(String[] data) {
        return Stream.of(data)
                .map(this::escapeSpecial)
                .collect(Collectors.joining(","));
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Client());
    }
    
    class MessageUpdater extends TimerTask {
        private JList list;
        private ArrayList<String> chatNames;
        
        public MessageUpdater(JList list, ArrayList<String> chatNames) {
            this.list = list;
            this.chatNames = chatNames;
        }
        
        @Override
        public void run() {
            update();
        }
        
        public void update() {
            String[] tempChats = null;
            try {
                writer.writeObject("view message");
                tempChats = (String[]) reader.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            
            chatNames.clear();
            chatNames.addAll(Arrays.asList(tempChats));
            
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    list.setListData(chatNames.toArray());
                }
            });
        }
    }
    
    class ChatUpdater extends TimerTask {
        private JList list;
        private ArrayList<String> messages;
        private JScrollPane scrollPane;
        
        public ChatUpdater(JList list, JScrollPane scrollPane, ArrayList<String> messages) {
            this.list = list;
            this.messages = messages;
            this.scrollPane = scrollPane;
        }
        
        @Override
        public void run() {
            update();
        }
        
        public void update() {
            ArrayList<String> tempMessages = null;
            try {
                writer.writeObject("get messages");
                tempMessages = (ArrayList<String>) reader.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            
            messages.clear();
            messages.addAll(tempMessages);
            
            /*for (String tempMessage : tempMessages) {
                System.out.print(tempMessage + ", ");
            }
            System.out.println();*/
            
            for (int i = 0; i < tempMessages.size(); i++) {
                String s = tempMessages.get(i);
                Scanner scanner = new Scanner(s);
                long messageTime = scanner.nextLong();
                String date = new java.text.SimpleDateFormat("HH:mm:ss").format(
                        new java.util.Date(messageTime));
                String userName = scanner.next();
                String message = scanner.nextLine();
                
                tempMessages.set(i, String.format("%s %s: %s%n", date, userName, message));
            }
            
            JScrollBar scrollBar = scrollPane.getVerticalScrollBar();
            boolean atBottom = scrollBar.getMaximum() - scrollBar.getValue() - scrollBar.getVisibleAmount() < 10;
            
            ArrayList<String> finalTempMessages = tempMessages;
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    list.setListData(finalTempMessages.toArray());
                }
            });
            if (atBottom) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        list.ensureIndexIsVisible(finalTempMessages.size() - 1);
                    }
                });
            }
        }
    }
}
