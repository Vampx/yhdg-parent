package cn.com.yusong.yhdg.frontserver.entity;

import java.util.ArrayList;
import java.util.List;

public class Strategy {

    String uid;
    String strategyName;

    SwitchSetting switchSetting = new SwitchSetting();
    VolumeSetting volumeSetting = new VolumeSetting();
    DownloadSetting downloadSetting = new DownloadSetting();
    LogSetting logSetting = new LogSetting();

    public static class TimeRangeSetting {
        int active;
        String begin;
        String end;

        public int getActive() {
            return active;
        }

        public void setActive(int active) {
            this.active = active;
        }

        public String getBegin() {
            return begin;
        }

        public void setBegin(String begin) {
            this.begin = begin;
        }

        public String getEnd() {
            return end;
        }

        public void setEnd(String end) {
            this.end = end;
        }
    }

    public static class TimeSetting {
        int active;
        String time;

        public int getActive() {
            return active;
        }

        public void setActive(int active) {
            this.active = active;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }
    }

    public static class SwitchSetting {
        int [] week;
        List<TimeRangeSetting> openTime = new ArrayList<TimeRangeSetting>();
        List<TimeSetting> rebootTime = new ArrayList<TimeSetting>();

        public int[] getWeek() {
            return week;
        }

        public void setWeek(int[] week) {
            this.week = week;
        }

        public List<TimeRangeSetting> getOpenTime() {
            return openTime;
        }

        public void setOpenTime(List<TimeRangeSetting> openTime) {
            this.openTime = openTime;
        }

        public List<TimeSetting> getRebootTime() {
            return rebootTime;
        }

        public void setRebootTime(List<TimeSetting> rebootTime) {
            this.rebootTime = rebootTime;
        }
    }

    public static class VolumeSetting {
        int volume;
        int stationVolume;
        int [] week;
        List<VolumeTimeSetting> time = new ArrayList<VolumeTimeSetting>();

        public static class VolumeTimeSetting {
            String begin;
            String end;
            int active;
            int volume;

            public String getBegin() {
                return begin;
            }

            public void setBegin(String begin) {
                this.begin = begin;
            }

            public String getEnd() {
                return end;
            }

            public void setEnd(String end) {
                this.end = end;
            }

            public int getActive() {
                return active;
            }

            public void setActive(int active) {
                this.active = active;
            }

            public int getVolume() {
                return volume;
            }

            public void setVolume(int volume) {
                this.volume = volume;
            }
        }

        public int getVolume() {
            return volume;
        }

        public void setVolume(int volume) {
            this.volume = volume;
        }

        public int getStationVolume() {
            return stationVolume;
        }

        public void setStationVolume(int stationVolume) {
            this.stationVolume = stationVolume;
        }

        public int[] getWeek() {
            return week;
        }

        public void setWeek(int[] week) {
            this.week = week;
        }

        public List<VolumeTimeSetting> getTime() {
            return time;
        }

        public void setTime(List<VolumeTimeSetting> time) {
            this.time = time;
        }
    }

    public static class DownloadSetting {
        int [] week;
        List<SpeedTimeSetting> time = new ArrayList<SpeedTimeSetting>();

        public static class SpeedTimeSetting {
            String begin;
            String end;
            int active;
            int speed;

            public String getBegin() {
                return begin;
            }

            public void setBegin(String begin) {
                this.begin = begin;
            }

            public String getEnd() {
                return end;
            }

            public void setEnd(String end) {
                this.end = end;
            }

            public int getActive() {
                return active;
            }

            public void setActive(int active) {
                this.active = active;
            }

            public int getSpeed() {
                return speed;
            }

            public void setSpeed(int speed) {
                this.speed = speed;
            }
        }

        public int[] getWeek() {
            return week;
        }

        public void setWeek(int[] week) {
            this.week = week;
        }

        public List<SpeedTimeSetting> getTime() {
            return time;
        }

        public void setTime(List<SpeedTimeSetting> time) {
            this.time = time;
        }
    }

    public static class LogSetting {
        int wireless;
        int timeType;
        String begin;
        String end;
        int interval;

        public int getWireless() {
            return wireless;
        }

        public void setWireless(int wireless) {
            this.wireless = wireless;
        }

        public int getTimeType() {
            return timeType;
        }

        public void setTimeType(int timeType) {
            this.timeType = timeType;
        }

        public String getBegin() {
            return begin;
        }

        public void setBegin(String begin) {
            this.begin = begin;
        }

        public String getEnd() {
            return end;
        }

        public void setEnd(String end) {
            this.end = end;
        }

        public int getInterval() {
            return interval;
        }

        public void setInterval(int interval) {
            this.interval = interval;
        }
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getStrategyName() {
        return strategyName;
    }

    public void setStrategyName(String strategyName) {
        this.strategyName = strategyName;
    }

    public SwitchSetting getSwitchSetting() {
        return switchSetting;
    }

    public void setSwitchSetting(SwitchSetting switchSetting) {
        this.switchSetting = switchSetting;
    }

    public VolumeSetting getVolumeSetting() {
        return volumeSetting;
    }

    public void setVolumeSetting(VolumeSetting volumeSetting) {
        this.volumeSetting = volumeSetting;
    }

    public DownloadSetting getDownloadSetting() {
        return downloadSetting;
    }

    public void setDownloadSetting(DownloadSetting downloadSetting) {
        this.downloadSetting = downloadSetting;
    }

    public LogSetting getLogSetting() {
        return logSetting;
    }

    public void setLogSetting(LogSetting logSetting) {
        this.logSetting = logSetting;
    }
}
