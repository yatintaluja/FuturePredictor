package in.iamyat.futurepredictor;



import in.iamyat.futurepredictor.ShakeDetector.OnShakeListener;
import android.graphics.drawable.AnimationDrawable;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {
	
	public static final String TAG = MainActivity.class.getSimpleName();
	
	private FuturePredictor mFuturePredictor = new FuturePredictor();
	private TextView mAnswerLabel;
	private ImageView mFuturePredictionImage;
	private SensorManager mSensorManager;
	private Sensor mAccelerometer; 
	private ShakeDetector mShakeDetector;

    @Override 	
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        //assign variables
        mAnswerLabel = (TextView) findViewById(R.id.textView1);
        mFuturePredictionImage = (ImageView) findViewById(R.id.imageView1);
        
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new ShakeDetector(new OnShakeListener() {
			
			@Override
			public void onShake() {
				handleNewAnswer(); 
			}
		});
        
      Toast.makeText(this, "Welcome to Future Predictor!",Toast.LENGTH_LONG).show();
      Toast.makeText(this, "Ask a Question!",Toast.LENGTH_LONG).show();
      
      Log.d(TAG, "We're logging from the onCreate() method!");		
    }
    
    @Override
    public void onResume() {
    	super.onResume();	
    	mSensorManager.registerListener(mShakeDetector, mAccelerometer, 
           		SensorManager.SENSOR_DELAY_UI);
    }
    
    @Override
    public void onPause() {
    	super.onPause();
    	mSensorManager.unregisterListener(mShakeDetector);
    }

    
    private void animateAnswer() {
    	AlphaAnimation fadeInAnimation = new AlphaAnimation(0,1);
    	fadeInAnimation.setDuration(1500);
    	fadeInAnimation.setFillAfter(true);
    	
    	mAnswerLabel.setAnimation(fadeInAnimation);
    }
    
    private void animateFuturePrediction() {
    	mFuturePredictionImage.setImageResource(R.drawable.ball_animation);
    	AnimationDrawable ballAnimation = (AnimationDrawable) mFuturePredictionImage.getDrawable();
    	if(ballAnimation.isRunning()){
    		ballAnimation.stop();
    	}
    	ballAnimation.start();
    } 
    
    private void playSound() {
    	MediaPlayer player = MediaPlayer.create(this, R.raw.crystal_ball);
    	player.start();
    	player.setOnCompletionListener(new OnCompletionListener() {
			
			public void onCompletion(MediaPlayer mp) {
				mp.release();	
			}
		});
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


	private void handleNewAnswer() {
		String answer = mFuturePredictor.getAnAnswer();
		
		// Update the label with our dynamic answer	
		mAnswerLabel.setText(answer);
		animateFuturePrediction();
		animateAnswer();
		playSound();
	}
}
