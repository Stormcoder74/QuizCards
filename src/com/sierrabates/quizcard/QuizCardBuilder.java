package com.sierrabates.quizcard;

import java.util.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.*;
import java.io.*;

public class QuizCardBuilder {

    private JTextArea questionArea;
    private JTextArea answerArea;
    private ArrayList<QuizCard> cardList;
    private JFrame frame;
    private int currentIndex;
    private JLabel qLabel;
    private JLabel aLabel;

    // additional, bonus method not found in any book!

    public static void main(String[] args) {
        QuizCardBuilder builder = new QuizCardBuilder();
        builder.go();
    }

    private void go() {
        // build gui
        frame = new JFrame("Quiz Card Builder");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);  // title bar
        currentIndex = 0;

        JPanel mainPanel = new JPanel();
        Font bigFont = new Font("sanserif", Font.BOLD, 24);
        questionArea = new JTextArea(6, 20);
        questionArea.setLineWrap(true);
        questionArea.setWrapStyleWord(true);
        questionArea.setFont(bigFont);

        JScrollPane qScroller = new JScrollPane(questionArea);
        qScroller.setVerticalScrollBarPolicy(
                ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        qScroller.setHorizontalScrollBarPolicy(
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        answerArea = new JTextArea(6, 20);
        answerArea.setLineWrap(true);
        answerArea.setWrapStyleWord(true);
        answerArea.setFont(bigFont);

        JScrollPane aScroller = new JScrollPane(answerArea);
        aScroller.setVerticalScrollBarPolicy(
                ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        aScroller.setHorizontalScrollBarPolicy(
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        JButton previousButton = new JButton("Previous card");
        JButton nextButton = new JButton("Next card");
        cardList = new ArrayList<QuizCard>();
        qLabel = new JLabel("Question: 1");
        aLabel = new JLabel("Answer: 1");

        mainPanel.add(qLabel);
        mainPanel.add(qScroller);
        mainPanel.add(aLabel);
        mainPanel.add(aScroller);
        mainPanel.add(previousButton);
        mainPanel.add(nextButton);
        nextButton.addActionListener(new NextCardListener());
        previousButton.addActionListener(new PreviuosCardListener());

        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");

        JMenuItem newMenuItem = new JMenuItem("New");
        JMenuItem saveMenuItem = new JMenuItem("Save");

        newMenuItem.addActionListener(new NewMenuListener());
        saveMenuItem.addActionListener(new SaveMenuListener());

        fileMenu.add(newMenuItem);
        fileMenu.add(saveMenuItem);
        menuBar.add(fileMenu);
        frame.setJMenuBar(menuBar);

        frame.getContentPane().add(BorderLayout.CENTER, mainPanel);
        frame.setSize(500, 600);
        frame.setVisible(true);
    }

    private void loadNext(int index){
        questionArea.setText(cardList.get(index).getQuestion());
        answerArea.setText(cardList.get(index).getAnswer());
    }

    public class PreviuosCardListener implements ActionListener {
        public void actionPerformed(ActionEvent ev) {
            if (!"".equals(questionArea.getText()) && !"".equals(answerArea.getText())) {
                QuizCard card = new QuizCard(questionArea.getText(), answerArea.getText());
                if (currentIndex == cardList.size()) {
                    cardList.add(card);
                    loadNext(--currentIndex);
                } else if (currentIndex > 0) {
                    cardList.set(currentIndex, card);
                    loadNext(--currentIndex);
                }
            } else {
                if (currentIndex > 0) {
                    loadNext(--currentIndex);
                }
            }
            qLabel.setText("Question: " + (currentIndex+1));
            aLabel.setText("Answer: " + (currentIndex+1));
        }
    }

    public class NextCardListener implements ActionListener {
        public void actionPerformed(ActionEvent ev) {
            if (!"".equals(questionArea.getText()) && !"".equals(answerArea.getText())) {
                QuizCard card = new QuizCard(questionArea.getText(), answerArea.getText());
                if (currentIndex < cardList.size()) {
                    cardList.set(currentIndex, card);
                    currentIndex++;
                    if (currentIndex == cardList.size()) {
                        clearCard();
                    } else {
                        loadNext(currentIndex);
                    }
                } else {
                    cardList.add(card);
                    clearCard();
                    currentIndex++;
                }
            } else {
                if (currentIndex < cardList.size()) {
                    loadNext(++currentIndex);
                }
            }
            qLabel.setText("Question: " + (currentIndex+1));
            aLabel.setText("Answer: " + (currentIndex+1));
        }
    }

    public class SaveMenuListener implements ActionListener {
        public void actionPerformed(ActionEvent ev) {
            if (!"".equals(questionArea.getText()) &&
                    !"".equals(answerArea.getText()) &&
                    currentIndex == cardList.size()) {
                QuizCard card = new QuizCard(questionArea.getText(), answerArea.getText());
                cardList.add(card);
            }

            JFileChooser fileSave = new JFileChooser("C:\\Users\\Stormcoder\\Documents\\Java\\SierraBates\\QuizCard");
            fileSave.showSaveDialog(frame);
            saveFile(fileSave.getSelectedFile());
        }
    }

    public class NewMenuListener implements ActionListener {
        public void actionPerformed(ActionEvent ev) {
            cardList.clear();
            clearCard();
        }
    }


    private void clearCard() {
        questionArea.setText("");
        answerArea.setText("");
        questionArea.requestFocus();
    }

    private void saveFile(File file) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            Iterator cardIterator = cardList.iterator();
            while (cardIterator.hasNext()) {
                QuizCard card = (QuizCard) cardIterator.next();
                writer.write(card.getQuestion() + "/");
                writer.write(card.getAnswer() + "\n");
            }
            writer.close();

        } catch (IOException ex) {
            System.out.println("couldn't write the cardList out");
            ex.printStackTrace();
        }
    } // close method
}