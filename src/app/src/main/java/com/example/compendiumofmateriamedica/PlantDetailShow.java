package com.example.compendiumofmateriamedica;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;

import model.Datastructure.Plant;
import model.Datastructure.PlantTreeManager;
import model.Datastructure.RBTreeNode;


/**
 * @author: Haochen Gong
 * @description: 植物数据显示页面。
 **/
public class PlantDetailShow extends AppCompatActivity {

    private TextView commonName;
    private TextView slug;
    private TextView scientificName;
    private TextView genus;
    private TextView family;
    private TextView description;

    // Xing Chen: 这里假设要putExtra一个植物的id
    private int plantId;




//    private RBTree<User> userTree;
//    private RBTree<Plant> plantTree;
//    private RBTree<Post> postTree;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_detail_show);

        // 因为用了单例模式，LoginActivity已经实例化了，这些不需要了
//        // 运行加载数据的函数
//        try {
//            DataInitial();
//        } catch (JSONException e) {
//            throw new RuntimeException(e);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }

        commonName = findViewById(R.id.common_name);
        slug = findViewById(R.id.slug);
        scientificName = findViewById(R.id.scientific_name);
        genus = findViewById(R.id.genus);
        family = findViewById(R.id.family);
        description = findViewById(R.id.description);

        // Xing Chen: 这里假设要putExtra一个植物的id，key为"PlantId"
        plantId = (int) this.getIntent().getSerializableExtra("PlantId");
        ArrayList<RBTreeNode<Plant>> plants = PlantTreeManager.instance.search(PlantTreeManager.PlantInfoType.ID, String.valueOf(plantId));
        RBTreeNode<Plant> node = plants.get(0);
        Plant plant = node.getValue();

        commonName.setText(plant.getCommon_name());
        slug.setText(plant.getSlug());
        scientificName.setText(plant.getScientific_name());
        genus.setText(plant.getGenus());
        family.setText(plant.getFamily());
        description.setText(plant.getDescription());
    }


    // 因为用了单例模式，LoginActivity已经实例化了，这些不需要了
//    /**
//     * @author: Haochen Gong
//     * @description: 加载数据
//     **/
//    private void DataInitial() throws JSONException, IOException {
//        userTree = (RBTree<User>) GeneratorFactory.tree(this, DataType.USER, R.raw.users);
//        plantTree = (RBTree<Plant>) GeneratorFactory.tree(this, DataType.PLANT, R.raw.plants);
//        postTree = (RBTree<Post>) GeneratorFactory.tree(this, DataType.POST, R.raw.posts);
//    }


}