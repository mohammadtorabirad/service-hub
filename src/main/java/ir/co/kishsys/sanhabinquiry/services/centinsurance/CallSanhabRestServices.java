package ir.co.kishsys.sanhabinquiry.services.centinsurance;

import ir.co.kishsys.sanhabinquiry.models.SanhabServiceInfo;
import ir.co.kishsys.sanhabinquiry.utils.ConfigLocator;
import ir.co.kishsys.sanhabinquiry.utils.Constants;
import ir.co.kishsys.sanhabinquiry.utils.SecurityUtils;
import lombok.AllArgsConstructor;
import okhttp3.*;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@AllArgsConstructor
public class CallSanhabRestServices implements SanhabService {

    private final OkHttpClient okHttpClient;
    private final ConfigLocator configLocator;

    @Override
    public String inquiry(String serviceName, String jsonBody) {

        SanhabServiceInfo sanhabServiceInfo = configLocator.loadServiceConfig(serviceName);
        RequestBody body = RequestBody.create(
                jsonBody, MediaType.parse(sanhabServiceInfo.getMediaType()));

        Request request = new Request.Builder().url(sanhabServiceInfo.getUrl())
                .post(body)
                .addHeader(Constants.REST_Service_Authorization_Header_Name,
                        SecurityUtils.authorisation(sanhabServiceInfo.getUserName(),
                                sanhabServiceInfo.getPassword())
                )
                .addHeader(Constants.Service_Content_Type, sanhabServiceInfo.getMediaType())
//                //todo : read accepted encoding from config file
                .addHeader(Constants.Service_Accept_Type, Constants.Sanhab_Rest_Accepted_Encoding)
                .build();

        Response sanhabResp;

        try {
            sanhabResp = okHttpClient.newCall(request).execute();
            return sanhabResp.body().string();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
