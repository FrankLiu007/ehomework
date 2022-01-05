package com.zxrh.ehomework.common.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.thymeleaf.util.ArrayUtils;

import com.zxrh.ehomework.common.constant.Default;
import com.zxrh.ehomework.common.constant.PreSuf;
import com.zxrh.ehomework.common.pojo.FailException;

public class FileUtils{

	public static boolean delete(File file){
		if(file == null || !file.exists()) return false;
		if(file.isDirectory()){
			File[] files = file.listFiles();
			if(!ArrayUtils.isEmpty(files)){
				for(File f:files){
					delete(f);
				}
			}
		}
		return file.delete();
	}
	
	public static String getName(String filename){
		return filename.substring(0,filename.lastIndexOf(Default.PERIOD));
	}
	
	public static String getSuffix(String filename){
		return filename.substring(getName(filename).length());
	}
	
	public static String read(File file){
		if(file == null || !file.isFile()) return null;
		BufferedInputStream bis = null;
		try{
			bis = new BufferedInputStream(new FileInputStream(file));
			byte[] buffer = new byte[1024*8];
			StringBuilder sb = new StringBuilder();
			for(int l;(l=bis.read(buffer))!=-1;sb.append(new String(buffer,0,l))){}
			return sb.toString();
		}catch(IOException e){
			throw new FailException(PreSuf.IO_EXCEPTION_PREFIX+"文件读取失败");
		}finally{
			if(bis != null){
				try{
					bis.close();
				}catch(IOException e){
					throw new FailException(PreSuf.IO_EXCEPTION_PREFIX+"关闭文件流异常");
				}
			}
		}
	}
	
}