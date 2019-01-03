package com.danikula.videocache;

import android.text.TextUtils;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * created by jw200 at 2019/1/3 17:24
 **/
public class ProxyServerWatcher {

    enum Instance {
        INSTANCE;

        ProxyServerWatcher watcher;

        Instance() {
            watcher = new ProxyServerWatcher();
        }

        public ProxyServerWatcher getInstance() {
            return watcher;
        }
    }

    private ProxyServerWatcher() {
    }

    public static ProxyServerWatcher getInstance() {
        return Instance.INSTANCE.getInstance();
    }

    LinkedHashMap<String, ThreadStatus> threadStatusMap;

    public void updateThreadStatus(String threadName, int status) {
        if (TextUtils.isEmpty(threadName)) {
            return;
        }
        if (threadStatusMap == null) {
            synchronized (this) {
                if (threadStatusMap == null) {
                    threadStatusMap = new LinkedHashMap<>();
                }
            }
        }
        synchronized (threadStatusMap) {
            ThreadStatus threadStatus = threadStatusMap.get(threadName);
            if (threadStatus == null) {
                threadStatus = new ThreadStatus();
                threadStatus.name = threadName;
                threadStatusMap.put(threadName, threadStatus);
            }
            threadStatus.status = status;
        }
    }

    public void updateThreadProgress(String threadName, Progress progress) {
        if (TextUtils.isEmpty(threadName)) {
            return;
        }
        if (threadStatusMap == null) {
            synchronized (this) {
                if (threadStatusMap == null) {
                    threadStatusMap = new LinkedHashMap<>();
                }
            }
        }
        synchronized (threadStatusMap) {
            ThreadStatus threadStatus = threadStatusMap.get(threadName);
            if (threadStatus == null) {
                threadStatus = new ThreadStatus();
                threadStatus.name = threadName;
                threadStatusMap.put(threadName, threadStatus);
            }
            if (threadStatus.progressList != null && threadStatus.progressList.size() > 0) {
                boolean merged = false;
                for (Progress p : threadStatus.progressList) {
                    if (p.equals(progress)) {
                        p.current = progress.current;
                        p.total = progress.total;
                        merged = true;
                    }
                }
                if (!merged) {
                    threadStatus.progressList.add(progress);
                }
            } else {
                if (threadStatus == null) {
                    threadStatus.progressList = new ArrayList<>();
                }
                threadStatus.progressList.add(progress);
            }
        }
    }

    public void updateSubThreadStatus(String threadName, String subThreadName, int status) {
        if (TextUtils.isEmpty(threadName) || TextUtils.isEmpty(subThreadName)) {
            return;
        }
        if (threadStatusMap == null) {
            synchronized (this) {
                if (threadStatusMap == null) {
                    threadStatusMap = new LinkedHashMap<>();
                }
            }
        }
        synchronized (threadStatusMap) {
            ThreadStatus threadStatus = threadStatusMap.get(threadName);
            if (threadStatus == null) {
                threadStatus = new ThreadStatus();
                threadStatus.name = threadName;
                threadStatusMap.put(threadName, threadStatus);
            }
            if (threadStatus.subThread != null) {
                threadStatus.subThread.name = subThreadName;
                threadStatus.subThread.status = status;
            } else {
                threadStatus.subThread = new ThreadStatus();
                threadStatus.subThread.name = subThreadName;
                threadStatus.subThread.status = status;
            }
        }
    }

    public void updateSubThreadProgress(String threadName, String subThreadName,
        Progress progress) {
        if (TextUtils.isEmpty(threadName) || TextUtils.isEmpty(subThreadName)) {
            return;
        }
        if (threadStatusMap == null) {
            synchronized (this) {
                if (threadStatusMap == null) {
                    threadStatusMap = new LinkedHashMap<>();
                }
            }
        }
        synchronized (threadStatusMap) {
            ThreadStatus threadStatus = threadStatusMap.get(threadName);
            if (threadStatus == null) {
                threadStatus = new ThreadStatus();
                threadStatus.name = threadName;
                threadStatusMap.put(threadName, threadStatus);
            }
            if (threadStatus.subThread != null) {
                threadStatus.subThread.name = subThreadName;
                if (threadStatus.subThread.progressList != null
                    && threadStatus.subThread.progressList.size() > 0) {
                    boolean merged = false;
                    for (Progress p : threadStatus.subThread.progressList) {
                        if (p.equals(progress)) {
                            p.current = progress.current;
                            p.total = progress.total;
                            merged = true;
                        }
                    }
                    if (!merged) {
                        threadStatus.subThread.progressList.add(progress);
                    }
                } else {
                    if (threadStatus == null) {
                        threadStatus.progressList = new ArrayList<>();
                    }
                    threadStatus.progressList.add(progress);
                }
            } else {
                threadStatus.subThread = new ThreadStatus();
                threadStatus.subThread.name = subThreadName;
                threadStatus.subThread.progressList = new ArrayList<>();
                threadStatus.subThread.progressList.add(progress);
            }
        }
    }

    public static class ThreadStatus {
        public static final int STATUS_DEAD = 0;
        public static final int STATUS_NEW = 1;
        public static final int STATUS_WATTING = 2;
        public static final int STATUS_BLOCKING = 3;
        public static final int STATUS_RUNNING = 4;

        public int status = STATUS_DEAD;
        public String name = "unknownThread";
        public List<Progress> progressList = null;
        public ThreadStatus subThread = null;
    }

    public static class FileThreadStatus extends ThreadStatus {
        public String filename;
    }

    public static class Progress {
        public String name;
        public int total = 0;
        public int current = 0;

        public Progress() {
        }

        public Progress(String name, int total, int current) {
            this.name = name;
            this.total = total;
            this.current = current;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null
                || !(o instanceof Progress)
                || TextUtils.isEmpty(((Progress) o).name)
                || TextUtils.isEmpty(name)) {
                return false;
            }
            return ((Progress) o).name.equals(name);
        }
    }
}
