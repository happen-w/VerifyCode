import java.awt.Color;
import java.awt.image.BufferedImage;

public class ImgUtil {

	public static int[] encode(BufferedImage img) {
		int[] is=new int[img.getWidth()];
		for (int i = 0; i < img.getWidth(); i++) {
			int temp=0;
			for (int j = 0; j < img.getHeight(); j++) {
				temp <<=1;
				if (img.getRGB(i, j) == Color.WHITE.getRGB()) {
					temp=temp+1;
				}
			}
			is[i]=temp;
		}
		return is;
	}
	
	public static BufferedImage decode(int[] is,int width,int height) {
		width=width==0?is.length:width;
		height=height==0?32:height;
		BufferedImage img = new BufferedImage(width, height, 6);
		for (int i = is.length-1; i > 0; i--) {
			int temp=is[i];
			int index=height-1;
			while(index>0) {
				if((temp&1) == 1) {
					img.setRGB(i, index, Color.WHITE.getRGB());
				}else {
					img.setRGB(i, index, Color.BLACK.getRGB());
				}
				temp >>=1;
				index--;
			}
		}
		return img;
	}
	
	public static void moveUp(int[] is,int offestUp) {
		for (int i = 0; i < is.length; i++) {
			is[i] <<=offestUp;
		}
	}
	public static void moveDown(int[] is,int offestDown) {
		for (int i = 0; i < is.length; i++) {
			is[i]>>=offestDown;
		}
	}
	public static void moveLeft(int[] is,int offestLeft) {
		for (int i = 0; i < is.length; i++) {
			if(i+offestLeft>is.length-1) {
				is[i]=0;
			}else {
				is[i]=is[i+offestLeft];	
			}
		}
	}
	public static void moveRight(int[] is,int offestRight) {
		for (int i = is.length-1; i >0 ; i--) {
			if(i>=offestRight) {
				is[i]=is[i-offestRight];	
			}else {
				is[i]=0;
			}
		}
	}
	
	//转化成黑白图 deep 表示要以多少判断位白 deep=350
	public static BufferedImage binaryImg(BufferedImage img,int deep)
			throws Exception {
		int width = img.getWidth();
		int height = img.getHeight();
		for (int x = 0; x < width; ++x) {
			for (int y = 0; y < height; ++y) {
				if (isWhite(img,x, y,deep) == 1) {  
					img.setRGB(x, y, Color.BLACK.getRGB());
				} else {
					//判断周围是不是也是白的，如果不是，认为是干扰线
					img.setRGB(x, y, Color.WHITE.getRGB()); 
				}
			}
		}
		return img;
	}
	private static int isWhite(BufferedImage img, int x, int y,int deep) {
		try {
			Color color = new Color(img.getRGB(x, y));
			if (color.getRed() + color.getGreen() + color.getBlue() > deep) {
				return 1;
			}
		} catch (Exception e) {
			return 0;
		}
		return 0;
	}
	
	//计算 图片x 相对与 图片 y的重合度   x和y 1位置相同的个数相同个数  /x中 1的个数
	public static double contactRatio(BufferedImage imgx,BufferedImage imgy) {
		return contactRatio(imgx,imgy,0,0);
	}
	
	public static int countOne(int[] is) {
		int sumX=0;
		for (int i : is) {
			int countOne = ByteUtil.countOne(i);
			sumX+=countOne;
		}
		return sumX;
	}
	
	// 对 图片x进行偏移后,再计算与图片y的重合度
	public static double contactRatio(BufferedImage imgx,BufferedImage imgy,int ud,int lr) {
		int[] is = encode(imgx);
		int countOne = countOne(is); //imgx 1的个数
		if(ud>0) {
			moveUp(is, ud);
		}else if(ud<0) {
			moveDown(is, -ud);
		}
		int countOneUD = countOne(is); //imgx 1的个数
		if(4*countOneUD < 3*countOne) return 0;  // 移动后1个个数太少  //这个值需要调整
		if(lr>0) {
			moveRight(is, lr);
		}else if(lr<0) {
			moveLeft(is, -lr);
		}
		int countOnelr = countOne(is); //imgx 1的个数
		if(4*countOnelr < 3*countOne) return 0;  // 移动后1个个数太少  //这个值需要调整
		
		int[] isy = encode(imgy);
		for (int i = 0; i < is.length; i++) {
			is[i]&=isy[i];
		}
		int countOneXy=countOne(is);
		return ((double)countOneXy)/countOne; //返回最后结果
	}
	
	//默认进行上下左右10个距离进行偏移匹对,返回最大的匹配值
	public static double contactRatioDefault(BufferedImage imgx,BufferedImage imgy) {
		double maxRate=0;
		for (int x = -10; x < 10; x++) {
			for (int y = -10; y < 10; y++) {
				double contactRatio = ImgUtil.contactRatio(imgx, imgy, x, y);
				if(contactRatio>maxRate) {
					maxRate=contactRatio;
				}
			}
		}
		return maxRate;
	}
	
	
}
