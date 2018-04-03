package de.rgzm.alligator.log;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Logging {

    public static String getMessageJSON(Exception exception, String endClass) {
        // START BUILD JSON
        JSONObject jsonobj_error = new JSONObject(); // {}
        JSONObject jsonobj_error_data = new JSONObject(); // {}
        JSONArray jsonarray_element = new JSONArray();
        for (StackTraceElement element : exception.getStackTrace()) {
            JSONObject errMessage = new JSONObject();
            errMessage.put("class", element.getClassName());
            errMessage.put("method", element.getMethodName());
            errMessage.put("line", element.getLineNumber());
            jsonarray_element.add(errMessage);
            if (element.getClassName().equals(endClass)) {
                break;
            }
        }
        // get error code
        String code = "";
        String userMessage = "";
        if (exception.toString().contains("NullPointerException")) {
            code = "1";
            userMessage = "some value is not available";
        } else if (exception.toString().contains("ValidateJSONObjectException")) {
            code = "2";
			String[] ex = exception.toString().split(": ");
            userMessage = "validate JSON object exception: " + ex[1];
        }
        // output
        jsonobj_error.put("errors", jsonobj_error_data);
        jsonobj_error_data.put("internalMessage", exception.toString());
        jsonobj_error_data.put("userMessage", userMessage);
        jsonobj_error_data.put("code", code);
        jsonobj_error_data.put("developerInfo", jsonarray_element);
        // OUTPUT AS pretty print JSON 
        return jsonobj_error_data.toJSONString();
    }


}
