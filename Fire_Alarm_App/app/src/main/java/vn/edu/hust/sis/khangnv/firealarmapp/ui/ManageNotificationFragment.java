package vn.edu.hust.sis.khangnv.firealarmapp.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import vn.edu.hust.sis.khangnv.firealarmapp.R;
import vn.edu.hust.sis.khangnv.firealarmapp.adapter.ManageNotificationAdapter;
import vn.edu.hust.sis.khangnv.firealarmapp.dto.NotificationDto;
import vn.edu.hust.sis.khangnv.firealarmapp.utils.StatusSeenNotification;
import vn.edu.hust.sis.khangnv.firealarmapp.utils.Utils;
import vn.edu.hust.sis.khangnv.firealarmapp.viewmodel.NotificationViewModel;

public class ManageNotificationFragment extends Fragment implements ManageNotificationAdapter.ItemClickListener{
    RecyclerView recyclerViewNotification;
    TextView tvNoNotificationList;
    private List<NotificationDto> mListNotifications;
    private Context mContext;
    private ManageNotificationAdapter manageNotificationAdapter;
    private NotificationViewModel notificationViewModel;

    public ManageNotificationFragment() {
        mListNotifications = new ArrayList<NotificationDto>();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manage_notification, container, false);

        // init UI
        initUI(view);

        // call API get data
        bindData();

        return view;
    }

    private void initUI(View view) {
        recyclerViewNotification = view.findViewById(R.id.recyclerViewNotification);
        tvNoNotificationList = view.findViewById(R.id.tvNoNotificationList);

        // setup Recycle View
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        recyclerViewNotification.setLayoutManager(linearLayoutManager);

        // setup Adapter
        manageNotificationAdapter = new ManageNotificationAdapter(mContext, this.mListNotifications, this);
        recyclerViewNotification.setAdapter(manageNotificationAdapter);
    }

    // call API and bind data to view
    private void bindData() {
        notificationViewModel = new ViewModelProvider(this).get(NotificationViewModel.class);
        notificationViewModel.getNotificationList();
        notificationViewModel.getNotificationListLiveData().observe(getViewLifecycleOwner(), notificationDtos -> {
            if(notificationDtos.size() == 0){
                recyclerViewNotification.setVisibility(View.GONE);
                tvNoNotificationList.setVisibility(View.VISIBLE);
            } else {
                mListNotifications = notificationDtos;
                manageNotificationAdapter.setNotificationList(mListNotifications);
                tvNoNotificationList.setVisibility(View.GONE);
                recyclerViewNotification.setVisibility(View.VISIBLE);
            }
        });
    }

    // Event click notification item
    @Override
    public void onNotificationClick(NotificationDto notificationDto) {
        // Utils.showToast(mContext, "Event click notification");
        ManageNotificationFragment.AsyncTaskNotification asyncTaskNotification = new ManageNotificationFragment.AsyncTaskNotification(mContext);
        asyncTaskNotification.execute(notificationDto);
    }

    // Call API in background use AsyncTask
    private class AsyncTaskNotification extends AsyncTask<NotificationDto, String, Void> {
        private final ProgressDialog dialog;

        private AsyncTaskNotification(Context context) {
            this.dialog = Utils.buildProgressDialog(context, "NOTIFICATION", "Loading...");
        }

        @Override
        protected void onPostExecute(Void string) {
            // execution of result of Long time consuming operation
            if (dialog.isShowing()) {
                Utils.sleep(500);
                dialog.dismiss();
            }
        }

        @Override
        protected Void doInBackground(NotificationDto... notificationDtos) {
            if(Objects.equals(notificationDtos[0].getStatusSeen(), StatusSeenNotification.NO.getStringValue())){
                NotificationDto notificationDto = new NotificationDto(StatusSeenNotification.YES.getStringValue(), notificationDtos[0].getContent());
                notificationViewModel.updateNotification(notificationDto, notificationDtos[0].getId(), mContext);
                notificationViewModel.getNotificationList();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            dialog.show();
        }
    }
}