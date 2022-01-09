Verifies that a submitted music scheduling program produces the expected JSON given an input JSON file.

The verifier will execute the provided command multiple times, passing in a JSON file each time and diffing
the output against a corresponding "<input file name>.expected.json" file.

I. Usage:
1. cd to the directory where you want to run the program (e.g. run.bat)
2. Execute:

   2.1 Build music-schedule project: cd <path_to_verifier> verify-music-build.bat
        - For example:
            - cd ../music-schedule/verifier
            - verify-music-build.bat

   2.2 Run project by /music-schedule/verifier/run.bat file with one argument as input path
        - For example:
            - cd ../music-schedule/verifier
            - run.bat

II. Approach algorithm solution

Assume the input json is standard and no need to verify it.

Note: The key idea here is, we will find available range time (the time is not violent any music band schedule),
and based on it to fill the music band schedule in order priority and start time.

1. Read input json file and convert it to the list as MusicBandSchedule
2. Filter each MusicBandSchedule by its priority and put them into their separate priority level list.
3. Scheduling from highest priority to lowest priority
    3.1 Put the highest priority list and process schedule on it first, and then mark it as the first standard schedule to follow.
    3.2 After the scheduled highest priority list, we compute again available range time to prepare workers with the next lower priority list.
    3.3 Traverse steps 3.1 and 3.2 with the next priority list until the last lowest priority music band schedule.
