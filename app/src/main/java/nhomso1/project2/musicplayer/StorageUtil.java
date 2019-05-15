package nhomso1.project2.musicplayer;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;


/**
 * Ta dùng bộ lưu trữ sau với SharedPreferences để public danh sách audio cho service chạy
 * Cũng như lưu trước các danh sách quan trọng clear, chuyển đổi linh hoạt như một nơi lưu trữ dễ xài
 * Nó cũng đảm bảo tính nhất quán cao cho ứng dụng
 * Tuy là nó cũng sẽ làm chậm nhưng không đáng kể
 * Và lưu ý nó cũng không thể sử dụng trên multiple processes
 */

public class StorageUtil {


    private final String STORAGE = "nhomso1.project2.MusicPlayer.STORAGE";


    private SharedPreferences preferences;

    private Context context;


    public StorageUtil(Context context) {
        this.context = context;
    }

    public void storeAudio(ArrayList<Audio> arrayList) {
        preferences = context.getSharedPreferences(STORAGE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        //Gson là một thư viện Java có thể được sử dụng để chuyển đổi các Đối tượng Java
        // thành biểu diễn JSON của chúng. Nó cũng có thể được sử dụng để chuyển đổi một
        // chuỗi JSON thành một đối tượng Java tương đương.
        //Trong trường hợp này ta sẽ chuyển arrayList sang JSON và lưu vào SharedPreferences
        Gson gson = new Gson();
        String json = gson.toJson(arrayList);

        editor.putString("audioArrayList", json);
        editor.apply();
    }

    public ArrayList<Audio> loadAudio() {
        preferences = context.getSharedPreferences(STORAGE, Context.MODE_PRIVATE);

        Gson gson = new Gson();
        String json = preferences.getString("audioArrayList", null);
        Type type = new TypeToken<ArrayList<Audio>>() {
        }.getType();

        return gson.fromJson(json, type);
    }

    public void storeAudioIndex(int index) {
        preferences = context.getSharedPreferences(STORAGE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("audioIndex", index);
        editor.apply();
    }

    public int loadAudioIndex() {
        preferences = context.getSharedPreferences(STORAGE, Context.MODE_PRIVATE);
        return preferences.getInt("audioIndex", -1);//return -1 if no data found
    }

    public void clearCachedAudioPlaylist() {
        preferences = context.getSharedPreferences(STORAGE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
    }
}
