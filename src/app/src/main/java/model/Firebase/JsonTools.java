package model.Firebase;

import android.content.Context;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

public class JsonTools {

    // 从res/raw目录加载JSON字符串
    public static String loadJsonFromRaw(Context context, int resourceId) {
        StringBuilder json = new StringBuilder();
        try {
            InputStream is = context.getResources().openRawResource(resourceId);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            String line;
            while ((line = reader.readLine()) != null) {
                json.append(line);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json.toString();
    }

    // 泛型方法，可以加载任何指定类型的列表
    public static <T> T loadListFromJson(Context context, int resourceId, Type typeOfT) {
        String json = loadJsonFromRaw(context, resourceId);
        if (json == null) {
            return null;
        }
        Gson gson = new Gson();
        return gson.fromJson(json, typeOfT);
    }

}
