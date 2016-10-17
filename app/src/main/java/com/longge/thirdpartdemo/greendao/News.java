package com.longge.thirdpartdemo.greendao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by long on 2016/10/17.
 */

@Entity
public class News {

    private String title;
    private String content;
    @Generated(hash = 1399441025)
    public News(String title, String content) {
        this.title = title;
        this.content = content;
    }
    @Generated(hash = 1579685679)
    public News() {
    }
    public String getTitle() {
        return this.title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getContent() {
        return this.content;
    }
    public void setContent(String content) {
        this.content = content;
    }

}
