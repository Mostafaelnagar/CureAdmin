package app.grand.ophthalmicadmin.doctor.reservation;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;

import com.google.gson.Gson;

import app.grand.ophthalmicadmin.PassingObject;
import app.grand.ophthalmicadmin.R;
import app.grand.ophthalmicadmin.base.BaseFragment;
import app.grand.ophthalmicadmin.base.Params;
import app.grand.ophthalmicadmin.base.constantsutils.Codes;
import app.grand.ophthalmicadmin.databinding.FragmentReservationsBinding;
import app.grand.ophthalmicadmin.databinding.FragmentReservationsDetailsBinding;
import app.grand.ophthalmicadmin.doctor.reservation.viewModels.ReservationsDetailsViewModels;
import app.grand.ophthalmicadmin.doctor.reservation.viewModels.ReservationsViewModels;


public class ReservationsDetailsFragment extends BaseFragment {
    FragmentReservationsDetailsBinding detailsBinding;
    ReservationsDetailsViewModels detailsViewModels;
    Bundle bundle;
    String passingObject;


    public ReservationsDetailsFragment() {
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
        detailsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_reservations_details, container, false);
        detailsViewModels = new ReservationsDetailsViewModels();
        detailsBinding.setReserveViewModel(detailsViewModels);
        bundle = this.getArguments();

        liveDataListeners();
        checkConnection();
        return detailsBinding.getRoot();
    }


    private void liveDataListeners() {
        detailsViewModels.getClicksMutableLiveData().observe(this, result -> {
            if (result == View.VISIBLE || result == View.GONE) {
                accessLoadingBar(result);
            } else if (result == Codes.SHOW_MESSAGE_ERROR) {
                showMessage(detailsViewModels.getReturnedMessage(), 0, 1);
            }
        });

        ConnectionLiveData.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isConnected) {
                if (isConnected) {
                    if (bundle != null) {
                        passingObject = bundle.getString(Params.BUNDLE);
                        detailsViewModels.setPassingObject(new Gson().fromJson(passingObject, PassingObject.class));
                        detailsViewModels.getReservations();
                    }
                } else {
                    showMessage(getActivity().getResources().getString(R.string.connection_invaild_msg), 0, 1);
                }
            }
        });


    }


}
