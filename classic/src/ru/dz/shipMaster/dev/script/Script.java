package ru.dz.shipMaster.dev.script;


import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.CountDownLatch;

import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.script.SimpleBindings;
import javax.script.SimpleScriptContext;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import ru.dz.shipMaster.config.items.CliParameter.Direction;
import ru.dz.shipMaster.config.items.CliParameter.Type;
import ru.dz.shipMaster.data.misc.CommunicationsException;
import ru.dz.shipMaster.dev.DriverPort;
import ru.dz.shipMaster.dev.PortDataOutput;
import ru.dz.shipMaster.dev.ThreadedDriver;

/**
 * This driver lets user to execute his script periodically.
 * @author dz
 */
public class Script extends ThreadedDriver {
	private String name = "New Script";
	private String script = "";
	
	protected static final int N_INPUTS = 4;
	protected static final int N_OUTPUTS = 4;
	
	protected final double [] inputs = new double [N_INPUTS];
	protected final double [] outputs = new double [N_OUTPUTS];
	private DriverPort [] inputPort = new DriverPort [N_INPUTS];
	
	
	// -----------------------------------------------------------
	// Init 
	// -----------------------------------------------------------
	
	/**
	 * Default constructor.
	 */
	public Script() {
		super(1000, Thread.NORM_PRIORITY, "Script driver");
	}

	@Override
	protected void doDriverTask() throws Throwable {

		// Run script
		String out = executeCommand(script);
		if(out != null)
		{
			out = out.trim();

			if( out.length() > 0 )
				log.severe("Script "+name+": "+out);
		}
		
		// Now pass script generated data to system
		for( int i = 0; i < N_INPUTS; i++ )
		{
			inputPort[i].sendDoubleData(inputs[i]);
		}
	}

	@Override
	protected void doStartDriver() throws CommunicationsException {
		setupScripting();
	}

	@Override
	protected void doStopDriver() throws CommunicationsException {
		// Empty
	}

	@Override
	public String getDeviceName() { return "Script"; }

	@Override
	public String getInstanceName() {		return name;	}

	@Override
	public boolean isAutoSeachSupported() { return false; }


	@Override
	protected void updatePorts(Vector<DriverPort> ports) {
		DriverPort.absentize(ports);
		int i;
		
		for( i = 0; i < N_OUTPUTS; i++ )
		{
			final int outputNo = i;
			int portNo = outputNo*2+1;
			String portName = "output no. "+outputNo;
			
			getPort(ports, portNo, Direction.Output, Type.Numeric, portName).setPortDataOutput(
					new PortDataOutput() {
				@Override
				public void receiveDoubleData(double value) {
					outputs[outputNo] = value; 
				}});
		}

		for( i = 0; i < N_INPUTS; i++ )
		{
			final int inputNo = i;
			int portNo = inputNo*2+0;
			String portName = "input no. "+inputNo;
			inputPort [inputNo] = getPort(ports, portNo, Direction.Input, Type.Numeric, portName);
		}

	}

// -----------------------------------------------------------
//	UI part 
// -----------------------------------------------------------

	private JTextField nameField = new JTextField(36);
	private JTextArea scriptField = new JTextArea(10,36);
	
	@Override
	protected void setupPanel(JPanel panel) {
		panel.add(new JLabel("Name:"), consL);
		panel.add(nameField, consR);

		panel.add(new JLabel("Script:"), consL);
		panel.add(scriptField, consR);
		// hack - textarea has small font by default
		scriptField.setFont(nameField.getFont());

	}

	@Override
	protected void doLoadPanelSettings() {
		nameField.setText(name);
		scriptField.setText(script);
	}

	@Override
	protected void doSavePanelSettings() {
		name = nameField.getText();
		script = scriptField.getText();
	}


	// -----------------------------------------------------------
	// Help 
	// -----------------------------------------------------------

	{
		scriptField.setToolTipText(
				"Sccript will be executed once a second. Array 'outputs' will " +
				"contain data from the system (may change during script run), " +
				"array 'inputs' will be translated back to system after script" +
				" is finished."
				);
	}


	// -----------------------------------------------------------
	// Engine part 
	// -----------------------------------------------------------

    // script engine initialization occurs in background.
    // This latch is used to coorrdinate engine init and eval.
    private CountDownLatch engineReady = new CountDownLatch(1);
	
    // Script engine that evaluates scripts
    private volatile ScriptEngine engine;
	
	
	private void setupScripting()
	{
		engineReady = new CountDownLatch(1);
        createScriptEngine();

        new Thread(new Runnable() {
            public void run() {
                // initialize the script engine
                setGlobals();
                loadInitFile();
                engineReady.countDown();
            }
        }).start();		
	}
	
    private ScriptEngineManager manager = new ScriptEngineManager();
	
    private void createScriptEngine() {
        String language = "JavaScript"; //getScriptLanguage();
        engine = manager.getEngineByName(language);
        if (engine == null) {
            throw new RuntimeException("cannot load " + language + " engine");
        }
        //extension = engine.getFactory().getExtensions().get(0);
        //prompt = extension + ">";
        //engine.setBindings(createBindings(), ScriptContext.ENGINE_SCOPE);
        

        ScriptContext context = new SimpleScriptContext();
        context.setBindings(createBindings(), ScriptContext.ENGINE_SCOPE);
        //context.setWriter(writer)
		engine.setContext(context );
    }

    // create Bindings that is backed by a synchronized HashMap
    private Bindings createBindings() {
        Map<String, Object> map =
                Collections.synchronizedMap(new HashMap<String, Object>());
        return new SimpleBindings(map);
    }

    // set pre-defined global variables for script
    protected void setGlobals() {
        engine.put("engine", engine);
        engine.put("inputs", inputs);
        engine.put("outputs", outputs);

        //engine.put("window", window);
        //engine.put("plugin", this);
    }

    
    // load initial script file (jconsole.<extension>)
    protected void loadInitFile() {
        String oldFilename = (String) engine.get(ScriptEngine.FILENAME);
        String extension = "js";
        
        engine.put(ScriptEngine.FILENAME, "<built-in jconsole." + extension + ">");
        try {            
            InputStream stream = this.getClass().getResourceAsStream("scriptDriverInit." + extension);
            if (stream != null) {
                engine.eval(new InputStreamReader(new BufferedInputStream(stream)));
            }
        } catch (Exception exp) {
            exp.printStackTrace();
        } finally {
            engine.put(ScriptEngine.FILENAME, oldFilename);
        }
    }
    
    protected String executeCommand(String cmd) {
        String res;
        try {
           engineReady.await();
           Object tmp = engine.eval(cmd);
           res = (tmp == null)? null : tmp.toString();
        } catch (InterruptedException ie) {
           res = ie.getMessage();
        } catch (ScriptException se) {
           res = se.getMessage();
        }
        return res;
    }


    
    // -----------------------------------------------------------
	// Getters/setters 
	// -----------------------------------------------------------

    
	public String getName() {		return name;	}
	/**
	 * Just user-visible name of script.
	 * @param name Script name.
	 */
	public void setName(String name) {		this.name = name;	}

	public String getScript() {		return script;	}
	/**
	 * @param script Text of script to be executed by driver.
	 */
	public void setScript(String script) {		this.script = script;	}
	

}
