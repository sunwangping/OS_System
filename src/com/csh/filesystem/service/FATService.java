package com.csh.filesystem.service;

import java.awt.Component;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JPanel;
import javax.swing.tree.DefaultMutableTreeNode;

import com.csh.filesystem.model.Disk;
import com.csh.filesystem.model.FAT;
import com.csh.filesystem.model.File;
import com.csh.filesystem.model.Folder;
import com.csh.filesystem.model.OpenFile;
import com.csh.filesystem.model.OpenFiles;
import com.csh.filesystem.util.FileSystemUtil;
import com.csh.filesystem.util.MessageUtil;

public class FATService {

	private static FAT[] myFAT;
	private static OpenFiles openFiles;
	
	public FATService(){
		openFiles = new OpenFiles();
	}
	
	public void addOpenFile(FAT fat, int flag){
		OpenFile openFile = new OpenFile();
		openFile.setFile((File)fat.getObject());
		openFile.setFlag(flag);
		openFiles.addFile(openFile);
	}
	
	public void removeOpenFile(FAT fat){
		for (int i=0; i<openFiles.getFiles().size(); i++){
			if (openFiles.getFiles().get(i).getFile() == (File)fat.getObject()){
				openFiles.getFiles().remove(i);
			}
		}
	}
	
	public boolean checkOpenFile(FAT fat){
		for (int i=0; i<openFiles.getFiles().size(); i++){
			if (openFiles.getFiles().get(i).getFile() == (File)fat.getObject()){
				return true;
			}
		}
		return false;
	}
	
	public void initFAT(){
		myFAT = new FAT[128];
		myFAT[0] = new FAT(FileSystemUtil.END, FileSystemUtil.DISK, null);
		myFAT[1] = new FAT(FileSystemUtil.END, FileSystemUtil.DISK, new Disk("C"));
	}
	
	//创建文件夹
	public int createFolder(String path){
		String folderName = null;
		boolean canName = true;
		int index = 1;
		//得到新建文件夹名字
		do {
			folderName = "新建文件夹";
			canName = true;
			folderName += index;
			for (int i=0; i<myFAT.length; i++){
				if (myFAT[i] != null){
					if (myFAT[i].getType() == FileSystemUtil.FOLDER){
						Folder folder = (Folder)myFAT[i].getObject();
						if (path.equals(folder.getLocation())){
							if (folderName.equals(folder.getFolderName())){
								canName = false;
							}
						}
					}
				}
			}
			index ++;
		} while (!canName);
		//在myFAT中添加文件夹
		int index2 = searchEmptyFromMyFAT(); 
		if (index2 == FileSystemUtil.ERROR){
			return FileSystemUtil.ERROR;
		} else {
			Folder folder = new Folder(folderName, path, index2);
			myFAT[index2] = new FAT(FileSystemUtil.END, FileSystemUtil.FOLDER, folder);
		}
		return index2;
	}
	
	/**
	 * 创建文件
	 * @return
	 */
	public int createFile(String path){
		String fileName = null;
		boolean canName = true;
		int index = 1;
		//得到新建文件名字
		do {
			fileName = "新建文件";
			canName = true;
			fileName += index;
			for (int i=0; i<myFAT.length; i++){
				if (myFAT[i] != null){
					if (myFAT[i].getType() == FileSystemUtil.FILE){
						File file = (File)myFAT[i].getObject();
						if (path.equals(file.getLocation())){
							if (fileName.equals(file.getFileName())){
								canName = false;
							}
						}
					}
				}
			}
			index ++;
		} while (!canName);
		//在myFAT中添加文件
		int index2 = searchEmptyFromMyFAT(); 
		if (index2 == FileSystemUtil.ERROR){
			return FileSystemUtil.ERROR;
		} else {
			File file = new File(fileName, path, index2);
			myFAT[index2] = new FAT(FileSystemUtil.END, FileSystemUtil.FILE, file);
		}
		return index2;
	}
	
	//得到myFAT中第一个为空的磁盘块索引
	public int searchEmptyFromMyFAT(){
		for (int i=2; i<myFAT.length; i++){
			if (myFAT[i] == null){
				return i;
			}
		}
		return FileSystemUtil.ERROR;
	}
	
	//得到磁盘块的使用
	public int getNumOfFAT(){
		int n = 0;
		for (int i=2; i<myFAT.length; i++){
			if (myFAT[i] != null){
				n++;
			}
		}
		return n;
	}
	
	//得到空的磁盘块数量
	public int getSpaceOfFAT(){
		int n = 0;
		for (int i=2; i<myFAT.length; i++){
			if (myFAT[i] == null){
				n++;
			}
		}
		return n;
	}
	
	//保存数据时重新分配磁盘
/*	public void saveToModifyFATS(int num, FAT fat){
		int begin = ((File)fat.getObject()).getDiskNum();
		int n = this.searchEmptyFromMyFAT();
		myFAT[begin].setIndex(n);
		for (int i=1; i<num; i++){
			n = this.searchEmptyFromMyFAT();
			if (i == num-1){
				myFAT[n] = new FAT(20, FileSystemUtil.FILE, (File)fat.getObject());
			} else {
				myFAT[n] = new FAT(20, FileSystemUtil.FILE, (File)fat.getObject());
				int n2 = this.searchEmptyFromMyFAT();
				myFAT[n].setIndex(n2);
			}
		}
	}*/
	
	public boolean saveToModifyFATS2(Component parent, int num, FAT fat){
		//从哪片磁盘开始
		int begin = ((File)fat.getObject()).getDiskNum();
		int index = myFAT[begin].getIndex();
		int oldNum = 1;
		while (index != FileSystemUtil.END){
			oldNum ++;
			if (myFAT[index].getIndex() == FileSystemUtil.END){
				begin = index;
			}
			index = myFAT[index].getIndex();
		}
		
		//
		
		if (num > oldNum){
			//需要添加磁盘块
			int n = num - oldNum;
			if (this.getSpaceOfFAT() < n){
				MessageUtil.showErrorMgs(parent, "保存的内容已经超过磁盘的容量");
				return false;
			}
			int space = this.searchEmptyFromMyFAT();
			myFAT[begin].setIndex(space);
			for (int i=1; i<=n; i++){
				space = this.searchEmptyFromMyFAT();
				if (i == n){
					myFAT[space] = new FAT(255, FileSystemUtil.FILE, (File)fat.getObject());
				} else {
					myFAT[space] = new FAT(20, FileSystemUtil.FILE, (File)fat.getObject());
					int space2 = this.searchEmptyFromMyFAT();
					myFAT[space].setIndex(space2);
				}
			}
			return true;
		} else {
			//不需要添加磁盘块
			return true;
		}
	}
	
	public List<Folder> getFolders(String path){
		List<Folder> list = new ArrayList<Folder>();
		for (int i=0; i<myFAT.length; i++){
			if (myFAT[i] != null){
				if (myFAT[i].getObject() instanceof Folder){
					if (((Folder)(myFAT[i].getObject())).getLocation().equals(path)){
						list.add((Folder)myFAT[i].getObject());
					}
				}
			}
		}
		return list;
	}
	
	public List<File> getFiles(String path){
		List<File> list = new ArrayList<File>();
		for (int i=0; i<myFAT.length; i++){
			if (myFAT[i] != null){
				if (myFAT[i].getObject() instanceof File){
					if (((File)(myFAT[i].getObject())).getLocation().equals(path)){
						list.add((File)myFAT[i].getObject());
					}
				}
			}
		}
		return list;
	}
	
	public List<FAT> getFATs(String path){
		List<FAT> fats = new ArrayList<FAT>();
		for (int i=0; i<myFAT.length; i++){
			if (myFAT[i] != null){
				if (myFAT[i].getObject() instanceof Folder){
					if (((Folder)(myFAT[i].getObject())).getLocation().equals(path) && myFAT[i].getIndex() == FileSystemUtil.END){
						fats.add(myFAT[i]);
					}
				}
			}
		}
		for (int i=0; i<myFAT.length; i++){
			if (myFAT[i] != null){
				if (myFAT[i].getObject() instanceof File){
					if (((File)(myFAT[i].getObject())).getLocation().equals(path) && myFAT[i].getIndex() == FileSystemUtil.END){
						fats.add(myFAT[i]);
					}
				}
			}
		}
		return fats;
		
	}
	
	public void modifyLocation(String oldPath, String newPath){
		for (int i=0; i<myFAT.length; i++){
			if (myFAT[i] != null){
				if (myFAT[i].getType()==FileSystemUtil.FILE){
					if (((File)myFAT[i].getObject()).getLocation().contains(oldPath)){
						((File)myFAT[i].getObject()).setLocation(((File)myFAT[i].getObject()).getLocation().replace(oldPath, newPath));
					}
				} else if (myFAT[i].getType()==FileSystemUtil.FOLDER){
					if (((Folder)myFAT[i].getObject()).getLocation().contains(oldPath)){
						((Folder)myFAT[i].getObject()).setLocation(((Folder)myFAT[i].getObject()).getLocation().replace(oldPath, newPath));
					}
				}
			}
		}
				
	}
	
	/**
	 * 删除
	 * @param jp1
	 * @param fat
	 * @param map
	 */
	public void delete(JPanel jp1, FAT fat,	Map<String, DefaultMutableTreeNode> map) {
		if (fat.getType() == FileSystemUtil.FILE){
			//---------------->文件
			//判断是否文件正在打开，如果打开则不能删除
			for (int i=0; i<openFiles.getFiles().size(); i++){
				if (openFiles.getFiles().get(i).getFile().equals(fat.getObject())){
					MessageUtil.showErrorMgs(jp1, "文件正打开着，不能删除");
					return ;
				}
			}
			
			for (int i=0; i<myFAT.length; i++){
				if (myFAT[i] != null && myFAT[i].getType() == FileSystemUtil.FILE){
					if (((File)myFAT[i].getObject()).equals((File)fat.getObject())){
						myFAT[i] = null;
						System.out.println("----------------------->删除");
					}
				}
			}
			
		} else {
			//---------------->文件夹
			String path = ((Folder)fat.getObject()).getLocation();
			String folderPath = ((Folder)fat.getObject()).getLocation() + "\\" + ((Folder)fat.getObject()).getFolderName();
System.out.println("路径：" + folderPath);
			int index = 0;
			for (int i=2; i<myFAT.length; i++){
				if (myFAT[i] != null){
					Object  obj = myFAT[i].getObject();
					if (myFAT[i].getType() == FileSystemUtil.FOLDER){
						if (((Folder)obj).getLocation().equals(folderPath)){
							MessageUtil.showErrorMgs(jp1, "文件夹不为空，不能删除");
							return ;
						}
					} else {
						if (((File)obj).getLocation().equals(folderPath)){
							MessageUtil.showErrorMgs(jp1, "文件夹不为空，不能删除");
							return ;
						}
					}
					if (myFAT[i].getType() == FileSystemUtil.FOLDER){
						if (((Folder)myFAT[i].getObject()).equals((Folder)fat.getObject())){
							index = i;
						}
					}
				}
			}

			myFAT[index] = null;
			DefaultMutableTreeNode parentNode = map.get(path);
			parentNode.remove(map.get(folderPath));
			map.remove(folderPath);
		}
	}
	
	public static FAT[] getMyFAT() {
		return myFAT;
	}

	public static void setMyFAT(FAT[] myFAT) {
		FATService.myFAT = myFAT;
	}
	
	public static FAT getFAT(int index){
		return myFAT[index];
	}
	
	public static OpenFiles getOpenFiles() {
		return openFiles;
	}

	public static void setOpenFiles(OpenFiles openFiles) {
		FATService.openFiles = openFiles;
	}

}
