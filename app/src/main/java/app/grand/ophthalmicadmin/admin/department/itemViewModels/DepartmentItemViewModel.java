package app.grand.ophthalmicadmin.admin.department.itemViewModels;

import android.widget.ImageView;

import androidx.databinding.Bindable;
import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;

import app.grand.ophthalmicadmin.admin.doctors.models.DepartmentModel;
import app.grand.ophthalmicadmin.base.BaseViewModel;
import app.grand.ophthalmicadmin.base.constantsutils.Codes;


public class DepartmentItemViewModel extends BaseViewModel {
    private DepartmentModel departmentmodel;

    public DepartmentItemViewModel(DepartmentModel departmentmodel) {
        this.departmentmodel = departmentmodel;
    }


    @Bindable
    public DepartmentModel getDepartmentmodel() {
        return departmentmodel;
    }

    public void toDepartmentDetails() {
        getClicksMutableLiveData().setValue(Codes.DEPARTMENT_DETAILS);
    }

    @BindingAdapter({"departmentImage"})
    public static void loadImage(ImageView view, String departmentImage) {
        if (!departmentImage.equals(""))
            Glide.with(view.getContext()).load(departmentImage).into(view);
    }

}
