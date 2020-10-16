package com.csh.filesystem.viewer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.border.Border;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import com.csh.filesystem.model.Disk;
import com.csh.filesystem.model.FAT;
import com.csh.filesystem.model.File;
import com.csh.filesystem.model.Folder;
import com.csh.filesystem.service.FATService;
import com.csh.filesystem.util.FileSystemUtil;
import com.csh.filesystem.util.MessageUtil;

public class MainFrame extends JFrame {
	
	private JPanel jp1, jp2, jp3, jp4, jp5;
	private JTextField jtf1;
	private Tree jtr;
	private JTable jta,jta2;
	private JScrollPane jsp1, jsp2;
	private JMenuBar jmb;
	private JMenu jm;
	private JMenuItem jmi;
	private JLabel jl1, jl2, jl3;
	private JButton jb1;
	private TableModel tm;
	private int n;
	private boolean isFile;
	private OpenFileTableModel oftm;
	
	private Map<String, DefaultMutableTreeNode> map; //�����״չʾ��·�����ͽڵ�
	private FATService fatService;
	private List<FAT> fatList;
	private int fatIndex = 0;
	
	
	public MainFrame(){
		oftm = new OpenFileTableModel();
		fatList = new ArrayList<FAT>();
		map = new HashMap<String, DefaultMutableTreeNode>();
		initService();
		//��ʼ��
		initMainFrame();
		
		this.jmiAddListener();
		
		//�˵���
		jm.add(jmi);
		jmb.add(jm);
		
		//JTable
		tm = new TableModel();
		jta = new JTable(tm);
		jsp1 = new JScrollPane(jta);
		jsp1.setPreferredSize(new Dimension(300, 355));

		//JTable openfile
		jta2 = new JTable(oftm);
		jsp2 = new JScrollPane(jta2);
		jsp2.setPreferredSize(new Dimension(300, 100));
		
		jb1AddListener();
		
		//�Ϸ��Ĳ�ѯ
		jtf1.setPreferredSize(new Dimension(450, 20));
		jtf1.setText("C:");
		jp2.add(jl2);
		jp2.add(jtf1);
		JLabel jspace = new JLabel("                                                         ");
		jp2.add(jspace);
		jp2.add(jb1);
		
		jp5.add(jl1);
		jp5.add(jsp1, BorderLayout.CENTER);
		jp5.add(jl3);
		jp5.add(jsp2);
		
		//tree
		jp2.setLayout(new FlowLayout(FlowLayout.LEFT));
		jp2.setPreferredSize(new Dimension(1000, 30));
		jp5.setPreferredSize(new Dimension(300, 500));
		jp1.setLayout(new BorderLayout());
		jp1.add(jp2, BorderLayout.NORTH);
		jp1.add(jtr, BorderLayout.WEST);
		jp1.add(jp5, BorderLayout.EAST);
		
		
		//����MainJFrame����
		this.setTitle("ģ������ļ�ϵͳ");
		this.setSize(1000, 600);
		this.setLocation(200, 100);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setLayout(new BorderLayout());
		
		this.add(jmb, BorderLayout.NORTH);
		this.add(jp1, BorderLayout.CENTER);
		
		//����ͼƬ
		ImageIcon image = new ImageIcon(getClass().getResource(FileSystemUtil.imgPath));
		this.setIconImage(image.getImage());
		
		this.setVisible(true);
	}
	
	//ϵͳ����
	private void jmiAddListener(){
		jmi.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new JieShao(jp1);
			}
		});
	}
	
	private void jb1AddListener(){
		jb1.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				new HelpDialog(jp1);
			}
		});
	}
	
	//��ʼ����̨����
	private void initService() {
		fatService = new FATService();
		fatService.initFAT();
	}

	public void initMainFrame(){
		jp1 = new JPanel(); 
		jp2 = new JPanel(); 
		jp3 = new JPanel(); 
		jp4 = new JPanel(); 
		jp5 = new JPanel(); 
		jtf1 = new JTextField();
		jtr = new Tree();
		jl1 = new JLabel("���̷���");
		jl3 = new JLabel("�Ѵ��ļ�");
		jl2 = new JLabel("                   ·����");
		jmb = new JMenuBar();
		jm = new JMenu("ϵͳ");
		jmi = new JMenuItem("����");
		jb1 = new JButton("����");
	}
	
	
	/*
	 * ��
	 */
	public class Tree extends JPanel {
		
		private static final long serialVersionUID = 2352829445429133249L;
		private JTree tree;
		private JScrollPane jsp1, jsp2;
		private JSplitPane jsp;
		private JPanel jp1;
		private MyJLabel[] jLabel;
		private JPopupMenu pm, pm2;
		private JMenuItem mi1, mi2, mi3, mi4, mi5, mi6;
		private DefaultMutableTreeNode node1;

		public Tree() {
			
			this.initMenuItem();
			this.initMenuItenByJLabel();
			this.menuItemAddListener();
			
			this.initTree();
			this.treeAddListener();
			this.jPanelAddListener();
			
			jp1.setLayout(new FlowLayout(FlowLayout.LEFT));
			jp1.setBackground(Color.white);
			jp1.add(pm);
			
			jsp2 = new JScrollPane(jp1);
			jsp2.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			jsp2.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			jsp2.setPreferredSize(new Dimension(482, 515));
			jsp2.setBackground(Color.white);
			jsp2.setViewportView(jp1);
			
			jsp1.setPreferredSize(new Dimension(200, 515));
			
			jsp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,jsp1,jsp2);
			
			jsp.setDividerSize(0);
			jsp.setDividerLocation(200);
			jsp.setEnabled(false);
			
			this.add(jsp);
		}
		
		/**
		 * ��ʼ����
		 */
		private void initTree() {
			node1 = new DefaultMutableTreeNode(new Disk("C"));
			map.put("C:", node1);
			jp1 = new JPanel();
			tree = new JTree(node1);
			jsp1 = new JScrollPane(tree);
		}
		
		private void treeAddListener(){
			tree.addMouseListener(new MouseListener() {
				
				@Override
				public void mouseReleased(MouseEvent e) {
				}
				
				@Override
				public void mousePressed(MouseEvent e) {
				}
				
				@Override
				public void mouseExited(MouseEvent e) {
				}
				
				@Override
				public void mouseEntered(MouseEvent e) {
				}
				
				@Override
				public void mouseClicked(MouseEvent e) {
					//�ı��ַ��·��
					TreePath path = tree.getSelectionPath();
					if (path != null){
						String pathStr = path.toString().replace("[", "").replace("]", "").replace(",", "\\").replace(" ", "").replaceFirst("C", "C:");
						jtf1.setText(pathStr);
						
						//����jp1
						jp1.removeAll();
						addJLabel(fatService.getFATs(pathStr), pathStr);
						jp1.updateUI();
					}
				}
			});
		}
		
		/**
		 * ����������JLabel
		 */
		private void addJLabel(List<FAT> fats, String path){
			fatList = fats;
			isFile = true;
			n = fats.size();
			System.out.println(n);
			jp1.setPreferredSize(new Dimension(482, FileSystemUtil.getHeight(n)));
			jLabel = new MyJLabel[n];
			for (int i=0; i<n; i++){
				if (fats.get(i).getIndex() == FileSystemUtil.END){
					if (fats.get(i).getType() == FileSystemUtil.FOLDER){
						isFile = false;
						jLabel[i] = new MyJLabel(isFile, ((Folder)fats.get(i).getObject()).getFolderName());
					} else {
						isFile = true;
						jLabel[i] = new MyJLabel(isFile, ((File)fats.get(i).getObject()).getFileName());
					}
					jp1.add(jLabel[i]);
					jLabel[i].add(pm2);
					jLabel[i].addMouseListener(new MouseListener() {
						
						@Override
						public void mouseReleased(MouseEvent e) {
							
						}
						
						@Override
						public void mousePressed(MouseEvent e) {
							for (int j=0; j<n; j++){
								if (e.getSource() == jLabel[j] && ((e.getModifiers() & InputEvent.BUTTON3_MASK)!=0)){
									pm2.show(jLabel[j], e.getX(), e.getY());
								}
							}
						}
						
						@Override
						public void mouseExited(MouseEvent e) {
							for (int j=0; j<n; j++){
								if (e.getSource() == jLabel[j]){
									fatIndex = j;
									if (jLabel[j].type){
										jLabel[j].setIcon(new ImageIcon(getClass().getResource(FileSystemUtil.filePath)));
									} else {
										jLabel[j].setIcon(new ImageIcon(getClass().getResource(FileSystemUtil.folderPath)));
									}
								}
							}
						}
						
						@Override
						public void mouseEntered(MouseEvent e) {
							for (int j=0; j<n; j++){
								if (e.getSource() == jLabel[j]){
									fatIndex = j;
									if (jLabel[j].type){
										jLabel[j].setIcon(new ImageIcon(getClass().getResource(FileSystemUtil.file1Path)));
									} else {
										jLabel[j].setIcon(new ImageIcon(getClass().getResource(FileSystemUtil.folder1Path)));
									}
								}
							}
						}
						
						@Override
						public void mouseClicked(MouseEvent e) {
							if (e.getClickCount() == 2){
								if (fatList.get(fatIndex).getType() == FileSystemUtil.FILE){
									//�ļ�
									if (fatService.getOpenFiles().getFiles().size() < FileSystemUtil.num){
										if (fatService.checkOpenFile(fatList.get(fatIndex))){
											MessageUtil.showErrorMgs(jp1, "�ļ��Ѵ�");
											return;
										}
										fatService.addOpenFile(fatList.get(fatIndex), FileSystemUtil.flagWrite);
										oftm.initData();
										jta2.updateUI();
										new OpenFileJFrame(fatList.get(fatIndex), fatService, oftm, jta2, tm, jta);
									} else {
										MessageUtil.showErrorMgs(jp1, "�Ѿ���5���ļ��ˣ��ﵽ����");
									}
									
								} else {
									//�ļ���
									Folder folder = (Folder)fatList.get(fatIndex).getObject();
									String path = folder.getLocation() + "\\" + folder.getFolderName();
									
									jp1.removeAll();
									addJLabel(fatService.getFATs(path), path);
									jp1.updateUI();
									jtf1.setText(path);
								}
							}
						}
					});
				}
			}
		}
		
		/**
		 * �������Ӽ�����
		 */
		private void jPanelAddListener() {
			//����Ҽ�ʱ���¼�
			jp1.addMouseListener(new MouseListener() {
				
				@Override
				public void mouseReleased(MouseEvent e) {
				}
				
				@Override
				public void mousePressed(MouseEvent e) {
					int mods = e.getModifiers();
					if ((mods&InputEvent.BUTTON3_MASK) != 0){
						pm.show(jp1, e.getX(), e.getY());
					}
				}
				
				@Override
				public void mouseExited(MouseEvent e) {
					
				}
				
				@Override
				public void mouseEntered(MouseEvent e) {
					
				}
				
				@Override
				public void mouseClicked(MouseEvent e) {
					
				}
			});			
		}

		//��ʼ���Ҽ��˵�
		public void initMenuItem(){
			pm = new JPopupMenu();
			mi1 = new JMenuItem("�½��ļ�");
			mi2 = new JMenuItem("�½��ļ���");
			pm.add(mi1);
			pm.add(mi2);
		}
		
		public void initMenuItenByJLabel(){
			pm2 = new JPopupMenu();
			mi3 = new JMenuItem("��");
			mi4 = new JMenuItem("������");
			mi5 = new JMenuItem("ɾ��");
			mi6 = new JMenuItem("����");
			pm2.add(mi3);
			pm2.add(mi4);
			pm2.add(mi5);
			pm2.add(mi6);
		}
		
		/**
		 * ����Ҽ�ѡ����Ӽ�����
		 */
		public void menuItemAddListener(){
			mi1.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					int index = fatService.createFile(jtf1.getText());
					if (index == FileSystemUtil.ERROR){
						MessageUtil.showErrorMgs(jp1, "�����������޷������ļ�");
					} else {
//						FAT fat = fatService.getFAT(index);
//						DefaultMutableTreeNode node = new DefaultMutableTreeNode((File)(fat.getObject()));
//						map.put(jtf1.getText() + "\\" + ((File)(fat.getObject())).getFileName(), node);
//						DefaultMutableTreeNode nodeParent = map.get(jtf1.getText());
//						nodeParent.add(node);
						tree.updateUI();
						tm.initData();
						jta.updateUI();
						
						jp1.removeAll();
						addJLabel(fatService.getFATs(jtf1.getText()), jtf1.getText());
						jp1.updateUI();
					}
				}
			});
			mi2.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					int index = fatService.createFolder(jtf1.getText());
					if (index == FileSystemUtil.ERROR){
						MessageUtil.showErrorMgs(jp1, "�����������޷������ļ���");
					} else {
						FAT fat = fatService.getFAT(index);
						DefaultMutableTreeNode node = new DefaultMutableTreeNode((Folder)(fat.getObject()));
						map.put(jtf1.getText() + "\\" + ((Folder)(fat.getObject())).getFolderName(), node);
						DefaultMutableTreeNode nodeParent = map.get(jtf1.getText());
						nodeParent.add(node);
						tree.updateUI();
						tm.initData();
						jta.updateUI();
						
						jp1.removeAll();
						addJLabel(fatService.getFATs(jtf1.getText()), jtf1.getText());
						jp1.updateUI();
					}
				}
			
			});
			mi3.addActionListener(new ActionListener() {
				//��
				@Override
				public void actionPerformed(ActionEvent e){
					if (fatList.get(fatIndex).getType() == FileSystemUtil.FILE){
						//�ļ�
						if (fatService.getOpenFiles().getFiles().size() < FileSystemUtil.num){
							if (fatService.checkOpenFile(fatList.get(fatIndex))){
								MessageUtil.showErrorMgs(jp1, "�ļ��Ѵ�");
								return;
							}
							fatService.addOpenFile(fatList.get(fatIndex), FileSystemUtil.flagWrite);
							oftm.initData();
							jta2.updateUI();
							new OpenFileJFrame(fatList.get(fatIndex), fatService, oftm, jta2, tm, jta);
						} else {
							MessageUtil.showErrorMgs(jp1, "�Ѿ���5���ļ��ˣ��ﵽ����");
						}
						
					} else {
						//�ļ���
						Folder folder = (Folder)fatList.get(fatIndex).getObject();
						String path = folder.getLocation() + "\\" + folder.getFolderName();
						
						jp1.removeAll();
						addJLabel(fatService.getFATs(path), path);
						jp1.updateUI();
						jtf1.setText(path);
					}
				}
			});
			mi4.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
System.out.println("������");
					new ShowRenameDialog(jp1, fatList.get(fatIndex), map, fatService);
					tree.updateUI();
					tm.initData();
					jta.updateUI();
					
					jp1.removeAll();
					addJLabel(fatService.getFATs(jtf1.getText()), jtf1.getText());
					jp1.updateUI();
				}
			});
			mi5.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					int i = MessageUtil.showConfirmMgs(jp1, "�Ƿ�ȷ��Ҫɾ�����ļ���");
					if (i==0){
						fatService.delete(jp1, fatList.get(fatIndex), map);
						
						tree.updateUI();
						tm.initData();
						jta.updateUI();
						
						jp1.removeAll();
						addJLabel(fatService.getFATs(jtf1.getText()), jtf1.getText());
						jp1.updateUI();
					}
					
				}
			});
			mi6.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
System.out.println("����");
					new ShowPropertyDialog(jp1, fatList.get(fatIndex));
				}
			});
		}
		
		
	}
}
