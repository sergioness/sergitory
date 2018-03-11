import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.*;
import java.net.*;

public class ClientChat {

    private static int user;
    private JTextField messageField;
    private JTextArea chatArea;
    private PrintWriter writer;
    private BufferedReader reader;
    private Socket socket;
    private String nickname = "user"+user;

    private int PORT = 4242;
    private String IP = "127.0.0.1";

    ClientChat(){
        user++;
    }

    public static void main (String args[]){
        new ClientChat().go();
    }

    private void go() {
        setupConnection();
        new Thread(new Runnable() {
            @Override
            public void run() {
                String message;
                try{
                    while( (message = reader.readLine()) != null ){
                        System.out.println("[read] "+message);
                        chatArea.append(message+"\n");
                    }
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }).start();
        setupGUI();
    }

    private void setupGUI(){
        JFrame frame = new JFrame("Chat client");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel();
        messageField = new JTextField(20);
        chatArea = new JTextArea(15,50);
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);
        chatArea.setEditable(false);
        JScrollPane scroller = new JScrollPane(chatArea);
        scroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        JButton send = new JButton("Send");
        send.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    writer.println(nickname+": "+messageField.getText());
                    writer.flush();
                } catch(Exception ex){
                    ex.printStackTrace();
                }
                messageField.setText("");
                messageField.requestFocus();
            }
        });
//        panel.add(messageField);
//        panel.add(scroller);
//        panel.add(send);
//        frame.getContentPane().add(BorderLayout.CENTER,panel);
        panel.add(messageField);
        panel.add(send);
        frame.getContentPane().add(BorderLayout.SOUTH,panel);

        JPanel another = new JPanel();
        another.add(scroller);
        another.setBorder(BorderFactory.createEmptyBorder(5,280,10,10));
        frame.getContentPane().add(BorderLayout.CENTER,another);

        JTextField nameField = new JTextField();
        nameField.setSize(20,5);
        JButton setName = new JButton("Set name");
        setName.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(nameField.getText() != null)
                    nickname = nameField.getText();
            }
        });
        Box nameBox = new Box(BoxLayout.Y_AXIS);
        nameBox.add(nameField);
        nameBox.add(setName);
        JPanel namePanel = new JPanel(new BorderLayout());
        namePanel.setBorder(BorderFactory.createEmptyBorder(75,10,100,10));
        namePanel.add(BorderLayout.CENTER,nameBox);
        frame.getContentPane().add(BorderLayout.WEST,namePanel);

        frame.setSize(400,300);
        frame.setVisible(true);
    }

    private void setupConnection() {
        try{
            socket = new Socket(IP,PORT);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream());
            System.out.println("successfully connected");
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
