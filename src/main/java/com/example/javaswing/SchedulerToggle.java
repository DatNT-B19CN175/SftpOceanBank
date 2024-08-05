package com.example.javaswing;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class SchedulerToggle {
    private boolean isAutoRunEnabled = false;
    private LocalDateTime lastRunTime;

    public boolean isAutoRunEnabled(){
        return isAutoRunEnabled;
    }

    //Setter
    public void setAutoRunEnabled(boolean autoRunEnabled) {
        this.isAutoRunEnabled = autoRunEnabled;
    }

    public LocalDateTime getLastRunTime() {
        return lastRunTime;
    }

    //Setter
    public void setLastRunTime(LocalDateTime lastRunTime) {
        this.lastRunTime = lastRunTime;
    }
}
