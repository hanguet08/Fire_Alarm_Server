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
import vn.edu.hust.sis.khangnv.firealarmapp.dto.RoomDto;

public class ManageRoomAdapter extends RecyclerView.Adapter<ManageRoomAdapter.RoomViewHolder> {
    ManageRoomAdapter.ItemClickListener itemClickListener;
    List<RoomDto> roomDtoList;
    Context mContext;

    // constructor
    public ManageRoomAdapter(Context mContext, List<RoomDto> roomDtoList, ManageRoomAdapter.ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
        this.mContext = mContext;
        this.roomDtoList = roomDtoList;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setRoomList(List<RoomDto> roomDtoList){
        this.roomDtoList = roomDtoList;
        notifyDataSetChanged();
    }

    // init view
    @NonNull
    @Override
    public RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.room_item, parent, false);
        return new RoomViewHolder(view, ManageRoomAdapter.this);
    }

    // bind data and event listener
    @Override
    public void onBindViewHolder(@NonNull RoomViewHolder holder, int position) {
        // item clicked
        RoomDto roomDtoClicked = roomDtoList.get(position);

        // bind data
        holder.tvRoomName.setText(roomDtoClicked.getRoomName());
        holder.tvRoomOwner.setText(roomDtoClicked.getOwner());
        holder.tvRoomPosition.setText(roomDtoClicked.getPosition());

        // init listener
        holder.imgRoom.setOnClickListener(view -> itemClickListener.onRoomClick(roomDtoClicked));
        holder.imgEditRoom.setOnClickListener(view -> itemClickListener.onRoomEditClick(roomDtoClicked));
        holder.imgDeleteRoom.setOnClickListener(view -> itemClickListener.onRoomDeleteClick(roomDtoClicked));
    }

    // get count of item in recycle view
    @Override
    public int getItemCount() {
        if(roomDtoList != null)
            return roomDtoList.size();
        return 0;
    }

    public class RoomViewHolder extends RecyclerView.ViewHolder {
        ManageRoomAdapter manageRoomAdapter;
        ImageView imgRoom, imgEditRoom, imgDeleteRoom;
        TextView tvRoomName, tvRoomPosition, tvRoomOwner;
        public RoomViewHolder(@NonNull View itemView, ManageRoomAdapter manageRoomAdapter) {
            super(itemView);
            this.manageRoomAdapter = manageRoomAdapter;
            imgRoom = itemView.findViewById(R.id.imgRoom);
            imgEditRoom = itemView.findViewById(R.id.imgEditRoom);
            imgDeleteRoom = itemView.findViewById(R.id.imgDeleteRoom);
            tvRoomName = itemView.findViewById(R.id.tvRoomName);
            tvRoomPosition = itemView.findViewById(R.id.tvRoomPosition);
            tvRoomOwner = itemView.findViewById(R.id.tvRoomOwner);
        }
    }

    public interface ItemClickListener {
        void onRoomClick(RoomDto roomDto);
        void onRoomEditClick(RoomDto roomDto);
        void onRoomDeleteClick(RoomDto roomDto);
    }
}
