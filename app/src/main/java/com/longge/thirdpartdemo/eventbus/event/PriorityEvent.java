package com.longge.thirdpartdemo.eventbus.event;

/**
 * Created by long on 2016/10/15.
 */

public class PriorityEvent {
    public int cancelIndex = -1;

    public PriorityEvent() {
    }

    public PriorityEvent(int cancelIndex) {
        this.cancelIndex = cancelIndex;
    }
}
