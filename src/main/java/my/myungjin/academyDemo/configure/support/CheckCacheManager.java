package my.myungjin.academyDemo.configure.support;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CheckCacheManager implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(CheckCacheManager.class);

    private final CacheManager cacheManager;

    @Override
    public void run(String... args) throws Exception {
        logger.info("\n\n" + "=========================================================\n"
                + "Using cache manager: " + this.cacheManager.getClass().getName() + "\n"
                + "=========================================================\n\n");
    }
}
