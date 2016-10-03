package net.toracode.mobilepricebd.adapters;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import net.toracode.mobilepricebd.DetailsActivity;
import net.toracode.mobilepricebd.R;
import net.toracode.mobilepricebd.beans.Post;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {
    private LayoutInflater inflater;
    private List<Post> postList;
    private Activity context;

    public RecyclerAdapter(Activity context, List<Post> postList) {
        this.inflater = LayoutInflater.from(context);
        this.postList = postList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View view = inflater.inflate(R.layout.single_recycler_item, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder myViewHolder, int position) {
        Post post = this.postList.get(position);
        Glide.with(context).load(post.getImageIconUrl()).placeholder(R.mipmap.ic_launcher).diskCacheStrategy(DiskCacheStrategy.ALL).into(myViewHolder.thumbView);
        myViewHolder.titleView.setText(post.getTitle());
        myViewHolder.priceView.setText(post.getPrice());
    }

    @Override
    public int getItemCount() {
        return this.postList.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView thumbView;
        TextView titleView;
        TextView priceView;


        public MyViewHolder(View itemView) {
            super(itemView);
            thumbView = (ImageView) itemView.findViewById(R.id.thumbView);
            titleView = (TextView) itemView.findViewById(R.id.titleView);
            priceView = (TextView) itemView.findViewById(R.id.priceView);

//            Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/SolaimanLipi.ttf");
//            titleTextView.setTypeface(typeface);
//            summaryTextView.setTypeface(typeface);
//            sourceNameTextView.setTypeface(typeface);
//            newsTimeTextView.setTypeface(typeface);

//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    context.startActivity(
//                            new Intent(context, DetailsActivity.class)
//                                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                                    .putExtra("detailsUrl", postList.get(getAdapterPosition()).getDetailsUrl())
//                    );
//                }
//            });

        }
    }


}