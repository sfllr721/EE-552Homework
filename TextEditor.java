/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package texteditor;
import java.awt.*; 
import java.awt.event.*; 
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import static javax.swing.JOptionPane.*;
import java.util.regex.*;
import javax.swing.text.*;

/**
 *
 * @author Sam
 * Used references to online examples for Saving and Opening Files
 */
public class TextEditor extends JFrame {
    private Container c;
    private boolean save = false;
    private boolean error = false;
    private JTextArea ta;
    private String fileName = "";
    private int lineNumber = 0;
    public TextEditor(String title, Color bg, int w, int h) throws AWTException{
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        //text
        ta = new JTextArea();
        ta.addKeyListener(new KeyAdapter(){
            public void keyPressed (KeyEvent e){
                save = false;
            }
        });
        Font f = new Font("Helvetica", Font.PLAIN, 18);
        ta.setFont(f);
     
        //Menu
        JMenuBar mainMenu = new JMenuBar();
        
        //File Menu
        JMenu fileMenu = new JMenu("File");
        JMenuItem fileMenu1 = new JMenuItem("New"); 
        JMenuItem fileMenu2 = new JMenuItem("Save");
        JMenuItem fileMenu3 = new JMenuItem("Open"); 
        JMenuItem fileMenu4 = new JMenuItem("Quit");
        
        //File Menu Action Listners 
        fileMenu1.addActionListener(new MyListener());
        fileMenu2.addActionListener(new MyListener());
        fileMenu3.addActionListener(new MyListener());
        fileMenu4.addActionListener(new MyListener());
        
        //Add Menu Items to File Menu 
        fileMenu.add(fileMenu1);
        fileMenu.add(fileMenu2);
        fileMenu.add(fileMenu3);
        fileMenu.add(fileMenu4);
        
        //Build Menu 
        JMenu buildMenu = new JMenu("Build");
        JMenuItem buildMenu1 = new JMenuItem("Compile"); 
        JMenuItem buildMenu2 = new JMenuItem("Run");
        
        //Build Menu Action Listener 
        buildMenu1.addActionListener(new MyListener());
        buildMenu2.addActionListener(new MyListener());
        
        //Add Menu Items to Build Menu
        buildMenu.add(buildMenu1);
        buildMenu.add(buildMenu2);
        
        //Add File and Compile Menus to Menu Bar 
        mainMenu.add(fileMenu);
        mainMenu.add(buildMenu);
        
        //Set up the compile error button
        Robot r = new Robot();
        ta.addKeyListener(new KeyAdapter(){
            public void keyPressed(KeyEvent e){
                if(error && e.getKeyCode() == KeyEvent.VK_F4)
                    ta.setCaretPosition(ta.getDocument().getDefaultRootElement().getElement(lineNumber - 1).getStartOffset());
                    try
                    {
                        int offset = ta.getCaretPosition();
                        int line = ta.getLineOfOffset(offset);
                        System.out.printf("Offset %d on line %d%n", offset, line);
                    }
                    catch (BadLocationException ex)
                    {
                        ex.printStackTrace();
                    }
            }
        });
       
        
        //Set Up Container
        c = getContentPane();
        c.add(BorderLayout.NORTH, mainMenu);
        c.add(ta);
        c.setBackground(bg);
        setTitle(title);
        setSize(w,h); 
        setVisible(true);
    }
    
    class MyListener implements ActionListener {
        public void actionPerformed(ActionEvent e){
            String command = e.getActionCommand();
            
            //Check Save/New/Open etc. 
            if(command.equals("New")){
                try {
                    new TextEditor("New File", Color.WHITE, 800, 600);
                } catch (AWTException ex) {
                    Logger.getLogger(TextEditor.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else if(command.equals("Save")){
                fileName = save();
            }
            //Opens Files
            else if(command.equals("Open")){
                JFileChooser f = new JFileChooser("f: ");
                int a = f.showOpenDialog(null);
                if(a == JFileChooser.APPROVE_OPTION){ 
                     File fl = new File(f.getSelectedFile().getAbsolutePath());
                     try {
                        //Open File
                        String s1, s2 = " ";
                        FileReader fr = new FileReader(fl);
                        BufferedReader br = new BufferedReader(fr);
                        s2 = br.readLine();
                        while((s1 = br.readLine()) != null){
                            s2 = s2 + "\n" + s1;
                        }
                        ta.setText(s2);
                    } 
                    catch (IOException ex) {
                        Logger.getLogger(TextEditor.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            //Exits if Already Saved
            else if(command.equals("Quit") && save){
                System.exit(0);
            }
            //Dialog Box if quit is chosen without saving
            else if(command.equals("Quit") && !save){
                int returnValue = showConfirmDialog(null, "Do You Want to Save?", "Warning", JOptionPane.YES_NO_OPTION);
                if(returnValue == 1)
                    System.exit(0); 
                else {
                    fileName = save(); 
                    System.exit(0);
               }
            }
            else if(command.equals("Compile")){
                if(!save)
                    fileName = save();
                try {
                    compileCode(fileName);
                } catch (IOException ex) {
                    Logger.getLogger(TextEditor.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
            else if(command.equals("Run")){
                JFileChooser f = new JFileChooser("f: ");
                int a = f.showOpenDialog(null);
                if(a == JFileChooser.APPROVE_OPTION){ 
                     String chosenFile = f.getSelectedFile().getAbsolutePath();
                     try {
                         compileCode(chosenFile);
                    } 
                    catch (IOException ex) {
                        Logger.getLogger(TextEditor.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            } 
        } 
       //Saves Files
       public String save(){
           JFileChooser f = new JFileChooser("f: ");
            int a = f.showSaveDialog(null);
            if(a == JFileChooser.APPROVE_OPTION){
                File fl = new File(f.getSelectedFile().getAbsolutePath());
                try {
                        //Save File
                        FileWriter fw = new FileWriter(fl, false);
                        BufferedWriter bw = new BufferedWriter(fw);
                        bw.write(ta.getText());
                        bw.flush(); 
                        bw.close();
                        save = true;
                    } 
                    catch (IOException ex) {
                        Logger.getLogger(TextEditor.class.getName()).log(Level.SEVERE, null, ex);
                    }
            }
            return f.getSelectedFile().getAbsolutePath();
       }

        private void compileCode(String file) throws IOException {
           Process p = Runtime.getRuntime().exec("javac " + file);
           BufferedReader errorReader = new BufferedReader (new InputStreamReader(p.getErrorStream()));
           String line;
           Matcher matcher, matcher2= null;
           Pattern pattern = Pattern.compile(":[0-9]+");
           Pattern pattern2 = Pattern.compile("(error:)+");
           String regResult = null; 
           String another = null;
           while((line = errorReader.readLine()) != null){
               matcher2 = pattern2.matcher(line);
               while(matcher2.find())
                   another = matcher2.group();
               if(another != null){
                    error = true;
                    matcher = pattern.matcher(line);
                    while(matcher.find())
                        regResult = matcher.group().replaceAll("[:]", "");
                    lineNumber = Integer.parseInt(regResult);
                   }
           }
              
           System.out.println(lineNumber);
           
        }
        
    }
    
    
    
    public static void main(String[] args) throws AWTException{
        new TextEditor("Editor", Color.WHITE, 800, 600);
        
    }
}
