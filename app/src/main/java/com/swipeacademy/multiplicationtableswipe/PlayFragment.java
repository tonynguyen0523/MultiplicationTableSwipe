package com.swipeacademy.multiplicationtableswipe;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.swipeacademy.multiplicationtableswipe.Util.OnSwipeTouchListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class PlayFragment extends Fragment {

    @BindView(R.id.swipe_view)
    View mSwipeView;

    private Unbinder unbinder;

    public PlayFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_play, container, false);
        unbinder = ButterKnife.bind(this, view);

        mSwipeView.setOnTouchListener(new OnSwipeTouchListener(getContext()) {

            @Override
            public void onSwipeTop() {
                super.onSwipeTop();
                nextQuestion();
                Toast.makeText(getContext(), "top", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSwipeRight() {
                super.onSwipeRight();
                nextQuestion();
                Toast.makeText(getContext(), "right", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSwipeLeft() {
                super.onSwipeLeft();
                nextQuestion();
                Toast.makeText(getContext(), "left", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSwipeBottom() {
                super.onSwipeBottom();
                nextQuestion();
                Toast.makeText(getContext(), "bottom", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void nextQuestion() {

        int mCurrentScore = Utility.getCurrentScore(getContext());
        int mRemainingQuestions = Utility.getRemainingQuestions(getContext());

        mCurrentScore++;
        mRemainingQuestions--;

        Utility.setCurrentScore(getContext(), mCurrentScore);
        Utility.setRemainingQuestions(getContext(), mRemainingQuestions);

        if (mRemainingQuestions <= 0) {
            Intent intent = new Intent(getActivity(), HomeActivity.class);
            Utility.setCurrentScore(getContext(), 0);
            Utility.setRemainingQuestions(getContext(), 10);
            startActivity(intent);
        } else {
            ((PlayActivity) getActivity()).updateNumbers(mCurrentScore, mRemainingQuestions);
        }
    }


}
