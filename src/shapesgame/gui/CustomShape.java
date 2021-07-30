package shapesgame.gui;

import java.awt.*;
import java.io.*;
import java.awt.geom.*;
import java.util.*;

import icy.type.geom.Polygon2D;

public class CustomShape implements Serializable
{	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2L;
	
	private final static short CIRCLES_RADIUS = 3;
	private final static int MOVE_OFFSET = 2;
	
	private Polygon2D shape = new Polygon2D();
	private ArrayList<Ellipse2D.Double> circles = new ArrayList<Ellipse2D.Double>();
	private Color color = new Color((int) (Math.random() * 256), (int) (Math.random() * 256), (int) (Math.random() * 256));
	
	public boolean isMovingRight = false, isMovingLeft = false, isMovingUp = false, isMovingDown = false;
	
	CustomShape()
	{
		
	}
	
	public void addCircle(int x, int y)
	{
		circles.add(new Ellipse2D.Double(x - CIRCLES_RADIUS, y - CIRCLES_RADIUS, 2 * CIRCLES_RADIUS, 2 * CIRCLES_RADIUS));
		shape.addPoint(x, y);
	}
	
	public boolean isEmpty()
	{
		return shape.npoints == 0;
	}
	
	public void moveVertex(double deltaX, double deltaY, int chosenVertexIndex)
	{
		shape.xpoints[chosenVertexIndex] += deltaX;
		shape.ypoints[chosenVertexIndex] += deltaY;
		shape.calculatePath();
		
		Ellipse2D.Double newCircle = new Ellipse2D.Double(circles.get(chosenVertexIndex).x + deltaX, circles.get(chosenVertexIndex).y + deltaY, 2 * CIRCLES_RADIUS, 2 * CIRCLES_RADIUS);
		circles.remove(chosenVertexIndex);
		circles.add(chosenVertexIndex, newCircle);
	}
	
	public void removeLastCircle()
	{
		if (!circles.isEmpty() && shape.npoints > 0)
		{
			circles.remove(circles.size() - 1);
			
			if (shape.npoints > 1)
			{
				double[] xPoints = new double[shape.npoints - 1];
				double[] yPoints = new double[shape.npoints - 1];
		
				for (int i = 0; i < shape.npoints - 1; ++i)
				{
					xPoints[i] = shape.xpoints[i];
					yPoints[i] = shape.ypoints[i];
				}
				
				shape = new Polygon2D(xPoints, yPoints, xPoints.length);
			}
			else shape = new Polygon2D();
			
		}
	}
	
	//rotation around shape's center
	public void rotate(double angle)
	{
		if (shape.npoints <= 1) return; //cant rotate a single point
		
		angle = Math.PI * angle / 180.;
		
		Point2D.Double shapesCenter = new Point2D.Double();
		shapesCenter.x = shape.getBounds2D().getCenterX();
		shapesCenter.y = shape.getBounds().getCenterY();
		
		for (int i = 0; i < shape.npoints; ++i)
		{
			double newXpos = shapesCenter.x + (shape.xpoints[i] - shapesCenter.x) * Math.cos(angle) + (shape.ypoints[i] - shapesCenter.y) * Math.sin(angle);
			double newYpos = shapesCenter.y - (shape.xpoints[i] - shapesCenter.x) * Math.sin(angle) + (shape.ypoints[i] - shapesCenter.y) * Math.cos(angle);
			
			shape.xpoints[i] = newXpos;
			shape.ypoints[i] = newYpos;
		}
		
		shape.calculatePath();
		
		for (int i = 0; i < circles.size(); ++i)
		{
			Ellipse2D.Double newCircle = new Ellipse2D.Double(shape.xpoints[i] - CIRCLES_RADIUS, shape.ypoints[i] - CIRCLES_RADIUS, 2 * CIRCLES_RADIUS, 2 * CIRCLES_RADIUS);
			circles.remove(i);
			circles.add(i, newCircle);
		}
	}
	
	public void scale(double scale)
	{
		if (shape.npoints <= 1) return; //cant scale a single point
		
		
		Point2D.Double shapesCenter = new Point2D.Double();
		shapesCenter.x = shape.getBounds2D().getCenterX();
		shapesCenter.y = shape.getBounds().getCenterY();
		
		for (int i = 0; i < shape.npoints; ++i)
		{
			shape.xpoints[i] -= shapesCenter.x;
			shape.ypoints[i] -= shapesCenter.y;
			
			shape.xpoints[i] *= scale;
			shape.ypoints[i] *= scale;
			
			shape.xpoints[i] += shapesCenter.x;
			shape.ypoints[i] += shapesCenter.y;
 		}
		
		shape.calculatePath();
		
		for (int i = 0; i < circles.size(); ++i)
		{
			Ellipse2D.Double newCircle = new Ellipse2D.Double(shape.xpoints[i] - CIRCLES_RADIUS, shape.ypoints[i] - CIRCLES_RADIUS, 2 * CIRCLES_RADIUS, 2 * CIRCLES_RADIUS);
			circles.remove(i);
			circles.add(i, newCircle);
		}
	}
	
	public void autoMove()
	{
		if (isMovingRight) move(MOVE_OFFSET, 0);
		if (isMovingLeft) move(-MOVE_OFFSET, 0);
		if (isMovingUp) move(0, -MOVE_OFFSET);
		if (isMovingDown) move(0, MOVE_OFFSET);
	}
	
	public void move(double deltaX, double deltaY)
	{
		shape.translate(deltaX, deltaY);

		for (int i = 0; i < circles.size(); ++i)
		{
			Ellipse2D.Double newCircle = new Ellipse2D.Double(circles.get(i).x + deltaX, circles.get(i).y + deltaY, 2 * CIRCLES_RADIUS, 2 * CIRCLES_RADIUS);
			circles.remove(i);
			circles.add(i, newCircle);
		}
	}
	
	public Polygon2D getShape()
	{
		return shape;
	}
	
	public ArrayList<Ellipse2D.Double> getCircList()
	{
		return circles;
	}

	public Color getColor()
	{
		return color;
	}

	public void setColor(Color color)
	{
		this.color = color;
	}
	
	public void setRandomColor()
	{
		this.color = new Color((int) (Math.random() * 256), (int) (Math.random() * 256), (int) (Math.random() * 256));
	}
}
