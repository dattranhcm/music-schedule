package com.skedulo.codechallenge.dto;

import com.skedulo.codechallenge.utils.ISO8601Utils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Calendar;

@Data
@AllArgsConstructor
public class AvailableSlot {
    private Calendar start;
    private Calendar end;

    public AvailableSlot (Calendar start, Calendar end) {
        this.start = start;
        this.end = end;
    }

    private transient String startString;
    private transient String finishString;

    public String getStartString() {
        return ISO8601Utils.fromCalendar(this.start);
    }

    public String getFinishString() {
        return ISO8601Utils.fromCalendar(this.end);
    }

    public void setStartString() {
        this.startString = ISO8601Utils.fromCalendar(this.start);
    }

    public void setFinishString() {
        this.finishString = ISO8601Utils.fromCalendar(this.end);
    }
}
