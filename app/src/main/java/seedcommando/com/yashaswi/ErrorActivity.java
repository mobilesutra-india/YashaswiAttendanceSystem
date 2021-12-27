package seedcommando.com.yashaswi;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ErrorActivity extends AppCompatActivity {

	TextView error,send,tv_title_page;
    ImageView img_back;
    String Error ="";

    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
		setContentView(R.layout.activity_error);
		//showDialog(ErrorActivity.this, "Seed Application is not responding");

		error = findViewById(R.id.error);
		send = findViewById(R.id.send);
		tv_title_page = findViewById(R.id.tv_title_page);
		img_back = findViewById(R.id.img_back);
		Error = getIntent().getStringExtra("error");
		
		error.setText(Error);
		
		
		 
		
		send.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				SendErrorMail(getApplicationContext(),Error);
				Toast.makeText(ErrorActivity.this, "Error Report sent to server.", Toast.LENGTH_LONG).show();
			}
		});
		
		img_back.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
			
				System.exit(0);
				finish();
			}
		});

	}
	
	private void SendErrorMail(Context _context, String ErrorContent )
	 {
//	  Intent sendIntent = new Intent(Intent.ACTION_SEND);
	  String subject ="CrashReport_MailSubject";
	  String body = "CrashReport_MailBody " +
	   "\n\n"+
	   ErrorContent+
	   "\n\n";
//	  sendIntent.putExtra(Intent.EXTRA_EMAIL,
//	    new String[] {"bodakesatish@gmail.com"});
//	  sendIntent.putExtra(Intent.EXTRA_TEXT, body);
//	  sendIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
//	  sendIntent.setType("message/rfc822");
//	  _context.startActivity( Intent.createChooser(sendIntent, "Title:") );


		 final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
		 emailIntent.setType("text/plain");
		 emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{  "vikas.lakade@emsphere.com"});
		 emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
		 emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, body);


		 emailIntent.setType("message/rfc822");

		 try {
			 startActivity(Intent.createChooser(emailIntent,
					 "Send email using..."));
		 } catch (android.content.ActivityNotFoundException ex) {
			 Toast.makeText(ErrorActivity.this,
					 "No email clients installed.",
					 Toast.LENGTH_SHORT).show();
		 }

	 }


		
		
		
		
		/*List<Intent> targetShareIntents=new ArrayList<Intent>();
        Intent shareIntent=new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        List<ResolveInfo> resInfos=this.getPackageManager().queryIntentActivities(shareIntent, 0);
        if(!resInfos.isEmpty()){
            System.out.println("Have package");
            for(ResolveInfo resInfo : resInfos){
                String packageName=resInfo.activityInfo.packageName;
                Log.i("Package Name", packageName);
                *//*if(packageName.contains("com.whatsapp") || packageName.contains("com.facebook.katana") || packageName.contains("com.google.android.gm")){*//*
                    Intent intent=new Intent();
                    intent.setComponent(new ComponentName(packageName, resInfo.activityInfo.name));
                    intent.setAction(Intent.ACTION_SEND);
                    intent.putExtra(Intent.ACTION_SENDTO, Uri.parse("mailto:vikas.lakade@emsphere.com"));
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_CC, "vikas.lakade@emsphere.com");
                    intent.putExtra(Intent.EXTRA_TEXT, body);
                    intent.putExtra(Intent.EXTRA_SUBJECT, subject);
                    intent.setPackage(packageName);
                    targetShareIntents.add(intent);
                *//*}*//*
            }
            if(!targetShareIntents.isEmpty()){
                System.out.println("Have Intent");
                Intent chooserIntent= Intent.createChooser(targetShareIntents.remove(0), "Choose app to share");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetShareIntents.toArray(new Parcelable[]{}));
                startActivity(chooserIntent);
				//startActivity(new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:vikas.lakade@emsphere.com")));
            }else{
                System.out.println("Do not Have Intent");

            }
        }*/


	

	public void processFinish(String output) {
		// TODO Auto-generated method stub
		
	}

	public void processFinishLog(String output) {
		
		
	}
	
	public String showDialog(Context context, String msg) {
		
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(msg)
				//.setIcon(android.R.drawable.ic_dialog_alert)
				.setNegativeButton("Close", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// Yes button clicked, do something
						/*Intent intent = getIntent();
						
						startActivity(intent);*/
						dialog.dismiss();
						finish();
					}
				}).show();
		return null;
	}

	@Override
	public void onBackPressed()
	{
		super.onBackPressed();
		System.exit(0);
		finish();
	}
	
}
