package com.csh.filesystem.viewer;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import com.csh.filesystem.model.FAT;

public class HelpDialog extends JDialog {
	
	private JDialog jd1;
	private JLabel jl;
	
	public HelpDialog(Component c){
		jd1 = this;
		this.setTitle("帮助");
		this.setSize(280, 330);
		this.setLayout(new FlowLayout());
		this.setModal(true);
		this.setLocationRelativeTo(c);
		this.addJLabel();
		this.setVisible(true);
	}
	
	private void addJLabel(){
		jl = new JLabel();
		String text = "<html>帮助：<br><br>中间的面板中点击右键：<br>1、创建文件；<br>2、创建文件夹<br><br>";
		text += "点击文件夹或文件右键：<br>";
		text += "1、打开<br>";
		text += "2、重命名<br>";
		text += "3、删除<br>";
		text += "4、属性<br><br>";
		text += "双击文件夹或文件则是打开<br><br><br>";
		text += "Thank you!";
		text += "</html>";
		jl.setText(text);
		this.add(jl);
	}
}
