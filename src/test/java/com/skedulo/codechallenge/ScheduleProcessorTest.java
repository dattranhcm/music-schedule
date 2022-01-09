package com.skedulo.codechallenge;

import com.skedulo.codechallenge.dto.MusicBandSchedule;
import com.skedulo.codechallenge.utils.IOScheduleSupport;
import org.junit.Test;
import org.junit.*;
import org.junit.jupiter.api.BeforeEach;

import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * Unit test for ScheduleProcessor
 */
public class ScheduleProcessorTest
{
    ClassLoader loader;
    ScheduleProcessor scheduleProcessor;
    @Before
    public void setUp() {
        loader = ClassLoader.getSystemClassLoader();
    }

    @BeforeEach
    public void setUpEachTest() {
        scheduleProcessor = new ScheduleProcessor();
    }

    /**
     * The test case just verify after scheduling, each music band schedule will be in order by time and not violent each others
     * @throws Exception
     */
    @Test
    public void testOverLapTimeComplexCase() throws Exception {
        String urlInput = Paths.get(loader.getResource("verify/overlap_time_case.json").toURI()).toString();
        String[] args = new String[] {urlInput};
        scheduleProcessor.main(args);
        String urlExpectedSchedule = Paths.get(loader.getResource("verify/overlap_time_case.expected.json").toURI()).toString();
        ArrayList<MusicBandSchedule> bandSchedules = IOScheduleSupport.readInputFile(urlExpectedSchedule);
        for(int i = 0; i < bandSchedules.size() - 1; i ++) {
            Assert.assertTrue(bandSchedules.get(i).isStartTimeBefore(bandSchedules.get(i+1).getStartTime()));
            Assert.assertTrue(bandSchedules.get(i).isFinishTimeBeforeOrEqual(bandSchedules.get(i+1).getStartTime()));
        }
    }
}
