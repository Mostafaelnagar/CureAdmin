package app.grand.ophthalmicadmin.doctor.posts.viewModels;


import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.databinding.Bindable;
import androidx.databinding.BindingAdapter;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import app.grand.ophthalmicadmin.PassingObject;
import app.grand.ophthalmicadmin.R;
import app.grand.ophthalmicadmin.base.BaseViewModel;
import app.grand.ophthalmicadmin.base.MyApplication;
import app.grand.ophthalmicadmin.base.UserPreferenceHelper;
import app.grand.ophthalmicadmin.base.constantsutils.Codes;
import app.grand.ophthalmicadmin.doctor.models.DiagnosisRequest;

public class NewPostViewModels extends BaseViewModel {
    private FirebaseFirestore firebaseFirestore;
    public String postContent;

    public NewPostViewModels() {
        firebaseFirestore = FirebaseFirestore.getInstance();
    }


    public void createPost() {
        if (postContent != null && !postContent.equals("") && !postContent.equals(" ")) {
            accessLoadingBar(View.VISIBLE);
            Map<String, Object> reserve = new HashMap<>();
            reserve.put("post_content", postContent);
            reserve.put("post_author", UserPreferenceHelper.getInstance(MyApplication.getInstance()).getUserData());
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            reserve.put("post_date", df.format(new Date()));

            firebaseFirestore.collection("Posts").document().set(reserve).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        accessLoadingBar(View.GONE);
                        setReturnedMessage("Post Addedd successfully");
                        getClicksMutableLiveData().setValue(Codes.SHOW_MESSAGE_SUCCESS);

                    } else {
                        accessLoadingBar(View.GONE);
                        setReturnedMessage(task.getException().getMessage());
                        getClicksMutableLiveData().setValue(Codes.SHOW_MESSAGE_ERROR);
                    }
                }
            });
        }
    }


}
