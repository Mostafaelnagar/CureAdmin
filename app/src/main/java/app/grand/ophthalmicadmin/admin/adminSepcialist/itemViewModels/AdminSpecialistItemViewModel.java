package app.grand.ophthalmicadmin.admin.adminSepcialist.itemViewModels;


import android.util.Log;
import android.widget.ImageView;

import androidx.databinding.Bindable;
import androidx.databinding.BindingAdapter;

import com.squareup.picasso.Picasso;

import app.grand.ophthalmicadmin.R;
import app.grand.ophthalmicadmin.auth.model.UserData;
import app.grand.ophthalmicadmin.base.BaseViewModel;
import app.grand.ophthalmicadmin.base.constantsutils.Codes;


public class AdminSpecialistItemViewModel extends BaseViewModel {
    private UserData specialData;

    public AdminSpecialistItemViewModel(UserData specialData) {
        this.specialData = specialData;

    }

    @Bindable
    public UserData getSpecialData() {
        return specialData;
    }


    @BindingAdapter({"specialImage"})
    public static void loadImage(ImageView view, String countryImage) {
        if (!countryImage.equals(""))
            Picasso.get().load(countryImage).placeholder(R.color.overlayBackground).into(view);
    }
}
