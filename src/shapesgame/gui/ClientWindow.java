package shapesgame.gui;

import javax.swing.*;

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
		frame = new JFrame("ShapesGame v1.0");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//mainPanel.setLayout(new BorderLayout());
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));
		
		setUpButtonPanel();
		
		//mainPanel.add(BorderLayout.WEST, drawPanel);
		//mainPanel.add(BorderLayout.CENTER, buttonPanel);
	
		mainPanel.add(drawPanel);
		Dimension minSize = new Dimension(30, 600);
		Dimension prefSize = new Dimension(50, 600);
		Dimension maxSize = new Dimension(Short.MAX_VALUE, 600);
		mainPanel.add(new Box.Filler(minSize, prefSize, maxSize));
		//mainPanel.add(Box.createHorizontalGlue());
		mainPanel.add(buttonPanel);
		//mainPanel.add(Box.createHorizontalGlue());
		
		
		//frame.getContentPane().add(BorderLayout.CENTER, drawPanel);
		frame.getContentPane().add(mainPanel);
		frame.setSize(800, 600);
		frame.setVisible(true);
	}
	
	private void setUpButtonPanel()
	{
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.PAGE_AXIS));
		buttonPanel.setBackground(Color.blue);
		
		
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
		
		JButton saveButton = new JButton("Save");
		saveButton.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) { saveDrawPanel(); }
		});
		saveButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		JButton downloadButton = new JButton("Download");
		downloadButton.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) {  downloadDrawPanel(); }
		});
		downloadButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		JButton UpButton = new JButton("Up");
		UpButton.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) { drawPanel.getShapesContainer().moveCurrentShape(0., -2.); }
		});
		UpButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		JPanel controlsPanel = new JPanel();
		controlsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		controlsPanel.setBackground(Color.cyan);
		//controlsPanel.setPreferredSize(new Dimension(150, 50));
		
		JButton LeftButton = new JButton("Left");
		LeftButton.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) {  drawPanel.getShapesContainer().moveCurrentShape(-2., 0.); }
		});
		LeftButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		JButton DownButton = new JButton("Down");
		DownButton.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) {  drawPanel.getShapesContainer().moveCurrentShape(0., 2.); }
		});
		DownButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		JButton RightButton = new JButton("Right");
		RightButton.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) {  drawPanel.getShapesContainer().moveCurrentShape(2., 0.); }
		});
		RightButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		controlsPanel.add(LeftButton);
		controlsPanel.add(DownButton);
		controlsPanel.add(RightButton);
		
		buttonPanel.add(Box.createVerticalGlue());
		buttonPanel.add(newShapeButton);
		//buttonPanel.add(Box.createVerticalGlue());
		buttonPanel.add(goToNextShapeButton);
		buttonPanel.add(saveButton);
		buttonPanel.add(downloadButton);
		buttonPanel.add(Box.createVerticalGlue());
		buttonPanel.add(UpButton);
		buttonPanel.add(controlsPanel);
		buttonPanel.add(Box.createVerticalGlue());
	}
	
	private void downloadDrawPanel()
	{
		String filename = "test.ser";
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename)))
		{
			DrawPanel newDrawPanel = (DrawPanel) ois.readObject();
			drawPanel.setShapesContainer(newDrawPanel.getShapesContainer());
		} 
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		System.out.println("Downloaded succesfully!");
	}
	
	private void saveDrawPanel()
	{
		String filename = "test.ser";
		try (ObjectOutputStream ois = new ObjectOutputStream(new FileOutputStream(filename)))
		{
			ois.writeObject(drawPanel);
		} 
		catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Saved succesfully!");
	}
}
