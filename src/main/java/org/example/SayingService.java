package org.example;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SayingService {
    // 테이블에서 가장 마지막에 생성된 데이터의 no를 저장한 파일의 경로
    public static String getTableLastIdFilePath() {
        return getTableDirPath() + "/last_id.txt";
    }

    // 테이블에서 가장 마지막에 생성된 데이터의 no
    public static long getLastId() {
        return Util.file.readNoFromFile(getTableLastIdFilePath(), 0);
    }

    // 테이블에서 가장 마지막에 생성된 데이터의 no 갱신
    public static void saveLastNo(int no) {
        Util.file.saveNoToFile(getTableLastIdFilePath(), no);
    }

    // 특정 데이터의 파일경로
    // no : 5 -> /prod_data/wise_saying/5.json
    public static String getTableDataFilePath(int no) {
        return getTableDirPath() + "/" + no + ".json";
    }

    // 테이블의 데이터가 저장될 폴더 경로
    public static String getTableDirPath() {
        return "wiseSaying";
    }

    public static List<SayingDict> findAll() {
        List<Integer> fileIds = getFileIds();
        return fileIds.stream()
                .map(id -> findById(id))
                .collect(Collectors.toList());
    }

    private static List<Integer> getFileIds() {
        String path = getTableDirPath();
        List<String> fileNames = Util.file.getFileNamesFromDir(path);

        return fileNames
                .stream()
                .filter(fileName -> !fileName.equals("last_id.txt"))
                .filter(fileName -> !fileName.equals("data.json"))
                .filter(fileName -> fileName.endsWith(".json"))
                .map(fileName -> fileName.replace(".json", ""))
                .mapToInt(Integer::parseInt)
                .boxed()
                .collect(Collectors.toList());
    }

    public static SayingDict findById(int id) {
        String path = getTableDataFilePath(id);

        if (new File(path).exists() == false) {
            return null;
        }

        Map<String, Object> map = Util.json.jsonToMapFromFile(path);

        if (map == null) {
            return null;
        }

        return new SayingDict((int) map.get("no"), (String) map.get("content"), (String) map.get("author"));
    }

    public static void remove(SayingDict saying) {
        String path = getTableDataFilePath(saying.getNo());
        Util.file.deleteFile(path);
    }

    public static void modify(SayingDict saying, String content, String author) {
        saying.setContent(content);
        saying.setAuthor(author);

        String body = """
                        {
                            "no": %d,
                            "content": "%s",
                             "author": "%s"
                        }
                       """
                .stripIndent() //탭들 다 없애줌
                .formatted(saying.getNo(), content, author) ;

        String filePath = getTableDataFilePath(saying.getNo());
        Util.file.saveToFile(filePath, body);
    }

    public static void build() {
        List<SayingDict> sayings = findAll();

        Util.file.mkdir("build");

        String json = "[" + sayings
                .stream()
                .map(saying -> saying.toJson())
                .collect(Collectors.joining(",\n")) + "]";

        Util.file.saveToFile("build/data.json", json);
    }
}
