package com.andra.samplephotosbrowser.parser;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * This interface describes a class whose sole responsibility is receiving a JSON object
 * and parsing it into the requested data model
 *
 * @param <T> the type of the object that results from parsing of the JSON object
 */
public interface JSONParser<T> {

    T parseJSONResponse(JSONObject response);
}
