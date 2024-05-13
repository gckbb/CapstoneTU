import android.graphics.drawable.Icon
import android.os.Parcel
import android.os.Parcelable
import com.example.kakaotest.search.LocationLatLngEntity
import kotlinx.parcelize.Parcelize

@Parcelize
data class RouteEntity(
    val markerIcon: Icon,
    val viaPointId: String,
    val name: String,
    val fullAddress: String,
    val locationLatLng: LocationLatLngEntity
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readParcelable<Icon>(Icon::class.java.classLoader)!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readParcelable<LocationLatLngEntity>(LocationLatLngEntity::class.java.classLoader)!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(markerIcon, flags)
        parcel.writeString(viaPointId)
        parcel.writeString(name)
        parcel.writeString(fullAddress)
        parcel.writeParcelable(locationLatLng, flags)
    }

    override fun describeContents(): Int {
        return 0
    }
}
