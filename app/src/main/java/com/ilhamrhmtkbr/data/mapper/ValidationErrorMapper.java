package com.ilhamrhmtkbr.data.mapper;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ilhamrhmtkbr.core.state.FormState;
import com.ilhamrhmtkbr.core.network.callback.FormCallback;

import java.util.HashMap;
import java.util.Map;

public class ValidationErrorMapper<T> {
    public void handle(String errorBody, FormCallback<T> callback) {
        if (!errorBody.isEmpty()) {
            JsonObject jsonObject = JsonParser.parseString(errorBody).getAsJsonObject();

            if (jsonObject.has("errors")) {
                JsonObject errors = jsonObject.getAsJsonObject("errors");
                Map<String, String> validationErrors = new HashMap<>();
                for (String key : errors.keySet()) {
                    if (errors.get(key).isJsonArray()) {
                        validationErrors.put(key, errors.getAsJsonArray(key).get(0).getAsString());
                    } else {
                        validationErrors.put(key, errors.get(key).getAsString());
                    }
                }
                callback.onResult(FormState.validationError(validationErrors));
            } else {
                // General error
                String message = jsonObject.has("message")
                        ? jsonObject.get("message").getAsString()
                        : "Unknown error";
                callback.onResult(FormState.error(message));
            }
        }
    }
}
