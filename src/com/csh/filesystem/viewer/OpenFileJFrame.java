package com.csh.filesystem.viewer;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import com.csh.filesystem.model.Folder;
import com.csh.filesystem.model.File;
import com.csh.filesystem.model.FAT;
import com.csh.filesystem.service.FATService;
import com.csh.filesystem.util.FileSystemUtil;
import com.csh.filesystem.util.MessageUtil;

public class OpenFileJFrame extends JFrame {
	
	private JTextArea jta1;
	private JMenuBar jmb;
	private JMenu jm;
	private JMenuItem jmi1, jmi2;
	
	private FAT fat;
	private File file;
	private String oldContent;
	private int length;
	private FATService fatService;
	private OpenFileTableModel oftm;
	private JTable jt;
	private JFrame jf;
	
	private TableModel tm;
	private JTable jta;
	
	private boolean canClose = true;
	
	public OpenFileJFrame(FAT fat, FATService fatService, OpenFileTableModel oftm, JTable jt, TableModel tm, JTable jta){
		this.jf = this;
		this.fat = fat;
		this.fatService = fatService;
		this.oftm = oftm;
		this.jt = jt;
		this.tm = tm;
		this.jta = jta;
		this.file = (File)fat.getObject(); 
		jta1 = new JTextArea();
		jmb = new JMenuBar();
		jm = new JMenu("操作");
		jmi1 = new JMenuItem("保存");
		jmi2 = new JMenuItem("退出");
		jmb.add(jm);
		jm.add(jmi1);
		jm.add(jmi2);
		
		oldContent = file.getContent();
		
		jta1.setText(oldContent);
		
		init();
		menuItemAddListener();
	}
	
	private void init(){
		this.setResizable(false);
		this.setSize(600, 500);
		this.setTitle("打开");
		this.setLocation(200, 150);
		this.add(jmb, BorderLayout.NORTH);
		this.add(jta1);
		this.addWindowListener(new WindowClosingListener());
		
		this.setVisible(true);
	}
	
	private void menuItemAddListener(){
		jmi1.addActionListener(new ActionListener() {
			//保存
			@Override
			public void actionPerformed(ActionEvent e) {
				save();
			}
		});
		
		jmi2.addActionListener(new ActionListener() {
			//退出
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				jf.setVisible(false);
				fatService.removeOpenFile(fat);
				oftm.initData();
				jt.updateUI();
			}
		});
	}
	
	/**
	 * 保存数据
	 */
	private void save(){
		length = jta1.getText().length();
		if (length > ((File)fat.getObject()).getLength()-8){
			//添加内容的
			int num = FileSystemUtil.getNumOfFAT(length);
			if (num > 1){
				boolean boo = fatService.saveToModifyFATS2(this, num, fat);
				if (boo){
					file.setLength(length);
					file.setContent(jta1.getText());
				}
			} else {
				file.setLength(length);
				file.setContent(jta1.getText());
			}
			
			tm.initData();
			jta.updateUI();
		}
		
	}
	
	
	class WindowClosingListener extends WindowAdapter{

		@Override
		public void windowClosing(WindowEvent e) {
			if (!jta1.getText().equals(file.getContent())){
				int ret = MessageUtil.showConfirmMgs(jf, "还没有保存,是否保存");
				if (ret == 0){
					save();
				}
				fatService.removeOpenFile(fat);
				oftm.initData();
				jt.updateUI();
			}
			fatService.removeOpenFile(fat);
			oftm.initData();
			jt.updateUI();
		}
		
	}
	
}


