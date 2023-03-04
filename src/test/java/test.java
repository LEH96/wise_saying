package org.example;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class test {
    @Test
    @DisplayName("Util.json.jsonToMapFromFile을 불러왔을때 json파일이 잘 만들어졌는지 확인")
    void t1(){
        Map<String, Object> map = Util.json.jsonToMapFromFile("testData/1.json");
        Map<String, Object> expect = new LinkedHashMap<>(){{
            put("id",1);
            put("content","love now");
            put("author", "unkno");
        }};
        /*같은 내용
        Map<String, Object> expect = Map.of(
            "id", 1,
            "content", "love now",
            "author", "unknown"
        );
        */
        assertThat(map).isEqualTo(expect);
    }

    @Test
    @DisplayName("Util.file.saveToFile를 통해 파일을 만들고 " +
                 "readFromFile로 잘 읽어오는지 테스트하고" +
                 "deleteFile을 통해 삭제 후 isFile을 통해 잘 삭제됐는지 테스트")
    void t2(){
        String filePath = "testData/test.txt";
        Util.file.saveToFile(filePath,"Hi");
        String rs = Util.file.readFromFile(filePath, "");
        assertThat(rs).isEqualTo("Hi");
        Util.file.deleteFile(filePath);
        //Util.file.isFile(filePath);
    }

    @Test
    @DisplayName("Util.file.saveNoToFile, Util.file.readNoToFile 테스트")
    void t3(){
        String filePath = "testData/test.txt";
        Util.file.saveNoToFile(filePath,5);
        long no = Util.file.readNoFromFile(filePath, 0);
        Util.file.deleteFile(filePath);
        assertThat(no).isEqualTo(5);
    }

    @Test
    @DisplayName("Util.file.getFileNamesFromDir로 폴더 내 파일 확인")
    void t4(){
        Util.file.saveToFile("testData/2.json","");
        Util.file.saveToFile("testData/3.json","");
        List<String> fileNames = Util.file.getFileNamesFromDir("testData");
        Util.file.deleteFile("testData/2.json");
        Util.file.deleteFile("testData/3.json");
        assertThat(fileNames).isEqualTo(List.of("1.json", "2.json", "3.json"));
    }
}
