package alistar.app.brain;

import java.util.Map;
import java.util.HashMap;
import android.app.*;
import android.os.*;
import android.content.*;
import alistar.app.*;
import com.readystatesoftware.notificationlog.*;
import alistar.app.map.*;
import java.util.*;

public class WorkQueue extends Service
{

	private SaraViewService saraViewService = null;
	private MyService myService = null;
	private final IBinder serviceBinder = new ServiceBinder ( );
	private SharedPreferences sets;

	public class ServiceBinder extends Binder
	{
        public WorkQueue getService ( )
		{
            return WorkQueue.this;
        }
    }

    protected ServiceConnection saraViewServerConn = new ServiceConnection ( ) {
        @Override
        public void onServiceConnected ( ComponentName name, IBinder binder )
		{
            saraViewService = ( (SaraViewService.MyBinder) binder ).getService ( );
            Log.d ( "tag", "work queue service connected to saraViewService" );
        }

        @Override
        public void onServiceDisconnected ( ComponentName name )
		{
            Log.d ( "tag", "work queue srvice disconnected from saraViewService" );
        }
    };

	protected ServiceConnection mainServerConn = new ServiceConnection ( ) {
		@Override
		public void onServiceConnected ( ComponentName name, IBinder binder )
		{
			Log.d ( "tag", "work queue service connected to mainService" );
			myService = ( (MyService.ServiceBinder) binder ).getService ( );
		}

		@Override
		public void onServiceDisconnected ( ComponentName name )
		{
			Log.d ( "tag", "work queue srvice disconnected from mainService" );
		}
	};

    public void start ( )
	{
        // mContext is defined upper in code, I think it is not necessary to explain what is it
        Intent saraViewServiceintent = new Intent ( getApplicationContext ( ), SaraViewService.class );
        bindService ( saraViewServiceintent, saraViewServerConn, Context.BIND_AUTO_CREATE );
        startService ( saraViewServiceintent );

		Intent mainServiceintent = new Intent ( getApplicationContext ( ), MyService.class );
        bindService ( mainServiceintent, mainServerConn, Context.BIND_AUTO_CREATE );
        startService ( mainServiceintent );
    }

    public void stop ( )
	{
        //mContext.stopService(new Intent(mContext, ServiceRemote.class));
        unbindService ( saraViewServerConn );
    }

    @Override
    public IBinder onBind ( Intent p1 )
	{
        // TODO: Implement this method
        return serviceBinder;
    }

	@Override
	public void onCreate ( )
	{
		start ( );
		initWorkList ( );
		sets = getSharedPreferences ( "brain", 0 );
		super.onCreate ( );
	}

	@Override
	public int onStartCommand ( Intent intent, int flags, int startId )
	{
		// TODO: Implement this method
		try
		{
            Bundle bundle = null;
            if ( intent != null )
                bundle = intent.getExtras ( );
            {
                if ( bundle == null )
                    return super.onStartCommand ( intent, flags, startId );
                String command = bundle.getString ( "command" );
                if ( command != null )
				{
                    switch ( command )
					{
							/*case MyService.DETECT_LOCATION:
							 if (!detectingLocation) {
							 myService.detectLocation();
							 }
							 break;
							 case MyService.CANCEL_DETECT_LOCATION:
							 myService.stopDetectLocation();
							 break;
							 case MyService.CHECK_SIM:
							 myService.checkSim();
							 break;*/
                        case Utils.WAKEFUL_SEARCH_NEARBY_PLACE:
                            addWork ( System.currentTimeMillis ( ), Work.WorkName.FIND_ME );
                            break;
                        default:
                            break;
                    }
                }
            }
        }
		catch (Exception e)
		{
            e.printStackTrace ( );
        }
		return super.onStartCommand ( intent, flags, startId );
	}

	private LinkedHashMap <Long, Work> workList;
	private LinkedHashMap<Long ,TextQueueItem> textList;

	public void initWorkList ( )
	{
		workList = new LinkedHashMap<> ( );
		textList = new LinkedHashMap<> ( );
	}

	public void addWork ( long id, Work.WorkName workName )
	{
		Work work = new Work ( id, workName );
		workList.put ( id, work );
		switch ( work.getWork ( ) )
		{
			case OPEN_SARA_MAIN_VIEW:
				openSaraMainView ( );
				break;
			case OPEN_LIST:
				openList ( );
				break;
			case FIND_ME:
				findMe ( id );
				break;
			case ASK_FEELING:
				if ( !getWorkByName ( Work.WorkName.ASK_FEELING ) )
					askFeeling( id );
				break;
			case VOICE_RECORD:
				startVoiceRecord();
				break;
		}


	}

	public void removeWork ( long id )
	{
		workList.remove ( id );
	}

	private void openSaraMainView ( )
	{
		saraViewService.showSaraBase ( );
	}

	private void closeSaraMainView ( )
	{
		textList.clear ( );
		saraViewService.hideSaraBase ( );
	}

	/*private void organizWorks ( )
	 {
	 boolean doesAllWorksConfirmed = doesAllWorksConfirmed();
	 boolean isThereOngoingWork = isThereOngoingWork();
	 if ( workList.size() == 0 )
	 {
	 closeSaraMainView();
	 }

	 if( !doesAllWorksConfirmed & !isThereOngoingWork )
	 {

	 }
	 }*/

	private boolean doesAllWorksConfirmed ( )
	{
		for ( int i = 0; i < workList.size ( ); i++ )
		{
			if ( !getWorkByIndex ( i ).isConfirmed ( ) )
				return false;
		}
		return true;
	}

	private boolean isThereOngoingWork ( )
	{
		for ( int i = 0; i < workList.size ( ); i++ )
		{
			if ( getWorkByIndex ( i ).getStatus ( ) == Work.STATUS_ONGOING | getWorkByIndex ( i ).getStatus ( ) == Work.STATUS_PENDING  )
				return true;
		}
		return false;
	}
	
	private boolean getWorkByName ( Work.WorkName work )
	{
		for ( int i = 0; i < workList.size(); i++ )
		{
			if ( getWorkByIndex( i ).getWork() == work )
			{
				Log.d("svs", "work found");
				if ( getWorkByIndex( i ).getStatus() == Work.STATUS_ONGOING )
					return true;
			}
		}
		return false;
	}

	private Work getWorkByIndex ( int index )
	{
		return workList.get ( ( workList.keySet ( ).toArray ( ) ) [ index ] );
	}

	private TextQueueItem getTextQueueItemByIndex ( int index )
	{
		return textList.get ( ( textList.keySet ( ).toArray ( ) ) [ index ] );
	}

	private void removeTextQueueItemByIndex ( int index )
	{
		textList.remove ( ( textList.keySet ( ).toArray ( ) ) [ index ] );
	}

	private void saraSay ( String text )
	{
		saraViewService.say ( text );
	}

	private void printText ( long time )
	{

		new Handler ( Looper.getMainLooper() ).postDelayed ( new Runnable ( )
			{

				@Override
				public void run ( )
				{
					//if there is work
					if ( textList.size ( ) > 0 )
					{
						//if there just 1 work and it's ongoing
						if ( getTextQueueItemByIndex ( textList.size ( ) - 1 ).isOngoing ( ) & textList.size ( ) == 1 )
						{
							saraSay ( getTextQueueItemByIndex ( textList.size ( ) - 1 ).getText ( ) );
						}
						//if cuurrent work not ongoing
						if ( !getTextQueueItemByIndex ( textList.size ( ) - 1 ).isOngoing ( ) )
						{
							saraSay ( getTextQueueItemByIndex ( textList.size ( ) - 1 ).getText ( ) );
							removeTextQueueItemByIndex ( textList.size ( ) - 1 );
							printText ( 2000 );
							return;
						}
						//if is there ongoing work and works are more than 1 and all works are not ongoing
						if ( textList.size ( ) > 1 & getTextQueueItemByIndex ( textList.size ( ) - 1 ).isOngoing ( ) & !isAllTextItemsOngoing ( ) )
						{
							for ( int i = 0; i < textList.size ( ); i++ )
							{
								if ( !getTextQueueItemByIndex ( textList.size ( ) - i ).isOngoing ( ) )
								{
									saraSay ( getTextQueueItemByIndex ( textList.size ( ) - i ).getText ( ) );
									removeTextQueueItemByIndex ( textList.size ( ) - i );
									printText ( 2000 );
									break;
								}
							}
						}
						//if is there ongoing work and works are more than 1 and all works are ongoing
						if ( textList.size ( ) > 1 & getTextQueueItemByIndex ( textList.size ( ) - 1 ).isOngoing ( ) & !isAllTextItemsOngoing ( ) )
						{
							saraSay ( textList.get ( getOngoingWorkByPriority ( ).getId ( ) ).getText ( ) );
						}
					}
					//if there is no works
					else
					{
						sayGoodbay ( );
					}
				}


			}, time );
	}

	private boolean isAllTextItemsOngoing ( )
	{
		boolean response = false;
		for ( int i = 0; i < textList.size ( ); i++ )
		{
			if ( getTextQueueItemByIndex ( i ).isOngoing ( ) )
				response = true;
		}
		return response;
	}

	private Work getOngoingWorkByPriority ( )
	{
		Work work = getWorkByIndex ( 0 );
		for ( int i = 0; i < workList.size ( ); i++ )
		{
			if ( getWorkByIndex ( i ).getPriority ( ) > work.getPriority ( ) )
				work = getWorkByIndex ( i );
		}
		return work;
	}

	private void sayGoodbay ( )
	{
		new Handler ( ).postDelayed ( new Runnable ( )
			{

				@Override
				public void run ( )
				{
					saraSay ( "`(*∩_∩*)′" );
					new Handler ( ).postDelayed ( new Runnable ( )
						{

							@Override
							public void run ( )
							{
								closeSaraMainView ( );
							}


						}, 2000 );
				}


			}, 3000 );
	}

	public void addTextToQueue ( String text )
	{
		textList.put ( getCurrentTime ( ), new TextQueueItem ( text ) );
		/*if ( sets.getLong ( "last_hello_time", 0 ) + ( 5 * 60000 ) < System.currentTimeMillis ( ) )
		 {
		 //Log.d( "textList", "say hello" );
		 textList.put ( getCurrentTime ( ), new TextQueueItem ( "Hi Ali" ) );
		 sets.edit ( ).putLong ( "last_hello_time", System.currentTimeMillis ( ) ).commit ( );
		 }*/
		printText ( saraViewService.isSaraVisible ? 0 : 2500 );
		if ( !saraViewService.isSaraVisible )
			openSaraMainView ( );
	}

	public void addTextToQueue ( TextQueueItem text )
	{
		textList.put ( text.getId ( ), text );
		printText ( saraViewService.isSaraVisible ? 0 : 2500 );
		if ( !saraViewService.isSaraVisible )
			openSaraMainView ( );
		/*if ( sets.getLong ( "last_hello_time", 0 ) + ( 5 * 60000 ) < System.currentTimeMillis ( ) )
		 {
		 //Log.d( "textList", "say hello" );
		 saraSay( "Hi Ali" );
		 sets.edit ( ).putLong ( "last_hello_time", System.currentTimeMillis ( ) ).commit ( );
		 }*/

	}

	private void openList ( )
	{

	}

	private void findMe ( final long id )
	{
		final Work work = workList.get ( id );
		work.setListener ( new Work.WorkListener ( )
			{

				@Override
				public void onStartListener ( )
				{
					addTextToQueue ( new TextQueueItem ( work.getId ( ), work.getStartText ( ), true, work.getPriority ( ) ) );
				}

				@Override
				public void onFinishListener ( )
				{
					textList.remove ( id );
					addTextToQueue ( new TextQueueItem ( work.getEndText ( ) ) );
					workList.remove ( id );
				}


			} );
		work.setStartText ( "finding you..." );
		work.setStatus ( Work.STATUS_ONGOING );
		work.setPriority ( Work.PRIORITY_MED );
		myService.findCurrentLocation ( );
		myService.setOnLocationFoundListener ( new MyService.OnLocationFoundListener ( )
			{

				@Override
				public void onLocationFound ( MapMarker marker, String textToSay )
				{
					work.setEndText ( textToSay );
					work.setStatus ( Work.STATUS_DONE );

				}


			} );

	}
	
	public void askFeeling ( final long id )
	{
		Log.d( "WorkQueue", "Ask Feeling" );
		openSaraMainView();
		saraViewService.showSaveEmotionPage();
		final Work work = workList.get ( id );
		work.setListener ( new Work.WorkListener ( )
			{

				@Override
				public void onStartListener ( )
				{
					addTextToQueue ( new TextQueueItem ( work.getId ( ), work.getStartText ( ), true, work.getPriority ( ) ) );
				}

				@Override
				public void onFinishListener ( )
				{
					textList.remove ( id );
					addTextToQueue ( new TextQueueItem ( work.getEndText ( ) ) );
					workList.remove ( id );
				}


			} );
		work.setStartText ( "How are you feeling?" );
		work.setStatus ( Work.STATUS_ONGOING );
		work.setPriority ( Work.PRIORITY_HIG );
		saraViewService.setOnEmotionSavedListener( new SaraViewService.OnEmotionSaved ()
			{

				@Override
				public void onEmotionSaved ( int emotion )
				{
						Utils.getInstance( getApplicationContext() ).saveAliFeeling(emotion, null, null);
						saraViewService.closeSaveEmotionPage();
						work.setEndText( "Emotion saved value " + (emotion > 0 ? "+" + emotion : emotion ) );
						work.setStatus( Work.STATUS_DONE );
				}
				
			
		});
	}
	
	public void startVoiceRecord ( )
	{
		if ( isRecordingVoice() )
			return;
		myService.startVoiceRecord();
	}

	public boolean isRecordingVoice ( )
	{
		return myService.isRecordingVoice();
	}

	public void stopVoiceRecord ( )
	{
		myService.stopVoiceRecord();
	}

	public class TextQueueItem
	{
		long id = 0;
		String text;
		boolean ongoing = false;
		int priority = -1;

		public TextQueueItem ( long id, String text, boolean ongoing, int priority )
		{
			this.id = id;
			this.text = text;
			this.ongoing = ongoing;
			this.priority = priority;
		}

		public TextQueueItem ( String text )
		{
			this.text = text;
		}

		public long getId ( )
		{
			return this.id;
		}

		public String getText ( )
		{
			return this.text;
		}

		public boolean isOngoing ( )
		{
			return this.ongoing;
		}

		public int getPriority ( )
		{
			return this.priority;
		}
	}

	private long getCurrentTime ( )
	{
		return System.currentTimeMillis ( );
	}
}
