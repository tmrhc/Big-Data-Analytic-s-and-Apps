package com.example.androidapp;

import java.io.File;
import java.net.URL;
import java.util.Properties;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import net.neoremind.sshxcute.core.ConnBean;
import net.neoremind.sshxcute.core.SSHExec;
import net.neoremind.sshxcute.exception.TaskExecFailException;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.os.Build;




public class MainActivity extends ActionBarActivity {

	
	Button clk;
	Button clk2;
	Button clk3;
	Button clk4;
	Button clk5;
	
	
	
	/*public static void connectSSH()
	{
		// Initialize a SSHExec instance without referring any object. 
		/*SSHExec ssh = null;
		// Wrap the whole execution jobs into try-catch block   
		try {
		    // Initialize a ConnBean object, parameter list is ip, username, password
		    ConnBean cb = new ConnBean("134.193.136.127", "cloudera","cloudera");
		    // Put the ConnBean instance as parameter for SSHExec static method getInstance(ConnBean) to retrieve a real SSHExec instance
		    ssh = SSHExec.getInstance(cb);              
		
		    // Connect to server
		    ssh.connect();
		    // Upload sshxcute_test.sh to /home/tsadmin on remote system
		    File sdCard = Environment.getExternalStorageDirectory();
	        //File directory = new File (sdCard.getAbsolutePath() + "/Data");
		    String add = sdCard.getAbsolutePath()+"/Data/Sensorak.txt";
		    ssh.uploadSingleDataToServer(add, "/home/cloudera/");
		    
	
		} 
		catch (TaskExecFailException e) 
		{
		    System.out.println(e.getMessage());
		    e.printStackTrace();
		} 
		catch (Exception e) 
		{
		    System.out.println(e.getMessage());
		    e.printStackTrace();
		} 
		finally 
		{
		    ssh.disconnect();   
		}
		try
	    {
	        JSch jsch = new JSch();
	          Session session = jsch.getSession("cloudera","134.193.136.127", 22);
	          session.setPassword("cloudera");

	          // Avoid asking for key confirmation
	          Properties prop = new Properties();
	          prop.put("StrictHostKeyChecking", "no");
	          session.setConfig(prop);

	          session.connect();

	    }
	    catch (Exception e)
	    {
	      System.out.println(e.getMessage());
	    }
	}
	*/
	
	
	@Override
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		clk = (Button)findViewById(R.id.button1);
		clk.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//WebView webview = new WebView(MainActivity.this);
				// setContentView(webview);
				// webview.loadUrl("http://www.google.com");
				//connectSSH();
			}
		});
		
		clk2 = (Button)findViewById(R.id.button2);
		clk2.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				WebView webview = new WebView(MainActivity.this);
				 setContentView(webview);
				 webview.loadUrl("http://134.193.136.127:8080/HbaseWS/jaxrs/generic/hbaseCreate/Table_Tej/GeoLocation:Date:Accelerometer:Humidity:HumidityTemperature");
				
				
			}
		});
		
		clk3 = (Button)findViewById(R.id.button3);
		clk3.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				WebView webview = new WebView(MainActivity.this);
				setContentView(webview);
				webview.loadUrl("http://134.193.136.127:8080/HbaseWS/jaxrs/generic/hbaseInsert/Table_Tej/-home-cloudera-sensor_TejKiran.txt/GeoLocation:Date:Accelerometer:Humidity:HumidityTemperature");
				
				
			}
		});
		
		clk4 = (Button)findViewById(R.id.button4);
		clk4.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				WebView webview = new WebView(MainActivity.this);
				setContentView(webview);
				webview.loadUrl("http://134.193.136.127:8080/HbaseWS/jaxrs/generic/hbaseRetrieveAll/Table_Tej");
								
			}
		});
		
		clk5 = (Button)findViewById(R.id.button5);
		clk5.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				WebView webview = new WebView(MainActivity.this);
				setContentView(webview);
				webview.loadUrl("http://134.193.136.127:8080/HbaseWS/jaxrs/generic/hbasedeletel/Table_Tej");
								
			}
		});
		
		 
		 
		 
		 //web.loadUrl("www.google.com");

		/*if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}*/
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
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
			return rootView;
		}
	}

}
