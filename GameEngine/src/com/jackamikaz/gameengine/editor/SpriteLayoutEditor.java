package com.jackamikaz.gameengine.editor;

import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ImmediateModeRenderer;
import com.badlogic.gdx.graphics.glutils.ImmediateModeRenderer10;
import com.badlogic.gdx.graphics.glutils.ImmediateModeRenderer20;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.jackamikaz.gameengine.DisplayMaster;
import com.jackamikaz.gameengine.DisplayedEntity;
import com.jackamikaz.gameengine.Engine;
import com.jackamikaz.gameengine.InputEntity;
import com.jackamikaz.gameengine.resources.ResTexture;
import com.jackamikaz.gameengine.utils.Action;
import com.jackamikaz.gameengine.utils.ActionOnCondition;
import com.jackamikaz.gameengine.utils.ButtonWatcher;
import com.jackamikaz.gameengine.utils.InputCondition;
import com.jackamikaz.gameengine.utils.InputWatcher;
import com.jackamikaz.gameengine.utils.KeyWatcher;
import com.jackamikaz.gameengine.utils.NumericTextField;

public class SpriteLayoutEditor implements DisplayedEntity, InputEntity, Action {

	private class SpriteExt extends Sprite {
		public String name = "unnamed";
		public String resname = null;
		
		private boolean needResCheck = false;
		
		public SpriteExt() {
			super();
		}
		
		public SpriteExt(SpriteExt spr) {
			super(spr);
			resname = spr.resname;
		}
		
		public void SetResource(String name) {
			resname = name;
			needResCheck = true;
		}
		
		public void CheckResource() {
			if (needResCheck && currentSprite != null) {
				needResCheck = false;
				Texture tex = Engine.ResourceManager().GetTexture(resname);
				if (tex != null) {
					float x = getX();
					float y = getY();
					float sx = getScaleX();
					float sy = getScaleY();
					float ox = getOriginX();
					float oy = getOriginY();
					float rot = getRotation();
					set(new Sprite(tex));
					setPosition(x, y);
					setScale(sx,sy);
					setOrigin(ox, oy);
					setRotation(rot);
				}
			}
		}
		
		@Override
		public void draw(SpriteBatch batch) {
			CheckResource();
			super.draw(batch);
		}
		
		public void draw(SpriteBatch batch, float alphaModulation) {
			CheckResource();
			super.draw(batch, alphaModulation);
		}
	}
	
	private LinkedList<SpriteExt> sprites;
	private SpriteExt currentSprite;
	
	private OrthographicCamera camera;
	private Vector2 camPos;
	private float camZoom;
	
	// window stuff
	private JFrame frame;
	private JFileChooser fc;
	
	private JButton next;
	private JButton back;
	private JButton load;
	private JButton save;
	private JButton shiftUp;
	private JButton shiftDown;
	private JButton add;
	private JButton remove;
	
	private JTextField name;
	private JComboBox resource;
	
	private NumericTextField posx;
	private NumericTextField posy;
	private NumericTextField sizex;
	private NumericTextField sizey;
	private NumericTextField originx;
	private NumericTextField originy;
	private NumericTextField rotation;
	
	private boolean isRefreshing = false;
	
	private ImmediateModeRenderer renderer;
	
	private InputWatcher iptSelect;
	private InputWatcher iptTurn;
	private InputWatcher iptOrigin;
	private float rotOnClic;
	
	public void ActivateOnKey(int key) {
		KeyWatcher kw = new KeyWatcher(key);
		
		Engine.InputMaster().Add(kw);
		Engine.UpdateMaster().Add(new ActionOnCondition(this, new InputCondition(kw)));
	}
	
	@Override
	public void DoAction() {
		Start();
	}
	
	public SpriteLayoutEditor() {
		
		iptSelect = new ButtonWatcher(Buttons.LEFT);
		iptTurn = new ButtonWatcher(Buttons.MIDDLE);
		iptOrigin = new ButtonWatcher(Buttons.RIGHT);
		
		camera = new OrthographicCamera();
		camPos = new Vector2(0.0f,0.0f);
		camZoom = 1.0f;
		
		sprites = new LinkedList<SpriteExt>();
		currentSprite = new SpriteExt();
		sprites.add(currentSprite);
		
		frame = new JFrame();
		fc = new JFileChooser(System.getProperty("user.dir"));
		
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(0,2));
		
		load = new JButton("load");
		save = new JButton("save");
		next = new JButton("next");
		back = new JButton("previous");
		shiftUp = new JButton("shift up");
		shiftDown = new JButton("shift down");
		add = new JButton("add");
		remove = new JButton("remove");
		
		name = new JTextField();
		resource = new JComboBox(Engine.ResourceManager().ExtractResourceNameOfType(ResTexture.class));
		
		posx = new NumericTextField(4);
		posy = new NumericTextField(4);
		sizex = new NumericTextField(4);
		sizey = new NumericTextField(4);
		originx = new NumericTextField(4);
		originy = new NumericTextField(4);
		rotation = new NumericTextField(4);
		
		panel.add(load);
		panel.add(save);
		panel.add(back);
		panel.add(next);
		panel.add(shiftDown);
		panel.add(shiftUp);
		panel.add(add);
		panel.add(remove);
		panel.add(name);
		panel.add(resource);
		panel.add(posx);
		panel.add(posy);
		panel.add(sizex);
		panel.add(sizey);
		panel.add(originx);
		panel.add(originy);
		panel.add(new Label("rotation : "));
		panel.add(rotation);
		
		resource.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent action) {
				if (!isRefreshing && currentSprite != null) {
					String resname = (String)resource.getSelectedItem();
					currentSprite.SetResource(resname);
				}
			}
		});
		
		posx.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent action) {
				if (currentSprite != null)
					currentSprite.setPosition(	posx.getValue(),
												currentSprite.getY());
			}
		});
		
		posy.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent action) {
				if (currentSprite != null)
					currentSprite.setPosition(	currentSprite.getX(),
												posy.getValue());
			}
		});
		
		sizex.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent action) {
				if (currentSprite != null)
					currentSprite.setScale(	sizex.getValue(),
											currentSprite.getScaleY());
			}
		});
		
		sizey.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent action) {
				if (currentSprite != null)
					currentSprite.setScale(	currentSprite.getScaleX(),
											sizey.getValue());
			}
		});
		
		originx.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent action) {
				if (currentSprite != null)
					currentSprite.setOrigin(originx.getValue(),
											currentSprite.getOriginY());
			}
		});
		
		originy.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent action) {
				if (currentSprite != null)
					currentSprite.setOrigin(currentSprite.getOriginX(),
											originy.getValue());
			}
		});
		
		rotation.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent action) {
				if (currentSprite != null)
					currentSprite.setRotation(Angle(rotation.getValue()));
			}
		});
		
		add.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (currentSprite != null) {
					currentSprite = new SpriteExt(currentSprite);
					sprites.add(currentSprite);
					currentSprite.setPosition(0.0f, 0.0f);
					UpdateFields();
				}
			}
		});
		
		remove.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (sprites.size() > 1 && currentSprite != null) {
					int idx = sprites.indexOf(currentSprite);
					sprites.remove(idx);
					if (idx >= sprites.size())
						idx = 0;
					currentSprite = sprites.get(idx);
					UpdateFields();
				}
			}
		});
		
		next.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (!sprites.isEmpty()) {
					int idx = sprites.indexOf(currentSprite)+1;
					if (idx >= sprites.size())
						idx = 0;
					currentSprite = sprites.get(idx);
					UpdateFields();
				}
			}
		});
		
		back.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (!sprites.isEmpty()) {
					int idx = sprites.indexOf(currentSprite)-1;
					if (idx < 0)
						idx = sprites.size() - 1;
					currentSprite = sprites.get(idx);
					UpdateFields();
				}
			}
		});
		
		shiftDown.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (currentSprite != null) {
					int idx = sprites.indexOf(currentSprite);
					
					sprites.remove(idx);
					
					idx -= 1;
					if (idx < 0)
						idx = 0;
					
					sprites.add(idx, currentSprite);
				}
			}
		});
		
		shiftUp.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (currentSprite != null) {
					int idx = sprites.indexOf(currentSprite);
					
					sprites.remove(idx);
					
					idx += 1;
					if (idx > sprites.size())
						idx = sprites.size();
					
					sprites.add(idx, currentSprite);
				}
			}
		});
		
		name.addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent arg0) {
				if (currentSprite != null)
					currentSprite.name = name.getText();
			}
			
			@Override
			public void focusGained(FocusEvent arg0) {}
		});
		
		save.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				String check = CheckSaveValidity();
				if (check == null) {
					// save popup
					int returnVal = fc.showSaveDialog(frame);
					
					if (returnVal == JFileChooser.APPROVE_OPTION) {
						File f = fc.getSelectedFile();
						if (f != null)
							Save(f);
					}
				}
				else {
					// error popup
					JOptionPane.showMessageDialog(frame, check, "Validity error", JOptionPane.ERROR_MESSAGE);
				}
					
			}
		});
		
		load.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				// warning popup (current will be cleared)
				int ok = JOptionPane.OK_OPTION;
				if (!sprites.isEmpty())
					ok = JOptionPane.showConfirmDialog(frame, "The current layout will be cleared.\nContinue?", "Be careful", JOptionPane.OK_CANCEL_OPTION);
				if (ok == JOptionPane.OK_OPTION) {
					int returnVal = fc.showOpenDialog(frame);
					
					if (returnVal == JFileChooser.APPROVE_OPTION) {
						File f = fc.getSelectedFile();
						if (f != null)
							Load(Gdx.files.absolute(f.getAbsolutePath()));
					}
				}
			}
		});
		
		frame.add(panel);
		frame.pack();
		
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e){ 
		    	Engine.PopAll();
		    	frame.setVisible(false);
		    }
		});

		if (Gdx.graphics.isGL20Available())
			renderer = new ImmediateModeRenderer20(false, false, 0);
		else
			renderer = new ImmediateModeRenderer10();
		
		UpdateFields();
	}
	
	
	private void UpdateFields() {
		isRefreshing = true;
		if (currentSprite != null) {
			
			name.setEditable(true);
			posx.setEditable(true);
			posy.setEditable(true);
			sizex.setEditable(true);
			sizey.setEditable(true);
			originx.setEditable(true);
			originy.setEditable(true);
			rotation.setEditable(true);
			resource.setEditable(true);
			
			name.setText(currentSprite.name);
			posx.setValue(currentSprite.getX());
			posy.setValue(currentSprite.getY());
			sizex.setValue(currentSprite.getScaleX());
			sizey.setValue(currentSprite.getScaleY());
			originx.setValue(currentSprite.getOriginX());
			originy.setValue(currentSprite.getOriginY());
			rotation.setValue(currentSprite.getRotation());
			resource.setSelectedItem(currentSprite.resname);
			
		}
		else
		{
			name.setEditable(false);
			posx.setEditable(false);
			posy.setEditable(false);
			sizex.setEditable(false);
			sizey.setEditable(false);
			originx.setEditable(false);
			originy.setEditable(false);
			rotation.setEditable(false);
			resource.setEditable(false);
		}
		isRefreshing = false;
	}
	
	public void Start() {
		if (!frame.isVisible()) {
			frame.setVisible(true);
			
			Engine.PushAll();
			Engine.DisplayMaster().Add(this);
			Engine.InputMaster().Add(this);
		}
	}

	@Override
	public void Display(float lerp) {
		Gdx.gl.glClearColor(0.8f, 0.8f, 0.8f, 1.0f);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		DisplayMaster dm = Engine.DisplayMaster();
		camera.setToOrtho(false, dm.GetWidth(), dm.GetHeight());
		camera.position.x = camPos.x;
		camera.position.y = camPos.y;
		camera.zoom = camZoom;
		camera.update();
		
		Engine.Batch().setProjectionMatrix(camera.projection);
		Engine.Batch().setTransformMatrix(camera.view);
		Engine.Batch().begin();
		
		for(SpriteExt spr : sprites) {
			spr.CheckResource();
			if (spr.getTexture() != null)
				spr.draw(Engine.Batch());
		}
		
		Engine.Batch().end();
		
		if (currentSprite != null) {
			
			Rectangle r = currentSprite.getBoundingRectangle();
			renderer.begin(camera.combined, GL10.GL_LINE_STRIP);
			
			renderer.vertex(r.x			, r.y			, 0.0f);
			renderer.vertex(r.x+r.width	, r.y			, 0.0f);
			renderer.vertex(r.x+r.width	, r.y+r.height	, 0.0f);
			renderer.vertex(r.x			, r.y+r.height	, 0.0f);
			renderer.vertex(r.x			, r.y			, 0.0f);
			
			renderer.end();
			
			float ox = currentSprite.getX() + currentSprite.getOriginX();
			float oy = currentSprite.getY() + currentSprite.getOriginY();
			
			renderer.begin(camera.combined, GL10.GL_LINES);
			
			renderer.vertex(ox			, r.y			, 0.0f);
			renderer.vertex(ox			, r.y+r.height	, 0.0f);
			renderer.vertex(r.x			, oy			, 0.0f);
			renderer.vertex(r.x+r.width	, oy			, 0.0f);
			
			renderer.end();
		}
	}

	@Override
	public int GetDisplayRank() {
		return 0;
	}
	
	static final private float Angle(float val) {
		val = val % 360.0f;
		if (val < 0.0f)
			return val + 360.0f;
		else
			return val;
	}
	
	@Override
	public void NewInput(Input input) {
		
		iptSelect.NewInput(input);
		iptTurn.NewInput(input);
		iptOrigin.NewInput(input);
		
		Vector2 clicPos = new Vector2(
				-Engine.DisplayMaster().GetWidth()/2  + input.getX(),
				Engine.DisplayMaster().GetHeight()/2 - input.getY());
		clicPos.add(camPos);
		
		if (iptSelect.wasPressed()) {
			currentSprite = null;
			for (SpriteExt spr : sprites) {
				Rectangle r = spr.getBoundingRectangle();
				
				if (clicPos.x > r.x
				&&	clicPos.x < r.x+r.width
				&&	clicPos.y > r.y
				&&	clicPos.y < r.y+r.height) {
					currentSprite = spr;
				}
			}
			UpdateFields();
		}
		else if (iptTurn.isPressed()
				&& input.isKeyPressed(Keys.CONTROL_LEFT)) {
					camPos.x -= input.getDeltaX();
					camPos.y += input.getDeltaY();
				}
		else if (iptTurn.wasPressed() && currentSprite != null) {
			Vector2.tmp.set(
					currentSprite.getX()+currentSprite.getOriginX(),
					currentSprite.getY()+currentSprite.getOriginY());
			Vector2.tmp.sub(clicPos);
			rotOnClic = Vector2.tmp.angle()-currentSprite.getRotation();
		}
		else if (iptTurn.isPressed() && currentSprite != null) {
			Vector2.tmp.set(
					currentSprite.getX()+currentSprite.getOriginX(),
					currentSprite.getY()+currentSprite.getOriginY());
			Vector2.tmp.sub(clicPos);
			float rot = Vector2.tmp.angle();
			currentSprite.setRotation(Angle(rot-rotOnClic));
			UpdateFields();
		}
		else if (iptOrigin.wasPressed()) {
			float cr = MathUtils.cosDeg(currentSprite.getRotation());
			float sr = MathUtils.sinDeg(currentSprite.getRotation());
			
			float ox = currentSprite.getOriginX();
			float oy = currentSprite.getOriginY();
			
			float nox = clicPos.x - (currentSprite.getX() + ox);
			float noy = clicPos.y - (currentSprite.getY() + oy);
			
			currentSprite.setOrigin(ox+nox*cr+noy*sr, oy+noy*cr-nox*sr);
			currentSprite.translate(
					clicPos.x - (currentSprite.getX() + currentSprite.getOriginX()),
					clicPos.y - (currentSprite.getY() + currentSprite.getOriginY()));
			UpdateFields();
		}
		
		if (iptSelect.isPressed() && currentSprite != null) {
			currentSprite.translate(input.getDeltaX(), -input.getDeltaY());
			UpdateFields();
		}
	}
	
	private String CheckSaveValidity() {
		
		// Names must not contain spaces or tabs, and not be emtpy
		Pattern pattern = Pattern.compile("\\s");
		for(SpriteExt spr : sprites) {
			Matcher matcher = pattern.matcher(spr.name);
			if(matcher.find())
				return "Cannot save file because the name \""+spr.name+"\" contains spaces";
			if(spr.name.length() == 0)
				return "Cannot save file because there is at least one empty name";
		}
		
		// Names must be unique
		for(int i=0; i<sprites.size()-1; ++i) {
			String curName = sprites.get(i).name;
			for(int j=i+1; j<sprites.size(); ++j) {
				if (curName.equalsIgnoreCase(sprites.get(j).name))
					return "Cannot save file because the name \""+curName+"\" is not unique";
			}
		}
		
		return null;
	}
	
	private void Save(File file) {
		
		try {
			FileWriter fw = new FileWriter(file);
			
			for (SpriteExt spr : sprites) {
				fw.write( spr.name + " "
						+ spr.resname + " "
						+ spr.getX() + " " + spr.getY() + " "
						+ spr.getScaleX() + " " + spr.getScaleY() + " "
						+ spr.getOriginX() + " " + spr.getOriginY() + " "
						+ spr.getRotation() + "\r\n");
			}
			
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void Load(FileHandle file) {
		
		sprites.clear();
		currentSprite = null;
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(file.read()));
		String line;

		try {
			line = reader.readLine();
			
			while (line != null) {
				String[] tokens = line.split("[ ]+");
				
				if (!(tokens.length >= 4)) {
					sprites.clear();
					JOptionPane.showMessageDialog(frame, "The selected file is not valid.","File read error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				SpriteExt spr = new SpriteExt();
				spr.name = tokens[0];
				spr.SetResource(tokens[1]);
				spr.setPosition(Float.parseFloat(tokens[2]), Float.parseFloat(tokens[3]));
				if (tokens.length >= 6)
					spr.setScale(Float.parseFloat(tokens[4]), Float.parseFloat(tokens[5]));
				if (tokens.length >= 8)
					spr.setOrigin(Float.parseFloat(tokens[6]), Float.parseFloat(tokens[7]));
				if (tokens.length >= 9)
					spr.setRotation(Float.parseFloat(tokens[8]));
				
				sprites.add(spr);
				
				line = reader.readLine();
			}
			
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if (!sprites.isEmpty())
			currentSprite = sprites.getFirst();
		
		UpdateFields();
	}
}
