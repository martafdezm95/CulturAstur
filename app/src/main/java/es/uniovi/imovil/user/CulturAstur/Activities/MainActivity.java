package es.uniovi.imovil.user.CulturAstur.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
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
	public static int pos = 0;
	private static boolean RUN_ONCE = true;

	static final int PICK_CONTACT_REQUEST = 0;

	//Bloqueamos el cambio de orientación al arrancar la aplicación para que no se llame a ondestroy y oncreate
	//No se ha desactivado el llamar a ondestroy para poder llamar a distintos layuts dependiendo de la orientación
	public void runOnce() {
		if (RUN_ONCE) {
			RUN_ONCE = false;
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
		}
		else{
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
		}
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		runOnce(); //No permitimos que se reinicie el proceso de descarga de datos y filtrado del json
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		initInstancesDrawer(); // Inicializamos y ponemos a punto el drawer y la toolbar

		if (findViewById(R.id.article_details_container) != null) {
			mTwoPanes = true; // true si se ha cargado el layout para dispositipos tipo tablet
		}
	}
	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);

		// Ya que se llama a onDestoy para permitir cambios de layouts debemos cargar qué elemento se estaba
		//mostrando anteriormente para volver a amostrar dicho elemento tras llamar a ondestroy
		if (mTwoPanes) {
			FragmentManager fragmentManager = getSupportFragmentManager();
			ArticleShowFragment fragment = (ArticleShowFragment) fragmentManager.findFragmentById(R.id.course_details_frag);
			fragment.setArticle(pos);
		}
	}
//Inicializamos la toolbar y el drawer
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
			//Según el item seleccionado se realiza una acción distinta
			switch (id) {
                case R.id.navigation_drawer_home:
                	Toast.makeText(getApplicationContext(),R.string.main_already_home,Toast.LENGTH_LONG).show();
					break;
				case R.id.navigation_drawer_maps:
					//Lanzamos MapsActivity que utilizará un layout distinto según el tipo de dispositivo
					//y según la orientación
					Intent i = new Intent(MainActivity.this, MapsActivity.class);
					startActivityForResult(i, PICK_CONTACT_REQUEST);
					break;
				case R.id.navigation_favourite:
					//Lanzamos actividad que muestra las preferencias del usuario
					i = new Intent(MainActivity.this, FavouritesActivity.class);
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
		//Se muestran los datos que ya han sido filtrados, para evitar tener que pasar por el proceso de filtrado y
		//de descarga de nuevo
		FragmentManager fragmentManager = getSupportFragmentManager();
		ArticleListFragment fragment = (ArticleListFragment) fragmentManager.findFragmentById(R.id.course_list_frag);
		fragment.setArticleList(ArticleListFragment.articlesModelListFinal);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate el menu
		// añadimos items al actionbar
        getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
			case R.id.search_toolbar:
				//Lanzamos la actividad diseñada para el filtro.
				//Se ha optado por implementar un filtro determina con objeto de poder buscar elementos según
				//localidad y nombre independientemente ya que son dos atributos que no tienen relación
				//Además se puede ordenar por proximidad
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
			//Si es un dispositivo tipo tablet se cargará también el fragmento que muestra detalles sobre los elementos
			// de la lista
			FragmentManager fragmentManager = getSupportFragmentManager();
			ArticleShowFragment fragment = (ArticleShowFragment) fragmentManager.findFragmentById(R.id.course_details_frag);
			fragment.setArticle(posicion);
			pos = posicion;
		} else {
			//Si no es una tablet se lanza una actividad en la que se muestra dicha información
			Intent intent = new Intent(this, ArticleShowActivity.class);
			intent.putExtra("myArticle", posicion);
			startActivity(intent);
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		//Si se cierra una actividad que requiere actualizar la lista de elementos
		if (requestCode == PICK_CONTACT_REQUEST && resultCode == RESULT_OK && data != null) {

			//Obtenemos la lista modificada obtenida por medio de un intent de la actividad que finalizó
			ArrayList<Article> myList = (ArrayList<Article>) data.getSerializableExtra("myArticle");
			FragmentManager fragmentManager1 = getSupportFragmentManager();
			ArticleListFragment fragment1 = (ArticleListFragment) fragmentManager1.findFragmentById(R.id.course_list_frag);
			fragment1.setArticleList(myList);

            if(MainActivity.mTwoPanes){
				//CArgamos información sobre elemento 0 de la lista
                FragmentManager fragmentManager2 = getSupportFragmentManager();
                ArticleShowFragment fragment2 = (ArticleShowFragment) fragmentManager2.findFragmentById(R.id.course_details_frag);
                fragment2.setArticle(0);
				pos = 0;
            }
		}
		if (resultCode == 2) {
			//Ir desde mapas a favoritos
			Intent i = new Intent(MainActivity.this, FavouritesActivity.class);
			startActivityForResult(i, PICK_CONTACT_REQUEST);
			favourite = true;
		}
		if (requestCode == PICK_CONTACT_REQUEST && resultCode == 3 && data != null) {
			//Ir desde favoritos a mapas
			Intent i = new Intent(MainActivity.this, MapsActivity.class);
			startActivityForResult(i, PICK_CONTACT_REQUEST);
		}
	}
}
