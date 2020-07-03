package com.administrator.learndemo.dagger;

import dagger.Module;
import dagger.Provides;

@Module
public class CommonModule {

    private ICommonView iView;
    private String iName;

    public CommonModule(ICommonView iView, String iName) {
        this.iView = iView;
        this.iName = iName;
    }

    @Provides
    public ICommonView provideIcommonView() {
        return this.iView;
    }

    @Provides
    public String provideName() {
        return this.iName;
    }
}
