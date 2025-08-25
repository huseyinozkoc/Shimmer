import android.view.accessibility.AccessibilityNodeInfo.RangeInfo
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.CustomAccessibilityAction
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.customActions
import androidx.compose.ui.semantics.disabled
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.password
import androidx.compose.ui.semantics.progressBarRangeInfo
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription

/**
 * Jetpack Compose component'leri için merkezi ve gelişmiş erişilebilirlik verilerini tutan sınıf.
 * Bu sınıf, bir component'in anlamsal ağacını (semantics tree) yapılandırmak için gereken
 * tüm bilgileri içerir.
 *
 * @param contentDescription Component'in metinsel açıklaması. İkonlar, resimler için kullanılır.
 * @param stateDescription Component'in mevcut durumunu açıklar (örn: "Açık", "Seçili").
 * @param onClickLabel Tıklama eyleminin ne yapacağını açıklar.
 * @param role Component'in rolünü belirtir (Button, Switch, Image vb.).
 * @param isHeading Component'in bir başlık olup olmadığını belirtir.
 * @param mergeDescendants Eğer true ise, alt component'lerin anlamsal bilgilerini birleştirir.
 * @param enabled Component'in etkileşimli olup olmadığını belirtir.
 * @param customActions Tıklama dışındaki özel eylemler (örn: "Sil", "Arşivle").
 * @param liveRegion Component içeriği değiştiğinde ekran okuyucunun anons yapıp yapmayacağını belirler.
 * @param rangeInfo Slider veya ProgressBar gibi bileşenlerin değer aralığını belirtir.
 * @param isPassword Bu alanın bir şifre alanı olup olmadığını belirtir.
 * @param errorMessage Component ile ilişkili bir hata mesajı varsa burada belirtilir.
 */
data class AccessibilityData(
    // Temel Özellikler
    val contentDescription: String? = null,
    val stateDescription: String? = null,
    val onClickLabel: String? = null,
    val role: Role? = null,
    val isHeading: Boolean = false,
    val mergeDescendants: Boolean = false,
    val enabled: Boolean = true,
    val customActions: List<CustomAccessibilityAction> = emptyList(),
    val liveRegion: LiveRegionMode = LiveRegionMode.Polite,
    val rangeInfo: ProgressBarRangeInfo? = null,
    val isPassword: Boolean = false,
    val errorMessage: String? = null
)

/**
 * Bir [AccessibilityData] nesnesini alarak bir Modifier'a ilgili tüm
 * anlamsal (semantics) özellikleri uygular.
 *
 * Bu, erişilebilirlik mantığını merkezi ve yeniden kullanılabilir bir şekilde yönetmeyi sağlar.
 *
 * @param data Uygulanacak erişilebilirlik verilerini içeren nesne.
 * @return Anlamsal özellikler uygulanmış yeni bir Modifier.
 */
fun Modifier.accessibility(data: AccessibilityData): Modifier = this.semantics(
    // mergeDescendants en dışta, property block'unun dışında olmalıdır.
    mergeDescendants = data.mergeDescendants
) {
    // Rol
    data.role?.let { this.role = it }

    // İçerik ve Durum Açıklamaları
    data.contentDescription?.let { this.contentDescription = it }
    data.stateDescription?.let { this.stateDescription = it }

    // Etkinlik Durumu
    if (!data.enabled) {
        disabled()
    }

    // Başlık
    if (data.isHeading) {
        heading()
    }

    // Hata Mesajı
    data.errorMessage?.let { error(it) }

    // Şifre Alanı
    if (data.isPassword) {
        password()
    }

    // Tıklama Etiketi (label)
    // Bu, clickable modifier'daki `onClickLabel` ile aynı amaca hizmet eder
    // ve bazen daha esnek olabilir.
    data.onClickLabel?.let {
        onClick(label = it, action = null)
    }

    // Değer Aralığı Bilgisi
    data.rangeInfo?.let {
        progressBarRangeInfo = ProgressBarRangeInfo(
            current = it.current,
            range = it.range,
            steps = it.steps
        )
    }

    // Özel Eylemler
    if (data.customActions.isNotEmpty()) {
        customActions = data.customActions.map { customAction ->
            // Kendi data class'ımızı Compose'un beklediği türe dönüştürüyoruz.
            CustomAccessibilityAction(label = customAction.label, action = customAction.action)
        }
    }

    // Canlı Bölge
    data.liveRegion.let {
        liveRegion = data.liveRegion
    }
}