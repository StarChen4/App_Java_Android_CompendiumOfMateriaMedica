package model;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import model.Datastructure.DataType;
import model.Datastructure.Plant;
import model.Datastructure.PlantTreeGenerator;
import model.Datastructure.Post;
import model.Datastructure.PostTreeGenerator;
import model.Datastructure.RBTree;
import model.Datastructure.User;
import model.Datastructure.UserTreeGenerator;

/**
 * @author: Haochen Gong
 * @description: 生成树的工厂类，封装了读取json文件，通过对应的树的生成器处理json文件，并生成对应的树的过程。
 **/
public class GeneratorFactory {
    public static RBTree<?> tree(ArrayList<?> data, DataType dataType){
        // 根据读取的数据类型创建对应的树的生成器，生成树
        switch (dataType) {
            case USER:
                UserTreeGenerator userTreeGenerator = new UserTreeGenerator();
                return userTreeGenerator.generateTree((ArrayList<User>)data);
            case PLANT:
                PlantTreeGenerator plantTreeGenerator = new PlantTreeGenerator();
                return plantTreeGenerator.generateTree((ArrayList<Plant>)data);
            case POST:
                PostTreeGenerator postTreeGenerator = new PostTreeGenerator();
                return postTreeGenerator.generateTree((ArrayList<Post>)data);
            default:
                throw new IllegalArgumentException("Invalid type: " + dataType);
        }
    }
}
