package app.grand.ophthalmicadmin.doctor.reservation.viewModels;


import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.Bindable;
import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.grand.ophthalmicadmin.PassingObject;
import app.grand.ophthalmicadmin.R;
import app.grand.ophthalmicadmin.base.BaseViewModel;
import app.grand.ophthalmicadmin.base.MyApplication;
import app.grand.ophthalmicadmin.base.UserPreferenceHelper;
import app.grand.ophthalmicadmin.base.constantsutils.Codes;
import app.grand.ophthalmicadmin.doctor.models.DiagnosisRequest;
import app.grand.ophthalmicadmin.doctor.reservation.adapters.ReservationsDetailsAdapter;
import app.grand.ophthalmicadmin.doctor.reservation.models.ReservationsResponse;

public class PatientProfileViewModels extends BaseViewModel {
    private FirebaseFirestore firebaseFirestore;
    PassingObject passingObject;
    private DiagnosisRequest diagnosisRequest;

    public PatientProfileViewModels() {
        firebaseFirestore = FirebaseFirestore.getInstance();
        passingObject = new PassingObject();
        diagnosisRequest = new DiagnosisRequest();
    }

    public DiagnosisRequest getDiagnosisRequest() {
        return diagnosisRequest;
    }

    public PassingObject getPassingObject() {
        return passingObject;
    }

    @Bindable
    public void setPassingObject(PassingObject passingObject) {
        notifyPropertyChanged(app.grand.ophthalmicadmin.BR.passingObject);
        this.passingObject = passingObject;
    }

    @BindingAdapter({"patientImage"})
    public static void loadImage(ImageView view, String countryImage) {
        if (!countryImage.equals(""))
            Picasso.get().load(countryImage).placeholder(R.color.overlayBackground).into(view);
    }

    public void toMedicalRecords() {
        getClicksMutableLiveData().setValue(Codes.MEDICAL_RECORD);
    }

    public void createDiagnosis() {
        accessLoadingBar(View.VISIBLE);
        Map<String, Object> reserve = new HashMap<>();
        reserve.put("note", getDiagnosisRequest().getNote());
        reserve.put("medicine", getDiagnosisRequest().getMedicine());

        firebaseFirestore.collection("Reservations").document(passingObject.getObject()).update(reserve).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    saveNotifications(getPassingObject().getObject(), "user");
                    if (getDiagnosisRequest().getRays() != null && !getDiagnosisRequest().getRays().equals("") && !getDiagnosisRequest().getRays().equals(" ")) {
                        addToRays();
                    } else {
                        accessLoadingBar(View.GONE);
                        setReturnedMessage("Diagnosis updated successfully");
                        getClicksMutableLiveData().setValue(Codes.SHOW_MESSAGE_SUCCESS);
                    }

                } else {
                    accessLoadingBar(View.GONE);
                    setReturnedMessage(task.getException().getMessage());
                    getClicksMutableLiveData().setValue(Codes.SHOW_MESSAGE_ERROR);
                }
            }
        });
    }

    private void addToRays() {
        accessLoadingBar(View.VISIBLE);
        Map<String, Object> rays = new HashMap<>();
        rays.put("patient_id", getPassingObject().getObjectClass().getId());
        rays.put("x_ray_image", "");
        rays.put("x_ray_name", getDiagnosisRequest().getRays());
        rays.put("x_ray_desc", "");
        rays.put("x_ray_result", "");

        firebaseFirestore.collection("X-Rays").document(getPassingObject().getObject()).set(rays).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    accessLoadingBar(View.GONE);
                    setReturnedMessage("Diagnosis updated successfully");
                    getClicksMutableLiveData().setValue(Codes.SHOW_MESSAGE_SUCCESS);
                    saveNotifications(getPassingObject().getObject(), "special");

                } else {
                    accessLoadingBar(View.GONE);
                    setReturnedMessage(task.getException().getMessage());
                    getClicksMutableLiveData().setValue(Codes.SHOW_MESSAGE_ERROR);
                }
            }
        });
    }

    private void saveNotifications(String id, String type) {
        Map<String, Object> reserve = new HashMap<>();
        reserve.put("title", "New reservation");
        if (type.equals("special")) {
            reserve.put("body", "new Ray");
            reserve.put("user_id", passingObject.getObject());
            reserve.put("to", "special");
        } else {
            reserve.put("body", "New Medicine available");
            reserve.put("user_id", getPassingObject().getObjectClass().getId());
            reserve.put("to", "user");
        }
        reserve.put("reserve_id", id);
        DocumentReference documentReference = firebaseFirestore.collection("Notifications").document();
        documentReference.set(reserve).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {

                } else {
                    accessLoadingBar(View.GONE);
                    setReturnedMessage(task.getException().getMessage());
                    getClicksMutableLiveData().setValue(Codes.SHOW_MESSAGE_ERROR);
                }
            }
        });
    }

}
