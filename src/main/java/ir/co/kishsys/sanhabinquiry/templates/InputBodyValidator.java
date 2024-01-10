package ir.co.kishsys.sanhabinquiry.templates;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;
import ir.co.kishsys.sanhabinquiry.utils.ConfigLocator;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
public class InputBodyValidator implements TemplateValidator {

    private final ConfigLocator configLocator;

    @Override
    public void validateJson(String inputJson, String inputSchema) {

        try {

            ObjectMapper mapper = new ObjectMapper();
            JsonSchemaFactory factory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V4);
            JsonSchema jsonSchema = factory.getSchema(new FileInputStream(inputSchema));
            JsonNode jsonNode = mapper.readTree(inputJson);

            Set<ValidationMessage> validationresult = jsonSchema.validate(jsonNode);
            StringBuilder errorList = new StringBuilder();

            validationresult.stream().forEach(
                    vm -> errorList.append(vm.getCode()).append(StringUtils.SPACE).append(vm.getMessage()).append(System.getProperty("line.separator"))
            );

            Optional.of(errorList).ifPresent(list -> {
                if (list.length() > 0) {
                    throw new RuntimeException(list.toString());
                }

            });

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage() + StringUtils.SPACE + inputSchema);
        }
    }
}
