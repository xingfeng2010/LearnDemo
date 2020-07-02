package com.administrator.learndemo.dagger;

import dagger.Module;
import dagger.Provides;

@Module
public class CommonModule {

    private ICommonView iView;

    public CommonModule(ICommonView iView) {
        this.iView = iView;
    }

    @Provides
    public ICommonView provideIcommonView() {
        return this.iView;
    }
}
