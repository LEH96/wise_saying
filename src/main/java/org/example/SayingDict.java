package org.example;

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

    public String toJson() {
        return """
                 {
                     "no": %d,
                     "content": "%s",
                     "author": "%s"
                } 
                """
                .stripIndent()
                .formatted(no, content, author)
                .trim();
    }
}
