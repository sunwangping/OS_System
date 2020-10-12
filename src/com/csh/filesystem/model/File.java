package com.csh.filesystem.model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class File {

	//文件的目录项 占用8个字节
	private String fileName; //字母、数字和除 $ . /   3个字节
	private String type;//2个字节   类型
	private String property;//1个字节  属性
	private int diskNum;//1个字节 起始盘块号
	private int length;//1个字节
	private String content;//内容
	
	private int numOfFAT;
	private Folder parent;
	
	//查看的属性
	private String location; //位置
	private String size;	//大小
	private String space;	//占用空间
	private Date createTime; //创建时间
	
	private boolean isReadOnly;
	private boolean isHide;
	
	public File(String fileName) {
		super();
		this.fileName = fileName;
	}
	

	public File(String fileName, String location, int diskNum) {
			super();
			this.fileName = fileName;
			this.location = location;
			this.size = "64B";
			this.space = "64B";
			this.createTime = new Date();
			this.diskNum = diskNum;
			this.type = "File";
			this.isReadOnly = false;
			this.isHide = false;
			this.length = 1;
			this.content = "";
	}


	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public int getDiskNum() {
		return diskNum;
	}

	public void setDiskNum(int diskNum) {
		this.diskNum = diskNum;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getSpace() {
		return space;
	}

	public void setSpace(String space) {
		this.space = space;
	}

	public String getCreateTime(){
		SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日  HH:mm:ss");
		return format.format(createTime);
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	public boolean isReadOnly() {
		return isReadOnly;
	}

	public void setReadOnly(boolean isReadOnly) {
		this.isReadOnly = isReadOnly;
	}

	public boolean isHide() {
		return isHide;
	}

	public void setHide(boolean isHide) {
		this.isHide = isHide;
	}
	
	@Override
	public String toString() {
		return fileName;
	}
}
