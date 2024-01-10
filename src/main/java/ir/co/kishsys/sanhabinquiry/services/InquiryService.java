package ir.co.kishsys.sanhabinquiry.services;

import java.util.Map;

public interface InquiryService {

    String inquiry( Map<String,String> headers , String body);
}
