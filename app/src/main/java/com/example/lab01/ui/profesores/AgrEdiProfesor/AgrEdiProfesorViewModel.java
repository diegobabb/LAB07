package com.example.lab01.ui.profesores.AgrEdiProfesor;

import android.util.Patterns;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.lab01.R;

public class AgrEdiProfesorViewModel extends ViewModel {
    private MutableLiveData<AgrEdiProfesorFormState> agrEdiProfesorFormState = new MutableLiveData<>();
    private MutableLiveData<AgrEdiProfesorResult> agrEdiProfesorResult = new MutableLiveData<>();

    LiveData<AgrEdiProfesorFormState> getAgrEdiProfesorFormState() {
        return agrEdiProfesorFormState;
    }

    LiveData<AgrEdiProfesorResult> getAgrEdiProfesorResult() {
        return agrEdiProfesorResult;
    }

    public void AgrEdiProfesorDataChanged(String username, String cedula, String phone, String email) {
        if (!isUserNameValid(username)) {
            agrEdiProfesorFormState.setValue(new AgrEdiProfesorFormState(R.string.invalid_username, null, null, null));
        } else if (!isCedulaOrPhonoValid(cedula, 9)) {
            agrEdiProfesorFormState.setValue(new AgrEdiProfesorFormState(null, R.string.invalid_cedula, null, null));
        } else if (!isCedulaOrPhonoValid(phone, 8)) {
            agrEdiProfesorFormState.setValue(new AgrEdiProfesorFormState(null, null, R.string.invalid_phone, null));
        } else if (!isEmailValid(email)) {
            agrEdiProfesorFormState.setValue(new AgrEdiProfesorFormState(null, null, null, R.string.invalid_email));
        } else {
            agrEdiProfesorFormState.setValue(new AgrEdiProfesorFormState(true));
        }
    }

    private boolean isUserNameValid(String username) {
        return !username.trim().isEmpty();
    }

    private boolean isCedulaOrPhonoValid(String cedulaOrPhone, int size){
        return !cedulaOrPhone.trim().isEmpty() && cedulaOrPhone.length() == size;
    }

    private boolean isEmailValid(String email){
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
