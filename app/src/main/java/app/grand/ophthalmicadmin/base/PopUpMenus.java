package app.grand.ophthalmicadmin.base;


import android.content.Context;
import android.view.View;

import androidx.appcompat.widget.PopupMenu;

import java.util.List;

import app.grand.ophthalmicadmin.admin.doctors.models.DepartmentModel;

public class PopUpMenus {

    public static PopupMenu showWorkingDaysPopUp(Context context, View view, List<String> types) {
        PopupMenu typesPopUps;
        typesPopUps = new PopupMenu(context, view);
        for (int i = 0; i < types.size(); i++) {
            typesPopUps.getMenu().add(i, i, i, types.get(i));
        }
        typesPopUps.show();
        return typesPopUps;
    }

    public static PopupMenu showDepartment(Context context, View view, List<DepartmentModel> types) {
        PopupMenu typesPopUps;
        typesPopUps = new PopupMenu(context, view);
        for (int i = 0; i < types.size(); i++) {
            typesPopUps.getMenu().add(i, i, i, types.get(i).getName());
        }
        typesPopUps.show();
        return typesPopUps;
    }

//    public static PopupMenu showCatPopUp(Context context, View view, List<CategoriesItem> types) {
//        PopupMenu typesPopUps;
//        typesPopUps = new PopupMenu(context, view);
//        for (int i = 0; i < types.size(); i++) {
//            typesPopUps.getMenu().add(i, i, i, types.get(i).getName());
//        }
//        typesPopUps.show();
//        return typesPopUps;
//    }
//
//    public static PopupMenu showDistrictPopUp(Context context, View view, List<DistrictsItem> types) {
//        PopupMenu typesPopUps;
//        typesPopUps = new PopupMenu(context, view);
//        for (int i = 0; i < types.size(); i++) {
//            typesPopUps.getMenu().add(i, i, i, types.get(i).getName());
//        }
//        typesPopUps.show();
//        return typesPopUps;
//    }
//
//    public static PopupMenu showOptionsPopUp(Context context, View view, List<OptionsItem> types) {
//        PopupMenu typesPopUps;
//        typesPopUps = new PopupMenu(context, view);
//        for (int i = 0; i < types.size(); i++) {
//            typesPopUps.getMenu().add(i, i, i, types.get(i).getOption());
//        }
//        typesPopUps.show();
//        return typesPopUps;
//    }


}
