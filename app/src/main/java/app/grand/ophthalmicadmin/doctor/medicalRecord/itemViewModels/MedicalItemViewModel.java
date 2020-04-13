package app.grand.ophthalmicadmin.doctor.medicalRecord.itemViewModels;

import android.widget.ImageView;

import androidx.databinding.Bindable;
import androidx.databinding.BindingAdapter;

import app.grand.ophthalmicadmin.base.BaseViewModel;
import app.grand.ophthalmicadmin.base.constantsutils.Codes;
import app.grand.ophthalmicadmin.doctor.reservation.models.ReservationsResponse;

public class MedicalItemViewModel extends BaseViewModel {
    private ReservationsResponse reservationsResponse;

    public MedicalItemViewModel(ReservationsResponse reservationsResponse) {
        this.reservationsResponse = reservationsResponse;
    }

    @Bindable
    public ReservationsResponse getReservationsResponse() {
        return reservationsResponse;
    }

    public void toRecordDetails() {
        getClicksMutableLiveData().setValue(Codes.MEDICAL_RECORD_DETAILS);
    }
}
