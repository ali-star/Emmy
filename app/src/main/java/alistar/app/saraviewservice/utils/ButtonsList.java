package alistar.app.saraviewservice.utils;
import alistar.app.*;
import alistar.app.brain.*;
import alistar.app.timeline.*;
import alistar.app.ui.*;
import android.content.*;
import android.provider.*;
import android.view.*;
import android.view.View.*;
import java.util.*;

public class ButtonsList
{

	private SaraViewService svs;
	private List<DiamondButton> buttonasList;

	public ButtonsList ( SaraViewService context )
	{
		this.svs = context;
		buttonasList = new ArrayList<>();
		createButtons();
	}
	
	private void addButton ( String text, OnClickListener ocl )
	{
		DiamondButton button = new DiamondButton ( svs );
		button.setText( text );
		button.setOnClickListener( ocl );
		buttonasList.add( button );
	}
	
	public List<DiamondButton> getButtonsList ()
	{
		return buttonasList;
	}

	//* add buttons here *//
	private void createButtons ( )
	{
		//Dialer
		addButton( "Dialer", new View.OnClickListener ( )
			{
				@Override
				public void onClick ( View p1 )
				{
					Intent intent = new Intent ( Intent.ACTION_DIAL );
					intent.setFlags ( Intent.FLAG_ACTIVITY_NEW_TASK );
					svs.startActivity ( intent );
					svs.hideList ( );
					svs.workQueue.addTextToQueue ( "here is the dialer" );
				}	
		});
		//Massages
		addButton( "Massages", new View.OnClickListener ( )
			{
				@Override
				public void onClick ( View p1 )
				{
					Intent eventIntentMessage = svs.getPackageManager ( )
						.getLaunchIntentForPackage ( Telephony.Sms.getDefaultSmsPackage ( svs.getApplicationContext ( ) ) );
					svs.startActivity ( eventIntentMessage );
					svs.hideList ( );
					svs.workQueue.addTextToQueue ( "here is you're massages" );
				}
			});
		//save feeling
		addButton( "Save Feeling", new View.OnClickListener ( )
			{
				@Override
				public void onClick ( View p1 )
				{
					svs.hideList ( );
					if ( Utils.getInstance(svs).canAskFeeling() )
						svs.getWorkQueueService().addWork( System.currentTimeMillis(), Work.WorkName.ASK_FEELING );
					//svs.say ( "hope you ok ^.^" );
				}	
			});
		//save moment
		addButton( "Save Moment", new View.OnClickListener ( )
			{
				@Override
				public void onClick ( View p1 )
				{
					svs.startActivity ( new Intent ( svs.getApplicationContext ( ), QuickTimelineNote.class ).setFlags ( Intent.FLAG_ACTIVITY_NEW_TASK ) );
					svs.hideList ( );
					svs.workQueue.addTextToQueue ( "Moments..." );
				}	
			});
		//Find Me
		addButton( "Find Me", new View.OnClickListener ( )
			{
				@Override
				public void onClick ( View p1 )
				{
					svs.workQueue.addWork ( System.currentTimeMillis ( ), Work.WorkName.FIND_ME );
				}	
			});
		//Music
		addButton( "Music", new View.OnClickListener ( )
			{
				@Override
				public void onClick ( View p1 )
				{

				}	
			});
		//Lock
		addButton( "Lock", new View.OnClickListener ( )
			{
				@Override
				public void onClick ( View p1 )
				{
					Utils.getInstance(svs.getApplicationContext()).lockScreen();
					svs.hideList ( );
					svs.workQueue.addTextToQueue ( "Locking screen..." );
				}	
			});
		/*addButton( "text", new View.OnClickListener ( )
			{
				@Override
				public void onClick ( View p1 )
				{

				}	
			});*/
	}

}
