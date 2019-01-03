package com.danikula.videocache.sample;

import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;
import com.danikula.videocache.CacheListener;
import com.danikula.videocache.HttpProxyCacheServer;
import com.danikula.videocache.ProxyCacheException;
import java.io.File;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.InstanceState;
import org.androidannotations.annotations.SeekBarTouchStop;
import org.androidannotations.annotations.ViewById;

/**
 * created by jw200 at 2018/12/29 17:01
 **/
@EFragment(R.layout.fragment_priority)
public class PriorityFragment extends Fragment implements CacheListener, View.OnClickListener {
    private final String LOG_TAG = "PriorityFragment";
    @FragmentArg String url;

    @InstanceState int position;
    @InstanceState boolean playerStarted;

    @ViewById VideoView videoView;
    @ViewById ProgressBar progressBar;
    @ViewById Button start;
    @ViewById Button breakThread;
    @ViewById TextView breakInfo;

    private final VideoProgressUpdater updater = new VideoProgressUpdater();

    public static Fragment build(String url) {
        return PriorityFragment_.builder()
            .url(url)
            .build();
    }

    @AfterViews
    void afterViewInjected() {
        startProxy();
        start.setOnClickListener(this);
        breakThread.setOnClickListener(this);
        checkCachedState();
    }

    private void startPlayer() {
        if (videoView != null) {
            if (!playerStarted) {
                videoView.seekTo(position);
                videoView.start();
                start.setText("pause");
                playerStarted = true;
            } else {
                position = videoView.getCurrentPosition();
                videoView.pause();
                start.setText("start");
                playerStarted = false;
            }
        }
    }

    private void startProxy() {
        HttpProxyCacheServer proxy = App.getProxy(getActivity());
        proxy.registerCacheListener(this, url);
        videoView.setVideoPath(proxy.getProxyUrl(url));
    }

    @Override
    public void onResume() {
        super.onResume();
        updater.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        updater.stop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        videoView.stopPlayback();
        App.getProxy(getActivity()).unregisterCacheListener(this);
    }

    @Override
    public void onCacheAvailable(File file, String url, int percentsAvailable) {
        progressBar.setSecondaryProgress(percentsAvailable);
    }

    private void updateVideoProgress() {
        int videoProgress = videoView.getCurrentPosition() * 100 / videoView.getDuration();
        progressBar.setProgress(videoProgress);
    }

    @SeekBarTouchStop(R.id.progressBar)
    void seekVideo() {
        int videoPosition = videoView.getDuration() * progressBar.getProgress() / 100;
        videoView.seekTo(videoPosition);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.start: {
                startPlayer();
            }
            break;
            case R.id.breakThread:
                HttpProxyCacheServer proxy = App.getProxy(getActivity());
                //try {
                boolean fullyCached = proxy.isCached(url);
                breakInfo.setText("fullyCached:" + fullyCached);
                //proxy.shutdownClients(url);
                //} catch (ProxyCacheException e) {
                //    e.printStackTrace();
                //}
                break;
        }
    }

    private void checkCachedState() {
        HttpProxyCacheServer proxy = App.getProxy(getActivity());
        boolean fullyCached = proxy.isCached(url);
        if (fullyCached) {
            progressBar.setSecondaryProgress(100);
        }
    }

    private final class VideoProgressUpdater extends Handler {

        public void start() {
            sendEmptyMessage(0);
        }

        public void stop() {
            removeMessages(0);
        }

        @Override
        public void handleMessage(Message msg) {
            updateVideoProgress();
            sendEmptyMessageDelayed(0, 500);
        }
    }
}
