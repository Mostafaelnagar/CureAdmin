package app.grand.ophthalmicadmin.admin.doctors;


import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;

import com.google.gson.Gson;

import app.grand.ophthalmicadmin.PassingObject;
import app.grand.ophthalmicadmin.R;
import app.grand.ophthalmicadmin.admin.adminReservations.viewModels.AdminReservationsViewModels;
import app.grand.ophthalmicadmin.base.BaseFragment;
import app.grand.ophthalmicadmin.base.Params;
import app.grand.ophthalmicadmin.base.constantsutils.Codes;
import app.grand.ophthalmicadmin.databinding.FragmentAdminDoctorReservationsBinding;
import app.grand.ophthalmicadmin.databinding.FragmentAdminWaitingReservationsBinding;


public class AdminDoctorReservationsFragment extends BaseFragment {
    FragmentAdminDoctorReservationsBinding doctorReservationsBinding;
    AdminReservationsViewModels adminReservationsViewModels;
    private String passingObject;
    private Bundle bundle;


    public AdminDoctorReservationsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        doctorReservationsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_admin_doctor_reservations, container, false);
        adminReservationsViewModels = new AdminReservationsViewModels();
        doctorReservationsBinding.setDoctorReservationsViewModel(adminReservationsViewModels);
        bundle = this.getArguments();

        liveDataListeners();
        checkConnection();
        return doctorReservationsBinding.getRoot();
    }


    private void liveDataListeners() {
        adminReservationsViewModels.getClicksMutableLiveData().observe(this, result -> {
            if (result == View.VISIBLE || result == View.GONE) {
                accessLoadingBar(result);
            } else if (result == Codes.SHOW_MESSAGE_SUCCESS) {
                showMessage(adminReservationsViewModels.getReturnedMessage(), 0, 0);
                adminReservationsViewModels.goBack(getActivity());
            }
        });
        adminReservationsViewModels.getReservationsAdapter().getAdminActions().observe(getActivity(), new Observer<PassingObject>() {
            @Override
            public void onChanged(PassingObject passingObject) {
                if (passingObject.getCode() == Codes.ADMIN_ACCEPT) {
                    adminReservationsViewModels.acceptReserve(passingObject.getObject());
                } else {
                    rejectDialog(passingObject.getObject());
                }
            }
        });
        ConnectionLiveData.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isConnected) {
                if (isConnected) {
                    if (bundle != null) {
                        passingObject = bundle.getString(Params.BUNDLE);
                        adminReservationsViewModels.setPassingObject(new Gson().fromJson(passingObject, PassingObject.class));
                        adminReservationsViewModels.getReservations("");
                    }
                } else {
                    showMessage(getResources().getString(R.string.connection_invaild_msg), 0, 1);
                }
            }
        });
    }

    private void rejectDialog(String docId) {
        final Dialog dialog = new Dialog(getActivity(), R.style.PauseDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
        dialog.setContentView(R.layout.reject_dialog);
        EditText editText = dialog.findViewById(R.id.rejectReason);
        Button send = dialog.findViewById(R.id.btn_reject);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(editText.getText())) {
                    adminReservationsViewModels.rejectReserve(docId, editText.getText().toString());
                    dialog.dismiss();
                }
            }
        });

        dialog.show();
    }

}
