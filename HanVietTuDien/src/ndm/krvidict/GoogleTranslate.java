package ndm.krvidict;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class GoogleTranslate extends Activity{
	EditText vanBan;
	WebView webView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.google_translate_activity);
		
		 vanBan = (EditText)findViewById(R.id.vanban);
		
		Button hanViet = (Button)findViewById(R.id.btnHV);
		Button vietHan = (Button)findViewById(R.id.btnVH);
		
		 webView = (WebView)findViewById(R.id.vanban_nghia);
		
		hanViet.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				if(isOnline()){
				String nguon = vanBan.getText().toString();
				try {
					DichVanBan dich = new DichVanBan("ko","vi", nguon, webView);
					dich.execute();
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				}
				else{
					Toast.makeText(getApplicationContext(), "Lỗi kết nối mạng", Toast.LENGTH_LONG).show();
				}
				
				
			}
		});
		
		vietHan.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(isOnline()){
				String nguon = vanBan.getText().toString();
				try {
					DichVanBan dich = new DichVanBan("vi","ko", nguon, webView);
					dich.execute();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				}
				else{
					Toast.makeText(getApplicationContext(), "Lỗi kết nối mạng", Toast.LENGTH_LONG).show();
				}
				
			}
		});
	}
	
	public void backtomain (View v) {
		
		
			Intent intent = new Intent(getApplicationContext(),MainActivity.class);
			startActivity(intent);
			
	}
	
	public boolean isOnline() {
	    ConnectivityManager cm =
	        (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo netInfo = cm.getActiveNetworkInfo();
	    return netInfo != null && netInfo.isConnectedOrConnecting();
	}

}
