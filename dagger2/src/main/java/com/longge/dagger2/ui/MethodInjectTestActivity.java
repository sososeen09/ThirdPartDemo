package com.longge.dagger2.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.longge.dagger2.R;
import com.longge.dagger2.di.component.test.DaggerMethodTestComponent;
import com.longge.dagger2.entity.TestEntity2;

import javax.inject.Inject;

public class MethodInjectTestActivity extends AppCompatActivity {

    private TestEntity2 testEntity2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_method_inject_test);
        DaggerMethodTestComponent.create().inject(this);
        Toast.makeText(this, testEntity2 == null ? "testEntity2==null" : testEntity2.desc, Toast.LENGTH_LONG).show();

    }

    @Inject
    public void inject(TestEntity2 testEntity2) {
        this.testEntity2 = testEntity2;
    }
}
