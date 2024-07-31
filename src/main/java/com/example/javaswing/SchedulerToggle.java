package com.example.javaswing;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class SchedulerToggle {
    private boolean isAutoRunEnabled = true;
    private LocalDateTime lastRunTime;

    public boolean isAutoRunEnabled(){
        return isAutoRunEnabled;
    }

    public void setAutoRunEnabled(boolean autoRunEnabled) {
        this.isAutoRunEnabled = autoRunEnabled;
    }

    public LocalDateTime getLastRunTime() {
        return lastRunTime;
    }

    public void setLastRunTime(LocalDateTime lastRunTime) {
        this.lastRunTime = lastRunTime;
    }
}
