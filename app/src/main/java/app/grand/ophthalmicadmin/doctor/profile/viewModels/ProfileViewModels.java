package app.grand.ophthalmicadmin.doctor.profile.viewModels;


import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.Bindable;
import androidx.databinding.BindingAdapter;
import androidx.databinding.library.baseAdapters.BR;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import app.grand.ophthalmicadmin.PassingObject;
import app.grand.ophthalmicadmin.R;
import app.grand.ophthalmicadmin.auth.model.UserData;
import app.grand.ophthalmicadmin.base.BaseViewModel;
import app.grand.ophthalmicadmin.base.MyApplication;
import app.grand.ophthalmicadmin.base.UserPreferenceHelper;
import app.grand.ophthalmicadmin.base.constantsutils.Codes;
import app.grand.ophthalmicadmin.doctor.profile.models.DoctorDetails;

public class ProfileViewModels extends BaseViewModel {
    private UserData userData;
    private DoctorDetails doctorDetails;
    public Uri filePath;
    private StorageReference storageReference;
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private PassingObject passingObject;

    public ProfileViewModels() {
        doctorDetails = new DoctorDetails();
        passingObject = new PassingObject();
        storageReference = FirebaseStorage.getInstance().getReference();
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        userData = new UserData();
        setUserData(UserPreferenceHelper.getInstance(MyApplication.getInstance()).getUserData());
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(false)
                .build();
        db.setFirestoreSettings(settings);
    }

    public DoctorDetails getDoctorDetails() {
        return doctorDetails;
    }

    public void setDoctorDetails(DoctorDetails doctorDetails) {
        this.doctorDetails = doctorDetails;
    }

    public PassingObject getPassingObject() {
        return passingObject;
    }

    @Bindable
    public void setPassingObject(PassingObject passingObject) {
        notifyPropertyChanged(BR.passingObject);
        this.passingObject = passingObject;
    }

    public UserData getUserData() {
        return userData;
    }

    public void setUserData(UserData userData) {
        this.userData = userData;
    }

    public void updateProfileImage() {
        if (filePath != null) {

            accessLoadingBar(View.VISIBLE);
            StorageReference ref
                    = storageReference
                    .child(
                            "profile/"
                                    + auth.getCurrentUser().getUid());
            final UploadTask uploadTask = ref.putFile(filePath);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    setReturnedMessage(e.getMessage());
                    getClicksMutableLiveData().setValue(Codes.SHOW_MESSAGE_ERROR);
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }
                            return ref.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                Log.e("then", "then: " + task.getResult().toString());
                                updateUerImage(task.getResult().toString());
                            } else {
                                setReturnedMessage(task.getException().getMessage());
                                getClicksMutableLiveData().setValue(Codes.SHOW_MESSAGE_ERROR);
                            }
                        }
                    });
                }
            });
        }
    }

    private void updateUerImage(String imgUrl) {
        if (!TextUtils.isEmpty(imgUrl)) {
            //create hashMap to store name and image of user
            Map<String, Object> user_Map = new HashMap<>();
            user_Map.put("image", imgUrl);
            //create collection (users table ) and insert user id and Map
            db.collection("Users").document(auth.getCurrentUser().getUid()).update(user_Map).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        accessLoadingBar(View.GONE);
                        setReturnedMessage("Image Updated Successfully");
                        getClicksMutableLiveData().setValue(Codes.SHOW_MESSAGE_SUCCESS);
                        getUserData().setImage(imgUrl);
                        UserPreferenceHelper.getInstance(MyApplication.getInstance()).userLogin(getUserData());
                    } else {
                        setReturnedMessage(task.getException().getMessage());
                        getClicksMutableLiveData().setValue(Codes.SHOW_MESSAGE_ERROR);
                    }
                    accessLoadingBar(View.GONE);

                }
            });
        }
    }

    public void updateUserDate() {
        if (getPassingObject().getCode() == Codes.UPDATE_DATA) {
            updateDoctorProfile();
        } else {
            updateDoctorExtraData();
        }
    }

    private void updateDoctorExtraData() {
        Map<String, Object> user_Map = new HashMap<>();
        user_Map.put("master", getDoctorDetails().getMaster());
        user_Map.put("patient_number", getDoctorDetails().getPatient_number());
        accessLoadingBar(View.VISIBLE);
        db.collection("Doctor_details").document(getUserData().getId()).update(user_Map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    accessLoadingBar(View.GONE);
                    setReturnedMessage("Updated Successfully");
                    getClicksMutableLiveData().setValue(Codes.SHOW_MESSAGE_SUCCESS);
                } else {
                    accessLoadingBar(View.GONE);
                    setReturnedMessage(task.getException().getMessage());
                    getClicksMutableLiveData().setValue(Codes.SHOW_MESSAGE_SUCCESS);
                }
            }
        });
    }

    private void updateDoctorProfile() {
        Map<String, Object> user_Map = new HashMap<>();
        user_Map.put("user_name", getUserData().getUser_name());
        user_Map.put("phone", getUserData().getPhone());
        accessLoadingBar(View.VISIBLE);
        db.collection("Users").document(getUserData().getId()).update(user_Map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    accessLoadingBar(View.GONE);
                    UserData userData = UserPreferenceHelper.getInstance(MyApplication.getInstance()).getUserData();
                    userData.setPhone(getUserData().getPhone());
                    userData.setUser_name(getUserData().getUser_name());
                    UserPreferenceHelper.getInstance(MyApplication.getInstance()).userLogin(userData);
                    setReturnedMessage("Updated Successfully");
                    getClicksMutableLiveData().setValue(Codes.SHOW_MESSAGE_SUCCESS);
                } else {
                    accessLoadingBar(View.GONE);
                    setReturnedMessage(task.getException().getMessage());
                    getClicksMutableLiveData().setValue(Codes.SHOW_MESSAGE_SUCCESS);
                }
            }
        });
    }

    public void toBack() {
        getClicksMutableLiveData().setValue(Codes.BACK);
    }

    public void selectImage() {
        getClicksMutableLiveData().setValue(Codes.SELECT_PROFILE_IMAGE);
    }

    public void toUpdateAuth() {
        getClicksMutableLiveData().setValue(Codes.UPDATE_AUTH);
    }

    public void toUpdateData() {
        getClicksMutableLiveData().setValue(Codes.UPDATE_DATA);
    }

    @BindingAdapter({"userImage"})
    public static void loadImage(ImageView view, String countryImage) {
        if (!TextUtils.isEmpty(countryImage))
            Picasso.get().load(countryImage).placeholder(R.color.overlayBackground).into(view);
    }

    public void getExtraData() {
        accessLoadingBar(View.VISIBLE);
        Log.e("getExtraData", "getExtraData: " + UserPreferenceHelper.getInstance(MyApplication.getInstance()).getUserData().getId());
        db.collection("Doctor_details").document(UserPreferenceHelper.getInstance(MyApplication.getInstance()).getUserData().getId()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                accessLoadingBar(View.GONE);
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        DoctorDetails doctorDetails = new DoctorDetails();
                        Map<String, Object> objectMap = new HashMap<>();
                        objectMap.putAll(document.getData());
                        for (Map.Entry me : objectMap.entrySet()) {
                            String key = me.getKey().toString();
                            String value = me.getValue().toString();
                            if (key.equals("degree")) {
                                doctorDetails.setMaster(value);
                            }
                            if (key.equals("patient_number")) {
                                doctorDetails.setPatient_number(value);
                            }
                        }
                        setDoctorDetails(doctorDetails);
                        notifyChange();
                    } else {
                        setReturnedMessage("User Does not exists");
                        getClicksMutableLiveData().setValue(Codes.SHOW_MESSAGE_ERROR);
                    }
                } else {
                    setReturnedMessage(task.getException().getMessage());
                    getClicksMutableLiveData().setValue(Codes.SHOW_MESSAGE_ERROR);
                }
            }
        });
    }
}
