package app.grand.ophthalmicadmin.specialist.reservations;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;

import app.grand.ophthalmicadmin.R;
import app.grand.ophthalmicadmin.base.BaseFragment;
import app.grand.ophthalmicadmin.base.constantsutils.Codes;
import app.grand.ophthalmicadmin.databinding.FragmentSpecialistReservationsBinding;
import app.grand.ophthalmicadmin.specialist.reservations.viewModels.SpecialistReservationsViewModels;


public class SpecialistReservationsFragment extends BaseFragment {
    FragmentSpecialistReservationsBinding reservationsBinding;
    SpecialistReservationsViewModels reservationsViewModels;


    public SpecialistReservationsFragment() {
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
        reservationsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_specialist_reservations, container, false);
        reservationsViewModels = new SpecialistReservationsViewModels();
        reservationsBinding.setReserveViewModel(reservationsViewModels);
        liveDataListeners();
        checkConnection();
        return reservationsBinding.getRoot();
    }


    private void liveDataListeners() {
        reservationsViewModels.getClicksMutableLiveData().observe(this, result -> {
            if (result == View.VISIBLE || result == View.GONE) {
                accessLoadingBar(result);
            } else if (result == Codes.SHOW_MESSAGE_ERROR) {
                showMessage(reservationsViewModels.getReturnedMessage(), 0, 1);
            }
        });

        ConnectionLiveData.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isConnected) {
                if (isConnected) {
                    reservationsViewModels.getReservations();
                } else {
                    showMessage(getActivity().getResources().getString(R.string.connection_invaild_msg), 0, 1);
                }
            }
        });


    }


}
