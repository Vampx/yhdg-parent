package cn.com.yusong.yhdg.staticserver.constant;

public class AppConstEnum {

    public enum AgentConfigKey {
        TERMINAL_TIMEOUT_TIME("terminal.timeout.time"),
        VIDEO_SUFFIX("video.suffix"),
        IMAGE_SUFFIX("image.suffix"),
        MIN_SPEED("min.speed"),
        DOWNLOAD_COUNT("download.count"),
        VIDEO_FORMAT_SWITCH("video.format.switch"),
        VIDEO_CONVERT_FORMAT("video.convert.format"),
        VIDEO_FORMAT_CMD("video.format.cmd");

        private final String value;

        AgentConfigKey(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

}
