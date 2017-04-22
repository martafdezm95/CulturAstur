package es.uniovi.imovil.user.courses.Activities;

/**
 * Created by marta on 20/04/2017.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import es.uniovi.imovil.user.courses.Models.Article;
import es.uniovi.imovil.user.courses.R;


/**
 * Created by marta on 19/04/2017.
 */

public class ArticleShowActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_article);

        Intent intent = getIntent();
        Article myArticleInfo = (Article) intent.getSerializableExtra("myArticle");
        TextView textView = (TextView) findViewById(R.id.nameArticleShow);
        textView.setText(myArticleInfo.getDynamicElement().get(0).getDynamicContent().getContent());

        // ViewGroup layout = (ViewGroup) findViewById(R.id.activity_display_message);
        // layout.addView(textView);
    }
}
