package de.fuseki.entities;

import de.fuseki.converter.LocalDateConvert;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@SuperBuilder
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="order_type",
        discriminatorType = DiscriminatorType.STRING)
@Entity(name = "orders")
public abstract class Order {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name= "book_id")
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "person_id")
    private Person person;

    @Column(name = "begin_date", nullable = true)
    @Convert(converter = LocalDateConvert.class)
    private LocalDate beginDate;

    @Column(name = "end_date", nullable = false)
    @Convert(converter = LocalDateConvert.class)
    private LocalDate endDate;
}
