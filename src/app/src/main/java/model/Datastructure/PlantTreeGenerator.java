package model.Datastructure;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

/**
 * @author: Haochen Gong
 * @description: plant树的生成器（处理plant的json数据，generateTree()方法可以将处理后的数据生成树）
 **/
public class PlantTreeGenerator implements TreeGenerator<Plant>{
    private final RBTree<Plant> plantRBTree;

    public PlantTreeGenerator() {
        this.plantRBTree = new RBTree<Plant>();
    }

    @Override
    public RBTree<Plant> generateTree(ArrayList<Plant> arrayList) {
        for(Plant plant : arrayList){
            // 设置plant id 作key
            plantRBTree.insert(plant.getId(), plant);
        }
        return plantRBTree;
    }
}
