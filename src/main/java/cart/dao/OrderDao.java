package cart.dao;

import cart.entity.OrderEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class OrderDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;
    private final RowMapper<OrderEntity> rowMapper = (resultSet, rowNum) -> new OrderEntity(
            resultSet.getLong("id"),
            resultSet.getLong("delivery_fee"),
            resultSet.getLong("member_coupon_id"),
            resultSet.getLong("member_id")
    );

    public OrderDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("orders")
                .usingColumns("delivery_fee", "member_coupon_id", "member_id")
                .usingGeneratedKeyColumns("id");
    }


    public OrderEntity insert(final OrderEntity orderEntity) {
        final SqlParameterSource params = new BeanPropertySqlParameterSource(orderEntity);
        final long id = jdbcInsert.executeAndReturnKey(params).longValue();
        return new OrderEntity(id, orderEntity.getDeliveryFee(), orderEntity.getMemberCouponId(),
                orderEntity.getMemberId());
    }

    public List<OrderEntity> findAllByMemberId(final Long memberId) {
        final String sql = "SELECT * FROM orders WHERE member_id = ?";
        return jdbcTemplate.query(sql, rowMapper, memberId);
    }

    public Optional<OrderEntity> findById(final Long id) {
        final String sql = "SELECT * FROM orders WHERE id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, id));
        } catch (final EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}