package hu.uni.miskolc.droidcafe;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import hu.uni.miskolc.droidcafe.service.SelectedCakesDTO;

public class CakesAdapter extends RecyclerView.Adapter<CakesAdapter.MyViewHolder> {

    private Context context;
    private List<SelectedCakesDTO> selectedCakesDTOArrayList;

    public CakesAdapter(MainActivity context, List<SelectedCakesDTO> selectedCakesDTOArrayList) {
        this.context = context;
        this.selectedCakesDTOArrayList = selectedCakesDTOArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.my_cakes, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        SelectedCakesDTO selectedCakesDTO = selectedCakesDTOArrayList.get(position);
        holder.cakeText.setText(selectedCakesDTO.getName());
        holder.cakeImg.setImageResource(selectedCakesDTO.getImage());
        holder.selectedCake.setChecked(selectedCakesDTO.isSelected());

        holder.mainLayout.setOnClickListener(v -> {
            Intent intent = new Intent(context, DessertsDescriptionActivity.class);
                intent.putExtra("cakesNames", selectedCakesDTO.getName());
                intent.putExtra("cakesDesc", selectedCakesDTO.getDescData());
                intent.putExtra("cakesImages", selectedCakesDTO.getImage());
                context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return selectedCakesDTOArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView cakeText;
        CheckBox selectedCake;
        ImageView cakeImg;
        ConstraintLayout mainLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.cakeText = itemView.findViewById(R.id.cakes_text);
            this.selectedCake = itemView.findViewById(R.id.select_cake);
            this.cakeImg = itemView.findViewById(R.id.picture);
            this.mainLayout = itemView.findViewById(R.id.mainLayout);

            selectedCake.setOnClickListener(v -> {
                boolean checked = ((CheckBox) v).isChecked();

                if (checked) {
                    selectedCakesDTOArrayList.get(getAdapterPosition()).setSelected(true);
                } else {
                    selectedCakesDTOArrayList.get(getAdapterPosition()).setSelected(false);
                }
                Toast.makeText(context, String.valueOf(selectedCakesDTOArrayList.get(getAdapterPosition()).getName()), Toast.LENGTH_SHORT).show();
            });

            mainLayout.setOnClickListener(v -> {
                Toast.makeText(context, String.valueOf(selectedCakesDTOArrayList.get(getAdapterPosition()).getName()), Toast.LENGTH_SHORT).show();
            });
        }
    }
}
