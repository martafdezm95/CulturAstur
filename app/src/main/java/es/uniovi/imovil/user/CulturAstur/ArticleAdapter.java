package es.uniovi.imovil.user.CulturAstur;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import es.uniovi.imovil.user.CulturAstur.Models.Article;

public class ArticleAdapter extends BaseAdapter {


	static class ViewHolder {
		public TextView mName;
		public TextView mCouncil;
		public ImageView mImageView;
	}


	private List<Article> articleInfo;
	public LayoutInflater mInflater;
	public Context cont = null;

	
	public ArticleAdapter(Context context, List<Article> articleInfo) {

		if (context == null || articleInfo == null ) {
			throw new IllegalArgumentException();
		}
			
		this.articleInfo = articleInfo;
		this.mInflater = LayoutInflater.from(context);
		cont = context;
	}
		
	@Override
	public int getCount() {

		return articleInfo.size();
	}

	@Override
	public Object getItem(int position) {
		
		return articleInfo.get(position);
	}

	@Override
	public long getItemId(int position) {

		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		//Inflamos la vista del listview
		View rowView = convertView;
		ViewHolder viewHolder;
		if (rowView == null) {
			rowView = mInflater.inflate(R.layout.list_item_article, parent, false);
			viewHolder = new ViewHolder();
			viewHolder.mImageView = (ImageView) rowView.findViewById(R.id.imageView);
			viewHolder.mName = (TextView) rowView.findViewById(R.id.nameTextView);
			viewHolder.mCouncil = (TextView) rowView.findViewById(R.id.teacherTextView);
			rowView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) rowView.getTag();
		}

		Article m = articleInfo.get(position);
		viewHolder.mName.setText(m.getName());

		viewHolder.mCouncil.setText( cont.getResources().getString(R.string.adapter_council)+ m.getCouncil());
		/*if(m.getURL()!=""){
			//Picasso.with(parent.getContext()).load(m.getURL()).resize(60,60).into(viewHolder.mImageView);
		}*/
		
		return rowView;
	}
}
