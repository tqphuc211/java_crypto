package cms;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import sercure.RSA;
import sercure.Signing;

import java.security.interfaces.RSAPrivateKey;
import java.util.Base64;
import java.util.List;

public class service {
    public static synchronized JsonObject route(byte[] msg, String ip) {
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
                rs = signup(ms, ip);
                break;
            case "login":
                rs = login(ms, ip);
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

    public static JsonObject signup(String ms, String ip) {
        JsonObject rs = new JsonObject();

        JsonObject data = null;
        JsonObject obj = null;
        try {
            obj = new JsonParser().parse(ms).getAsJsonObject();
            data = obj.get("data").getAsJsonObject();
        } catch (Exception ex) {
            return returnError(400);
        }
        if (obj == null || data == null)
            return returnError(400);

        dto acc = dao.findByName(data.get("name").getAsString());

        if (acc != null) {
            return returnLogicError(411, "Tên đăng nhập đã tồn tại");
        }

        acc = new dto();
        acc.setName(data.get("name").getAsString());
        //TODO hash password
        acc.setPass(data.get("pass").getAsString());
        acc.setState("offline");

        dto rsInsert = dao.signup(acc);
        if (rsInsert == null) {
            return returnLogicError(412, "Lỗi tạo tài khoản");
        }

        setLoginInfo(rsInsert, ip);
        rs.addProperty("status_code", 200);
        rs.add("data", new JsonParser().parse(new Gson().toJson(rsInsert)));

        return rs;
    }

    public static JsonObject login(String ms, String ip) {
        JsonObject rs = new JsonObject();

        JsonObject data = null;
        JsonObject obj = null;
        try {
            obj = new JsonParser().parse(ms).getAsJsonObject();
            data = obj.get("data").getAsJsonObject();
        } catch (Exception ex) {
            return returnError(400);
        }
        if (obj == null || data == null)
            return returnError(400);

        dto acc = dao.login(data.get("name").getAsString(), data.get("pass").getAsString());
        if (acc == null)
            return returnLogicError(404, "Tài khoản không đúng");
        setLoginInfo(acc, ip);
        rs.addProperty("status_code", 200);
        rs.add("data", new JsonParser().parse(new Gson().toJson(acc)));

        return rs;
    }

    public static void setLoginInfo(dto acc, String ip) {
        acc.setState("online");
        dao.updateState(acc);
        //TODO gen token
        acc.setToken("default_token");
        dao.updateToken(acc);
        acc.setPass("");
        acc.setIp(ip);
        dao.updateIP(acc);
    }

    public static JsonObject updateKey(String ms) {
        JsonObject rs = new JsonObject();

        JsonObject data = null;
        JsonObject obj = null;
        try {
            obj = new JsonParser().parse(ms).getAsJsonObject();
            data = obj.get("data").getAsJsonObject();
        } catch (Exception ex) {
            return returnError(400);
        }
        if (obj == null || data == null)
            return returnError(400);

        dto acc = new dto();
        acc.setId(data.get("id").getAsInt());
        acc.setToken(data.get("token").getAsString());
//        acc.setPublic_key(data.get("key").getAsString());
        acc.setPublic_key(Base64.getEncoder().encodeToString(RSA.getPublicKey()));


        try {
            JsonObject cerObj = new JsonObject();
            cerObj.addProperty("id", acc.getId());
            cerObj.addProperty("public_key", acc.getPublic_key());
            byte[] sign = Signing.rsaSign((RSAPrivateKey) RSA.getPrivateKey(), cerObj.toString().getBytes());
            cerObj.addProperty("sign", Base64.getEncoder().encodeToString(sign));
            acc.setCer(cerObj.toString());
        } catch (Exception ex) {
            returnLogicError(415, "Lỗi tạo certificate");
        }

        acc = dao.updateToken(acc);
        if (acc == null)
            return returnLogicError(416, "Lỗi không thể cập nhật key");


        rs.addProperty("status_code", 200);
        rs.add("data", new JsonParser().parse(new Gson().toJson(acc)));

        return rs;
    }

    public static JsonObject connectUser(String ms) {
        String str = "{\"status_code\":200,\"data\":{\"id\":123,\"token\":\"anc1n53bhb2n\"}}";
        JsonObject rs = new JsonParser().parse(str).getAsJsonObject();
        return rs;
    }

    public static JsonObject getListUser(String ms) {
//        String state = "";
//        try {
//            JsonObject obj = new JsonParser().parse(ms).getAsJsonObject();
//            state = obj.get("data").getAsJsonObject().get("state").getAsString();
//        } catch (Exception ex) {
//            return returnError(400);
//        }
//        String str = "{\"status_code\":200,\"data\":[{\"id\": 123,\"name\": \"user_123\",\"state\":\"online\"},{\"id\": 234,\"name\": \"user_234\",\"state\":\"online\"},{\"id\": 456,\"name\": \"user_456\",\"state\":\"offline\"},{\"id\": 678,\"name\": \"user_678\",\"state\":\"offline\"},{\"id\": 789,\"name\": \"user_789\",\"state\":\"online\"}]}";
//        if (state.equals("online"))
//            str = "{\"status_code\":200,\"data\":[{\"id\": 123,\"name\": \"user_123\",\"state\":\"online\"},{\"id\": 234,\"name\": \"user_234\",\"state\":\"online\"},{\"id\": 789,\"name\": \"user_789\",\"state\":\"online\"}]}";
//        if (state.equals("offline"))
//            str = "{\"status_code\":200,\"data\":[{\"id\": 456,\"name\": \"user_456\",\"state\":\"offline\"},{\"id\": 678,\"name\": \"user_678\",\"state\":\"offline\"}]}";
//        JsonObject rs = new JsonParser().parse(str).getAsJsonObject();
//        return rs;

        JsonObject rs = new JsonObject();

        JsonObject data = null;
        JsonObject obj = null;
        try {
            obj = new JsonParser().parse(ms).getAsJsonObject();
            data = obj.get("data").getAsJsonObject();
        } catch (Exception ex) {
            return returnError(400);
        }
        if (obj == null || data == null)
            return returnError(400);

        String state = data.get("state").getAsString();
        List<dto> acc = null;
        if (state.equals("online") || state.equals("offline"))
            acc = dao.getListAccount(state);
        else
            acc = dao.getListAccount("");

        if (acc == null) {
            return returnLogicError(412, "Lỗi lấy dữ liệu");
        }
        rs.addProperty("status_code", 200);
        rs.add("data", new JsonParser().parse(new Gson().toJson(acc)));
        return rs;
    }

    public static JsonObject returnError(int code) {
        String str = "{\"status_code\":400,\"data\":{\"message\":\"Bad request\"}}";
        JsonObject err = new JsonParser().parse(str).getAsJsonObject();
        return err;
    }

    public static JsonObject returnLogicError(int code, String mess) {
        String str = "{\"status_code\":" + code + ",\"data\":{\"message\":\"" + mess + "\"}}";
        JsonObject err = new JsonParser().parse(str).getAsJsonObject();
        return err;
    }
}
