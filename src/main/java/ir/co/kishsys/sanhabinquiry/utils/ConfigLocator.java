package ir.co.kishsys.sanhabinquiry.utils;

import ir.co.kishsys.sanhabinquiry.config.PropsConfig;
import ir.co.kishsys.sanhabinquiry.models.SanhabServiceInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringSubstitutor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class ConfigLocator {

    private final PropsConfig propsConfig;
    @Value("${sanhab.username.key}")
    private String userNameKey;

    @Value("${sanhab.password.key}")
    private String passwordKey;

    public ConfigLocator(PropsConfig propsConfig) {
        this.propsConfig = propsConfig;
    }

    public String configValue(String serviceName) {
        return String.valueOf(propsConfig.loadProperties().get(serviceName));
    }

    public String prepareSoapBody(String serviceName, String srvBodyPath, String jsonBody) throws RuntimeException {
        try {
            String soapBodyTemplate = Files.readAllLines(Paths.get(srvBodyPath),
                    StandardCharsets.UTF_8).stream().collect(Collectors.joining());

            Map<String, Object> parameterizeMap = FormatConvertor.json2Map(jsonBody);
            SanhabServiceInfo sanhabServiceInfo = loadServiceConfig(serviceName);
            parameterizeMap.put(userNameKey, sanhabServiceInfo.getUserName());
            parameterizeMap.put(passwordKey, sanhabServiceInfo.getPassword());
            parameterizeMap.put(Constants.SOAP_Service_Version_Key, propsConfig.loadProperties().get(serviceName.concat(
                    Constants.SOAP_Service_Version_Value_Postfix)));

            Arrays.stream(propsConfig.loadProperties().get(serviceName.concat(Constants.Object_List)).toString().split(",")).forEach(
                    element -> {
                        try {
                            String embeddedObjFormatPath = propsConfig.loadProperties().get(serviceName.concat("_").concat(element)).toString();
                            String elementContent = Files.readAllLines(Paths.get(embeddedObjFormatPath),
                                    StandardCharsets.UTF_8).stream().collect(Collectors.joining());

                            StringBuilder embededValue = new StringBuilder();
                            ((ArrayList) parameterizeMap.get(element)).stream().forEach( linkeHashMap ->
                                    embededValue.append(StringSubstitutor.replace(elementContent, (LinkedHashMap) linkeHashMap, "${", "}"))
                            );
                            parameterizeMap.put(element , embededValue);


                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
            );
            String retVal = StringSubstitutor.replace(soapBodyTemplate, parameterizeMap, "${", "}");

            return removeInvalidNS(retVal);


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String removeInvalidNS(String soapEnv) {

        String retVal = soapEnv;
        while (retVal.contains(Constants.Invalid_Tag_Starter)) {

            int firstPartIndex = retVal.indexOf(Constants.Parameterize_String);
            String firstPart = retVal.substring(0, firstPartIndex);
            int firstPartlastIndex = firstPart.lastIndexOf("<");
            firstPart = firstPart.substring(0, firstPartlastIndex - 1);

            String secondPart = retVal.substring(firstPartIndex);
            int lasrPartIndex = secondPart.indexOf(">") + 1;
            secondPart = secondPart.substring(lasrPartIndex);

            retVal = firstPart.concat(secondPart);

        }
        return retVal;

    }

    public SanhabServiceInfo loadServiceConfig(String serviceName) {
        return new SanhabServiceInfo(
                propsConfig.loadProperties().get(serviceName.toLowerCase().concat(Constants.Service_URL_Postfix)).toString(),
                propsConfig.loadProperties().get(serviceName.toLowerCase().concat(Constants.SOAP_ACTION_Postfix)) != null ?
                        propsConfig.loadProperties().get(serviceName.toLowerCase().concat(Constants.SOAP_ACTION_Postfix)).toString() : null,
                propsConfig.loadProperties().get(serviceName.toLowerCase().concat(Constants.MediaType_Postfix)).toString(),
                propsConfig.loadProperties().get(Constants.Service_UserName_Prefix.concat(StringUtils.substringBetween(serviceName.toLowerCase()
                        , Constants.Naming_Delimiter , Constants.Naming_Delimiter))).toString(),
                propsConfig.loadProperties().get(Constants.Service_Password_Prefix.concat(StringUtils.substringBetween(serviceName.toLowerCase()
                        , Constants.Naming_Delimiter , Constants.Naming_Delimiter))).toString());
    }

}
