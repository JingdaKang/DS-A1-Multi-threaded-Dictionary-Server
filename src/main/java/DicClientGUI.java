/**
 *
 * @author jingda Kang
 * @id 1276802
 *
 **/

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ContainerAdapter;
import java.awt.event.ContainerEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DicClientGUI {
    private JFrame frame;
    private JButton queryButton;

    private JButton addButton;
    private JButton removeButton;
    private JButton updateButton;
    private JButton exitButton;



    private JPanel panel1;
    private JScrollPane result_area;
    private JTextArea textArea1;
    private JButton clearButton;
    private JPanel panel2;

    private DicClient dicClient;



    public DicClientGUI(DicClient client) {
        Locale.setDefault(new Locale("USA"));
        dicClient = client;
        initialize();

        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textArea1.setText(null);
            }
        });

    }



    private Boolean singleCheck(String word, String meaning, int command) {
        if (word.equals("") || word == null) {
            JOptionPane.showMessageDialog(frame, "Please Enter a word.", "Warning", JOptionPane.WARNING_MESSAGE);
            return false;
        } else if ((command == RequestCode.ADD && meaning.equals("")) ) {
            JOptionPane.showMessageDialog(frame, "Please Enter the word's meaning(s).", "Warning", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    private Boolean multiCheck(String word, ArrayList<String> meanings, int code) {
        if (word.equals("") || word == null) {
            JOptionPane.showMessageDialog(frame, "Please Enter a word.", "Warning", JOptionPane.WARNING_MESSAGE);
            return false;
        } else if (code == RequestCode.ADD && (meanings.contains("") || meanings.contains(null))) {
            JOptionPane.showMessageDialog(frame, "Please Enter the word's meaning(s).", "Warning", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }


    public void initialize() {

        frame = new JFrame("DicClientGUI");
        frame.setBounds(700, 100, 1000, 1300);
        frame.setContentPane(panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);


        MyDialog myDailog = new MyDialog();

        queryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {
                    String word = myDailog.queryWord();
                    if (singleCheck(word, "", RequestCode.QUERY)) {
                        ResultPackage result = dicClient.query(word);
                        int state = result.getRequestCode();
                        ArrayList meaning = result.getMeaning();
                        if (state == RequestCode.FAIL) {
                            JOptionPane.showMessageDialog(frame, "Query failed\nWord doesn't exists!", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                        else if (state == RequestCode.FAIL_CONNECTION){
                            JOptionPane.showMessageDialog(null, "Connection to server failed! Try again later.", "Error", JOptionPane.ERROR_MESSAGE);
                        }else {
                                textArea1.setText("Meaning: "+result.getMeaning().toString());

                            }
                        }

                }catch (NullPointerException E){
                    System.out.println("cancel query.");;
                }
            }
        });
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int num = myDailog.selectAddNum();
                    WordAndMeanings pair = myDailog.inputWordandMeaning(num);
                    if (num ==1){
                        if (singleCheck(pair.getWord(), pair.getMeanings().get(0), RequestCode.ADD)) {
                            ResultPackage result = dicClient.add(pair);
                            int state = result.getRequestCode();

                            if (state == RequestCode.FAIL) {
                                JOptionPane.showMessageDialog(frame, "Add failed!\nWord and meaning(s) already exist!", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                            else if (state == RequestCode.SUCCESS_WORD_EXIST){
                                textArea1.setText("Add another meaning(s) successfully since word has already existed! ");
                            }else if (state == RequestCode.FAIL_CONNECTION){
                                JOptionPane.showMessageDialog(null, "Connection to server failed! Try again later.", "Error", JOptionPane.ERROR_MESSAGE);
                            }else {
                                textArea1.setText("Add word and meaning successfully! ");
                            }
                        }
                    }else {
                        if (multiCheck(pair.getWord(), pair.getMeanings(), RequestCode.ADD)) {
                            ResultPackage result = dicClient.add(pair);
                            int state = result.getRequestCode();

                            if (state == RequestCode.FAIL) {
                                JOptionPane.showMessageDialog(frame, "Add failed!\nWord and meaning(s) have already existed!", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                            else if (state == RequestCode.SUCCESS_WORD_EXIST){
                                textArea1.setText("Add another meaning successfully since word has already existed! ");
                            }else if (state == RequestCode.FAIL_CONNECTION){
                                JOptionPane.showMessageDialog(null, "Connection to server failed! Try again later.", "Error", JOptionPane.ERROR_MESSAGE);
                            }else {
                                textArea1.setText("Add word and meaning successfully! ");
                            }
                        }
                    }


                }catch (NullPointerException E){
                    System.out.println("Cancel add.");;
                }
            }
        });
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String word = myDailog.removeWord();
                    if (singleCheck(word, "", RequestCode.REMOVE)) {
                        ResultPackage result = dicClient.remove(word);
                        int state = result.getRequestCode();
                        if (state == RequestCode.FAIL) {
                            JOptionPane.showMessageDialog(frame, "Remove failed\nWord doesn't exist!", "Error", JOptionPane.ERROR_MESSAGE);
                        }else if (state == RequestCode.FAIL_CONNECTION){
                            JOptionPane.showMessageDialog(null, "Connection to server failed! Try again later.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                        else {
                            textArea1.setText("Remove word successfully!");
                        }
                    }
                }catch (NullPointerException E){
                    System.out.println("cancel remove.");;
                }

            }
        });
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String word = myDailog.updateWord();
                    //check whether word exists
                    if (singleCheck(word, "", RequestCode.UPDATE)) {
                        ResultPackage result1 = dicClient.query(word);
                        int state = result1.getRequestCode();
                        if (state == RequestCode.FAIL) {
                            JOptionPane.showMessageDialog(frame, "Update failed\nWord doesn't exist!", "Error", JOptionPane.ERROR_MESSAGE);
                        }else if (state == RequestCode.FAIL_CONNECTION){
                            JOptionPane.showMessageDialog(null, "Connection to server failed! Try again later.", "Error", JOptionPane.ERROR_MESSAGE);
                        } else {
                            int num = myDailog.selectUpdateNum();
                            WordAndMeanings pair = myDailog.inputMeaning(num);
                            pair.setWord(word);
                            ResultPackage result2 = dicClient.update(pair);

                            textArea1.setText("Update word successfully!");
                        }
                    }
                }catch (NullPointerException E){
                    System.out.println("cancel update.");;
                }

            }
        });
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }



}


class MyDialog extends JDialog{

    public static String queryWord() {

        String inputWord = JOptionPane.showInputDialog("Please input a word to query:").trim().toLowerCase(Locale.ROOT);
        System.out.println("input value: " + inputWord);
        return inputWord;
    }
    public static WordAndMeanings inputWordandMeaning(int num) {
        WordAndMeanings WordandMeaning = new WordAndMeanings(null, null);
        List<JTextField> inputs = new ArrayList<>();
        ArrayList<String> meanings = new ArrayList();

        JPanel myPanel = new JPanel();
        //myPanel.setBounds(30,30,500,500);
        JTextField word = new JTextField(8);


        myPanel.add(new Label("Input a word"));
        myPanel.add(word);
        myPanel.add(Box.createVerticalStrut(20));

        for(int i =0; i<num; i++){
            JTextField meaning = new JTextField(8);
            //myPanel.add(Box.createVerticalStrut(10));
            myPanel.add(new Label("Input meaning "+(i+1)));
            myPanel.add(meaning);
            inputs.add(meaning);
        }
        myPanel.setLayout(new BoxLayout(myPanel, BoxLayout.Y_AXIS));


        int res = JOptionPane.showConfirmDialog(null, myPanel,
                "Please Enter Word and Meaning(s)", JOptionPane.OK_CANCEL_OPTION);
        if (res == 0){
            System.out.println("input word: " + word.getText().trim().toLowerCase(Locale.ROOT));
            for (JTextField jtextfield: inputs){
                System.out.println("input meaning: " + jtextfield.getText().trim().toLowerCase(Locale.ROOT));
                meanings.add(jtextfield.getText().trim().toLowerCase(Locale.ROOT));
            }

            WordandMeaning.setWord(word.getText().trim());
            WordandMeaning.setMeanings(meanings);
            System.out.println(WordandMeaning.getMeanings());
        }
        return WordandMeaning;
    }

    public static WordAndMeanings inputMeaning(int num) {
        WordAndMeanings WordandMeaning = new WordAndMeanings(null, null);
        List<JTextField> inputs = new ArrayList<>();
        ArrayList<String> meanings = new ArrayList();

        JPanel myPanel = new JPanel();
        //myPanel.setBounds(30,30,500,500);

        myPanel.add(Box.createVerticalStrut(5));

        for(int i =0; i<num; i++){
            JTextField meaning = new JTextField(8);
            //myPanel.add(Box.createVerticalStrut(10));
            myPanel.add(new Label("Input meaning "+(i+1)));
            myPanel.add(meaning);
            inputs.add(meaning);
        }
        myPanel.setLayout(new BoxLayout(myPanel, BoxLayout.Y_AXIS));


        int res = JOptionPane.showConfirmDialog(null, myPanel,
                "Please Enter Word and Meaning(s)", JOptionPane.OK_CANCEL_OPTION);
        if (res == 0){

            for (JTextField jtextfield: inputs){
                System.out.println("input meaning: " + jtextfield.getText().trim().toLowerCase(Locale.ROOT));
                meanings.add(jtextfield.getText().trim().toLowerCase(Locale.ROOT));
            }

            WordandMeaning.setWord(null);
            WordandMeaning.setMeanings(meanings);
            System.out.println(WordandMeaning.getMeanings());
        }
        return WordandMeaning;
    }

    public static String removeWord() {

        String inputWord = JOptionPane.showInputDialog("Please input a word to remove:").trim().toLowerCase(Locale.ROOT);
        System.out.println("input value: " + inputWord);
        return inputWord;
    }

    public static String updateWord() {

        String inputWord = JOptionPane.showInputDialog("Please input a word to update:").trim().toLowerCase(Locale.ROOT);
        System.out.println("input value: " + inputWord);

        return inputWord;
    }

    public static int selectAddNum() {

        Object[] possibleValues = { "1", "2", "3", "4", "5" }; // 用户的选择项目
        Integer selectedValue = Integer.parseInt(JOptionPane.showInputDialog(null, "Choose number of meanings to be added","Select",JOptionPane.INFORMATION_MESSAGE, null, possibleValues,possibleValues[0]).toString());
        System.out.println("select num: "+selectedValue);
        return selectedValue;
    }

    public static int selectUpdateNum() {

        Object[] possibleValues = { "1", "2", "3", "4", "5" }; // 用户的选择项目
        Integer selectedValue = Integer.parseInt(JOptionPane.showInputDialog(null, "Choose number of meanings to be updated","Select",JOptionPane.INFORMATION_MESSAGE, null, possibleValues,possibleValues[0]).toString());
        System.out.println("select num: "+selectedValue);
        return selectedValue;
    }

}

class WordAndMeanings{
    private String word;
    private ArrayList<String> meanings;

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public ArrayList<String> getMeanings() {
        return meanings;
    }

    public void setMeanings(ArrayList<String> meanings) {
        this.meanings = meanings;
    }

    public WordAndMeanings(String word, ArrayList<String> meanings) {
        this.word = word;
        this.meanings = meanings;
    }
}

