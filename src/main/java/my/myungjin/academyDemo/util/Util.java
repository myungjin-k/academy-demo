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
    public static String makeQuery(){
        String masterId = Util.getUUID(), itemId = Util.getUUID(), deliveryId = Util.getUUID(), deliveryItemId = Util.getUUID();
        return "INSERT INTO order_master (order_email, id, abbr_items_name, member_id, total_amount, order_name, order_tel, order_addr1, order_addr2, create_at) VALUES \n" +
                "('open7894.v2@gmail.com', '" + masterId + "','그랜드 핀턱 팬츠 (2color)', '3a18e633a5db4dbd8aaee218fe447fa4', 43000, '명진', '010-1234-5678','XX시 XX구 XX로', '1-1111', dateadd('hour', -3, current_timestamp));\n" +
                "INSERT INTO order_item (id, ORDER_ID, item_id, count, create_at) VALUES ('" + itemId +"', '" + masterId +"', 'c9402883dbe540e898a417e4884845bf', 1, dateadd('hour', -3, current_timestamp));\n"  +
                "INSERT INTO delivery (id, order_id, STATUS, RECEIVER_NAME, RECEIVER_TEL, RECEIVER_ADDR1, RECEIVER_ADDR2, create_at) VALUES \n" +
                " ('"+ deliveryId +"', '" + masterId +"', 1, '명진', '010-1234-5678','XX시 XX구 XX로', '1-1111', dateadd('hour', -3, current_timestamp));\n" +
                "INSERT INTO delivery_item (id, delivery_id, item_id, count, create_at) VALUES ('"+ deliveryItemId +"', '"+ deliveryId +"', 'c9402883dbe540e898a417e4884845bf', 1, dateadd('hour', -3, current_timestamp));\n" +
                "UPDATE order_item SET delivery_item_id = '"+ deliveryItemId +"' WHERE id = '"+ itemId +"';\n";
    }
    public static void main(String[] args) {

        System.out.println(Util.getUUID());
        System.out.println(Util.getUUID());
        System.out.println(Util.getUUID());

    }
}
