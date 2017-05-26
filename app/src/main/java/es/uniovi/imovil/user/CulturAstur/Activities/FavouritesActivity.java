package es.uniovi.imovil.user.CulturAstur.Activities;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import es.uniovi.imovil.user.CulturAstur.Fragments.ArticleListFragment;
import es.uniovi.imovil.user.CulturAstur.Fragments.ArticleShowFragment;
import es.uniovi.imovil.user.CulturAstur.Models.Article;
import es.uniovi.imovil.user.CulturAstur.R;

public class FavouritesActivity extends AppCompatActivity implements ArticleListFragment.Callbacks{

    private boolean favourite = false;
    public static boolean mTwoPanes = false;
    private DrawerLayout mDrawerLayout = null;
    private NavigationView navigation  = null;
    private ActionBarDrawerToggle mDrawerToggle = null;

    static final int PICK_CONTACT_REQUEST = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.article_list_single_pane);

        initInstancesDrawer();


        if (findViewById(R.id.article_details_container) != null) {
            mTwoPanes = true;
        }
        //Se guardarán las preferencias parseando la lista de articles en un string
        SharedPreferences prefs = getSharedPreferences("favs", Context.MODE_PRIVATE);
        String articleParamJSONList = prefs.getString("favs", "");

        List<Article> articleParamList = new Gson().fromJson(articleParamJSONList, new TypeToken<List<Article>>() {
        }.getType());
        if(articleParamList==null) articleParamList = new ArrayList<Article>();

        //Motramos los elementos guardados en favoritos
        FragmentManager fragmentManager = getSupportFragmentManager();
        ArticleListFragment fragment = (ArticleListFragment) fragmentManager.findFragmentById(R.id.course_list_frag);
        fragment.setArticleList(articleParamList);
        favourite = true;
        Intent resultIntent = new Intent();
        setResult(Activity.RESULT_OK, resultIntent);

    }

    private void initInstancesDrawer() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(R.string.toolbar_title_fav);
        mToolbar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.toolbarFavouriteColor)));
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_design_support_layout);
        mDrawerToggle = new ActionBarDrawerToggle(
                this,  mDrawerLayout, mToolbar,
                R.string.app_name, R.string.app_name
        );
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        mDrawerToggle.syncState();
        navigation = (NavigationView) findViewById(R.id.navigation_drawer);
        navigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                int id = menuItem.getItemId();
                switch (id) {
                    case R.id.navigation_drawer_home:
                        //Volver a home
                        Intent resultIntent = new Intent();
                        ArrayList<Article> list = (ArrayList<Article>)ArticleListFragment.articlesModelListFinal;
                        resultIntent.putExtra("myArticle", list);
                        setResult(Activity.RESULT_OK, resultIntent);
                        MainActivity.favourite = false;
                        finish();
                       // ArticleListFragment.articlesModelList=ArticleListFragment.articlesModelListFinal;
                        break;
                    case R.id.navigation_drawer_maps:
                        //Ir a los mapas
                        resultIntent = new Intent();
                        list = (ArrayList<Article>)ArticleListFragment.articlesModelListFinal;
                        resultIntent.putExtra("myArticle", list);
                        setResult(3, resultIntent);
                        MainActivity.favourite = false;
                        finish();
                        break;
                    case R.id.navigation_favourite:
                        MainActivity.favourite = true;
                        Toast.makeText(getApplicationContext(),R.string.toast_already_fav,Toast.LENGTH_LONG).show();
                        break;
                }
                return false;
            }
        });
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle .syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle .onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search_toolbar:
                //permitimos filtrar la lista de favoritos
                Intent i = new Intent(FavouritesActivity.this, FilterActivity.class);
                i.putExtra("parentId", 1);
                startActivityForResult(i, PICK_CONTACT_REQUEST);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public void onCourseSelected(int posicion) {
        //Mostramos la información del objeto elegido
            Intent i = new Intent(FavouritesActivity.this, ArticleShowActivity.class);
            i.putExtra("myArticle", posicion);
            startActivityForResult(i, PICK_CONTACT_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_CONTACT_REQUEST && resultCode == RESULT_OK && data != null) {
            ArrayList<Article> myList = (ArrayList<Article>) data.getSerializableExtra("myArticle");
            FragmentManager fragmentManager = getSupportFragmentManager();
            ArticleListFragment fragment = (ArticleListFragment) fragmentManager.findFragmentById(R.id.course_list_frag);
            fragment.setArticleList(myList);
        }
    }
    @Override
    public void onBackPressed()
    {
        //Preparar la lista a mostrar en mainactivity
        Intent resultIntent = new Intent();
        ArrayList<Article> list = (ArrayList<Article>)ArticleListFragment.articlesModelListFinal;
        resultIntent.putExtra("myArticle", list);
        setResult(Activity.RESULT_OK, resultIntent);
        MainActivity.favourite = false;
        finish();
    }
}
