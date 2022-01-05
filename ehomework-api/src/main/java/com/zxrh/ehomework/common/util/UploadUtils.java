package com.zxrh.ehomework.common.util;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.ArrayUtils;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.zxrh.ehomework.common.constant.PreSuf;
import com.zxrh.ehomework.common.pojo.FailException;
import com.zxrh.ehomework.common.properties.OSSProperties;

@Component
public class UploadUtils{

	private static OSSProperties ossProperties;
	
	@Autowired
	public void setOSSProperties(OSSProperties ossProperties){
		UploadUtils.ossProperties = ossProperties;
	}
	
	public static String upload(String prefix,MultipartFile file) throws FailException{
		if(file == null) return null;
		String endpoint = ossProperties.getEndpoint();
		String bucketName = ossProperties.getBucketName();
		OSS oss = new OSSClientBuilder().build(PreSuf.HTTP_PREFIX+endpoint,ossProperties.getAccessKeyId(),ossProperties.getAccessKeySecret());
		if(oss.doesBucketExist(bucketName)){
			String filename = file.getOriginalFilename();
			int dotIndex = filename.lastIndexOf(".");
			String suffix = dotIndex==-1?"":filename.substring(dotIndex);
			String key = prefix + UUID.randomUUID().toString() + suffix;
			try{
				oss.putObject(bucketName,key,file.getInputStream());
				return PreSuf.HTTPS_PREFIX+bucketName+"."+endpoint+"/"+key;
			}catch(IOException e){
				throw new FailException(PreSuf.IO_EXCEPTION_PREFIX+filename+"上传OSS失败");
			}finally{
				oss.shutdown();
			}
		}else{
			oss.shutdown();
			throw new FailException(PreSuf.OSS_EXCEPTION_PREFIX+"未找到存储桶");
		}
	}
	
	public static void uploadDirectory(String prefix,String directory){
		File parent = new File(directory);
		if(!parent.isDirectory()) throw new FailException("目录不存在");
		String endpoint = ossProperties.getEndpoint();
		String bucketName = ossProperties.getBucketName();
		OSS oss = new OSSClientBuilder().build(PreSuf.HTTP_PREFIX+endpoint,ossProperties.getAccessKeyId(),ossProperties.getAccessKeySecret());
		if(oss.doesBucketExist(bucketName)){
			File[] files = parent.listFiles();
			if(!ArrayUtils.isEmpty(files)){
				for(int i=0,l=files.length;i<l;i++){
					File file = files[i];
					if(file.isFile()){
						oss.putObject(bucketName,prefix + file.getName(), file);
					}
				}
			}
			oss.shutdown();
		}else{
			oss.shutdown();
			throw new FailException(PreSuf.OSS_EXCEPTION_PREFIX+"未找到存储桶");
		}
	}
	
	public static Map<String,String> batchUpload(String prefix,List<MultipartFile> files){
		Map<String,String> map = new HashMap<>();
		if(CollectionUtils.isEmpty(files)) return map;
		String endpoint = ossProperties.getEndpoint();
		String bucketName = ossProperties.getBucketName();
		OSS oss = new OSSClientBuilder().build(PreSuf.HTTP_PREFIX+endpoint,ossProperties.getAccessKeyId(),ossProperties.getAccessKeySecret());
		if(oss.doesBucketExist(bucketName)){
			String filename = "",suffix = "",key = "";
			int dotIndex;
			try{
				for(MultipartFile file:files){
					if(file == null){
						continue;
					}
					filename = file.getOriginalFilename();
					dotIndex = filename.lastIndexOf(".");
					suffix = dotIndex==-1?"":filename.substring(dotIndex);
					key = prefix + UUID.randomUUID().toString() + suffix;
					oss.putObject(bucketName,key,file.getInputStream());
					map.put(filename,PreSuf.HTTPS_PREFIX+bucketName+"."+endpoint+"/"+key);
				}
				return map;
			}catch(IOException e){
				throw new FailException(PreSuf.IO_EXCEPTION_PREFIX+filename+"上传OSS失败");
			}finally{
				oss.shutdown();
			}
		}else{
			oss.shutdown();
			throw new FailException(PreSuf.OSS_EXCEPTION_PREFIX+"未找到存储桶");
		}
	}

	public static void delete(String path) throws FailException{
		if(path == null) return;
		String endpoint = ossProperties.getEndpoint();
		String bucketName = ossProperties.getBucketName();
		OSS oss = new OSSClientBuilder().build(PreSuf.HTTP_PREFIX+endpoint,ossProperties.getAccessKeyId(),ossProperties.getAccessKeySecret());
		if(oss.doesBucketExist(bucketName)){
			String key = path.substring((PreSuf.HTTPS_PREFIX+bucketName+"."+endpoint+"/").length());
			oss.deleteObject(bucketName,key);
			oss.shutdown();
		}else{
			oss.shutdown();
			throw new FailException(PreSuf.OSS_EXCEPTION_PREFIX+"未找到存储桶");
		}
	}
	
	public static void batchDelete(String prefix,List<String> filenames){
		if(CollectionUtils.isEmpty(filenames)) return;
		String endpoint = ossProperties.getEndpoint();
		String bucketName = ossProperties.getBucketName();
		OSS oss = new OSSClientBuilder().build(PreSuf.HTTP_PREFIX+endpoint,ossProperties.getAccessKeyId(),ossProperties.getAccessKeySecret());
		if(oss.doesBucketExist(bucketName)){
			for(String filename:filenames){
				oss.deleteObject(bucketName,prefix+filename);
			}
			oss.shutdown();
		}else{
			oss.shutdown();
			throw new FailException(PreSuf.OSS_EXCEPTION_PREFIX+"未找到存储桶");
		}
	}
	
}
