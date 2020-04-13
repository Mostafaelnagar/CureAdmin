package app.grand.ophthalmicadmin;


import android.os.Handler;

import app.grand.ophthalmicadmin.base.BaseViewModel;
import app.grand.ophthalmicadmin.base.constantsutils.Codes;


public class SplashScreenViewModel extends BaseViewModel {
    public SplashScreenViewModel() {
        startApp();
    }


    private void startApp() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getClicksMutableLiveData().setValue(Codes.LOGIN_SCREEN);
            }
        }, 2000);

    }

}
