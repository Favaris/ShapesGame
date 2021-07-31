package shapesgame.gui;

import javax.swing.*;
//import javax.swing.filechooser.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class ClientWindow
{
	private JFrame frame;
	private JPanel mainPanel = new JPanel();
	private DrawPanel drawPanel = new DrawPanel();
	private JPanel buttonPanel = new JPanel();
	
	public void start()
	{
		frame = new JFrame("ShapesGame v1.1");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false); // disable resizing because when I shrink the window, the button panel gets very big and the draw panel almost disappears 
		// TODO: solve the problem described above
		
		mainPanel.setLayout(new BorderLayout());
		//mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));
		
		setUpButtonPanel();
		
		mainPanel.add(BorderLayout.WEST, drawPanel);
		mainPanel.add(BorderLayout.CENTER, buttonPanel);
	
		//mainPanel.add(drawPanel);
		//Dimension minSize = new Dimension(30, 600);
		//Dimension prefSize = new Dimension(30, 600);
		//Dimension maxSize = new Dimension(30, 600);
		//mainPanel.add(new Box.Filler(minSize, prefSize, maxSize));
		//mainPanel.add(Box.createHorizontalGlue());
		//mainPanel.add(buttonPanel);
		//mainPanel.add(Box.createHorizontalGlue());
		
		
		//frame.getContentPane().add(BorderLayout.CENTER, drawPanel);
		frame.getContentPane().add(mainPanel);
		frame.setSize(800, 600);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
	private void setUpButtonPanel()
	{
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
		buttonPanel.setBackground(Color.gray);
		buttonPanel.setMinimumSize(new Dimension(200, 600));
		buttonPanel.setMaximumSize(new Dimension(200, Short.MAX_VALUE));
		
		
		JButton newShapeButton = new JButton("New shape");
		//newShapeButton.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		newShapeButton.addActionListener(new ActionListener() 
				{
					public void actionPerformed(ActionEvent e) { drawPanel.addNewShape(); }
				});
		newShapeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		
		JButton goToNextShapeButton = new JButton("Go to the next shape");
		goToNextShapeButton.addActionListener(new ActionListener() 
				{
					public void actionPerformed(ActionEvent e) { drawPanel.goToNextShape(); }
				});
		goToNextShapeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		JButton controlsInfoButton = new JButton("Controls");
		controlsInfoButton.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) { 
				JFrame infoFrame = new JFrame();
				infoFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				
				String infoText = "<html><br>To create a shape, simply click anywhere on the window</br>"
						+ "<br>You can drag shapes by using the left mouse button, or drag any vertex of the selected shape</br>"
						+ "<br>You can change any shape's color by clicking on it with the scroll button</br>"
						+ "<br>You can delete the last added vertex by clicking the right mouse button</br>"
						+ "<br>W, A, S, D - controls</br>"
						+ "<br>E - new shape</br>"
						+ "<br>R - go to the next shape</br>"
						+ "<br>Arrows - scale/rotate</br></html>";
				
				JLabel info = new JLabel(infoText);
				
				infoFrame.add(info);
				infoFrame.setSize(250, 300);
				infoFrame.setLocationRelativeTo(null);
				infoFrame.setVisible(true);
			}
		});
		controlsInfoButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		buttonPanel.add(Box.createVerticalGlue());
		buttonPanel.add(newShapeButton);
		//buttonPanel.add(Box.createVerticalGlue());
		buttonPanel.add(goToNextShapeButton);
		buttonPanel.add(new SaveOrDownloadPanel());
		buttonPanel.add(Box.createVerticalGlue());
		buttonPanel.add(new ControlButtonsPanel());
		buttonPanel.add(Box.createVerticalGlue());
		buttonPanel.add(controlsInfoButton);
		buttonPanel.add(Box.createVerticalGlue());
	}
	
	private class SaveOrDownloadPanel extends JPanel
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		private JButton saveButton = new JButton("Save");
		private JButton downloadButton = new JButton("Download");
		
		private JFileChooser fileChooser = new JFileChooser();
		
		SaveOrDownloadPanel()
		{
			//setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			setUpButtons();
			setUpFileChooser();
		}
		
		private void setUpButtons()
		{
						
			saveButton.setAlignmentX(Component.CENTER_ALIGNMENT);
			saveButton.setAlignmentY(Component.CENTER_ALIGNMENT);
			saveButton.setPreferredSize(new Dimension(140, 30));
			
			downloadButton.setAlignmentX(Component.CENTER_ALIGNMENT);
			downloadButton.setAlignmentY(Component.CENTER_ALIGNMENT);
			downloadButton.setPreferredSize(new Dimension(140, 30));
			
			add(saveButton);
			
			add(downloadButton);		
		}
		
		private void setUpFileChooser()
		{
			saveButton.addActionListener((ActionEvent e) -> 
			{
				fileChooser.setDialogTitle("Save the progress");
				
				fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				
				int result = fileChooser.showSaveDialog(mainPanel);
				if (result == JFileChooser.APPROVE_OPTION)
				{
					File file = fileChooser.getSelectedFile();
					saveShapes(file);
					System.out.println("Successfully saved in the " + file.getName() + " file!");
				}
			});
			
			downloadButton.addActionListener((ActionEvent e) -> 
			{
				fileChooser.setDialogTitle("Download the progress");
				
				fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				
				int result = fileChooser.showOpenDialog(mainPanel);
				if (result == JFileChooser.APPROVE_OPTION)
				{
					File file = fileChooser.getSelectedFile();
					downloadShapes(file);
					System.out.println("Successfully downloaded from the " + file.getName() + " file!");
				}
			});
		}
	}
	
	private class ControlButtonsPanel extends JPanel
	{
		/**
		 * this class contains control buttons
		 * i do not use action listeners since i want the shape to move/scale/rotate all the time the user is pressing a button
		 * instead of them i use mouse listeners 
		 */
		private static final long serialVersionUID = 1L;
		
		private static final short ICON_SIZE = 32;
		
		private static final String UP_ICON_PATH = "icons/arrow-up.png";
		private static final String LEFT_ICON_PATH = "icons/arrow-left.png";
		private static final String DOWN_ICON_PATH = "icons/arrow-down.png";
		private static final String RIGHT_ICON_PATH = "icons/arrow-right.png";
		private static final String UP_PRESSED_ICON_PATH = "icons/arrow-up-pressed.png";
		private static final String LEFT_PRESSED_ICON_PATH = "icons/arrow-left-pressed.png";
		private static final String DOWN_PRESSED_ICON_PATH = "icons/arrow-down-pressed.png";
		private static final String RIGHT_PRESSED_ICON_PATH = "icons/arrow-right-pressed.png";
		private static final String ROTATE_LEFT_ICON_PATH = "icons/arrow-rotate-left.png";
		private static final String ROTATE_RIGHT_ICON_PATH = "icons/arrow-rotate-right.png";
		private static final String ROTATE_LEFT_PRESSED_ICON_PATH = "icons/arrow-rotate-left-pressed.png";
		private static final String ROTATE_RIGHT_PRESSED_ICON_PATH = "icons/arrow-rotate-right-pressed.png";
		private static final String CHANGE_COLOR_ICON_PATH = "icons/circle-change-color.png";
		private static final String CHANGE_COLOR_PRESSED_ICON_PATH = "icons/circle-change-color-pressed.png";
		private static final String ENLARGE_ICON_PATH = "icons/enlarge.png";
		private static final String ENLARGE_PRESSED_ICON_PATH = "icons/enlarge-pressed.png";
		private static final String SHRINK_ICON_PATH = "icons/shrink.png";
		private static final String SHRINK_PRESSED_ICON_PATH = "icons/shrink-pressed.png";
		
		private JButton UpButton = new JButton();
		private JButton LeftButton = new JButton();
		private JButton DownButton = new JButton();
		private JButton RightButton = new JButton();
		private JButton RotLeftButton = new JButton();
		private JButton RotRightButton = new JButton();
		private JButton ChangeColorButton = new JButton();
		private JButton EnlargeButton = new JButton();
		private JButton ShrinkButton = new JButton();
		
		ControlButtonsPanel()
		{
			setUpButtons();
			this.setBackground(Color.gray);
			setAlignmentX(CENTER_ALIGNMENT);
			setMaximumSize(new Dimension(3 * ICON_SIZE + 20, 3 * ICON_SIZE + 20)); //3 icons * icon_size + 20 - flowlayout adds a 5 pixel gap between each component
		}
		
		@Override
		public Dimension getPreferredSize() 
		{
			return new Dimension(3 * ICON_SIZE + 20, 3 * ICON_SIZE + 20);
		}
		
		private void setUpButtons()
		{
			UpButton.addMouseListener(new ButtonMouseListener(ButtonMouseListener.UP));
			UpButton.setIcon(new ImageIcon(UP_ICON_PATH));
			UpButton.setRolloverIcon(new ImageIcon(UP_ICON_PATH));
			UpButton.setPressedIcon(new ImageIcon(UP_PRESSED_ICON_PATH));
			UpButton.setDisabledIcon(new ImageIcon(UP_ICON_PATH));
			setUpCommonFeatures(UpButton);
			
			LeftButton.addMouseListener(new ButtonMouseListener(ButtonMouseListener.LEFT));
			LeftButton.setIcon(new ImageIcon(LEFT_ICON_PATH));
			LeftButton.setRolloverIcon(new ImageIcon(LEFT_ICON_PATH));
			LeftButton.setPressedIcon(new ImageIcon(LEFT_PRESSED_ICON_PATH));
			LeftButton.setDisabledIcon(new ImageIcon(LEFT_ICON_PATH));
			setUpCommonFeatures(LeftButton);
			
			DownButton.addMouseListener(new ButtonMouseListener(ButtonMouseListener.DOWN));
			DownButton.setIcon(new ImageIcon(DOWN_ICON_PATH));
			DownButton.setRolloverIcon(new ImageIcon(DOWN_ICON_PATH));
			DownButton.setPressedIcon(new ImageIcon(DOWN_PRESSED_ICON_PATH));
			DownButton.setDisabledIcon(new ImageIcon(DOWN_ICON_PATH));
			setUpCommonFeatures(DownButton);
		
			RightButton.addMouseListener(new ButtonMouseListener(ButtonMouseListener.RIGHT));
			RightButton.setIcon(new ImageIcon(RIGHT_ICON_PATH));
			RightButton.setRolloverIcon(new ImageIcon(RIGHT_ICON_PATH));
			RightButton.setPressedIcon(new ImageIcon(RIGHT_PRESSED_ICON_PATH));
			RightButton.setDisabledIcon(new ImageIcon(RIGHT_ICON_PATH));
			setUpCommonFeatures(RightButton);
			
			RotLeftButton.addMouseListener(new ButtonMouseListener(ButtonMouseListener.ROTATE_LEFT));
			RotLeftButton.setIcon(new ImageIcon(ROTATE_LEFT_ICON_PATH));
			RotLeftButton.setRolloverIcon(new ImageIcon(ROTATE_LEFT_ICON_PATH));
			RotLeftButton.setPressedIcon(new ImageIcon(ROTATE_LEFT_PRESSED_ICON_PATH));
			RotLeftButton.setDisabledIcon(new ImageIcon(ROTATE_LEFT_ICON_PATH));
			setUpCommonFeatures(RotLeftButton);
			
			RotRightButton.addMouseListener(new ButtonMouseListener(ButtonMouseListener.ROTATE_RIGHT));
			RotRightButton.setIcon(new ImageIcon(ROTATE_RIGHT_ICON_PATH));
			RotRightButton.setRolloverIcon(new ImageIcon(ROTATE_RIGHT_ICON_PATH));
			RotRightButton.setPressedIcon(new ImageIcon(ROTATE_RIGHT_PRESSED_ICON_PATH));
			RotRightButton.setDisabledIcon(new ImageIcon(ROTATE_RIGHT_ICON_PATH));
			setUpCommonFeatures(RotRightButton);
			
			ChangeColorButton.addMouseListener(new ButtonMouseListener(ButtonMouseListener.CHANGE_COLOR));
			ChangeColorButton.setIcon(new ImageIcon(CHANGE_COLOR_ICON_PATH));
			ChangeColorButton.setRolloverIcon(new ImageIcon(CHANGE_COLOR_ICON_PATH));
			ChangeColorButton.setPressedIcon(new ImageIcon(CHANGE_COLOR_PRESSED_ICON_PATH));
			ChangeColorButton.setDisabledIcon(new ImageIcon(CHANGE_COLOR_ICON_PATH));
			setUpCommonFeatures(ChangeColorButton);
			
			EnlargeButton.addMouseListener(new ButtonMouseListener(ButtonMouseListener.ENLARGE));
			EnlargeButton.setIcon(new ImageIcon(ENLARGE_ICON_PATH));
			EnlargeButton.setRolloverIcon(new ImageIcon(ENLARGE_ICON_PATH));
			EnlargeButton.setPressedIcon(new ImageIcon(ENLARGE_PRESSED_ICON_PATH));
			EnlargeButton.setDisabledIcon(new ImageIcon(ENLARGE_ICON_PATH));
			setUpCommonFeatures(EnlargeButton);
			
			ShrinkButton.addMouseListener(new ButtonMouseListener(ButtonMouseListener.SHRINK));
			ShrinkButton.setIcon(new ImageIcon(SHRINK_ICON_PATH));
			ShrinkButton.setRolloverIcon(new ImageIcon(SHRINK_ICON_PATH));
			ShrinkButton.setPressedIcon(new ImageIcon(SHRINK_PRESSED_ICON_PATH));
			ShrinkButton.setDisabledIcon(new ImageIcon(SHRINK_ICON_PATH));
			setUpCommonFeatures(ShrinkButton);
			
			add(RotLeftButton);
			add(UpButton);
			add(RotRightButton);
			add(LeftButton);
			add(ChangeColorButton);
			add(RightButton);
			add(ShrinkButton);
			add(DownButton);
			add(EnlargeButton);
		}
		
		private void setUpCommonFeatures(JButton button)
		{
			button.setBorderPainted(false);
			button.setFocusPainted(false);
			button.setContentAreaFilled(false);
			button.setPreferredSize(new Dimension(ICON_SIZE, ICON_SIZE));
			button.setAlignmentX(Component.CENTER_ALIGNMENT);
		}
		
		private class ButtonMouseListener implements MouseListener
		{
			public static final String RIGHT = "right";
			public static final String LEFT = "left";
			public static final String UP = "up";
			public static final String DOWN = "down";
			public static final String ROTATE_LEFT = "rotate_left";
			public static final String ROTATE_RIGHT = "rotate_right";
			public static final String CHANGE_COLOR = "change_color";
			public static final String ENLARGE = "enlarge";
			public static final String SHRINK = "shrink";
			
			private String direction;
			
			ButtonMouseListener(String direction)
			{
				this.direction = direction;
			}

			@Override
			public void mouseClicked(MouseEvent e)
			{
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent e)
			{
				if (e.getButton() == MouseEvent.BUTTON1)
				{
					switch (direction)
					{
						case LEFT:
						{
							drawPanel.getShapesContainer().setMovingLeft(true);
							break;
						}
						case RIGHT:
						{
							drawPanel.getShapesContainer().setMovingRight(true);
							break;
						}
						case UP:
						{
							drawPanel.getShapesContainer().setMovingUp(true);
							break;
						}
						case DOWN:
						{
							drawPanel.getShapesContainer().setMovingDown(true);
							break;
						}
						case ROTATE_LEFT:
						{
							drawPanel.getShapesContainer().setRotatingLeft(true);
							break;
						}
						case ROTATE_RIGHT:
						{
							drawPanel.getShapesContainer().setRotatingRight(true);
							break;
						}
						case CHANGE_COLOR:
						{
							drawPanel.getShapesContainer().setRandomColorForCurrentShape();
							break;
						}
						case ENLARGE:
						{
							drawPanel.getShapesContainer().setEnlarging(true);
							break;
						}
						case SHRINK:
						{
							drawPanel.getShapesContainer().setShrinking(true);
							break;
						}
						default: break;
					}
				}
			}

			@Override
			public void mouseReleased(MouseEvent e)
			{
				if (e.getButton() == MouseEvent.BUTTON1)
				{
					switch (direction)
					{
						case LEFT:
						{
							drawPanel.getShapesContainer().setMovingLeft(false);
							break;
						}
						case RIGHT:
						{
							drawPanel.getShapesContainer().setMovingRight(false);
							break;
						}
						case UP:
						{
							drawPanel.getShapesContainer().setMovingUp(false);
							break;
						}
						case DOWN:
						{
							drawPanel.getShapesContainer().setMovingDown(false);
							break;
						}
						case ROTATE_LEFT:
						{
							drawPanel.getShapesContainer().setRotatingLeft(false);
							break;
						}
						case ROTATE_RIGHT:
						{
							drawPanel.getShapesContainer().setRotatingRight(false);
							break;
						}
						case ENLARGE:
						{
							drawPanel.getShapesContainer().setEnlarging(false);
							break;
						}
						case SHRINK:
						{
							drawPanel.getShapesContainer().setShrinking(false);
							break;
						}
						default: break;
					}
				}
			}

			@Override
			public void mouseEntered(MouseEvent e)
			{
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent e)
			{
				// TODO Auto-generated method stub
				
			}
			
		}
	}
	
	
	private void downloadShapes(File file)
	{
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file)))
		{
			Object newObj = ois.readObject();
			if (newObj instanceof ShapesContainer)
			{
				drawPanel.setShapesContainer((ShapesContainer) newObj);

				System.out.println("Downloaded succesfully!");
			}
			// else we can throw some type of exception but I don't know what exactly 
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		} 
		
	}
	
	
	private void saveShapes(File file)
	{
		try (ObjectOutputStream ois = new ObjectOutputStream(new FileOutputStream(file)))
		{
			ois.writeObject(drawPanel.getShapesContainer()); //we save only shapes container, there is no need to save whole draw panel
			System.out.println("Saved succesfully!");
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		} 
	}
}
