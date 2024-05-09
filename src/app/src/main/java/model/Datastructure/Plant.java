package model.Datastructure;

import androidx.annotation.NonNull;

/**
 * @author: Haochen Gong
 * @description: Plant类
 **/
public class Plant implements Comparable<Plant>{
    private int id;
    private String common_name;
    private String slug;
    private String scientific_name;
    private String image_url;
    private String genus;
    private String family;
    private String description;

    // 无参数构造函数
    public Plant() {}

    public Plant(int id, String commonName, String slug, String scientific_name, String image_url, String genus, String family, String description){
        this.id = id;
        this.common_name = commonName;
        this.slug = slug;
        this.scientific_name = scientific_name;
        this.image_url = image_url;
        this.genus = genus;
        this.family = family;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getCommon_name() {
        return common_name;
    }

    public String getSlug() {
        return slug;
    }

    public String getScientific_name() {
        return scientific_name;
    }

    public String getImage_url() {
        return image_url;
    }

    public String getGenus() {
        return genus;
    }

    public String getFamily() {
        return family;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public int compareTo(Plant plant) {
        return this.id - plant.id;
    }

    @NonNull
    @Override
    public String toString() {
        return "{PlantID: " + id + ", "
                + "CommonName: " + common_name + ", "
                + "Slug: " + slug + ", "
                + "ScientificName: " + scientific_name + ", "
                + "ImageUrl: " + image_url + ", "
                + "Genus: " + genus + ", "
                + "Family: " + family + ", "
                + "Description: " + description + "}";
    }
}