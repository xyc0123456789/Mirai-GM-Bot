import com.king.util.DateFormateUtil;
import com.king.util.FileUtil;
import com.king.util.MyStringUtil;

import java.io.File;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class SensitiveSqlGenerator {

    public static String generate(String word){
        String formateSql = "INSERT INTO `qq_bot`.`sensitive_word`(`id`, `mseeage`, `lable`, `ext_data`, `ctime`, `utime`) VALUES (NULL, '%s', NULL, NULL, '%s', NULL);";
        return String.format(formateSql, word, DateFormateUtil.formatYYYYMMDDHHMMSS(new Date()));
    }

    public static String generate(String[] wordList){
        StringBuilder stringBuilder = new StringBuilder();
        String date = DateFormateUtil.formatYYYYMMDDHHMMSS(new Date());
        Set<String> all = new HashSet<>();
        String formateSql = "INSERT INTO `qq_bot`.`sensitive_word`(`id`, `mseeage`, `lable`, `ext_data`, `ctime`, `utime`) VALUES (NULL, '%s', NULL, NULL, '%s', NULL);";
        for (String word: wordList){
            if (MyStringUtil.isEmpty(word)){
                continue;
            }
            String trim = word.trim();
            if (all.contains(trim)){
                continue;
            }
            all.add(trim);
            stringBuilder.append(String.format(formateSql, trim, date)).append("\n");
        }
        return stringBuilder.toString();
    }

    public static void main(String[] args) {
        String s = FileUtil.readFile(new File("D:\\home\\appuser\\mirai_workplace\\sensitiveWordsBak\\政治类.txt"));
        String[] split = s.split("\n", -1);
        System.out.println(generate(split));

    }

}
