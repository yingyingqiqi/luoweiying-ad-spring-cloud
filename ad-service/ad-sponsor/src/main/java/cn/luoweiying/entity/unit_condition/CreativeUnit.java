package cn.luoweiying.entity.unit_condition;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;

/**
 * Created by Qinyi.
 */
@Data

@AllArgsConstructor
@Entity
@Table(name = "creative_unit")
public class CreativeUnit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Basic
    @Column(name = "creative_id", nullable = false)
    private Long creativeId;

    @Basic
    @Column(name = "unit_id", nullable = false)
    private Long unitId;

    public CreativeUnit(Long creativeId, Long unitId) {
        this.creativeId = creativeId;
        this.unitId = unitId;
    }
}
