package model.Datastructure;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

/**
 * @author: Haochen Gong
 * @description: user树的生成器（处理user的json数据，generateTree()方法可以将处理后的数据生成树）
 **/
public class UserTreeGenerator implements TreeGenerator<User> {

    private final RBTree<User> userRBTree;

    public UserTreeGenerator() {
        this.userRBTree = new RBTree<User>();
    }

    @Override
    public RBTree<User> generateTree(ArrayList<User> arrayList) {
        for(User user : arrayList){
            // 设置user id 作key
            userRBTree.insert(user.getUser_id(), user);
        }
        return userRBTree;
    }
}
