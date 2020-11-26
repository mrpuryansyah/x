package com.example.x;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.YouTubePlayerTracker;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

//import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivityorg extends AppCompatActivity  {

    private YouTubePlayer youTubePlayer;
    YouTubePlayerTracker tracker = new YouTubePlayerTracker();

//    @BindView(R.id.youtube_player_view) YouTubePlayerView youTubePlayerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

//        YouTubePlayerView youTubePlayerView = findViewById(R.id.youtube_player_view);
//        getLifecycle().addObserver(youTubePlayerView);

//        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
//            @Override
//            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
//                String videoId = "YIIC-e8nV7c";
//                youTubePlayer.loadVideo(videoId, 0);
//
//                youTubePlayer.addListener(tracker);
//
//                tracker.getState();
//                tracker.getCurrentSecond();
//                tracker.getVideoDuration();
//                tracker.getVideoId();
//
//                Log.e("state", tracker.getState() + "");
//                Log.e("currentsecond", tracker.getCurrentSecond() + "");
//                Log.e("videoduration", tracker.getVideoDuration() + "");
//                Log.e("videoid", tracker.getVideoId() + "");
//            }
//        });
//    }
    }

}
