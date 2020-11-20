package my.myungjin.academyDemo.util;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;
public class Util {

    public static String getUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static Timestamp timestampOf(LocalDateTime time) {
        return time == null ? null : Timestamp.valueOf(time);
    }

    public static LocalDateTime dateTimeOf(Timestamp timestamp) {
        return timestamp == null ? null : timestamp.toLocalDateTime();
    }
    public static void main(String[] args) {
        System.out.println(getUUID());
        System.out.println(getUUID());
        System.out.println(getUUID());
        System.out.println(getUUID());
        System.out.println(getUUID());
    }
}
