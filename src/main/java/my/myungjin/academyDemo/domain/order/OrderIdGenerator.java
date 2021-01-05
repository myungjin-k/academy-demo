package my.myungjin.academyDemo.domain.order;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

public class OrderIdGenerator implements IdentifierGenerator {
    @Override
    public Serializable generate(SharedSessionContractImplementor sharedSessionContractImplementor, Object o) throws HibernateException {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHH24mmss")) + randomAlphabetic(4).toUpperCase(Locale.ROOT);
    }
}
