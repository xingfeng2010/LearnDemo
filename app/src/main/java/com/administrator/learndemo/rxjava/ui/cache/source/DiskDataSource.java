package com.administrator.learndemo.rxjava.ui.cache.source;

import io.reactivex.Observable;
import com.administrator.learndemo.rxjava.ui.cache.model.Data;

public class DiskDataSource {
    private Data data;

    public Observable<Data> getData() {
        return Observable.create(emitter -> {
            if (data != null) {
                emitter.onNext(data);
            }

            emitter.onComplete();
        });
    }

    public void saveToDisk(Data data) {
        this.data = data.clone();
        this.data.source = "disk";
    }
}
