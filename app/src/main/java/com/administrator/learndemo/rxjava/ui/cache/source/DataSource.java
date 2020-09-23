package com.administrator.learndemo.rxjava.ui.cache.source;

import com.administrator.learndemo.rxjava.ui.cache.model.Data;

import io.reactivex.Observable;

public class DataSource {
    private MemoryDataSource memoryDataSource;
    private DiskDataSource diskDataSource;
    private NetworkDataSource networkDataSource;

    public DataSource(MemoryDataSource memoryDataSource,
                      DiskDataSource diskDataSource,
                      NetworkDataSource networkDataSource) {
        this.memoryDataSource = memoryDataSource;
        this.diskDataSource = diskDataSource;
        this.networkDataSource = networkDataSource;
    }

    public Observable<Data> getDataFromMemory() {
        return memoryDataSource.getData();
    }

    public Observable<Data> getDataFromDisk() {
        return diskDataSource.getData().doOnNext(data ->
                memoryDataSource.cacheInMemory(data)
        );
    }

    public Observable<Data> getDataFromNetwork() {
        return networkDataSource.getData().doOnNext(data -> {
            diskDataSource.saveToDisk(data);
            memoryDataSource.cacheInMemory(data);
        });
    }
}
