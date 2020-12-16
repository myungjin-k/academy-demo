package my.myungjin.academyDemo.domain.item;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import my.myungjin.academyDemo.domain.common.CommonCode;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;

import static java.time.LocalDateTime.now;
import static java.util.Optional.ofNullable;


@Entity
@Table(name = "item_master")
@ToString(exclude = {"options", "display"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id")
public class ItemMaster {
    @Id @Getter
    private String id;

    @Getter
    @Size(min = 1, max = 50)
    @Column(name = "item_name", nullable = false, unique = true)
    private String itemName;

    @Getter
    @Column(name = "price", nullable = false)
    private int price;

    @Setter
    @Getter
    @Size(min = 1, max = 255)
    @Column(name = "thumbnail")
    private String thumbnail;


    @Getter
    @Column(name = "create_at", insertable = false, updatable = false,
            columnDefinition = "datetime default current_timestamp")
    private LocalDateTime createAt;

    @Column(name = "update_at")
    private LocalDateTime updateAt;

    @Getter @Setter
    @JsonIgnore
    @OneToMany(mappedBy = "itemMaster", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE) //JOIN
    private Collection<ItemOption> options;

    @Setter @Getter
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private CommonCode category;

    @Getter @Setter
    @JsonManagedReference
    @OneToOne(mappedBy = "itemMaster", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private ItemDisplay display;

    @Builder
    public ItemMaster(String id, @Size(min = 1, max = 50) String itemName,
                      int price, String thumbnail, LocalDateTime createAt, LocalDateTime updateAt) {
        this.id = id;
        this.itemName = itemName;
        this.price = price;
        this.thumbnail = thumbnail;
        this.createAt = createAt;
        this.updateAt = updateAt;
        this.options = new HashSet<>();
    }

    public Optional<LocalDateTime> getUpdateAt(){
        return ofNullable(updateAt);
    }

    public void addOption(ItemOption option){
        options.add(option);
        option.setItemMaster(this);
    }

    public void modify(String itemName, int price){
        this.itemName = itemName;
        this.price = price;
        this.updateAt = now();
    }

    @Entity
    @Table(name = "item_option")
    @ToString
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @EqualsAndHashCode(of = "id")
    public static class ItemOption {
        @Id @Getter
        private String id;

        @Getter
        @Size(max = 10)
        @Column(name = "size", nullable = false, columnDefinition = "varchar(10) default 'ONE SIZE'")
        private String size;

        @Getter
        @Size(max = 10)
        @Column(name = "color", nullable = false, columnDefinition = "varchar(10) default 'ONE COLOR'")
        private String color;

        @Getter
        @Column(name = "create_at", insertable = false, updatable = false,
                columnDefinition = "datetime default current_timestamp")
        private LocalDateTime createAt;

        @Column(name = "update_at")
        private LocalDateTime updateAt;

        @Setter @Getter
        @JsonBackReference
        @ManyToOne
        @JoinColumn(name = "master_id", nullable = false)
        private ItemMaster itemMaster;

        @Builder
        public ItemOption(String id, @Size(min = 1, max = 10) String size, @Size(min = 1, max = 10) String color) {
            this.id = id;
            this.size = size;
            this.color = color;
        }

        public Optional<LocalDateTime> getUpdateAt(){
            return ofNullable(updateAt);
        }

        public void modify(String color, String size){
            this.color = color;
            this.size = size;
        }
    }
}
