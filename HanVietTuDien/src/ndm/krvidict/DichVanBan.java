package ndm.krvidict;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import android.os.AsyncTask;
import android.webkit.WebView;

class DichVanBan extends AsyncTask<String, Void, String> {

   
    String sl,tl,text;
    WebView Wv;
    
    public DichVanBan(String sl, String tl, String text, WebView webview){
    	this.sl = sl;
    	this.tl = tl;
    	this.text = text;
    	this.Wv = webview;
    }

    protected String doInBackground(String... urls) {
        try {
        	// fetch
            URL url = new URL("http://translate.google.com.tw/translate_a/t?client=t&hl=en&sl=" +
                    sl + "&tl=" + tl + "&ie=UTF-8&oe=UTF-8&multires=1&oc=1&otf=2&ssel=0&tsel=0&sc=1&q=" + 
                    URLEncoder.encode(text, "UTF-8"));
            URLConnection urlConnection = url.openConnection();
            urlConnection.setRequestProperty("User-Agent", "Something Else");
            BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String result = br.readLine();
            br.close();
            // parse
            // System.out.println(result);
            result = result.substring(2, result.indexOf("]]") + 1);
            StringBuilder sb = new StringBuilder();
            String[] splits = result.split("(?<!\\\\)\"");
            for(int i = 1; i < splits.length; i += 8)
                sb.append(splits[i]);
            String mean = "<div style = 'background-color:#ebebeb; font-size:18px; padding:10px'>"+ sb.toString().replace("\\n", "\n").replaceAll("\\\\(.)", "$1")+ "</div>";
            Wv.loadDataWithBaseURL("", mean, "text/html", "UTF-8", "");
            return "";
        } catch (Exception e) {
            
            return null;
        }
    }

    protected void onPostExecute() {
       
    }
}