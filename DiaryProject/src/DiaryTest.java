import java.io.File;
import java.io.IOException;

public class DiaryTest {
	
    private static final String USERS_FILE = "./users.txt";

    public static void main(String[] args) {
        DiaryApp diary = new DiaryApp();
        // 사용자 파일이 없을 경우 생성
        File usersFile = new File(USERS_FILE);
        if (!usersFile.exists()) {
            try {
                usersFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        diary.loginUser(); // 프로그램 시작 시 로그인 화면 표시
    }
}
