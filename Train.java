package com.dr.iptv.util.verifyCode;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.imageio.ImageIO;

public class Train {

	
	public static void createBaseImg(String trainDir,String baseDir) throws IOException{
		Map<Character,Integer> mNum=new HashMap<Character,Integer>();//统计数字出现的个数
		Map<Character,List<BufferedImage>> charImgs=new HashMap<Character,List<BufferedImage>>();//待匹配的IMG
		
		File trainDirf=new File(trainDir);
		File[] listFiles = trainDirf.listFiles();
		for (File file : listFiles) {
			if(file.isDirectory()) continue;
			BufferedImage img1 = ImageIO.read(file);
			List<BufferedImage> charSplit = getCharSplit(img1);
			for (int i = 0; i < charSplit.size(); i++) {
				char charAt = file.getName().charAt(i);
				Integer cout = mNum.get(charAt);
				if(cout==null) cout=0;
				cout++;
				mNum.put(charAt, cout);
				//BufferedImage binaryImg = ImgUtil.binaryImg( charSplit.get(i), 350);
				List<BufferedImage> list = charImgs.get(charAt);
				if(list==null) {
					list=new ArrayList<>();
					charImgs.put(charAt, list);
				}
				list.add(charSplit.get(i));
			}
		}
	/*	for (Entry<Character, List<BufferedImage>> entry : charImgs.entrySet()) {  //输出全部切图
			Character charAt = entry.getKey();
			List<BufferedImage> imgs = entry.getValue();
			for (int i = 0; i < imgs.size(); i++) {
				String charFileName=baseDir+charAt+"_"+i+".png"; 
				ImageIO.write(imgs.get(i), "png", new File(charFileName));
			}
		}*/
		for (Entry<Character, List<BufferedImage>> entry : charImgs.entrySet()) {
			Character charAt = entry.getKey();
			List<BufferedImage> imgs = entry.getValue();  //每个数字训练集中的图片集合
			double sumOfmaxRateX=0;
			String maxFileName=charAt+"_"+0+".png";
			BufferedImage img=imgs.get(0);
			for (int i = 0; i < imgs.size(); i++) {
				double tempSumOfmaxRateX=0;  //
				String charFileName=charAt+"_"+i+".png"; 
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
	//分割图片
	private static List<BufferedImage> getCharSplit(BufferedImage image){
        List<BufferedImage> biList = new ArrayList<>();
        int width = image.getWidth()/4;
        int height = image.getHeight();
        biList.add(image.getSubimage(0,0,width,height));
        biList.add(image.getSubimage(width,0,width,height));
        biList.add(image.getSubimage(width*2,0,width,height));
        biList.add(image.getSubimage(width*3,0,width,height));
        return biList;
    }
	
	
	
	public static void main(String[] args) throws IOException {
		String trainDir="E:\\trainCode\\test2\\";
		String baseDir="E:\\trainCode\\test2\\base\\";
		createBaseImg(trainDir, baseDir);
//		String testDir="E:\\trainCode\\test2\\";
//		File testDirf=new File(testDir);
//		File[] listFiles = testDirf.listFiles();
//		for (File file : listFiles) {
//			if(file.isDirectory()) continue;
//			long start = System.currentTimeMillis();
//			String num=VerifyCode.verify(file);
//			long end = System.currentTimeMillis();
//			CharSequence subSequence = file.getName().subSequence(0, 4);
//			boolean res = subSequence.equals(num);
//			System.out.println("文件"+file.getName()+"识别的结果位"+num+"耗时为"+(end-start)+"匹配结果："+res);
//		}
//		File file=new File(testDir + "8337.png");
//		String num=VerifyCode.verify(file);
//		System.out.println(num);
	}
	
}
