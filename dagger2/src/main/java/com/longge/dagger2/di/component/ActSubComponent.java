package com.longge.dagger2.di.component;

import com.longge.dagger2.ui.SubFragment;

import dagger.Subcomponent;

/**
 * Created by suyunlong on 2016/10/27.
 */

//@com.longge.dagger2.di.scope.SubFragment
@Subcomponent
public interface ActSubComponent {
    void inject(SubFragment subFragment);
}
