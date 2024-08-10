import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import javax.swing.*;

public class LoginUtils {
	
    private JFrame mainFrame;
    private static final String USERS_FILE = "./users.txt";
    
    public LoginUtils(JFrame mainFrame) {
        this.mainFrame = mainFrame;
    }
	
    // ȸ������ ȭ�� ǥ�� �޼ҵ�
    private void showSignUpScreen() {
        JFrame signUpFrame = new JFrame("ȸ������");
        signUpFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        signUpFrame.setSize(300, 200);
        signUpFrame.setLayout(null); // ���� ��ġ�� ����
        signUpFrame.setLocationRelativeTo(mainFrame); // ���� �����ӿ� ��������� �߾� ��ġ

        JLabel usernameLabel = new JLabel("����� �̸�:");
        usernameLabel.setBounds(20, 30, 80, 25); // (x, y, width, height)
        signUpFrame.add(usernameLabel);

        JTextField usernameField = new JTextField(20);
        usernameField.setBounds(110, 30, 160, 25);
        signUpFrame.add(usernameField);

        JLabel passwordLabel = new JLabel("��й�ȣ:");
        passwordLabel.setBounds(20, 60, 80, 25);
        signUpFrame.add(passwordLabel);

        JPasswordField passwordField = new JPasswordField(20);
        passwordField.setBounds(110, 60, 160, 25);
        signUpFrame.add(passwordField);

        JButton signUpButton = new JButton("����");
        signUpButton.setBounds(100, 100, 100, 25);
        signUpButton.addActionListener(e -> {
        	// �Էµ� ����� �̸�, ��й�ȣ ��������
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            // ����ִ��� Ȯ��
            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(signUpFrame, "����� �̸��� ��й�ȣ�� �Է����ּ���.");
            // ����� �̸��� �����ϴ��� Ȯ��
            } else if (userExists(username)) {
                JOptionPane.showMessageDialog(signUpFrame, "�̹� �����ϴ� ����� �̸��Դϴ�.");
            // ����� �̸��� �������� ������ ���Ͽ� ����
            } else {
                writeUser(username, password);
                JOptionPane.showMessageDialog(signUpFrame, "ȸ������ �Ϸ�!");
                signUpFrame.dispose(); // ȸ������ â �ݱ�
            }
        });
        signUpFrame.add(signUpButton);

        signUpFrame.setVisible(true);
    }


    // ����� ������ ���Ͽ� �����ϴ� �޼ҵ�
    private void writeUser(String username, String password) {
        // ���� ����
    	try (FileWriter writer = new FileWriter(USERS_FILE, true);
             BufferedWriter bw = new BufferedWriter(writer);
             PrintWriter out = new PrintWriter(bw)) {
            // ����� �̸�, ��й�ȣ ���Ͽ� �ۼ�
    		out.println(username + "," + password);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(mainFrame, "����� ��� ����: " + e.getMessage());
        }
    }


    // ����� �̸��� �̹� �����ϴ��� Ȯ���ϴ� �޼ҵ�
    private boolean userExists(String username) {
        File file = new File(USERS_FILE);
        if (!file.exists()) { // �������� ������ false ��ȯ
            return false;
        }

        // ���� �б�
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            // ����� �̸� Ȯ��
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
            	// ������ ����� �̸��� �Էµ� ����� �̸��� ��ġ�ϸ� true ��ȯ
                if (parts.length >= 2 && parts[0].equals(username)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;  // �������� ������ false
    }

    // �α��� ȭ�� ǥ�� �޼ҵ�
    public void showLoginScreen() {
        JFrame loginFrame = new JFrame("�α���");
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setSize(400, 350);
        loginFrame.setLayout(null); // ���� ��ġ�� ����
        loginFrame.setLocationRelativeTo(null); // ȭ�� �߾ӿ� ��ġ

        // ���̺� �߰�
        JLabel titleLabel = new JLabel("Diary Appication");
        titleLabel.setBounds(115, 50, 200, 25); // ��� �߾ӿ� ��ġ
        loginFrame.add(titleLabel);
        
        // ��Ʈ ũ�⸦ 20���� ����
        Font titleFont = new Font("SansSerif", Font.BOLD, 21);
        titleLabel.setFont(titleFont);

        // ����� �̸� �󺧰� �ʵ� �߰�
        JLabel usernameLabel = new JLabel("����� �̸�:");
        usernameLabel.setBounds(60, 100, 100, 25); // (x, y, width, height)
        loginFrame.add(usernameLabel);

        JTextField usernameField = new JTextField(15);
        usernameField.setBounds(155, 100, 180, 25);
        loginFrame.add(usernameField);

        // ��й�ȣ �󺧰� �ʵ� �߰�
        JLabel passwordLabel = new JLabel("��й�ȣ:");
        passwordLabel.setBounds(60, 140, 100, 25);
        loginFrame.add(passwordLabel);

        JPasswordField passwordField = new JPasswordField(15);
        passwordField.setBounds(155, 140, 180, 25);
        loginFrame.add(passwordField);

        // �α��� �� ȸ������ ��ư �߰�
        JButton loginButton = new JButton("�α���");
        loginButton.setBounds(100, 200, 90, 25);
        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            // ����� �̸��� ��й�ȭ Ȯ���� �α��� ����
            if (verifyUser(username, password)) {
                JOptionPane.showMessageDialog(loginFrame, "�α��� ����!");
                loginFrame.dispose();
                mainFrame.setVisible(true); // ���� ������ ���̱�
            } else {
                JOptionPane.showMessageDialog(loginFrame, "����� �̸� �Ǵ� ��й�ȣ�� ��ġ���� �ʽ��ϴ�.");
            }
        });
        loginFrame.add(loginButton);

        // ȸ������ ��ư �߰� �� �׼� ������
        JButton signUpButton = new JButton("ȸ������");
        signUpButton.setBounds(200, 200, 90, 25);
        signUpButton.addActionListener(e -> showSignUpScreen());
        loginFrame.add(signUpButton);

        loginFrame.setVisible(true);
    }



    // ����� �̸��� ��й�ȣ ������ �α��� ���� ��ȯ
    private boolean verifyUser(String username, String password) {
        File file = new File(USERS_FILE);
        if (!file.exists()) {
            return false; // ����� ������ ������ �α��� ����
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2 && parts[0].equals(username) && parts[1].equals(password)) {
                    return true; // ����� �̸��� ��й�ȣ�� ��ġ�ϸ� �α��� ����
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return false; // ����� �̸� �Ǵ� ��й�ȣ�� ��ġ���� ����
    }

    
	public void loginUser() {
        showLoginScreen(); // �α��� ȭ�� ǥ��
    }	
	
    // ���� ���� ���α׷� �����ӿ� �α׾ƿ� ��ư�� �߰��ϴ� �޼���
    public void addLogoutButton() {
        JButton logoutButton = new JButton("�α׾ƿ�");
        logoutButton.setBounds(285, 275, 85, 25); // �ʿ信 ���� ��ġ�� ũ�� ����
        mainFrame.add(logoutButton); // mainFrame�� �߰�

        logoutButton.addActionListener((ActionListener) new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performLogout();
            }
        });
    }

    // �α׾ƿ��� �����ϴ� �޼���
    private void performLogout() {
        int option = JOptionPane.showConfirmDialog(mainFrame, "�α׾ƿ� �Ͻðڽ��ϱ�?", "�α׾ƿ�", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            // �α׾ƿ� ���� ����, ��: UI �缳��, ���� ������ �ʱ�ȭ
            JOptionPane.showMessageDialog(mainFrame, "�α׾ƿ� �Ǿ����ϴ�.");
            // ��: mainFrame ����� �α��� ȭ�� �ٽ� ǥ��
            mainFrame.setVisible(false);
            showLoginScreen(); // �α��� ȭ�� �ٽ� ǥ��
        }
    }
}