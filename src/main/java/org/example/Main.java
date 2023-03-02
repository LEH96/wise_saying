package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        int no = 1;
        String input;
        List<SayingDict> datum = new ArrayList<>();

        System.out.println("== 명언 앱 ==");

        while(true){
            System.out.print("명령) ");
            input = sc.nextLine().trim();

            if(input.equals("등록")) {

                System.out.print("명언: ");
                String title = sc.nextLine();

                System.out.print("작가: ");
                String author = sc.nextLine();

                datum.add(new SayingDict(no, title, author));
                System.out.printf("%d번 명언이 등록되었습니다.\n", no++);

            } else if(input.equals("목록")) {

                System.out.println("번호 / 작가 / 명언");
                System.out.println("----------------------");

                List<SayingDict> sorted_datum = datum.stream()
                        .sorted((e2, e1) -> e1.getNo() - e2.getNo())
                        .collect(Collectors.toList());

                for(SayingDict say : sorted_datum)
                    System.out.println(say.getNo() + " / " + say.getAuthor() + " / " + say.getTitle());

            } else if(input.substring(0,2).equals("삭제")) {

                int id = Integer.parseInt(input.split("=")[1]);
                int idIdx = -1;

                for(int i=0;i<datum.size();i++){
                    if(datum.get(i).getNo() == id) {
                        idIdx = i;
                        break;
                    }
                }

                if(idIdx != -1){
                    datum.remove(idIdx);
                    System.out.printf("%d번 명언이 삭제되었습니다.\n", id);
                } else {
                    System.out.printf("%d번 명언은 존재하지 않습니다.\n", id);
                }

            } else if(input.substring(0,2).equals("수정")) {

                int id = Integer.parseInt(input.split("=")[1]);
                int idIdx = -1;

                for(int i=0;i<datum.size();i++){
                    if(datum.get(i).getNo() == id) {
                        idIdx = i;
                        break;
                    }
                }

                if(idIdx != -1){
                    System.out.printf("명언(기존) : %s\n", datum.get(idIdx).getTitle());
                    System.out.print("명언 : ");
                    String new_title = sc.nextLine();
                    datum.get(idIdx).setTitle(new_title);

                    System.out.printf("작가(기존) : %s\n", datum.get(idIdx).getAuthor());
                    System.out.print("작가 : ");
                    String new_author = sc.nextLine();
                    datum.get(idIdx).setAuthor(new_author);
                } else {
                    System.out.printf("%d번 명언은 존재하지 않습니다.\n", id);
                }

            } else if(input.equals("종료")) {
                if(datum.size() > 0){

                }
                return;
            }
        }
    }
}

class SayingDict {
    private int no;
    private String title;
    private String author;

    public SayingDict(int no, String title, String author) {
        this.no = no;
        this.title = title;
        this.author = author;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}