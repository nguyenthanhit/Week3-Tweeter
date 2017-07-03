package com.codepath.apps.mytwitter.fragment;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.mytwitter.R;
import com.codepath.apps.mytwitter.models.User;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Admin on 7/3/2017.
 */

public class PostTweetDialogFragment extends DialogFragment {
    @BindView(R.id.ibClose)
    ImageButton btnClose;

    @BindView(R.id.etContent)
    EditText mContent;

    @BindView(R.id.btnTweet)
    Button btnTweet;

    @BindView(R.id.tvNickName)
    TextView tvNick;

    @BindView(R.id.tvName)
    TextView tvName;

    @BindView(R.id.ivAvatar2)
    ImageView mAvartar;

    @BindView(R.id.tvNumberText)
    TextView tvNumberText;

    private  User user;

    public static PostTweetDialogFragment newInstance() {
        Bundle args = new Bundle();
        PostTweetDialogFragment  fragment= new PostTweetDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dialog_tweet,container,false);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
//        Dialog dialog = getDialog();
//        if (dialog != null) {
//            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
//            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        }
    }

}
