package app.grand.ophthalmicadmin;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import app.grand.ophthalmicadmin.admin.HomeAdminFragment;
import app.grand.ophthalmicadmin.admin.adminReservations.AdminReservationsContainerFragment;
import app.grand.ophthalmicadmin.admin.adminSepcialist.AdminAddSpecialistFragment;
import app.grand.ophthalmicadmin.admin.adminSepcialist.AdminSpecialistFragment;
import app.grand.ophthalmicadmin.admin.department.AddNewDepartmentsFragment;
import app.grand.ophthalmicadmin.admin.department.DepartmentsFragment;
import app.grand.ophthalmicadmin.admin.department.EditDepartmentsFragment;
import app.grand.ophthalmicadmin.admin.doctors.AdminAddDoctorsFragment;
import app.grand.ophthalmicadmin.admin.doctors.AdminDoctorProfileFragment;
import app.grand.ophthalmicadmin.admin.doctors.AdminDoctorReservationsFragment;
import app.grand.ophthalmicadmin.admin.doctors.AdminDoctorsFragment;
import app.grand.ophthalmicadmin.admin.profile.ProfileFragment;
import app.grand.ophthalmicadmin.admin.profile.UpdateAuthFragment;
import app.grand.ophthalmicadmin.auth.EmailConfirmationFragment;
import app.grand.ophthalmicadmin.auth.LoginFragment;
import app.grand.ophthalmicadmin.base.MovementManager;
import app.grand.ophthalmicadmin.base.Params;
import app.grand.ophthalmicadmin.base.ParentActivity;
import app.grand.ophthalmicadmin.base.UserPreferenceHelper;
import app.grand.ophthalmicadmin.base.constantsutils.Codes;
import app.grand.ophthalmicadmin.databinding.ActivityBaseBinding;
import app.grand.ophthalmicadmin.doctor.HomeDoctorFragment;
import app.grand.ophthalmicadmin.doctor.medicalRecord.MedicalRecordDetailsFragment;
import app.grand.ophthalmicadmin.doctor.medicalRecord.MedicalRecordFragment;
import app.grand.ophthalmicadmin.doctor.posts.NewPostFragment;
import app.grand.ophthalmicadmin.doctor.profile.DoctorProfileFragment;
import app.grand.ophthalmicadmin.doctor.profile.DoctorUpdateAuthFragment;
import app.grand.ophthalmicadmin.doctor.reservation.PatientProfileFragment;
import app.grand.ophthalmicadmin.doctor.reservation.ReservationsDetailsFragment;
import app.grand.ophthalmicadmin.doctor.reservation.ReservationsFragment;
import app.grand.ophthalmicadmin.specialist.HomeSpecialistFragment;
import app.grand.ophthalmicadmin.specialist.reservations.SpecialistReservationsFragment;
import app.grand.ophthalmicadmin.specialist.reservations.SpecialistUpdateReservationFragment;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class BaseActivity extends ParentActivity {
    public ActivityBaseBinding activityBaseBinding;
    public String lang;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/font1.otf")
                .setFontAttrId(R.attr.fontPath)
                .build());
        super.onCreate(savedInstanceState);
        activityBaseBinding = DataBindingUtil.setContentView(this, R.layout.activity_base);
        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, 1);
        lang = UserPreferenceHelper.getInstance(this).getCurrentLanguage(this);
        if (getIntent().hasExtra(Params.INTENT_PAGE)) {
            addFragment(getIntent().getIntExtra(Params.INTENT_PAGE, 0));
        } else {
            addFragment(Codes.SPLASH);
        }
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.e("getInstanceId", "getInstanceId failed" + task.getException());
                            return;
                        }
                        // Get new Instance ID token
                        String token = task.getResult().getToken();
                        UserPreferenceHelper.getInstance(BaseActivity.this).saveToken(token);
                    }
                });
    }


    private void addFragment(int page) {
        Log.e("addFragment", "addFragment: " + page);
        if (page == Codes.SPLASH) {
            MovementManager.addFragment(this, new SplashFragment(), "");
        } else if (page == Codes.LOGIN_SCREEN) {
            MovementManager.addFragment(this, new LoginFragment(), "");
        } else if (page == Codes.SEND_CODE_SCREEN) {
            MovementManager.addFragment(this, new EmailConfirmationFragment(), "");
        } else if (page == Codes.DOCTOR_HOME) {
            MovementManager.addFragment(this, new HomeDoctorFragment(), "");
        } else if (page == Codes.ADMIN_HOME) {
            MovementManager.addFragment(this, new HomeAdminFragment(), "");
        } else if (page == Codes.Specialist_HOME) {
            MovementManager.addFragment(this, new HomeSpecialistFragment(), "");
        } else if (page == Codes.DOCTOR_RESERVATIONS) {
            MovementManager.addFragment(this, new ReservationsFragment(), "");
        } else if (page == Codes.ADMIN_RESERVATIONS) {
            MovementManager.addFragment(this, new AdminReservationsContainerFragment(), "");
        } else if (page == Codes.ADMIN_DOCTORS) {
            MovementManager.addFragment(this, new AdminDoctorsFragment(), "");
        } else if (page == Codes.ADMIN_DEPARTAMENTS) {
            MovementManager.addFragment(this, new DepartmentsFragment(), "");
        } else if (page == Codes.ADMIN_SEPIALIST) {
            MovementManager.addFragment(this, new AdminSpecialistFragment(), "");
        } else if (page == Codes.ADMIN_PROFILE) {
            MovementManager.addFragment(this, new ProfileFragment(), "");
        } else if (page == Codes.ADD_NEW_DEPARTMENT) {
            MovementManager.addFragment(this, new AddNewDepartmentsFragment(), "");
        } else if (page == Codes.DOCTOR_POSTS) {
            MovementManager.addFragment(this, new NewPostFragment(), "");
        } else if (page == Codes.DOCTOR_PROFILE) {
            MovementManager.addFragment(this, new DoctorProfileFragment(), "");
        } else if (page == Codes.ADD_DOCTOR) {
            MovementManager.addFragment(this, new AdminAddDoctorsFragment(), "");
        } else if (page == Codes.ADD_SPECIALIST) {
            MovementManager.addFragment(this, new AdminAddSpecialistFragment(), "");
        } else if (page == Codes.Specialist_RESERVATIONS) {
            MovementManager.addFragment(this, new SpecialistReservationsFragment(), "");
        } else if (page == Codes.DOCTOR_RESERVATIONS_DETAILS) {
            ReservationsDetailsFragment fragment = new ReservationsDetailsFragment();
            Bundle bundle = new Bundle();
            bundle.putString(Params.BUNDLE, getIntent().getStringExtra(Params.BUNDLE));
            fragment.setArguments(bundle);
            MovementManager.addFragment(this, fragment, "");
        } else if (page == Codes.PATIENT_DETAILS) {
            PatientProfileFragment fragment = new PatientProfileFragment();
            Bundle bundle = new Bundle();
            bundle.putString(Params.BUNDLE, getIntent().getStringExtra(Params.BUNDLE));
            fragment.setArguments(bundle);
            MovementManager.addFragment(this, fragment, "");
        } else if (page == Codes.MEDICAL_RECORD) {
            MedicalRecordFragment fragment = new MedicalRecordFragment();
            Bundle bundle = new Bundle();
            bundle.putString(Params.BUNDLE, getIntent().getStringExtra(Params.BUNDLE));
            fragment.setArguments(bundle);
            MovementManager.addFragment(this, fragment, "");
        } else if (page == Codes.MEDICAL_RECORD_DETAILS) {
            MedicalRecordDetailsFragment fragment = new MedicalRecordDetailsFragment();
            Bundle bundle = new Bundle();
            bundle.putString(Params.BUNDLE, getIntent().getStringExtra(Params.BUNDLE));
            fragment.setArguments(bundle);
            MovementManager.addFragment(this, fragment, "");
        } else if (page == Codes.UPDATE_AUTH || page == Codes.UPDATE_DATA) {
            DoctorUpdateAuthFragment fragment = new DoctorUpdateAuthFragment();
            Bundle bundle = new Bundle();
            bundle.putString(Params.BUNDLE, getIntent().getStringExtra(Params.BUNDLE));
            fragment.setArguments(bundle);
            MovementManager.addFragment(this, fragment, "");
        } else if (page == Codes.ADMIN_DOCTORS_RESERVATIONS) {
            AdminDoctorReservationsFragment fragment = new AdminDoctorReservationsFragment();
            Bundle bundle = new Bundle();
            bundle.putString(Params.BUNDLE, getIntent().getStringExtra(Params.BUNDLE));
            fragment.setArguments(bundle);
            MovementManager.addFragment(this, fragment, "");
        } else if (page == Codes.ADMIN_DOCTORS_PROFILE) {
            AdminDoctorProfileFragment fragment = new AdminDoctorProfileFragment();
            Bundle bundle = new Bundle();
            bundle.putString(Params.BUNDLE, getIntent().getStringExtra(Params.BUNDLE));
            fragment.setArguments(bundle);
            MovementManager.addFragment(this, fragment, "");
        } else if (page == Codes.DEPARTMENT_DETAILS) {
            EditDepartmentsFragment fragment = new EditDepartmentsFragment();
            Bundle bundle = new Bundle();
            bundle.putString(Params.BUNDLE, getIntent().getStringExtra(Params.BUNDLE));
            fragment.setArguments(bundle);
            MovementManager.addFragment(this, fragment, "");
        } else if (page == Codes.UPDATE_ADMIN_AUTH || page == Codes.UPDATE_ADMIN_DATA) {
            UpdateAuthFragment fragment = new UpdateAuthFragment();
            Bundle bundle = new Bundle();
            bundle.putString(Params.BUNDLE, getIntent().getStringExtra(Params.BUNDLE));
            fragment.setArguments(bundle);
            MovementManager.addFragment(this, fragment, "");
        } else if (page == Codes.Specialist_UPDTAE_IMAGE) {
            SpecialistUpdateReservationFragment fragment = new SpecialistUpdateReservationFragment();
            Bundle bundle = new Bundle();
            bundle.putString(Params.BUNDLE, getIntent().getStringExtra(Params.BUNDLE));
            fragment.setArguments(bundle);
            MovementManager.addFragment(this, fragment, "");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


    }

}