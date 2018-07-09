package com.hydra.core.common.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.atomic.AtomicInteger;

public class FileUtil {
	public static int skipLines(BufferedReader br, int lines) throws IOException{
		int i = 0;
		while(null != br.readLine()){
			if(++i >= lines){
				break;
			}
		}
		return i;
	}
	
	public static void main(String[] args) throws IOException {
		File file = new File("E:/SQLS/all_script.sql");
		FileInputStream fis = new FileInputStream(file);
		InputStreamReader isr = new InputStreamReader(fis);
		BufferedReader br = new BufferedReader(isr);
		System.out.println(skipLines(br, 5));
		System.out.println(br.readLine());
		br.close();
	}
	
	private FileUtil() {throw new UnsupportedOperationException();}
}
