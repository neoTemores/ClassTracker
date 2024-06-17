package model;

public class Term {
    private int id;
    private String name;
    private int year;
    private boolean isActive;

    public Term() {
        this(0, "name - n/a", 0, Boolean.FALSE);
    }

    public Term(int id, String name, int year, boolean isActive) {
        this.id = id;
        this.name = name;
        this.year = year;
        this.isActive = isActive;
    }

    @Override
    public String toString() {
        return "Term [id=" + id + ", name=" + name + ", year=" + year + ", isActive=" + isActive + "]";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

}