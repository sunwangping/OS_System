package com.csh.filesystem.viewer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextPane;
import javax.swing.JTree;
import javax.swing.SwingConstants;
import javax.swing.tree.DefaultMutableTreeNode;

import com.csh.filesystem.model.Disk;
import com.csh.filesystem.model.File;
import com.csh.filesystem.model.Folder;
import com.csh.filesystem.util.FileSystemUtil;
import com.csh.filesystem.util.MessageUtil;

public class Tree extends JPanel {
	
	private static final long serialVersionUID = 2352829445429133249L;
	private JTree tree;
	private JScrollPane jsp1, jsp2;
	private JSplitPane jsp;
	private JPanel jp1;
	private JLabel jl, jl2, jl3, jl4;
	private JLabel[] jLabel;
	private JList middle;
	private MainFrame mainFrame;

	public Tree() {
		DefaultMutableTreeNode node1 = new DefaultMutableTreeNode(new Disk("C"));
		jp1 = new JPanel();
		tree = new JTree(node1);
		jsp1 = new JScrollPane(tree);
		
		jp1.setLayout(new FlowLayout(FlowLayout.LEFT));
		jp1.setBackground(Color.white);
		
		final int n = 101;
		jp1.setPreferredSize(new Dimension(482, FileSystemUtil.getHeight(n)));
		jLabel = new JLabel[n];
		for (int i=0; i<n; i++){
			jLabel[i] = new MyJLabel(true, "文件" +i);
			jp1.add(jLabel[i]);
			jLabel[i].addMouseListener(new MouseListener() {
				
				@Override
				public void mouseReleased(MouseEvent e) {
//					jl.setIcon(new ImageIcon(FileSystemUtil.file1Path));
					
				}
				
				@Override
				public void mousePressed(MouseEvent e) {
					
				}
				
				@Override
				public void mouseExited(MouseEvent e) {
					// TODO Auto-generated method stub
					//jl.setIcon(new ImageIcon(FileSystemUtil.filePath));
					for (int j=0; j<n; j++){
						if (e.getSource() == jLabel[j]){
							jLabel[j].setIcon(new ImageIcon(FileSystemUtil.filePath));
						}
					}
				}
				
				@Override
				public void mouseEntered(MouseEvent e) {
					for (int j=0; j<n; j++){
						if (e.getSource() == jLabel[j]){
							jLabel[j].setIcon(new ImageIcon(FileSystemUtil.file1Path));
						}
					}
				}
				
				@Override
				public void mouseClicked(MouseEvent e) {
					// TODO Auto-generated method stub
					if (e.getClickCount() == 2){
						MessageUtil.showMgs(jsp2, "哈哈~逗你玩");
					}
				}
			});
		}
		
		
		jsp2 = new JScrollPane(jp1);
		jsp2.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		jsp2.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		jsp2.setPreferredSize(new Dimension(482, 515));
//		jsp2.setSize(482, 515);
		jsp2.setBackground(Color.white);
		jsp2.setViewportView(jp1);
		jsp1.setPreferredSize(new Dimension(200, 515));
		
		jsp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,jsp1,jsp2);
		
		jsp.setDividerSize(0);
		jsp.setDividerLocation(200);
		jsp.setEnabled(false);
		
		this.add(jsp);
	}
}
