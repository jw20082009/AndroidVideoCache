package com.danikula.videocache.sample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.danikula.videocache.ProxyServerWatcher;
import java.util.List;

/**
 * created by jw200 at 2019/1/8 14:13
 **/
public class CacheWatcherFragment extends Fragment {

    ListView mWatcherList;
    WatcherAdapter mWatcherAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
        @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_cache_watcher, null, false);
        return layout;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mWatcherList = (ListView) view.findViewById(R.id.listView);
        mWatcherAdapter = new WatcherAdapter();
        mWatcherList.setAdapter(mWatcherAdapter);
    }

    private List<ProxyServerWatcher.ThreadStatus> getThreadStatusList() {
        List<ProxyServerWatcher.ThreadStatus> result = null;
        if (mWatcherAdapter != null) {
            if (ProxyServerWatcher.getInstance().isUpdated()) {
                result = ProxyServerWatcher.getInstance().getThreadStatusList();
                mWatcherAdapter.notifyDataSetChanged();
            } else {
                result = ProxyServerWatcher.getInstance().getThreadStatusList();
            }
        }
        if (result == null) {
            result = ProxyServerWatcher.getInstance().getThreadStatusList();
        }
        return result;
    }

    class WatcherAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return getThreadStatusList().size();
        }

        @Override
        public ProxyServerWatcher.ThreadStatus getItem(int position) {
            return getThreadStatusList().get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder cache = null;
            if (convertView == null) {
                convertView =
                    LayoutInflater.from(getContext()).inflate(R.layout.item_cache_watcher, null);
                cache = new ViewHolder();
                cache.tvThreadName = (TextView) convertView.findViewById(R.id.tv_thread_name);
                cache.etThreadStatus = (EditText) convertView.findViewById(R.id.et_thread_progress);
                cache.llSubThread = (LinearLayout) convertView.findViewById(R.id.ll_subthread);
                cache.tvSubThreadName = (TextView) convertView.findViewById(R.id.tv_subthread_name);
                cache.etSubThreadStatus =
                    (EditText) convertView.findViewById(R.id.et_subthread_progress);
                convertView.setTag(R.id.tag_cache_id, cache);
            } else {
                cache = (ViewHolder) convertView.getTag(R.id.tag_cache_id);
            }
            ProxyServerWatcher.ThreadStatus status = getItem(position);
            cache.tvThreadName.setText(status.name + "(" + status.status + ")");
            if (status.progressList != null && status.progressList.size() > 0) {
                StringBuilder stringBuilder = new StringBuilder();
                for (ProxyServerWatcher.Progress progress : status.progressList) {
                    stringBuilder.append(progress.name);
                    stringBuilder.append(":");
                    stringBuilder.append(progress.total);
                    stringBuilder.append("\\");
                    stringBuilder.append(progress.current);
                    stringBuilder.append("<br/>");
                }
                cache.etThreadStatus.setVisibility(View.VISIBLE);
                cache.etThreadStatus.setText(Html.fromHtml(stringBuilder.toString()));
            } else {
                cache.etThreadStatus.setVisibility(View.GONE);
            }
            if (status.subThread != null) {
                cache.llSubThread.setVisibility(View.VISIBLE);
                cache.tvSubThreadName.setText(
                    status.subThread.name + "(" + status.subThread.status + ")");
                if (status.subThread.progressList != null
                    && status.subThread.progressList.size() > 0) {
                    StringBuilder stringBuilder = new StringBuilder();
                    for (ProxyServerWatcher.Progress progress : status.subThread.progressList) {
                        stringBuilder.append(progress.name);
                        stringBuilder.append(":");
                        stringBuilder.append(progress.total);
                        stringBuilder.append("\\");
                        stringBuilder.append(progress.current);
                        stringBuilder.append("<br/>");
                    }
                    cache.etSubThreadStatus.setVisibility(View.VISIBLE);
                    cache.etSubThreadStatus.setText(Html.fromHtml(stringBuilder.toString()));
                } else {
                    cache.etSubThreadStatus.setVisibility(View.GONE);
                }
            } else {
                cache.llSubThread.setVisibility(View.GONE);
            }
            return convertView;
        }
    }

    class ViewHolder {
        TextView tvThreadName;
        EditText etThreadStatus;
        LinearLayout llSubThread;
        TextView tvSubThreadName;
        EditText etSubThreadStatus;
    }
}
