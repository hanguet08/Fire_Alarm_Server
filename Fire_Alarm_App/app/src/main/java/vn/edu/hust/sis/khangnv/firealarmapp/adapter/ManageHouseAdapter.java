package vn.edu.hust.sis.khangnv.firealarmapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import vn.edu.hust.sis.khangnv.firealarmapp.R;
import vn.edu.hust.sis.khangnv.firealarmapp.dto.HouseDto;

public class ManageHouseAdapter extends RecyclerView.Adapter<ManageHouseAdapter.HouseViewHolder>{
    private Context context;
    private List<HouseDto> houseList;
    private ManageHouseAdapter.ItemClickListener itemClickListener;

    // constructor
    public ManageHouseAdapter(Context context, List<HouseDto> houseList, ManageHouseAdapter.ItemClickListener itemClickListener){
        this.context = context;
        this.houseList = houseList;
        this.itemClickListener = itemClickListener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setHouseList(List<HouseDto> HouseDtoList){
        this.houseList = HouseDtoList;
        notifyDataSetChanged();
    }

    // init view
    @NonNull
    @Override
    public ManageHouseAdapter.HouseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.house_item, parent, false);
        return new ManageHouseAdapter.HouseViewHolder(view,ManageHouseAdapter.this);
    }

    // bind data to view
    @Override
    public void onBindViewHolder(@NonNull ManageHouseAdapter.HouseViewHolder holder, @SuppressLint("RecyclerView") int position) {
        // item clicked
        HouseDto houseDto = this.houseList.get(position);

        // bind data
        holder.textviewHouseName.setText(houseDto.getHouseName());
        holder.textviewHouseAddress.setText(houseDto.getAddress());

        // init listener
        holder.imageHouse.setOnClickListener(view -> this.itemClickListener.onHouseClick(houseDto));
        holder.imageEditHouse.setOnClickListener(view -> this.itemClickListener.onHouseEditClick(houseDto));
        holder.imageDeleteHouse.setOnClickListener(view -> this.itemClickListener.onHouseDeleteClick(houseDto));
    }

    // get count of item in recycle view
    @Override
    public int getItemCount() {
        if(this.houseList != null) {
            return this.houseList.size();
        }
        return 0;
    }

    // class ViewHolder represent item in recycle view
    public class HouseViewHolder extends RecyclerView.ViewHolder{
        ManageHouseAdapter manageHouseAdapter;
        private ImageView imageHouse, imageEditHouse, imageDeleteHouse;
        private TextView textviewHouseName, textviewHouseAddress;

        // constructor
        public HouseViewHolder(@NonNull View itemView, ManageHouseAdapter manageHouseAdapter) {
            super(itemView);
            this.manageHouseAdapter = manageHouseAdapter;
            imageHouse = (ImageView) itemView.findViewById(R.id.imgHouse);
            imageEditHouse = (ImageView) itemView.findViewById(R.id.imgEditHouse);
            imageDeleteHouse = (ImageView) itemView.findViewById(R.id.imgDeleteHouse);
            textviewHouseName = (TextView) itemView.findViewById(R.id.textviewHouseName);
            textviewHouseAddress = (TextView) itemView.findViewById(R.id.textviewHouseAddress);
        }
    }

    public interface ItemClickListener{
        void onHouseClick(HouseDto houseDto);
        void onHouseEditClick(HouseDto houseDto);
        void onHouseDeleteClick(HouseDto houseDto);
    }
}
