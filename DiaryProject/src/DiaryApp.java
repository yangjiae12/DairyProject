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

    // ���̾�� ������
    public DiaryApp() {
        mainFrame = new JFrame("Diary App");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(400, 350);
        mainFrame.setLayout(null);

        JButton writeDiaryButton = new JButton("�ϱ� �ۼ�");
        JButton listDiariesButton = new JButton("�ϱ� ���");
        JButton searchDiaryButton = new JButton("�ϱ� �˻�");
        JButton setReminderButton = new JButton("�˸� ����");

        // ��ư�� ���� �׼� ������
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
        
        // LoginUtils ��ü ���� �� �α׾ƿ� ��ư �߰�
        LoginUtils loginUtils = new LoginUtils(mainFrame);
        loginUtils.addLogoutButton();

        // �߾ӿ� ��ġ
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
        // ũ�� ���� �Ұ�
        mainFrame.setResizable(false);
    }
    
    // �ϱ� �ۼ� ȭ���� ǥ���ϴ� �޼ҵ�
    private void showWriteDiaryScreen() {
        JFrame writeFrame = new JFrame("�ϱ� �ۼ�");
        
        // ���α׷� ��ü�� �ƴ϶� â�� ����
        writeFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        writeFrame.setSize(400, 350);

        JTextField titleField = new JTextField(20);  // ���� �Է¶�
        JTextArea textArea = new JTextArea(10, 30);  // ���� �Է¶�
        
        JButton saveButton = new JButton("����");

        JPanel datePanel = new JPanel(new FlowLayout());

        // ���� ���� �޺��ڽ�
        JComboBox<String> yearComboBox = new JComboBox<>(generateYearOptions());
        // �� ���� �޺� �ڽ�
        JComboBox<String> monthComboBox = new JComboBox<>(generateMonthOptions());
        // ���� ���� �޺� �ڽ�
        JComboBox<String> dayComboBox = new JComboBox<>(generateDayOptions());

        // ������ ��¥�� �޺��ڽ��� �⺻������ �����ص�
        // Ķ���� ��ü ���
        Calendar calendar = Calendar.getInstance();
        yearComboBox.setSelectedItem(String.valueOf(calendar.get(Calendar.YEAR)));
        // ���� ������ �� �� �ڸ��� �����صΰ�, +1�� �������ν� ���� ��¥�� ���� ������ �� �ֵ��� �� (Ķ�������� ���� 0���� ����)
        monthComboBox.setSelectedItem(String.format("%02d", calendar.get(Calendar.MONTH) + 1));
        // ���ڸ� ������ �� �� �ڸ��� ����
        dayComboBox.setSelectedItem(String.format("%02d", calendar.get(Calendar.DAY_OF_MONTH)));

        datePanel.add(new JLabel("�⵵:"));
        datePanel.add(yearComboBox);
        datePanel.add(new JLabel("��:"));
        datePanel.add(monthComboBox);
        datePanel.add(new JLabel("��:"));
        datePanel.add(dayComboBox);

        saveButton.addActionListener(e -> {
        	// getSelectedItem()�� ���� �޺��ڽ����� ���õ� �׸� ��ȯ
        	// ������ ��¥�� ���ڿ��� �����ϵ��� ��
            String selectedDate = yearComboBox.getSelectedItem() + "-" +
                                  monthComboBox.getSelectedItem() + "-" +
                                  dayComboBox.getSelectedItem();
            // �Էµ� �ϱ� ������ ����
            saveDiary(selectedDate, titleField.getText(), textArea.getText());
            // �ϱ� �ۼ�â �ݱ�
            writeFrame.dispose();
        });

        writeFrame.setLayout(new BorderLayout());
        JPanel topPanel = new JPanel(new GridLayout(2, 1));
        topPanel.add(datePanel);
        topPanel.add(titleField);
        writeFrame.add(topPanel, BorderLayout.NORTH);
        writeFrame.add(new JScrollPane(textArea), BorderLayout.CENTER);
        writeFrame.add(saveButton, BorderLayout.SOUTH);

        // ���� ������ �߾ӿ� ��ġ
        writeFrame.setLocationRelativeTo(mainFrame);
        writeFrame.setVisible(true);
    }

    // �ֱ� 10�� ���� ���� ������ ������
    private String[] generateYearOptions() {
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        String[] years = new String[10];
        for (int i = 0; i < 10; i++) {
            years[i] = String.valueOf(currentYear - i);
        }
        return years;
    }

    // 1������ 12������ ������
    private String[] generateMonthOptions() {
        String[] months = new String[12];
        for (int i = 0; i < 12; i++) {
            months[i] = String.format("%02d", i + 1);
        }
        return months;
    }

    // 1�Ϻ��� 31�ϱ����� ������
    private String[] generateDayOptions() {
        String[] days = new String[31];
        for (int i = 0; i < 31; i++) {
            days[i] = String.format("%02d", i + 1);
        }
        return days;
    }
    
    // �ϱ� ���� �޼ҵ�
    private void saveDiary(String date, String title, String content) {
    	// ���� �̸� ���� : ��¥_���� �������� (���Խ� �̿��� Ư������ ��ü
        String fileName = date + "_" + title.replaceAll("[^a-zA-Z0-9��-�R]", " ") + ".txt";
        // �ϱ� ���丮
        File diaryDir = new File(DIARY_DIRECTORY);
        if (!diaryDir.exists()) {
            diaryDir.mkdir();
        }
        // ���� ����
        try (FileWriter writer = new FileWriter(DIARY_DIRECTORY + fileName)) {
            writer.write(content);
            JOptionPane.showMessageDialog(mainFrame, "�ϱ� ���� �Ϸ�!");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(mainFrame, "�ϱ� ���� ����: " + ex.getMessage());
        }
    }

    // �ϱ� ��� ȭ�� ǥ��
    private void showDiaryListScreen() {
        JFrame listFrame = new JFrame("�ϱ� ���");
        listFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        listFrame.setSize(400, 300);

        // �ϱ� ���丮 ��ü
        File directory = new File(DIARY_DIRECTORY);
        File[] files = directory.listFiles((dir, name) -> name.endsWith(".txt"));
        
        String[] fileNames;
        // ������ ���� �� �޽���
        if (files == null || files.length == 0) {
            fileNames = new String[]{"����� �ϱⰡ �����ϴ�."};
        } else {
        	// ���� ��� ���� �̸� �迭 ����
            fileNames = new String[files.length];
            for (int i = 0; i < files.length; i++) {
                fileNames[i] = files[i].getName().replace(".txt", "");
            }
        }

        JList<String> diaryList = new JList<>(fileNames);
        diaryList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JButton readButton = new JButton("�б�");
        JButton editButton = new JButton("����");
        JButton deleteButton = new JButton("����");

        // �б� ��ư Ŭ��
        readButton.addActionListener(e -> {
        	// ���õ� �׸��� ���� ���
            if (diaryList.getSelectedIndex() != -1 && files != null && files.length != 0) {
            	// ���� �����ͼ� ǥ��
            	File selectedFile = files[diaryList.getSelectedIndex()];
                showDiary(selectedFile);
            }
        });

        // ���� ��ư Ŭ��
        editButton.addActionListener(e -> {
        	// ���õ� �׸��� ���� ���
            if (diaryList.getSelectedIndex() != -1 && files != null && files.length != 0) {
            	// ���� �����ͼ� ����ȭ�� ǥ��
            	File selectedFile = files[diaryList.getSelectedIndex()];
                editDiary(selectedFile, listFrame);
            }
        });

        // ���� ��ư Ŭ��
        deleteButton.addActionListener(e -> {
        	// ���õ� �׸��� ���� ���
            if (diaryList.getSelectedIndex() != -1 && files != null && files.length != 0) {
            	// ���õ� ������ ���� �� ���̾�α� ǥ��
            	int option = JOptionPane.showConfirmDialog(listFrame, "������ �����Ͻðڽ��ϱ�?", "�ϱ� ����", JOptionPane.YES_NO_OPTION);
                // ����ڰ� Ȯ�� �� �ϱ� ����
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

        // ���� ������ �߾ӿ� ��ġ
        listFrame.setLocationRelativeTo(mainFrame);
        listFrame.setVisible(true);
    }

    // �ϱ� �˻� ȭ�� ǥ�� �޼ҵ�
    private void showSearchDiaryScreen() {
        JFrame searchFrame = new JFrame("�ϱ� �˻�");
        searchFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        searchFrame.setSize(400, 150);

        JTextField searchField = new JTextField(20);
        JButton searchButton = new JButton("�˻�");
        
        // �˻� ��ư Ŭ�� ��
        searchButton.addActionListener(e -> {
        	// �˻�� �ؽ�Ʈ �ʵ忡�� ������
            String searchTerm = searchField.getText();
        	// �ϱ� �˻� �� ��� ǥ��
            List<File> searchResults = searchDiaries(searchTerm);
            showSearchResults(searchResults);
            searchFrame.dispose();
        });

        JPanel searchPanel = new JPanel(new FlowLayout());
        searchPanel.add(new JLabel("�˻���:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        searchFrame.add(searchPanel);
        searchFrame.setLocationRelativeTo(mainFrame);
        searchFrame.setVisible(true);
        searchFrame.setResizable(false);
    }

    // �˻��� �������� �ϱ� �˻��ϴ� �޼ҵ�
    private List<File> searchDiaries(String searchTerm) {
    	// �˻� ����� ������ ����Ʈ
        List<File> searchResults = new ArrayList<>();
        // �ϱ� ���丮 ����
        File directory = new File(DIARY_DIRECTORY);
        // ���� ��� ��������
        File[] files = directory.listFiles((dir, name) -> name.endsWith(".txt"));

        // ������ �����ϴ� ���
        if (files != null) {
            for (File file : files) {
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    String line;
                    // ������ �� ���� ������ �˻�� ���Ե� ��� �˻� ����� �߰�
                    while ((line = reader.readLine()) != null) {
                        if (line.contains(searchTerm)) {
                            searchResults.add(file);
                            break;
                        }
                    }
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(mainFrame, "�˻� ����: " + ex.getMessage());
                }
            }
        }
        return searchResults;  // �˻� ��� ��ȯ
    }

    // �˻� ����� ȭ�鿡 ǥ���ϴ� �޼ҵ�
    private void showSearchResults(List<File> searchResults) {
        JFrame resultsFrame = new JFrame("�˻� ���");
        resultsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        resultsFrame.setSize(400, 300);

        // �˻� ��� ���� �̸� �迭 ���� (Ȯ���ڴ� ����)
        String[] resultNames = searchResults.stream().map(file -> file.getName().replace(".txt", "")).toArray(String[]::new);

        // �˻� ����� ǥ���� ����Ʈ
        JList<String> resultList = new JList<>(resultNames);
        // �ϳ��� ������ �� �ֵ��� ����
        resultList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JButton readButton = new JButton("�б�");
        JButton editButton = new JButton("����");
        JButton deleteButton = new JButton("����");

        // �б� ��ư Ŭ��
        readButton.addActionListener(e -> {
        	// ���õ� �׸��� ���� ���
            if (resultList.getSelectedIndex() != -1) {
            	// ���� �����ͼ� ǥ��
                File selectedFile = searchResults.get(resultList.getSelectedIndex());
                showDiary(selectedFile);
            }
        });

        // ���� ��ư Ŭ��
        editButton.addActionListener(e -> {
        	// ���õ� �׸��� ���� ���
            if (resultList.getSelectedIndex() != -1) {
            	// ���� �����ͼ� ����ȭ�� ǥ��
                File selectedFile = searchResults.get(resultList.getSelectedIndex());
                editDiary(selectedFile, resultsFrame);
            }
        });

        // ���� ��ư Ŭ��
        deleteButton.addActionListener(e -> {
        	// ���õ� �׸��� ���� ���
            if (resultList.getSelectedIndex() != -1) {
            	// ���õ� ������ ���� �� ���̾�α� ǥ��
                int option = JOptionPane.showConfirmDialog(resultsFrame, "������ �����Ͻðڽ��ϱ�?", "�ϱ� ����", JOptionPane.YES_NO_OPTION);
                // ����ڰ� Ȯ�� �� �ϱ� ����
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

    // �ϱ� ������ ȭ�鿡 ǥ����
    // ������ �Ұ���
    private void showDiary(File diaryFile) {
        JFrame diaryFrame = new JFrame(diaryFile.getName().replace(".txt", "") + " �ϱ�");
        // �ش� �����Ӹ� �ݾ������� ����
        diaryFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        diaryFrame.setSize(400, 300);

        JTextArea textArea = new JTextArea(10, 30);
        textArea.setEditable(false);

        // �ϱ� ������ �ؽ�Ʈ ����� ǥ��
        // ���� ó�� (���� �߻��� �޽��� ǥ��)
        try (BufferedReader reader = new BufferedReader(new FileReader(diaryFile))) {
            textArea.read(reader, null);
        } catch (IOException ex) {
            textArea.setText("�ϱ⸦ �д� �� ������ �߻��߽��ϴ�: " + ex.getMessage());
        }

        diaryFrame.setLayout(new BorderLayout());
        diaryFrame.add(new JScrollPane(textArea), BorderLayout.CENTER);

        // ���� ������ �������� ���̾ ������ ��ġ ��Ŵ
        diaryFrame.setLocationRelativeTo(mainFrame);
        diaryFrame.setVisible(true);
    }

    // ������ �ϱ� ������ ����, ����, ���� ���� ����
    private void editDiary(File diaryFile, JFrame listFrame) {
        JFrame editFrame = new JFrame("�ϱ� ����");
        // �ش� �����Ӹ� �ݾ������� ����
        editFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        editFrame.setSize(400, 400);

        JTextArea textArea = new JTextArea(10, 30);
        JButton saveButton = new JButton("����");

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

        datePanel.add(new JLabel("�⵵:"));
        datePanel.add(yearComboBox);
        datePanel.add(new JLabel("��:"));
        datePanel.add(monthComboBox);
        datePanel.add(new JLabel("��:"));
        datePanel.add(dayComboBox);

        try (BufferedReader reader = new BufferedReader(new FileReader(diaryFile))) {
            textArea.read(reader, null);
        } catch (IOException ex) {
            textArea.setText("�ϱ⸦ �д� �� ������ �߻��߽��ϴ�: " + ex.getMessage());
        }

        // ���� ��ư Ŭ�� ��
        saveButton.addActionListener(e -> {
        	// ������ ����� ���ڸ� ����Ͽ� ���ο� ���� ����
            String selectedDate = yearComboBox.getSelectedItem() + "-" +
                                  monthComboBox.getSelectedItem() + "-" +
                                  dayComboBox.getSelectedItem();
            String newTitle = titleField.getText().replaceAll("[^a-zA-Z0-9��-�R]", " ");
            File newFile = new File(DIARY_DIRECTORY + selectedDate + "_" + newTitle + ".txt");

            // ���� Ȥ�� ��¥�� ���� ���� ��� ���� ���� ����
            if (!diaryFile.equals(newFile)) {
                diaryFile.delete();
            }
            
            // ������ �ϱ� �� ���Ͽ� ����
            // ���� ó�� (���� �޽��� ǥ��)
            try (FileWriter writer = new FileWriter(newFile)) {
                writer.write(textArea.getText());
                JOptionPane.showMessageDialog(editFrame, "�ϱ� ���� �Ϸ�!");
                editFrame.dispose();  // ���� ������ �ݱ�
                listFrame.dispose();  // �ϱ� ��� ȭ�鵵 ����
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(editFrame, "�ϱ� ���� ����: " + ex.getMessage());
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

    // �ϱ� ���� �޼ҵ�
    private void deleteDiary(File file, JFrame parentFrame) {
        if (file.delete()) {
            JOptionPane.showMessageDialog(mainFrame, "�ϱ� ���� �Ϸ�!");
            parentFrame.dispose();
            showDiaryListScreen();
        } else {
            JOptionPane.showMessageDialog(mainFrame, "�ϱ� ���� ����!");
        }
    }

    // �˸� ���� ȭ���� ǥ���ϴ� �޼ҵ�
    private void showSetReminderScreen() {
        JFrame reminderFrame = new JFrame("�˸� ����");
        reminderFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        reminderFrame.setSize(300, 200);

        // �ð� ���� �޺��ڽ� �迭 ����
        String[] hours = new String[24];
        for (int i = 0; i < 24; i++) {
            hours[i] = String.format("%02d", i);
        }

        // �� ���� �޺��ڽ� �迭 (5�� ����)
        String[] minutes = new String[12];
        for (int i = 0; i < 12; i++) {
            minutes[i] = String.format("%02d", i * 5);
        }

        JComboBox<String> hourComboBox = new JComboBox<>(hours);
        JComboBox<String> minuteComboBox = new JComboBox<>(minutes);
        JButton setButton = new JButton("����");

        JPanel panel = new JPanel();
        panel.setLayout(null); // �����ġ�� ����

        // ��ġ �� ũ�� ����
        hourComboBox.setBounds(70, 40, 50, 25); // x, y, width, height
        JLabel hourLabel = new JLabel("��");
        hourLabel.setBounds(125, 40, 20, 25);
        minuteComboBox.setBounds(150, 40, 50, 25);
        JLabel minuteLabel = new JLabel("��");
        minuteLabel.setBounds(205, 40, 20, 25);
        setButton.setBounds(95, 90, 100, 30); // ���� ��ư ��ġ�� ũ�� ����

        panel.add(hourComboBox);
        panel.add(hourLabel);
        panel.add(minuteComboBox);
        panel.add(minuteLabel);
        panel.add(setButton);

        // ���� ��ư Ŭ�� �� ��� �߰�
        setButton.addActionListener(e -> {
            int hour = Integer.parseInt((String) hourComboBox.getSelectedItem());
            int minute = Integer.parseInt((String) minuteComboBox.getSelectedItem());
            setReminder(hour, minute);  // �˸� ����
            reminderFrame.dispose();  // ������ �ݱ�
        });

        reminderFrame.add(panel);
        reminderFrame.setLocationRelativeTo(null); // ȭ�� �߾ӿ� ��ġ
        reminderFrame.setVisible(true);
    }


    // �˸� ���� �޼ҵ�
    private void setReminder(int hour, int minute) {
    	// ������ Ÿ�̸� ���
        if (reminderTimer != null) {
            reminderTimer.cancel();
        }

        // ���� �ð� ��������
        Calendar now = Calendar.getInstance();
        // �˸� �ð� ����
        Calendar reminderTime = (Calendar) now.clone();
        reminderTime.set(Calendar.HOUR_OF_DAY, hour);
        reminderTime.set(Calendar.MINUTE, minute);
        reminderTime.set(Calendar.SECOND, 0);

        // �˸� �ð��� ���� �����̸�, �������� �������ֱ�
        if (reminderTime.before(now)) {
            reminderTime.add(Calendar.DAY_OF_MONTH, 1);
        }

        // �� Ÿ�̸� �����
        reminderTimer = new Timer();
        // �˸��� ���߾� ���� �ݺ��ϵ��� ����
        reminderTimer.schedule(new TimerTask() {
            @Override
            public void run() {
            	// �˸� �޽��� ǥ��
            	JOptionPane.showMessageDialog(mainFrame, "�ϱ� �ۼ� �ð��Դϴ�!");
            }
        }, reminderTime.getTime(), 24 * 60 * 60 * 1000);  // ���� �ݺ�
    }
    
    public void loginUser() {
        LoginUtils loginUtils = new LoginUtils(mainFrame);
        loginUtils.showLoginScreen();
    }
}
