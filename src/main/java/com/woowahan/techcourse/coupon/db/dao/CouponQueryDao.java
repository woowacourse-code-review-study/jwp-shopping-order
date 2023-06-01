package com.woowahan.techcourse.coupon.db.dao;

import com.woowahan.techcourse.coupon.domain.Coupon;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class CouponQueryDao {

    private static final RowMapper<Coupon> rowMapper = (rs, rowNum) -> new Coupon(
            rs.getLong("c_id"),
            rs.getString("c_name"),
            DiscountConditionHelper.findByName(rs.getString("dc_discount_condition")).mapRow(rs, rowNum),
            DiscountPolicyHelper.findByName(rs.getString("dt_discount_type")).mapRow(rs, rowNum)
    );
    private static final String BASE_FIND_ALL_SQL =
            "SELECT c.id AS c_id, c.name AS c_name, c.discount_condition_id AS c_discount_condition_id, c.discount_type_id AS c_discount_type_id, "
                    + "dc.discount_condition_type AS dc_discount_condition,"
                    + "dt.discount_type AS dt_discount_type, dt.discount_amount_id AS dt_discount_amount,"
                    + "ad.rate AS ad_rate "
                    + "FROM COUPON AS c "
                    + "LEFT JOIN DISCOUNT_CONDITION AS dc ON c.discount_condition_id=dc.id "
                    + "LEFT JOIN DISCOUNT_TYPE AS dt ON dt.id=c.discount_type_id "
                    + "LEFT JOIN AMOUNT_DISCOUNT AS ad ON ad.id=dt.discount_amount_id ";
    private static final String FIND_BY_MEMBER_ID_SQL =
            BASE_FIND_ALL_SQL + "JOIN COUPON_MEMBER AS mc ON mc.coupon_id=c.id WHERE mc.member_id=?";
    private static final String FIND_BY_ID_SQL =
            BASE_FIND_ALL_SQL + "WHERE c.id=?";


    private final JdbcTemplate jdbcTemplate;

    public CouponQueryDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<Coupon> findById(Long id) {
        try {
            return Optional.of(jdbcTemplate.queryForObject(FIND_BY_ID_SQL, rowMapper, id));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public List<Coupon> findAllByMemberId(Long memberId) {
        return jdbcTemplate.query(FIND_BY_MEMBER_ID_SQL, rowMapper, memberId);
    }

    public List<Coupon> findAll() {
        return jdbcTemplate.query(BASE_FIND_ALL_SQL, rowMapper);
    }

    public List<Coupon> findAllByIds(List<Long> couponIds) {
        String inSql = String.join(",", Collections.nCopies(couponIds.size(), "?"));
        String sql = String.format(BASE_FIND_ALL_SQL + " WHERE c.id IN (%s)", inSql);
        return jdbcTemplate.query(sql, rowMapper, couponIds.toArray());
    }
}