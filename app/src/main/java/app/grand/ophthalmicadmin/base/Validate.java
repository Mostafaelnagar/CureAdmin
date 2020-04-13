package app.grand.ophthalmicadmin.base;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputLayout;
import com.jakewharton.rxbinding3.widget.RxTextView;

import java.net.InetAddress;
import java.util.regex.Pattern;

import app.grand.ophthalmicadmin.R;
import io.reactivex.disposables.Disposable;

@SuppressLint("NewApi")
public class Validate {
    public static String appColor = "#9966ff";

    //<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    public static boolean isInternetAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com"); //You can replace it with your name
            return ipAddr.equals("") ? false : true;
        } catch (Exception e) {
            return false;
        }

    }

    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }


    public static Boolean isEmpty(String str) {
        return str == null || str.isEmpty() ? true : false;
    }


    public static Boolean isAvLen(String str, int from, int to) {
        return str.length() >= from && str.length() <= to ? true : false;
    }

    public static Boolean isMail(CharSequence str) {

        return str == null ? false : android.util.Patterns.EMAIL_ADDRESS.matcher(str).matches();
    }


    public static Boolean isPhone(CharSequence str) {
//        Pattern SAUDI_PHONE = Pattern.compile("/^(009665|9665|\\+9665|05|5)(5|0|3|6|4|9|1|8|7)([0-9]{7})$/");

        return str == null ? false : android.util.Patterns.PHONE.matcher(str).matches();
    }

    public static Boolean isPassword(CharSequence str) {
        Pattern PASSWORD_PATTERN =
                Pattern.compile("^" +
                        "(?=.*[0-9])" +         //at least 1 digit
                        //"(?=.*[a-z])" +         //at least 1 lower case letter
                        //"(?=.*[A-Z])" +         //at least 1 upper case letter
                        "(?=.*[a-zA-Z])" +      //any letter
//                        "(?=.*[@#$%^&+=])" +    //at least 1 special character
                        "(?=\\S+$)" +           //no white spaces
                        ".{8,}" +               //at least 4 characters
                        "$");
        return str == null ? false : PASSWORD_PATTERN.matcher(str).matches();
    }
//type numbers
    // 0 =>> Email
    // 1 =>> password
    // 2 =>> phone
    // 3 =>> desc lenght
    // 4 =>> durations

    public static boolean isInputValid(TextInputLayout inputLayout, EditText editText, int type, Context context) {
        Disposable b = RxTextView.textChanges(editText)
                .subscribe(charSequence -> {
                    if (type == 0) {
                        boolean email = isMail(charSequence);
                        if (!email) {
                            inputLayout.setError(context.getResources().getString(R.string.invalidEmail));
                            result = false;
                        } else {
                            inputLayout.setErrorEnabled(false);
                            result = true;
                        }
                    } else if (type == 1) {
                        boolean password = isPassword(charSequence);
                        if (!password) {
                            inputLayout.setError(context.getResources().getString(R.string.invalidPassword));
                            result = false;
                        } else {
                            inputLayout.setErrorEnabled(false);
                            result = true;
                        }
                    } else {
                        if (!TextUtils.isEmpty(charSequence.toString())) {
                            inputLayout.setErrorEnabled(false);
                            result = true;
                        } else {
                            inputLayout.setError(context.getResources().getString(R.string.fieldRequired));
                            result = false;
                        }
                    }

                });
        return result;
    }
    static boolean result;

}
