import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.imageio.ImageIO;

public class MyTest4 {

	
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
	
	//得到基本的图片
	public static void main(String[] args) throws Exception {
		String trainDir="/home/wang/img/train/"; //训练集
		String baseDir="/home/wang/img/base/"; //训练集收集到的基本图片
		Map<Character,Integer> mNum=new HashMap<Character,Integer>();//统计数字出现的个数
		Map<Character,List<BufferedImage>> charImgs=new HashMap<Character,List<BufferedImage>>();//待匹配的IMG
		//切割出来的图片
		File trainDirf=new File(trainDir);
		File[] listFiles = trainDirf.listFiles();
		for (File file : listFiles) {
			BufferedImage img1 = ImageIO.read(file);
			List<BufferedImage> charSplit = getCharSplit(img1);
			for (int i = 0; i < charSplit.size(); i++) {
				char charAt = file.getName().charAt(i);
				Integer cout = mNum.getOrDefault(charAt, 0)+1;
				mNum.put(charAt, cout);
				BufferedImage binaryImg = ImgUtil.binaryImg( charSplit.get(i), 350);
				List<BufferedImage> list = charImgs.get(charAt);
				if(list==null) {
					list=new ArrayList<>();
					charImgs.put(charAt, list);
				}
				list.add(binaryImg);
			}
		}
		/*for (Entry<Character, List<BufferedImage>> entry : charImgs.entrySet()) {  //输出全部切图
			Character charAt = entry.getKey();
			List<BufferedImage> imgs = entry.getValue();
			for (int i = 0; i < imgs.size(); i++) {
				String charFileName=baseDir+charAt+"_"+i+".png"; 
				ImageIO.write(imgs.get(i), "png", new File(charFileName));
			}
		}*/
		//选择基本的图片
		for (Entry<Character, List<BufferedImage>> entry : charImgs.entrySet()) {
			Character charAt = entry.getKey();
			List<BufferedImage> imgs = entry.getValue();  //每个数字训练集中的图片集合
			double sumOfmaxRateX=0;
			String maxFileName=baseDir+charAt+"_"+0+".png";
			BufferedImage img=imgs.get(0);
			for (int i = 0; i < imgs.size(); i++) {
				double tempSumOfmaxRateX=0;  //
				String charFileName=baseDir+charAt+"_"+i+".png"; 
				for (int j = 0; j < imgs.size(); j++) {
					if(i==j) continue;
					double maxRateX=ImgUtil.contactRatio(imgs.get(i), imgs.get(j));
					tempSumOfmaxRateX+=maxRateX;
				}
				if(tempSumOfmaxRateX>sumOfmaxRateX) {
					sumOfmaxRateX=tempSumOfmaxRateX;
					maxFileName=charFileName;
					img=imgs.get(i);
				}
			}
			System.out.println("数字"+charAt+"重合度最高的是"+maxFileName+",重合度为"+sumOfmaxRateX+",进行比较的集合大小为"+imgs.size());
			maxFileName=baseDir+charAt+".png";
			ImageIO.write(img, "png", new File(maxFileName));
		}
	}
}
