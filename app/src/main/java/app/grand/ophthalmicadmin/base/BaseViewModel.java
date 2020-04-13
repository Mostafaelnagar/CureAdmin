package app.grand.ophthalmicadmin.base;


import android.app.Activity;
import android.content.Context;

import androidx.databinding.BaseObservable;
import androidx.lifecycle.MutableLiveData;


public class BaseViewModel extends BaseObservable {
    private String returnedMessage;
    private MutableLiveData<Integer> clicksMutableLiveData;
    private Context context;

    public BaseViewModel() {

    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void accessLoadingBar(int visiablity) {
        getClicksMutableLiveData().setValue(visiablity);
    }

    public void goBack(Context context) {
        ((Activity) context).finish();
    }


    public MutableLiveData<Integer> getClicksMutableLiveData() {
        if (clicksMutableLiveData == null) clicksMutableLiveData = new MutableLiveData<>();
        return clicksMutableLiveData;
    }

    public String getReturnedMessage() {
        return returnedMessage;
    }

    public void setClicksMutableLiveData(MutableLiveData<Integer> clicksMutableLiveData) {
        this.clicksMutableLiveData = clicksMutableLiveData;
    }

    public void setReturnedMessage(String returnedMessage) {
        this.returnedMessage = returnedMessage;
    }
}
