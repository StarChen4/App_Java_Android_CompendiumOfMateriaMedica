package model.Datastructure;

import java.util.ArrayList;

/**
 * @author: Haochen Gong
 * @description: plant树的管理方法类
 **/
public class PlantTreeManager implements TreeManager<Plant>{

    private final RBTree<Plant> plantRBTree;

    public enum PlantInfoType {
        ID, COMMON_NAME, SLUG, SCIENTIFIC_NAME, GENUS, FAMILY;
    }

    public static PlantTreeManager instance;
    private PlantTreeManager(RBTree<Plant> plantRBTree) {
        this.plantRBTree = plantRBTree;
    }
    public static synchronized PlantTreeManager getInstance(RBTree<Plant> plantRBTree) {
        if (instance == null) {
            instance = new PlantTreeManager(plantRBTree);
        }
        return instance;
    }
    @Override
    public void insert(int id, Plant plant) {
        plantRBTree.insert(id,plant);
    }

    @Override
    public void delete(int id) {
        plantRBTree.delete(id);
    }

    // 对外的搜索接口，调用这个方法来开始搜索
    public <T> ArrayList<RBTreeNode<Plant>> search (PlantInfoType infoType, T info) {

        ArrayList<RBTreeNode<Plant>> plants = new ArrayList<>();

        if (infoType == PlantInfoType.ID) {
            RBTreeNode<Plant> plant = plantRBTree.search(Integer.parseInt((String) info));
            if (plant != null) {
                plants.add(plant);
            }
        } else {
            search(plantRBTree.root, infoType, info, plants);
        }

        return plants;
    }

    // 实际的递归搜索方法
    private  <T> void search(RBTreeNode<Plant> node, PlantInfoType infoType, T info, ArrayList<RBTreeNode<Plant>> plants) {
        // 如果当前节点是null，说明已经到达了叶子节点的子节点，直接返回
        if (node == null) {
            return;
        }

        // 如果当前节点的值与搜索的值相等，加入结果列表
        switch (infoType) {
            case COMMON_NAME:
                if (node.getValue().getCommon_name().contains((CharSequence) info)) {
                    plants.add(node);
                }
            case SLUG:
                if (node.getValue().getSlug().contains((CharSequence) info)) {
                    plants.add(node);
                }
            case SCIENTIFIC_NAME:
                if (node.getValue().getScientific_name().contains((CharSequence) info)) {
                    plants.add(node);
                }
            case GENUS:
                if (node.getValue().getGenus().contains((CharSequence) info)) {
                    plants.add(node);
                }
            case FAMILY:
                if (node.getValue().getFamily().contains((CharSequence) info)) {
                    plants.add(node);
                }
        }

        // 继续在左子树中递归搜索
        search(node.getLeft(), infoType, info, plants);
        // 继续在右子树中递归搜索
        search(node.getRight(), infoType, info, plants);
    }


}
