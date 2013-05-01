package ua.naiksoftware.jadx;

import android.app.ProgressDialog;
import android.content.*;
import android.os.*;
import android.widget.*;
import android.app.AlertDialog;
import android.view.View;
import android.view.LayoutInflater;
import android.text.Html;
import android.util.Log;

/**
 * Wait dialog
 */
public class Decompiler extends AsyncTask<String, String, Boolean> {

    private static final String tag = "Decompiler";
    private Context context;
    private AlertDialog dialog;
	private View dialogView;
	private TextView tvConsole;
	private StringBuilder logBuilder;

    public Decompiler(Context context) {
        this.context = context;
		dialogView = LayoutInflater.from(context).inflate(R.layout.progress_dialog_layout, null);
		tvConsole = (TextView) dialogView.findViewById(R.id.tvConsole);
		logBuilder = new StringBuilder();
    }

    //update progress
    public void update(String where, String add, Level level) {
        //Log.e(tag, "update: " + add);
		String color = null;
		switch (level) {
			case INFO: color = "<font color=\"white\">";
				break;
			case DEBUG: color = "<font color=\"blue\">";
				break;
			case WARNING: color = "<font color=\"orange\">";
				break;
			case ERROR: color = "<font color=\"red\">";
				break;
		}
		logBuilder.append(color).append(add).append("</font><\\br>");
        publishProgress(logBuilder.toString());
    }

    @Override
    protected Boolean doInBackground(String[] p1) {
        final String strInput = p1[0];
        final String strOutput = p1[1];

        jadx.Main.main(new String[]{"-d", strOutput, strInput});

        return true;
    }

    @Override
    public void onProgressUpdate(String... s) {
        tvConsole.setText(Html.fromHtml(s[0]));
    }

    @Override
    public void onPreExecute() {
        //Log.e(tag, "onPreExecute");
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        //dialog.setTitle(R.string.decompiling);
        builder.setView(dialogView);
		dialog = builder.create();
        dialog.show();
    }
	
    @Override
    public void onPostExecute(Boolean result) {
        //Log.e("Installator", "onPostExecute with: " + result);
        if (result) {
            dialog.dismiss();
            Toast t = Toast.makeText(context, context.getResources().getString(R.string.decompile_complete), Toast.LENGTH_LONG);
            t.show();
        } else {
            dialog.dismiss();
            Toast t = Toast.makeText(context, "Err in Decompiler", Toast.LENGTH_LONG);
            t.show();
        }
        //Log.e(tag, "onPostExecuted");
    }
}
