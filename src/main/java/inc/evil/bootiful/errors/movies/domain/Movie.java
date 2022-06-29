package inc.evil.bootiful.errors.movies.domain;

import inc.evil.bootiful.errors.common.entity.AbstractEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "movies")
public class Movie extends AbstractEntity {
    private String name;
    private int publishYear;

    protected Movie() {
    }

    private Movie(MovieBuilder builder) {
        this.name = builder.name;
        this.publishYear = builder.publishYear;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPublishYear() {
        return publishYear;
    }

    public void setPublishYear(int publishYear) {
        this.publishYear = publishYear;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "name='" + name + '\'' +
                ", publishYear=" + publishYear +
                ", id=" + id +
                '}';
    }

    public static MovieBuilder builder() {
        return new MovieBuilder();
    }

    public static class MovieBuilder {
        private String name;
        private int publishYear;

        public MovieBuilder name(String name) {
            this.name = name;
            return this;
        }

        public MovieBuilder publishYear(int publishYear) {
            this.publishYear = publishYear;
            return this;
        }

        public Movie build() {
            return new Movie(this);
        }
    }
}
