package ir.co.kishsys.sanhabinquiry.templates;

import com.networknt.schema.ValidationMessage;
import ir.co.kishsys.sanhabinquiry.models.exceptions.FormatException;

import java.util.Set;

public interface TemplateValidator {

    void validateJson( String inputJson , String inputSchema);
}
