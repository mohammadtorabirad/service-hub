package ir.co.kishsys.sanhabinquiry.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.stereotype.Service;

import java.util.ArrayDeque;
import java.util.Map;
import java.util.Queue;

import static org.apache.logging.log4j.ThreadContext.containsKey;


@Service
public class FormatConvertor {


    public static String xml2Json(String xmlMsg) {
        try {
//            XmlMapper xmlMapper = new XmlMapper();
//            JsonNode node = xmlMapper.readTree(xmlMsg.getBytes());
//
//            ObjectMapper jsonMapper = new ObjectMapper();
//            return jsonMapper.writeValueAsString(node);


            JSONObject jsonObj = XML.toJSONObject(xmlMsg);
            return jsonObj.toString(Constants.JSON_INDENTATION);


        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public static String json2Xml(String jsonMsg) {

        return "";
    }

    public static String prettyJson(String inputJson) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readTree(inputJson).fields().next().getValue().toPrettyString();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String extractJsonSpecificTag(String jsonInput, String tagName) {

        ObjectMapper mapper = new ObjectMapper();
        try {

            JsonNode jsonNode = mapper.readTree(jsonInput)
                    .fields()
                    .next().getValue().get("s:Body").fields().next().getValue().get(tagName);
            return jsonNode.toPrettyString();

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }

    public static Map json2Map(String jsonInput) {

        try {
            return new ObjectMapper().readValue(
                    jsonInput, new TypeReference<Map<String, Object>>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

}
