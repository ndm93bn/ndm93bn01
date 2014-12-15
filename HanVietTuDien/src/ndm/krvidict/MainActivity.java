package ndm.krvidict;

import java.util.ArrayList;
import java.util.Locale;

import ndm.krvidict.database.MyDatabaseHelper;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity {

	/** Hien thi goi y tim kiem */
	public CustomAutoCompleteView myAutoComplete;

	/** adapter for auto-complete */
	public ArrayAdapter<Word> myAdapter;
	
	/** database */
	public MyDatabaseHelper db;
	
	/** Mang cac tu */
	public Word[] words;
	
	/** Chuyen tu text sang am thanh */
	TextToSpeech myTextToSpeech;
	
	/** TextView hien thi word */
	TextView tvWord;
	
	/** TextView nghia cua tu */
	TextView tvWordMean;
	
	/** Tu dang tra */
	Word word;
	
	/** Button doc tu */
	ImageButton btn_speaker;
	
	/** Webview hien thi content */
	WebView browser;
	
	/** Button micorophone */
	ImageButton btnMicrophone;
	
	 private final int REQ_CODE_SPEECH_INPUT = 100;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//Khoi ta database
		db = MyDatabaseHelper.getInstance(this);

		/* An cac view khong can thiet */
		
		//Nut doc tu
		btn_speaker = (ImageButton) findViewById(R.id.speaker);
		btn_speaker.setVisibility(View.GONE);

		//Tu
		tvWord = (TextView) findViewById(R.id.word);
		tvWord.setVisibility(View.GONE);
		
		//Nghia cua tu
		tvWordMean = (TextView) findViewById(R.id.word_mean);
		tvWordMean.setVisibility(View.GONE);

		//Webview content
	    browser = (WebView) findViewById(R.id.mean);
	
	    //Chen noi dung ban dau cho webview
		String browserFirstContent = "<div style='color:green' ><center><h3>Hàn Việt Từ Điển</h3>"
				+ "Version 1.0 by Ndmgroup</center>"
				+ "<ul>"
				+ "<li>Tra từ Hàn Việt</li>"
				+ "<li>Tra từ Việt Hàn</li>"
				+ "<li>Dịch văn bản qua mạng</li>"
				+ "<li>Tra từ bằng dọng nói</li>"
				+ "<li>Phát âm từ </li>"
				+ "<li>Dịch chữ Việt ra chữ Hàn</li>"
				+ "<li>Tra từ offline</li>"
				+ "<li>Đơn giản, tiện dụng, thân thiện</li>"
				+ "<li>Hoàn toàn miễn phí</li>"
				+ "</ul>"
				+ "</div>";
		
		browser.loadDataWithBaseURL("", browserFirstContent, "text/html", "UTF-8", "");

		//Chuyen van ban thanh am thanh
		myTextToSpeech = new TextToSpeech(getApplicationContext(),
				new TextToSpeech.OnInitListener() {
					@Override
					public void onInit(int status) {
						if (status != TextToSpeech.ERROR) {
							myTextToSpeech.setLanguage(Locale.KOREAN);
						}
					}
				});
		
		//xu ly speech to text
		btnMicrophone = (ImageButton)findViewById(R.id.speech);
		btnMicrophone.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				 promptSpeechInput();
			}
		} );
		
		
         //Xu ly auto complete
		try {
			// autocompletetextview is in activity_main.xml
			myAutoComplete = (CustomAutoCompleteView) findViewById(R.id.myautocomplete);

			//Xu ly kich vao 1 item
			myAutoComplete
					.setOnItemClickListener(new AdapterView.OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> parent,
								View arg1, int pos, long id) {

							LinearLayout rl = (LinearLayout) arg1;
							TextView tv = (TextView) rl.getChildAt(0);
							
							//Bo chon o tim kiem
							myAutoComplete.clearFocus();

							//An ban phim ao
							InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
							im.hideSoftInputFromWindow(
									myAutoComplete.getWindowToken(), 0);

							//Goi ham xu ly tim kiem tu
							xuLyKetQua((Integer) tv.getTag());

						}

					});
			
			//dich văn bản
			ImageButton btnGoogleTransle = (ImageButton)findViewById(R.id.translate);
			btnGoogleTransle.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent(getApplicationContext(), GoogleTranslate.class);
					startActivity(intent);
					
				}
			});

			// add the listener so it will tries to suggest while the user types
			myAutoComplete
					.addTextChangedListener(new CustomAutoCompleteTextChangedListener(
							this));

			//ObjectItemData has no value at first
			Word[] ObjectItemData = db.read("");

			// set the custom ArrayAdapter
			myAdapter = new AutocompleteCustomArrayAdapter(this,
					R.layout.list_view_row, ObjectItemData);
			myAutoComplete.setAdapter(myAdapter);

		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void xuLyKetQua(int id) {

		

		//tra tu theo id
		word = db.getWordById(id);
		
		//neu tim thay tu
		if (word != null) {
			
			//Hien cac view
			tvWord.setVisibility(View.VISIBLE);
			tvWordMean.setVisibility(View.VISIBLE);
			btn_speaker.setVisibility(View.VISIBLE);
			
			//Hien thi tu va nghia
			tvWord.setText(word.objectName);
			tvWordMean.setText(word.mean);
			
			//set doc tu
			btn_speaker.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
				
					//doc tu
					myTextToSpeech.speak(word.pron, TextToSpeech.QUEUE_FLUSH, null);
				}
			});

			//Load content
			WebView browser = (WebView) findViewById(R.id.mean);
			
			browser.loadDataWithBaseURL("", db.traTu(word), "text/html", "UTF-8","");
		}
		else{
			
			tvWord.setVisibility(View.GONE);
			tvWordMean.setVisibility(View.GONE);
			
			//Load content
			WebView browser = (WebView) findViewById(R.id.mean);
			
			browser.loadDataWithBaseURL("", "Từ bạn tìm không có vui lòng thử lại", "text/html", "UTF-8","");
			
		}

	}
	
	private void xuLyTu(String str){
		word = db.getWord(str);
		//neu tim thay tu
				if (word != null) {
					
					//Hien cac view
					tvWord.setVisibility(View.VISIBLE);
					tvWordMean.setVisibility(View.VISIBLE);
					btn_speaker.setVisibility(View.VISIBLE);
					
					//Hien thi tu va nghia
					tvWord.setText(word.objectName);
					tvWordMean.setText(word.mean);
					
					//set doc tu
					btn_speaker.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
						
							//doc tu
							myTextToSpeech.speak(word.pron, TextToSpeech.QUEUE_FLUSH, null);
						}
					});

					//Load content
					WebView browser = (WebView) findViewById(R.id.mean);
					
					browser.loadDataWithBaseURL("", db.traTu(word), "text/html", "UTF-8","");
				}
				else{
					
					tvWord.setVisibility(View.GONE);
					tvWordMean.setVisibility(View.GONE);
					
					//Load content
					WebView browser = (WebView) findViewById(R.id.mean);
					
					browser.loadDataWithBaseURL("", "Từ bạn tìm không có vui lòng thử lại", "text/html", "UTF-8","");
					
				}
	}


	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		if (word != null) {
			//Luu id cua tu datra
			outState.putInt("id", word.id);
		}
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onRestoreInstanceState(savedInstanceState);
		int id = savedInstanceState.getInt("id");
		if (id != 0) {
			//phuc hoi noi dung tu da tra
			xuLyKetQua(id);
		}
	}
	
	/**
     * Showing google speech input dialog
     * */
    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,Locale.KOREA);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
               "Đọc từ cần tra");
        
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    "Xin lỗi, máy bạn không hỗ trợ chức năng này",
                    Toast.LENGTH_SHORT).show();
        }
    }
    
    /**
     * Receiving speech input
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
 
        switch (requestCode) {
        case REQ_CODE_SPEECH_INPUT: {
            if (resultCode == RESULT_OK && null != data) {
 
                ArrayList<String> result = data
                        .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                myAutoComplete.setText(result.get(0));
                xuLyTu(result.get(0));
            }
            break;
        }
 
        }
    }

}
