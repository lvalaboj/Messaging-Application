import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Account extends Thread {
    private static ArrayList<String> usernames = new ArrayList<String>();
    private static ArrayList<String> passwords = new ArrayList<String>();
    private static ArrayList<String> deletedUsers = new ArrayList<String>();
    private ArrayList<String> chats = new ArrayList<String>();
    private ArrayList<Integer> ids = new ArrayList<Integer>();
    private String password;
    private String username;
    private File userList;
    private File passList;
    private File chatList;
    private File idList;
    private File deletedUser;
    private Object accLock = new Object();
    
    public Account() {
        userList = new File("usernames.txt");
        passList = new File("passwords.txt");
        deletedUser = new File("deletedUsers.txt");
        try {
            userList.createNewFile();
            passList.createNewFile();
            deletedUser.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public boolean deletedUserCheck(String username) {
        for (int i = 0; i < deletedUsers.size(); i++) {
            if (deletedUsers.get(i).equals(username)) {
                return true;
            }
        }
        return false;
    }
    
    public boolean checkUsername(String username) {
        boolean hasSpaces = false;
        for (int i = 0; ((i < username.length()) && (!hasSpaces)); i++) {
            hasSpaces = ((username.charAt(i)) == ' ');
        }
        return (!hasSpaces);
    }
    
    public boolean checkPass(String pass) {
        if (pass.length() != 8) {
            return false;
        }
        
        String[] space = pass.split(" ");
        if (space.length > 1) {
            return false;
        }
        
        char[] chPass = pass.toCharArray();
        
        int lowerCount = 0;
        int upperCount = 0;
        int numCount = 0;
        
        for (int i = 0; i < chPass.length; i++) {
            try {
                String s = String.valueOf(chPass[i]);
                int num = Integer.parseInt(s);
                numCount++;
            } catch (NumberFormatException e) {
                if (chPass[i] >= 65 && chPass[i] <= 90) {
                    upperCount++;
                } else if (chPass[i] >= 97 && chPass[i] <= 122) {
                    lowerCount++;
                }
            }
        }
        
        if (lowerCount == 0 || upperCount == 0 || numCount == 0) {
            return false;
        }
        
        return true;
    }
    
    public void addUser(String user, String pass) {
        synchronized (accLock) {
            try {
                FileWriter fw = new FileWriter(userList, true);
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter pw = new PrintWriter(bw);
                pw.println(user);
                
                pw.flush();
                
                pw.close();
                bw.close();
                fw.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            try {
                FileWriter fw = new FileWriter(passList, true);
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter pw = new PrintWriter(bw);
                pw.println(pass);
                
                pw.flush();
                
                pw.close();
                bw.close();
                fw.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            startApplication();
        }
    }
    
    public void addToArray(File user) {
        synchronized (accLock) {
            FileReader fr = null;
            
            try {
                fr = new FileReader(user);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            
            BufferedReader bfr = new BufferedReader(fr);
            String line = null;
            
            try {
                line = bfr.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            while (line != null) {
                if (user == userList) {
                    usernames.add(line);
                } else if (user == passList) {
                    passwords.add(line);
                } else if (user == chatList) {
                    chats.add(line);
                } else if (user == deletedUser) {
                    deletedUsers.add(line);
                } else {
                    ids.add(Integer.parseInt(line));
                }
                try {
                    line = bfr.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                bfr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    public void setUser(String username) {
        synchronized (accLock) {
            chats.clear();
            ids.clear();
            this.username = username;
            if (username != null) {
                String FileName;
                
                FileName = username.concat(".txt");
                idList = new File(username.concat("ids.txt"));
                try {
                    idList.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                chatList = new File(FileName);
            }
            try {
                chatList.createNewFile();
                idList.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    public String getUser() {
        return username;
    }
    
    public String getPass() {
        int ind = usernames.indexOf(username);
        return passwords.get(ind);
    }
    
    public void startApplication() {
        usernames.clear();
        passwords.clear();
        addToArray(userList);
        addToArray(passList);
    }
    
    public boolean userExist(String user) {
        synchronized (accLock) {
            for (int i = 0; i < usernames.size(); i++) {
                if (usernames.get(i).equals(user)) {
                    password = passwords.get(i);
                    return true;
                }
            }
            return false;
        }
    }
    
    public boolean passExist(String pass) {
        synchronized (accLock) {
            if (pass.equals(password)) {
                return true;
            }
            return false;
        }
    }
    
    public void deleteUser() {
        synchronized (accLock) {
            int index = usernames.indexOf(username);
            usernames.remove(username);
            passwords.remove(index);
            clearList();
            updateList();
            
            try {
                FileWriter fw = new FileWriter(deletedUser, true);
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter pw = new PrintWriter(bw);
                pw.println(username);
                
                pw.flush();
                
                pw.close();
                bw.close();
                fw.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            addToArray(deletedUser);
        }
    }
    
    public void changePass(String newPass) {
        synchronized (accLock) {
            int index = usernames.indexOf(username);
            passwords.set(index, newPass);
            clearList();
            updateList();
        }
    }
    
    public void clearList() {
        synchronized (accLock) {
            PrintWriter writer = null;
            try {
                writer = new PrintWriter(userList);
                writer.print("");
                writer.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            
            try {
                writer = new PrintWriter(passList);
                writer.print("");
                writer.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            
            try {
                writer = new PrintWriter(chatList);
                writer.print("");
                writer.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            
            try {
                writer = new PrintWriter(idList);
                writer.print("");
                writer.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
    
    public void updateList() {
        synchronized (accLock) {
            for (int i = 0; i < usernames.size(); i++) {
                try {
                    FileWriter fw = new FileWriter(userList, true);
                    BufferedWriter bw = new BufferedWriter(fw);
                    PrintWriter pw = new PrintWriter(bw);
                    pw.println(usernames.get(i));
                    
                    pw.flush();
                    
                    pw.close();
                    bw.close();
                    fw.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            
            for (int i = 0; i < passwords.size(); i++) {
                try {
                    FileWriter fw = new FileWriter(passList, true);
                    BufferedWriter bw = new BufferedWriter(fw);
                    PrintWriter pw = new PrintWriter(bw);
                    pw.println(passwords.get(i));
                    
                    pw.flush();
                    
                    pw.close();
                    bw.close();
                    fw.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            
            for (int i = 0; i < chats.size(); i++) {
                try {
                    FileWriter fw = new FileWriter(chatList, true);
                    BufferedWriter bw = new BufferedWriter(fw);
                    PrintWriter pw = new PrintWriter(bw);
                    pw.println(chats.get(i));
                    
                    pw.flush();
                    
                    pw.close();
                    bw.close();
                    fw.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            
            for (int i = 0; i < ids.size(); i++) {
                try {
                    FileWriter fw = new FileWriter(idList, true);
                    BufferedWriter bw = new BufferedWriter(fw);
                    PrintWriter pw = new PrintWriter(bw);
                    pw.println(ids.get(i));
                    
                    pw.flush();
                    
                    pw.close();
                    bw.close();
                    fw.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    public void createChat(String username) {
        synchronized (accLock) {
            try {
                FileWriter fw = new FileWriter(chatList, true);
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter pw = new PrintWriter(bw);
                pw.println(username);
                
                pw.flush();
                
                pw.close();
                bw.close();
                fw.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            int check = checkChat(username);
            if (check != -1) {
                int id = getDeletedChat(check, username);
                writeId(id);
                startChatApplication();
                Messages.getMessage(id).addUser(this.username);
            } else {
                int id = getNewId();
                writeId(id);
                startChatApplication();
                addMember(username, id);
                Messages.getMessage(id).addUser(this.username);
            }
        }
    }
    
    public int checkChat(String username) {
        File userChat = new File(username.concat(".txt"));
        try {
            userChat.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        ArrayList<String> chatList = new ArrayList<String>();
        FileReader fr = null;
        
        try {
            fr = new FileReader(userChat);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        
        BufferedReader bfr = new BufferedReader(fr);
        String line = null;
        
        try {
            line = bfr.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (line != null) {
            chatList.add(line);
            try {
                line = bfr.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            bfr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        for (int i = 0; i < chatList.size(); i++) {
            if (chatList.get(i).equals(this.username)) {
                return i;
            }
        }
        return -1;
    }
    
    public int getDeletedChat(int index, String username) {
        File userChat = new File(username.concat("ids.txt"));
        try {
            userChat.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ArrayList<String> chatList = new ArrayList<String>();
        
        FileReader fr = null;
        
        try {
            fr = new FileReader(userChat);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        
        BufferedReader bfr = new BufferedReader(fr);
        String line = null;
        
        try {
            line = bfr.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (line != null) {
            chatList.add(line);
            try {
                line = bfr.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            bfr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        int id = Integer.parseInt(chatList.get(index));
        return id;
    }
    
    public void addMember(String usrName, int id) {
        synchronized (accLock) {
            try {
                String remoteUserFileName = usrName.concat(".txt");
                
                File remoteUser = new File(remoteUserFileName);
                
                try {
                    remoteUser.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                
                FileWriter fw = new FileWriter(remoteUser, true);
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter pw = new PrintWriter(bw);
                pw.println(this.username);
                
                pw.flush();
                
                pw.close();
                bw.close();
                fw.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            try {
                String remoteUserFileName = usrName.concat("ids.txt");
                
                File remoteUser = new File(remoteUserFileName);
                
                try {
                    remoteUser.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                
                FileWriter fw = new FileWriter(remoteUser, true);
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter pw = new PrintWriter(bw);
                pw.println(id);
                
                pw.flush();
                
                pw.close();
                bw.close();
                fw.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            Messages.getMessage(id).addUser(usrName);
        }
    }
    
    public int getNewId() {
        synchronized (accLock) {
            int newId;
            File testFile;
            
            do {
                newId = (int) Math.floor(Math.random() * 2147483647);
                testFile = new File(String.format("chats%d.txt", newId));
            } while (Files.exists(Paths.get(testFile.getPath())));
            
            new Messages(newId);
            return newId;
        }
    }
    
    public void writeId(int id) {
        synchronized (accLock) {
            try {
                FileWriter fw = new FileWriter(idList, true);
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter pw = new PrintWriter(bw);
                pw.println(id);
                
                pw.flush();
                
                pw.close();
                bw.close();
                fw.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    public String createGroup(String[] names) {
        synchronized (accLock) {
            try {
                FileWriter fw = new FileWriter(chatList, true);
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter pw = new PrintWriter(bw);
                if (names.length > 0) {
                    for (int i = 0; i < (names.length - 1); i++) {
                        pw.print(names[i] + ",");
                    }
                }
                pw.println(names[names.length - 1]);
                
                pw.flush();
                
                pw.close();
                bw.close();
                fw.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            String chatFile = names[0];
            
            for (int j = 1; j < names.length; j++) {
                chatFile = chatFile.concat(names[j]);
            }
            
            //chats.clear();
            //ids.clear();
            startChatApplication();
            int id = getNewId();
            writeId(id);
    
            Messages.getMessage(id).addUser(this.username);
            
            addGroup(names, id);
            return chatFile;
        }
    }
    
    public void addGroup(String[] usernames, int id) {
        synchronized (accLock) {
            try {
                for (int i = 0; i < usernames.length; i++) {
                    if (!usernames[i].equals(this.username)) {
                        String remoteUserFileName = usernames[i].concat(".txt");
                        File remoteUser = new File(remoteUserFileName);
                        FileWriter fw = new FileWriter(remoteUser, true);
                        BufferedWriter bw = new BufferedWriter(fw);
                        PrintWriter pw = new PrintWriter(bw);
                        
                        for (int k = 0; k < usernames.length; k++) {
                            if (!usernames[i].equals(usernames[k])) {
                                pw.print(usernames[k] + ",");
                            }
                        }
                        pw.println(this.username);
                        
                        
                        pw.flush();
                        
                        pw.close();
                        bw.close();
                        fw.close();
                    }
                    
                }
                
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            try {
                for (int i = 0; i < usernames.length; i++) {
                    if (!usernames[i].equals(this.username)) {
                        String remoteUserFileName = usernames[i].concat("ids.txt");
                        File remoteUser = new File(remoteUserFileName);
                        FileWriter fw = new FileWriter(remoteUser, true);
                        BufferedWriter bw = new BufferedWriter(fw);
                        PrintWriter pw = new PrintWriter(bw);
                        
                        pw.println(id);
                        
                        pw.flush();
                        
                        pw.close();
                        bw.close();
                        fw.close();
                    }
                }
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Messages msg = Messages.getMessage(id);
        for (String s : usernames) {
            msg.addUser(s);
        }
    }
    
    public boolean chatExists(String chatName) {
        for (int i = 0; i < chats.size(); i++) {
            if (chatName.equals(chats.get(i))) {
                return true;
            }
        }
        return false;
    }
    
    public ArrayList<String> getChats() {
        chats.clear();
        addToArray(chatList);
        return chats;
    }
    
    public void startChatApplication() {
        chats.clear();
        ids.clear();
        addToArray(chatList);
        addToArray(idList);
    }
    
    public boolean groupChatExists(String[] chatName) {
        int count = 0;
        for (int i = 0; i < chats.size(); i++) {
            for (int j = 0; j < chatName.length; j++) {
                if (chats.get(i).contains(chatName[j])) {
                    count++;
                }
            }
            if (count == chatName.length) {
                return true;
            } else {
                count = 0;
            }
        }
        return false;
    }
    
    public int getId(int index) {
        return ids.get(index);
    }
    
    public void deleteChat(int index) {
        synchronized (accLock) {
            chats.remove(index);
            ids.remove(index);
            clearList();
            updateList();
        }
    }
    
    public String getChatName(int index) {
        return chats.get(index);
    }
    
    public boolean hasNewMessages() {
        Messages msg;
        for (int id : ids) {
            msg = Messages.getMessage(id);
            if (msg.hasRead(username)) {
                return true;
            }
        }
        return false;
    }
}`
