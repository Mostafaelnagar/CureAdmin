package app.grand.ophthalmicadmin.admin.doctors.itemViewModels;


import android.util.Log;
import android.widget.ImageView;

import androidx.databinding.Bindable;
import androidx.databinding.BindingAdapter;

import com.squareup.picasso.Picasso;

import app.grand.ophthalmicadmin.R;
import app.grand.ophthalmicadmin.auth.model.UserData;
import app.grand.ophthalmicadmin.base.BaseViewModel;
import app.grand.ophthalmicadmin.base.constantsutils.Codes;


public class AdminDoctorsItemViewModel extends BaseViewModel {
    private UserData doctorData;

    public AdminDoctorsItemViewModel(UserData doctorData) {
        this.doctorData = doctorData;

    }

    @Bindable
    public UserData getDoctorData() {
        return doctorData;
    }

    public void toDoctorReservations() {
        getClicksMutableLiveData().setValue(Codes.ADMIN_DOCTORS_RESERVATIONS);
    }

    public void toDoctorProfile() {
        Log.e("toDoctorProfile", "toDoctorProfile: " );
        getClicksMutableLiveData().setValue(Codes.ADMIN_DOCTORS_PROFILE);
    }

    @BindingAdapter({"doctorImage"})
    public static void loadImage(ImageView view, String countryImage) {
        if (!countryImage.equals(""))
            Picasso.get().load(countryImage).placeholder(R.color.overlayBackground).into(view);
    }
}
