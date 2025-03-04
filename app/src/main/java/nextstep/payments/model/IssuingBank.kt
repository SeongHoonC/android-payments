package nextstep.payments.model

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import nextstep.payments.R

enum class IssuingBank(val bankName: String, @DrawableRes val logo: Int, val color: Color) {
    BC_CARD("BC카드", R.drawable.img_bc, color = Color(0xFFf04651)),
    SHINHAN_CARD("신한카드", R.drawable.img_shinhan, Color(0xFF0046ff)),
    KAKAO_BANK("카카오뱅크", R.drawable.img_kakao, Color(0xFFffee00)),
    HYUNDAE_CARD("현대카드", R.drawable.img_hyundae, Color(0xFF000000)),
    WOORI_CARD("우리카드", R.drawable.img_woori, Color(0xFF0078c7)),
    HANA_CARD("하나카드", R.drawable.img_hana, Color(0xFF009490)),
    KB_CARD("국민카드", R.drawable.img_kb, Color(0xFF554e45)),
    LOTTE_CARD("롯데카드", R.drawable.img_lotte, Color(0xFFed1c24)),
}
