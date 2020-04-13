package app.grand.ophthalmicadmin.base;

import android.content.Intent;
import android.graphics.Color;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.muddzdev.styleabletoast.StyleableToast;

import app.grand.ophthalmicadmin.R;

public class ParentActivity extends AppCompatActivity {

    public void showMessage(String message, int type) {
        if (type == 0) {//success
            new StyleableToast
                    .Builder(MyApplication.getInstance())
                    .text(message)
                    .textColor(this.getResources().getColor(R.color.accept_clr))
                    .iconEnd(R.mipmap.ic_success_icon)
                    .stroke(2, this.getResources().getColor(R.color.green))
                    .textSize(14)
                    .textBold()
                    .backgroundColor(Color.WHITE)
                    .show();
        } else {
            new StyleableToast
                    .Builder(MyApplication.getInstance())
                    .text(message)
                    .textColor(this.getResources().getColor(R.color.red))
                    .stroke(2, this.getResources().getColor(R.color.red))
                    .textSize(14)
                    .textBold()
                    .iconEnd(R.mipmap.ic_warning_icon)
                    .backgroundColor(Color.WHITE)
                    .show();
        }
    }

    public void setTitle(String title) {

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fl_home_container);
        fragment.onActivityResult(requestCode, resultCode, data);
    }
}
