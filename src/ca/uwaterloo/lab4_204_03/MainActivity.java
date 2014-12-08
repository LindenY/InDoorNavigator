package ca.uwaterloo.lab4_204_03;

import java.io.File;
import java.io.FilenameFilter;

import ca.uwaterloo.mapper.MapLoader;
import ca.uwaterloo.mapper.MapView;
import ca.uwaterloo.mapper.NavigationalMap;
import ca.uwaterloo.mapper.PositionListener;
import ca.uwaterloo.navigation.Navigation;
import ca.uwaterloo.navigationView.CompassView;
import ca.uwaterloo.pathFinding.DefaultPositionListener;
import ca.uwaterloo.pathFinding.PathFinding;
import ca.uwaterloo.sensor.GeoOrientationSensor;
import ca.uwaterloo.sensor.SoftSensor;
import ca.uwaterloo.sensor.SoftSensorManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.LinearLayout;
import android.os.Build;

public class MainActivity extends ActionBarActivity {

	static private MapView mapView;
	static private Navigation navigation;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		
		mapView = new MapView(getApplicationContext(), 750, 550, 40, 40);
		registerForContextMenu(mapView);
		NavigationalMap map1 = MapLoader.loadMap(getExternalFilesDir(null), "Lab-room-peninsula.svg");
		mapView.setMap(map1);
		SoftSensorManager.initSensorManager(getApplicationContext());
		
		navigation = new Navigation(getApplicationContext(), mapView);
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		mapView.onCreateContextMenu(menu, v, menuInfo); 
	}
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		return super.onContextItemSelected(item) || mapView.onContextItemSelected(item); 
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		File dir = getExternalFilesDir(null);
		FilenameFilter filenameFilter = new FilenameFilter() {
			@Override
			public boolean accept(File dir, String filename) {
				if (filename.lastIndexOf(".") > 0) {
					String fileExtension = filename.substring(filename.lastIndexOf("."));
					if (fileExtension.equals(".svg")) {
						return true;
					}
				}
				return false;
			}
		};
		File[] mapFiles = dir.listFiles(filenameFilter);
		for (int i=0; i<mapFiles.length; i++) {
			menu.add(mapFiles[i].getName());
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		NavigationalMap map = MapLoader.loadMap(getExternalFilesDir(null), item.getTitle().toString());
		mapView.setMap(map);
		mapView.setUserPath(null);
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			LinearLayout layout = (LinearLayout) rootView.findViewById(R.id.linearLayout);
			layout.addView(mapView);
			
			
			layout.addView(navigation.getView());
			return rootView;
		}
	}

}
