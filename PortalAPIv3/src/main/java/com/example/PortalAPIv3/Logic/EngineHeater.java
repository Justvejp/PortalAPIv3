package com.example.PortalAPIv3.Logic;

import com.fasterxml.jackson.databind.JsonNode;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.*;

public class EngineHeater {

    public String devUI;
    public JsonNode jn;
    List<String> times = new ArrayList<>();
    List<HeaterTime> heaterTimes = new ArrayList<>();

    public int currentTemp;


    public TimerTask checkTask;

    public EngineHeater(String devUI, JsonNode jn) {
        this.devUI = devUI;
        this.jn = jn;
        addTimesToList();
        parseTimesFromList();
        startTasks();
    }

    public void parseTimesFromList() {
        String dayString;
        int year = 0;
        int month = 0;
        int weekOfYear = 0;
        int hour = 0;
        int min = 0;

        for (int i = 0; i < times.size(); i++) {
            String trimS = times.get(i).substring(1, times.get(i).length()-4);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            LocalDateTime dateTime = LocalDateTime.parse(trimS, formatter);

            weekOfYear = dateTime.get(WeekFields.ISO.weekOfYear());
            year = Integer.parseInt(times.get(i).substring(1,5));
            month = Integer.parseInt(times.get(i).substring(6,8));
            dayString = String.valueOf(dateTime.getDayOfWeek());
            hour = Integer.parseInt(times.get(i).substring(12,14));
            min = Integer.parseInt(times.get(i).substring(15,17));

            HeaterTime ht = new HeaterTime(year, month, weekOfYear, dayString, hour, min);
            heaterTimes.add(ht);
        }
    }
    public String getDevUI() {
        return this.devUI;
    }

    public void startTasks() {
        for (int i = 0; i < heaterTimes.size(); i++) {
            checkCurrentTempTask(heaterTimes.get(i));
        }
    }

    public void checkCurrentTempTask(HeaterTime ht) {
        //Skapa upp en heatertime som är time now och jämför mot.
        //return om datumet redan är pass

        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.WEEK_OF_YEAR, ht.getWeekOfYear());
        calendar.set(Calendar.YEAR, ht.getYear());
        calendar.set(Calendar.MONTH, ht.getMonth());
        calendar.set(Calendar.DAY_OF_WEEK, ht.getDay());
        calendar.set(Calendar.HOUR_OF_DAY, ht.getHour()-3);
        calendar.set(Calendar.MINUTE, ht.getMinute());
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        Timer timer = new Timer();
        timer.schedule(runTimes(ht), calendar.getTime());

        //timer global sen cancel??
        //om tid redan varit, starta inte task
    }
    public Calendar calculateStartTime(HeaterTime ht) {
        Calendar calendar = Calendar.getInstance();
        int minutesEarlier = 0;
        int hoursEarlier = 0;

        //om de är 10 grader + så skicka inget start alls.

        if (currentTemp >= -5 && currentTemp <= 0) {
            hoursEarlier = 1;
            minutesEarlier = 30;

        } else if (currentTemp >= -10 && currentTemp < -5) {
            hoursEarlier = 2;

        } else if (currentTemp >= -15 && currentTemp < -10) {
            hoursEarlier = 2;
            minutesEarlier = 30;

        } else if (currentTemp <= -15) {
            hoursEarlier = 3;
        }

        calendar.set(Calendar.WEEK_OF_YEAR, ht.getWeekOfYear());
        calendar.set(Calendar.YEAR, ht.getYear());
        calendar.set(Calendar.MONTH, ht.getMonth());
        calendar.set(Calendar.DAY_OF_WEEK, ht.getDay());
        calendar.set(Calendar.HOUR_OF_DAY, ht.getHour()-hoursEarlier);
        calendar.set(Calendar.MINUTE, ht.getMinute()-minutesEarlier);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar;
    }

    public TimerTask runTimes(HeaterTime ht) {
        checkTask = new TimerTask() {
            @Override
            public void run() {
                System.out.println("API anrop VILL HA TEMP, " + "DevUI: " + devUI);

                currentTemp = -10;
                //API anrop med riktig temp, sätt temp till responsen från api
                //om tempen är 10+ starta inte all, dvs cancel timer nedanför

                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        System.out.println("skicka nerlänk START " + " DevUI: " + devUI);
                    }
                }, calculateStartTime(ht).getTime());
            }
        };
        return checkTask;
    }

    public void addTimesToList() {
        if (jn.get("monTimeActive").toString().equals("true")) {
            times.add(jn.get("monEndTime").toString());
        }
        if (jn.get("tueTimeActive").toString().equals("true")) {
            times.add(jn.get("tueEndTime").toString());
        }
        if (jn.get("wedTimeActive").toString().equals("true")) {
            times.add(jn.get("wedEndTime").toString());
        }
        if (jn.get("thuTimeActive").toString().equals("true")) {
            times.add(jn.get("thuEndTime").toString());
        }
        if (jn.get("friTimeActive").toString().equals("true")) {
            times.add(jn.get("friEndTime").toString());
        }
        if (jn.get("satTimeActive").toString().equals("true")) {
            times.add(jn.get("satEndTime").toString());
        }
        if (jn.get("sunTimeActive").toString().equals("true")) {
            times.add(jn.get("sunEndTime").toString());
        }
    }
}
