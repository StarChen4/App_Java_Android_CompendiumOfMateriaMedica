package model.Datastructure;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;

/**
 * @author: Haochen Gong
 * @description: post树的生成器（处理post的json数据，generateTree()方法可以将处理后的数据生成树）
 **/
public class PostTreeGenerator implements TreeGenerator<Post>{

    private final RBTree<Post> postRBTree;

    public PostTreeGenerator() {
        this.postRBTree = new RBTree<Post>();
    }

    @Override
    public RBTree<Post> generateTree(ArrayList<Post> arrayList) {
        for(Post post : arrayList){

//            // 解析 likes
//            List<Integer> likes = new ArrayList<>();
//            JSONArray likesArray = jsonObject.getJSONArray("likes");
//            for (int i = 0; i < likesArray.length(); i++) {
//                likes.add(likesArray.getInt(i));
//            }
//
//            // 解析 likesRecord
//            List<Integer> likesRecord = new ArrayList<>();
//            JSONArray likesRecordArray = jsonObject.getJSONArray("likesRecord");
//            for (int i = 0; i < likesRecordArray.length(); i++) {
//                likesRecord.add(likesRecordArray.getInt(i));
//            }
//
//            // 解析 comments
//            Map<Integer, String> comments = new LinkedHashMap<>();
//            JSONObject commentsObject = jsonObject.getJSONObject("comments");
//            Iterator<String> keys = commentsObject.keys();
//            while (keys.hasNext()) {
//                String key = keys.next();
//                String value = commentsObject.getString(key);
//                comments.put(Integer.parseInt(key), value);
//            }

            // 设置post id 作key
            postRBTree.insert(post.getPost_id(), post);

        }
        return postRBTree;
    }
}
