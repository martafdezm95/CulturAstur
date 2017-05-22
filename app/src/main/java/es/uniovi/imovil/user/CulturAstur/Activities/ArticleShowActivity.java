package es.uniovi.imovil.user.CulturAstur.Activities;

/**
 * Created by marta on 20/04/2017.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.util.List;

import es.uniovi.imovil.user.CulturAstur.Fragments.ArticleListFragment;
import es.uniovi.imovil.user.CulturAstur.Models.Article;
import es.uniovi.imovil.user.CulturAstur.R;

public class ArticleShowActivity extends Activity {
    public static String jsonFavs = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_article);

        Intent intent = getIntent();
        final int pos = (int) intent.getSerializableExtra("myArticle");
        Article article = ArticleListFragment.articlesModelList.get(pos);
        TextView textView = (TextView) findViewById(R.id.nameArticleShow);
        textView.setText(article.getName());
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        try {
            int posArt = ArticleListFragment.articlesModelList.get(pos).getModelPos();
            int posFav = ArticleListFragment.articlesModelListFinal.get(posArt).isFav();
            if ( posFav != -1) {
                fab.setImageResource(android.R.drawable.star_big_on);
            }

        }catch(RuntimeException e){
            int posArt = ArticleListFragment.articlesFavouriteList.get(pos).getModelPos();
            int posFav = ArticleListFragment.articlesModelListFinal.get(posArt).isFav();
            if ( posFav != -1) {
                fab.setImageResource(android.R.drawable.star_big_on);
            }
        }
        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    if (!MainActivity.favourite) {
                        // Position of the object at FinalList
                        int posArt = ArticleListFragment.articlesModelList.get(pos).getModelPos();
                        //Position at FavouriteList
                        int posFav = ArticleListFragment.articlesModelListFinal.get(posArt).isFav();
                        //If =! -1, remove from favourite
                        if (posFav != -1) {
                            fab.setImageResource(android.R.drawable.star_big_off); //Replace yellow star for grey star

                            //Remove from favouriteList
                            ArticleListFragment.articlesFavouriteList.remove(ArticleListFragment.articlesModelList.get(pos).isFav());

                            //Restart position at Favourite in both lists
                            ArticleListFragment.articlesModelList.get(pos).setFav(-1);
                            ArticleListFragment.articlesModelListFinal.get(posArt).setFav(-1);

                            //Save favourite list
                            List<Article> articleParamList = ArticleListFragment.articlesFavouriteList;
                            String articleParamJSONList = new Gson().toJson(articleParamList);
                            SharedPreferences prefs = getSharedPreferences("favs", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putString("favs", articleParamJSONList);

                            editor.apply();
                        } else {
                            fab.setImageResource(android.R.drawable.star_big_on);  //Replace gray star for yellow star

                            //Add article at favourite list
                            ArticleListFragment.articlesFavouriteList.add(ArticleListFragment.articlesModelList.get(pos));

                            //Save favourite list
                            List<Article> articleParamList = ArticleListFragment.articlesFavouriteList;
                            String articleParamJSONList = new Gson().toJson(articleParamList);
                            SharedPreferences prefs = getSharedPreferences("favs", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putString("favs", articleParamJSONList);
                            editor.apply();

                            //Set position at favourite list
                            ArticleListFragment.articlesModelList.get(pos).setFav(ArticleListFragment.articlesFavouriteList.size() - 1);
                            ArticleListFragment.articlesModelListFinal.get(posArt).setFav(ArticleListFragment.articlesFavouriteList.size() - 1);

                        }
                    } else {
                        int posArt = ArticleListFragment.articlesFavouriteList.get(pos).getModelPos();
                        int posFav = ArticleListFragment.articlesModelListFinal.get(posArt).isFav();
                        if (posFav != -1) {
                            fab.setImageResource(android.R.drawable.star_big_off);
                            ArticleListFragment.articlesFavouriteList.get(pos).setFav(-1);
                            ArticleListFragment.articlesFavouriteList.remove(pos);
                            ArticleListFragment.articlesModelListFinal.get(posArt).setFav(-1);
                            ArticleListFragment.articlesModelList.get(pos).setFav(-1);
                            ArticleListFragment.articlesModelList.remove(pos);
                        }
                        List<Article> articleParamList = ArticleListFragment.articlesFavouriteList;
                        String articleParamJSONList = new Gson().toJson(articleParamList);
                        SharedPreferences prefs = getSharedPreferences("favs", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("favs", articleParamJSONList);

                        editor.apply();
                        refreshFavPos();
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("myArticle", ArticleListFragment.articlesFavouriteList);
                        setResult(Activity.RESULT_OK, resultIntent);
                        finish();
                    }
                }catch(ArrayIndexOutOfBoundsException e) {
                    int posArt = ArticleListFragment.articlesFavouriteList.get(pos).getModelPos();
                    int posFav = ArticleListFragment.articlesModelListFinal.get(posArt).isFav();
                    if ( posFav != -1) {
                        fab.setImageResource(android.R.drawable.star_big_off);
                        ArticleListFragment.articlesFavouriteList.get(pos).setFav(-1);
                        ArticleListFragment.articlesFavouriteList.remove(pos);
                        ArticleListFragment.articlesModelListFinal.get(posArt).setFav(-1);
                        ArticleListFragment.articlesModelList.get(pos).setFav(-1);
                        ArticleListFragment.articlesModelList.remove(pos);
                        }
                        List<Article> articleParamList = ArticleListFragment.articlesFavouriteList;
                        String articleParamJSONList = new Gson().toJson(articleParamList);
                        SharedPreferences prefs = getSharedPreferences("favs", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("favs", articleParamJSONList);

                        editor.apply();
                    }
                }
        });

        ImageView thumbNail = (ImageView) findViewById(R.id.networkImage);
        if(article.getURL()!=""){
           Glide.with(getApplicationContext()).load(article.getURL()).fitCenter()
                    .centerCrop()
                    .into(thumbNail);
            /*.resize(Resources.getSystem().getDisplayMetrics().widthPixels*2+Resources.getSystem().getDisplayMetrics().widthPixels,
                            Resources.getSystem().getDisplayMetrics().heightPixels)*/
        }
        else{
            thumbNail.setImageResource(R.drawable.museum);
        }
        try{
        TextView tvName = (TextView) findViewById(R.id.nameArticleShow);
        tvName.setText(article.getName());
        }catch(NullPointerException e){}
        try{
        TextView zone = (TextView) findViewById(R.id.textZone);
        zone.setText(article.getCouncil()+ " (" +
                article.getZone() + ")");
        }catch(NullPointerException e){}
        try{
        TextView address = (TextView) findViewById(R.id.textAddress);
        address.setText(article.getDirection());
        }catch(NullPointerException e){}
        int i=0; boolean found=false;
        TextView info = (TextView) findViewById(R.id.textInfo);
        info.setText("");
        String text ="";
        while(i<article.getInfo().size() || !found){
            try{
                text = article.getInfo().get(i).replaceAll("<p>", "");
                text = text.replaceAll("</p>", "");
                text = text.replaceAll(";", "");
                text = text.replaceAll("\t", "");
                text = text.replaceAll("<p style="+ "\"text-align: justify\">", "");
                text = text.replaceAll("<strong>", "");
                text = text.replaceAll("</strong>", "");
                text = text.replaceAll("<br>", "");
                text = text.replaceAll("<br />", "");
                text = text.replaceAll("<b>", "");
                text = text.replaceAll("</b>", "");
                text = text.replaceAll("<li>", "");
                text = text.replaceAll("</li>", "");
                text = text.replaceAll("<div>", "");
                text = text.replaceAll("</div>", "");
                text = text.replaceAll("<em>", "");
                text = text.replaceAll("</em>", "");
                text = text.replaceAll("<ul>", "");
                text = text.replaceAll("</ul>", "");
                text = text.replaceAll("&nbsp", "");
                info.setText(info.getText() + text);
                found=true;
            }catch(NullPointerException e){}
            i++;
        }
        try{
        TextView telephone = (TextView) findViewById(R.id.textTelephone);
        telephone.setText(article.getTelephone());
        }catch(NullPointerException e){}
        try{
            TextView email = (TextView) findViewById(R.id.textEmail);
            email.setText(article.getEmail());
        }catch(NullPointerException e){}
        try{
            TextView website = (TextView) findViewById(R.id.textWebsite);
            website.setText(article.getWeb());
        }catch(NullPointerException e){}

        //thumbNail.setImageResource(R.drawable.ic_action_add_course);
        // ViewGroup layout = (ViewGroup) findViewById(R.id.activity_display_message);
        // layout.addView(textView);
    }
    @Override
    public void onBackPressed()
    {
        refreshFavPos();
        Intent resultIntent = new Intent();
        resultIntent.putExtra("myArticle", ArticleListFragment.articlesFavouriteList);
        setResult(Activity.RESULT_OK, resultIntent);
       // ArticleListFragment.articlesModelList=ArticleListFragment.articlesModelListFinal;
        finish();
    }
    public void refreshFavPos(){
        for(int i=0; i<ArticleListFragment.articlesFavouriteList.size(); i++){
            ArticleListFragment.articlesFavouriteList.get(i).setFav(i);
        }
    }
}
