package es.uniovi.imovil.user.courses.Fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import es.uniovi.imovil.user.courses.R;
import es.uniovi.imovil.user.courses.Volley.VolleySingleton;

/**
 * Created by marta on 19/04/2017.
 */

public class ArticleShowFragment extends Fragment {
    private static final String ARTICLE_NAME = "Name";

    ImageView ivImageView;
    RequestQueue mRequestQueue;
    public static ArticleShowFragment newInstance(String name) {

        ArticleShowFragment fragment = new ArticleShowFragment();

        Bundle args = new Bundle();
        args.putString(ARTICLE_NAME, name);
        fragment.setArguments(args);

        return fragment;
    }

    public ArticleShowFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView;
        rootView = inflater.inflate(R.layout.article_info_fragment, container, false);

        // Si estamos restaurando desde un estado previo no hacemos nada
        if (savedInstanceState != null) {
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
        ivImageView = (ImageView)rootView.findViewById(R.id.ivImageView);
        VolleySingleton.getInstance().getImageLoader().get("http://www.turismoasturias.es"+m.getDynamicElement().get(i-1).getDynamicContent().getContent(), ImgController.getInstance().getImageLoader(), new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer imageContainer, boolean b) {
                Bitmap bitmap = imageContainer.getBitmap();
                //use bitmap
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        NetworkImageView nv = (NetworkImageView) rootView.findViewById(R.id.photo);
        nv.setDefaultImageResId(R.drawable.ic_launcher); // image for loading...
        nv.setImageUrl("http://www.turismoasturias.es"+m.getDynamicElement().get(i-1).getDynamicContent().getContent(), ImgController.getInstance().getImageLoader()); //ImgController from your code.
        return rootView;
    }

    public void setName(String name) {

        TextView tvName = (TextView) getView().findViewById(R.id.nameArticleShow);
        tvName.setText(name);
    }
}
