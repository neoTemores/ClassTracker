public class Course {
    private int id;
    private int termId;
    private String code;
    private String name;

    public Course() {
        this(0, 0, "code - n/a", "name - n/a");
    }

    public Course(int id, int termId, String code, String name) {
        this.id = id;
        this.termId = termId;
        this.code = code;
        this.name = name;
    }

    @Override
    public String toString() {
        return "Course [id=" + id + ", termId=" + termId + ", code=" + code + ", name=" + name + "]";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTermId() {
        return termId;
    }

    public void setTermId(int termId) {
        this.termId = termId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}