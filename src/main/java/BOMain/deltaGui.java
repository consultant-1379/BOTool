package BOMain;
//package BOMain;

import java.awt.Button;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.LinkedHashMap;

import javax.swing.ButtonGroup;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;

public class deltaGui extends Frame implements ActionListener {

	public static JProgressBar jprogressbar;
	static String endMessage = "Not done";
    String oldFile, newFile, outputFile;
	
	Label oldFileLabel;
	Label newFileLabel;
	Label saveFileLabel;
	
	TextField oldFiletxt;
	TextField newFiletxt;
	TextField saveFiletxt;

	Button oldFileSave;
	Button newFileSave;
	Button outputFileSave;
	Button create;
	Button cancel;
	
	public deltaGui() {

		addWindowListener(new fdmyWindowAdapter());
		setBackground(Color.lightGray);
		setForeground(Color.black);
		setLayout(null);
		jprogressbar = new JProgressBar();

		oldFileLabel = new Label("Enter old File Path", Label.LEFT);
		newFileLabel = new Label("Enter new File Path", Label.LEFT);
		saveFileLabel = new Label("Save output file Path", Label.LEFT);
		
		oldFiletxt = new TextField();
		newFiletxt=new TextField();
		saveFiletxt=new TextField();

		oldFileSave = new Button("..");
		newFileSave=new Button("...");
		outputFileSave=new Button("....");
		create = new Button("Create");
		cancel = new Button("Cancel");
		
		add(oldFileLabel);
		add(newFileLabel);
		add(saveFileLabel);

		add(oldFiletxt);
		add(newFiletxt);
		add(saveFiletxt);


		add(oldFileSave);
		add(newFileSave);
		add(outputFileSave);
		add(create);
		add(cancel);
		
		oldFileLabel.setBounds(25, 85, 130, 30);
		newFileLabel.setBounds(25, 145, 130, 30);
		saveFileLabel.setBounds(25, 205, 130, 30);
		
		oldFiletxt.setBounds(160, 85, 450, 30);
		newFiletxt.setBounds(160, 145, 450, 30);
		saveFiletxt.setBounds(160, 205, 450, 30);
		
		oldFileSave.setBounds(620, 85, 30, 30);
		newFileSave.setBounds(620, 145, 30, 30);
		outputFileSave.setBounds(620, 205, 30, 30);
		create.setBounds(215, 300, 65, 30);
		cancel.setBounds(435, 300, 65, 30);
		

		oldFiletxt.setText("C:\\Users\\user\\Documents");
		newFiletxt.setText("C:\\Users\\user\\Documents");
		saveFiletxt.setText("C:\\Users\\user\\Documents");


		oldFileSave.addActionListener(this);
		newFileSave.addActionListener(this);
		outputFileSave.addActionListener(this);
		create.addActionListener(this);
		cancel.addActionListener(this);
		
	}

	public void actionPerformed(ActionEvent ae) {

		if (ae.getActionCommand().equals("..")) {
			System.out.println("Enter the old input directory");
			saveOldFile();
		}
		if (ae.getActionCommand().equals("...")) {
			System.out.println("Enter the new input directory");
			saveNewFile();
		}
		if (ae.getActionCommand().equals("....")) {
			System.out.println("Enter the csv output directory");
			saveOutFile();
		}
		
		if (ae.getActionCommand().equals("Create")) {
			disableFields();
			validationReqInputs();
			try {
				initiateProcess1();
			} catch (Exception e) {
				e.printStackTrace();
			}
			enableFields();
		}
		
		if (ae.getActionCommand().equals("Cancel")) {
			System.exit(0);
		}
		
	}
		
		
	public void saveOldFile() {
		final JFileChooser jfc = new JFileChooser();
		 jfc.getSelectedFile();
	 jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
	 jfc.setDialogTitle("Save As");
		jfc.showSaveDialog(null);
		oldFiletxt.setText(jfc.getSelectedFile().toString());
		oldFile=oldFiletxt.getText();
		System.out.println("old:" + oldFile);
	}
	
	public void saveNewFile() {
		final JFileChooser jfc = new JFileChooser();
		 jfc.getSelectedFile();
		jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		jfc.setDialogTitle("Save As");
		jfc.showSaveDialog(null);
		newFiletxt.setText(jfc.getSelectedFile().toString());
		newFile=newFiletxt.getText();
		System.out.println("new:" + newFile);
	}
	
	public void saveOutFile() {
		final JFileChooser jfc = new JFileChooser();
		jfc.getSelectedFile();
		jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		jfc.setDialogTitle("Save As");
		jfc.showSaveDialog(null);
		saveFiletxt.setText(jfc.getSelectedFile().toString());
		outputFile=saveFiletxt.getText();
		System.out.println("out:" + outputFile);
	}
	
	public void disableFields() {

		
		oldFileLabel.setEnabled(false);
		newFileLabel.setEnabled(false);
		saveFileLabel.setEnabled(false);
		
		oldFiletxt.setEnabled(false);
		newFiletxt.setEnabled(false);
		saveFiletxt.setEnabled(false);
		
		oldFileSave.setEnabled(false);
		newFileSave.setEnabled(false);
		outputFileSave.setEnabled(false);
		create.setEnabled(false);

	}

	public void enableFields() {

		oldFileLabel.setEnabled(true);
		newFileLabel.setEnabled(true);
		saveFileLabel.setEnabled(true);
		
		oldFiletxt.setEnabled(true);
		newFiletxt.setEnabled(true);
		saveFiletxt.setEnabled(true);
	
		oldFileSave.setEnabled(true);
		newFileSave.setEnabled(true);
		outputFileSave.setEnabled(true);
	
	}

	public void validationReqInputs() {

		if (oldFiletxt.getText().equals("")) {
			JOptionPane.showMessageDialog(null, "Select path to save CSV file.");
			System.out.println("Select path to save OLD file.");
		}
		
		if (newFiletxt.getText().equals("")) {
			JOptionPane.showMessageDialog(null, "Select path to save CSV file.");
			System.out.println("Select path to save NEW file.");
		}
		
		if (saveFiletxt.getText().equals("")) {
			JOptionPane.showMessageDialog(null, "Select path to save CSV file.");
			System.out.println("Select path to save Output file.");
		}

	}
	
	public void initiateProcess1() throws Exception {

		String oldInputPath = oldFiletxt.getText();
		String newInputPath = newFiletxt.getText();
		String outputPath = saveFiletxt.getText();

		findDelta fd = new findDelta();
		fd.fileInput(oldInputPath,newInputPath,outputPath);
		showEndMsg1(outputPath);
		endMessage = "Done";

	}
	
	public void showEndMsg1(String opath) {
		JOptionPane.showMessageDialog(null, "The output files available at : " + opath, "BO Reports Tool",
				JOptionPane.PLAIN_MESSAGE);
	}

}

class fdmyWindowAdapter extends WindowAdapter {
	@Override
	public void windowClosing(final WindowEvent we) {
		System.exit(0);
	}
}
