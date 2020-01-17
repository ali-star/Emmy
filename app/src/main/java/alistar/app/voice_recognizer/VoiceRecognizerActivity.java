package alistar.app.voice_recognizer;
import android.app.*;
import edu.cmu.pocketsphinx.*;
import android.os.*;
import alistar.app.*;
import java.io.*;
import android.widget.*;
import java.util.*;
import com.readystatesoftware.notificationlog.*;

public class VoiceRecognizerActivity extends Activity implements RecognitionListener
{

	@Override
	public void onError(Exception p1)
	{
		// TODO: Implement this method
	}

	@Override
	public void onTimeout()
	{
		// TODO: Implement this method
	}

	
	private static final String HOTWORD_SEARCH = "sara";//

	// Fields

	/**
	 * Title and filename of grammar ("grammar/filename.gram" in JSGF format).
	 */
	private static final String s_VOICERE_SEARCH = "hey sara";

	/**
	 * Speech mRecognizer listener.
	 * 
	 * You will get notified on speech end event in onEndOfSpeech callback of
	 * the mRecognizer listener.
	 */
	private SpeechRecognizer mRecognizer;

	/**
	 * Results through Speech recognizer listener.
	 */
	private ArrayList<String> mVoiceResults = new ArrayList<String>();

	/**
	 * Assets: Directory with dictionaries (dict), grammars (grammar), acoustic
	 * models (hmm) and language models (lm). IT IS VERY IMPORTNAT TO REMEMBER:
	 * Each file must have it's own md5sum file and each file must be listed in
	 * the assets.lst file!
	 */
	private Assets mAssets;
	private File mAssetsDir;
	private File mModelsDir;
	private File mGrammarFile;
	
	private TextView captionTxt, resultTxt;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		setContentView(R.layout.voice_recognizer_activity);
		
		captionTxt = (TextView) findViewById(R.id.caption_text);
		resultTxt = (TextView) findViewById(R.id.result_text);
		
        captionTxt.setText("Preparing the recognizer");
			
		runRecognizerSetup();
		
	}
	
	private void runRecognizerSetup() {
        // Recognizer initialization is a time-consuming and it involves IO,
        // so we execute it in async task
        new AsyncTask<Void, Void, Exception>() {
            @Override
            protected Exception doInBackground(Void... params) {
                try {
                    // Recognizer initialization
					mAssets = new Assets(getApplicationContext());
					mAssetsDir = mAssets.syncAssets();
                    setupRecognizer(mAssetsDir);
                } catch (IOException e) {
                    return e;
                }
                return null;
            }

            @Override
            protected void onPostExecute(Exception result) {
                if (result != null) {
                    captionTxt.setText("Failed to init recognizer " + result);
                } else {
                    captionTxt.setText("listening");
                }
            }
        }.execute();
    }

    private void setupRecognizer(File assetsDir) throws IOException {
        mModelsDir = new File(mAssetsDir, "models");
		mRecognizer = SpeechRecognizerSetup.defaultSetup()
			.setAcousticModel(new File(mModelsDir, "hmm/en-us-semi"))
			.setDictionary(new File(mModelsDir, "short_data.dic"))
			.setRawLogDir(mAssetsDir).setKeywordThreshold(1e-20f)
			.getRecognizer();
		mRecognizer.addListener(this);

		// Create keyword-activation search.//
		//mRecognizer.addKeywordSearch(HOTWORD_SEARCH, new File(assetsDir, "models/commands.txt"));
		mRecognizer.addKeyphraseSearch(HOTWORD_SEARCH, "SARA");

		// Create grammar-based searches
		mGrammarFile = new File(mModelsDir, "short_data"
								+ ".lm");
		mRecognizer.addNgramSearch(HOTWORD_SEARCH, mGrammarFile);

		// Start recognition (start of utterance). You will get notified on
		// speech end event in onEndOfSpeech callback of the mRecognizer
		// listener. The startListening make new RecognizerThread
		// (unexpected error in device screen rotation).
		mRecognizer.cancel();
		mRecognizer.stop();
		mRecognizer.startListening(HOTWORD_SEARCH);
		
    }

	@Override
	protected void onPause()
	{
		// TODO: Implement this method
		super.onPause();
	}

	@Override
	protected void onResume()
	{
		// TODO: Implement this method
		super.onResume();
	}

	@Override
	protected void onDestroy()
	{
		// TODO: Implement this method
		super.onDestroy();
		// Prevent unexpected error
		if (mRecognizer != null) {

			// Stops recognition. All listeners should receive final result if
			// there is any. Does nothing if recognition is not active.
			mRecognizer.stop();

			// Removes listener
			mRecognizer.removeListener(this);
			
			mRecognizer.shutdown();

		}
		
	}
	
	// Methods of edu.cmu.pocketsphinx.RecognitionListener class

	/**
	 * Called when partial recognition result is available.
	 * 
	 * @see edu.cmu.pocketsphinx.RecognitionListener#onPartialResult(edu.cmu.pocketsphinx.Hypothesis)
	 */
	@Override
	public void onPartialResult(Hypothesis hypothesis) {
		if(hypothesis == null)
			return;
		
		String text = hypothesis.getHypstr();

		captionTxt.setText(R.string.engine_on_partial_result);

		resultTxt.setText(text + "...");

		// Add word to results
		// mVoiceResults.add(text);

		// Start recognition (start of utterance). You will get notified on
		// speech end event in onEndOfSpeech callback of the mRecognizer
		// listener. The startListening make new RecognizerThread
		// (unexpected error in device screen rotation).
		// mRecognizer.cancel();
		// mRecognizer.stop();
		// mRecognizer.startListening(s_VOICERE_SEARCH);
		
	}

	/**
	 * Called after the recognition is ended.
	 * 
	 * @see edu.cmu.pocketsphinx.RecognitionListener#onResult(edu.cmu.pocketsphinx.Hypothesis)
	 */
	@Override
	public void onResult(Hypothesis hypothesis) {
		/*for (Segment seg : mRecognizer.getDecoder().seg()) {
			Log.d("tag", seg.getWord() + " " + seg.getProb());
		}*/
		// Display the caption.
		/*captionTxt.setText(R.string.engine_on_result);

		if (hypothesis != null) {
			String text = hypothesis.getHypstr();
			
			if(text.split(" ").length == -1)
			{
				mRecognizer.startListening(s_VOICERE_SEARCH);
				return;
			}

			// Display the text.
			//resultTxt.setText(text);

			// Add text to results.
			mVoiceResults.add(text);
			
			Toast.makeText(VoiceRecognizerActivity.this, text, Toast.LENGTH_LONG).show();
			
			mRecognizer.startListening(HOTWORD_SEARCH);*/
			/*
			// Prepare data to be sent (back) to the main (parent) activity
			Intent resultIntent = new Intent();
			resultIntent.putExtra(
				android.speech.RecognizerIntent.EXTRA_RESULTS,
				mVoiceResults);
			setResult(android.app.Activity.RESULT_OK, resultIntent);

			// Quit event is the same as Back button event.
			this.onBackPressed();
			*/
		//}
	}

	/**
	 * Called at the start of utterance.
	 * 
	 * @see edu.cmu.pocketsphinx.RecognitionListener#onBeginningOfSpeech()
	 */
	@Override
	public void onBeginningOfSpeech() {

		// Display the caption.
		captionTxt.setText(R.string.engine_on_beginning_of_speech);
	}

	/**
	 * Called at the end of utterance.
	 * 
	 * @see edu.cmu.pocketsphinx.RecognitionListener#onEndOfSpeech()
	 */
	@Override
	public void onEndOfSpeech() {

		// Display the caption.
		captionTxt.setText(R.string.engine_on_end_of_speech);

		// Stops recognition. All listeners should receive final result if there
		// is any. Does nothing if recognition is not active.
		// The final result be passed you in onResult callback.
		mRecognizer.stop();
	}

}
