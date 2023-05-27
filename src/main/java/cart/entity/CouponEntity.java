package cart.entity;

import java.util.Objects;

public class CouponEntity {

    private final Long id;
    private final String name;
    private final String policyType;
    private final long discountPrice;
    private final int discountPercent;
    private final boolean discountDeliveryFee;
    private final String conditionType;
    private final long minimumPrice;

    public CouponEntity(
            final String name,
            final String policyType,
            final long discountPrice,
            final int discountPercent,
            final boolean discountDeliveryFee,
            final String conditionType,
            final long minimumPrice
    ) {
        this(null, name, policyType, discountPrice, discountPercent, discountDeliveryFee, conditionType, minimumPrice);
    }

    public CouponEntity(
            final Long id,
            final String name,
            final String policyType,
            final long discountPrice,
            final int discountPercent,
            final boolean discountDeliveryFee,
            final String conditionType,
            final long minimumPrice
    ) {
        Objects.requireNonNull(name);
        Objects.requireNonNull(policyType);
        Objects.requireNonNull(conditionType);
        this.id = id;
        this.name = name;
        this.policyType = policyType;
        this.discountPrice = discountPrice;
        this.discountPercent = discountPercent;
        this.discountDeliveryFee = discountDeliveryFee;
        this.conditionType = conditionType;
        this.minimumPrice = minimumPrice;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPolicyType() {
        return policyType;
    }

    public long getDiscountPrice() {
        return discountPrice;
    }

    public int getDiscountPercent() {
        return discountPercent;
    }

    public boolean isDiscountDeliveryFee() {
        return discountDeliveryFee;
    }

    public String getConditionType() {
        return conditionType;
    }

    public long getMinimumPrice() {
        return minimumPrice;
    }
}