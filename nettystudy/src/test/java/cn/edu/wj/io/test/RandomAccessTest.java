package cn.edu.wj.io.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class RandomAccessTest {

	static File LOAD_FILE = new File("D:\\mnt\\mfs\\INN17101888ZM_777290058138766.txt");
	
	public static void main(String[] args) {
		testBufferReader();
	}
	
	public static void testBufferReader(){
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(LOAD_FILE)), 10*1024*1024);
			String tempString = null;
			long startTime = System.currentTimeMillis();
			int line = 0;
			while((tempString = reader.readLine()) != null){
				line++;
			}
			System.out.println("读取行数:"+line+", 总共耗时:"+(System.currentTimeMillis()-startTime));
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			if(null!=reader){
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void testRandomAccess(){
		RandomAccessFile reader=null;//输入流
		try {
			
			reader = new RandomAccessFile(LOAD_FILE,"r");
			long begingPos = 0;//文件解析起始位置
			
			String tempString = null;
			long startTime = System.currentTimeMillis();
			while((tempString = reader.readLine()) != null){
				//System.out.println("读取内容:"+tempString);
			}
			System.out.println("总共耗时:"+(System.currentTimeMillis()-startTime));
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			if(null!=reader){
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
}
