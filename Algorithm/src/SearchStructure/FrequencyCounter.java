package SearchStructure;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class FrequencyCounter {
    public static void main(String[] args) {

        if (args.length < 1) {
            System.out.println("사용법: java FrequencyCounter <minlen>");
            return;
        }

        int minlen = Integer.parseInt(args[0]);
        ST<String, Integer> st = new SequentialSearchST<>();  // ✅ 구현체 사용

        File file = new File("input.txt"); // 원하는 파일 이름으로 변경 가능

        Scanner sc = null;
        try {
            sc = new Scanner(file);
            long start = System.currentTimeMillis(); // 실행 시간 측정 시작

            while (sc.hasNext()) {
                String word = sc.next();
                if (word.length() < minlen) continue;

                if (!st.contains(word)) st.put(word, 1);
                else st.put(word, st.get(word) + 1);
            }

            String maxKey = "";
            int maxValue = 0;
            for (String word : st.keys()) {
                int val = st.get(word);
                if (val > maxValue) {
                    maxKey = word;
                    maxValue = val;
                }
            }

            long end = System.currentTimeMillis();
            System.out.println("가장 많이 나온 단어: " + maxKey + " (" + maxValue + "회)");
            System.out.println("소요 시간 = " + (end - start) + "ms");

        } catch (FileNotFoundException e) {
            System.out.println("파일을 찾을 수 없습니다.");
            e.printStackTrace();
        } finally {
            if (sc != null) sc.close();
        }
    }
}

