package de.fuseki.entities;

import de.fuseki.converter.AddressConverter;
import de.fuseki.converter.LocalDateConvert;
import de.fuseki.enums.OrderType;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "order")
public class Order {
    @Id
    private Integer id;

    @OneToOne
    @JoinColumn(name= "book_id")
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "person_id")
    private Person person;

    @Column(name = "begin_date", nullable = false)
    @Convert(converter = LocalDateConvert.class)
    private LocalDate beginDate;

    @Column(name = "end_date", nullable = false)
    @Convert(converter = LocalDateConvert.class)
    private LocalDate endDate;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderType type;
}
