package se.what.inventorymanager.domain;
import jakarta.persistence.*;

@Entity
@Table(name = "search_record")
public class SearchRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String query;

    public SearchRecord(String query) {
        this.query = query;
    }

    public SearchRecord() {
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    @Override
    public String toString() {
        return "\n\033[1mId:\033[0m " + id +
                " | \033[1mQuery:\033[0m " + query;
    }

}