package com.alqayim.btech

import android.Manifest
import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_SEND
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.KeyEvent
import android.view.View
import android.view.View.VISIBLE
import android.webkit.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
/*
import com.google.firebase.iid.FirebaseInstanceId
*/
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    var share_url: String? = null
    private var url = "https://www.google.com"
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        myWebView.setWebViewClient(WebViewClient())
        myWebView.loadUrl(url)
        //javascript enabled.
        myWebView.webViewClient = WebViewClient()
        myWebView.settings.javaScriptEnabled = true
        myWebView.settings.javaScriptCanOpenWindowsAutomatically

        FirebaseMessaging.getInstance().isAutoInitEnabled = true
        /*
        notification()
        */

        //Download Listener
        myWebView.setDownloadListener { url, userAgent, contentDisposition, mimetype, contentLength ->
            //checking Runtime permissions
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    //Do this, if permission granted
                    downloadDialog(url, userAgent, contentDisposition, mimetype)

                } else {

                    //Do this, if there is no permission
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        1
                    )

                }
            } else {
                //Code for devices below API 23 or Marshmallow
                downloadDialog(url, userAgent, contentDisposition, mimetype)

            }
        }


    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {

        //navigate back with back button
        if (keyCode == KeyEvent.KEYCODE_BACK && this.myWebView.canGoBack()) {
            myWebView.goBack()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    fun goBack(view: View) {
        //go previous web page
        if (myWebView.canGoBack())
            myWebView.goBack()
    }

    fun goForward(view: View) {

        if (myWebView.canGoForward())
            myWebView.goForward()
    }

    fun goHome(view: View) {
        //load demo home page.
        myWebView.loadUrl("https://sites.google.com/view/alqayimm/")
    }

    fun refresh(view: View) {
        //reload webpage.
        myWebView.reload()
    }

    fun share(view: View) {
        //Share url code.
        val shareIntent = Intent()
        shareIntent.action = ACTION_SEND
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, share_url)
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "URL")
        startActivity(Intent.createChooser(shareIntent, "مشاركة الرابط"))

    }

    private fun downloadDialog(
        url: String,
        userAgent: String,
        contentDisposition: String,
        mimetype: String
    ) {
        //getting file name from ur
        val filename = URLUtil.guessFileName(url, null  , mimetype)
        //Alertdialog
        val builder = AlertDialog.Builder(this@MainActivity)
        //title for AlertDialog
        builder.setTitle("تأكيد تحميل الملف")
        //message of AlertDialog
        //builder.setMessage("سيتم تحميل الملف باسم\n $filename")
        //if YES button clicks
        builder.setPositiveButton("تحميل") { dialog, which ->
            //DownloadManager.Request created with url.
            val request = DownloadManager.Request(Uri.parse(url))
            //cookie
            val cookie = CookieManager.getInstance().getCookie(url)
            //Add cookie and User-Agent to request
            request.addRequestHeader("Cookie", cookie)
            request.addRequestHeader("User-Agent", userAgent)
            //Download is visible and its progress, after completion too.
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            //DownloadManager created
            val downloadmanager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            //Saving file in Download folder
            request.setDestinationInExternalPublicDir(
                Environment.DIRECTORY_DOWNLOADS,
                "معهد الدين القيم/$filename"
            )
            //download enqued
            downloadmanager.enqueue(request)
        }
        //If Cancel button clicks
        builder.setNegativeButton("إلغاء")
        { dialog, which ->
            //cancel the dialog if Cancel clicks
            dialog.cancel()
        }

        val dialog: AlertDialog = builder.create()
        //shows alertdialog
        dialog.show()

    }

    override fun onBackPressed() {
        AlertDialog.Builder(this)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setTitle("تأكيد إغلاق التطبيق")
            .setMessage("هل تريد الخروج من التطبيق ؟")
            .setPositiveButton(
                "نعم"
            ) { dialog, which -> finish() }
            .setNegativeButton("لا", null)
            .show()
    }
/*
    private fun notification() {
        FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener {
            it.result?.token?.let {
                println("This is token: ${it}")

            }
        }
    }
*/

}

