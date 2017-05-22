package es.uniovi.imovil.user.CulturAstur;

/**
 * Created by marta on 09/05/2017.
 */

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import java.util.ArrayList;
import java.util.List;

import es.uniovi.imovil.user.CulturAstur.Fragments.ArticleListFragment;

/**
 * Created by arias on 17/02/2016.
 */
public class SimpleLoader extends AsyncTaskLoader<List<String>> {

    ArticleListFragment mainActivity;
    public SimpleLoader(Context context) {
        super(context);
    }

    @Override
    public List<String> loadInBackground() {

        final ArrayList<String> list = new ArrayList<String>();
        int length = mainActivity.articlesModelList.size();
        for (int i = 0; i < length; ++i) {
            list.add(mainActivity.articlesModelList.get(i).getDynamicElement().get(0).getDynamicContent().getContent());
            try {
                Thread.sleep(1); //simulated network delay
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return list;
    }

    private class MainActivity {
    }
}
