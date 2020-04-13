package app.grand.ophthalmicadmin.doctor.reservation.itemViewModels;

import android.widget.ImageView;

import androidx.databinding.Bindable;
import androidx.databinding.BindingAdapter;

import app.grand.ophthalmicadmin.base.BaseViewModel;
import app.grand.ophthalmicadmin.base.constantsutils.Codes;
import app.grand.ophthalmicadmin.doctor.reservation.models.ReservationsResponse;


public class ReservationsItemViewModel extends BaseViewModel {
    private ReservationsResponse reservationsResponse;

    public ReservationsItemViewModel(ReservationsResponse reservationsResponse) {
        this.reservationsResponse = reservationsResponse;
    }


    @Bindable
    public ReservationsResponse getReservationsResponse() {
        return reservationsResponse;
    }

    public void toDoctorReservations() {
        getClicksMutableLiveData().setValue(Codes.DOCTOR_RESERVATIONS_DETAILS);
    }

}
