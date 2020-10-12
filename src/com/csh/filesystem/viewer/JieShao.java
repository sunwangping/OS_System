package com.csh.filesystem.viewer;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.image.BufferStrategy;

import javax.swing.JDialog;
import javax.swing.JLabel;

public class JieShao extends JDialog {
	private JDialog jd1;
	private JLabel jl;
	
	public JieShao(Component c){
		jd1 = this;
		this.setTitle("介绍");
		this.setSize(280, 330);
		this.setLayout(new FlowLayout());
		this.setModal(true);
		this.setLocationRelativeTo(c);
		this.addJLabel();
		this.setVisible(true);
	}
	
	private void addJLabel(){
		jl = new JLabel();
		StringBuffer text = new StringBuffer();
		text.append("<html>");
		text.append("模拟磁盘文件系统<br><br>");
		text.append("组员：<br><br>");
		text.append("XXXXXXX");
		text.append("</html>");
		jl.setText(text.toString());
		this.add(jl);
	}
}
