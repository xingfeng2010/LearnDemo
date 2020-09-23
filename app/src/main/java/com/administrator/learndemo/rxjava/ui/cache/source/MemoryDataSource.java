package com.administrator.learndemo.rxjava.ui.cache.source;

import com.administrator.learndemo.rxjava.ui.cache.model.Data;

import io.reactivex.Observable;

public class MemoryDataSource {
    private Data data;

    public Observable<Data> getData() {
        return Observable.create(emitter -> {
            if (data != null) {
                emitter.onNext(data);
            }

            emitter.onComplete();
        });
    }

    public void cacheInMemory(Data data) {
        this.data = data.clone();
        this.data.source = "memory";
    }
}
