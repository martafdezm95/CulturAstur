package es.uniovi.imovil.user.CulturAstur.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.util.List;

import es.uniovi.imovil.user.CulturAstur.Models.Article;
import es.uniovi.imovil.user.CulturAstur.R;


/**
 * Created by marta on 19/04/2017.
 */

public class ArticleShowFragment extends Fragment {
    private static Article articleFragment = new Article();
    private static final String ARTICLE_NAME = "Name";
    private RequestQueue mRequestQueue;
    private int pos ;
    View rootView;


    public static ArticleShowFragment newInstance() {

        ArticleShowFragment fragment = new ArticleShowFragment();

        Bundle args = new Bundle();
        args.putSerializable("article", articleFragment);
        fragment.setArguments(args);

        return fragment;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_show_article, container, false);

        // Si estamos restaurando desde un estado previo no hacemos nada
        if (savedInstanceState != null) {
            return rootView;
        }
        if(rootView!=null){
            if((ViewGroup)rootView.getParent()!=null)
                ((ViewGroup)rootView.getParent()).removeView(rootView);
            return rootView;
        }

        Bundle args = getArguments();
        TextView tvName = (TextView) rootView.findViewById(R.id.nameArticleShow);
        if (args != null) {
            String description = args.getString(ARTICLE_NAME);
            tvName.setText(description);
        } else {
            tvName.setText(null);
        }

          return rootView;
    }

    public void setArticle(int position) {
        this.pos = position;
        Article article = ArticleListFragment.articlesModelList.get(position);
        this.articleFragment = article;
        ImageView thumbNail = (ImageView) getView()
                .findViewById(R.id.networkImage);
        final FloatingActionButton fab = (FloatingActionButton) getView().findViewById(R.id.floatingActionButton);
        fab.setImageResource(R.drawable.empty_like);
        try {
            int posArt = ArticleListFragment.articlesModelList.get(pos).getModelPos();
            int posFav = ArticleListFragment.articlesModelListFinal.get(posArt).isFav();
            if ( posFav != -1) {
                fab.setImageResource(R.drawable.filled_like);
            }

        }catch(RuntimeException e){
            int posArt = ArticleListFragment.articlesFavouriteList.get(pos).getModelPos();
            int posFav = ArticleListFragment.articlesModelListFinal.get(posArt).isFav();
            if ( posFav != -1) {
                fab.setImageResource(R.drawable.filled_like);
            }
        }
        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    //if (!MainActivity.favourite) {
                    // Position of the object at FinalList
                    int posArt = ArticleListFragment.articlesModelList.get(pos).getModelPos();
                    //Position at FavouriteList
                    int posFav = ArticleListFragment.articlesModelListFinal.get(posArt).isFav();
                    //If =! -1, remove from favourite
                    if (posFav != -1) {
                        fab.setImageResource(R.drawable.empty_like); //Replace yellow star for grey star

                        //Remove from favouriteList
                        ArticleListFragment.articlesFavouriteList.remove(ArticleListFragment.articlesModelList.get(pos).isFav());

                        //Restart position at Favourite in both lists
                        ArticleListFragment.articlesModelList.get(pos).setFav(-1);
                        ArticleListFragment.articlesModelListFinal.get(posArt).setFav(-1);

                        //Save favourite list
                        List<Article> articleParamList = ArticleListFragment.articlesFavouriteList;
                        String articleParamJSONList = new Gson().toJson(articleParamList);
                        SharedPreferences prefs = getActivity().getSharedPreferences("favs", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("favs", articleParamJSONList);

                        editor.apply();
                    } else {
                        fab.setImageResource(R.drawable.filled_like);  //Replace gray star for yellow star

                        //Add article at favourite list
                        ArticleListFragment.articlesFavouriteList.add(ArticleListFragment.articlesModelList.get(pos));

                        //Save favourite list
                        List<Article> articleParamList = ArticleListFragment.articlesFavouriteList;
                        String articleParamJSONList = new Gson().toJson(articleParamList);
                        SharedPreferences prefs = getActivity().getSharedPreferences("favs", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("favs", articleParamJSONList);
                        editor.apply();

                        //Set position at favourite list
                        ArticleListFragment.articlesModelList.get(pos).setFav(ArticleListFragment.articlesFavouriteList.size() - 1);
                        ArticleListFragment.articlesModelListFinal.get(posArt).setFav(ArticleListFragment.articlesFavouriteList.size() - 1);
                    }
                   /* } else {
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
                    }*/
                }catch(ArrayIndexOutOfBoundsException e) {
                    int posArt = ArticleListFragment.articlesFavouriteList.get(pos).getModelPos();
                    int posFav = ArticleListFragment.articlesModelListFinal.get(posArt).isFav();
                    if ( posFav != -1) {
                        fab.setImageResource(R.drawable.empty_like);
                        ArticleListFragment.articlesFavouriteList.get(pos).setFav(-1);
                        ArticleListFragment.articlesFavouriteList.remove(pos);
                        ArticleListFragment.articlesModelListFinal.get(posArt).setFav(-1);
                        ArticleListFragment.articlesModelList.get(pos).setFav(-1);
                        ArticleListFragment.articlesModelList.remove(pos);
                    }
                    List<Article> articleParamList = ArticleListFragment.articlesFavouriteList;
                    String articleParamJSONList = new Gson().toJson(articleParamList);
                    SharedPreferences prefs = getActivity().getSharedPreferences("favs", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("favs", articleParamJSONList);

                    editor.apply();
                }
            }
        });
        if(article.getURL()!=""){

            Glide.with(getActivity().getApplicationContext()).load(article.getURL()).fitCenter()
                    .centerCrop()
                    .into(thumbNail);
        }
        else{
            thumbNail.setImageResource(R.drawable.museum);
        }
        try{
        TextView tvName = (TextView) getView().findViewById(R.id.nameArticleShow);
        tvName.setText(article.getName());
        }catch(NullPointerException e){}
        try{
        TextView zone = (TextView) getView().findViewById(R.id.textZone);
        zone.setText(article.getCouncil() + " (" +
                article.getZone() + ")");
        }catch(NullPointerException e){}
        try{
        TextView address = (TextView) getView().findViewById(R.id.textAddress);
        address.setText(article.getDirection());
        }catch(NullPointerException e){}
        int i=0; boolean found=false;
        TextView info = (TextView) getView().findViewById(R.id.textInfo);
        info.setText("");
        String text ="";
        while(i<article.getInfo().size() || !found){
            try{
                    text = article.getInfo().get(i).replaceAll("<p>", "");
                    text = text.replaceAll("</p>", "");
                    text = text.replaceAll(";", "");
                    text = text.replaceAll("  ", "");
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
        TextView telephone = (TextView) getView().findViewById(R.id.textTelephone);
        telephone.setText(article.getTelephone());
        }catch(NullPointerException e){}
        try{
            TextView email = (TextView) getView().findViewById(R.id.textEmail);
            email.setText(article.getEmail());
        }catch(NullPointerException e){}
        try{
            TextView website = (TextView) getView().findViewById(R.id.textWebsite);
            website.setText(article.getWeb());
        }catch(NullPointerException e){}

}
   /* @Override
    public void onBackPressed()
    {
        refreshFavPos();
        Intent resultIntent = new Intent();
        resultIntent.putExtra("myArticle", ArticleListFragment.articlesFavouriteList);
        getActivity().setResult(Activity.RESULT_OK, resultIntent);
        ArticleListFragment.articlesModelList=ArticleListFragment.articlesModelListFinal;
        getActivity().finish();
    }*/
    public Article getObject(Article art){
        Article result =
                ArticleListFragment.articlesModelListFinal.get(
                        ArticleListFragment.articlesModelListFinal.indexOf(art)
                );
        return result;
    }
    public void refreshFavPos(){
        for(int i=0; i<ArticleListFragment.articlesFavouriteList.size(); i++){
            ArticleListFragment.articlesFavouriteList.get(i).setFav(i);
        }
    }
}
