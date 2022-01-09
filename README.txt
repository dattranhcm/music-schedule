Verifies that a submitted music scheduling program produces the expected JSON given an input JSON file.

The verifier will execute the provided command multiple times, passing in a JSON file each time and diffing
the output against a corresponding "<input file name>.expected.json" file.

I. Environment Requisites:

- Window OS (run bat file)

- Java 8 or higher version

- Maven 3.8.1

II. Approach Algorithm Solution

Assume the input json is standard and no need to verify it.

Note 1: The key idea here is, we will find available range time (the time is not violated any music band schedule),
and based on it to fill the music band schedule in order priority and start time.

Note 2:
- a 'MusicBandSchedule' is an item like that:
    {
       "band" : "Soundgarden",
       "start" : "1993-05-25T02:00:00Z",
       "finish" : "1993-05-25T02:50:00Z",
       "priority" : 5
     }
 - 'available range time': can be the free time before start time of first MusicBandSchedule,
    or after the last finish time of MusicBandSchedule, or the period time between a finish time of a MusicBandSchedule and the start time of next MusicBandSchedule
Basicly the process like below:
1. Read input json file and convert it to the list as MusicBandSchedule
2. Filter each MusicBandSchedule by it`s priority and put them into their separate priority level list.
    Example: all MusicBandSchedule has same highest priority 10 in the separate list, and the same for others priority level
3. Scheduling from highest priority to lowest priority MusicBandSchedule list
    3.1 Pick the highest priority list and process schedule on it first, and then mark it as the first standard schedule to follow.
    3.2 After the scheduled highest priority list, we compute again available range time list to preparing work on the next lower priority list.
    3.3 Repeat steps 3.1 and 3.2 with the next priority list until the last lowest priority music band schedule.

III. Usage:
1. cd to the directory where you want to run the program (e.g. run.bat)
2. Execute:

   2.1 Build music-schedule project: cd <path_to_verifier> verify-music-build.bat
        - For example:
            - cd ../music-schedule/verifier
            - verify-music-build.bat

   2.2 Run project by /music-schedule/verifier/run.bat file with one argument as input path
        - For example:
            - cd ../music-schedule/verifier
            - run.bat F:\music-schedule\verifier\input.json
            - and check the result as the same input path with name: input.expected.json
IV. Enhancement
- Handle string date to ISO8601 directly when parsing from json is better
- Handle multiple timezone now it handle just ISO8601 yyyy-MM-dd'T'HH:mm:ss'Z' (+00:00) for standard cases.
