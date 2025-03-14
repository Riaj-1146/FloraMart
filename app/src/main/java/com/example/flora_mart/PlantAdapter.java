package com.example.flora_mart;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class PlantAdapter extends RecyclerView.Adapter<PlantAdapter.PlantViewHolder> {

    private final Context context;
    private final List<Plant> plantList;
    private final OnPlantClickListener listener;

    public interface OnPlantClickListener {
        void onUpdateClick(Plant plant);
        void onDeleteClick(Plant plant);
    }

    public PlantAdapter(Context context, List<Plant> plantList, OnPlantClickListener listener) {
        this.context = context;
        this.plantList = plantList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PlantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_plant_view, parent, false);
        return new PlantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlantViewHolder holder, int position) {
        Plant plant = plantList.get(position);
        holder.nameTextView.setText(plant.getName());

        Bitmap bitmap = BitmapFactory.decodeByteArray(plant.getImageBytes(), 0, plant.getImageBytes().length);
        holder.plantImageView.setImageBitmap(bitmap);

        holder.updateButton.setOnClickListener(v -> listener.onUpdateClick(plant));
        holder.deleteButton.setOnClickListener(v -> listener.onDeleteClick(plant));
    }

    @Override
    public int getItemCount() {
        return plantList.size();
    }

    static class PlantViewHolder extends RecyclerView.ViewHolder {
        ImageView plantImageView;
        TextView nameTextView;
        Button updateButton;
        Button deleteButton;

        public PlantViewHolder(@NonNull View itemView) {
            super(itemView);
            plantImageView = itemView.findViewById(R.id.image_view_plant);
            nameTextView = itemView.findViewById(R.id.text_view_plant_name);
            updateButton = itemView.findViewById(R.id.button_update);
            deleteButton = itemView.findViewById(R.id.button_delete);
        }
    }
}
