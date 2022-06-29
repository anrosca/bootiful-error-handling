package inc.evil.bootiful.errors.common.entity;

import javax.persistence.*;
import java.util.Objects;

@MappedSuperclass
public abstract class AbstractEntity {
    @Id
    @SequenceGenerator(name = "id-generator", sequenceName = "id_sequence", allocationSize = 1)
    @GeneratedValue(generator = "id-generator")
    protected Long id;

    protected AbstractEntity() {
    }

    public Long getId() {
        return id;
    }

    public boolean equals(Object other) {
        if (!(other instanceof AbstractEntity otherEntity))
            return false;
        return Objects.equals(getId(), otherEntity.getId());
    }

    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "AbstractEntity{" +
                "id=" + id +
                '}';
    }
}
