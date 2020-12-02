package my.myungjin.academyDemo.error;

import my.myungjin.academyDemo.util.MessageUtil;
import org.apache.commons.lang3.StringUtils;

public class NotFoundException extends ServiceRuntimeException{

    static final String MESSAGE_KEY = "error.notfound";

    static final String MESSAGE_DETAILS = "error.notfound.details";

    public NotFoundException(Class<?> cls, Object... params){
        this(cls.getSimpleName(), params);
    }

    public NotFoundException(String targetName, Object... params){
        super(MESSAGE_KEY, MESSAGE_DETAILS,
                new String[]{targetName,
                        (params != null && params.length > 0) ? StringUtils.join(params, " ,") : ""});
    }

    @Override
    public String getMessage() {
        return MessageUtil.getMessage(getDetailKey(), getParams());
    }

    @Override
    public String toString() {
        return MessageUtil.getMessage(getMessageKey());
    }

}
