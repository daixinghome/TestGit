package com.stohelper.view;
import com.stohelper.util.BaseActivity;
import com.stohelper.view.R;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

public class StartActivity extends BaseActivity {

	private ViewPager viewPager;
	private ArrayList<View> pageViews;
	private ImageView imageView;
	private ImageView[] imageViews;
	// ��������ͼƬLinearLayout
	private ViewGroup main;
	// ����СԲ���LinearLayout
	private ViewGroup group;
	@SuppressWarnings("unused")
	private TextView content;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// �����ޱ��ⴰ��
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		int[] img = new int[] { R.drawable.no1, R.drawable.no2, R.drawable.no1,
				R.drawable.no2, R.drawable.no1 };
		LayoutInflater inflater = getLayoutInflater();
		pageViews = new ArrayList<View>();
		for (int i = 0; i < img.length; i++) {
			LinearLayout layout = new LinearLayout(this);
			LayoutParams ltp = new LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT);
			final ImageView imageView = new ImageView(this);
			imageView.setScaleType(ScaleType.CENTER_INSIDE);
			imageView.setImageResource(img[i]);
			imageView.setPadding(15, 30, 15, 30);
			layout.addView(imageView, ltp);
			pageViews.add(layout);
		}
		imageViews = new ImageView[pageViews.size()];
		main = (ViewGroup) inflater.inflate(R.layout.activity_start, null);
		group = (ViewGroup) main.findViewById(R.id.viewGroup);
		viewPager = (ViewPager) main.findViewById(R.id.guidePages);
		content = (TextView)main. findViewById(R.id.photo_content);
		
		/**
		 * �м���ͼƬ �������ʾ����СԲ��
		 */

		for (int i = 0; i < pageViews.size(); i++) {
			LinearLayout.LayoutParams margin = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			//����ÿ��СԲ�������ߵļ��
			//margin.setMargins(10, 0, 0, 0);
			margin.setMargins(10, 0, 0, 150);
			imageView = new ImageView(StartActivity.this);
			//����ÿ��СԲ��Ŀ��
			imageView.setLayoutParams(new LayoutParams(15, 15));
			imageViews[i] = imageView;
			if (i == 0) {
				// Ĭ��ѡ�е�һ��ͼƬ
				imageViews[i].setBackgroundResource(R.drawable.page_indicator_focused);
			} else {
				//����ͼƬ������δѡ��״̬
				imageViews[i].setBackgroundResource(R.drawable.page_indicator_unfocused);
			}
			group.addView(imageViews[i], margin);
		}

		setContentView(main);
		//��viewpager����������
		viewPager.setAdapter(new GuidePageAdapter());
		//��viewpager���ü����¼�
		viewPager.setOnPageChangeListener(new GuidePageChangeListener());
		
		
		
		
			
	}

	// ָ��ҳ������������
	class GuidePageAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return pageViews.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public int getItemPosition(Object object) {
			// TODO Auto-generated method stub
			return super.getItemPosition(object);
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			// TODO Auto-generated method stub
			((ViewPager) arg0).removeView(pageViews.get(arg1));
		}

		@Override
		public Object instantiateItem(View arg0, int arg1) {
			// TODO Auto-generated method stub
			((ViewPager) arg0).addView(pageViews.get(arg1));
			return pageViews.get(arg1);
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
			// TODO Auto-generated method stub

		}

		@Override
		public Parcelable saveState() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void startUpdate(View arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void finishUpdate(View arg0) {
			// TODO Auto-generated method stub

		}
	}

	// ָ��ҳ������¼�������
	class GuidePageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPageSelected(int arg0) {
			//���������õ�ǰѡ��ͼƬ�µ�СԲ��������ɫ
			for (int i = 0; i < imageViews.length; i++) {
				imageViews[arg0]
						.setBackgroundResource(R.drawable.page_indicator_focused);

				if (arg0 != i) {
					imageViews[i]
							.setBackgroundResource(R.drawable.page_indicator_unfocused);
				}
			}
		}
	}
	
	public void toLogin(View v){
		Intent intent=new Intent(StartActivity.this,LoginActivity.class);
        startActivity(intent);	
	}
	
	public void toRegister(View v){
		Intent intent=new Intent(StartActivity.this,RegisterActivity.class);
        startActivity(intent);	
	}

	@Override
	public void processMessage(Message message) {
		// TODO Auto-generated method stub
		
	}
}
