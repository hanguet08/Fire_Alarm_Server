package vn.edu.hust.sis.khangnv.firealarmapp.adapter;

import static vn.edu.hust.sis.khangnv.firealarmapp.utils.DeviceType.*;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import vn.edu.hust.sis.khangnv.firealarmapp.R;
import vn.edu.hust.sis.khangnv.firealarmapp.dto.DeviceDto;
import vn.edu.hust.sis.khangnv.firealarmapp.utils.DeviceStatus;

public class ManageDeviceAdapter extends RecyclerView.Adapter<ManageDeviceAdapter.DeviceViewHolder> {
    private Context context;
    private List<DeviceDto> deviceDtoList;
    private ManageDeviceAdapter.ItemClickListener itemClickListener;

    // constructor
    public ManageDeviceAdapter(@NonNull Context context, List<DeviceDto> deviceList, ManageDeviceAdapter.ItemClickListener itemClickListener) {
        this.context = context;
        this.deviceDtoList = deviceList;
        this.itemClickListener = itemClickListener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setDeviceList(List<DeviceDto> deviceDtoLists){
        this.deviceDtoList = deviceDtoLists;
        notifyDataSetChanged();
    }

    // init view
    @NonNull
    @Override
    public DeviceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.device_item, parent, false);
        return new ManageDeviceAdapter.DeviceViewHolder(view, ManageDeviceAdapter.this);
    }

    // bind data to view
    @Override
    public void onBindViewHolder(@NonNull DeviceViewHolder holder, int position) {
        // item clicked
        DeviceDto deviceDto = deviceDtoList.get(position);

        // bind data
        holder.tvDeviceName.setText("Device name: "+deviceDto.getDeviceName());
        holder.tvDevicePosition.setText("Position: "+deviceDto.getPosition());
        // holder.tvValue.setText();
        if(DeviceStatus.isValidStatus(deviceDto.getStatus())) {
            holder.tvDeviceStatus.setText("Status: "+deviceDto.getStatus());
        } else holder.tvDeviceStatus.setText("Invalid status!");
        if(deviceDto.getDeviceType() == FLAME_SENSOR.getIntValue()){
            holder.imgDevice.setBackgroundResource(R.drawable.flame_sensor);
        } else if(deviceDto.getDeviceType() == MQ2_SENSOR.getIntValue()) {
            holder.imgDevice.setBackgroundResource(R.drawable.mq2_sensor);
        } else if (deviceDto.getDeviceType() == DHT11_SENSOR.getIntValue()) {
            holder.imgDevice.setBackgroundResource(R.drawable.temperature_sensor);
        }

        // init listener
        holder.imgEditDevice.setOnClickListener(view -> this.itemClickListener.onDeviceEditClick(deviceDto));
        holder.imgDeleteDevice.setOnClickListener(view -> this.itemClickListener.onDeviceDeleteClick(deviceDto));
    }

    // get count of item in recycle view
    @Override
    public int getItemCount() {
        if(this.deviceDtoList != null) {
            return this.deviceDtoList.size();
        }
        return 0;
    }

    // class ViewHolder represent item in recycle view
    public class DeviceViewHolder extends RecyclerView.ViewHolder{
        ManageDeviceAdapter manageDeviceAdapter;
        ImageView imgDevice, imgEditDevice, imgDeleteDevice;
        TextView tvValue, tvDeviceName, tvDevicePosition, tvDeviceStatus;
        Spinner spinner;
        public DeviceViewHolder(@NonNull View itemView, ManageDeviceAdapter manageDeviceAdapter) {
            super(itemView);
            this.manageDeviceAdapter = manageDeviceAdapter;
            imgDevice = itemView.findViewById(R.id.imgDevice);
            imgEditDevice = itemView.findViewById(R.id.imgEditDevice);
            imgDeleteDevice = itemView.findViewById(R.id.imgDeleteDevice);
            tvValue = itemView.findViewById(R.id.tvValue);
            tvDeviceName = itemView.findViewById(R.id.tvDeviceName);
            tvDevicePosition = itemView.findViewById(R.id.tvDevicePosition);
            tvDeviceStatus = itemView.findViewById(R.id.tvDeviceStatus);
            spinner = itemView.findViewById(R.id.deviceType_spinner);
        }
    }

    public interface ItemClickListener{
        void onDeviceClick(DeviceDto deviceDto);
        void onDeviceEditClick(DeviceDto deviceDto);
        void onDeviceDeleteClick(DeviceDto deviceDto);
    }
}
