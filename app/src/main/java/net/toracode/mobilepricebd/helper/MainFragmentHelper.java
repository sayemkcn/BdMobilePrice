package net.toracode.mobilepricebd.helper;

import android.app.Activity;
import android.app.ProgressDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.toracode.mobilepricebd.R;
import net.toracode.mobilepricebd.adapters.RecyclerAdapter;
import net.toracode.mobilepricebd.beans.Post;
import net.toracode.mobilepricebd.commons.Commons;
import net.toracode.mobilepricebd.commons.HttpProvider;

/**
 * Created by sayemkcn on 10/2/16.
 */
public class MainFragmentHelper {


    private final Activity context;
    private final int pageNumber;
    private final RecyclerView recyclerView;
    private ProgressBar progressBar;
    private Button seeMoreButton;

    List<Post> postList = new ArrayList<>();

    private int pageCount = 1;

    private ProgressDialog progressDialog;

    public MainFragmentHelper(Activity context, View rootView, int pageNumber) {
        this.context = context;
        this.pageNumber = pageNumber;
        this.recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        this.progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        this.seeMoreButton = (Button) rootView.findViewById(R.id.moreButton);
        this.progressDialog = Commons.getProgressDialog(context);
    }

    public void exec() {
        this.loadData(this.buildUrl(this.pageNumber - 1));
        this.seeMoreButton.setOnClickListener(v -> onButtonClick(v));
    }

    private void onButtonClick(View v) {
        this.progressDialog.show();
        this.pageCount++;
        this.loadData(this.buildUrl(this.pageNumber - 1));
        Log.i("URL", this.buildUrl(this.pageNumber - 1));
    }

    private String buildUrl(int pageNumber) {
        String baseUrl = context.getResources().getString(R.string.baseUrl);
        String[] brandNames = context.getResources().getStringArray(R.array.brand_names);
        if (this.pageCount == 1)
            return baseUrl + brandNames[pageNumber].toLowerCase();
        return baseUrl + brandNames[pageNumber].toLowerCase() + "/page/" + this.pageCount;
    }

    private void loadData(String url) {
        new Thread(() -> this.fetchData(url)).start();
    }

    private void fetchData(String url) {
        try {
            String response = new HttpProvider(this.context).fetchData(url);
            context.runOnUiThread(() -> onResponse(response));
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
        this.recyclerView.setAdapter(new RecyclerAdapter(this.context, postList));
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this.context));
        this.recyclerView.setNestedScrollingEnabled(false);
    }
}
