
public class ByteUtil {

	//count byte
	private static int countOne(byte b) {
		int sum=0;
		while(b!=0x00) {
			b &=(b-1);   //v=v&(v-1)这个操作可以直接消除掉v中的最右边的1。
			sum++;
		}
		return sum;
	}
	
	public static int countOne(int i) {
		byte[] iTob = iTob(i);
		int sum=0;
		for (byte b : iTob) {
			int countOne = countOne(b);
			sum+=countOne;
		}
		return sum;
	}
	
	public static int countOne(byte[] bs) {
		int sum=0;
		for (byte b : bs) {
			sum+=countOne(b);
		}
		return sum;
	}
	
	 //u4 byte转成ｉｎｔ
    public static int bToi(final int offset,byte[] buff) {
	    return ((buff[offset] & 0xFF) << 24)
	        | ((buff[offset + 1] & 0xFF) << 16)
	        | ((buff[offset + 2] & 0xFF) << 8)
	        | (buff[offset + 3] & 0xFF);
	 }
    // int 转　byte
    public static byte[] iTob(int i) {
	    byte[] bs=new byte[4];
	    bs[0]=(byte)((i >> 24)& 0xFF);
	    bs[1]=(byte)((i >> 16)& 0xFF);
	    bs[2]=(byte)((i >> 8)& 0xFF);
	    bs[3]=(byte)(i & 0xFF);
	    return bs;
	 }
	
    public static String toStringBs(byte[] bs) {
    	int readU4 = bToi(0, bs);
    	return Integer.toBinaryString(readU4);
    }
    
   //将boolean 数组转为byte　数组
    public static byte blsToBs(boolean[] bls) {
    	if(bls.length!=8) {
    		throw new IllegalArgumentException("bls必须为８");
    	}
    	byte b=0;
    	byte b1=1;
    	for (int i = bls.length-1; i >=0; i--) {
			if (bls[i]) {
				b |=b1;
			}
			b1 <<=1;
		}
    	return b;
    }
    // 将String转位byte
    public static byte strToBs(String str) {
    	if(str.length()!=8) {
    		throw new IllegalArgumentException("str长度必须为８");
    	}
    	boolean[] b=new boolean[8];
    	for (int i = 0; i < str.length(); i++) {
			if(str.charAt(i)=='0') {
				b[i]=false;
			}else {
				b[i]=true;
			}
		}
    	return blsToBs(b);
    }
    
    //计算２个byte中在位置上相同的个数
    public static int  sameCount(byte b1,byte b2) {
    	byte b3 = (byte) (b1^b2);
    	return countOne(b3);
    }
    
    //计算２个byte中在位置上相同的个数
    public static int  sameOneCount(byte b1,byte b2) {
    	byte b3 = (byte) (b1&b2);
    	return countOne(b3);
    }
    
}
