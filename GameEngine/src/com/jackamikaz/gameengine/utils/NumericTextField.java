package com.jackamikaz.gameengine.utils;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;

import javax.swing.JTextField;
 
public class NumericTextField extends JTextField
{
	private static final long serialVersionUID = 1L;
	private float oldValue;

	public NumericTextField(String _initialStr, int _col)
	{
		super (_initialStr, _col) ;
		oldValue = 0.0f;
		
		Reformat();
	}
	
	public NumericTextField (int _col) 
	{ 
		this ("", _col) ; 
	}
	
	public void processKeyEvent(KeyEvent e)
	{
		char c = e.getKeyChar() ;
		int k = e.getKeyCode();
		
		if (c == '.')
		{
			if (getText().contains("."))
				return;
		}
		else if (c == '-')
		{
			if (getCaretPosition() > 0 || getText().contains("-"))
				return;
		}
		else if (k == KeyEvent.VK_ENTER)
		{
			Reformat();
		}
		else
		if (!( (k == KeyEvent.VK_BACK_SPACE)
			|| (k == KeyEvent.VK_DELETE) 
			|| (k == KeyEvent.VK_TAB)
			|| (k == KeyEvent.VK_LEFT)
			|| (k == KeyEvent.VK_RIGHT)
			|| (Character.isDigit(c)))) 
		{
			return;
		}
		super.processKeyEvent(e);
	}
	
	public void processFocusEvent(FocusEvent e)
	{
		if (e.getID() == FocusEvent.FOCUS_LOST)
		{
			Reformat();
			fireActionPerformed();
		}
		super.processFocusEvent(e);
	}
	
	private void Reformat()
	{
		setValue(getValue());
	}
	
	public void setValue(float val)
	{
		oldValue = val;
		setText(Float.toString(val).replace(',', '.'));
	}
	
	public float getValue()
	{
		String t = getText();
		
		if (t.isEmpty())
			return oldValue;
		
		try
		{
			return Float.parseFloat(t);
		}
		catch (java.lang.NumberFormatException e)
		{
			return oldValue;
		}
	}
}