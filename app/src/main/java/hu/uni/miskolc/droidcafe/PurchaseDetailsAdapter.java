package hu.uni.miskolc.droidcafe;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import hu.uni.miskolc.droidcafe.service.PurchaseDataDTO;

public class PurchaseDetailsAdapter extends RecyclerView.Adapter<PurchaseDetailsAdapter.ViewHolder> {
    List<PurchaseDataDTO> dataDTOS;

    public PurchaseDetailsAdapter(List<PurchaseDataDTO> dataDTOS) {
        this.dataDTOS = dataDTOS;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            PurchaseDataDTO purchaseDataDTOS = dataDTOS.get(position);

            holder.tvName.setText(purchaseDataDTOS.getName());
            holder.tvAddress.setText(purchaseDataDTOS.getAddress());
            holder.tvPhone.setText(purchaseDataDTOS.getPhoneNumber());
            holder.tvNote.setText(purchaseDataDTOS.getNote());
    }

    @Override
    public int getItemCount() {
        return dataDTOS.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvAddress, tvPhone, tvNote;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            tvAddress = itemView.findViewById(R.id.tv_address);
            tvPhone = itemView.findViewById(R.id.tv_phone);
            tvNote = itemView.findViewById(R.id.tv_note);
        }
    }
}
