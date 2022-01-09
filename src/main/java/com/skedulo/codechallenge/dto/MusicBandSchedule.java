package com.skedulo.codechallenge.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.gson.annotations.Expose;
import com.skedulo.codechallenge.utils.ISO8601Utils;
import lombok.*;

import java.util.Calendar;
import java.util.Objects;

@Data
@Builder
@JsonDeserialize
public class MusicBandSchedule {
    @Expose
    private String band;

    @Expose
    private String start;

    @Expose
    private String finish;

    private Integer priority;

    @JsonIgnore
    private transient Calendar startTime;
    @JsonIgnore
    private transient Calendar finishTime;

    @SneakyThrows
    public Calendar getStartTime() {
        if(Objects.isNull(this.startTime)) {
            this.startTime = ISO8601Utils.toCalendar(this.start);
        }
        return this.startTime;
    }

    @SneakyThrows
    public Calendar getFinishTime() {
        if(Objects.isNull(this.finishTime)) {
            this.finishTime = ISO8601Utils.toCalendar(this.finish);
        }
        return this.finishTime;
    }

    public boolean isStartTimeBefore(Calendar compareDate) {
       return this.getStartTime().compareTo(compareDate)  < 0;
    }

    public boolean isStartTimeBeforeOrEqual(Calendar compareDate) {
        return this.getStartTime().compareTo(compareDate)  <= 0;
    }

    public boolean isStartTimeAfter(Calendar compareDate) {
        return this.getStartTime().compareTo(compareDate)  > 0;
    }

    public boolean isStartTimeAfterOrEqual(Calendar compareDate) {
        return this.getStartTime().compareTo(compareDate)  >= 0;
    }

    public boolean isFinishTimeBefore(Calendar compareDate) {
        return this.getFinishTime().compareTo(compareDate)  < 0;
    }

    public boolean isFinishTimeBeforeOrEqual(Calendar compareDate) {
        return this.getFinishTime().compareTo(compareDate)  <= 0;
    }

    public boolean isFinishTimeAfter(Calendar compareDate) {
        return this.getFinishTime().compareTo(compareDate)  > 0;
    }

    public boolean isFinishTimeAfterOrEqual(Calendar compareDate) {
        return this.getFinishTime().compareTo(compareDate)  >= 0;
    }
}
