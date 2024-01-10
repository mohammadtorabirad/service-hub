package ir.co.kishsys.sanhabinquiry.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SanhabServiceInfo {

    private String url;
    private String soapAction;
    private String mediaType;
    private String userName;
    private String password;
}
