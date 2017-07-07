package com.codepath.apps.mytwitter.adapter;

import android.content.Context;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.codepath.apps.mytwitter.R;
import com.codepath.apps.mytwitter.Utity.DateUtity;
import com.codepath.apps.mytwitter.models.Tweet;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by Admin on 7/3/2017.
 */

public class TweetAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Tweet> mTweets;
    private static final int NO_IMAGE = 0;
    private static final int HAVE_IMAGE =1;
    private Context mContext;
    private Listener mListener;
    private int indexItem = 0;
    public void setmListener(Listener mListener) {
        this.mListener = mListener;
    }

    public TweetAdapter(Context context) {
        mContext = context;
        mTweets = new ArrayList<>();
    }
    public void setData(List<Tweet> tweets) {
        mTweets.clear();
        mTweets.addAll(tweets);
        notifyDataSetChanged();
    }
    public void addData(List<Tweet> tweets) {
        int pos = mTweets.size();
        mTweets.addAll(pos,tweets);
        notifyItemRangeInserted(pos,mTweets.size());
    }

    public void updateData(Tweet tweet) {
        mTweets.set(indexItem,tweet);
        notifyItemChanged(indexItem);
    }

    @Override
    public int getItemViewType(int position) {
        Tweet tweet = mTweets.get(position);
        if (tweet.getMedia() != null) {
            return HAVE_IMAGE;
        }
        return NO_IMAGE;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == HAVE_IMAGE) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_image,parent,false);
            return new TweetImage(view);
        }
        else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_no_image,parent,false);
            return new TweetNoImage(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Tweet tweet = mTweets.get(position);
        if (holder instanceof TweetImage) {
            bindViewTweetImage((TweetImage)holder,tweet);
        }
        else if (holder instanceof TweetNoImage) {
            bindViewTweetNoImage((TweetNoImage)holder,tweet);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onClickItem(tweet);
                }

            }
        });
        if (position == mTweets.size() -1) {
            mListener.onLoadMore();
        }
    }

    private void bindViewTweetNoImage(TweetNoImage holder, Tweet tweet) {

        holder.tvContent.setText(tweet.getContent());
        holder.tvName.setText(tweet.getUser().getName());
        holder.tvNickName.setText("@"+tweet.getUser().getScreenName());
        holder.tvNumberOfLike.setText(tweet.getFavouritecount()+"");
        holder.tvNumberOfShare.setText(tweet.getRetweetcount()+"");
        holder.tvTime.setText(DateUtity.parseDate(tweet.getCreateAt()));
        holder.ibLike.setImageResource((
                tweet.isFavorite() ? R.drawable.ic_like : R.drawable.ic_no_like));
        holder.ibShare.setImageResource(tweet.isRetweet() ? R.drawable.ic_repeat : R.drawable.ic_no_repeat);
        holder.ibLike.setOnClickListener( v -> {
            indexItem = mTweets.indexOf(tweet);
            if (tweet.isFavorite()) {
                mListener.unLike(tweet.getId());
            }
            else {
                mListener.onLike(tweet.getId());
            }
        });

        holder.ibShare.setOnClickListener( v-> {
            indexItem = mTweets.indexOf(tweet);
            if (!tweet.isRetweet()) {
                mListener.onShowDialogShare(tweet);
            }
            });
        Glide.with(mContext)
                .load(tweet.getUser().getUrlAvatar())
                .bitmapTransform(new RoundedCornersTransformation(mContext,3,2))
                .into(holder.ivAvatar);

    }

    private void bindViewTweetImage(TweetImage holder, Tweet tweet) {
        holder.tvContent.setText(tweet.getContent());
        holder.tvName.setText(tweet.getUser().getName());
        holder.tvNickName.setText("@"+tweet.getUser().getScreenName());
        holder.tvNumberOfLike.setText(tweet.getFavouritecount()+"");
        holder.tvNumberOfShare.setText(tweet.getRetweetcount()+"");
        holder.tvTime.setText(DateUtity.parseDate(tweet.getCreateAt()));

        holder.ibLike.setImageResource((
                tweet.isFavorite() ? R.drawable.ic_like : R.drawable.ic_no_like));
        holder.ibShare.setImageResource(tweet.isRetweet() ? R.drawable.ic_repeat : R.drawable.ic_no_repeat);
        holder.ibLike.setOnClickListener( v -> {
            indexItem = mTweets.indexOf(tweet);
            if (tweet.isFavorite()) {
                mListener.unLike(tweet.getId());
            }
            else {
                mListener.onLike(tweet.getId());
            }
        });
        holder.ibShare.setOnClickListener( v-> {
            indexItem = mTweets.indexOf(tweet);
            mListener.onShowDialogShare(tweet);});
        Glide.with(mContext)
                .load(tweet.getUser().getUrlAvatar())
                .bitmapTransform(new RoundedCornersTransformation(mContext,3,2))
                .into(holder.ivAvatar);

        Glide.with(mContext)
                .load(tweet.getMedia().getUrl())
                .bitmapTransform(new RoundedCornersTransformation(mContext,10,2))
                .into(holder.ivIamge);
    }


    @Override
    public int getItemCount() {
        return mTweets.size();
    }

    public void addOneTweet(Tweet tweet) {
        mTweets.add(0,tweet);
        notifyItemChanged(0);
    }

    public interface Listener {
        void onClickItem(Tweet tweet);
        void onLoadMore();
        void onLike(long idTweet);
        void unLike(long idTweet);
        void onShowDialogShare(Tweet tweet);
        void onReply(long idTweet);
    }

    class TweetNoImage extends RecyclerView.ViewHolder {
        @BindView(R.id.ibLike)
        ImageButton ibLike;

        @BindView(R.id.ibReply)
        ImageButton ibReply;

        @BindView(R.id.ibShare)
        ImageButton ibShare;

        @BindView(R.id.tvContent)
        TextView tvContent;


        @BindView(R.id.tvNumberOfShare)
        TextView tvNumberOfShare;

        @BindView(R.id.tvLike)
        TextView tvNumberOfLike;

        @BindView(R.id.tvName)
        TextView tvName;

        @BindView(R.id.tvNickName)
        TextView tvNickName;

        @BindView(R.id.tvTime)
        TextView tvTime;

        @BindView(R.id.ivAvatar)
        ImageView ivAvatar;

        public TweetNoImage(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    class TweetImage extends RecyclerView.ViewHolder {
        @BindView(R.id.ibLike)
        ImageButton ibLike;

        @BindView(R.id.ibReply)
        ImageButton ibReply;

        @BindView(R.id.ibShare)
        ImageButton ibShare;

        @BindView(R.id.tvContent)
        TextView tvContent;

        @BindView(R.id.tvNumberOfShare)
        TextView tvNumberOfShare;

        @BindView(R.id.tvLike)
        TextView tvNumberOfLike;

        @BindView(R.id.tvName)
        TextView tvName;

        @BindView(R.id.tvNickName)
        TextView tvNickName;

        @BindView(R.id.tvTime)
        TextView tvTime;

        @BindView(R.id.ivAvatar)
        ImageView ivAvatar;

        @BindView(R.id.ivImage)
        ImageView ivIamge;
        public TweetImage(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
