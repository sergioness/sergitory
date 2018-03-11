import javax.sound.midi.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.*;

public class BeatBox {
    JFrame frame;
    JPanel panel;
    ArrayList<JCheckBox> checkBoxes;
    Sequencer sequencer;
    Sequence sequence;
    Track track;
    String[] instrumentNames = {
            "Bass Drum",
            "Closed Hi-Hat",
            "Open Hi-Hat",
            "Acoustic Snare",
            "Crash Cymbal",
            "Hand Clap",
            "High Tom",
            "Hi Bongo",
            "Maracas",
            "Whistle",
            "Low Conga",
            "Cowbell",
            "Vibraslap",
            "Low-mid Tom",
            "High Agogo",
            "Open Hi Conga"
    };
    int[] instruments = {
            35,
            42,
            46,
            38,
            49,
            39,
            50,
            60,
            70,
            72,
            64,
            56,
            58,
            47,
            67,
            63
    };
    final static int ticks = 16;
    final static int BMP = 120;
    final static String CHECKBOXES_FILENAME = "checkboxes.ser";

    public static void main (String args[]){
        new BeatBox().build();
    }

    public void build(){
        //setting up main frame
        frame = new JFrame("Cyber Beatbox");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel background = new JPanel(new BorderLayout());
        background.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        //checkboxes array
        checkBoxes = new ArrayList<>();
        //buttons box
        Box buttons = new Box(BoxLayout.Y_AXIS);

        //creating and adding buttons into buttons box
        JButton start = new JButton("Start");
        start.addActionListener(new StartListener());
        buttons.add(start);

        JButton stop = new JButton("Stop");
        stop.addActionListener(new StopListener());
        buttons.add(stop);

        JButton tempoUP = new JButton("Tempo Up");
        tempoUP.addActionListener(new TempoUPListener());
        buttons.add(tempoUP);

        JButton tempoDOWN = new JButton("Tempo Down");
        tempoDOWN.addActionListener(new TempoDOWNListener());
        buttons.add(tempoDOWN);

        JButton reset = new JButton("Reset");
        reset.addActionListener(new ResetListener());
        buttons.add(reset);

        JButton save = new JButton("Save this schema");
        save.addActionListener(new SaveListener());
        buttons.add(save);

        JButton restore = new JButton("Restore last saved schema");
        restore.addActionListener(new RestoreListener());
        buttons.add(restore);

        //instrument names box
        Box names = new Box(BoxLayout.Y_AXIS);
        for (int i = 0; i < instrumentNames.length; i++)
            names.add(new Label(instrumentNames[i]));

        //adding buttons and instument names boxes onto background panel
        background.add(BorderLayout.EAST, buttons);
        background.add(BorderLayout.WEST, names);

        //adding background panel to main frame
        frame.getContentPane().add(background);

        //creating and adding grid 16x16 onto background panel
        GridLayout grid = new GridLayout(instrumentNames.length, instruments.length);
        grid.setVgap(1);
        grid.setVgap(2);
        panel = new JPanel(grid);
        background.add(BorderLayout.CENTER, panel);

        for (int i = 0; i < instrumentNames.length*instruments.length; i++) {
            JCheckBox item = new JCheckBox();
            item.setSelected(false);
            checkBoxes.add(item);
            panel.add(item);
        }

        setupMIDI();

        frame.setBounds(50,50,300,300);
        frame.pack();
        frame.setVisible(true);
    }

    public void setupMIDI(){
        try{
            sequencer = MidiSystem.getSequencer();
            sequencer.open();
            sequence = new Sequence(Sequence.PPQ, 4);
            track = sequence.createTrack();
            sequencer.setTempoInBPM(BMP);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void buildTrackAndStrat(){
        //to store value for each instrument for 16 ticks
        int[] tracklist = null;

        sequence.deleteTrack(track);
        track = sequence.createTrack();

        for (int i = 0; i < instruments.length; i++) {
            tracklist = new int[ticks];
            for (int j = 0; j < ticks; j++) {
                if(checkBoxes.get(j+(16*i)).isSelected()){
                    tracklist[j] = instruments[i];
                } else{
                    tracklist[j] = 0;
                }
            }
            //creating events for this instrument for all 16 ticks and adding these into the track
            makeTracks(tracklist);
            track.add(makeEvent(176,1,127,0,16));
        }

        track.add(makeEvent(192,9,1,0,15));

        try{
            sequencer.setSequence(sequence);
            sequencer.setLoopCount(sequencer.LOOP_CONTINUOUSLY);
            sequencer.start();
            sequencer.setTempoInBPM(BMP);
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public void makeTracks(int[] list){
        for (int i = 0; i < ticks; i++) {
            if(list[i] != 0){
                track.add(makeEvent(144,9,list[i],100,i));
                track.add(makeEvent(144,9,list[i],100,i+1));
            }
        }
    }

    public static MidiEvent makeEvent(int command, int channel, int data1, int data2, int tick){
        MidiEvent event = null;
        try {
            ShortMessage a = new ShortMessage();
            a.setMessage(command, channel, data1, data2);
            event = new MidiEvent(a, tick);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return event;
    }

    private class StartListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            buildTrackAndStrat();
        }
    }

    private class StopListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            sequencer.stop();
        }
    }

    private class TempoUPListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            sequencer.setTempoFactor(sequencer.getTempoFactor()*1.03f);
        }
    }

    private class TempoDOWNListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            sequencer.setTempoFactor(sequencer.getTempoFactor()*0.97f);
        }
    }

    private class ResetListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            for (JCheckBox cb:checkBoxes) {
                cb.setSelected(false);
            }
        }
    }
    //turn to 494 page and do the task at the bottom of the page!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    private class SaveListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            boolean[] checkboxes = new boolean[256];
            for (int i = 0; i < 256; i++)
                checkboxes[i] = checkBoxes.get(i).isSelected();

            try(FileOutputStream fs = new FileOutputStream(new File(CHECKBOXES_FILENAME))) {
                ObjectOutputStream os = new ObjectOutputStream(fs);
                os.writeObject(checkboxes);
            } catch (IOException ex){
                ex.printStackTrace();
            }
        }
    }

    private class RestoreListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            boolean[] checkboxes = null;
            try(FileInputStream fi = new FileInputStream(new File(CHECKBOXES_FILENAME))){
                ObjectInputStream os = new ObjectInputStream(fi);
                checkboxes = (boolean[]) os.readObject();
            } catch(Exception ex) {
                ex.printStackTrace();
            }
            for (int i = 0; i < checkboxes.length; i++)
                checkBoxes.get(i).setSelected(checkboxes[i]);

            sequencer.stop();
            buildTrackAndStrat();
        }
    }
}