import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class client implements ActionListener {

    JTextField t1;
    static JPanel a1;
    static Box vertical = Box.createVerticalBox();
    static JFrame f = new JFrame();
    static DataOutputStream otp;

    client() {
        f.setLayout(null);

        JPanel p = new JPanel();
        p.setBackground(Color.black);
        p.setBounds(0, 0, 450, 70);
        p.setLayout(null);
        f.add(p);

        ImageIcon il = new ImageIcon(ClassLoader.getSystemResource("left.png"));
        Image i2 = il.getImage().getScaledInstance(25, 25, Image.SCALE_DEFAULT);
        JLabel back = new JLabel(new ImageIcon(i2));
        back.setBounds(5, 20, 25, 25);
        p.add(back);

        back.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent ae) {
                System.exit(0);
            }
        });

        ImageIcon i4 = new ImageIcon(ClassLoader.getSystemResource("Allu arjun.jpg"));
        Image i5 = i4.getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT);
        JLabel profile = new JLabel(new ImageIcon(i5));
        profile.setBounds(40, 10, 50, 50);
        p.add(profile);

        JLabel name = new JLabel("Allu Arjun");
        name.setBounds(110, 15, 150, 18);
        name.setForeground(Color.WHITE);
        name.setFont(new Font("SAN_SERIF", Font.BOLD, 18));
        p.add(name);

        JLabel status = new JLabel("Active now");
        status.setBounds(110, 35, 100, 14);
        status.setForeground(Color.WHITE);
        status.setFont(new Font("SAN_SERIF", Font.PLAIN, 14));
        p.add(status);

        a1 = new JPanel();
        a1.setBounds(5, 75, 440, 570);
        a1.setBackground(Color.WHITE);
        a1.setLayout(new BorderLayout());
        f.add(a1);

        t1 = new JTextField();
        t1.setBounds(5, 655, 310, 40);
        t1.setFont(new Font("SAN_SERIF", Font.BOLD, 16));
        f.add(t1);

        JButton send = new JButton("Send");
        send.setBounds(320, 655, 123, 40);
        send.setBackground(new Color(7, 94, 84));
        send.setForeground(Color.WHITE);
        send.addActionListener(this);
        f.add(send);

        f.setTitle("Client Chat");
        f.setSize(450, 700);
        f.setLocation(700, 50); // open on right side of server window
        f.setUndecorated(true);
        f.getContentPane().setBackground(Color.white);
        f.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        try {
            String out = t1.getText().trim();
            if (out.isEmpty())
                return;

            System.out.println("Message sent: " + out);
            t1.setText("");

            JPanel p2 = formatLabel(out);

            JPanel right = new JPanel(new BorderLayout());
            right.add(p2, BorderLayout.LINE_END);
            right.setBackground(Color.WHITE);

            vertical.add(right);
            vertical.add(Box.createVerticalStrut(15));

            a1.add(vertical, BorderLayout.PAGE_START);

            if (otp != null)
                otp.writeUTF(out); // send message to server

            a1.revalidate();
            a1.repaint();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static JPanel formatLabel(String out) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel output = new JLabel("<html><p style=\"width:150px\">" + out + "</p></html>");
        output.setFont(new Font("Tahoma", Font.PLAIN, 16));
        output.setBackground(new Color(220, 248, 198));
        output.setOpaque(true);
        output.setBorder(new EmptyBorder(10, 15, 10, 50));

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        JLabel time = new JLabel(sdf.format(cal.getTime()));
        time.setFont(new Font("SAN_SERIF", Font.PLAIN, 10));
        time.setForeground(Color.GRAY);

        panel.add(output);
        panel.add(time);

        return panel;
    }

    public static void main(String[] args) {
        new client();

        try {
            Socket s = new Socket("127.0.0.1", 6001);
            DataInputStream din = new DataInputStream(s.getInputStream());
            otp = new DataOutputStream(s.getOutputStream()); // ✅ assign to static otp

            while (true) {
                String msg = din.readUTF();

                JPanel panel = formatLabel(msg);

                JPanel left = new JPanel(new BorderLayout());
                left.add(panel, BorderLayout.LINE_START);
                left.setBackground(Color.WHITE);

                vertical.add(left);
                vertical.add(Box.createVerticalStrut(15));

                a1.add(vertical, BorderLayout.PAGE_START);
                a1.revalidate();
                a1.repaint();
                f.validate();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
