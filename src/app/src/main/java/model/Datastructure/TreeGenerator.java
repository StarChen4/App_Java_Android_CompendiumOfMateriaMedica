package model.Datastructure;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * @author: Haochen Gong
 * @description: 数据处理+树的生成器接口
 **/
public interface TreeGenerator<T> {
    RBTree<T> generateTree(ArrayList<T> arrayList);
}


