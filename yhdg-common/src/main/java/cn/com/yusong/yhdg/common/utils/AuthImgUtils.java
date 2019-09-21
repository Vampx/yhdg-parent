package cn.com.yusong.yhdg.common.utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Random;

public class AuthImgUtils {

	static Font mFont = new Font("Arial Black", Font.PLAIN, 16);

	static Color getRandColor(int fc, int bc) {
		Random random = new Random();
		if (fc > 255)
			fc = 255;
		if (bc > 255)
			bc = 255;
		int r = fc + random.nextInt(bc - fc);
		int g = fc + random.nextInt(bc - fc);
		int b = fc + random.nextInt(bc - fc);
		return new Color(r, g, b);
	}

	public static AuthImg getAuthImg() throws IOException {

		int width = 60, height = 18;
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		Graphics g = image.getGraphics();
		Random random = new Random();
		g.setColor(getRandColor(200, 250));
		g.fillRect(1, 1, width - 1, height - 1);
		g.setColor(new Color(102, 102, 102));
		g.drawRect(0, 0, width - 1, height - 1);
		g.setFont(mFont);

		g.setColor(getRandColor(160, 200));
		for (int i = 0; i < 155; i++) {
			int x = random.nextInt(width - 1);
			int y = random.nextInt(height - 1);
			int xl = random.nextInt(6) + 1;
			int yl = random.nextInt(12) + 1;
			g.drawLine(x, y, x + xl, y + yl);
		}
		for (int i = 0; i < 70; i++) {
			int x = random.nextInt(width - 1);
			int y = random.nextInt(height - 1);
			int xl = random.nextInt(12) + 1;
			int yl = random.nextInt(6) + 1;
			g.drawLine(x, y, x - xl, y - yl);
		}

		StringBuilder buf = new StringBuilder();
		for (int i = 0; i < 4; i++) {
			int n = getRandomNum();
			buf.append(n);
			g.setColor(new Color(20 + random.nextInt(110), 20 + random.nextInt(110), 20 + random.nextInt(110)));
			g.drawString(String.valueOf(n), 15 * i + 2, 15);
		}

		g.dispose();
		
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		ImageIO.write(image, "JPEG", stream);
		
		AuthImg authImg = new AuthImg();
		authImg.authCode = buf.toString();
		authImg.buf = stream.toByteArray();
		
		return authImg;
	}

	private static int getRandomNum() {
		return (int) (Math.random() * 10);
	}
	
	public static class AuthImg {
		public byte[] buf;
		public String authCode;
	}

}
