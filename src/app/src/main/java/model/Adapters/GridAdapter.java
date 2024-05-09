package model.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.compendiumofmateriamedica.MainActivity;
import com.example.compendiumofmateriamedica.PlantDetailShow;
import com.example.compendiumofmateriamedica.R;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

import model.Datastructure.Plant;
import model.Datastructure.PlantTreeManager;
import model.Datastructure.RBTreeNode;

public class GridAdapter extends RecyclerView.Adapter<GridAdapter.GridViewHolder> {
    private Context context;
    private ArrayList<Integer> data;
//    private PlantTreeManager plantTreeManager;

    public GridAdapter(Context context, ArrayList<Integer> data) throws JSONException, IOException {
        this.context = context;
        this.data = data;
//        plantTreeManager = PlantTreeManager((RBTree<Plant>) GeneratorFactory.tree(this.context, DataType.PLANT, R.raw.plants));

    }

    @NonNull
    @Override
    public GridViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_layout_item, parent, false);
        return new GridViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull GridViewHolder holder, int position) {
        ArrayList<RBTreeNode<Plant>> nodes = PlantTreeManager.instance.search(PlantTreeManager.PlantInfoType.ID, String.valueOf(data.get(position)));
        Log.println(Log.ASSERT, "DEBUG", "[GridAdapter] onBindViewHolder: nodes size " + nodes.size());
        String plantURL = nodes.get(0).getValue().getImage_url();
        MainActivity.loadImageFromURL(this.context, plantURL, holder.plantImage, "Photo");
        String plantName = nodes.get(0).getValue().getCommon_name();
        String plantFamily = nodes.get(0).getValue().getFamily();
        holder.postContent.setText(plantName);
        holder.plantSubheading.setText(plantFamily);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PlantDetailShow.class);
                intent.putExtra("PlantId",  nodes.get(0).getValue().getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class GridViewHolder extends RecyclerView.ViewHolder {
        public ImageView plantImage;
        public TextView postContent;
        public TextView plantSubheading;


        public GridViewHolder(View itemView) {
            super(itemView);
            plantImage = itemView.findViewById(R.id.plantImage);
            postContent = itemView.findViewById(R.id.plantHeading);
            plantSubheading = itemView.findViewById(R.id.plantSubHeading);
        }
    }

    public void setData(ArrayList<Integer> data) {
        this.data.clear();
        this.data.addAll(data);
        notifyDataSetChanged();
    }
}
