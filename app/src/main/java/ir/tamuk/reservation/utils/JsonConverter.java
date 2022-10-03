package ir.tamuk.reservation.utils;

import org.json.JSONException;
import org.json.JSONObject;

import ir.tamuk.reservation.models.Photo;

public class JsonConverter {

    public static Photo convertPhotoJsonToObject(JSONObject photoJson) {

        Photo photo = new Photo();

        try {

            if (photoJson.has("_id"))
                photo.id = photoJson.getString("_id");
            if (photoJson.has("filename"))
                photo.filename = photoJson.getString("filename");
            if (photoJson.has("fileName"))
                photo.filename = photoJson.getString("fileName");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return photo;
    }

}
