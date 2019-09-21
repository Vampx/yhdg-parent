package cn.com.yusong.yhdg.webserver.utils;

import cn.com.yusong.yhdg.common.utils.CmdUtils;
import it.sauronsoftware.jave.*;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;

public class MediaUtils {
	static Logger log = LogManager.getLogger(MediaUtils.class);

    public static MediaInfo getVideoInfo(File f) throws InputFormatException, EncoderException {
		Encoder encoder = new Encoder();//视频格式判断
		MultimediaInfo info = encoder.getInfo(f);
		
		MediaInfo mediaInfo = new MediaInfo();
		mediaInfo.format = info.getFormat();
		mediaInfo.time = (int) info.getDuration() / 1000;
		VideoSize size = info.getVideo().getSize();
		mediaInfo.width = size.getWidth();
		mediaInfo.height = size.getHeight();
		
		return mediaInfo;
	}
	
	public static MediaInfo getAudioInfo(File f) throws InputFormatException, EncoderException {
		Encoder encoder = new Encoder();//视频格式判断
		MultimediaInfo info = encoder.getInfo(f);
		
		MediaInfo mediaInfo = new MediaInfo();
		mediaInfo.format = info.getFormat();
		mediaInfo.time = (int) info.getDuration() / 1000;
		
		return mediaInfo;
	}
	
	public static MediaInfo getImageInfo(File f) throws IOException {
		BufferedImage source = ImageIO.read(f);
		
		MediaInfo mediaInfo = new MediaInfo();
		mediaInfo.width = source.getWidth();
		mediaInfo.height = source.getHeight();
		
		return mediaInfo;
	}
	
	public static void makeImageSnapshot(File sourceFile, File targetFile, String format, int width, int height) throws IOException {
		
		try {
			BufferedImage source = ImageIO.read(sourceFile);
			BufferedImage target = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			target.getGraphics().drawImage(source.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0, 0, null);
			ImageIO.write(target, format, targetFile);
		} catch(Exception e) {
            FileUtils.copyFile(sourceFile, targetFile);
        }
	}

	public static void makeImageSnapshotGraphics2D (File sourceFile, File targetFile, String format, int width, int height) throws IOException {

		try {
			BufferedImage source = ImageIO.read(sourceFile);
			BufferedImage target = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			Graphics2D resizedG = target.createGraphics();
			target = resizedG.getDeviceConfiguration().createCompatibleImage(width, height, Transparency.TRANSLUCENT);
			resizedG.dispose();
			resizedG = target.createGraphics();
			@SuppressWarnings("static-access")
			Image from = source.getScaledInstance(width, height, source.SCALE_AREA_AVERAGING);
			resizedG.drawImage(from, 0, 0, null);
			resizedG.dispose();
			//target.getGraphics().drawImage(source.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0, 0, null);
			ImageIO.write(target, format, targetFile);
		} catch(Exception e) {
			FileUtils.copyFile(sourceFile, targetFile);
		}
	}
	
	public static void makeVideoSnapshot(File sourceFile, File targetFile, int width, int height) {
		
		try {
			String cmd = "FFMPEG_PATH  -ss 2 -i VIDEO_PATH -y -f image2 -vframes 1 -s IMAGE_WIDTHxIMAGE_HEIGHT IMAGE_PATH";
			DefaultFFMPEGLocator ffmpeg = new DefaultFFMPEGLocator();

			if(AppUtils.isWindows()) {
				cmd = cmd.replace("FFMPEG_PATH", '"' + ffmpeg.getFFMPEGExecutablePath() + '"');
				cmd = cmd.replace("VIDEO_PATH", '"' + sourceFile.getAbsolutePath() + '"');
				cmd = cmd.replace("IMAGE_WIDTH", String.format("%d", width));
				cmd = cmd.replace("IMAGE_HEIGHT", String.format("%d", height));
				cmd = cmd.replace("IMAGE_PATH", '"' + targetFile.getAbsolutePath() + '"');
			} else {
				cmd = cmd.replace("FFMPEG_PATH", ffmpeg.getFFMPEGExecutablePath() );
				cmd = cmd.replace("VIDEO_PATH", sourceFile.getAbsolutePath() );
				cmd = cmd.replace("IMAGE_WIDTH", String.format("%d", width));
				cmd = cmd.replace("IMAGE_HEIGHT", String.format("%d", height));
				cmd = cmd.replace("IMAGE_PATH",  targetFile.getAbsolutePath() );
			}

			CmdUtils.syncExec(cmd);
		} catch(Exception e) {
			log.error(e);
        }
	}

    public static void videoConvert(File sourceFile, File targetFile, String cmd, CmdUtils.LineListener stdListener) {
        try {
            DefaultFFMPEGLocator ffmpeg = new DefaultFFMPEGLocator();
            cmd = cmd.replace("TOOL_PATH", ffmpeg.getFFMPEGExecutablePath() );
            cmd = cmd.replace("INPUT", sourceFile.getAbsolutePath() );
            cmd = cmd.replace("OUTPUT", targetFile.getAbsolutePath() );
            CmdUtils.syncExec(cmd, null, stdListener);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        if(!targetFile.exists()) {
            throw new RuntimeException("Target file not exist");
        }
    }

    public static MediaInfo getImageSnapshotSize(MediaInfo size, int standard) {
        CalcSuitableSize calc = new CalcSuitableSize(standard);
        return calc.calc(size.width, size.height);
    }

	public static Rectangle getWhiteBorder(File file) throws IOException {
		int[] rgb = new int[3];
		BufferedImage bi = ImageIO.read(file);

		int width = bi.getWidth();
		int height = bi.getHeight();

		Rectangle rect = new Rectangle();

		//求出白边的X
		outer: for(int w = 0; w < width; w++) {
			rect.x = w;
			for(int h = 0; h < height; h++) {
				int pixel = bi.getRGB(w, h);
				rgb[0] = (pixel & 0xff0000) >> 16;
				rgb[1] = (pixel & 0xff00) >> 8;
				rgb[2] = (pixel & 0xff);
				if(rgb[0] != 255 || rgb[1] != 255 || rgb[2] != 255) {
					break outer;
				}
			}
		}

		//求出白边的Y
		outer: for(int h = 0; h < height; h++) {
			rect.y = h;
			for(int w = 0; w < width; w++) {
				int pixel = bi.getRGB(w, h);
				rgb[0] = (pixel & 0xff0000) >> 16;
				rgb[1] = (pixel & 0xff00) >> 8;
				rgb[2] = (pixel & 0xff);
				if(rgb[0] != 255 || rgb[1] != 255 || rgb[2] != 255) {
					break outer;
				}
			}
		}

		//求出白边的W
		rect.width = width - rect.x * 2;

		//求出白边的H
		outer: for(int h = height - 1; h >= 0; h--) {
			rect.height = h;
			for(int w = 0; w < width; w++) {
				int pixel = bi.getRGB(w, h);
				rgb[0] = (pixel & 0xff0000) >> 16;
				rgb[1] = (pixel & 0xff00) >> 8;
				rgb[2] = (pixel & 0xff);
				if(rgb[0] != 255 || rgb[1] != 255 || rgb[2] != 255) {
					break outer;
				}
			}
		}
		rect.height -= rect.y;
		return rect;
	}

	public static void cut(int x, int y, int width, int height, File source, File target) {
		int borderX = 0, borderY= 0;
		if(x > 2) {
			borderX = 2;
		}
		if(y > 2) {
			borderY = 2;
		}

		x -= borderX;
		y -= borderY;
		width += borderX * 2;
		height += borderY * 2;

		FileInputStream is = null;
		ImageInputStream iis = null;
		try {
			is = new FileInputStream(source);
			String fileSuffix = source.getName().substring(source.getName().lastIndexOf(".") + 1);
			Iterator<ImageReader> it = ImageIO.getImageReadersByFormatName(fileSuffix);
			ImageReader reader = it.next();
			iis = ImageIO.createImageInputStream(is);
			reader.setInput(iis, true);
			ImageReadParam param = reader.getDefaultReadParam();
			Rectangle rect = new Rectangle(x, y, width, height);
			param.setSourceRegion(rect);
			BufferedImage bi = reader.read(0, param);
			ImageIO.write(bi, fileSuffix, target);

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if(is != null) {
				try { is.close();} catch (Exception e) {};
			}
			if(iis != null) {
				try { iis.close();} catch (Exception e) {};
			}
		}
	}
	
	public static class MediaInfo {
		public int time;
		public String format;
		public int width, height;
	}

    public static class CalcSuitableSize {
        private int standard;

        public CalcSuitableSize(int standard) {
            this.standard = standard;
        }

        public MediaInfo calc(int width, int height) {
            MediaInfo mediaInfo = new MediaInfo();

            if(width <= standard && height <= standard) {
                mediaInfo.height = height;
                mediaInfo.width = width;
            } else {
                if(width > height) {
                    mediaInfo.width = standard;
                    mediaInfo.height = (int) standard * height / width;
                } else {
                    mediaInfo.height = standard;
                    mediaInfo.width = (int) standard * width / height;
                }
            }

            return mediaInfo;
        }
    }
}
