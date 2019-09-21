package cn.com.yusong.yhdg.routeserver.utils;

public class ArithmeticUtils {
	
	public enum Arithmetic {
		SwapOddEven
	}
	
	public static byte[] encode(byte[] buf, Arithmetic arithmetic) {
		if(arithmetic.equals(Arithmetic.SwapOddEven)) {
			return new SwapOddEven().encode(buf);
		} else{
			throw new IllegalArgumentException("not support arithmetic");
		}
	}

	public static byte[] decode(byte[] buf) {
		if(buf[0] == 1) {
			return new SwapOddEven().decode(buf);
		} else{
			throw new IllegalArgumentException("not support arithmetic");
		}
	}
	
	private static class SwapOddEven {
		
		private byte flag = 1;
		
		public byte[] encode(byte[] buf) {
			byte[] target = new byte[buf.length + 1];
			target[0] = flag;
			
			int index = 0, len = buf.length - (buf.length % 2);
			
			for(int i = 0; i < len; i = i + 2) {
				target[++index] = buf[i + 1];
				target[++index] = buf[i];
			}
			
			if(buf.length % 2 == 1) {
				target[++index] = buf[buf.length - 1];
			}
			return target;
		}
		
		public byte[] decode(byte[] buf) {
			byte[] target = new byte[buf.length - 1];
			System.arraycopy(buf, 1, target, 0, buf.length - 1);
			
			int len = target.length - (target.length % 2);
			byte swap;
			
			for(int i = 0; i < len; i = i + 2) {
				swap = target[i + 1];
				target[i + 1] = target[i];
				target[i] = swap;
			}
			
			return target;
		}
	}
}
