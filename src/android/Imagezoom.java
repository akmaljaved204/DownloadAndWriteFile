package com.fortsolution.playerplugin;
 
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import android.os.Environment;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;




public class Imagezoom extends CordovaPlugin {
    public static final String ACTION_TRIGGER_ZOOM = "writeFile";
	public CallbackContext myCallback;
	
    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        try {
            if (ACTION_TRIGGER_ZOOM.equals(action)) { 
                JSONObject arg_object = args.getJSONObject(0);
				String fileUrl= arg_object.getString("fileUrl");
				String fileName= arg_object.getString("fileName");
				String dataObject= arg_object.getString("dataObject");
			   myCallback=callbackContext;
			   download(dataObject,fileUrl,fileName);
               return true;
            }
            callbackContext.error("Invalid action");
            return false;
        } catch(Exception e) {
            System.err.println("Exception: " + e.getMessage());
            callbackContext.error(e.getMessage());
            return false;
        } 
    }
	
	private void download(final String  data ,final String strFileURL,final String strFileName){
		Thread thread=new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					HttpClient httpclient = new DefaultHttpClient();		
					HttpPost httpPost = new HttpPost(strFileURL);
					StringEntity params = new StringEntity(data);
					httpPost.addHeader("content-type", "application/json");
					httpPost.setEntity(params);
					HttpResponse response = httpclient.execute(httpPost);
					HttpEntity entity = response.getEntity();
					
					InputStream input = new BufferedInputStream(entity.getContent());
					File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/FreeInoviceMaker/");
					if(!dir.exists()){
						dir.mkdirs();
					}
					File file = new File(dir, strFileName);
					OutputStream output = new FileOutputStream(file);
					int count;
					byte data[] = new byte[1024];
					while ((count = input.read(data)) != -1) {
						output.write(data, 0, count);
					}
					output.flush();
					output.close();
					input.close();
					myCallback.success(file.getAbsolutePath());
				} catch (Exception e) {
					System.out.println("Exception = "+e);
					myCallback.error("Exception = "+e.getMessage());
				}
				
			}
		});thread.start();
	}
}