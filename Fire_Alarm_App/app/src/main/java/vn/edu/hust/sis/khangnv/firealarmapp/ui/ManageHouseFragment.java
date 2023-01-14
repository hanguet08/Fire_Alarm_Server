package vn.edu.hust.sis.khangnv.firealarmapp.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import vn.edu.hust.sis.khangnv.firealarmapp.adapter.ManageHouseAdapter;
import vn.edu.hust.sis.khangnv.firealarmapp.dto.HouseDto;
import vn.edu.hust.sis.khangnv.firealarmapp.utils.Utils;
import vn.edu.hust.sis.khangnv.firealarmapp.viewmodel.HouseViewModel;

public class ManageHouseFragment extends Fragment implements ManageHouseAdapter.ItemClickListener{
    private ImageView imgViewAddHouse;
    private TextView textviewNoHouseList, txtNotificationHouse;
    private EditText edtHouseName, edtHouseAddress, edtHouseArea, edtHouseFloor, edtHouseMembers;
    private RecyclerView mRecyclerView;
    private ManageHouseAdapter manageHouseAdapter;
    private HouseViewModel houseViewModel;
    private List<HouseDto> houseDtoList;
    private HouseDto houseForEdit;
    private Context mContext;

    public ManageHouseFragment() {
        this.houseDtoList = new ArrayList<>();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manage_house, container, false);

        // init UI
        initUI(view);

        // init listener
        initListener();

        // call API get data
        bindData();

        return view;
    }

    private void initUI(View view) {
        imgViewAddHouse = (ImageView) view.findViewById(R.id.btnAddNewHouseFragment);
        mRecyclerView = view.findViewById(R.id.recyclerView);
        textviewNoHouseList = (TextView) view.findViewById(R.id.textviewNoHouseList);

        // setup Recycle View
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        // setup Adapter
        manageHouseAdapter = new ManageHouseAdapter(mContext, this.houseDtoList, this);
        mRecyclerView.setAdapter(manageHouseAdapter);
    }

    private void initListener() {
        // click FAB
        imgViewAddHouse.setOnClickListener(view -> showDialogAddNewHouse(false));
    }

    // call API and bind data to view
    private void bindData() {
        houseViewModel = new ViewModelProvider(this).get(HouseViewModel.class);
        houseViewModel.getHouseList();
        houseViewModel.getHouseListLiveData().observe(getViewLifecycleOwner(), houseEntities -> {
            if(houseEntities.size() == 0){
                mRecyclerView.setVisibility(View.GONE);
                textviewNoHouseList.setVisibility(View.VISIBLE);
            } else {
                houseDtoList = houseEntities;
                manageHouseAdapter.setHouseList(houseDtoList);
                textviewNoHouseList.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.VISIBLE);
            }
        });
    }

    // form add or edit
    private void showDialogAddNewHouse(boolean isEdit) {
        AlertDialog dialogBuilder = new AlertDialog.Builder(mContext).create();
        View dialogView = this.getLayoutInflater().inflate(R.layout.form_add_house, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.show();

        // init UI
        TextView addHouseTitleDialog = dialogView.findViewById(R.id.addHouseTitleDialog);
        edtHouseName = dialogView.findViewById(R.id.edtHouseName);
        edtHouseAddress = dialogView.findViewById(R.id.edtHouseAddress);
        edtHouseArea = dialogView.findViewById(R.id.edtHouseArea);
        edtHouseFloor = dialogView.findViewById(R.id.edtHouseFloor);
        edtHouseMembers = dialogView.findViewById(R.id.edtHouseMembers);
        Button btnAddNewHouse = dialogView.findViewById(R.id.btnAddNewHouse);
        Button btnCancelAddNewHouse = dialogView.findViewById(R.id.btnCancelAddNewHouse);
        txtNotificationHouse = dialogView.findViewById(R.id.txtNotificationHouse);

        if(isEdit) {
            // change ui
            addHouseTitleDialog.setText("Edit House");
            btnAddNewHouse.setText(R.string.btn_edit_string);
            // bind data to dialog
            edtHouseName.setText(houseForEdit.getHouseName());
            edtHouseAddress.setText(houseForEdit.getAddress());
            edtHouseArea.setText(String.valueOf(houseForEdit.getArea()));
            edtHouseFloor.setText(String.valueOf(houseForEdit.getFloor()));
            edtHouseMembers.setText(String.valueOf(houseForEdit.getMembers()));
        }

        // init listener click Create Button
        btnAddNewHouse.setOnClickListener(view -> {
            txtNotificationHouse.setVisibility(View.GONE);

            String name = Utils.getValueFromEditText(edtHouseName);
            String address = Utils.getValueFromEditText(edtHouseAddress);
            String area = Utils.getValueFromEditText(edtHouseArea);
            String floor = Utils.getValueFromEditText(edtHouseFloor);
            String  members = Utils.getValueFromEditText(edtHouseMembers);

            // validate input
            boolean isValid = validate(name, address, area, floor, members);

            if(isValid) {
                // call API
                HouseDto houseDto = new HouseDto(name, address, Integer.parseInt(floor), Integer.parseInt(members), Integer.parseInt(area));
                if(isEdit) {
                    houseViewModel.updateHouse(houseDto, houseForEdit.getId(), mContext);
                } else houseViewModel.insertHouse(houseDto, mContext);

                Utils.sleep(300);
                dialogBuilder.dismiss();
            } else {
                txtNotificationHouse.setText(R.string.warning_enter_all_fields);
                txtNotificationHouse.setVisibility(View.VISIBLE);
            }
        });

        // init listener click Cancel Button
        btnCancelAddNewHouse.setOnClickListener(view -> dialogBuilder.dismiss());
    }

    // dialog alert when delete
    private void showDialogAlertDeleteHouse(HouseDto houseDto) {
        // use sweet alert dialog
        new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Delete house!")
                .setContentText("Do you want to delete " + houseDto.getHouseName() + "?")
                .setConfirmText("Delete")
                .setConfirmClickListener(sDialog -> {
                    houseViewModel.deleteHouse(houseDto.getId(), mContext);
                    sDialog.dismissWithAnimation();
                })
                .setCancelButton("Cancel", sDialog -> sDialog.dismissWithAnimation())
                .show();
    }

    // validate form
    private boolean validate(String nameHouse, String addressHouse, String area, String floor, String members) {
        if(nameHouse.isEmpty() || addressHouse.isEmpty() || area.isEmpty() || floor.isEmpty() || members.isEmpty()) {
            return false;
        }
        return true;
    }

    // transmit 'fragment house' to 'fragment room'
    // Event click house image
    @Override
    public void onHouseClick(HouseDto houseDto) {
        // Utils.showToast(getContext(), "Click house icon");
        FragmentTransaction transaction =  requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ManageRoomFragment fragment = new ManageRoomFragment();

        Bundle bundle = new Bundle();
        bundle.putSerializable("house", houseDto);
        fragment.setArguments(bundle);
        transaction.replace(R.id.contentFrame, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    // Event click edit button
    @Override
    public void onHouseEditClick(HouseDto houseDto) {
        // Utils.showToast(getContext(), "Click edit house icon");
        this.houseForEdit = houseDto;
        showDialogAddNewHouse(true);
    }

    // Event click delete button
    @Override
    public void onHouseDeleteClick(HouseDto houseDto) {
        // Utils.showToast(getContext(), "Click delete house icon");
        showDialogAlertDeleteHouse(houseDto);
    }
}