package app.grand.ophthalmicadmin.auth.viewModels;

import android.view.View;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.rpc.Code;

import java.util.HashMap;
import java.util.Map;

import app.grand.ophthalmicadmin.auth.model.LoginRequest;
import app.grand.ophthalmicadmin.auth.model.RegisterRequest;
import app.grand.ophthalmicadmin.auth.model.UserData;
import app.grand.ophthalmicadmin.base.BaseViewModel;
import app.grand.ophthalmicadmin.base.MovementManager;
import app.grand.ophthalmicadmin.base.MyApplication;
import app.grand.ophthalmicadmin.base.UserPreferenceHelper;
import app.grand.ophthalmicadmin.base.constantsutils.Codes;


public class AuthViewModels extends BaseViewModel {
    LoginRequest loginRequest;
    private RegisterRequest registerRequest;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    public AuthViewModels() {
        loginRequest = new LoginRequest();
        registerRequest = new RegisterRequest();
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
    }

    public LoginRequest getLoginRequest() {
        return loginRequest;
    }

    public RegisterRequest getRegisterRequest() {
        return registerRequest;
    }

    public void login() {
        getClicksMutableLiveData().setValue(Codes.CHECK_ERRORS);
    }

    public void loginAction() {
        accessLoadingBar(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(getLoginRequest().getEmail(), getLoginRequest().getPassword()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    accessLoadingBar(View.GONE);
                    setReturnedMessage(task.getException().getMessage());
                    getClicksMutableLiveData().setValue(Codes.SHOW_MESSAGE_ERROR);
                } else {
                    final String user_Id = mAuth.getCurrentUser().getUid();
                    Map<String, Object> token_Map = new HashMap<>();
                    token_Map.put("token", UserPreferenceHelper.getInstance(MyApplication.getInstance()).getToken());
                    db.collection("Users").document(user_Id).update(token_Map)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    getUserData(user_Id);
                                }
                            });

                }
            }
        });
    }

    private void getUserData(String user_Id) {
        db.collection("Users").document(user_Id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                accessLoadingBar(View.GONE);
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        UserData userData = new UserData();
                        Map<String, Object> objectMap = new HashMap<>();
                        objectMap.putAll(document.getData());
                        for (Map.Entry me : objectMap.entrySet()) {
                            String key = me.getKey().toString();
                            String value = me.getValue().toString();
                            if (key.equals("user_name")) {
                                userData.setUser_name(value);
                            }
                            if (key.equals("image")) {
                                userData.setImage(value);
                            }
                            if (key.equals("type")) {
                                userData.setType(value);
                            }
                            if (key.equals("phone")) {
                                userData.setPhone(value);
                            }
                        }
                        userData.setId(user_Id);
                        userData.setEmail(getLoginRequest().getEmail());
                        UserPreferenceHelper.getInstance(MyApplication.getInstance()).userLogin(userData);
                        if (UserPreferenceHelper.getInstance(MyApplication.getInstance()).getUserData().getType().equals("1"))
                           getClicksMutableLiveData().setValue(Codes.DOCTOR_HOME);
                        else if (UserPreferenceHelper.getInstance(MyApplication.getInstance()).getUserData().getType().equals("2"))
                            getClicksMutableLiveData().setValue(Codes.Specialist_HOME);
                        else if (UserPreferenceHelper.getInstance(MyApplication.getInstance()).getUserData().getType().equals("3"))
                            getClicksMutableLiveData().setValue(Codes.ADMIN_HOME);

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

    public void toForget() {
        getClicksMutableLiveData().setValue(Codes.SEND_CODE_SCREEN);
    }


}
