package my.myungjin.academyDemo.iamport;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.siot.IamportRestClient.Iamport;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.request.AuthData;
import com.siot.IamportRestClient.request.CancelData;
import com.siot.IamportRestClient.response.AccessToken;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Call;
import retrofit2.HttpException;
import retrofit2.Response;

import java.io.IOException;

@RequiredArgsConstructor
public class IamportClient {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final String apiKey;

    private final String apiSecret;

    private final Iamport iamport;


    private IamportResponse<AccessToken> getAuth() throws IamportResponseException, IOException {

        Call<IamportResponse<AccessToken>> call = this.iamport.token( new AuthData(this.apiKey, this.apiSecret) );
        Response<IamportResponse<AccessToken>> response = call.execute();

        if ( !response.isSuccessful() )	throw new IamportResponseException( getExceptionMessage(response), new HttpException(response) );

        return response.body();
    }

    public IamportResponse<Payment> paymentByImpUid(String impUid) throws IamportResponseException, IOException {
        AccessToken auth = getAuth().getResponse();
        Call<IamportResponse<Payment>> call = this.iamport.payment_by_imp_uid(auth.getToken(), impUid);

        Response<IamportResponse<Payment>> response = call.execute();
        if ( !response.isSuccessful() )	throw new IamportResponseException( getExceptionMessage(response), new HttpException(response) );

        Payment r = response.body().getResponse();
        StringBuilder sb = new StringBuilder()
                .append(r.getImpUid()).append(",")
                .append(r.getStatus()).append(",")
                .append(r.getPayMethod()).append(",")
                .append(r.getBuyerName()).append(",")
                .append(r.getAmount());

        log.info("Iamport Response: {}", sb);
        return response.body();
    }

    public IamportResponse<Payment> cancelPaymentByImpUid(CancelData cancelData) throws IamportResponseException, IOException {
        AccessToken auth = getAuth().getResponse();
        Call<IamportResponse<Payment>> call = this.iamport.cancel_payment(auth.getToken(), cancelData);

        Response<IamportResponse<Payment>> response = call.execute();
        if ( !response.isSuccessful() )	throw new IamportResponseException( getExceptionMessage(response), new HttpException(response) );

        return response.body();
    }

    private String getExceptionMessage(Response<?> response) {
        String error = null;
        try {
            JsonElement jsonElement = JsonParser.parseString(response.errorBody().string());
            error = jsonElement.getAsJsonObject().get("message").getAsString();
        } catch (JsonSyntaxException e) {
            log.warn("Json Parse Error : {}", e.getMessage(), e);
        } catch (IOException e) {
            log.warn("I/O Error : {}", e.getMessage(), e);
        }

        if ( error == null )	error = response.message();

        return error;
    }

}
