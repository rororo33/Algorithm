package SequentialSearch;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class TestClient {
    public static void main(String[] args) {
        ST<String, Integer> st = new SequentialSearchST<>();  // ✅ 구현체 사용

        File file;
        final JFileChooser fc = new JFileChooser(); // 파일 선택기를 사용

        if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            file = fc.getSelectedFile();
        } else {
            JOptionPane.showMessageDialog(null, "파일을 선택하세요.", "오류", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Scanner sc = null;
        try {
            sc = new Scanner(file);
            for (int i = 0; sc.hasNext(); i++) {
                String key = sc.next();
                st.put(key, i);
            }
            for (String s : st.keys()) {
                System.out.println(s + " " + st.get(s));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (sc != null) sc.close();
        }
    }
}
