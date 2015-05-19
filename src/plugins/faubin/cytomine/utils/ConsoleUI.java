package plugins.faubin.cytomine.utils;

import java.awt.Color;

import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

import plugins.faubin.cytomine.Config;

@SuppressWarnings("serial")
public class ConsoleUI extends JTextPane {

	public JScrollPane scroll;
	public static ConsoleUI console;

	public ConsoleUI() {
		super();

		setEditable(false);
		append("Icytomine\n");
		append("version ");
		append(Config.version + "\n", Color.BLUE.darker());

	}

	public void append(String msg, Color c) {
		setEditable(true);

		StyleContext sc = StyleContext.getDefaultStyleContext();
		AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY,
				StyleConstants.Foreground, c);

		aset = sc.addAttribute(aset, StyleConstants.FontFamily,
				"Lucida Console");
		aset = sc.addAttribute(aset, StyleConstants.Alignment,
				StyleConstants.ALIGN_JUSTIFIED);

		int len = getDocument().getLength();
		setCaretPosition(len);
		setCharacterAttributes(aset, true);
		replaceSelection(msg);

		setEditable(false);

		if (scroll != null) {
			scroll.getVerticalScrollBar().setValue(
					scroll.getVerticalScrollBar().getMaximum());
		}

	}

	public void clear() {
		setText("");
	}

	public void append(String msg) {
		append(msg, Color.BLACK);
	}

}
