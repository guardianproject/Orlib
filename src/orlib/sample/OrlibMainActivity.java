package orlib.sample;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.SocketAddress;
import java.net.UnknownHostException;

import javax.net.SocketFactory;

import info.guardianproject.net.SocksHttpClient;
import info.guardianproject.net.SocksSocketFactory;
import info.guardianproject.orlib.R;
import info.guardianproject.orlib.R.layout;

import net.sourceforge.jsocks.socks.Socks5Proxy;
import net.sourceforge.jsocks.socks.SocksSocket;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class OrlibMainActivity extends Activity {
	
	private final static String TAG = "OrlibSample";
	private TextView textView = null;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        textView = (TextView)findViewById(R.id.WizardTextBody);
        Button btn = ((Button)findViewById(R.id.btnWizard1));
        
        btn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
			 
				Runnable runnable = new Runnable ()
				{
					public void run()
					{
						openSocksSocket("check.torproject.org",80);
					}
				};
				
				Handler handle = new Handler();
				handle.post(runnable);
			}
        });

        btn = ((Button)findViewById(R.id.btnWizard2));
        
        btn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
			 
				checkHTTP("http://check.torproject.org");
				
			}
        });
        
        
        btn = ((Button)findViewById(R.id.btnWizard3));
        
        btn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
			 
				checkHTTP("https://check.torproject.org:443");

				
			}
        });
        
    }
    
    
    public void openSocksSocket (String checkHost, int checkPort)
    {
    	textView.setText("Opening socket to " + checkHost + " on port " + checkPort + "\n");
    	
    	try
    	{
    		
    		Socks5Proxy proxy = new Socks5Proxy("localhost",9050);
    		proxy.resolveAddrLocally(false);
   
    		SocksSocket ss = null;
    		
    		PrintWriter out = null;
    		BufferedReader in = null;
    		
    		 try {
    	       
    			 ss = new SocksSocket(proxy,checkHost, checkPort);
    			 
    	            out = new PrintWriter(ss.getOutputStream(), true);
    	            in = new BufferedReader(new InputStreamReader(
    	                                        ss.getInputStream()));
    	           
    	    		
    	            out.println("GET / HTTP/1.0");
    	            out.println("Host: check.torproject.org");
    	            out.println();
    	            
    	            String line = null;
    	            
    	            while ((line = in.readLine())!=null)
    	            {
    	            	textView.append(line);
    	            	textView.append("\n");
    	            }
    	            
    	            
    	        } catch (UnknownHostException e) {
    	            Log.i(TAG,"Could not find host",e);
    	        } catch (IOException e) {
    	            Log.i(TAG,"Error reading and writing",e);
    	           
    	        }

    		

    		out.close();
    		in.close();
    		
    	}
    	catch (Exception e)
    	{
    		textView.append(e.getMessage());
    		
    		Log.e(TAG, "Unable to connect to torproject",e);
    	}
    }
    
    
    public void checkHTTP (String url)
    {
    
    	textView.setText("Attempting to connect to: " + url + "\n");

    	try
    	{
    		//get an HTTP client configured to work with local Tor SOCKS5 proxy
    		HttpClient hc = new SocksHttpClient("localhost",9050);
        	
        	HttpGet httpget = new HttpGet(url);
    		HttpResponse response = hc.execute(httpget);

    		textView.append( response.getStatusLine().getStatusCode() + "\n");
    	
    	}
    	catch (Exception e)
    	{
    		textView.append(e.getMessage());
    		
    		Log.e(TAG, "Unable to connect to torproject",e);
    	}
    }
}