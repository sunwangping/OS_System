package com.csh.filesystem.model;

public class FAT {
	
	private int index;//磁盘块
	private int type;//该磁盘块中存放的是File还是Folder
	private Object object;//File或Folder
	
	public FAT(int index, int type, Object object) {
		super();
		this.index = index;
		this.type = type;
		this.object = object;
	}
	
	
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public Object getObject() {
		return object;
	}
	public void setObject(Object object) {
		this.object = object;
	}
}
