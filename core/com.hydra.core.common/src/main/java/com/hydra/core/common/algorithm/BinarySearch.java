package com.hydra.core.common.algorithm;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BinarySearch {
	public static int findPosition(List<Integer> list, Integer item){
		int low = 0;
		int high = list.size() - 1;
		Integer guess = null;
		AtomicInteger count = new AtomicInteger(0);
		int c = 0;
		while(low <= high){
			c = count.addAndGet(1);
			int mid = (low + high) / 2;
			guess = list.get(mid);
			if(item.equals(guess)){
				log.info("共执行了{}次。", c);
				return mid;
			}
			if(guess > item){
				high = mid - 1;
			}else{
				low = mid + 1;
			}
		}
		return -1;
	}
	
	public static void main(String[] args) {
		List<Integer> orderList = new ArrayList<>(128);
		for(int i = 1; i <= 100; i ++){
			orderList.add(i);
		}
		log.info("orderList size is:{}", orderList.size());
		int position = findPosition(orderList, 18);
		log.info("position is:{}", position);
	}
	
	private BinarySearch() {throw new UnsupportedOperationException();}
}
