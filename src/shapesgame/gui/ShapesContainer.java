package shapesgame.gui;

import java.awt.*;
import java.awt.geom.*;
import java.io.*;
import java.util.*;

import icy.type.geom.Polygon2D;

public class ShapesContainer implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private ArrayList<CustomShape> shapes = new ArrayList<CustomShape>();
	private CustomShape current;
	
	private int chosenVertexIndex = -1;
	private int chosenShapeIndex = -1;
	
	
	ShapesContainer()
	{
		shapes.add(new CustomShape());
		current = shapes.get(0);
	}
	
	public void scaleCurrentShape(double scale)
	{
		current.scale(scale);
	}
	
	public void rotateCurrentShape(double angle)
	{
		current.rotate(angle);
	}
	
	public void addCircle(int x, int y)
	{
		current.addCircle(x, y);
	}
	
	public void removeLastCircle()
	{
		current.removeLastCircle();
	}
	
	public void moveChosenVertex(double deltaX, double deltaY)
	{
		current.moveVertex(deltaX, deltaY, chosenVertexIndex);
	}
	
	public void moveChosenShape(double deltaX, double deltaY)
	{
		shapes.get(chosenShapeIndex).move(deltaX, deltaY);
	}
	
	public void newShape()
	{
		shapes.add(new CustomShape());
		current = shapes.get(shapes.size() - 1);
	}
	
	public boolean searchForShapeIntersection(Point point)
	{
		if (current.getShape().contains(point))
		{
			chosenShapeIndex = shapes.indexOf(current);
			System.out.println("contains!");
			return true;
		}
		
		for (int i = shapes.size() - 1; i >= 0; --i)
		{
			if (shapes.get(i).getShape().contains(point))
			{
				chosenShapeIndex = i;
				System.out.println("contains!");
				return true;
			}
		}
		return false;
	}
	
	public boolean searchForVertexIntersection(Point point)
	{
		for (int i = 0; i < current.getCircList().size(); ++i)
		{
			if (current.getCircList().get(i).contains(point))
			{
				chosenVertexIndex = i;
				return true;
			}
		}
		return false;
	}
	
	public ArrayList<CustomShape> getAllShapes()
	{
		return shapes;
	}
	
	public void setRandomColorForChosenShape()
	{
		shapes.get(chosenShapeIndex).setRandomColor();
	}
	
	public void goToNextShape()
	{
		if (shapes.isEmpty()) return;
		
		if (current.getShape().npoints == 0)
		{
			int currentIndex = shapes.indexOf(current);
			shapes.remove(current);
			if (currentIndex < shapes.size() - 1)
				current = shapes.get(currentIndex);
			else current = shapes.get(0);
			
			return;
		}
		
		setMovingRight(false);
		setMovingLeft(false);
		setMovingUp(false);
		setMovingDown(false);
		
		if (shapes.indexOf(current) < shapes.size() - 1 )
		{
			current = shapes.get(shapes.indexOf(current) + 1);
		}
		else
		{
			current = shapes.get(0);
		}
	}
	
	public void moveCurrentShape(double x, double y)
	{
		current.move(x, y);
	}
	
	public Polygon2D getCurrentShape()
	{
		return current.getShape();
	}
	
	public void setMovingRight(boolean flag)
	{
		current.isMovingRight = flag;
	}
	
	public void setMovingLeft(boolean flag)
	{
		current.isMovingLeft = flag;
	}
	
	public void setMovingUp(boolean flag)
	{
		current.isMovingUp = flag;
	}
	
	public void setMovingDown(boolean flag)
	{
		current.isMovingDown = flag;
	}
	
	public void paintElements(Graphics2D g2d)
	{
		current.autoMove();
		
		for (CustomShape shp : shapes)
		{
			if (shp == current) continue;
			
			g2d.setColor(shp.getColor());
			g2d.fill(shp.getShape());
			g2d.setColor(Color.BLACK);
			g2d.draw(shp.getShape());
		}
		
		g2d.setColor(current.getColor());
		g2d.fill(current.getShape());
		g2d.setColor(Color.BLACK);
		g2d.draw(current.getShape());
		
		for (Ellipse2D.Double circ : current.getCircList())
		{
			g2d.draw(circ);
		}
	}
}
