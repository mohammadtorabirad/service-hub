package ir.co.kishsys.sanhabinquiry.utils;

import java.util.Map;
import java.util.Optional;

public class HttpHeaders {

    public static String headerValue(Map<String, String> httpHeaders, String keyName) {

        return Optional.ofNullable(httpHeaders.get(keyName))
                .orElseThrow(() -> new RuntimeException("Service Name : "
                        + httpHeaders.get(keyName)
                        + " is Not defined in Inquiry Service List" ));

    }

}
