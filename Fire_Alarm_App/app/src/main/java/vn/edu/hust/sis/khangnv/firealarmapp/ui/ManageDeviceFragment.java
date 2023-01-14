package vn.edu.hust.sis.khangnv.firealarmapp.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;
import vn.edu.hust.sis.khangnv.firealarmapp.R;
import vn.edu.hust.sis.khangnv.firealarmapp.adapter.ManageDeviceAdapter;
import vn.edu.hust.sis.khangnv.firealarmapp.dto.DeviceDto;
import vn.edu.hust.sis.khangnv.firealarmapp.dto.RoomDto;
import vn.edu.hust.sis.khangnv.firealarmapp.utils.DeviceStatus;
import vn.edu.hust.sis.khangnv.firealarmapp.utils.Utils;
import vn.edu.hust.sis.khangnv.firealarmapp.viewmodel.DeviceViewModel;

public class ManageDeviceFragment extends Fragment implements ManageDeviceAdapter.ItemClickListener, AdapterView.OnItemSelectedListener{
    private TextView tvNoDeviceList, txtNotificationDevice;
    private EditText edtDeviceName, edtDevicePosition;
    private ImageView btnAddNewDevice;
    private RecyclerView recyclerViewDevice;
    private Context mContext;
    private ManageDeviceAdapter manageDeviceAdapter;
    private List<DeviceDto> deviceDtoList;
    private DeviceViewModel deviceViewModel;
    private RoomDto roomDto;
    private DeviceDto deviceForEdit;
    private int deviceType = 1;

    public ManageDeviceFragment() {
        this.deviceDtoList = new ArrayList<DeviceDto>();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manage_device, container, false);

        // init UI
        initUI(view);

        // init listener
        initListener();

        // call API get data
        bindData();

        return view;
    }

    private void initUI(View view) {
        TextView txtNameRoomDetailDevice = view.findViewById(R.id.txtNameRoomDetailDevice);
        TextView txtPositionDetailDevice = view.findViewById(R.id.txtPositionDetailDevice);
        TextView txtOwnerDetailDevice = view.findViewById(R.id.txtOwnerDetailDevice);
        TextView txtAreaDetailDevice = view.findViewById(R.id.txtAreaDetailDevice);
        tvNoDeviceList = view.findViewById(R.id.tvNoDeviceList);
        btnAddNewDevice = view.findViewById(R.id.btnAddNewDevice);
        recyclerViewDevice = view.findViewById(R.id.recyclerViewDevice);

        // setup Recycle View
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        recyclerViewDevice.setLayoutManager(linearLayoutManager);

        // setup Adapter
        manageDeviceAdapter = new ManageDeviceAdapter(mContext, this.deviceDtoList, this);
        recyclerViewDevice.setAdapter(manageDeviceAdapter);

        // get roomDto from ManageRoomFragment
        Bundle bundle = this.getArguments();
        if(bundle != null) {
            this.roomDto = (RoomDto) bundle.getSerializable("room");
            txtNameRoomDetailDevice.setText("Room name: "+roomDto.getRoomName());
            txtPositionDetailDevice.setText("Room position: "+roomDto.getPosition());
            txtOwnerDetailDevice.setText("Room owner: "+roomDto.getOwner());
            txtAreaDetailDevice.setText("Room area: "+roomDto.getArea()+" m2");
        }
    }

    private void initListener() {
        // click FAB
        btnAddNewDevice.setOnClickListener(view -> showDialogAddNewDevice(false));
    }

    // call API and bind data to view
    private void bindData() {
        deviceViewModel = new ViewModelProvider(this).get(DeviceViewModel.class);
        deviceViewModel.getDeviceList(this.roomDto.getId());
        deviceViewModel.getDeviceListLiveData().observe(getViewLifecycleOwner(), deviceDtos -> {
            if(deviceDtos == null){
                recyclerViewDevice.setVisibility(View.GONE);
                tvNoDeviceList.setVisibility(View.VISIBLE);
            } else {
                deviceDtoList = deviceDtos;
                manageDeviceAdapter.setDeviceList(deviceDtoList);
                tvNoDeviceList.setVisibility(View.GONE);
                recyclerViewDevice.setVisibility(View.VISIBLE);
            }
        });
    }

    // spinner in form
    private void initSpinner(View dialogView) {
        Spinner spinner = dialogView.findViewById(R.id.deviceType_spinner);
        spinner.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(mContext,
                R.array.device_type_string, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    // form add or edit
    private void showDialogAddNewDevice(boolean isEdit) {
        AlertDialog dialogBuilder = new AlertDialog.Builder(mContext).create();
        View dialogView = this.getLayoutInflater().inflate(R.layout.form_add_device, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.show();

        // init spinner
        initSpinner(dialogView);

        // init UI
        TextView addDeviceTitleDialog = dialogView.findViewById(R.id.addDeviceTitleDialog);
        edtDeviceName = dialogView.findViewById(R.id.edtDeviceName);
        edtDevicePosition = dialogView.findViewById(R.id.edtDevicePosition);
        Button btnCreateNewDevice = dialogView.findViewById(R.id.btnCreateNewDevice);
        Button btnCancelAddNewDevice = dialogView.findViewById(R.id.btnCancelAddNewDevice);
        txtNotificationDevice = dialogView.findViewById(R.id.txtNotificationDevice);
        RadioGroup radioGroupStatusDevice = dialogView.findViewById(R.id.radioGroupStatusDevice);
        RadioButton radioButtonStatusOn = dialogView.findViewById(R.id.radioButtonStatusOn);
        RadioButton radioButtonStatusOff = dialogView.findViewById(R.id.radioButtonStatusOff);

        if(isEdit) {
            // change ui
            addDeviceTitleDialog.setText("Edit Device");
            btnCreateNewDevice.setText(R.string.btn_edit_string);
            // bind data to dialog
            edtDeviceName.setText(deviceForEdit.getDeviceName());
            edtDevicePosition.setText(deviceForEdit.getPosition());
            if(Objects.equals(deviceForEdit.getStatus(), DeviceStatus.ON.getStringValue())) {
                radioButtonStatusOn.setChecked(true);
            } else {
                radioButtonStatusOff.setChecked(true);
            }
        }

        // init listener click Create Button
        btnCreateNewDevice.setOnClickListener(view -> {
            txtNotificationDevice.setVisibility(View.GONE);
            String name = Utils.getValueFromEditText(edtDeviceName);
            String position = Utils.getValueFromEditText(edtDevicePosition);
            String status = "";
            int statusNumber = radioGroupStatusDevice.getCheckedRadioButtonId();
            if(statusNumber == R.id.radioButtonStatusOn) {
                status = DeviceStatus.ON.getStringValue();
            } else if (statusNumber == R.id.radioButtonStatusOff) {
                status = DeviceStatus.OFF.getStringValue();
            }
            // validate input
            boolean isValid = validate(name, position, status);

            if(isValid) {
                // call API
                DeviceDto deviceDto = new DeviceDto(name, position, status, this.roomDto.getId(), deviceType);
                if(isEdit) {
                    deviceViewModel.updateDevice(deviceDto, deviceForEdit.getId(), mContext);
                } else {
                    deviceViewModel.insertDevice(deviceDto, mContext);
                }
                Utils.sleep(300);
                dialogBuilder.dismiss();
            } else {
                txtNotificationDevice.setText(R.string.warning_enter_all_fields);
                txtNotificationDevice.setVisibility(View.VISIBLE);
            }
        });

        // init listener click Cancel Button
        btnCancelAddNewDevice.setOnClickListener(view -> dialogBuilder.dismiss());
    }

    // validate form
    private boolean validate(String nameDevice, String position, String status) {
        if(nameDevice.isEmpty() || position.isEmpty() || status.isEmpty()) {
            return false;
        }
        return true;
    }

    // dialog alert when delete
    private void showDialogAlertDeleteDevice(DeviceDto deviceDto) {
        // use sweet alert dialog
        new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Delete device!")
                .setContentText("Do you want to delete " + deviceDto.getDeviceName() + "?")
                .setConfirmText("Delete")
                .setConfirmClickListener(sDialog -> {
                    deviceViewModel.deleteDevice(deviceDto.getId(), this.roomDto.getId(), mContext);
                    sDialog.dismissWithAnimation();
                })
                .setCancelButton("Cancel", sDialog -> sDialog.dismissWithAnimation())
                .show();
    }

    // Event click device image
    @Override
    public void onDeviceClick(DeviceDto deviceDto) {
        // Utils.showToast(getContext(), "Click device icon");
    }

    // Event click edit button
    @Override
    public void onDeviceEditClick(DeviceDto deviceDto) {
        // Utils.showToast(getContext(), "Click edit device icon");
        this.deviceForEdit = deviceDto;
        showDialogAddNewDevice(true);
    }

    // Event click delete button
    @Override
    public void onDeviceDeleteClick(DeviceDto deviceDto) {
        // Utils.showToast(getContext(), "Click delete device icon");
        showDialogAlertDeleteDevice(deviceDto);
    }

    // use for spinner
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String[] device_type_values = getResources().getStringArray(R.array.device_type_value);
        deviceType = Integer.parseInt(device_type_values[i]);
    }

    // use for spinner
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }
}