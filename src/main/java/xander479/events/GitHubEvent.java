package xander479.events;

import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class GitHubEvent implements ActionListener {
	private String repoName;
	
	public GitHubEvent(String repoName) {
		this.repoName = repoName;
	}
	
	// Opens the GitHub repo for this project if the action is supported.
	public void actionPerformed(ActionEvent e) {
		Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
		if(desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
			try {
				desktop.browse(new URI("https://github.com/Xander479/" + repoName));
			}
			catch(Exception ex) {
				ex.printStackTrace();
			}
		}
		else {
			JFrame frame = new JFrame("Error");
			frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			JLabel label = new JLabel("Your platform doesn't support this action.", JLabel.CENTER);
			label.setPreferredSize(new Dimension(350, 50));
			frame.getContentPane().add(label);
			frame.pack();
			frame.setLocationRelativeTo(null);
			frame.setVisible(true);
		}
	}
}
