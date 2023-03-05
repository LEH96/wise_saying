package org.example;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.example.SayingService.getTableDataFilePath;

public class Main {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        int no = (int)(SayingService.getLastId() + 1);
        String command;

        System.out.println("== 명언 앱 ==");

        while(true){
            System.out.print("명령) ");
            command = sc.nextLine().trim();

            if(command.equals("등록")) {

                System.out.print("명언: ");
                String content = sc.nextLine().trim();

                System.out.print("작가: ");
                String author = sc.nextLine().trim();

                SayingDict saying = new SayingDict(no, content, author);
                System.out.printf("%d번 명언이 등록되었습니다.\n", no);

                // 폴더 생성
                Util.file.mkdir(SayingService.getTableDirPath());
                // json 폴더에 넣을 내용 만들기
                String body = saying.toJson();
                // 파일 경로 생성
                String filePath = getTableDataFilePath(no);
                // 파일 저장
                Util.file.saveToFile(filePath, body);
                SayingService.saveLastNo(no++);

            } else if(command.equals("목록")) {

                System.out.println("번호 / 작가 / 명언");
                System.out.println("----------------------");

                List<SayingDict> sayings = SayingService.findAll();
                for(int i = sayings.size() - 1 ; i >= 0 ; i--){
                    SayingDict saying = sayings.get(i);
                    System.out.println(saying.getNo() + " / " + saying.getAuthor() + " / " + saying.getContent());
                }

            } else if(command.startsWith("삭제")) {

                int id = Integer.parseInt(command.split("=")[1]);
                SayingDict saying = SayingService.findById(id);

                if(saying == null){
                    System.out.printf("%d번 명언은 존재하지 않습니다.\n", id);
                } else {
                    SayingService.remove(saying);
                    System.out.printf("%d번 명언이 삭제되었습니다.\n", id);
                }

            } else if(command.substring(0,2).equals("수정")) {

                int id = Integer.parseInt(command.split("=")[1]);
                SayingDict saying = SayingService.findById(id);

                if(saying == null){
                    System.out.printf("%d번 명언은 존재하지 않습니다.\n", id);
                } else {
                    System.out.printf("명언(기존) : %s\n", saying.getContent());
                    System.out.print("명언 : ");
                    String content = sc.nextLine().trim();

                    System.out.printf("작가(기존) : %s\n", saying.getAuthor());
                    System.out.print("작가 : ");
                    String author = sc.nextLine().trim();

                    SayingService.modify(saying, content, author);

                    System.out.printf("%d번 명언이 수정되었습니다.\n", id);
                }

            } else if(command.equals("빌드")) {
                SayingService.build();
                System.out.println("data.json 파일의 내용이 갱신되었습니다.");

            } else if(command.equals("종료")) {
                return;
            }
        }
    }
}

