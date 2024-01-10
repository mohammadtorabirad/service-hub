package ir.co.kishsys.sanhabinquiry.services.centinsurance;

import ir.co.kishsys.sanhabinquiry.models.SanhabServiceInfo;
import ir.co.kishsys.sanhabinquiry.utils.ConfigLocator;
import ir.co.kishsys.sanhabinquiry.utils.Constants;
import ir.co.kishsys.sanhabinquiry.utils.FormatConvertor;
import lombok.AllArgsConstructor;
import okhttp3.*;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;

import java.io.IOException;


@Service
@AllArgsConstructor
public class CallSanhabSOAPServices implements SanhabService {

    private final OkHttpClient okHttpClient;
    private final ConfigLocator configLocator;

    public String inquiry(String serviceName, String jsonBody) {

        String srvBodyPath = configLocator.configValue(serviceName.toLowerCase().concat(Constants.SOAP_BODY_PREFIX));
        String soapBody = configLocator.prepareSoapBody(serviceName, srvBodyPath, jsonBody);

        SanhabServiceInfo sanhabServiceInfo = configLocator.loadServiceConfig(serviceName);
        RequestBody body = RequestBody.create(
                soapBody, MediaType.parse(sanhabServiceInfo.getMediaType()));
        Request request = new Request.Builder().url(sanhabServiceInfo.getUrl())
                .post(body).addHeader(Constants.SOAP_ACTION_KEY, sanhabServiceInfo.getSoapAction())
                .build();
        Response sanhabResp;
        String responseBody = null;

        try {
            sanhabResp = okHttpClient.newCall(request).execute();
            responseBody = sanhabResp.body().string();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (sanhabResp.code() == HttpStatus.OK.value()) {
            String sanhabConvertedRsp = FormatConvertor.xml2Json(responseBody);
            return FormatConvertor.extractJsonSpecificTag(sanhabConvertedRsp,
                    configLocator.configValue(serviceName.concat(Constants.SOAP_Service_Response_Postfix))
            );

        } else {
            return FormatConvertor.xml2Json(responseBody);
        }

    }

}
