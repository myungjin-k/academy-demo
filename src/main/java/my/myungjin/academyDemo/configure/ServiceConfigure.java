package my.myungjin.academyDemo.configure;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.google.gson.GsonBuilder;
import com.siot.IamportRestClient.Iamport;
import com.zaxxer.hikari.HikariDataSource;
import my.myungjin.academyDemo.aws.S3Client;
import my.myungjin.academyDemo.iamport.IamportClient;
import my.myungjin.academyDemo.service.mail.MailService;
import my.myungjin.academyDemo.util.MessageUtil;
import net.sf.log4jdbc.Log4jdbcProxyDataSource;
import okhttp3.OkHttpClient;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

@Configuration
public class ServiceConfigure {

    @Bean
    @Profile("test")
    public DataSource testDataSource() throws SQLException {
        DataSourceBuilder factory = DataSourceBuilder
                .create()
                .driverClassName("org.h2.Driver")
                .url("jdbc:h2:mem:test_academy;MODE=MYSQL;DB_CLOSE_ON_EXIT=FALSE;DB_CLOSE_DELAY=-1");
        HikariDataSource dataSource = (HikariDataSource) factory.build();
        dataSource.setPoolName("TEST_H2_DB");
        dataSource.setMaximumPoolSize(2);
/*
        return new Log4jdbcProxyDataSource(DataSourceBuilder
                .create()
                .driverClassName("org.h2.Driver")
                .url("jdbc:h2:mem:test_academy;MODE=MYSQL;DB_CLOSE_ON_EXIT=FALSE;DB_CLOSE_DELAY=-1")
                .build());*/
        return new Log4jdbcProxyDataSource(dataSource);
    }

    @Bean
    public MessageSourceAccessor messageSourceAccessor(MessageSource messageSource){
        MessageSourceAccessor messageSourceAccessor = new MessageSourceAccessor(messageSource);
        MessageUtil.setMessageSourceAccessor(messageSourceAccessor);
        return messageSourceAccessor;
    }

    @Bean
    public AmazonS3 amazonS3(AwsConfigure awsConfigure){
        return AmazonS3ClientBuilder.standard()
                .withRegion(awsConfigure.getRegion())
                .withCredentials(
                    new AWSStaticCredentialsProvider(
                            new BasicAWSCredentials(
                                awsConfigure.getAccessKey(),
                                awsConfigure.getSecretKey()
                            )
                    )
                ).build();
    }

    @Bean
    public S3Client s3Client(AmazonS3 amazonS3, AwsConfigure awsConfigure){
        return new S3Client(amazonS3, awsConfigure.getUrl(), awsConfigure.getBucketName());
    }

    @Bean
    public Iamport iamport(){
        final String API_URL = "https://api.iamport.kr";
        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create()))
                .client(client)
                .build();

        return retrofit.create(Iamport.class);
    }

    @Bean
    public IamportClient iamportClient(Iamport iamport, IamportConfigure iamportConfigure){
        return new IamportClient(iamportConfigure.getApiKey(), iamportConfigure.getApiSecret(), iamport);
    }

    @Bean
    public JavaMailSender getJavaMailSender(MailConfig mailConfig) {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(mailConfig.getHost());
        mailSender.setPort(Integer.parseInt(mailConfig.getPort()));
        mailSender.setUsername(mailConfig.getUsername());
        mailSender.setPassword(mailConfig.getPassword());

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        //props.put("mail.debug", "true");

        return mailSender;
    }


    @Bean
    public MailService mailService(MailConfig mailConfig){
        return new MailService(getJavaMailSender(mailConfig), thymeleafTemplateEngine());
    }

    @Bean
    public ResourceBundleMessageSource emailMessageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("mailMessages");
        return messageSource;
    }

    @Bean
    public ITemplateResolver thymeleafTemplateResolver() {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix("templates/static/mail/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode("HTML");
        templateResolver.setCharacterEncoding("UTF-8");
        return templateResolver;
    }

    @Bean
    public SpringTemplateEngine thymeleafTemplateEngine() {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(thymeleafTemplateResolver());
        templateEngine.setTemplateEngineMessageSource(emailMessageSource());
        return templateEngine;
    }
}
