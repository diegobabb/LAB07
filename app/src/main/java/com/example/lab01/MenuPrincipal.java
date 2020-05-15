package com.example.lab01;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Menu;
import com.example.lab01.ui.cursos.CursoFragment;
import com.example.lab01.ui.login.LoginActivity;
import com.google.android.material.navigation.NavigationView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MenuPrincipal extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.nav_profesores, R.id.nav_cursos, R.id.nav_logout)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        navigationView.getMenu().findItem(R.id.nav_logout).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(MenuPrincipal.this, LoginActivity.class);
                startActivity(intent);
                return true;
            }
        });
        String accion = (String) getIntent().getSerializableExtra("vamos_a_cursos");
        getIntent().removeExtra("vamos_a_cursos");
        if (accion != null) {
            toolbar.setTitle("Cursos"); // Titulo en el header
            navigationView.getMenu().getItem(1).setChecked(true); // Opcion focuseada en el menu
            moveToFragment(new CursoFragment()); // Contenido
        }
    }

    //Invocar otros fragment
    private void moveToFragment(Fragment fragment) {
        if (fragment instanceof CursoFragment) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_profesores, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();
        } else {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_cursos, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}

