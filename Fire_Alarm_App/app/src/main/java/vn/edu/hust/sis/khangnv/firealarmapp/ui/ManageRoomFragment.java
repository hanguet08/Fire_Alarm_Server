package vn.edu.hust.sis.khangnv.firealarmapp.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import vn.edu.hust.sis.khangnv.firealarmapp.R;
import vn.edu.hust.sis.khangnv.firealarmapp.adapter.ManageRoomAdapter;
import vn.edu.hust.sis.khangnv.firealarmapp.dto.HouseDto;
import vn.edu.hust.sis.khangnv.firealarmapp.dto.RoomDto;
import vn.edu.hust.sis.khangnv.firealarmapp.utils.Utils;
import vn.edu.hust.sis.khangnv.firealarmapp.viewmodel.RoomViewModel;

public class ManageRoomFragment extends Fragment implements ManageRoomAdapter.ItemClickListener {
    private TextView tvNoRoomList, txtNotificationRoom;
    private EditText edtRoomName, edtRoomPosition, edtRoomArea, edtRoomOwner;
    private ImageView btnAddNewRoom;
    private RecyclerView recyclerViewRoom;
    private HouseDto houseDto;
    private Context mContext;
    private ManageRoomAdapter manageRoomAdapter;
    private List<RoomDto> roomDtoList;
    private RoomViewModel roomViewModel;
    private RoomDto roomForEdit;

    public ManageRoomFragment() {
        this.roomDtoList = new ArrayList<>();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manage_room, container, false);

        // init UI
        initUI(view);

        // init listener
        initListener();

        // call API get data
        bindData();

        return view;
    }

    private void initUI(View view) {
        TextView txtHouseName = view.findViewById(R.id.txtHouseName);
        TextView txtHouseAddress = view.findViewById(R.id.txtHouseAddress);
        TextView txtHouseFloor = view.findViewById(R.id.txtHouseFloor);
        TextView txtHouseArea = view.findViewById(R.id.txtHouseArea);
        TextView txtHouseMembers = view.findViewById(R.id.txtHouseMembers);
        tvNoRoomList = view.findViewById(R.id.tvNoRoomList);
        recyclerViewRoom = view.findViewById(R.id.recyclerViewRoom);
        btnAddNewRoom = view.findViewById(R.id.btnAddNewRoom);

        // setup Recycle View
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        recyclerViewRoom.setLayoutManager(linearLayoutManager);

        // setup Adapter
        manageRoomAdapter = new ManageRoomAdapter(mContext, this.roomDtoList, this);
        recyclerViewRoom.setAdapter(manageRoomAdapter);

        // get houseDto from ManageHouseFragment
        Bundle bundle = this.getArguments();
        if(bundle != null) {
            this.houseDto = (HouseDto) bundle.getSerializable("house");
            txtHouseName.setText("House name: "+houseDto.getHouseName());
            txtHouseAddress.setText("House address: "+houseDto.getAddress());
            txtHouseArea.setText("Area: "+ String.valueOf(houseDto.getArea())+" m2");
            txtHouseFloor.setText("Floor: "+String.valueOf(houseDto.getFloor()));
            txtHouseMembers.setText("Members: "+String.valueOf(houseDto.getMembers()));
        }
    }

    private void initListener() {
        // click FAB
        btnAddNewRoom.setOnClickListener(view -> this.showDialogAddNewRoom(false));
    }

    // call API and bind data to view
    private void bindData() {
        roomViewModel = new ViewModelProvider(this).get(RoomViewModel.class);
        roomViewModel.getRoomList(this.houseDto.getId());
        roomViewModel.getRoomListLiveData().observe(getViewLifecycleOwner(), roomDtoLists -> {
            if(roomDtoLists == null){
                recyclerViewRoom.setVisibility(View.GONE);
                tvNoRoomList.setVisibility(View.VISIBLE);
            } else {
                this.roomDtoList = roomDtoLists;
                manageRoomAdapter.setRoomList(roomDtoList);
                tvNoRoomList.setVisibility(View.GONE);
                recyclerViewRoom.setVisibility(View.VISIBLE);
            }
        });
    }

    // form add or edit
    private void showDialogAddNewRoom(boolean isEdit) {
        AlertDialog dialogBuilder = new AlertDialog.Builder(mContext).create();
        View dialogView = this.getLayoutInflater().inflate(R.layout.form_add_room, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.show();

        // init UI
        TextView addRoomTitleDialog = dialogView.findViewById(R.id.addRoomTitleDialog);
        edtRoomName = dialogView.findViewById(R.id.edtRoomName);
        edtRoomPosition = dialogView.findViewById(R.id.edtRoomPosition);
        edtRoomOwner = dialogView.findViewById(R.id.edtRoomOwner);
        edtRoomArea = dialogView.findViewById(R.id.edtRoomArea);
        Button btnCreateNewRoom = dialogView.findViewById(R.id.btnCreateNewRoom);
        Button btnCancelAddNewRoom = dialogView.findViewById(R.id.btnCancelAddNewRoom);
        txtNotificationRoom = dialogView.findViewById(R.id.txtNotificationRoom);

        if(isEdit) {
            // change ui
            addRoomTitleDialog.setText("Edit Room");
            btnCreateNewRoom.setText(R.string.btn_edit_string);
            // bind data to dialog
            edtRoomName.setText(roomForEdit.getRoomName());
            edtRoomPosition.setText(roomForEdit.getPosition());
            edtRoomOwner.setText(roomForEdit.getOwner());
            edtRoomArea.setText(String.valueOf(roomForEdit.getArea()));
        }

        // init listener click Create Button
        btnCreateNewRoom.setOnClickListener(view -> {
            txtNotificationRoom.setVisibility(View.GONE);
            String name = Utils.getValueFromEditText(edtRoomName);
            String position = Utils.getValueFromEditText(edtRoomPosition);
            String area = Utils.getValueFromEditText(edtRoomArea);
            String owner = Utils.getValueFromEditText(edtRoomOwner);

            // validate input
            boolean isValid = validate(name, position, area, owner);

            if(isValid) {
                // call API
                RoomDto roomDto = new RoomDto(name, position, Integer.parseInt(area), owner, this.houseDto.getId());
                if(isEdit) {
                    roomViewModel.updateRoom(roomDto, roomForEdit.getId(), mContext);
                } else roomViewModel.insertRoom(roomDto, mContext);

                Utils.sleep(300);
                dialogBuilder.dismiss();
            } else {
                txtNotificationRoom.setText(R.string.warning_enter_all_fields);
                txtNotificationRoom.setVisibility(View.VISIBLE);
            }
        });

        // init listener click Cancel Button
        btnCancelAddNewRoom.setOnClickListener(view -> dialogBuilder.dismiss());
    }

    // validate form
    private boolean validate(String nameRoom, String position, String area, String owner) {
        if(nameRoom.isEmpty() || position.isEmpty() || area.isEmpty() || owner.isEmpty()) {
            return false;
        }
        return true;
    }

    // dialog alert when delete
    private void showDialogAlertDeleteRoom(RoomDto roomDto) {
        // use sweet alert dialog
        new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Delete room!")
                .setContentText("Do you want to delete " + roomDto.getRoomName() + "?")
                .setConfirmText("Delete")
                .setConfirmClickListener(sDialog -> {
                    roomViewModel.deleteRoom(roomDto.getId(), this.houseDto.getId(), mContext);
                    sDialog.dismissWithAnimation();
                })
                .setCancelButton("Cancel", sDialog -> sDialog.dismissWithAnimation())
                .show();
    }

    // transmit 'fragment room' to 'fragment device'
    // Event click room image
    @Override
    public void onRoomClick(RoomDto roomDto) {
        // Utils.showToast(getContext(), "Click room icon");
        FragmentTransaction transaction =  requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ManageDeviceFragment fragment = new ManageDeviceFragment();

        Bundle bundle = new Bundle();
        bundle.putSerializable("room", roomDto);
        fragment.setArguments(bundle);
        transaction.replace(R.id.contentFrame, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    // Event click edit button
    @Override
    public void onRoomEditClick(RoomDto roomDto) {
        // Utils.showToast(getContext(), "Click edit room icon");
        this.roomForEdit = roomDto;
        showDialogAddNewRoom(true);
    }

    // Event click delete button
    @Override
    public void onRoomDeleteClick(RoomDto roomDto) {
        // Utils.showToast(getContext(), "Click delete room icon");
        showDialogAlertDeleteRoom(roomDto);
    }
}