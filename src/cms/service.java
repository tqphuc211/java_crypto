package cms;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class service {
    public static synchronized JsonObject route(byte[] msg) {
        String ms = new String(msg);

        JsonObject obj = null;
        String cmd = "";

        try {
            obj = new JsonParser().parse(ms).getAsJsonObject();
            cmd = obj.get("cmd").getAsString();
        } catch (Exception ex) {
            return returnError(400);
        }

        JsonObject rs = null;

        switch (cmd) {
            case "signup":
                rs = signup(ms);
                break;
            case "login":
                rs = login(ms);
                break;
            case "update_key":
                rs = updateKey(ms);
                break;
            case "connect":
                rs = connectUser(ms);
                break;
            case "get_list_user":
                rs = getListUser(ms);
                break;
        }

        return rs;
    }

    public static JsonObject signup(String ms) {
        String str = "{\"status_code\":200,\"data\":{\"id\":123,\"token\":\"anc1n53bhb2n\"}}";
        JsonObject rs = new JsonParser().parse(str).getAsJsonObject();
        return rs;
    }

    public static JsonObject login(String ms) {
        String str = "{\"status_code\":200,\"data\":{\"id\":123,\"token\":\"anc1n53bhb2n\"}}";
        JsonObject rs = new JsonParser().parse(str).getAsJsonObject();
        return rs;
    }

    public static JsonObject updateKey(String ms) {
        String str = "{\"status_code\":200}";
        JsonObject rs = new JsonParser().parse(str).getAsJsonObject();
        return rs;
    }

    public static JsonObject connectUser(String ms) {
        String str = "{\"status_code\":200,\"data\":{\"id\":123,\"token\":\"anc1n53bhb2n\"}}";
        JsonObject rs = new JsonParser().parse(str).getAsJsonObject();
        return rs;
    }

    public static JsonObject getListUser(String ms) {
        String state = "";
        try {
            JsonObject obj = new JsonParser().parse(ms).getAsJsonObject();
            state = obj.get("data").getAsJsonObject().get("state").getAsString();
        } catch (Exception ex) {
            return returnError(400);
        }
        String str = "{\"status_code\":200,\"data\":[{\"id\": 123,\"name\": \"user_123\",\"state\":\"online\"},{\"id\": 234,\"name\": \"user_234\",\"state\":\"online\"},{\"id\": 456,\"name\": \"user_456\",\"state\":\"offline\"},{\"id\": 678,\"name\": \"user_678\",\"state\":\"offline\"},{\"id\": 789,\"name\": \"user_789\",\"state\":\"online\"}]}";
        if (state.equals("online"))
            str = "{\"status_code\":200,\"data\":[{\"id\": 123,\"name\": \"user_123\",\"state\":\"online\"},{\"id\": 234,\"name\": \"user_234\",\"state\":\"online\"},{\"id\": 789,\"name\": \"user_789\",\"state\":\"online\"}]}";
        if (state.equals("offline"))
            str = "{\"status_code\":200,\"data\":[{\"id\": 456,\"name\": \"user_456\",\"state\":\"offline\"},{\"id\": 678,\"name\": \"user_678\",\"state\":\"offline\"}]}";
        JsonObject rs = new JsonParser().parse(str).getAsJsonObject();
        return rs;
    }

    public static JsonObject returnError(int code) {
        String str = "{\"status_code\":400,\"data\":{\"message\":\"Bad request\"}}";
        JsonObject err = new JsonParser().parse(str).getAsJsonObject();
        return err;
    }
}
