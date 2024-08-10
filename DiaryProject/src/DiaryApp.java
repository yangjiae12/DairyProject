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

    // ´ÙÀÌ¾î¸®¾Û »ý¼ºÀÚ
    public DiaryApp() {
        mainFrame = new JFrame("Diary App");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(400, 350);
        mainFrame.setLayout(null);

        JButton writeDiaryButton = new JButton("ÀÏ±â ÀÛ¼º");
        JButton listDiariesButton = new JButton("ÀÏ±â ¸ñ·Ï");
        JButton searchDiaryButton = new JButton("ÀÏ±â °Ë»ö");
        JButton setReminderButton = new JButton("¾Ë¸² ¼³Á¤");

        // ¹öÆ°¿¡ ´ëÇÑ ¾×¼Ç ¸®½º³Ê
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
        
        // LoginUtils °´Ã¼ »ý¼º ¹× ·Î±×¾Æ¿ô ¹öÆ° Ãß°¡
        LoginUtils loginUtils = new LoginUtils(mainFrame);
        loginUtils.addLogoutButton();

        // Áß¾Ó¿¡ ¹èÄ¡
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
        // Å©±â º¯°æ ºÒ°¡
        mainFrame.setResizable(false);
    }
    
    // ÀÏ±â ÀÛ¼º È­¸éÀ» Ç¥½ÃÇÏ´Â ¸Þ¼Òµå
    private void showWriteDiaryScreen() {
        JFrame writeFrame = new JFrame("ÀÏ±â ÀÛ¼º");
        
        // ÇÁ·Î±×·¥ ÀÚÃ¼°¡ ¾Æ´Ï¶ó Ã¢¸¸ ´ÝÈû
        writeFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        writeFrame.setSize(400, 350);

        JTextField titleField = new JTextField(20);  // Á¦¸ñ ÀÔ·Â¶õ
        JTextArea textArea = new JTextArea(10, 30);  // ³»¿ë ÀÔ·Â¶õ
        
        JButton saveButton = new JButton("ÀúÀå");

        JPanel datePanel = new JPanel(new FlowLayout());

        // ¿¬µµ ¼±ÅÃ ÄÞº¸¹Ú½º
        JComboBox<String> yearComboBox = new JComboBox<>(generateYearOptions());
        // ¿ù ¼±ÅÃ ÄÞº¸ ¹Ú½º
        JComboBox<String> monthComboBox = new JComboBox<>(generateMonthOptions());
        // ÀÏÀÚ ¼±ÅÃ ÄÞº¸ ¹Ú½º
        JComboBox<String> dayComboBox = new JComboBox<>(generateDayOptions());

        // ¿À´ÃÀÇ ³¯Â¥¸¦ ÄÞº¸¹Ú½ºÀÇ ±âº»°ªÀ¸·Î ¼³Á¤ÇØµÒ
        // Ä¶¸°´õ °´Ã¼ »ç¿ë
        Calendar calendar = Calendar.getInstance();
        yearComboBox.setSelectedItem(String.valueOf(calendar.get(Calendar.YEAR)));
        // ¿ùÀ» ¼±ÅÃÇÒ ¶§ µÎ ÀÚ¸®·Î ¼³Á¤ÇØµÎ°í, +1À» ÇØÁÜÀ¸·Î½á ½ÇÁ¦ ³¯Â¥ÀÇ ¿ùÀ» °¡Á®¿Ã ¼ö ÀÖµµ·Ï ÇÔ (Ä¶¸°´õ¿¡¼­ ¿ùÀÌ 0ºÎÅÍ ½ÃÀÛ)
        monthComboBox.setSelectedItem(String.format("%02d", calendar.get(Calendar.MONTH) + 1));
        // ÀÏÀÚ¸¦ ¼±ÅÃÇÒ ¶§ µÎ ÀÚ¸®·Î ÁöÁ¤
        dayComboBox.setSelectedItem(String.format("%02d", calendar.get(Calendar.DAY_OF_MONTH)));

        datePanel.add(new JLabel("³âµµ:"));
        datePanel.add(yearComboBox);
        datePanel.add(new JLabel("¿ù:"));
        datePanel.add(monthComboBox);
        datePanel.add(new JLabel("ÀÏ:"));
        datePanel.add(dayComboBox);

        saveButton.addActionListener(e -> {
        	// getSelectedItem()¸¦ ÅëÇØ ÄÞº¸¹Ú½º¿¡¼­ ¼±ÅÃµÈ Ç×¸ñ ¹ÝÈ¯
        	// ¼±ÅÃÇÑ ³¯Â¥ÀÇ ¹®ÀÚ¿­À» »ý¼ºÇÏµµ·Ï ÇÔ
            String selectedDate = yearComboBox.getSelectedItem() + "-" +
                                  monthComboBox.getSelectedItem() + "-" +
                                  dayComboBox.getSelectedItem();
            // ÀÔ·ÂµÈ ÀÏ±â Á¤º¸¸¦ ÀúÀå
            saveDiary(selectedDate, titleField.getText(), textArea.getText());
            // ÀÏ±â ÀÛ¼ºÃ¢ ´Ý±â
            writeFrame.dispose();
        });

        writeFrame.setLayout(new BorderLayout());
        JPanel topPanel = new JPanel(new GridLayout(2, 1));
        topPanel.add(datePanel);
        topPanel.add(titleField);
        writeFrame.add(topPanel, BorderLayout.NORTH);
        writeFrame.add(new JScrollPane(textArea), BorderLayout.CENTER);
        writeFrame.add(saveButton, BorderLayout.SOUTH);

        // ¸ÞÀÎ ÇÁ·¹ÀÓ Áß¾Ó¿¡ À§Ä¡
        writeFrame.setLocationRelativeTo(mainFrame);
        writeFrame.setVisible(true);
    }

    // ÃÖ±Ù 10³â °£ÀÇ ¿¬µµ ¼±ÅÃÁö ¸¸µé¾îµÒ
    private String[] generateYearOptions() {
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        String[] years = new String[10];
        for (int i = 0; i < 10; i++) {
            years[i] = String.valueOf(currentYear - i);
        }
        return years;
    }

    // 1¿ùºÎÅÍ 12¿ù±îÁö ¼±ÅÃÁö
    private String[] generateMonthOptions() {
        String[] months = new String[12];
        for (int i = 0; i < 12; i++) {
            months[i] = String.format("%02d", i + 1);
        }
        return months;
    }

    // 1ÀÏºÎÅÍ 31ÀÏ±îÁöÀÇ ¼±ÅÃÁö
    private String[] generateDayOptions() {
        String[] days = new String[31];
        for (int i = 0; i < 31; i++) {
            days[i] = String.format("%02d", i + 1);
        }
        return days;
    }
    
    // ÀÏ±â ÀúÀå ¸Þ¼Òµå
    private void saveDiary(String date, String title, String content) {
    	// ÆÄÀÏ ÀÌ¸§ »ý¼º : ³¯Â¥_Á¦¸ñ Çü½ÄÀ¸·Î (Á¤±Ô½Ä ÀÌ¿ëÇØ Æ¯¼ö¹®ÀÚ ´ëÃ¼
        String fileName = date + "_" + title.replaceAll("[^a-zA-Z0-9°¡-ÆR]", " ") + ".txt";
        // ÀÏ±â µð·ºÅä¸®
        File diaryDir = new File(DIARY_DIRECTORY);
        if (!diaryDir.exists()) {
            diaryDir.mkdir();
        }
        // ÆÄÀÏ ¾²±â
        try (FileWriter writer = new FileWriter(DIARY_DIRECTORY + fileName)) {
            writer.write(content);
            JOptionPane.showMessageDialog(mainFrame, "ÀÏ±â ÀúÀå ¿Ï·á!");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(mainFrame, "ÀÏ±â ÀúÀå ½ÇÆÐ: " + ex.getMessage());
        }
    }

    // ÀÏ±â ¸ñ·Ï È­¸é Ç¥½Ã
    private void showDiaryListScreen() {
        JFrame listFrame = new JFrame("ÀÏ±â ¸ñ·Ï");
        listFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        listFrame.setSize(400, 300);

        // ÀÏ±â µð·ºÅä¸® °´Ã¼
        File directory = new File(DIARY_DIRECTORY);
        File[] files = directory.listFiles((dir, name) -> name.endsWith(".txt"));
        
        String[] fileNames;
        // ÆÄÀÏÀÌ ¾øÀ» ¶¼ ¸Þ½ÃÁö
        if (files == null || files.length == 0) {
            fileNames = new String[]{"ÀúÀåµÈ ÀÏ±â°¡ ¾ø½À´Ï´Ù."};
        } else {
        	// ÀÖÀ» °æ¿ì ÆÄÀÏ ÀÌ¸§ ¹è¿­ »ý¼º
            fileNames = new String[files.length];
            for (int i = 0; i < files.length; i++) {
                fileNames[i] = files[i].getName().replace(".txt", "");
            }
        }

        JList<String> diaryList = new JList<>(fileNames);
        diaryList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JButton readButton = new JButton("ÀÐ±â");
        JButton editButton = new JButton("¼öÁ¤");
        JButton deleteButton = new JButton("»èÁ¦");

        // ÀÐ±â ¹öÆ° Å¬¸¯
        readButton.addActionListener(e -> {
        	// ¼±ÅÃµÈ Ç×¸ñÀÌ ÀÖÀ» °æ¿ì
            if (diaryList.getSelectedIndex() != -1 && files != null && files.length != 0) {
            	// ÆÄÀÏ °¡Á®¿Í¼­ Ç¥½Ã
            	File selectedFile = files[diaryList.getSelectedIndex()];
                showDiary(selectedFile);
            }
        });

        // ¼öÁ¤ ¹öÆ° Å¬¸¯
        editButton.addActionListener(e -> {
        	// ¼±ÅÃµÈ Ç×¸ñÀÌ ÀÖÀ» °æ¿ì
            if (diaryList.getSelectedIndex() != -1 && files != null && files.length != 0) {
            	// ÆÄÀÏ °¡Á®¿Í¼­ ¼öÁ¤È­¸é Ç¥½Ã
            	File selectedFile = files[diaryList.getSelectedIndex()];
                editDiary(selectedFile, listFrame);
            }
        });

        // »èÁ¦ ¹öÆ° Å¬¸¯
        deleteButton.addActionListener(e -> {
        	// ¼±ÅÃµÈ Ç×¸ñÀÌ ÀÖÀ» °æ¿ì
            if (diaryList.getSelectedIndex() != -1 && files != null && files.length != 0) {
            	// ¼±ÅÃµÈ ÆÄÀÏÀÌ ÀÖÀ» ¶§ ´ÙÀÌ¾ó·Î±× Ç¥½Ã
            	int option = JOptionPane.showConfirmDialog(listFrame, "Á¤¸»·Î »èÁ¦ÇÏ½Ã°Ú½À´Ï±î?", "ÀÏ±â »èÁ¦", JOptionPane.YES_NO_OPTION);
                // »ç¿ëÀÚ°¡ È®ÀÎ ½Ã ÀÏ±â »èÁ¦
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

        // ¸ÞÀÎ ÇÁ·¹ÀÓ Áß¾Ó¿¡ ¹èÄ¡
        listFrame.setLocationRelativeTo(mainFrame);
        listFrame.setVisible(true);
    }

    // ÀÏ±â °Ë»ö È­¸é Ç¥½Ã ¸Þ¼Òµå
    private void showSearchDiaryScreen() {
        JFrame searchFrame = new JFrame("ÀÏ±â °Ë»ö");
        searchFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        searchFrame.setSize(400, 150);

        JTextField searchField = new JTextField(20);
        JButton searchButton = new JButton("°Ë»ö");
        
        // °Ë»ö ¹öÆ° Å¬¸¯ ½Ã
        searchButton.addActionListener(e -> {
        	// °Ë»ö¾î¸¦ ÅØ½ºÆ® ÇÊµå¿¡¼­ °¡Á®¿Í
            String searchTerm = searchField.getText();
        	// ÀÏ±â °Ë»ö ÈÄ °á°ú Ç¥½Ã
            List<File> searchResults = searchDiaries(searchTerm);
            showSearchResults(searchResults);
            searchFrame.dispose();
        });

        JPanel searchPanel = new JPanel(new FlowLayout());
        searchPanel.add(new JLabel("°Ë»ö¾î:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        searchFrame.add(searchPanel);
        searchFrame.setLocationRelativeTo(mainFrame);
        searchFrame.setVisible(true);
        searchFrame.setResizable(false);
    }

    // °Ë»ö¾î ±âÁØÀ¸·Î ÀÏ±â °Ë»öÇÏ´Â ¸Þ¼Òµå
    private List<File> searchDiaries(String searchTerm) {
    	// °Ë»ö °á°ú¸¦ ÀúÀåÇÒ ¸®½ºÆ®
        List<File> searchResults = new ArrayList<>();
        // ÀÏ±â µð·ºÅä¸® ¼³Á¤
        File directory = new File(DIARY_DIRECTORY);
        // ÆÄÀÏ ¸ñ·Ï °¡Á®¿À±â
        File[] files = directory.listFiles((dir, name) -> name.endsWith(".txt"));

        // ÆÄÀÏÀÌ Á¸ÀçÇÏ´Â °æ¿ì
        if (files != null) {
            for (File file : files) {
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    String line;
                    // ÆÄÀÏÀÇ °¢ ÁÙÀ» ÀÐÀ¸¸ç °Ë»ö¾î°¡ Æ÷ÇÔµÈ °æ¿ì °Ë»ö °á°ú¿¡ Ãß°¡
                    while ((line = reader.readLine()) != null) {
                        if (line.contains(searchTerm)) {
                            searchResults.add(file);
                            break;
                        }
                    }
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(mainFrame, "°Ë»ö ¿À·ù: " + ex.getMessage());
                }
            }
        }
        return searchResults;  // °Ë»ö °á°ú ¹ÝÈ¯
    }

    // °Ë»ö °á°ú¸¦ È­¸é¿¡ Ç¥½ÃÇÏ´Â ¸Þ¼Òµå
    private void showSearchResults(List<File> searchResults) {
        JFrame resultsFrame = new JFrame("°Ë»ö °á°ú");
        resultsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        resultsFrame.setSize(400, 300);

        // °Ë»ö °á°ú ÆÄÀÏ ÀÌ¸§ ¹è¿­ »ý¼º (È®ÀåÀÚ´À Á¦°Å)
        String[] resultNames = searchResults.stream().map(file -> file.getName().replace(".txt", "")).toArray(String[]::new);

        // °Ë»ö °á°ú¸¦ Ç¥½ÃÇÒ ¸®½ºÆ®
        JList<String> resultList = new JList<>(resultNames);
        // ÇÏ³ª¸¸ ¼±ÅÃÇÒ ¼ö ÀÖµµ·Ï ¼³Á¤
        resultList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JButton readButton = new JButton("ÀÐ±â");
        JButton editButton = new JButton("¼öÁ¤");
        JButton deleteButton = new JButton("»èÁ¦");

        // ÀÐ±â ¹öÆ° Å¬¸¯
        readButton.addActionListener(e -> {
        	// ¼±ÅÃµÈ Ç×¸ñÀÌ ÀÖÀ» °æ¿ì
            if (resultList.getSelectedIndex() != -1) {
            	// ÆÄÀÏ °¡Á®¿Í¼­ Ç¥½Ã
                File selectedFile = searchResults.get(resultList.getSelectedIndex());
                showDiary(selectedFile);
            }
        });

        // ¼öÁ¤ ¹öÆ° Å¬¸¯
        editButton.addActionListener(e -> {
        	// ¼±ÅÃµÈ Ç×¸ñÀÌ ÀÖÀ» °æ¿ì
            if (resultList.getSelectedIndex() != -1) {
            	// ÆÄÀÏ °¡Á®¿Í¼­ ¼öÁ¤È­¸é Ç¥½Ã
                File selectedFile = searchResults.get(resultList.getSelectedIndex());
                editDiary(selectedFile, resultsFrame);
            }
        });

        // »èÁ¦ ¹öÆ° Å¬¸¯
        deleteButton.addActionListener(e -> {
        	// ¼±ÅÃµÈ Ç×¸ñÀÌ ÀÖÀ» °æ¿ì
            if (resultList.getSelectedIndex() != -1) {
            	// ¼±ÅÃµÈ ÆÄÀÏÀÌ ÀÖÀ» ¶§ ´ÙÀÌ¾ó·Î±× Ç¥½Ã
                int option = JOptionPane.showConfirmDialog(resultsFrame, "Á¤¸»·Î »èÁ¦ÇÏ½Ã°Ú½À´Ï±î?", "ÀÏ±â »èÁ¦", JOptionPane.YES_NO_OPTION);
                // »ç¿ëÀÚ°¡ È®ÀÎ ½Ã ÀÏ±â »èÁ¦
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

    // ÀÏ±â ÆÄÀÏÀ» È­¸é¿¡ Ç¥½ÃÇÔ
    // ¼öÁ¤Àº ºÒ°¡´É
    private void showDiary(File diaryFile) {
        JFrame diaryFrame = new JFrame(diaryFile.getName().replace(".txt", "") + " ÀÏ±â");
        // ÇØ´ç ÇÁ·¹ÀÓ¸¸ ´Ý¾ÆÁöµµ·Ï ¼³Á¤
        diaryFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        diaryFrame.setSize(400, 300);

        JTextArea textArea = new JTextArea(10, 30);
        textArea.setEditable(false);

        // ÀÏ±â ³»¿ëÀ» ÅØ½ºÆ® ¿¡¸®¾î¿¡ Ç¥½Ã
        // ¿¹¿Ü Ã³¸® (¿À·ù ¹ß»ý½Ã ¸Þ½ÃÁö Ç¥½Ã)
        try (BufferedReader reader = new BufferedReader(new FileReader(diaryFile))) {
            textArea.read(reader, null);
        } catch (IOException ex) {
            textArea.setText("ÀÏ±â¸¦ ÀÐ´Â Áß ¿À·ù°¡ ¹ß»ýÇß½À´Ï´Ù: " + ex.getMessage());
        }

        diaryFrame.setLayout(new BorderLayout());
        diaryFrame.add(new JScrollPane(textArea), BorderLayout.CENTER);

        // ¸ÞÀÎ ÇÁ·¹ÀÓ ±âÁØÀ¸·Î ´ÙÀÌ¾î¸® ÇÁ·¹ÀÓ À§Ä¡ ½ÃÅ´
        diaryFrame.setLocationRelativeTo(mainFrame);
        diaryFrame.setVisible(true);
    }

    // ¼±ÅÃÇÑ ÀÏ±â ÆÄÀÏÀÇ Á¦¸ñ, ³¯ÀÚ, ³»¿ë º¯°æ °¡´É
    private void editDiary(File diaryFile, JFrame listFrame) {
        JFrame editFrame = new JFrame("ÀÏ±â ¼öÁ¤");
        // ÇØ´ç ÇÁ·¹ÀÓ¸¸ ´Ý¾ÆÁöµµ·Ï ¼³Á¤
        editFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        editFrame.setSize(400, 400);

        JTextArea textArea = new JTextArea(10, 30);
        JButton saveButton = new JButton("ÀúÀå");

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

        datePanel.add(new JLabel("³âµµ:"));
        datePanel.add(yearComboBox);
        datePanel.add(new JLabel("¿ù:"));
        datePanel.add(monthComboBox);
        datePanel.add(new JLabel("ÀÏ:"));
        datePanel.add(dayComboBox);

        try (BufferedReader reader = new BufferedReader(new FileReader(diaryFile))) {
            textArea.read(reader, null);
        } catch (IOException ex) {
            textArea.setText("ÀÏ±â¸¦ ÀÐ´Â Áß ¿À·ù°¡ ¹ß»ýÇß½À´Ï´Ù: " + ex.getMessage());
        }

        // ÀúÀå ¹öÆ° Å¬¸¯ ½Ã
        saveButton.addActionListener(e -> {
        	// ¼öÁ¤ÇÑ Á¦¸ñ°ú ³¯ÀÚ¸¦ ±â¹ÝÇÏ¿© »õ·Î¿î ÆÄÀÏ »ý¼º
            String selectedDate = yearComboBox.getSelectedItem() + "-" +
                                  monthComboBox.getSelectedItem() + "-" +
                                  dayComboBox.getSelectedItem();
            String newTitle = titleField.getText().replaceAll("[^a-zA-Z0-9°¡-ÆR]", " ");
            File newFile = new File(DIARY_DIRECTORY + selectedDate + "_" + newTitle + ".txt");

            // Á¦¸ñ È¤Àº ³¯Â¥°¡ ¼öÁ¤ µÆÀ» °æ¿ì ±âÁ¸ ÆÄÀÏ »èÁ¦
            if (!diaryFile.equals(newFile)) {
                diaryFile.delete();
            }
            
            // ¼öÁ¤µÈ ÀÏ±â »õ ÆÄÀÏ¿¡ ÀúÀå
            // ¿¹¿Ü Ã³¸® (¿À·ù ¸Þ½ÃÁö Ç¥½Ã)
            try (FileWriter writer = new FileWriter(newFile)) {
                writer.write(textArea.getText());
                JOptionPane.showMessageDialog(editFrame, "ÀÏ±â ¼öÁ¤ ¿Ï·á!");
                editFrame.dispose();  // ÇöÀç ÇÁ·¹ÀÓ ´Ý±â
                listFrame.dispose();  // ÀÏ±â ¸ñ·Ï È­¸éµµ ´ÝÀ½
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(editFrame, "ÀÏ±â ¼öÁ¤ ½ÇÆÐ: " + ex.getMessage());
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

    // ÀÏ±â »èÁ¦ ¸Þ¼Òµå
    private void deleteDiary(File file, JFrame parentFrame) {
        if (file.delete()) {
            JOptionPane.showMessageDialog(mainFrame, "ÀÏ±â »èÁ¦ ¿Ï·á!");
            parentFrame.dispose();
            showDiaryListScreen();
        } else {
            JOptionPane.showMessageDialog(mainFrame, "ÀÏ±â »èÁ¦ ½ÇÆÐ!");
        }
    }

    // ¾Ë¸² ¼³Á¤ È­¸éÀ» Ç¥½ÃÇÏ´Â ¸Þ¼Òµå
    private void showSetReminderScreen() {
        JFrame reminderFrame = new JFrame("¾Ë¸² ¼³Á¤");
        reminderFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        reminderFrame.setSize(300, 200);

        // ½Ã°£ ¼±ÅÃ ÄÞº¸¹Ú½º ¹è¿­ ¼³Á¤
        String[] hours = new String[24];
        for (int i = 0; i < 24; i++) {
            hours[i] = String.format("%02d", i);
        }

        // ºÐ ¼±ÅÃ ÄÞº¸¹Ú½º ¹è¿­ (5ºÐ ´ÜÀ§)
        String[] minutes = new String[12];
        for (int i = 0; i < 12; i++) {
            minutes[i] = String.format("%02d", i * 5);
        }

        JComboBox<String> hourComboBox = new JComboBox<>(hours);
        JComboBox<String> minuteComboBox = new JComboBox<>(minutes);
        JButton setButton = new JButton("¼³Á¤");

        JPanel panel = new JPanel();
        panel.setLayout(null); // Àý´ë¹èÄ¡·Î º¯°æ

        // À§Ä¡ ¹× Å©±â ¼³Á¤
        hourComboBox.setBounds(70, 40, 50, 25); // x, y, width, height
        JLabel hourLabel = new JLabel("½Ã");
        hourLabel.setBounds(125, 40, 20, 25);
        minuteComboBox.setBounds(150, 40, 50, 25);
        JLabel minuteLabel = new JLabel("ºÐ");
        minuteLabel.setBounds(205, 40, 20, 25);
        setButton.setBounds(95, 90, 100, 30); // ¼³Á¤ ¹öÆ° À§Ä¡¿Í Å©±â ¼³Á¤

        panel.add(hourComboBox);
        panel.add(hourLabel);
        panel.add(minuteComboBox);
        panel.add(minuteLabel);
        panel.add(setButton);

        // ¼³Á¤ ¹öÆ° Å¬¸¯ ½Ã ±â´É Ãß°¡
        setButton.addActionListener(e -> {
            int hour = Integer.parseInt((String) hourComboBox.getSelectedItem());
            int minute = Integer.parseInt((String) minuteComboBox.getSelectedItem());
            setReminder(hour, minute);  // ¾Ë¸² ¼³Á¤
            reminderFrame.dispose();  // ÇÁ·¹ÀÓ ´Ý±â
        });

        reminderFrame.add(panel);
        reminderFrame.setLocationRelativeTo(null); // È­¸é Áß¾Ó¿¡ ¹èÄ¡
        reminderFrame.setVisible(true);
    }


    // ¾Ë¸² ¼³Á¤ ¸Þ¼Òµå
    private void setReminder(int hour, int minute) {
    	// ±âÁ¸ÀÇ Å¸ÀÌ¸Ó Ãë¼Ò
        if (reminderTimer != null) {
            reminderTimer.cancel();
        }

        // ÇöÀç ½Ã°£ °¡Á®¿À±â
        Calendar now = Calendar.getInstance();
        // ¾Ë¸² ½Ã°£ ¼³Á¤
        Calendar reminderTime = (Calendar) now.clone();
        reminderTime.set(Calendar.HOUR_OF_DAY, hour);
        reminderTime.set(Calendar.MINUTE, minute);
        reminderTime.set(Calendar.SECOND, 0);

        // ¾Ë¸² ½Ã°£ÀÌ ÇöÀç ÀÌÀüÀÌ¸é, ´ÙÀ½³¯·Î ¼³Á¤ÇØÁÖ±â
        if (reminderTime.before(now)) {
            reminderTime.add(Calendar.DAY_OF_MONTH, 1);
        }

        // »õ Å¸ÀÌ¸Ó ¸¸µé±â
        reminderTimer = new Timer();
        // ¾Ë¸²¿¡ ¸ÂÃß¾î ¸ÅÀÏ ¹Ýº¹ÇÏµµ·Ï ¼³Á¤
        reminderTimer.schedule(new TimerTask() {
            @Override
            public void run() {
            	// ¾Ë¸² ¸Þ½ÃÁö Ç¥½Ã
            	JOptionPane.showMessageDialog(mainFrame, "ÀÏ±â ÀÛ¼º ½Ã°£ÀÔ´Ï´Ù!");
            }
        }, reminderTime.getTime(), 24 * 60 * 60 * 1000);  // ¸ÅÀÏ ¹Ýº¹
    }
    
    public void loginUser() {
        LoginUtils loginUtils = new LoginUtils(mainFrame);
        loginUtils.showLoginScreen();
    }
}
