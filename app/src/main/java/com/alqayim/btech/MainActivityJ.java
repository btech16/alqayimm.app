package com.alqayim.btech;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class MainActivityJ extends AppCompatActivity {

//    comment
    WebView webView;
    boolean booleanNoOk;
    String homeUrl = "https://alqayim.org/";
    String driveMst1Url = "https://drive.google.com/drive/folders/1eQQze89BJI-DmPBGbatPm62SAE4g56k6";
    String appSiteUrl = "https://sites.google.com/view/alqayimm";
    String finalUrl;

    @SuppressLint("SetJavaScriptEnabled")

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        forceRTLIfSupported();

        webView = (WebView) this.findViewById(R.id.myWebView);

        //enables javascript in webView
        webView.getSettings().setJavaScriptEnabled(true);

        //set download listener
        webView.setDownloadListener(downloadlistener);

            //loads website homepage to webView
            webView.loadUrl(homeUrl);


//        Set progress bar
            webView.setWebViewClient(new WebViewClient() {

                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    super.onPageStarted(view, url, favicon);
                    findViewById(R.id.pb_WebView).setVisibility(View.VISIBLE);
                    setTitle("جارِ تحميل الصفحة...");
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    findViewById(R.id.pb_WebView).setVisibility(View.GONE);
                    setTitle(webView.getTitle().replaceFirst("موقع معهد الدين القيم - " , ""));
                }

            });

        }

    // Download listener method
    DownloadListener downloadlistener = new DownloadListener() {

        @Override
        public void onDownloadStart(final String url, final String userAgent, String contentDisposition, String mimetype, long contentLength) {
            //Checking runtime permission for devices above Marshmallow.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED) {
                    Log.v("Error", "Permission is granted");
                    downloadDialog(url, userAgent, contentDisposition, mimetype);

                } else {

                    Log.v("Error", "Permission is revoked");
                    //requesting permissions.
                    ActivityCompat.requestPermissions(MainActivityJ.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

                }
            } else {
                //Code for devices below API 23 or Marshmallow
                Log.v("Error", "Permission is granted");
                downloadDialog(url, userAgent, contentDisposition, mimetype);

            }
        }
    };

// Download dialog method
    public void downloadDialog(final String url, final String userAgent, String contentDisposition, String mimetype) {

        //getting filename from url.
        final String filename = contentDisposition;

        //alertdialog
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivityJ.this);

        //title of alertdialog
        builder.setTitle(R.string.download);
        //message of alertdialog
        builder.setMessage(filename);
        //if Yes button clicks.
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //DownloadManager.Request created with url.
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                //cookie
                String cookie = CookieManager.getInstance().getCookie(url);
                //Add cookie and User-Agent to request
                request.addRequestHeader("Cookie", cookie);
                request.addRequestHeader("User-Agent", userAgent);
                //file scanned by MediaScannar
                request.allowScanningByMediaScanner();
                //Download is visible and its progress, after completion too.
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                //DownloadManager created
                DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                //Saving files in Download folder
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, filename);
                request.setTitle("Thes is title");
                request.setTitle("Downloadis_is");
                //download enqued
                downloadManager.enqueue(request);
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //cancel the dialog if Cancel clicks
                dialog.cancel();
            }

        });
        //alertdialog shows.
        builder.create().show();

    }


    public void onBackPressed2() {
        //alertdialog
        new AlertDialog.Builder(MainActivityJ.this)
                .setIcon(R.drawable.ic_stat_name)
                .setTitle("تأكيد الخروج")
                .setMessage("سيتم الخروج من التطبيق")
                //if Yes button clicks.
                .setPositiveButton("خروج", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNegativeButton("الغاء", null).show();
    }
    @Override
    public void onBackPressed() {

        if (booleanNoOk) {
            onBackPressed2();
        } else {
            if (webView.canGoBack())
                webView.goBack();
            else {
                booleanNoOk = true;
                if (getApplicationContext() == null) {
                    return;
                } else
                    Toast.makeText(this, "إضغط مرة أخرى للخروج من التطبيق", Toast.LENGTH_SHORT).show();

                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        booleanNoOk = false;
                    }
                }, 3000);
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void forceRTLIfSupported()
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1){
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }
    }

}