package my.myungjin.academyDemo.iamport;

import com.google.gson.GsonBuilder;
import com.siot.IamportRestClient.Iamport;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import okhttp3.OkHttpClient;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.TestInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@SpringBootTest // H2 데이터베이스를 자동으로 실행
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class IamportTest {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private IamportClient iamportClient;

    @Before
    public void setup() {
        String test_api_key = "imp_apikey";
        String test_api_secret = "ekKoeW8RyKuT0zgaZsUtXXTLQ4AhPFW3ZGseDA6bkA5lamv9OqDMnxyeB9wqOsuO9W3Mx9YSJ4dTqJ3f";
        String API_URL = "https://api.iamport.kr";
        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create()))
                .build();

        Iamport iamport = retrofit.create(Iamport.class);
        iamportClient = new IamportClient(test_api_key, test_api_secret, iamport);
    }

    @Test
    public void testPaymentByImpUid() {
        try {

            String test_imp_uid = "imp_448280090638";
            IamportResponse<Payment> payment_response = iamportClient.paymentByImpUid(test_imp_uid);

            assertNotNull(payment_response.getResponse());
            assertEquals(test_imp_uid, payment_response.getResponse().getImpUid());
            log.info("Payment Response: {}", payment_response.getMessage());
        } catch (IamportResponseException e) {
            System.out.println(e.getMessage());

            switch(e.getHttpStatusCode()) {
                case 401 :
                    //TODO
                    break;
                case 500 :
                    //TODO
                    break;
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }
}