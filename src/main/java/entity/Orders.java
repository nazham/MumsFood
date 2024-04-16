package entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Setter
@Getter
@ToString
@Entity
public class Orders {
    @Id
    @Column(name = "id")
    private String orderId;

    @Column(name = "date_time")
    private LocalDateTime dateTime;

    @Column(name = "order_type")
    private String orderType;

    @Column(name = "total_amount")
    private Double totalAmount;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @OneToMany(mappedBy = "orders")
    private List<OrderDetail> orderDetails = new ArrayList<>();

    public Orders(String orderId, LocalDateTime dateTime, String orderType, Double totalAmount) {
        this.orderId = orderId;
        this.dateTime = dateTime;
        this.orderType = orderType;
        this.totalAmount = totalAmount;
    }
}