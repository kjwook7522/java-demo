import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StudentForm extends JFrame {
    private JTextField nameField;
    private JTextField idField;
    private JTextField scoreField;
    private JButton submitButton;
    private JTable table;
    private DefaultTableModel tableModel;

    public StudentForm() {
        // 기본값 설정
        setTitle("Student Manage System");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        // 창 설정
        getContentPane().setBackground(Color.WHITE);

        // 레이아웃 설정
        setLayout(new GridLayout(4, 2, 10, 10));

        // 라벨과 입력 필드 생성
        add(new JLabel("Name:"));
        nameField = new JTextField();
        add(nameField);

        add(new JLabel("ID:"));
        idField = new JTextField();
        add(idField);

        add(new JLabel("Score:"));
        scoreField = new JTextField();
        add(scoreField);

        // 중앙 테이블 패널
        String[] columns = { "Name", "Student ID", "Score" };
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // 버튼 인벤트 생성
        submitButton = new JButton("submit");
        add(submitButton);
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                String id = idField.getText();
                String score = scoreField.getText();

                // 필드가 비어있을 경우 경고
                if (name.isEmpty() || id.isEmpty() || score.isEmpty()) {
                    JOptionPane.showMessageDialog(
                            StudentForm.this,
                            "All fields are required.",
                            "Warning",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // 테이블에 데이터 추가
                tableModel.addRow(new Object[] { name, id, score });

                // 입력필드 초기화
                nameField.setText("");
                idField.setText("");
                scoreField.setText("");
            }
        });
    }
}
