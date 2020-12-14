package com.dr.iptv.util.verifyCode;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.imageio.ImageIO;

public class Test {
	
	public static void main(String[] args) throws IOException {
		String path="E:\\trainCode\\test2\\cp\\";
		File file=new File(path);
		File[] listFiles = file.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.startsWith("cp");
			}
		});
		for (File file2 : listFiles) {
			BufferedImage imageCP = ImageIO.read(file2);
			String oFileName = file2.getName().substring(2);
			BufferedImage binaryImg = ImgUtil.binaryImg(imageCP, 350);
			ImageIO.write(binaryImg, "png", new File(path+oFileName));
		}
		
		
//		for (File file2 : listFiles) {
//			BufferedImage imageCP = ImageIO.read(file2);
//			String oFileName = file2.getName().substring(2);
//			BufferedImage imageO = ImageIO.read(new File(path+oFileName));
//			int width = imageO.getWidth();
//			int height = imageO.getHeight();
//			for (int x = 0; x < width; ++x) {
//				for (int y = 0; y < height; ++y) {
//					int count = countWhite(imageO, x, y,new ColorFilter() {
//						@Override
//						public boolean isWhite(int i) {
//							Color color=new Color(120, 120, 120);
//							int rgb = color.getRGB();
//							return i >=rgb;
//						}
//						
//						@Override
//						public boolean isBlack(int i) {
//							return !isWhite(i);
//						}
//					});
//					int i = count*60;
//					Color c=new Color(i,i,i);
//					imageCP.setRGB(x, y, c.getRGB());
//				}
//			}
//			ImageIO.write(imageCP, "png", new File(path+file2.getName()));
//		}
		
	}

	private static int countWhite(BufferedImage binaryImg, int x, int y ,ColorFilter filter) {
		if(filter.isBlack(binaryImg.getRGB(x, y))) return 0;
		int count=0; 
		if(x-1>=0){
			if(filter.isWhite(binaryImg.getRGB(x-1, y))) count++;
		}
		if(x+1<binaryImg.getWidth()){
			if(filter.isWhite(binaryImg.getRGB(x+1, y) )) count++;
		}
		if(y-1>=0){
			if(filter.isWhite(binaryImg.getRGB(x, y-1))) count++;
		}
		if(y+1<binaryImg.getHeight()){
			if(filter.isWhite(binaryImg.getRGB(x, y+1))) count++;
		}
		return count;
	}
	
	interface ColorFilter{
		boolean isBlack(int i);
		boolean isWhite(int i);
	}
}
