package com.luminiadev.eventbus.api;

public interface EventPriorities {

    int FIRST = 100;

    int EARLY = 50;

    int NORMAL = 0;

    int LATE = -50;

    int LAST = -100;
}
