package app.grand.ophthalmicadmin.admin.adminReservations;


import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;

import app.grand.ophthalmicadmin.PassingObject;
import app.grand.ophthalmicadmin.R;
import app.grand.ophthalmicadmin.admin.adminReservations.viewModels.AdminReservationsViewModels;
import app.grand.ophthalmicadmin.base.BaseFragment;
import app.grand.ophthalmicadmin.base.constantsutils.Codes;
import app.grand.ophthalmicadmin.databinding.FragmentAdminWaitingReservationsBinding;


public class AdminWaitingReservationsFragment extends BaseFragment {
    FragmentAdminWaitingReservationsBinding currentReservationsBinding;
    AdminReservationsViewModels adminReservationsViewModels;


    public AdminWaitingReservationsFragment() {
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
        currentReservationsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_admin_waiting_reservations, container, false);
        adminReservationsViewModels = new AdminReservationsViewModels();
        currentReservationsBinding.setCurrentViewModel(adminReservationsViewModels);
        liveDataListeners();
        checkConnection();
        adminReservationsViewModels.getReservations("waiting");
        return currentReservationsBinding.getRoot();
    }


    private void liveDataListeners() {
        adminReservationsViewModels.getClicksMutableLiveData().observe(this, result -> {
            if (result == View.VISIBLE || result == View.GONE) {
                accessLoadingBar(result);
            } else if (result == Codes.SHOW_MESSAGE_SUCCESS) {
                showMessage(adminReservationsViewModels.getReturnedMessage(), 0, 0);
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
