package cn.com.yusong.yhdg.staticserver.service.yms;

import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.yms.Material;
import cn.com.yusong.yhdg.common.utils.AppUtils;
import cn.com.yusong.yhdg.staticserver.config.AppConfig;
import cn.com.yusong.yhdg.staticserver.constant.AppConstEnum;
import cn.com.yusong.yhdg.staticserver.constant.AppConstant;
import cn.com.yusong.yhdg.staticserver.job.MaterialConvertJob;
import cn.com.yusong.yhdg.staticserver.job.ThreadPoolManager;
import cn.com.yusong.yhdg.staticserver.persistence.basic.SystemConfigMapper;
import cn.com.yusong.yhdg.staticserver.persistence.yms.MaterialMapper;
import cn.com.yusong.yhdg.staticserver.utils.ArithmeticUtils;
import cn.com.yusong.yhdg.staticserver.utils.MediaUtils;
import it.sauronsoftware.jave.EncoderException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;

@Service
public class MaterialService {
    @Autowired
    MaterialMapper materialMapper;
    @Autowired
    MaterialIdGeneratorService materialIdGeneratorService;
    @Autowired
    ThreadPoolManager threadPoolManager;
    @Autowired
    AppConfig appConfig;
    @Autowired
    SystemConfigMapper systemConfigMapper;

    public Material find(Long id) {
        return materialMapper.find(id);
    }

    public int updateConvertStatus(long id, int convertStatus, String md5Sum) {
        return materialMapper.updateConvertStatus(id, convertStatus, md5Sum);
    }

    public int updateFilePathSize(long id, String materialName, long size, String filePath) {
        return materialMapper.updateFilePathSize(id, materialName, size, filePath);
    }

    public int updateProgress(long id, int convertProgress) {
        return materialMapper.updateProgress(id, convertProgress);
    }

    /**
     * 创建素材
     * @param file
     * @param material
     * @throws IOException
     * @throws EncoderException
     */
    public void create(MultipartFile file, Material material) throws IOException,IllegalArgumentException, EncoderException {
        material.setId(materialIdGeneratorService.newId());
        material.setCreateTime(new Date());
        material.setVersion(1);
        material.setMaterialName(ArithmeticUtils.decodeUrl(material.getMaterialName()));
        material.setOwnerName(ArithmeticUtils.decodeUrl(material.getOwnerName()));
        if(StringUtils.isEmpty(String.valueOf(material.getAgentId()))) {
            throw new IllegalArgumentException("运营商不能为空");
        }
        String videoSuffix = systemConfigMapper.find(AppConstEnum.AgentConfigKey.VIDEO_SUFFIX.getValue()).getConfigValue();
        int videoFormatSwitch = Integer.valueOf(systemConfigMapper.find(AppConstEnum.AgentConfigKey.VIDEO_FORMAT_SWITCH.getValue()).getConfigValue());
        String videoConvertFormat = systemConfigMapper.find(AppConstEnum.AgentConfigKey.VIDEO_CONVERT_FORMAT.getValue()).getConfigValue();
        String prefixPath = String.format(AppConstant.PATH_MATERIAL, material.getAgentId());
        material.setFilePath(prefixPath + material.getMaterialName());

        if(materialMapper.findUnique(material.getAgentId(), material.getFilePath()) != 0) {
            throw new IllegalArgumentException("文件已存在");
        }

        File target = new File(appConfig.appDir, material.getFilePath());
        AppUtils.makeParentDir(target);
        file.transferTo(target);
        String suffix = AppUtils.getFileSuffix(material.getMaterialName()).toLowerCase();
        if(videoSuffix.indexOf(suffix) != -1) {
            //视频
            material.setMaterialType(Material.MaterialType.VIDEO.getValue());
            material.setCoverPath(prefixPath + material.getMaterialName() + ".jpg");

            if(videoFormatSwitch == ConstEnum.Flag.TRUE.getValue()
                    && !suffix.equalsIgnoreCase(videoConvertFormat)) {
                //视频需要转化格式
                material.setConvertStatus(ConstEnum.VideoConvertStatus.WAIT.getValue());
                material.setConvertProgress(0);
            } else {
                //视频无需转化格式
                material.setConvertStatus(ConstEnum.VideoConvertStatus.SUCCESS.getValue());
                material.setConvertProgress(100);
                material.setMd5Sum(AppUtils.md5Hex(target));
            }

            File coverFile = new File(target.getParentFile(), material.getMaterialName() + ".jpg");

            MediaUtils.MediaInfo mediaInfo = MediaUtils.getVideoInfo(target);
            material.setDuration(mediaInfo.time);
            material.setWidth(mediaInfo.width);
            material.setHeight(mediaInfo.height);

            mediaInfo = MediaUtils.getImageSnapshotSize(mediaInfo, AppConstant.IMAGE_SNAPSHOT_SIZE);
            MediaUtils.makeVideoSnapshot(target, coverFile, mediaInfo.width, mediaInfo.height);

        } else {
            //图片
            material.setCoverPath(prefixPath + material.getMaterialName());
            material.setMaterialType(Material.MaterialType.IMAGE.getValue());
            material.setConvertStatus(ConstEnum.VideoConvertStatus.SUCCESS.getValue());
            material.setConvertProgress(100);
            material.setMd5Sum(AppUtils.md5Hex(target));
            material.setDuration(10);

            try {
                MediaUtils.MediaInfo mediaInfo = MediaUtils.getImageInfo(target);
                material.setWidth(mediaInfo.width);
                material.setHeight(mediaInfo.height);
            } catch (Exception e) {
                material.setWidth(1024);
                material.setHeight(768);
            }
        }
        material.setSize(target.length());

        materialMapper.insert(material);
        //上传完成后就进行格式转化
        if(material.getConvertStatus() == ConstEnum.VideoConvertStatus.WAIT.getValue()) {
            threadPoolManager.addVideo(new MaterialConvertJob(material.getId()));
        }
    }


    /**
     * 替换素材
     * @param file 上传的素材
     * @param material
     */
    public void replace(MultipartFile file, Material material) throws IOException, IllegalArgumentException, EncoderException {
        Material oldMaterial = materialMapper.find(material.getId());
        material.setMaterialName(ArithmeticUtils.decodeUrl(material.getMaterialName()).replace("[", "(").replace("]", ")"));
        material.setOwnerName(ArithmeticUtils.decodeUrl(material.getOwnerName()));
        material.setVersion(oldMaterial.getVersion() + 1);
        if(material.getAgentId() == null) {
            throw new IllegalArgumentException("运营商不能为空");
        }
        String videoSuffix = systemConfigMapper.find(AppConstEnum.AgentConfigKey.VIDEO_SUFFIX.getValue()).getConfigValue();
        int videoFormatSwitch = Integer.valueOf(systemConfigMapper.find( AppConstEnum.AgentConfigKey.VIDEO_FORMAT_SWITCH.getValue()).getConfigValue());
        String videoConvertFormat = systemConfigMapper.find(AppConstEnum.AgentConfigKey.VIDEO_CONVERT_FORMAT.getValue()).getConfigValue();
        String prefixPath = String.format(AppConstant.PATH_MATERIAL, material.getAgentId());

        material.setFilePath(prefixPath + material.getMaterialName());
        if(materialMapper.findUnique(material.getAgentId(), material.getFilePath()) != 0) {
            throw new IllegalArgumentException("文件已存在");
        }

        File target = new File(appConfig.appDir, material.getFilePath());
        AppUtils.makeParentDir(target);
        file.transferTo(target);
        String suffix = AppUtils.getFileSuffix(material.getMaterialName()).toLowerCase();
        if(videoSuffix.indexOf(suffix) != -1) {
            //视频
            material.setMaterialType(Material.MaterialType.VIDEO.getValue());
            material.setCoverPath(prefixPath + material.getMaterialName() + ".jpg");
            if(StringUtils.equals(suffix,"mp4")){
                //视频无需转化格式
                if(videoFormatSwitch == ConstEnum.Flag.TRUE.getValue() && !suffix.equalsIgnoreCase(videoConvertFormat)) {
                    //mp4视频转换开
                    material.setConvertStatus(ConstEnum.VideoConvertStatus.WAIT.getValue());
                    material.setConvertProgress(0);
                }else {
                    //mp4视频转换关
                    material.setConvertStatus(ConstEnum.VideoConvertStatus.SUCCESS.getValue());
                    material.setConvertProgress(100);
                    material.setMd5Sum(AppUtils.md5Hex(target));
                }
            }else {
                //视频需要转化格式
                if (videoFormatSwitch == ConstEnum.Flag.TRUE.getValue()) {
                    //视频转换开
                    material.setConvertStatus(ConstEnum.VideoConvertStatus.WAIT.getValue());
                    material.setConvertProgress(0);
                } else {
                    //视频转换关
                    material.setConvertStatus(ConstEnum.VideoConvertStatus.SUCCESS.getValue());
                    material.setConvertProgress(100);
                    material.setMd5Sum(AppUtils.md5Hex(target));
                }
            }
            File coverFile = new File(target.getParentFile(), material.getMaterialName() + ".jpg");

            MediaUtils.MediaInfo mediaInfo = MediaUtils.getVideoInfo(target);
            material.setDuration(mediaInfo.time);
            material.setWidth(mediaInfo.width);
            material.setHeight(mediaInfo.height);

            mediaInfo = MediaUtils.getImageSnapshotSize(mediaInfo, AppConstant.IMAGE_SNAPSHOT_SIZE);
            MediaUtils.makeVideoSnapshot(target, coverFile, mediaInfo.width, mediaInfo.height);

        } else {
            //图片
            material.setCoverPath(prefixPath + material.getMaterialName());
            material.setMaterialType(Material.MaterialType.IMAGE.getValue());
            material.setConvertStatus(ConstEnum.VideoConvertStatus.SUCCESS.getValue());
            material.setConvertProgress(100);
            material.setMd5Sum(AppUtils.md5Hex(target));
            material.setDuration(10);

            try {
                MediaUtils.MediaInfo mediaInfo = MediaUtils.getImageInfo(target);
                material.setWidth(mediaInfo.width);
                material.setHeight(mediaInfo.height);
            } catch (Exception e) {
                material.setWidth(1024);
                material.setHeight(768);
            }
        }
        material.setSize(target.length());
        materialMapper.update(material);
        //上传完成后就进行格式转化
        if(material.getConvertStatus() == ConstEnum.VideoConvertStatus.WAIT.getValue()) {
            threadPoolManager.addVideo(new MaterialConvertJob(material.getId()));
        }
    }

}
