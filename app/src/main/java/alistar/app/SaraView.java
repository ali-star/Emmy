package alistar.app;
import android.view.*;
import android.content.*;
import android.util.*;
import android.graphics.*;
import android.os.*;
import android.widget.*;
import com.readystatesoftware.notificationlog.Log;
import java.util.*;
import alistar.app.ui.Animate;
import com.nineoldandroids.animation.Animator;

public class SaraView extends View
{

	private Paint saraPaint = new Paint();
	private int width, height, widthCenter, heightCenter, circleRedius, circleStorkWidth, padding;
	private OnThreeTapsListenear threeTapsListnear;
	private Handler tapHandler;
	private Runnable tapRunnable;
	private int tapCounter = 0;
	private TextView saraTv;
	private Timer timer = new Timer();
	private String textString = "";
	private int letterCounter = -1;
	private String fullText;
	private TimerTask task;
	private boolean canSay = true;
	private OnTextCleared onTextClearedListener = null;
	private int loadingCircleAngle = 0;
	private Paint loadingCirclePaint = new Paint();
	private boolean waiting = false;
	
	public interface OnTextCleared
	{
		void OnTextCleared();
	}

	public SaraView(Context context)
	{
		super(context);
		init();
	}

	public SaraView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		init();
	}

	private void init()
	{
		circleStorkWidth = 12;
		padding = 1;

		saraPaint.setAntiAlias(true);
		saraPaint.setColor(Color.parseColor("#6994FD"));
		saraPaint.setStyle(Paint.Style.STROKE);
		saraPaint.setStrokeWidth(circleStorkWidth);
		
		loadingCirclePaint.setAntiAlias(true);
		loadingCirclePaint.setColor(Color.parseColor("#ffffff"));
		loadingCirclePaint.setStyle(Paint.Style.STROKE);
		loadingCirclePaint.setStrokeWidth(circleStorkWidth / 6);
		loadingCirclePaint.setPathEffect(new DashPathEffect(new float[] {8,12}, 0));
	}
	
	public void setColor ( int color )
	{
		saraPaint.setColor(color);
		invalidate();
	}

	public void setTextView(TextView tv)
	{
		saraTv = tv;
	}
	
	public void show()
	{
		SaraView.this.setVisibility(View.VISIBLE);
		com.github.florent37.viewanimator.ViewAnimator
			.animate(SaraView.this)
			.pulse()
			.flash()
			.duration(750)
			.start();
	}
	
	public void hide()
	{
		Animate.softOut(SaraView.this, 350, new Animator.AnimatorListener()
			{

				@Override
				public void onAnimationStart(Animator p1)
				{
					// TODO: Implement this method
				}

				@Override
				public void onAnimationEnd(Animator p1)
				{
					// TODO: Implement this method
					saraTv.setVisibility(View.GONE);
					SaraView.this.setVisibility(View.GONE);
				}

				@Override
				public void onAnimationCancel(Animator p1)
				{
					// TODO: Implement this method
				}

				@Override
				public void onAnimationRepeat(Animator p1)
				{
					// TODO: Implement this method
				}
				
			
		});
		clearText(350);
	}

	public void say(String text)
	{
		if (!canSay)
			return;
		Log.d("tag", "say");
		show();
		saraTv.setVisibility(View.VISIBLE);
		//Animate.alpha(saraTv, 1, 230, null);
		canSay = false;
		timer = new Timer();
		fullText = text;
		letterCounter = -1;
		textString = "";
		saraTv.setText("");
		//saraTv.setText(text);
		com.github.florent37.viewanimator.ViewAnimator
			.animate(SaraView.this)
			.pulse()
			.flash()
			.duration(750)
			.start();

		task = new TimerTask()
		{

			@Override
			public void run()
			{
				// TODO: Implement this method
				letterCounter++;
				if (letterCounter < fullText.length())
				{
					textString += fullText.charAt(letterCounter);
					SaraView.this.post(new Runnable()
						{

							@Override
							public void run()
							{
								// TODO: Implement this method

								//if(tester != letterCounter)
								//	return;

								saraTv.setText(textString);

							}


						});
				}
				else
				{
					letterCounter = 0;
					timer.cancel();
					canSay = true;
				}
				//StartTimer();
			}

		};

		timer.scheduleAtFixedRate(task, 50, 50);
	}
	
	public void setWaitingMode(boolean b)
	{
		waiting = b;
		invalidate();
	}

	@Override
	protected void onDraw(Canvas canvas)
	{
		// TODO: Implement this method
		super.onDraw ( canvas );
		if(loadingCircleAngle == 360)
			loadingCircleAngle = 0;
		loadingCircleAngle++;
		width = getWidth();
		height = getHeight();
		widthCenter = width / 2;
		heightCenter = height / 2;
		circleRedius = (width / 2) - (circleStorkWidth + padding);
		canvas.drawCircle(widthCenter, heightCenter, circleRedius, saraPaint);
		if(waiting)
		{
			/*if(dashWidth < 10)
			 {
			 dashWidth++;
			 loadingCirclePaint.setPathEffect(new DashPathEffect(new float[] {dashWidth,10}, 0));
			 }*/
			canvas.save();
			canvas.rotate(loadingCircleAngle, widthCenter, heightCenter);
			canvas.drawCircle(widthCenter, heightCenter, circleRedius + circleStorkWidth * 1.1f, loadingCirclePaint);
			canvas.restore();
			invalidate();
		}
	}

	public void setRedius(int r)
	{
		circleStorkWidth = r;
		saraPaint.setStrokeWidth(r);
		invalidate();
	}

	public interface OnThreeTapsListenear
	{
		void onThreeTaps();
	}

	public void setOnThreeTapsListnear(OnThreeTapsListenear ottl)
	{
		threeTapsListnear = ottl;
		tapHandler = new Handler();
		tapRunnable = new Runnable()
		{

			@Override
			public void run()
			{
				// TODO: Implement this method
				if (tapCounter > 0)
				{
					tapCounter--;
					//Toast.makeText(getContext(), String.valueOf(tapCounter), Toast.LENGTH_SHORT).show();
					//Log.d("tag", String.valueOf(tapCounter));
					tapHandler.postDelayed(tapRunnable, 700);
				}

			}

		};
		setOnClickListener(new OnClickListener()
			{

				@Override
				public void onClick(View p1)
				{
					// TODO: Implement this method
					tapCounter++;
					tapHandler.postDelayed(tapRunnable, 700);
					//Log.d("tag", String.valueOf(tapCounter));
					if (tapCounter == 3)
					{
						tapCounter = 0;
						threeTapsListnear.onThreeTaps();
					}
					//Toast.makeText(getContext(), String.valueOf(tapCounter), Toast.LENGTH_SHORT).show();

				}


			});
	}

	public void say(final String text, long time)
	{
		new Handler().postDelayed(new Runnable()
			{

				@Override
				public void run()
				{
					// TODO: Implement this method
					say(text);
				}


			}, time);
	}

	public void clearText()
	{
		if (!canSay)
			return;
		canSay = false;
		timer = new Timer();
		fullText = saraTv.getText().toString();
		letterCounter = fullText.length();
		task = new TimerTask()
		{

			@Override
			public void run()
			{
				// TODO: Implement this method
				SaraView.this.post(new Runnable()
					{

						@Override
						public void run()
						{
							if (letterCounter > -1)
							{
								textString = fullText.substring(0, letterCounter);
								saraTv.setText(textString);
								letterCounter--;
							}
							else
							{
								letterCounter = -1;
								timer.cancel();
								canSay = true;
							}

						}


					});
				//StartTimer();
			}
		};
		timer.scheduleAtFixedRate(task, 50, 50);
		saraTv.setVisibility(View.VISIBLE);
		//Animate.alpha(saraTv, 0, fullText.length() * 50, null);
	}

	public void say(String p0, final String p1)
	{
		// TODO: Implement this method
		say(p0);
		new Handler().postDelayed(new Runnable()
			{

				@Override
				public void run()
				{
					// TODO: Implement this method
					if (p1 == null)
					{
						clearText();
						return;
					}
					say(p1);
				}


			}, 3500);
	}

	public void say(String p0, final String p1, final String p2)
	{
		// TODO: Implement this method
		say(p0);
		new Handler().postDelayed(new Runnable()
			{

				@Override
				public void run()
				{
					// TODO: Implement this method
					say(p1);
					new Handler().postDelayed(new Runnable()
						{

							@Override
							public void run()
							{
								// TODO: Implement this method
								say(p2);
							}


						}, 3000);
				}


			}, 3500);
	}

	public void say(String p0, long d, final OnTextCleared listener)
	{
		// TODO: Implement this method
		say(p0);
		new Handler().postDelayed(new Runnable()
			{

				@Override
				public void run()
				{
					// TODO: Implement this method
					onTextClearedListener = listener;
					onTextClearedListener.OnTextCleared();
				}


			}, d);
	}

	public void say(String p0, final String p1, final OnTextCleared listener)
	{
		// TODO: Implement this method
		say(p0);
		new Handler().postDelayed(new Runnable()
			{

				@Override
				public void run()
				{
					// TODO: Implement this method
					say(p1);
					new Handler().postDelayed(new Runnable()
						{

							@Override
							public void run()
							{
								// TODO: Implement this method
								onTextClearedListener = listener;
								onTextClearedListener.OnTextCleared();
							}


						}, 3000);
				}


			}, 3500);
	}

	public void clearText(long d)
	{
		if (!canSay | saraTv.getText() == null)
			return;
		if(saraTv.getText().toString().length() == 0)
			return;
		canSay = false;
		timer = new Timer();
		fullText = saraTv.getText().toString();
		letterCounter = fullText.length();
		task = new TimerTask()
		{

			@Override
			public void run()
			{
				// TODO: Implement this method
				SaraView.this.post(new Runnable()
					{

						@Override
						public void run()
						{
							if (letterCounter > -1)
							{
								textString = fullText.substring(0, letterCounter);
								saraTv.setText(textString);
								letterCounter--;
							}
							else
							{
								letterCounter = -1;
								timer.cancel();
								canSay = true;
							}

						}


					});
				//StartTimer();
			}
		};
		timer.scheduleAtFixedRate(task, d / letterCounter, d / letterCounter);
		//Animate.alpha(saraTv, 0, d, null);
	}


}
