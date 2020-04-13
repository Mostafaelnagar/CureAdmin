package app.grand.ophthalmicadmin.doctor.reservation.itemViewModels;

import android.widget.ImageView;

import androidx.databinding.Bindable;
import androidx.databinding.BindingAdapter;

import com.squareup.picasso.Picasso;

import app.grand.ophthalmicadmin.R;
import app.grand.ophthalmicadmin.base.BaseViewModel;
import app.grand.ophthalmicadmin.base.constantsutils.Codes;
import app.grand.ophthalmicadmin.doctor.reservation.models.ReservationsResponse;


public class ReservationsDetailsItemViewModel extends BaseViewModel {
    private ReservationsResponse reservationsResponse;

    public ReservationsDetailsItemViewModel(ReservationsResponse reservationsResponse) {
        this.reservationsResponse = reservationsResponse;
    }


    @Bindable
    public ReservationsResponse getReservationsResponse() {
        return reservationsResponse;
    }

    public void toPatient() {
        getClicksMutableLiveData().setValue(Codes.PATIENT_DETAILS);
    }

    @BindingAdapter({"patientImage"})
    public static void loadImage(ImageView view, String countryImage) {
        if (!countryImage.equals(""))
            Picasso.get().load(countryImage).placeholder(R.color.overlayBackground).into(view);
    }
}
