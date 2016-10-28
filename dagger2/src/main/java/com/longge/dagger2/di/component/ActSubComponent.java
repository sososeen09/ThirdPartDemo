package com.longge.dagger2.di.component;

import com.longge.dagger2.di.scope.PerActivity;
import com.longge.dagger2.ui.SubFragment;

import dagger.Subcomponent;

/**
 * Created by suyunlong on 2016/10/27.
 */

//@com.longge.dagger2.di.scope.SubFragment
@Subcomponent
@PerActivity //如果是包含的方式，作用域与上一层的Component相同也没关系。采用依赖的方式就不行。
public interface ActSubComponent {
    void inject(SubFragment subFragment);
}
