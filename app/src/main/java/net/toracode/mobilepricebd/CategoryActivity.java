package net.toracode.mobilepricebd;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.google.firebase.analytics.FirebaseAnalytics;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.toracode.mobilepricebd.adapters.RecyclerAdapter;
import net.toracode.mobilepricebd.beans.Post;
import net.toracode.mobilepricebd.commons.Commons;
import net.toracode.mobilepricebd.commons.HttpProvider;
import net.toracode.mobilepricebd.commons.ItemClickSupport;

public class CategoryActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private Button seeMoreButton;
    private ProgressDialog progressDialog;

    private int pageCount = 1;

    private List<Post> postList = new ArrayList<>();

    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (this.getSupportActionBar() != null)
            this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.recyclerView = (RecyclerView) this.findViewById(R.id.recyclerView);
        this.progressBar = (ProgressBar) this.findViewById(R.id.progressBar);
        this.seeMoreButton = (Button) this.findViewById(R.id.moreButton);
        this.progressDialog = Commons.getProgressDialog(this);
        this.mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        String brandName = getIntent().getStringExtra("brandName");

        if (brandName != null)
            this.loadData(this.buildUrl(brandName, this.pageCount));
        this.seeMoreButton.setOnClickListener(v -> this.onButtonClick(v, brandName));
    }

    private void onButtonClick(View v, String brandName) {
        this.progressDialog.show();
        this.pageCount++;
        this.loadData(this.buildUrl(brandName, this.pageCount));
        Log.i("URL", this.buildUrl(brandName, this.pageCount));
    }

    private String buildUrl(String brandName, int pageCount) {
        String baseUrl = this.getResources().getString(R.string.baseUrl);
        if (this.pageCount == 1)
            return baseUrl + brandName;
        return baseUrl + brandName + "/page/" + pageCount;
    }

    private void loadData(String url) {
        new Thread(() -> this.fetchData(url)).start();
    }

    private void fetchData(String url) {
        try {
            String response = new HttpProvider(this).fetchData(url);
            runOnUiThread(() -> onResponse(response));
        } catch (IOException e) {
            if (this.progressDialog.isShowing())
                this.progressDialog.cancel();
            Log.e("IOException", e.toString());
        }
    }

    private void onResponse(String response) {
        if (this.progressDialog.isShowing())
            this.progressDialog.cancel();

        Document doc = Jsoup.parseBodyFragment(response);
        Elements elements = doc.getElementsByClass("post-template");
        // each post <div>
        for (Element element : elements) {
            Post post = new Post();

            Elements links = element.getElementsByTag("a");
            Element thumbLink = links.get(0);
            Element detailsLink = links.get(1);

            post.setImageIconUrl(thumbLink.attr("data-hidpi"));
            post.setTitle(detailsLink.text());
            post.setDetailsUrl(detailsLink.attr("href"));
            post.setPrice(element.getElementsByTag("p").get(0).text());

            this.postList.add(post);
        }

        // set recyclerview with data
        this.setupRecyclerView(this.postList);
        // hide progressbar
        if (this.progressBar.getVisibility() == View.VISIBLE) {
            progressBar.setVisibility(View.GONE);
        }
    }

    private void setupRecyclerView(List<Post> postList) {
        this.seeMoreButton.setVisibility(View.VISIBLE);
        this.recyclerView.setVisibility(View.VISIBLE);
        this.recyclerView.setAdapter(new RecyclerAdapter(this, postList));
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        this.recyclerView.setNestedScrollingEnabled(false);
        ItemClickSupport.addTo(this.recyclerView).setOnItemClickListener((recyclerView, position, view) -> {
            this.startActivity(
                    new Intent(this, DetailsActivity.class)
                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            .putExtra("detailsUrl", this.postList.get(position).getDetailsUrl())
            );
            // log event
            Bundle bundle = new Bundle();
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, this.postList.get(position).getTitle());
            bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "Device Name");
            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home)
            this.finish();
        return super.onOptionsItemSelected(item);
    }
}
