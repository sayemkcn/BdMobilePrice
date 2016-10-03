package net.toracode.mobilepricebd;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

import net.toracode.mobilepricebd.commons.HttpProvider;

public class DetailsActivity extends AppCompatActivity {
    private WebView webView;
    private TextView titleView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // init progressbar
        this.progressBar = (ProgressBar) this.findViewById(R.id.progressBar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        this.titleView = (TextView) this.findViewById(R.id.titleTextView);
        this.webView = (WebView) this.findViewById(R.id.detailsWebView);
        WebSettings webSetting = webView.getSettings();
        if (webSetting != null) {
            webSetting.setJavaScriptCanOpenWindowsAutomatically(true);
            webSetting.setJavaScriptEnabled(true);
            webSetting.setAppCacheEnabled(true);
            webSetting.setLoadWithOverviewMode(true);
        }


        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                final String response = new HttpProvider(DetailsActivity.this).fetchData(getIntent().getStringExtra("detailsUrl"));
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            onResponse(response);
                        }
                    });
            } catch (IOException e) {
                Log.e("IOException", e.getMessage());
            }
            }
        }).start();

    }

    private void onResponse(String response) {
        if (this.progressBar.getVisibility() == View.VISIBLE) {
            progressBar.setVisibility(View.GONE);
            this.webView.setVisibility(View.VISIBLE);
        }
        String[] parsedHtmlArray = this.parseHtml(response);
        this.titleView.setText(parsedHtmlArray[0]);
        this.webView.loadDataWithBaseURL("", parsedHtmlArray[1].replace("MobileDokan","").replace("Store",""), "text/html", "utf-8", " ");
    }

    private String[] parseHtml(String response) {
        Document doc = Jsoup.parseBodyFragment(response);
        Element body = doc.getElementById("content-parent");
        String titleText = body.getElementsByClass("post-title").get(0).text();
        Element content = body.getElementsByTag("article").get(0);

        return new String[]{titleText, content.toString()};
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home)
            this.finish();
        return super.onOptionsItemSelected(item);
    }
}
