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
	
    // 회원가입 화면 표시 메소드
    private void showSignUpScreen() {
        JFrame signUpFrame = new JFrame("회원가입");
        signUpFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        signUpFrame.setSize(300, 200);
        signUpFrame.setLayout(null); // 절대 배치로 설정
        signUpFrame.setLocationRelativeTo(mainFrame); // 메인 프레임에 상대적으로 중앙 배치

        JLabel usernameLabel = new JLabel("사용자 이름:");
        usernameLabel.setBounds(20, 30, 80, 25); // (x, y, width, height)
        signUpFrame.add(usernameLabel);

        JTextField usernameField = new JTextField(20);
        usernameField.setBounds(110, 30, 160, 25);
        signUpFrame.add(usernameField);

        JLabel passwordLabel = new JLabel("비밀번호:");
        passwordLabel.setBounds(20, 60, 80, 25);
        signUpFrame.add(passwordLabel);

        JPasswordField passwordField = new JPasswordField(20);
        passwordField.setBounds(110, 60, 160, 25);
        signUpFrame.add(passwordField);

        JButton signUpButton = new JButton("가입");
        signUpButton.setBounds(100, 100, 100, 25);
        signUpButton.addActionListener(e -> {
        	// 입력된 사용자 이름, 비밀번호 가져오기
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            // 비어있는지 확인
            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(signUpFrame, "사용자 이름과 비밀번호를 입력해주세요.");
            // 사용자 이름이 존재하는지 확인
            } else if (userExists(username)) {
                JOptionPane.showMessageDialog(signUpFrame, "이미 존재하는 사용자 이름입니다.");
            // 사용자 이름이 존재하지 않으면 파일에 저장
            } else {
                writeUser(username, password);
                JOptionPane.showMessageDialog(signUpFrame, "회원가입 완료!");
                signUpFrame.dispose(); // 회원가입 창 닫기
            }
        });
        signUpFrame.add(signUpButton);

        signUpFrame.setVisible(true);
    }


    // 사용자 정보를 파일에 저장하는 메소드
    private void writeUser(String username, String password) {
        // 파일 쓰기
    	try (FileWriter writer = new FileWriter(USERS_FILE, true);
             BufferedWriter bw = new BufferedWriter(writer);
             PrintWriter out = new PrintWriter(bw)) {
            // 사용자 이름, 비밀번호 파일에 작성
    		out.println(username + "," + password);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(mainFrame, "사용자 등록 실패: " + e.getMessage());
        }
    }


    // 사용자 이름이 이미 존재하는지 확인하는 메소드
    private boolean userExists(String username) {
        File file = new File(USERS_FILE);
        if (!file.exists()) { // 존재하지 않으면 false 반환
            return false;
        }

        // 파일 읽기
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            // 사용자 이름 확인
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
            	// 파일의 사용자 이름과 입력된 사용자 이름이 일치하면 true 반환
                if (parts.length >= 2 && parts[0].equals(username)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;  // 존재하지 않으면 false
    }

    // 로그인 화면 표시 메소드
    public void showLoginScreen() {
        JFrame loginFrame = new JFrame("로그인");
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setSize(400, 350);
        loginFrame.setLayout(null); // 절대 배치로 설정
        loginFrame.setLocationRelativeTo(null); // 화면 중앙에 위치

        // 레이블 추가
        JLabel titleLabel = new JLabel("Diary Appication");
        titleLabel.setBounds(115, 50, 200, 25); // 상단 중앙에 위치
        loginFrame.add(titleLabel);
        
        // 폰트 크기를 20으로 설정
        Font titleFont = new Font("SansSerif", Font.BOLD, 21);
        titleLabel.setFont(titleFont);

        // 사용자 이름 라벨과 필드 추가
        JLabel usernameLabel = new JLabel("사용자 이름:");
        usernameLabel.setBounds(60, 100, 100, 25); // (x, y, width, height)
        loginFrame.add(usernameLabel);

        JTextField usernameField = new JTextField(15);
        usernameField.setBounds(155, 100, 180, 25);
        loginFrame.add(usernameField);

        // 비밀번호 라벨과 필드 추가
        JLabel passwordLabel = new JLabel("비밀번호:");
        passwordLabel.setBounds(60, 140, 100, 25);
        loginFrame.add(passwordLabel);

        JPasswordField passwordField = new JPasswordField(15);
        passwordField.setBounds(155, 140, 180, 25);
        loginFrame.add(passwordField);

        // 로그인 및 회원가입 버튼 추가
        JButton loginButton = new JButton("로그인");
        loginButton.setBounds(100, 200, 90, 25);
        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            // 사용자 이름과 비밀번화 확인해 로그인 검증
            if (verifyUser(username, password)) {
                JOptionPane.showMessageDialog(loginFrame, "로그인 성공!");
                loginFrame.dispose();
                mainFrame.setVisible(true); // 메인 프레임 보이기
            } else {
                JOptionPane.showMessageDialog(loginFrame, "사용자 이름 또는 비밀번호가 일치하지 않습니다.");
            }
        });
        loginFrame.add(loginButton);

        // 회원가입 버튼 추가 및 액션 리스너
        JButton signUpButton = new JButton("회원가입");
        signUpButton.setBounds(200, 200, 90, 25);
        signUpButton.addActionListener(e -> showSignUpScreen());
        loginFrame.add(signUpButton);

        loginFrame.setVisible(true);
    }



    // 사용자 이름과 비밀번호 검증해 로그인 여부 반환
    private boolean verifyUser(String username, String password) {
        File file = new File(USERS_FILE);
        if (!file.exists()) {
            return false; // 사용자 파일이 없으면 로그인 실패
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2 && parts[0].equals(username) && parts[1].equals(password)) {
                    return true; // 사용자 이름과 비밀번호가 일치하면 로그인 성공
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return false; // 사용자 이름 또는 비밀번호가 일치하지 않음
    }

    
	public void loginUser() {
        showLoginScreen(); // 로그인 화면 표시
    }	
	
    // 메인 응용 프로그램 프레임에 로그아웃 버튼을 추가하는 메서드
    public void addLogoutButton() {
        JButton logoutButton = new JButton("로그아웃");
        logoutButton.setBounds(285, 275, 85, 25); // 필요에 따라 위치와 크기 조정
        mainFrame.add(logoutButton); // mainFrame에 추가

        logoutButton.addActionListener((ActionListener) new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performLogout();
            }
        });
    }

    // 로그아웃을 수행하는 메서드
    private void performLogout() {
        int option = JOptionPane.showConfirmDialog(mainFrame, "로그아웃 하시겠습니까?", "로그아웃", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            // 로그아웃 동작 수행, 예: UI 재설정, 세션 데이터 초기화
            JOptionPane.showMessageDialog(mainFrame, "로그아웃 되었습니다.");
            // 예: mainFrame 숨기고 로그인 화면 다시 표시
            mainFrame.setVisible(false);
            showLoginScreen(); // 로그인 화면 다시 표시
        }
    }
}