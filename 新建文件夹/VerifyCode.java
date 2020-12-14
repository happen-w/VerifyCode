package com.dr.iptv.util.verifyCode;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.imageio.ImageIO;

public class VerifyCode {

	public static Map<BufferedImage, Character> baseImgs=new HashMap<BufferedImage, Character>();
	private static String baseDir="E:\\trainCode\\test2\\base\\";  //基本的图片
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
	
	public static String verify(InputStream input) throws IOException{
		BufferedImage img = ImageIO.read(input);
		String num = getNum(img);
		return  num;
	}
	public static String verify(File File) throws IOException{
		BufferedImage img = ImageIO.read(File);
		String num = getNum(img);
		return  num;
	}
	//分割图片
	public static List<BufferedImage> getCharSplit(BufferedImage image){
        List<BufferedImage> biList = new ArrayList<>();
        int width = image.getWidth()/4;
        int height = image.getHeight();
        biList.add(image.getSubimage(0,0,width,height));
        biList.add(image.getSubimage(width,0,width,height));
        biList.add(image.getSubimage(width*2,0,width,height));
        biList.add(image.getSubimage(width*3,0,width,height));
        return biList;
    }
	
	//识别
	private static String getNum(BufferedImage img){
		List<BufferedImage> charSplit = getCharSplit(img);
		StringBuffer sb=new StringBuffer();
		for (int i = 0; i < charSplit.size(); i++) {
			//BufferedImage binaryImg = ImgUtil.binaryImg( charSplit.get(i), 350);
			double maxRate=0;
			char c='0';
			for (Entry<BufferedImage, Character> entry : baseImgs.entrySet()) {
				double contactRatioDefault = ImgUtil.contactRatioDefault(entry.getKey(), charSplit.get(i));
				System.out.println(entry.getValue()+"概率为"+contactRatioDefault);
				if(contactRatioDefault>maxRate) {
					maxRate=contactRatioDefault;
					c=entry.getValue();
				}
			}
			sb.append(c);
			System.out.println("识别结果为"+c);
		}
		return sb.toString();
	}
}
