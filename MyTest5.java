import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.imageio.ImageIO;

public class MyTest5 {
	
	private static Map<BufferedImage, Character> baseImgs=new HashMap<BufferedImage, Character>();
	private static String baseDir="/home/wang/img/base/";
	static {
		File baseDirs=new File(baseDir);
		File[] baseImgFiles = baseDirs.listFiles();
		for (File baseImgFile: baseImgFiles) {
			try {
				BufferedImage read = ImageIO.read(baseImgFile);
				baseImgs.put(read, baseImgFile.getName().charAt(0));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	
	public static List<BufferedImage> getCharSplit(BufferedImage image) throws IOException {
        List<BufferedImage> biList = new ArrayList<>();
        int width = image.getWidth()/4;
        int height = image.getHeight();
        biList.add(image.getSubimage(0,0,width,height));
        biList.add(image.getSubimage(width,0,width,height));
        biList.add(image.getSubimage(width*2,0,width,height));
        biList.add(image.getSubimage(width*3,0,width,height));
        return biList;
    }
	
	public static void main(String[] args) throws Exception{
		String testDir="/home/wang/img/train/";
		File testDirf=new File(testDir);
		File[] listFiles = testDirf.listFiles();
		for (File file : listFiles) {
			long start = System.currentTimeMillis();
			String num=getNum(file);
			long end = System.currentTimeMillis();
			System.out.println("文件"+file.getName()+"识别的结果位"+num+"耗时为"+(end-start));
		}
		
	}

	private static String getNum(File file) throws Exception {
		BufferedImage img = ImageIO.read(file);
		List<BufferedImage> charSplit = getCharSplit(img);
		StringBuffer sb=new StringBuffer();
		for (int i = 0; i < charSplit.size(); i++) {
			BufferedImage binaryImg = ImgUtil.binaryImg( charSplit.get(i), 350);
			double maxRate=0;
			char c='0';
			for (Entry<BufferedImage, Character> entry : baseImgs.entrySet()) {
				double contactRatioDefault = ImgUtil.contactRatioDefault(entry.getKey(), binaryImg);
				if(contactRatioDefault>maxRate) {
					maxRate=contactRatioDefault;
					c=entry.getValue();
				}
			}
			sb.append(c);
		}
		return sb.toString();
	}
	
	
}
