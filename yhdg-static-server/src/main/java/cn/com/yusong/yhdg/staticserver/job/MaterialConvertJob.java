package cn.com.yusong.yhdg.staticserver.job;


import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.yms.Material;
import cn.com.yusong.yhdg.common.spring.SpringContextHolder;
import cn.com.yusong.yhdg.common.utils.AppUtils;
import cn.com.yusong.yhdg.common.utils.CmdUtils;
import cn.com.yusong.yhdg.staticserver.config.AppConfig;
import cn.com.yusong.yhdg.staticserver.constant.AppConstEnum;
import cn.com.yusong.yhdg.staticserver.service.basic.SystemConfigService;
import cn.com.yusong.yhdg.staticserver.service.yms.MaterialService;
import cn.com.yusong.yhdg.staticserver.utils.MediaUtils;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MaterialConvertJob implements Runnable {

    static Logger log = LoggerFactory.getLogger(MaterialConvertJob.class);

    AppConfig appConfig = SpringContextHolder.getBean(AppConfig.class);
    MaterialService materialService = SpringContextHolder.getBean(MaterialService.class);
    SystemConfigService systemConfigService = SpringContextHolder.getBean(SystemConfigService.class);

    long id;
    int duration;
    volatile long lastUpdate = 0;

    public MaterialConvertJob(long id) {
        this.id = id;
    }

    @Override
    public void run() {

        Material material = materialService.find(id);
        if(material == null) {
            return;
        }

        duration = material.getDuration();
        materialService.updateConvertStatus(id, ConstEnum.VideoConvertStatus.RUNNING.getValue(),null);
        try {
            File sourceFile = new File(appConfig.getRealPath(material.getFilePath()));
            int videoFormatSwitch = Integer.valueOf( systemConfigService.find(AppConstEnum.AgentConfigKey.VIDEO_FORMAT_SWITCH.getValue()).getConfigValue());
            String videoConvertFormat = systemConfigService.find(AppConstEnum.AgentConfigKey.VIDEO_CONVERT_FORMAT.getValue()).getConfigValue();
            String videoFormatCmd = systemConfigService.find(AppConstEnum.AgentConfigKey.VIDEO_FORMAT_CMD.getValue()).getConfigValue();
            if(ConstEnum.Flag.TRUE.getValue() == videoFormatSwitch) { //原视频需要转换格式
                File targetFile = new File(sourceFile.getParentFile(), AppUtils.getFileName(sourceFile.getName()) + '.' + videoConvertFormat );
                if(targetFile.exists()) {
                    String name = AppUtils.getFileName(sourceFile.getName());
                    String suffix = AppUtils.getFileSuffix(sourceFile.getName());
                    for(int i = 1; i < Integer.MAX_VALUE; i++){
                        targetFile = new File(sourceFile.getParentFile(), name + '_' + suffix + String.valueOf(i) + '.' +  videoConvertFormat);
                        if( !targetFile.exists()){
                            break;
                        }
                    }
                }

                MediaUtils.videoConvert(sourceFile, targetFile, videoFormatCmd, new RefreshProgress());
                FileUtils.deleteQuietly(new File(appConfig.getRealPath(material.getFilePath())));
                String filePath = material.getFilePath().replace(sourceFile.getName(), targetFile.getName());
                long size = targetFile.length();
                String md5Sum = AppUtils.md5Hex(targetFile);
                materialService.updateFilePathSize(id, targetFile.getName(), size, filePath);
                materialService.updateConvertStatus(id, ConstEnum.VideoConvertStatus.SUCCESS.getValue(),md5Sum);
            }
            materialService.updateProgress(id, 100);
        } catch (Exception e) {
            materialService.updateConvertStatus(id, ConstEnum.VideoConvertStatus.FAIL.getValue(),null);
            log.error("视频优化错误", e);
        }
    }

    private void updateProgress(long id, String line) {
        if(AppUtils.isWindows()) {
            Pattern pattern = Pattern.compile("time=(\\d\\d):(\\d\\d):(\\d\\d)\\.\\d\\d");
            Matcher matcher = pattern.matcher(line);
            if (matcher.find()) {
                int time = Integer.parseInt(matcher.group(1), 10) * 3600 + Integer.parseInt(matcher.group(2), 10) * 60 + Integer.parseInt(matcher.group(3), 10);
                int progress =  (int)(100.0 * time / duration);
                materialService.updateProgress(id, progress);
            }
        } else {
            Pattern pattern = Pattern.compile("time=(\\d+)\\.(\\d+)");
            Matcher matcher = pattern.matcher(line);
            if(matcher.find()) {
                long time = Long.parseLong(matcher.group(1), 10);
                int progress =  (int)(100.0 * time / duration);
                materialService.updateProgress(id, progress);
            }
        }
    }

    private class RefreshProgress implements CmdUtils.LineListener {
        @Override
        public void receive(String line) {
            long now = System.currentTimeMillis();
            if(now - lastUpdate > 1000 * 2) {
                updateProgress(id, line);
                lastUpdate = now;
            }
        }
    }
}
