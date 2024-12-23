import java.awt.*;
import java.io.*;
import javax.swing.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class DiaryApp {

	private static final String DIARY_DIRECTORY = "./diaries/";
    public JFrame mainFrame;
    private Timer reminderTimer;

    // 다이어리앱 생성자
    public DiaryApp() {
        mainFrame = new JFrame("Diary App");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(400, 350);
        mainFrame.setLayout(null);

        JButton writeDiaryButton = new JButton("일기 작성");
        JButton listDiariesButton = new JButton("일기 목록");
        JButton searchDiaryButton = new JButton("일기 검색");
        JButton setReminderButton = new JButton("알림 설정");

        // 버튼에 대한 액션 리스너
        writeDiaryButton.addActionListener(e -> showWriteDiaryScreen());
        listDiariesButton.addActionListener(e -> showDiaryListScreen());
        searchDiaryButton.addActionListener(e -> showSearchDiaryScreen());
        setReminderButton.addActionListener(e -> showSetReminderScreen());

        writeDiaryButton.setBounds(145, 45, 100, 30);
        listDiariesButton.setBounds(145, 95, 100, 30);
        searchDiaryButton.setBounds(145, 145, 100, 30);
        setReminderButton.setBounds(145, 195, 100, 30);

        mainFrame.add(writeDiaryButton);
        mainFrame.add(listDiariesButton);
        mainFrame.add(searchDiaryButton);
        mainFrame.add(setReminderButton);
        
        // LoginUtils 객체 생성 및 로그아웃 버튼 추가
        LoginUtils loginUtils = new LoginUtils(mainFrame);
        loginUtils.addLogoutButton();

        // 중앙에 배치
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
        // 크기 변경 불가
        mainFrame.setResizable(false);
    }
    
    // 일기 작성 화면을 표시하는 메소드
    private void showWriteDiaryScreen() {
        JFrame writeFrame = new JFrame("일기 작성");
        
        // 프로그램 자체가 아니라 창만 닫힘
        writeFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        writeFrame.setSize(400, 350);

        JTextField titleField = new JTextField(20);  // 제목 입력란
        JTextArea textArea = new JTextArea(10, 30);  // 내용 입력란
        
        JButton saveButton = new JButton("저장");

        JPanel datePanel = new JPanel(new FlowLayout());

        // 연도 선택 콤보박스
        JComboBox<String> yearComboBox = new JComboBox<>(generateYearOptions());
        // 월 선택 콤보 박스
        JComboBox<String> monthComboBox = new JComboBox<>(generateMonthOptions());
        // 일자 선택 콤보 박스
        JComboBox<String> dayComboBox = new JComboBox<>(generateDayOptions());

        // 오늘의 날짜를 콤보박스의 기본값으로 설정해둠
        // 캘린더 객체 사용
        Calendar calendar = Calendar.getInstance();
        yearComboBox.setSelectedItem(String.valueOf(calendar.get(Calendar.YEAR)));
        // 월을 선택할 때 두 자리로 설정해두고, +1을 해줌으로써 실제 날짜의 월을 가져올 수 있도록 함 (캘린더에서 월이 0부터 시작)
        monthComboBox.setSelectedItem(String.format("%02d", calendar.get(Calendar.MONTH) + 1));
        // 일자를 선택할 때 두 자리로 지정
        dayComboBox.setSelectedItem(String.format("%02d", calendar.get(Calendar.DAY_OF_MONTH)));

        datePanel.add(new JLabel("년도:"));
        datePanel.add(yearComboBox);
        datePanel.add(new JLabel("월:"));
        datePanel.add(monthComboBox);
        datePanel.add(new JLabel("일:"));
        datePanel.add(dayComboBox);

        saveButton.addActionListener(e -> {
        	// getSelectedItem()를 통해 콤보박스에서 선택된 항목 반환
        	// 선택한 날짜의 문자열을 생성하도록 함
            String selectedDate = yearComboBox.getSelectedItem() + "-" +
                                  monthComboBox.getSelectedItem() + "-" +
                                  dayComboBox.getSelectedItem();
            // 입력된 일기 정보를 저장
            saveDiary(selectedDate, titleField.getText(), textArea.getText());
            // 일기 작성창 닫기
            writeFrame.dispose();
        });

        writeFrame.setLayout(new BorderLayout());
        JPanel topPanel = new JPanel(new GridLayout(2, 1));
        topPanel.add(datePanel);
        topPanel.add(titleField);
        writeFrame.add(topPanel, BorderLayout.NORTH);
        writeFrame.add(new JScrollPane(textArea), BorderLayout.CENTER);
        writeFrame.add(saveButton, BorderLayout.SOUTH);

        // 메인 프레임 중앙에 위치
        writeFrame.setLocationRelativeTo(mainFrame);
        writeFrame.setVisible(true);
    }

    // 최근 10년 간의 연도 선택지 만들어둠
    private String[] generateYearOptions() {
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        String[] years = new String[10];
        for (int i = 0; i < 10; i++) {
            years[i] = String.valueOf(currentYear - i);
        }
        return years;
    }

    // 1월부터 12월까지 선택지
    private String[] generateMonthOptions() {
        String[] months = new String[12];
        for (int i = 0; i < 12; i++) {
            months[i] = String.format("%02d", i + 1);
        }
        return months;
    }

    // 1일부터 31일까지의 선택지
    private String[] generateDayOptions() {
        String[] days = new String[31];
        for (int i = 0; i < 31; i++) {
            days[i] = String.format("%02d", i + 1);
        }
        return days;
    }
    
    // 일기 저장 메소드
    private void saveDiary(String date, String title, String content) {
    	// 파일 이름 생성 : 날짜_제목 형식으로 (정규식 이용해 특수문자 대체
        String fileName = date + "_" + title.replaceAll("[^a-zA-Z0-9가-힣]", " ") + ".txt";
        // 일기 디렉토리
        File diaryDir = new File(DIARY_DIRECTORY);
        if (!diaryDir.exists()) {
            diaryDir.mkdir();
        }
        // 파일 쓰기
        try (FileWriter writer = new FileWriter(DIARY_DIRECTORY + fileName)) {
            writer.write(content);
            JOptionPane.showMessageDialog(mainFrame, "일기 저장 완료!");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(mainFrame, "일기 저장 실패: " + ex.getMessage());
        }
    }

    // 일기 목록 화면 표시
    private void showDiaryListScreen() {
        JFrame listFrame = new JFrame("일기 목록");
        listFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        listFrame.setSize(400, 300);

        // 일기 디렉토리 객체
        File directory = new File(DIARY_DIRECTORY);
        File[] files = directory.listFiles((dir, name) -> name.endsWith(".txt"));
        
        String[] fileNames;
        // 파일이 없을 떼 메시지
        if (files == null || files.length == 0) {
            fileNames = new String[]{"저장된 일기가 없습니다."};
        } else {
        	// 있을 경우 파일 이름 배열 생성
            fileNames = new String[files.length];
            for (int i = 0; i < files.length; i++) {
                fileNames[i] = files[i].getName().replace(".txt", "");
            }
        }

        JList<String> diaryList = new JList<>(fileNames);
        diaryList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JButton readButton = new JButton("읽기");
        JButton editButton = new JButton("수정");
        JButton deleteButton = new JButton("삭제");

        // 읽기 버튼 클릭
        readButton.addActionListener(e -> {
        	// 선택된 항목이 있을 경우
            if (diaryList.getSelectedIndex() != -1 && files != null && files.length != 0) {
            	// 파일 가져와서 표시
            	File selectedFile = files[diaryList.getSelectedIndex()];
                showDiary(selectedFile);
            }
        });

        // 수정 버튼 클릭
        editButton.addActionListener(e -> {
        	// 선택된 항목이 있을 경우
            if (diaryList.getSelectedIndex() != -1 && files != null && files.length != 0) {
            	// 파일 가져와서 수정화면 표시
            	File selectedFile = files[diaryList.getSelectedIndex()];
                editDiary(selectedFile, listFrame);
            }
        });

        // 삭제 버튼 클릭
        deleteButton.addActionListener(e -> {
        	// 선택된 항목이 있을 경우
            if (diaryList.getSelectedIndex() != -1 && files != null && files.length != 0) {
            	// 선택된 파일이 있을 때 다이얼로그 표시
            	int option = JOptionPane.showConfirmDialog(listFrame, "정말로 삭제하시겠습니까?", "일기 삭제", JOptionPane.YES_NO_OPTION);
                // 사용자가 확인 시 일기 삭제
            	if (option == JOptionPane.YES_OPTION) {
                    File selectedFile = files[diaryList.getSelectedIndex()];
                    deleteDiary(selectedFile, listFrame);
                }
            }
        });

        listFrame.setLayout(new BorderLayout());
        listFrame.add(new JScrollPane(diaryList), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(readButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        listFrame.add(buttonPanel, BorderLayout.SOUTH);

        // 메인 프레임 중앙에 배치
        listFrame.setLocationRelativeTo(mainFrame);
        listFrame.setVisible(true);
    }

    // 일기 검색 화면 표시 메소드
    private void showSearchDiaryScreen() {
        JFrame searchFrame = new JFrame("일기 검색");
        searchFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        searchFrame.setSize(400, 150);

        JTextField searchField = new JTextField(20);
        JButton searchButton = new JButton("검색");
        
        // 검색 버튼 클릭 시
        searchButton.addActionListener(e -> {
        	// 검색어를 텍스트 필드에서 가져와
            String searchTerm = searchField.getText();
        	// 일기 검색 후 결과 표시
            List<File> searchResults = searchDiaries(searchTerm);
            showSearchResults(searchResults);
            searchFrame.dispose();
        });

        JPanel searchPanel = new JPanel(new FlowLayout());
        searchPanel.add(new JLabel("검색어:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        searchFrame.add(searchPanel);
        searchFrame.setLocationRelativeTo(mainFrame);
        searchFrame.setVisible(true);
        searchFrame.setResizable(false);
    }

    // 검색어 기준으로 일기 검색하는 메소드
    private List<File> searchDiaries(String searchTerm) {
    	// 검색 결과를 저장할 리스트
        List<File> searchResults = new ArrayList<>();
        // 일기 디렉토리 설정
        File directory = new File(DIARY_DIRECTORY);
        // 파일 목록 가져오기
        File[] files = directory.listFiles((dir, name) -> name.endsWith(".txt"));

        // 파일이 존재하는 경우
        if (files != null) {
            for (File file : files) {
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    String line;
                    // 파일의 각 줄을 읽으며 검색어가 포함된 경우 검색 결과에 추가
                    while ((line = reader.readLine()) != null) {
                        if (line.contains(searchTerm)) {
                            searchResults.add(file);
                            break;
                        }
                    }
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(mainFrame, "검색 오류: " + ex.getMessage());
                }
            }
        }
        return searchResults;  // 검색 결과 반환
    }

    // 검색 결과를 화면에 표시하는 메소드
    private void showSearchResults(List<File> searchResults) {
        JFrame resultsFrame = new JFrame("검색 결과");
        resultsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        resultsFrame.setSize(400, 300);

        // 검색 결과 파일 이름 배열 생성 (확장자느 제거)
        String[] resultNames = searchResults.stream().map(file -> file.getName().replace(".txt", "")).toArray(String[]::new);

        // 검색 결과를 표시할 리스트
        JList<String> resultList = new JList<>(resultNames);
        // 하나만 선택할 수 있도록 설정
        resultList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JButton readButton = new JButton("읽기");
        JButton editButton = new JButton("수정");
        JButton deleteButton = new JButton("삭제");

        // 읽기 버튼 클릭
        readButton.addActionListener(e -> {
        	// 선택된 항목이 있을 경우
            if (resultList.getSelectedIndex() != -1) {
            	// 파일 가져와서 표시
                File selectedFile = searchResults.get(resultList.getSelectedIndex());
                showDiary(selectedFile);
            }
        });

        // 수정 버튼 클릭
        editButton.addActionListener(e -> {
        	// 선택된 항목이 있을 경우
            if (resultList.getSelectedIndex() != -1) {
            	// 파일 가져와서 수정화면 표시
                File selectedFile = searchResults.get(resultList.getSelectedIndex());
                editDiary(selectedFile, resultsFrame);
            }
        });

        // 삭제 버튼 클릭
        deleteButton.addActionListener(e -> {
        	// 선택된 항목이 있을 경우
            if (resultList.getSelectedIndex() != -1) {
            	// 선택된 파일이 있을 때 다이얼로그 표시
                int option = JOptionPane.showConfirmDialog(resultsFrame, "정말로 삭제하시겠습니까?", "일기 삭제", JOptionPane.YES_NO_OPTION);
                // 사용자가 확인 시 일기 삭제
                if (option == JOptionPane.YES_OPTION) {
                    File selectedFile = searchResults.get(resultList.getSelectedIndex());
                    deleteDiary(selectedFile, resultsFrame);
                }
            }
        });

        resultsFrame.setLayout(new BorderLayout());
        resultsFrame.add(new JScrollPane(resultList), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(readButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        resultsFrame.add(buttonPanel, BorderLayout.SOUTH);

        resultsFrame.setLocationRelativeTo(mainFrame);
        resultsFrame.setVisible(true);
    }

    // 일기 파일을 화면에 표시함
    // 수정은 불가능
    private void showDiary(File diaryFile) {
        JFrame diaryFrame = new JFrame(diaryFile.getName().replace(".txt", "") + " 일기");
        // 해당 프레임만 닫아지도록 설정
        diaryFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        diaryFrame.setSize(400, 300);

        JTextArea textArea = new JTextArea(10, 30);
        textArea.setEditable(false);

        // 일기 내용을 텍스트 에리어에 표시
        // 예외 처리 (오류 발생시 메시지 표시)
        try (BufferedReader reader = new BufferedReader(new FileReader(diaryFile))) {
            textArea.read(reader, null);
        } catch (IOException ex) {
            textArea.setText("일기를 읽는 중 오류가 발생했습니다: " + ex.getMessage());
        }

        diaryFrame.setLayout(new BorderLayout());
        diaryFrame.add(new JScrollPane(textArea), BorderLayout.CENTER);

        // 메인 프레임 기준으로 다이어리 프레임 위치 시킴
        diaryFrame.setLocationRelativeTo(mainFrame);
        diaryFrame.setVisible(true);
    }

    // 선택한 일기 파일의 제목, 날자, 내용 변경 가능
    private void editDiary(File diaryFile, JFrame listFrame) {
        JFrame editFrame = new JFrame("일기 수정");
        // 해당 프레임만 닫아지도록 설정
        editFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        editFrame.setSize(400, 400);

        JTextArea textArea = new JTextArea(10, 30);
        JButton saveButton = new JButton("저장");

        JPanel datePanel = new JPanel(new FlowLayout());

        JComboBox<String> yearComboBox = new JComboBox<>(generateYearOptions());
        JComboBox<String> monthComboBox = new JComboBox<>(generateMonthOptions());
        JComboBox<String> dayComboBox = new JComboBox<>(generateDayOptions());

        String[] dateParts = diaryFile.getName().substring(0, 10).split("-");
        String originalTitle = diaryFile.getName().substring(11).replace(".txt", "");
        JTextField titleField = new JTextField(originalTitle, 20);

        yearComboBox.setSelectedItem(dateParts[0]);
        monthComboBox.setSelectedItem(dateParts[1]);
        dayComboBox.setSelectedItem(dateParts[2]);

        datePanel.add(new JLabel("년도:"));
        datePanel.add(yearComboBox);
        datePanel.add(new JLabel("월:"));
        datePanel.add(monthComboBox);
        datePanel.add(new JLabel("일:"));
        datePanel.add(dayComboBox);

        try (BufferedReader reader = new BufferedReader(new FileReader(diaryFile))) {
            textArea.read(reader, null);
        } catch (IOException ex) {
            textArea.setText("일기를 읽는 중 오류가 발생했습니다: " + ex.getMessage());
        }

        // 저장 버튼 클릭 시
        saveButton.addActionListener(e -> {
        	// 수정한 제목과 날자를 기반하여 새로운 파일 생성
            String selectedDate = yearComboBox.getSelectedItem() + "-" +
                                  monthComboBox.getSelectedItem() + "-" +
                                  dayComboBox.getSelectedItem();
            String newTitle = titleField.getText().replaceAll("[^a-zA-Z0-9가-힣]", " ");
            File newFile = new File(DIARY_DIRECTORY + selectedDate + "_" + newTitle + ".txt");

            // 제목 혹은 날짜가 수정 됐을 경우 기존 파일 삭제
            if (!diaryFile.equals(newFile)) {
                diaryFile.delete();
            }
            
            // 수정된 일기 새 파일에 저장
            // 예외 처리 (오류 메시지 표시)
            try (FileWriter writer = new FileWriter(newFile)) {
                writer.write(textArea.getText());
                JOptionPane.showMessageDialog(editFrame, "일기 수정 완료!");
                editFrame.dispose();  // 현재 프레임 닫기
                listFrame.dispose();  // 일기 목록 화면도 닫음
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(editFrame, "일기 수정 실패: " + ex.getMessage());
            }
        });

        editFrame.setLayout(new BorderLayout());
        editFrame.add(new JScrollPane(textArea), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(saveButton);
        editFrame.add(buttonPanel, BorderLayout.SOUTH);

        JPanel topPanel = new JPanel(new GridLayout(2, 1));
        topPanel.add(datePanel);
        topPanel.add(titleField);
        
        editFrame.add(topPanel, BorderLayout.NORTH);
        editFrame.setLocationRelativeTo(mainFrame);
        editFrame.setVisible(true);
    }

    // 일기 삭제 메소드
    private void deleteDiary(File file, JFrame parentFrame) {
        if (file.delete()) {
            JOptionPane.showMessageDialog(mainFrame, "일기 삭제 완료!");
            parentFrame.dispose();
            showDiaryListScreen();
        } else {
            JOptionPane.showMessageDialog(mainFrame, "일기 삭제 실패!");
        }
    }

    // 알림 설정 화면을 표시하는 메소드
    private void showSetReminderScreen() {
        JFrame reminderFrame = new JFrame("알림 설정");
        reminderFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        reminderFrame.setSize(300, 200);

        // 시간 선택 콤보박스 배열 설정
        String[] hours = new String[24];
        for (int i = 0; i < 24; i++) {
            hours[i] = String.format("%02d", i);
        }

        // 분 선택 콤보박스 배열 (5분 단위)
        String[] minutes = new String[12];
        for (int i = 0; i < 12; i++) {
            minutes[i] = String.format("%02d", i * 5);
        }

        JComboBox<String> hourComboBox = new JComboBox<>(hours);
        JComboBox<String> minuteComboBox = new JComboBox<>(minutes);
        JButton setButton = new JButton("설정");

        JPanel panel = new JPanel();
        panel.setLayout(null); // 절대배치로 변경

        // 위치 및 크기 설정
        hourComboBox.setBounds(70, 40, 50, 25); // x, y, width, height
        JLabel hourLabel = new JLabel("시");
        hourLabel.setBounds(125, 40, 20, 25);
        minuteComboBox.setBounds(150, 40, 50, 25);
        JLabel minuteLabel = new JLabel("분");
        minuteLabel.setBounds(205, 40, 20, 25);
        setButton.setBounds(95, 90, 100, 30); // 설정 버튼 위치와 크기 설정

        panel.add(hourComboBox);
        panel.add(hourLabel);
        panel.add(minuteComboBox);
        panel.add(minuteLabel);
        panel.add(setButton);

        // 설정 버튼 클릭 시 기능 추가
        setButton.addActionListener(e -> {
            int hour = Integer.parseInt((String) hourComboBox.getSelectedItem());
            int minute = Integer.parseInt((String) minuteComboBox.getSelectedItem());
            setReminder(hour, minute);  // 알림 설정
            reminderFrame.dispose();  // 프레임 닫기
        });

        reminderFrame.add(panel);
        reminderFrame.setLocationRelativeTo(null); // 화면 중앙에 배치
        reminderFrame.setVisible(true);
    }


    // 알림 설정 메소드
    private void setReminder(int hour, int minute) {
    	// 기존의 타이머 취소
        if (reminderTimer != null) {
            reminderTimer.cancel();
        }

        // 현재 시간 가져오기
        Calendar now = Calendar.getInstance();
        // 알림 시간 설정
        Calendar reminderTime = (Calendar) now.clone();
        reminderTime.set(Calendar.HOUR_OF_DAY, hour);
        reminderTime.set(Calendar.MINUTE, minute);
        reminderTime.set(Calendar.SECOND, 0);

        // 알림 시간이 현재 이전이면, 다음날로 설정해주기
        if (reminderTime.before(now)) {
            reminderTime.add(Calendar.DAY_OF_MONTH, 1);
        }

        // 새 타이머 만들기
        reminderTimer = new Timer();
        // 알림에 맞추어 매일 반복하도록 설정
        reminderTimer.schedule(new TimerTask() {
            @Override
            public void run() {
            	// 알림 메시지 표시
            	JOptionPane.showMessageDialog(mainFrame, "일기 작성 시간입니다!");
            }
        }, reminderTime.getTime(), 24 * 60 * 60 * 1000);  // 매일 반복
    }
    
    public void loginUser() {
        LoginUtils loginUtils = new LoginUtils(mainFrame);
        loginUtils.showLoginScreen();
    }
}
