package cart.service;

import static cart.fixture.CouponFixture._3만원_이상_2천원_할인_쿠폰;
import static cart.fixture.CouponFixture._3만원_이상_배달비_3천원_할인_쿠폰;
import static cart.fixture.MemberFixture.사용자1;
import static org.assertj.core.api.Assertions.assertThat;

import cart.domain.cart.MemberCoupon;
import cart.domain.coupon.Coupon;
import cart.domain.member.Member;
import cart.dto.CouponResponse;
import cart.repository.CouponRepository;
import cart.repository.MemberCouponRepository;
import cart.repository.MemberRepository;
import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@Transactional
@SpringBootTest
class CouponServiceTest {

    @Autowired
    private CouponService couponService;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberCouponRepository memberCouponRepository;

    @Test
    void 사용자의_아이디를_입력받아_사용자가_사용하지_않은_쿠폰을_전체_조회한다() {
        // given
        final Coupon coupon1 = couponRepository.save(_3만원_이상_2천원_할인_쿠폰);
        final Coupon coupon2 = couponRepository.save(_3만원_이상_배달비_3천원_할인_쿠폰);
        final Member member = memberRepository.save(사용자1);
        memberCouponRepository.saveAll(List.of(
                new MemberCoupon(member.getId(), coupon1),
                new MemberCoupon(member.getId(), coupon2)
        ));

        // when
        final List<CouponResponse> result = couponService.findAllByMemberId(member.getId());

        // then
        assertThat(result).usingRecursiveComparison().isEqualTo(List.of(
                new CouponResponse(coupon1.getId(), "30000원 이상 2000원 할인 쿠폰", "price", 2000L, 30000L),
                new CouponResponse(coupon2.getId(), "30000원 이상 배달비 할인 쿠폰", "delivery", 3000L, 30000L)
        ));
    }
}