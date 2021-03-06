package nadav.tasher.handasaim.plasma.views;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class SwitcherView extends JPanel {
	private static final long serialVersionUID = 1L;
	public static final int NONE = 0;
	public static final int INFINITE = 1;

	ArrayList<View> views = new ArrayList<>();
	private int repeat = NONE;
	private Thread switcher;

	public SwitcherView() {
		setLayout(new GridLayout(1, 1));
	}
	
	public void stop() {
		if(switcher!=null) {
		switcher.interrupt();
		}
		switcher=null;
	}
	
	public void clearViews() {
		views.clear();
		removeAll();
	}

	public void setRepeatType(int repeat) {
		this.repeat = repeat;
	}

	public void addView(Container p, int time) {
		views.add(new View(p, time));
	}

	public void start() {
		// for (int c=0;c<views.size();c++) {
		// views.get(c).panel.setVisible(false);
		// add(views.get(c).panel);
		// }
		switcher = new Thread(new Runnable() {

			@Override
			public void run() {
				while (true) {
					for (int c = 0; c < views.size(); c++) {
						add(views.get(c).panel);
						revalidate();
						repaint();
						try {
							Thread.sleep(1000 * views.get(c).time);
						} catch (InterruptedException e) {
						}
//						remove(views.get(c).panel);
						removeAll();
						revalidate();
						repaint();
					}
					if (repeat == NONE)
						break;
				}
			}
		});
		switcher.start();
	}

	public class View {
		private Container panel;
		private int time;

		public View(Container panel, int time) {
			this.panel = panel;
			this.time = time;
		}
	}
}
