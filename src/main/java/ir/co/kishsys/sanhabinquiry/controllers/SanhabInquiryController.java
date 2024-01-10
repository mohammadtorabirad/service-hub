package ir.co.kishsys.sanhabinquiry.controllers;

import ir.co.kishsys.sanhabinquiry.services.InquiryService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/v1/sanhab")
@AllArgsConstructor
public class SanhabInquiryController {

    private final InquiryService inquiryService;

    @PostMapping(value = "/inquiry", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public String inquirySanhab(@RequestHeader Map<String, String> headers,
                                @RequestBody String body) {

        return inquiryService.inquiry(headers, body);
    }
}
