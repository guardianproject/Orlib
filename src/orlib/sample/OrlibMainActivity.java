package orlib.sample;

import info.guardianproject.net.SocksHttpClient;
import info.guardianproject.orlib.R;
import info.guardianproject.orlib.R.layout;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

public class OrlibMainActivity extends Activity {
	
	private final static String TAG = "OrlibSample";
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Button btn = ((Button)findViewById(R.id.button1));
        
        checkTor();
    }
    
    public void checkTor ()
    {
    	HttpGet httpget = new HttpGet("http://check.torproject.org");
    	
    	HttpClient hc = new SocksHttpClient();
    	
    	try
    	{
    		HttpResponse response = hc.execute(httpget);

    		Log.i(TAG, response.getProtocolVersion() + "");
    		Log.i(TAG, response.getStatusLine().getStatusCode() + "");
    		Log.i(TAG, response.getStatusLine().getReasonPhrase());
    		Log.i(TAG, response.getStatusLine().toString());
    	
    	}
    	catch (Exception e)
    	{
    		Log.e(TAG, "Unable to connect to torproject",e);
    	}
    }
}