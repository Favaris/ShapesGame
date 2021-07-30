package shapesgame.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class DrawPanel extends JPanel 
{
	private static final long serialVersionUID = 1L;
	
	volatile private ShapesContainer shapesContainer = new ShapesContainer();
	transient private DrawPanelListener drlistener = new DrawPanelListener();
	
	DrawPanel()
	{
		setBorder(BorderFactory.createLineBorder(Color.BLACK, 10));
		this.addMouseListener(drlistener);
		setKeyBindings();
		Thread newThread = new Thread( () -> startUpdatingProcess(60L) );
		newThread.start();
	}
	
	@Override
	public void paintComponent(Graphics g)
	{
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setBackground(Color.WHITE);
		g2d.clearRect(0, 0, this.getWidth(), this.getHeight());
		
		shapesContainer.paintElements(g2d);
	}
	
	private void startUpdatingProcess(long fps)
	{
		//Pretty shitty solution to fps lock, should go back and do better
		//TODO: try Timer class to implement fps locker
		
		final long tickTime = 1000L / fps;
		
		while(isVisible())
		{
			updateMouseMoving();
			
			repaint();
			
			try
			{
				Thread.sleep(tickTime);
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	private void updateMouseMoving()
	{
		if (drlistener.isMovingVertex || drlistener.isMovingShape)
		{
			if (drlistener.previousMousePosition == null) //if this is the first time it is called
			{
				drlistener.previousMousePosition = MouseInfo.getPointerInfo().getLocation();
				SwingUtilities.convertPointFromScreen(drlistener.previousMousePosition, this);
			}
			else //else move the chosen vertex by current coords minus prev coords
			{
				Point currentMousePosition = MouseInfo.getPointerInfo().getLocation();
				SwingUtilities.convertPointFromScreen(currentMousePosition, this); //convert from absolute position to position relative to drawPanel
				
				if (drlistener.isMovingVertex) 
					shapesContainer.moveChosenVertex(currentMousePosition.x - drlistener.previousMousePosition.x, 
							currentMousePosition.y - drlistener.previousMousePosition.y);
				else if (drlistener.isMovingShape)
					shapesContainer.moveChosenShape(currentMousePosition.x - drlistener.previousMousePosition.x, 
							currentMousePosition.y - drlistener.previousMousePosition.y);
				
				drlistener.previousMousePosition = currentMousePosition;
			}
		}
	}
	
	enum ActionMapKeys
	{
		E_PRESSED("E_PRESSED"),
		R_PRESSED("R_PRESSED"),
		W_PRESSED("W_PRESSED"),
		A_PRESSED("A_PRESSED"),
		S_PRESSED("S_PRESSED"),
		D_PRESSED("D_PRESSED"), // depressed, just like me
		UP_PRESSED("UP_PRESSED"),
		RIGHT_PRESSED("RIGHT_PRESSED"),
		DOWN_PRESSED("DOWN_PRESSED"),
		LEFT_PRESSED("LEFT_PRESSED"),
		
		W_RELEASED("W_RELEASED"),
		A_RELEASED("A_RELEASED"),
		S_RELEASED("S_RELEASED"),
		D_RELEASED("D_RELEASED");
		
		private String name;
		ActionMapKeys(String name)
		{
			this.name = name;
		}
		public String getName()
		{
			return name;
		}
		
	}
	
	private void setKeyBindings()
	{
		ActionMap actionMap = getActionMap();
		InputMap inputMap = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		
		// хотел как можно красивше, думал "использую enum, буду крутым", в итоге размер кода только увеличился и стал еще больше нечитабельным :)
		// в будущем мб удали этот enum и сделай по человечески...
		
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_E, 0), ActionMapKeys.E_PRESSED.name);
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_R, 0), ActionMapKeys.R_PRESSED.name);
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_W, 0), ActionMapKeys.W_PRESSED.name);
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0), ActionMapKeys.A_PRESSED.name);
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0), ActionMapKeys.S_PRESSED.name);
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0), ActionMapKeys.D_PRESSED.name);
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), ActionMapKeys.UP_PRESSED.name);
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), ActionMapKeys.RIGHT_PRESSED.name);
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), ActionMapKeys.DOWN_PRESSED.name);
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), ActionMapKeys.LEFT_PRESSED.name);
		
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_W, 0, true), ActionMapKeys.W_RELEASED.name);
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0, true), ActionMapKeys.A_RELEASED.name);
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0, true), ActionMapKeys.S_RELEASED.name);
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0, true), ActionMapKeys.D_RELEASED.name);
		
		actionMap.put(ActionMapKeys.E_PRESSED.name, new KeyAction(ActionMapKeys.E_PRESSED.name));
		actionMap.put(ActionMapKeys.R_PRESSED.name, new KeyAction(ActionMapKeys.R_PRESSED.name));
		actionMap.put(ActionMapKeys.W_PRESSED.name, new KeyAction(ActionMapKeys.W_PRESSED.name));
		actionMap.put(ActionMapKeys.A_PRESSED.name, new KeyAction(ActionMapKeys.A_PRESSED.name));
		actionMap.put(ActionMapKeys.S_PRESSED.name, new KeyAction(ActionMapKeys.S_PRESSED.name));
		actionMap.put(ActionMapKeys.D_PRESSED.name, new KeyAction(ActionMapKeys.D_PRESSED.name));
		actionMap.put(ActionMapKeys.UP_PRESSED.name, new KeyAction(ActionMapKeys.UP_PRESSED.name));
		actionMap.put(ActionMapKeys.RIGHT_PRESSED.name, new KeyAction(ActionMapKeys.RIGHT_PRESSED.name));
		actionMap.put(ActionMapKeys.DOWN_PRESSED.name, new KeyAction(ActionMapKeys.DOWN_PRESSED.name));
		actionMap.put(ActionMapKeys.LEFT_PRESSED.name, new KeyAction(ActionMapKeys.LEFT_PRESSED.name));
		
		actionMap.put(ActionMapKeys.W_RELEASED.name, new KeyAction(ActionMapKeys.W_RELEASED.name));
		actionMap.put(ActionMapKeys.A_RELEASED.name, new KeyAction(ActionMapKeys.A_RELEASED.name));
		actionMap.put(ActionMapKeys.S_RELEASED.name, new KeyAction(ActionMapKeys.S_RELEASED.name));
		actionMap.put(ActionMapKeys.D_RELEASED.name, new KeyAction(ActionMapKeys.D_RELEASED.name));
	}
	
	private class KeyAction extends AbstractAction
	{
		/**
		 * TODO: THIS UID WAS GENERATED AUTOMATICALY BY ECLIPSE MAYBE YOU SHOULD CHANGE IT LATER
		 */
		private static final long serialVersionUID = -7436763374077364169L;

		public KeyAction(String actionCommand)
		{
			putValue(ACTION_COMMAND_KEY, actionCommand);
		}
		
		public void actionPerformed(ActionEvent actionEvt) 
		{
			ActionMapKeys key = ActionMapKeys.valueOf(actionEvt.getActionCommand());
			switch(key)
			{
				case E_PRESSED:
				{
					if (shapesContainer.getCurrentShape().npoints == 0) break;
					
					shapesContainer.newShape();
					
					break;
				}
				case R_PRESSED:
				{
					shapesContainer.goToNextShape();
					
					break;
				}
				case W_PRESSED:
				{
					shapesContainer.setMovingUp(true);
					
					break;
				}
				case A_PRESSED:
				{
					shapesContainer.setMovingLeft(true);
					
					break;
				}
				case S_PRESSED:
				{
					shapesContainer.setMovingDown(true);
					
					break;
				}
				case D_PRESSED:
				{
					shapesContainer.setMovingRight(true);
					
					break;
				}
				
				case W_RELEASED:
				{
					shapesContainer.setMovingUp(false);
					
					break;
				}
				case A_RELEASED:
				{
					shapesContainer.setMovingLeft(false);
					
					break;
				}
				case S_RELEASED:
				{
					shapesContainer.setMovingDown(false);
					
					break;
				}
				case D_RELEASED:
				{
					shapesContainer.setMovingRight(false);
					
					break;
				}
				case UP_PRESSED:
				{
					shapesContainer.scaleCurrentShape(1.01);
					
					break;
				}
				case DOWN_PRESSED:
				{
					shapesContainer.scaleCurrentShape(0.99);
					
					break;
				}
				case LEFT_PRESSED:
				{
					shapesContainer.rotateCurrentShape(2.0);
					
					break;
				}
				case RIGHT_PRESSED:
				{
					shapesContainer.rotateCurrentShape(-2.0);
					
					break;
				}
				default:
					break;	
			}
	    }
	}
	
	class DrawPanelListener implements MouseListener
	{
		public boolean isMovingVertex = false;
		public boolean isMovingShape = false;
		public Point previousMousePosition = null;
		
		@Override
		public void mouseClicked(MouseEvent e)
		{
			switch(e.getButton())
			{
				case MouseEvent.BUTTON1:
				{
					shapesContainer.addCircle(e.getX(), e.getY());
					
					break;
				}
				case MouseEvent.BUTTON2:
				{
					if (shapesContainer.searchForShapeIntersection(e.getPoint())) 
					{
						shapesContainer.setRandomColorForChosenShape();
					}
					
					break;
				}
				case MouseEvent.BUTTON3:
				{
					shapesContainer.removeLastCircle();
					
					break;
				}
				default:
				{
					break;
				}
			}
			
		}

		@Override
		public void mousePressed(MouseEvent e)
		{
			switch (e.getButton())
			{
				case MouseEvent.BUTTON1:
				{
					//System.out.println("Mouse pressed");
					
					isMovingVertex = shapesContainer.searchForVertexIntersection(e.getPoint());
					
					if (isMovingVertex == false)
					{
						isMovingShape = shapesContainer.searchForShapeIntersection(e.getPoint());
					}
					
					break;
				}
			}
		}

		@Override
		public void mouseReleased(MouseEvent e)
		{
			switch (e.getButton())
			{
				case MouseEvent.BUTTON1:
				{
					//System.out.println("Mouse released");
					
					if (isMovingVertex)
					{
						previousMousePosition = null;
						isMovingVertex = false;
					}
					else if (isMovingShape)
					{
						previousMousePosition = null;
						isMovingShape = false;
					}
					
					break;
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
		
	@Override
	public Dimension getPreferredSize() 
	{
		return new Dimension(600, 550);
	}
	
	public void setShapesContainer(ShapesContainer cont) 
	{
		shapesContainer = cont;
	}
	
	public void addNewShape()
	{
		shapesContainer.newShape();
	}
	
	public void goToNextShape()
	{
		shapesContainer.goToNextShape();
	}
	
	
	public ShapesContainer getShapesContainer()
	{
		return shapesContainer;
	}
	
}
