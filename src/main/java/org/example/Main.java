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

public class Main {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        int no = 1;
        String command;
        List<SayingDict> datum = new ArrayList<>();

        System.out.println("== 명언 앱 ==");

        while(true){
            System.out.print("명령) ");
            command = sc.nextLine().trim();

            if(command.equals("등록")) {

                System.out.print("명언: ");
                String content = sc.nextLine();

                System.out.print("작가: ");
                String author = sc.nextLine();

                datum.add(new SayingDict(no, content, author));
                System.out.printf("%d번 명언이 등록되었습니다.\n", no);

                //("C:/Users/lutea/OneDrive/바탕 화면/java/java_project/src/json/"+ no++ +".json");

            } else if(command.equals("목록")) {

                System.out.println("번호 / 작가 / 명언");
                System.out.println("----------------------");

                List<SayingDict> sorted_datum = datum.stream()
                        .sorted((e2, e1) -> e1.getNo() - e2.getNo())
                        .collect(Collectors.toList());

                for(SayingDict say : sorted_datum)
                    System.out.println(say.getNo() + " / " + say.getAuthor() + " / " + say.getContent());

            } else if(command.startsWith("삭제")) {

                int id = Integer.parseInt(command.split("=")[1]);
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

            } else if(command.substring(0,2).equals("수정")) {

                int id = Integer.parseInt(command.split("=")[1]);
                int idIdx = -1;

                for(int i=0;i<datum.size();i++){
                    if(datum.get(i).getNo() == id) {
                        idIdx = i;
                        break;
                    }
                }

                if(idIdx != -1){
                    System.out.printf("명언(기존) : %s\n", datum.get(idIdx).getContent());
                    System.out.print("명언 : ");
                    String new_title = sc.nextLine();
                    datum.get(idIdx).setContent(new_title);

                    System.out.printf("작가(기존) : %s\n", datum.get(idIdx).getAuthor());
                    System.out.print("작가 : ");
                    String new_author = sc.nextLine();
                    datum.get(idIdx).setAuthor(new_author);
                } else {
                    System.out.printf("%d번 명언은 존재하지 않습니다.\n", id);
                }

            } else if(command.equals("종료")) {
                return;
            }
        }
    }
}

class SayingDict {
    private int no;
    private String content;
    private String author;

    public SayingDict(int no, String content, String author) {
        this.no = no;
        this.content = content;
        this.author = author;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}

class Util {
    public static class json {

        // JSON 파일에 저장된 내용을 읽고, map으로 변환
        public static Map<String, Object> jsonToMapFromFile(String path) {
            String json = file.readFromFile(path, "");

            if (json.isEmpty()) {
                return null;
            }

            final String[] jsonBits = json
                    .replaceAll("\\{", "")
                    .replaceAll("\\}", "")
                    .split(",");

            final List<Object> bits = Stream.of(jsonBits)
                    .map(String::trim)
                    .flatMap(bit -> Arrays.stream(bit.split(":")))
                    .map(String::trim)
                    .map(s -> s.startsWith("\"") ? s.substring(1, s.length() - 1) : Integer.parseInt(s))
                    .collect(Collectors.toList());

            Map<String, Object> map = IntStream
                    .range(0, bits.size() / 2)
                    .mapToObj(i -> new AbstractMap.SimpleEntry<>((String) bits.get(i * 2), bits.get(i * 2 + 1)))
                    .collect(Collectors.toMap(p -> p.getKey(), p -> p.getValue(), (key1, key2) -> key1, LinkedHashMap::new));

            return map;
        }
    }

    public static class file {
        // 파일 저장
        public static void saveToFile(String path, String body) {
            new File(path).delete();

            try (RandomAccessFile stream = new RandomAccessFile(path, "rw");
                 FileChannel channel = stream.getChannel()) {
                byte[] strBytes = body.getBytes();
                ByteBuffer buffer = ByteBuffer.allocate(strBytes.length);
                buffer.put(strBytes);
                buffer.flip();
                channel.write(buffer);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        // 디렉토리 생성
        public static void mkdir(String path) {
            new File(path).mkdirs();
        }

        // 파일의 내용 읽기
        public static String readFromFile(String path, String defaultValue) {
            try (RandomAccessFile reader = new RandomAccessFile(path, "r")) {
                StringBuilder sb = new StringBuilder();

                String line;

                boolean isFirst = true;

                while ((line = reader.readLine()) != null) {
                    if (isFirst == false) {
                        sb.append("\n");
                    }

                    sb.append(new String(line.getBytes("iso-8859-1"), "utf-8"));

                    isFirst = false;
                }

                return sb.toString();

            } catch (FileNotFoundException e) {
                return defaultValue;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        // 디렉토리 삭제
        public static void deleteDir(String path) {
            Path rootPath = Paths.get(path);
            try (Stream<Path> walk = Files.walk(rootPath)) {
                walk.sorted(Comparator.reverseOrder())
                        .map(Path::toFile)
                        .forEach(File::delete);
            } catch (IOException e) {

            }
        }

        // 특정 디렉토리안에 존재하는 모든 파일(서브 디렉토리 제외)들의 이름을 배열로 반환
        public static List<String> getFileNamesFromDir(String path) {
            try (Stream<Path> stream = Files.walk(Paths.get(path), 1)) {
                return stream
                        .filter(file -> !Files.isDirectory(file))
                        .map(Path::getFileName)
                        .map(Path::toString)
                        .collect(Collectors.toList());
            } catch (IOException e) {
                return new ArrayList<>();
            }
        }

        // 파일에 숫자를 저장
        public static void saveNoToFile(String path, long no) {
            saveToFile(path, no + "");
        } // 예시 - Util.saveNoToFile("c:/a.txt",5)하면 a.txt 파일을 만들고 안에 5가 들어감


        // 파일에서 숫자를 읽는다
        public static long readNoFromFile(String path, long defaultValue) {
            String no = Util.file.readFromFile(path, "");

            if (no.isEmpty()) {
                return defaultValue;
            }

            try {
                return Long.parseLong(no);
            } catch (NumberFormatException e) {
                return defaultValue;
            }
        }

        // 파일삭제
        public static void deleteFile(String filePath) {
            new File(filePath).delete();
        }

        // 파일이 존재하는지 체크
        public static boolean isFile(String filePath) {
            return new File(filePath).exists();
        }
    }
}