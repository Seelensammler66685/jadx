package ua.naiksoftware.jadx;

import android.app.ProgressDialog;
import android.content.*;
import android.os.*;
import android.widget.*;

public class Decompiler extends AsyncTask<String, String, Boolean> {

    private Context context;
    private ProgressDialog dialog;

    public Decompiler(Context context) {
        this.context = context;
    }

    //update progress in feature
    public void update(int add) {
        //Log.d(tag, "update: " + add);
        //publishProgress(String.valueOf(add));
    }

    @Override
    protected Boolean doInBackground(String[] p1) {
        final String strInput = p1[0];
        final String strOutput = p1[1];

        jadx.Main.main(new String[]{"-d", strOutput, strInput});

        return true;
    }

    //@Override
    //public void onProgressUpdate(String... s) {
    //    dialog.incrementProgressBy(Integer.parseInt(s[0]));
    //}
    
    @Override
    public void onPreExecute() {
        //Log.i(tag, "onPreExecute");
        dialog = new ProgressDialog(context);
        dialog.setIndeterminate(true);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setCancelable(false);
        dialog.setTitle(R.string.decompiling);
        dialog.setMessage(context.getResources().getString(R.string.wait));
        dialog.show();
    }

    @Override
    public void onPostExecute(Boolean result) {
        //Log.i("Installator", "onPostExecute with: " + result);
        if (result) {
            dialog.dismiss();
            Toast t = Toast.makeText(context, context.getResources().getString(R.string.decompile_complete), Toast.LENGTH_LONG);
            t.show();
        } else {
            dialog.dismiss();
            Toast t = Toast.makeText(context, "Err in Decompiler", Toast.LENGTH_LONG);
            t.show();
        }
        //Log.d(tag, "Installator", "onPostExecuted");
    }
}
