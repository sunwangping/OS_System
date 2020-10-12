package com.csh.filesystem.viewer;

import java.util.List;
import java.util.Vector;
import javax.swing.table.AbstractTableModel;
import com.csh.filesystem.model.FAT;
import com.csh.filesystem.service.FATService;

public class TableModel extends AbstractTableModel {

	private Vector<String> columnNames;
	private Vector<Vector<Integer>> rowDatas;
	private FATService fatService;
	private int index = 0;
	
	public TableModel(){
		fatService = new FATService();
		initData();
	}
	
	public void initData(){
		columnNames = new Vector<String>();
		columnNames.add("磁盘块");
		columnNames.add("值");
		rowDatas = new Vector<Vector<Integer>>();
		Vector<Integer> vs = null;
		FAT[] list = fatService.getMyFAT();
		for (int i=0; i<128; i++){
			vs = new Vector<Integer>();
			if (list[i] != null){
				vs.add(i);
				vs.add(list[i].getIndex());
			} else {
				vs.add(i);
				vs.add(0);
			}
			rowDatas.add(vs);
		}
		
	}
	
	@Override
	public int getRowCount() {
		return 128;
	}

	@Override
	public int getColumnCount() {
		return 2;
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
