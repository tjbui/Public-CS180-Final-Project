import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class client extends JComponent implements Runnable {

    private Image image; // the canvas
    private int curX; // current mouse x coordinate
    private int curY; // current mouse y coordinate
    private int oldX; // previous mouse x coordinate
    private int oldY; // previous mouse y coordinate

    JButton loginButton;
    JButton signInButton;
    JButton signOutButton;

    client client;

    ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {

            if (e.getSource() == loginButton) {
                login();
            }

            if (e.getSource() == signInButton) {
                signIn();
            }

            if (e.getSource() == signOutButton) {
                signOut();
            }

        }
    };

    public String[] login() {

        String email = JOptionPane.showInputDialog(null, "Enter Email: ",
                "Login", JOptionPane.QUESTION_MESSAGE);
        /*
        if (true) {  //TODO when email exists, ask for password
            String password = JOptionPane.showInputDialog(null, "Enter Password: ",
                    "Login", JOptionPane.QUESTION_MESSAGE);
        }
        */

        if ((email == null) || (email.length() == 0)) {
            return null;
        }

        String password = JOptionPane.showInputDialog(null, "Enter Password: ",
                "Login", JOptionPane.QUESTION_MESSAGE);

        String[] loginInfo = new String[2];
        loginInfo[0] = email;
        loginInfo[1] = password;

        return loginInfo;

    }

    public String[] signIn() {

        String email = JOptionPane.showInputDialog(null, "Enter Email: ",
                "Sign In", JOptionPane.QUESTION_MESSAGE);

        String password = JOptionPane.showInputDialog(null, "Enter Password: ",
                "Sign In", JOptionPane.QUESTION_MESSAGE);

        String[] signInInfo = new String[2];
        signInInfo[0] = email;
        signInInfo[1] = password;

        return signInInfo;

    }

    public void signOut() {


    }


    public client() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                /* set oldX and oldY coordinates to beginning mouse press*/
                oldX = e.getX();
                oldY = e.getY();

            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                /* set current coordinates to where mouse is being dragged*/
                curX = e.getX();
                curY = e.getY();

                /* draw the line between old coordinates and new ones */

                /* refresh frame and reset old coordinates */
                repaint();
                oldX = curX;
                oldY = curY;

            }
        });
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(new client());
    }

    public void run() {
        /* set up JFrame */
        JFrame frame = new JFrame("Project 5");
        Container content = frame.getContentPane();
        content.setLayout(new BorderLayout());
        client = new client();
        content.add(client, BorderLayout.CENTER);


        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        loginButton = new JButton("Login");
        signInButton = new JButton("Sign In");
        signOutButton = new JButton("Sign Out");

        JPanel loginPanel = new JPanel();;
        loginPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        loginPanel.add(loginButton);
        loginButton.addActionListener(actionListener);
        loginPanel.add(signInButton);
        signInButton.addActionListener(actionListener);
        loginPanel.add(signOutButton);
        signOutButton.addActionListener(actionListener);

        content.add(loginPanel, BorderLayout.NORTH);
    }

}
