package es.uniovi.imovil.user.CulturAstur.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
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

import es.uniovi.imovil.user.CulturAstur.Models.Article;
import es.uniovi.imovil.user.CulturAstur.Fragments.ArticleShowFragment;
import es.uniovi.imovil.user.CulturAstur.Fragments.ArticleListFragment;
import es.uniovi.imovil.user.CulturAstur.R;

public class MainActivity extends AppCompatActivity implements ArticleListFragment.Callbacks{

	public static boolean favourite = false;
	public static boolean mTwoPanes = false;
	private DrawerLayout mDrawerLayout = null;
	private NavigationView navigation  = null;
	private ActionBarDrawerToggle mDrawerToggle = null;

	static final int PICK_CONTACT_REQUEST = 0;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		initInstancesDrawer();

		if (findViewById(R.id.article_details_container) != null) {
			mTwoPanes = true;
		}
	}

	private void initInstancesDrawer() {
		Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(mToolbar);
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
                	Toast.makeText(getApplicationContext(),"Ya está en el menú principal",Toast.LENGTH_LONG).show();
					break;
				case R.id.navigation_drawer_maps:
					Intent intent = new Intent(MainActivity.this, MapsActivity.class);
					startActivity(intent);
					break;
				case R.id.navigation_favourite:
					Intent i = new Intent(MainActivity.this, FavouritesActivity.class);
					startActivityForResult(i, PICK_CONTACT_REQUEST);
					favourite = true;
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
		FragmentManager fragmentManager = getSupportFragmentManager();
		ArticleListFragment fragment = (ArticleListFragment) fragmentManager.findFragmentById(R.id.course_list_frag);
		fragment.setArticleList(ArticleListFragment.articlesModelListFinal);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mDrawerToggle .onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
			case R.id.search_toolbar:
				Intent i = new Intent(MainActivity.this, FilterActivity.class);
				i.putExtra("parentId", 1);
				startActivityForResult(i, PICK_CONTACT_REQUEST);
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
	@Override
	public void onCourseSelected(int posicion) {

		if (mTwoPanes) {
			FragmentManager fragmentManager = getSupportFragmentManager();
			ArticleShowFragment fragment = (ArticleShowFragment) fragmentManager.findFragmentById(R.id.course_details_frag);
			fragment.setArticle(posicion);
		} else {
			Intent intent = new Intent(this, ArticleShowActivity.class);
			intent.putExtra("myArticle", posicion);
			startActivity(intent);
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == PICK_CONTACT_REQUEST && resultCode == RESULT_OK && data != null) {
			Intent intent = getIntent();
			ArrayList<Article> myList = (ArrayList<Article>) data.getSerializableExtra("myArticle");
			FragmentManager fragmentManager1 = getSupportFragmentManager();
			ArticleListFragment fragment1 = (ArticleListFragment) fragmentManager1.findFragmentById(R.id.course_list_frag);
			fragment1.setArticleList(myList);
            if(MainActivity.mTwoPanes){
                FragmentManager fragmentManager2 = getSupportFragmentManager();
                ArticleShowFragment fragment2 = (ArticleShowFragment) fragmentManager2.findFragmentById(R.id.course_details_frag);
                fragment2.setArticle(0
				);
            }

		}
	}
}
