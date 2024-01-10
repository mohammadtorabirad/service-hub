package ir.co.kishsys.sanhabinquiry.services;

import ir.co.kishsys.sanhabinquiry.config.PropsConfig;
import ir.co.kishsys.sanhabinquiry.services.centinsurance.SanhabService;
import ir.co.kishsys.sanhabinquiry.templates.InputBodyValidator;
import ir.co.kishsys.sanhabinquiry.utils.FormatConvertor;
import ir.co.kishsys.sanhabinquiry.utils.HttpHeaders;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

import ir.co.kishsys.sanhabinquiry.utils.Constants;

@Service
public class InquiryServiceImpl implements InquiryService {

    private final PropsConfig propsConfig;
    private final InputBodyValidator validator;
    private final SanhabService sanhabRestService;
    private final SanhabService sanhabSoapService;
    @Value("${http.header.service.name.key}")
    private String serviceNameKey;

    public InquiryServiceImpl(PropsConfig propsConfig, InputBodyValidator validator,

                              @Qualifier("callSanhabRestServices") SanhabService sanhabRestService,
                              @Qualifier("callSanhabSOAPServices") SanhabService sanhabSoapService) {
        this.propsConfig = propsConfig;
        this.validator = validator;
        this.sanhabRestService = sanhabRestService;
        this.sanhabSoapService = sanhabSoapService;
    }

    @Override
    public String inquiry(Map<String, String> headers, String body) {

        String serviceName = Constants.Service_Name_PREFIX.concat(HttpHeaders.headerValue(headers, serviceNameKey.toLowerCase()));

        body = FormatConvertor.prettyJson(body);
        validator.validateJson(body,
                propsConfig.loadProperties().get(serviceName.toLowerCase()).toString());

        String sanhabResponse = null;
        if (propsConfig.loadProperties().get(serviceName.toLowerCase().concat(Constants.Service_Type_Postfix))
                .toString().equalsIgnoreCase(Constants.Soap_Service_Type)) {
            sanhabResponse = sanhabSoapService.inquiry(serviceName, body);
        } else {
            sanhabResponse = sanhabRestService.inquiry(serviceName, body);
        }

        return sanhabResponse;

    }
}
