package com.hydra.core.common.algorithm;

public final class Factor {
	public static int maxFactor(int x, int y){
		if(x == y){
			return x;
		}else{
			if(x > y){
				return maxFactor(x - y, y);
			}else{
				return maxFactor(x, y - x);
			}
		}
	}
	
	public static void main(String[] args) {
		System.out.println(maxFactor(1680, 640));
	}
	
	private Factor() {throw new UnsupportedOperationException();}
}
