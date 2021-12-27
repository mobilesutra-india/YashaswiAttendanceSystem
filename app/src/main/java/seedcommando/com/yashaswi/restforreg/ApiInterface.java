package seedcommando.com.yashaswi.restforreg;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import seedcommando.com.yashaswi.pojos.LicensePOJO;
import seedcommando.com.yashaswi.pojos.RegistrationDetails;

/**
 * Created by commando4 on 1/2/2018.
 */

public interface ApiInterface {
    //Enterprise_MobileAppService //Empower_License//Enterprise_MobileAppService
    //cloude

    @FormUrlEncoded
    @POST("/Enterprise_MobileAppService/api/User/Registration")
    Call<RegistrationDetails> SendRegistrationData(@FieldMap Map<String, String> fields);

    @FormUrlEncoded
    @POST("/Enterprise_MobileAppService/api/User/VerifyLicenseNumber")
    Call<LicensePOJO> SendLicenseRegData(@FieldMap Map<String, String> fields);


}

