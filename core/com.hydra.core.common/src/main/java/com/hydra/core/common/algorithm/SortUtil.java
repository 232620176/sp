package com.hydra.core.common.algorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class SortUtil {
	public static List<Integer> quickSort(List<Integer> list){
		int len = list.size();
		if(len < 2){
			return list;
		}else{
			Integer pivot = list.remove(len/2);
			List<Integer> less = new ArrayList<>();
			List<Integer> greater = new ArrayList<>();
			for(Integer i : list){
				if(i <= pivot){
					less.add(i);
				}else{
					greater.add(i);
				}
			}
			List<Integer> res = new ArrayList<>(quickSort(less));
			res.add(pivot);
			res.addAll(new ArrayList<>(quickSort(greater)));
			return res;
		}
	}
	
	public static void main(String[] args) {
		List<Integer> list = new ArrayList<>(Arrays.asList(new Integer[]{1, 9, 2, 1, 5, 4, 3, 5}));
		System.out.println(quickSort(list));
	}
	
	private SortUtil() {throw new UnsupportedOperationException();}
}
