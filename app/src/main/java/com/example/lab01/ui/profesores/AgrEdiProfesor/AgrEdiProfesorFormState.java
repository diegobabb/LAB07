package com.example.lab01.ui.profesores.AgrEdiProfesor;

import androidx.annotation.Nullable;

public class AgrEdiProfesorFormState {
    @Nullable
    private Integer usernameError;
    @Nullable
    private Integer cedulaError;
    @Nullable
    private Integer phoneError;
    @Nullable
    private Integer emailError;
    private boolean isDataValid;

    public AgrEdiProfesorFormState(@Nullable Integer usernameError, @Nullable Integer cedulaError, @Nullable Integer phoneError, @Nullable Integer emailError) {
        this.usernameError = usernameError;
        this.cedulaError = cedulaError;
        this.phoneError = phoneError;
        this.emailError = emailError;
        this.isDataValid = false;
    }

    AgrEdiProfesorFormState(boolean isDataValid) {
        this.usernameError = null;
        this.cedulaError = null;
        this.phoneError = null;
        this.emailError = null;
        this.isDataValid = isDataValid;
    }

    @Nullable
    Integer getUsernameError() {
        return usernameError;
    }

    @Nullable
    public Integer getCedulaError() {
        return cedulaError;
    }

    @Nullable
    public Integer getPhoneError() {
        return phoneError;
    }

    @Nullable
    public Integer getEmailError() {
        return emailError;
    }

    boolean isDataValid() {
        return isDataValid;
    }
}
