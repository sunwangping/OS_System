package com.csh.filesystem.viewer;

import java.util.Vector;

import javax.swing.table.AbstractTableModel;

import com.csh.filesystem.model.OpenFiles;
import com.csh.filesystem.service.FATService;
import com.csh.filesystem.util.FileSystemUtil;

public class OpenFileTableModel extends AbstractTableModel {

	private Vector<String> columnNames;
	private Vector<Vector<String>> rowDatas;
	private FATService fatService;
	
	public OpenFileTableModel(){
		fatService = new FATService();
		initData();
	}
	
	public void initData(){
		columnNames = new Vector<String>();
		columnNames.add("文件名称");
		columnNames.add("文件打开方式");
		columnNames.add("文件起始盘块号");
		columnNames.add("文件路径");
		
		Vector<String> vc = null;
		rowDatas = new Vector<Vector<String>>();
		OpenFiles openFiles = fatService.getOpenFiles();
		for (int i=0; i<FileSystemUtil.num; i++){
			vc = new Vector<String>();
			if(i < openFiles.getFiles().size()){
				vc.add(openFiles.getFiles().get(i).getFile().getFileName());
				vc.add(openFiles.getFiles().get(i).getFlag()==FileSystemUtil.flagRead ? "只读" : "写读");
				vc.add(openFiles.getFiles().get(i).getFile().getDiskNum() + "");
				vc.add(openFiles.getFiles().get(i).getFile().getLocation());
			} else {
				vc.add("");
				vc.add("");
				vc.add("");
				vc.add("");
			}
			rowDatas.add(vc);
		}
	}
	
	@Override
	public int getRowCount() {
		return 5;
	}

	@Override
	public int getColumnCount() {
		return 4;
	}

	@Override
	public String getColumnName(int column) {
		return columnNames.get(column);
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return rowDatas.get(rowIndex).get(columnIndex);
	}


}
