package nadav.tasher.handasaim.plasma.views;

import nadav.tasher.handasaim.plasma.Utils;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class BarView extends JPanel {
    public static final Color barColor = new Color(154, 188, 222);
    private ArrayList<Message> pushMessages = new ArrayList<>(), scheduleMessages = new ArrayList<>();
    private JPanel statusPanel = new JPanel(), messagePanel = new JPanel();
    private TextView clock, date, message;
    private Timer timeTick = new Timer();
    private boolean isTimeTickScheduled = false;
    private Timer messageSwitch = new Timer();
    private boolean isMessageSwitchScheduled = false;

    public BarView() {
        setBackground(barColor);
        setLayout(new GridLayout(1, 2));
        statusPanel.setOpaque(false);
        messagePanel.setOpaque(false);
        add(statusPanel);
        add(messagePanel);
        setupStatusPanel();
        setupMessagePanel();
    }

    private void setupStatusPanel() {
        if (isTimeTickScheduled) {
            timeTick.cancel();
            isTimeTickScheduled = false;
        }
        statusPanel.removeAll();
        clock = new TextView();
        date = new TextView();
        clock.setTextSize(30);
        date.setTextSize(23);
        statusPanel.setLayout(new GridLayout(2, 1));
        statusPanel.add(clock);
        statusPanel.add(date);
        timeTick.schedule(new TimerTask() {
            private int lastHour = 0, lastMinute = 0;
            private int lastDay = 0;

            @Override
            public void run() {
                isTimeTickScheduled = true;
                Calendar currentTime = Calendar.getInstance();
                int currentHour = currentTime.get(Calendar.HOUR_OF_DAY), currentMinute = currentTime.get(Calendar.MINUTE), currentDay = currentTime.get(Calendar.DAY_OF_WEEK);
                if (currentHour != lastHour || currentMinute != lastMinute) {
                    lastHour = currentHour;
                    lastMinute = currentMinute;
                    StringBuilder timeBuilder = new StringBuilder();
                    timeBuilder.append(lastHour);
                    timeBuilder.append(':');
                    if (lastMinute < 10) timeBuilder.append(0);
                    timeBuilder.append(lastMinute);
                    clock.setText(timeBuilder.toString());
                    if (currentDay != lastDay) {
                        lastDay = currentDay;
                        String dateBuilder = Utils.dayConvert(currentDay) +
                                ',' + ' ' +
                                (currentTime.get(Calendar.DAY_OF_MONTH) + 1) +
                                '.' +
                                (currentTime.get(Calendar.MONTH) + 1);
                        date.setText(dateBuilder);
                    }
                }
            }
        }, 0, 1000);
    }

    public void setScheduleMessages(ArrayList<Message> messages) {
        scheduleMessages = messages;
    }

    public void setPushMessages(ArrayList<Message> messages) {
        pushMessages = messages;
    }

    private ArrayList<Message> getMessages() {
        ArrayList<Message> array = new ArrayList<>();
        array.addAll(scheduleMessages);
        array.addAll(pushMessages);
        return array;
    }

    private void setupMessagePanel() {
        if (isMessageSwitchScheduled) {
            messageSwitch.cancel();
            isMessageSwitchScheduled = false;
        }
        messagePanel.removeAll();
        message = new TextView();
        messageSwitch.schedule(new TimerTask() {
            private int index = getMessages().size();

            @Override
            public void run() {
                if (index >= getMessages().size()) {
                    index = 0;
                } else {
                    index++;
                }
                Message currentMessage = ((index >= getMessages().size()) ? null : getMessages().get(index));
                if (currentMessage != null) {
                    if (currentMessage.getType() == Message.TYPE_SCHEDULE) {
                        message.setTextColor(Color.BLACK);
                    } else {
                        message.setTextColor(Color.WHITE);
                    }
                    message.setText(currentMessage.getMessage());
                }
            }
        }, 0, 7000);
    }

    public class Message {
        public static final int TYPE_PUSH = 1;
        public static final int TYPE_SCHEDULE = 0;
        private String message;
        private int type = TYPE_SCHEDULE;

        public Message(String message, int type) {
            this.message = message;
            this.type = type;
        }

        public String getMessage() {
            return message;
        }

        public int getType() {
            return type;
        }
    }
}