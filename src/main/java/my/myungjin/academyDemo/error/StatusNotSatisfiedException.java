package my.myungjin.academyDemo.error;

import my.myungjin.academyDemo.commons.Id;
import my.myungjin.academyDemo.util.MessageUtil;
import org.apache.commons.lang3.StringUtils;

public class StatusNotSatisfiedException extends ServiceRuntimeException{

    public static final String MESSAGE_KEY = "error.status.notSatisfied";

    public static final String MESSAGE_DETAILS = "error.status.notSatisfied.details";


    public StatusNotSatisfiedException(Class<?> cls, Id<?, String> key, Object... params){
        this(cls.getSimpleName(), key, params);
    }

    public StatusNotSatisfiedException(String targetName, Object key, Object[] params) {
        super(MESSAGE_KEY, MESSAGE_DETAILS, new String[]{
                targetName,
                key.toString(),
                (params != null && params.length > 0) ? StringUtils.join(params, ",") : ""
        });
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
