package com.codesw.newsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.*;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.appbar.AppBarLayout;
import android.app.*;
import android.os.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import android.content.*;
import android.content.res.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.media.*;
import android.net.*;
import android.text.*;
import android.text.style.*;
import android.util.*;
import android.webkit.*;
import android.animation.*;
import android.view.animation.*;
import java.util.*;
import java.util.regex.*;
import java.text.*;
import org.json.*;
import java.util.HashMap;
import java.util.ArrayList;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.EditText;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.text.Editable;
import android.text.TextWatcher;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.bumptech.glide.Glide;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.DialogFragment;


public class MainActivity extends  AppCompatActivity  { 
	
	
	private Toolbar _toolbar;
	private AppBarLayout _app_bar;
	private CoordinatorLayout _coordinator;
	private String result = "";
	private HashMap<String, Object> map = new HashMap<>();
	private String articleString = "";
	private HashMap<String, Object> article_map = new HashMap<>();
	private String api_key = "";
	private boolean isSearching = false;
	
	private ArrayList<HashMap<String, Object>> news_result = new ArrayList<>();
	private ArrayList<String> country_list = new ArrayList<>();
	private ArrayList<String> category_list = new ArrayList<>();
	
	private LinearLayout searchbar;
	private ListView listview1;
	private ImageView imageview3;
	private EditText edittext2;
	private ImageView imageview4;
	
	private RequestNetwork req;
	private RequestNetwork.RequestListener _req_request_listener;
	private Intent i = new Intent();
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.main);
		initialize(_savedInstanceState);
		initializeLogic();
	}
	
	private void initialize(Bundle _savedInstanceState) {
		
		_app_bar = (AppBarLayout) findViewById(R.id._app_bar);
		_coordinator = (CoordinatorLayout) findViewById(R.id._coordinator);
		_toolbar = (Toolbar) findViewById(R.id._toolbar);
		setSupportActionBar(_toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _v) {
				onBackPressed();
			}
		});
		searchbar = (LinearLayout) findViewById(R.id.searchbar);
		listview1 = (ListView) findViewById(R.id.listview1);
		imageview3 = (ImageView) findViewById(R.id.imageview3);
		edittext2 = (EditText) findViewById(R.id.edittext2);
		imageview4 = (ImageView) findViewById(R.id.imageview4);
		req = new RequestNetwork(this);
		
		imageview3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				_Toolbar(true);
				searchbar.setVisibility(View.GONE);
			}
		});
		
		edittext2.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence _param1, int _param2, int _param3, int _param4) {
				final String _charSeq = _param1.toString();
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence _param1, int _param2, int _param3, int _param4) {
				
			}
			
			@Override
			public void afterTextChanged(Editable _param1) {
				
			}
		});
		
		imageview4.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				if (edittext2.getText().toString().length() > 0) {
					api_key = "fd8685453e874328b5d69cca2763df68";
					req.startRequestNetwork(RequestNetworkController.GET, "https://newsapi.org/v2/everything?q=".concat(edittext2.getText().toString()).concat("&apiKey=fd8685453e874328b5d69cca2763df68").concat("&sortBy=publishedAt"), "B", _req_request_listener);
					_Toolbar(true);
					isSearching = false;
					searchbar.setVisibility(View.GONE);
				}
				else {
					SketchwareUtil.showMessage(getApplicationContext(), "Please enter your topic you would like to search ");
				}
			}
		});
		
		_req_request_listener = new RequestNetwork.RequestListener() {
			@Override
			public void onResponse(String _param1, String _param2, HashMap<String, Object> _param3) {
				final String _tag = _param1;
				final String _response = _param2;
				final HashMap<String, Object> _responseHeaders = _param3;
				map = new Gson().fromJson(_response, new TypeToken<HashMap<String, Object>>(){}.getType());
				map.remove("totalResults");
				map.remove("status");
				articleString = (new Gson()).toJson(map.get("articles"), new TypeToken<ArrayList<HashMap<String, Object >>>(){}.getType());
				news_result = new Gson().fromJson(articleString, new TypeToken<ArrayList<HashMap<String, Object>>>(){}.getType());
				listview1.setAdapter(new Listview1Adapter(news_result));
				((BaseAdapter)listview1.getAdapter()).notifyDataSetChanged();
			}
			
			@Override
			public void onErrorResponse(String _param1, String _param2) {
				final String _tag = _param1;
				final String _message = _param2;
				SketchwareUtil.showMessage(getApplicationContext(), _message);
			}
		};
	}
	
	private void initializeLogic() {
		req.startRequestNetwork(RequestNetworkController.GET, "https://newsapi.org/v2/top-headlines?country=".concat("in").concat("&apiKey=fd8685453e874328b5d69cca2763df68"), "A", _req_request_listener);
		_Elevation(searchbar, 4);
		searchbar.setVisibility(View.GONE);
	}
	
	@Override
	protected void onActivityResult(int _requestCode, int _resultCode, Intent _data) {
		
		super.onActivityResult(_requestCode, _resultCode, _data);
		
		switch (_requestCode) {
			
			default:
			break;
		}
	}
	
	@Override
	protected void onPostCreate(Bundle _savedInstanceState) {
		super.onPostCreate(_savedInstanceState);
		country_list.add("in");
		country_list.add("us");
		
		
	}
	
	@Override
	public void onBackPressed() {
		if (isSearching) {
			_Toolbar(true);
			searchbar.setVisibility(View.GONE);
		}
		else {
			finish();
		}
	}
	public void _Elevation (final View _view, final double _number) {
		
		_view.setElevation((int)_number);
	}
	
	
	public void _gd (final View _view, final double _numb, final String _color) {
		android.graphics.drawable.GradientDrawable gd = new android.graphics.drawable.GradientDrawable();
		gd.setColor(Color.parseColor(_color));
		gd.setCornerRadius((int)_numb);
		_view.setBackground(gd);
	}
	
	
	public void _Toolbar (final boolean _v) {
		if (_v) {
			getSupportActionBar().show();
		}
		else {
			getSupportActionBar().hide();
		}
	}
	
	
	public void _MENU () {
	}
	@Override
	public boolean onCreateOptionsMenu (Menu menu){
		menu.add(0, 0, 0, "Search").setIcon(R.drawable.ic_search_white).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		switch (item.getItemId()){
			case 0:
			isSearching = true;
			_Toolbar(false);
			searchbar.setVisibility(View.VISIBLE);
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	
	public class Listview1Adapter extends BaseAdapter {
		ArrayList<HashMap<String, Object>> _data;
		public Listview1Adapter(ArrayList<HashMap<String, Object>> _arr) {
			_data = _arr;
		}
		
		@Override
		public int getCount() {
			return _data.size();
		}
		
		@Override
		public HashMap<String, Object> getItem(int _index) {
			return _data.get(_index);
		}
		
		@Override
		public long getItemId(int _index) {
			return _index;
		}
		@Override
		public View getView(final int _position, View _v, ViewGroup _container) {
			LayoutInflater _inflater = (LayoutInflater)getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View _view = _v;
			if (_view == null) {
				_view = _inflater.inflate(R.layout.news, null);
			}
			
			final LinearLayout linear6 = (LinearLayout) _view.findViewById(R.id.linear6);
			final LinearLayout linear1 = (LinearLayout) _view.findViewById(R.id.linear1);
			final LinearLayout linear3 = (LinearLayout) _view.findViewById(R.id.linear3);
			final LinearLayout layout_frame = (LinearLayout) _view.findViewById(R.id.layout_frame);
			final LinearLayout linear4 = (LinearLayout) _view.findViewById(R.id.linear4);
			final TextView textview3 = (TextView) _view.findViewById(R.id.textview3);
			final ImageView imageview2 = (ImageView) _view.findViewById(R.id.imageview2);
			final LinearLayout shadow = (LinearLayout) _view.findViewById(R.id.shadow);
			final LinearLayout linear5 = (LinearLayout) _view.findViewById(R.id.linear5);
			final TextView textview1 = (TextView) _view.findViewById(R.id.textview1);
			final TextView textview2 = (TextView) _view.findViewById(R.id.textview2);
			
			try{
				textview1.setText(_data.get((int)_position).get("title").toString());
				textview2.setText(_data.get((int)_position).get("publishedAt").toString().replace("T", " ").replace("Z", ""));
				textview3.setText(_data.get((int)_position).get("description").toString());
				Glide.with(getApplicationContext()).load(Uri.parse(_data.get((int)_position).get("urlToImage").toString())).into(imageview2);
				
				androidx.cardview.widget.CardView cv = new androidx.cardview.widget.CardView(MainActivity.this);
				
				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
				
				int mgs = (int)getDip(8);
				
				lp.setMargins(mgs,mgs,mgs,mgs);
				
				cv.setLayoutParams(lp);
				
				cv.setCardBackgroundColor(Color.WHITE);
				
				cv.setRadius((int)getDip(10));
				
				cv.setCardElevation(4);
				
				cv.setMaxCardElevation(4);
				
				cv.setPreventCornerOverlap(true);
				
				((ViewGroup)linear3.getParent()).removeView(linear3);
				
				linear1.removeAllViews();
				
				linear1.addView(cv);
				
				cv.addView(linear3);
			}catch (java.lang.Exception e){
			}
			try{
				linear1.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View _view) {
						if (!((_data.get((int)_position).get("title").toString().equals("") && _data.get((int)_position).get("author").toString().equals("")) && (_data.get((int)_position).get("source").toString().equals("") && (_data.get((int)_position).get("publishedAt").toString().equals("") && _data.get((int)_position).get("url").toString().equals(""))))) {
							i.setClass(getApplicationContext(), WeblogicActivity.class);
							i.putExtra("title", _data.get((int)0).get("title").toString());
							i.putExtra("author", _data.get((int)_position).get("author").toString());
							i.putExtra("source", _data.get((int)_position).get("source").toString());
							i.putExtra("publishedAt", _data.get((int)_position).get("publishedAt").toString());
							i.putExtra("url", _data.get((int)_position).get("url").toString());
							startActivity(i);
						}
						else {
							SketchwareUtil.showMessage(getApplicationContext(), "Wait for news to load");
						}
					}
				});
			}catch (java.lang.Exception e){
			}
			
			return _view;
		}
	}
	
	@Deprecated
	public void showMessage(String _s) {
		Toast.makeText(getApplicationContext(), _s, Toast.LENGTH_SHORT).show();
	}
	
	@Deprecated
	public int getLocationX(View _v) {
		int _location[] = new int[2];
		_v.getLocationInWindow(_location);
		return _location[0];
	}
	
	@Deprecated
	public int getLocationY(View _v) {
		int _location[] = new int[2];
		_v.getLocationInWindow(_location);
		return _location[1];
	}
	
	@Deprecated
	public int getRandom(int _min, int _max) {
		Random random = new Random();
		return random.nextInt(_max - _min + 1) + _min;
	}
	
	@Deprecated
	public ArrayList<Double> getCheckedItemPositionsToArray(ListView _list) {
		ArrayList<Double> _result = new ArrayList<Double>();
		SparseBooleanArray _arr = _list.getCheckedItemPositions();
		for (int _iIdx = 0; _iIdx < _arr.size(); _iIdx++) {
			if (_arr.valueAt(_iIdx))
			_result.add((double)_arr.keyAt(_iIdx));
		}
		return _result;
	}
	
	@Deprecated
	public float getDip(int _input){
		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, _input, getResources().getDisplayMetrics());
	}
	
	@Deprecated
	public int getDisplayWidthPixels(){
		return getResources().getDisplayMetrics().widthPixels;
	}
	
	@Deprecated
	public int getDisplayHeightPixels(){
		return getResources().getDisplayMetrics().heightPixels;
	}
	
}