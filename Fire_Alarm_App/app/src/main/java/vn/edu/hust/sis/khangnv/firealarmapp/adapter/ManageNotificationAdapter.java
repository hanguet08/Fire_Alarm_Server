package vn.edu.hust.sis.khangnv.firealarmapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Objects;

import vn.edu.hust.sis.khangnv.firealarmapp.R;
import vn.edu.hust.sis.khangnv.firealarmapp.dto.NotificationDto;
import vn.edu.hust.sis.khangnv.firealarmapp.utils.StatusSeenNotification;

public class ManageNotificationAdapter extends RecyclerView.Adapter<ManageNotificationAdapter.NotificationViewHolder> {
    private Context context;
    private List<NotificationDto> notificationDtoList;
    private ManageNotificationAdapter.ItemClickListener itemClickListener;

    // constructor
    public ManageNotificationAdapter(Context context, List<NotificationDto> notificationDtoList, ItemClickListener itemClickListener) {
        this.context = context;
        this.notificationDtoList = notificationDtoList;
        this.itemClickListener = itemClickListener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setNotificationList(List<NotificationDto> notificationDtoLists){
        this.notificationDtoList = notificationDtoLists;
        notifyDataSetChanged();
    }

    // init view
    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_item, parent, false);
        return new ManageNotificationAdapter.NotificationViewHolder(view,ManageNotificationAdapter.this);
    }

    // bind data to view
    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        // item clicked
        NotificationDto notificationDto = this.notificationDtoList.get(position);

        // bind data
        holder.tvContentNotification.setText(notificationDto.getContent());
        holder.tvDatetime.setText(notificationDto.getDateTime());
        if(Objects.equals(notificationDto.getStatusSeen(), StatusSeenNotification.NO.getStringValue())) {
            holder.coverNotification.setBackgroundColor(R.color.color_background_notification);
            holder.tvContentNotification.setTypeface(null, Typeface.BOLD);
            holder.tvDatetime.setTypeface(null, Typeface.BOLD);
        }

        // init listener
        holder.coverNotification.setOnClickListener(view -> this.itemClickListener.onNotificationClick(notificationDto));
    }

    // get count of item in recycle view
    @Override
    public int getItemCount() {
        if(this.notificationDtoList != null) {
            return this.notificationDtoList.size();
        }
        return 0;
    }

    public class NotificationViewHolder extends RecyclerView.ViewHolder {
        ManageNotificationAdapter manageNotificationAdapter;
        TextView tvContentNotification, tvDatetime, tvDeviceNotification;
        LinearLayout coverNotification;
        public NotificationViewHolder(@NonNull View itemView, ManageNotificationAdapter manageNotificationAdapter) {
            super(itemView);
            this.manageNotificationAdapter = manageNotificationAdapter;
            tvContentNotification = itemView.findViewById(R.id.tvContentNotification);
            tvDatetime = itemView.findViewById(R.id.tvDatetime);
            coverNotification = itemView.findViewById(R.id.coverNotification);
            // tvDeviceNotification = itemView.findViewById(R.id.tvDeviceNotification);
        }
    }

    public interface ItemClickListener{
        void onNotificationClick(NotificationDto notificationDto);
    }
}
