package app.grand.ophthalmicadmin.admin.adminReservations.itemViewModels;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.Bindable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.HashMap;
import java.util.Map;

import app.grand.ophthalmicadmin.base.BaseViewModel;
import app.grand.ophthalmicadmin.base.MyApplication;
import app.grand.ophthalmicadmin.base.UserPreferenceHelper;
import app.grand.ophthalmicadmin.base.constantsutils.Codes;
import app.grand.ophthalmicadmin.doctor.reservation.models.ReservationsResponse;


public class AdminReservationsItemViewModel extends BaseViewModel {
    private ReservationsResponse reservationsResponse;
    public int status;

    public AdminReservationsItemViewModel(ReservationsResponse reservationsResponse) {
        this.reservationsResponse = reservationsResponse;
        if (reservationsResponse.getStatus().equals("waiting")) {
            status = View.VISIBLE;
        } else
            status = View.GONE;
    }


    @Bindable
    public ReservationsResponse getReservationsResponse() {
        return reservationsResponse;
    }

    public void toDoctorReservations() {
        getClicksMutableLiveData().setValue(Codes.DOCTOR_RESERVATIONS_DETAILS);
    }

    public void accept() {
        getClicksMutableLiveData().setValue(Codes.ADMIN_ACCEPT);
    }

    public void decline() {
        getClicksMutableLiveData().setValue(Codes.ADMIN_DECLINE);

    }
}
