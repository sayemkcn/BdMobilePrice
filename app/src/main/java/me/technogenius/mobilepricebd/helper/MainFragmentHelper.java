package me.technogenius.mobilepricebd.helper;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.technogenius.mobilepricebd.R;
import me.technogenius.mobilepricebd.adapters.RecyclerAdapter;
import me.technogenius.mobilepricebd.beans.Post;
import me.technogenius.mobilepricebd.commons.HttpProvider;

/**
 * Created by sayemkcn on 10/2/16.
 */
public class MainFragmentHelper {


    private final Activity context;
    private final int pageNumber;
    private final RecyclerView recyclerView;

    public MainFragmentHelper(Activity context, View rootView, int pageNumber) {
        this.context = context;
        this.pageNumber = pageNumber;
        this.recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
    }

    public void exec() {

        String url = "http://www.mobiledokan.com/samsung/";
        new Thread(() -> this.fetchData(url)).start();

    }

    private void fetchData(String url) {
        try {
            String response = new HttpProvider(this.context).fetchData(url);
            context.runOnUiThread(() -> onResponse(response));
        } catch (IOException e) {
            Log.e("IOException", e.toString());
        }
    }

    private void onResponse(String response) {
        List<Post> postList = new ArrayList<>();

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

            postList.add(post);
        }

        this.setupRecyclerView(postList);
    }

    private void setupRecyclerView(List<Post> postList) {
        this.recyclerView.setAdapter(new RecyclerAdapter(this.context,postList));
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this.context));
    }
}
