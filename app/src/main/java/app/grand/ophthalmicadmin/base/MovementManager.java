package app.grand.ophthalmicadmin.base;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.FieldValue;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import app.grand.ophthalmicadmin.BaseActivity;
import app.grand.ophthalmicadmin.PassingObject;
import app.grand.ophthalmicadmin.R;
import app.grand.ophthalmicadmin.base.constantsutils.Codes;


public class MovementManager {

    public static void pickImage(final Context context) {
        String choiceString[] = new String[]{"Gallery"};
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setIcon(R.mipmap.ic_launcher);
        dialog.setTitle("Select image from");
        dialog.setItems(choiceString,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = null;
                        if (which == 0) {
                            intent = new Intent(
                                    Intent.ACTION_PICK,
                                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        }
//                        else {
//                            intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                        }
                        ((AppCompatActivity) context).startActivityForResult(Intent.createChooser(intent, "Select profile picture"), Codes.FILE_TYPE_IMAGE);
                    }
                }).show();

    }

    //---------Fragments----------//
    private static final int CONTAINER_ID = R.id.fl_home_container;


    public static void addFragment(Context context, Fragment fragment, String backStackText) {
        FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction().add(CONTAINER_ID, fragment);
        if (!backStackText.equals("")) {
            fragmentTransaction.addToBackStack(backStackText);
        }
        fragmentTransaction.commit();
    }

    public static void removeFragment(Context context, Fragment fragment, String backStackText) {
        FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction().remove(fragment);
        if (!backStackText.equals("")) {
            fragmentTransaction.addToBackStack(backStackText);
        }
        fragmentTransaction.commit();
    }

    public static void addHomeFragment(Context context, Fragment fragment, String backStackText) {
        FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction().add(R.id.ll_base_container, fragment);
        if (!backStackText.equals("")) {
            fragmentTransaction.addToBackStack(backStackText);
        }
        fragmentTransaction.commit();
    }


    public static void replaceHomeFragment(Context context, int layout, Fragment fragment, String backStackText) {
        FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction().replace(layout, fragment);
        if (!backStackText.equals("")) {
            fragmentTransaction.addToBackStack(backStackText);
        }
        fragmentTransaction.commit();
    }


    //-----------Activities-----------------//

    public static void startBaseActivity(Context context, int page) {
        Intent intent = new Intent(context, BaseActivity.class);
        intent.putExtra(Params.INTENT_PAGE, page);
        context.startActivity(intent);
        ((Activity) context).finishAffinity();
    }

    public static void startActivity(Context context, int page) {
        Intent intent = new Intent(context, BaseActivity.class);
        intent.putExtra(Params.INTENT_PAGE, page);
        context.startActivity(intent);
    }

    public static void startActivityWithObject(Context context, int page, PassingObject object) {
        Intent intent = new Intent(context, BaseActivity.class);
        intent.putExtra(Params.INTENT_PAGE, page);
        intent.putExtra(Params.BUNDLE, new Gson().toJson(object));
        context.startActivity(intent);
    }


    public static void loggout(Context context) {
        UserPreferenceHelper.getInstance(context).loggout();
        ((Activity) context).finishAffinity();
        startActivity(context, Codes.SPLASH);
    }
}
