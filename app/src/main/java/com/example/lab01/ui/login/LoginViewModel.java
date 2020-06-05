package com.example.lab01.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.os.AsyncTask;
import android.util.Patterns;

import com.example.lab01.Logica.Usuario;
import com.example.lab01.data.LoginRepository;
import com.example.lab01.data.model.LoggedInUser;
import com.example.lab01.R;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginViewModel extends ViewModel {

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    private LoginRepository loginRepository;

    LoginViewModel(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    public void login(String username, String password) {

        class MyAsyncTask extends AsyncTask<String, String, String> {

            private Usuario usuario;
            private String d_url;

            public MyAsyncTask(Usuario usuario) throws UnsupportedEncodingException {
                this.usuario = usuario;
                this.d_url = "http://10.0.2.2:8084/LAB01-WEB/ServletUsuario?cedula=" + usuario.getCedula().trim() + "&clave=" + usuario.getClave().trim();
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected String doInBackground(String... params) {

                String current = "";
                try {
                    URL url;
                    HttpURLConnection urlConnection = null;
                    try {
                        url = new URL(this.d_url);
                        urlConnection = (HttpURLConnection) url.openConnection();

                        urlConnection.setDoOutput(false);
                        urlConnection.setRequestMethod("GET");

                        InputStream in = urlConnection.getInputStream();
                        InputStreamReader isw = new InputStreamReader(in);
                        int data = isw.read();
                        while (data != -1) {
                            current += (char) data;
                            data = isw.read();
                            System.out.print(current);
                        }
                        // return the data to onPostExecute method
                        return current;
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        if (urlConnection != null) {
                            urlConnection.disconnect();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return "Exception: " + e.getMessage();
                }

                return current;
            }

            @Override
            protected void onPostExecute(String s) {

                if (s.trim().equals("1")) {
                    LoggedInUser data = new LoggedInUser(java.util.UUID.randomUUID().toString(), usuario.getCedula());
                    loginResult.setValue(new LoginResult(new LoggedInUserView(data.getDisplayName())));
                } else {
                    loginResult.setValue(new LoginResult(R.string.login_failed));
                }
            }
        }

        Usuario usuario = new Usuario(username, password);
        MyAsyncTask asyncTask = null;
        try {
            asyncTask = new MyAsyncTask(usuario);
        } catch (UnsupportedEncodingException e) {
            loginResult.setValue(new LoginResult(R.string.login_failed));
        }
        asyncTask.execute();
    }

    public void loginDataChanged(String username, String password) {
        if (!isUserNameValid(username)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        if (username.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        } else {
            return !username.trim().isEmpty();
        }
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }
}
