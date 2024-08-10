import java.io.File;
import java.io.IOException;

public class DiaryTest {
	
    private static final String USERS_FILE = "./users.txt";

    public static void main(String[] args) {
        DiaryApp diary = new DiaryApp();
        // ����� ������ ���� ��� ����
        File usersFile = new File(USERS_FILE);
        if (!usersFile.exists()) {
            try {
                usersFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        diary.loginUser(); // ���α׷� ���� �� �α��� ȭ�� ǥ��
    }
}
