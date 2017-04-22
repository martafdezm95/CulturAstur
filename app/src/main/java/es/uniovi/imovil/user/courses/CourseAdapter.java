package es.uniovi.imovil.user.courses;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import es.uniovi.imovil.user.courses.Models.Article;

public class CourseAdapter extends BaseAdapter {
	
	static class ViewHolder {
		public TextView mCourseName;
		public TextView mTeacherName;
	}


	private List<Article> articleInfo;
	public LayoutInflater mInflater;

	
	public CourseAdapter(Context context, List<Article> articleInfo) {

		if (context == null || articleInfo == null ) {
			throw new IllegalArgumentException();
		}
			
		this.articleInfo = articleInfo;
		this.mInflater = LayoutInflater.from(context);
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
		
		View rowView = convertView;
		ViewHolder viewHolder;
		if (rowView == null) {
			rowView = mInflater.inflate(R.layout.list_item_course2, parent, false);
			viewHolder = new ViewHolder();
			viewHolder.mCourseName = (TextView) rowView.findViewById(R.id.nameTextView);
			viewHolder.mTeacherName = (TextView) rowView.findViewById(R.id.teacherTextView);
			rowView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) rowView.getTag();
		}

		Article m = articleInfo.get(position);
		viewHolder.mCourseName.setText(m.getDynamicElement().get(0).getDynamicContent().getContent());
		viewHolder.mTeacherName.setText("Concejo: " + m.getDynamicElement().get(1).getDynamicContent().getContent());
		
		return rowView;
	}
	
	public void addCourse(Article article) {
		
		if (article == null) {
			throw new IllegalArgumentException();			
		}
		
		articleInfo.add(article);
		
		// Importante: notificar que ha cambiado el dataset
		notifyDataSetChanged();
	}
}
