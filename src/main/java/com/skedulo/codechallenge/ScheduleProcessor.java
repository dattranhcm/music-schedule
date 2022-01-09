package com.skedulo.codechallenge;

import com.skedulo.codechallenge.dto.MusicBandSchedule;
import com.skedulo.codechallenge.dto.AvailableSlot;
import com.skedulo.codechallenge.utils.IOScheduleSupport;
import com.skedulo.codechallenge.utils.ISO8601Utils;
import lombok.SneakyThrows;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class ScheduleProcessor {
    public static List<MusicBandSchedule> finalSchedule = new ArrayList<>();
    public static List<AvailableSlot> availableSlots = new ArrayList<>();

    /**
     * Temporary allocate slot of band music schedule base on priority, will remove those slot unuse after fill the input
     */
    private static Map<Integer, ArrayList<MusicBandSchedule>> actsScheduleMap = new LinkedHashMap<>(){{
        put(10, new ArrayList<>());
        put(9, new ArrayList<>());
        put(8, new ArrayList<>());
        put(7, new ArrayList<>());
        put(6, new ArrayList<>());
        put(5, new ArrayList<>());
        put(4, new ArrayList<>());
        put(3, new ArrayList<>());
        put(2, new ArrayList<>());
        put(1, new ArrayList<>());
    }};

    @SneakyThrows
    public static void main(String[] args) {
        if(args.length == 0 ) {
            System.out.printf("Please run with an args as correct full path of input json file.");
            return;
        }
        String inputFilePath = args[0];

        Path path = Paths.get(inputFilePath);
        String fileName = IOScheduleSupport.getNameWithoutExtension(path.getFileName().toString());
        String outputDestination = path.getParent().toString();

        ArrayList<MusicBandSchedule> musicBandSchedules = IOScheduleSupport.readInputFile(path.toString());
        sortByPriority(musicBandSchedules);
        musicBandSchedules.forEach(i -> {
            ArrayList<MusicBandSchedule> ls =  actsScheduleMap.get(i.getPriority());
            ls.add(i);
            actsScheduleMap.put(i.getPriority(), ls);
        });

        while (actsScheduleMap.values().remove(Collections.EMPTY_LIST));

        actsScheduleMap.forEach((k, v) -> sortByStartTime(v));

        int highestPriorityIndex = getHighestPriorityIndex(actsScheduleMap);
        actsScheduleMap.get(highestPriorityIndex).forEach((v -> finalSchedule.add(v)));
        refreshAvailableSlot();
        processSchedule(finalSchedule);

        actsScheduleMap.forEach((k, v) -> {
            if(k == highestPriorityIndex) return;
            processSchedule(v);
        });

        IOScheduleSupport.writeResultFileActsScheduleTest(finalSchedule,
                outputDestination + "\\" + fileName + ".expected.json");
        System.out.println("Please come: " + outputDestination + "\\" + fileName + ".optimal.json.expected to see output!");
    }


    private static int getHighestPriorityIndex(Map<Integer, ArrayList<MusicBandSchedule>> actsScheduleMap)
    {
        int count = 1;
        for (Map.Entry<Integer, ArrayList<MusicBandSchedule>> entry :
                actsScheduleMap.entrySet()) {
            if (count == 1) {
                return entry.getKey();
            }
            count++;
        }
        return count;
    }

    private static void processSchedule(List<MusicBandSchedule> musicBandSchedulesSortedStartTime) {
        musicBandSchedulesSortedStartTime.forEach(a -> {
            findAvailableSlotAndFillSchedule(a);
        });
        sortByStartTime(finalSchedule);
    }

    private static void findAvailableSlotAndFillSchedule(MusicBandSchedule musicBandSchedule) {
        for(int i = 0; i < availableSlots.size(); i++) {
            AvailableSlot availableSlot = availableSlots.get(i);
            if(i == 0) {
                checkFistAvailbeRangeTime(musicBandSchedule, availableSlot);
            } else if (i == availableSlots.size() - 1) {
                checkLastVailableRangeTime(musicBandSchedule, availableSlot);
            } else {
                checkMiddleAvailableRangeTime(musicBandSchedule, availableSlot);
            }
        }
        refreshAvailableSlot();
    }
    private static void checkFistAvailbeRangeTime(MusicBandSchedule musicBandSchedule, AvailableSlot availableSlot) {
        if(musicBandSchedule.isStartTimeBefore(availableSlot.getStart())
                && musicBandSchedule.isFinishTimeBefore(availableSlot.getStart())) {
            finalSchedule.add(musicBandSchedule);
        } else if(musicBandSchedule.isStartTimeBefore(availableSlot.getStart()) &&
                musicBandSchedule.isFinishTimeAfter(availableSlot.getStart()))
        {
            MusicBandSchedule actsNew = MusicBandSchedule.builder().band(musicBandSchedule.getBand())
                    .startTime(musicBandSchedule.getStartTime())
                    .finishTime(availableSlot.getStart())
                    .start(ISO8601Utils.fromCalendar(musicBandSchedule.getStartTime()))
                    .finish(ISO8601Utils.fromCalendar(availableSlot.getStart()))
                    .priority(musicBandSchedule.getPriority()).build();
            finalSchedule.add(actsNew);
            musicBandSchedule.setStart(ISO8601Utils.fromCalendar(availableSlot.getStart()));
            musicBandSchedule.setStartTime(availableSlot.getStart());
        }
    }

    private static void checkMiddleAvailableRangeTime(MusicBandSchedule musicBandSchedule, AvailableSlot availableSlot) {
        if(musicBandSchedule.isStartTimeAfterOrEqual(availableSlot.getStart())
                && musicBandSchedule.isFinishTimeBeforeOrEqual(availableSlot.getEnd())) {
            finalSchedule.add(musicBandSchedule);
        } else if(musicBandSchedule.isStartTimeBefore(availableSlot.getStart())
                && musicBandSchedule.isFinishTimeBefore(availableSlot.getEnd())) {
            MusicBandSchedule actsNew = MusicBandSchedule.builder().band(musicBandSchedule.getBand()).startTime(availableSlot.getStart()).finishTime(musicBandSchedule.getFinishTime())
                    .start(ISO8601Utils.fromCalendar(availableSlot.getStart())).finish(ISO8601Utils.fromCalendar(musicBandSchedule.getFinishTime())).priority(musicBandSchedule.getPriority()).build();
            musicBandSchedule.setFinishTime(availableSlot.getStart());
            musicBandSchedule.setFinish(ISO8601Utils.fromCalendar(availableSlot.getStart()));
            finalSchedule.add(actsNew);
        } else if(musicBandSchedule.isStartTimeBefore(availableSlot.getStart())
                && (musicBandSchedule.isFinishTimeAfter(availableSlot.getStart())
                && musicBandSchedule.isFinishTimeBefore(availableSlot.getEnd()))) {
            MusicBandSchedule actsNew = MusicBandSchedule.builder().band(musicBandSchedule.getBand())
                    .startTime(musicBandSchedule.getStartTime())
                    .finishTime(availableSlot.getEnd())
                    .start(ISO8601Utils.fromCalendar(musicBandSchedule.getStartTime()))
                    .finish(ISO8601Utils.fromCalendar(availableSlot.getEnd()))
                    .priority(musicBandSchedule.getPriority()).build();
            musicBandSchedule.setStartTime(availableSlot.getEnd());
            musicBandSchedule.setFinish(ISO8601Utils.fromCalendar(availableSlot.getEnd()));
            finalSchedule.add(actsNew);
        } else if(musicBandSchedule.isStartTimeBeforeOrEqual(availableSlot.getStart())
                && musicBandSchedule.isFinishTimeAfterOrEqual(availableSlot.getEnd())) {
            MusicBandSchedule actsNew = MusicBandSchedule.builder().band(musicBandSchedule.getBand())
                    .startTime(availableSlot.getStart())
                    .finishTime(availableSlot.getEnd())
                    .start(ISO8601Utils.fromCalendar(availableSlot.getStart()))
                    .finish(ISO8601Utils.fromCalendar(availableSlot.getEnd()))
                    .priority(musicBandSchedule.getPriority())
                    .build();
            musicBandSchedule.setStartTime(availableSlot.getEnd());
            musicBandSchedule.setFinish(ISO8601Utils.fromCalendar(availableSlot.getEnd()));
            finalSchedule.add(actsNew);
        }
    }

    private static void checkLastVailableRangeTime(MusicBandSchedule musicBandSchedule, AvailableSlot availableSlot) {
        if(musicBandSchedule.isStartTimeAfter(availableSlot.getEnd())) {
            finalSchedule.add(musicBandSchedule);
        } else if (musicBandSchedule.isStartTimeBefore(availableSlot.getEnd()) &&
                musicBandSchedule.isFinishTimeAfter(availableSlot.getEnd())) {
            MusicBandSchedule actsNew = MusicBandSchedule.builder().band(musicBandSchedule.getBand())
                    .startTime(availableSlot.getEnd())
                    .finishTime(musicBandSchedule.getFinishTime())
                    .start(ISO8601Utils.fromCalendar(availableSlot.getEnd()))
                    .finish(ISO8601Utils.fromCalendar(musicBandSchedule.getFinishTime()))
                    .priority(musicBandSchedule.getPriority()).build();
            finalSchedule.add(actsNew);
        }
    }

    @SneakyThrows
    private static void refreshAvailableSlot() {
            availableSlots.clear();
            List<MusicBandSchedule> finalScheduleTemp = new ArrayList<>(finalSchedule);
            sortByStartTime(finalScheduleTemp);

            AvailableSlot availableSlotStart = new AvailableSlot(finalScheduleTemp.get(0).getStartTime(), null);
            availableSlots.add(availableSlotStart);

            for (int i = 0; i < finalScheduleTemp.size() - 1; i++) {
                List<MusicBandSchedule> tem = new ArrayList<>();
                tem.add(finalScheduleTemp.get(i));
                tem.add(finalScheduleTemp.get(i+1));
                sortByStartTime(tem);
                if(tem.get(0).getFinishTime().compareTo(tem.get(1).getStartTime()) != 0) {
                    AvailableSlot availableSlot = new AvailableSlot(tem.get(0).getFinishTime(), tem.get(1).getStartTime());
                    availableSlots.add(availableSlot);
                }
            }
            AvailableSlot availableSlotEnd = new AvailableSlot(null, finalScheduleTemp.get(finalScheduleTemp.size() - 1).getFinishTime());
            availableSlots.add(availableSlotEnd);
    }

    private static void sortByPriority(List<MusicBandSchedule> musicBandSchedules) {
        Collections.sort(musicBandSchedules, new Comparator<MusicBandSchedule>() {
            @SneakyThrows
            @Override
            public int compare(MusicBandSchedule o1, MusicBandSchedule o2) {
                return o2.getPriority().compareTo(o1.getPriority());
            }
        });
    }
    private static void sortByStartTime(List<MusicBandSchedule> musicBandSchedules) {
        Collections.sort(musicBandSchedules, new Comparator<MusicBandSchedule>() {
            @SneakyThrows
            @Override
            public int compare(MusicBandSchedule o1, MusicBandSchedule o2) {
                return o1.getStart().compareTo(o2.getStart());
            }
        });
    }
}