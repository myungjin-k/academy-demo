package my.myungjin.academyDemo.util;

import org.springframework.context.support.MessageSourceAccessor;

public class MessageUtil {

    private static MessageSourceAccessor messageSourceAccessor;

    public static void setMessageSourceAccessor(MessageSourceAccessor messageSourceAccessor){
        MessageUtil.messageSourceAccessor = messageSourceAccessor;
    }

    public static String getMessage(String key){
        return messageSourceAccessor.getMessage(key);
    }

    public static String getMessage(String key, Object... params){
        return messageSourceAccessor.getMessage(key, params);
    }
}
