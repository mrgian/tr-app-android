package im.gian.tr.restaurant

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.google.gson.Gson
import im.gian.tr.R
import im.gian.tr.home.HomeViewModel
import im.gian.tr.model.Certification
import im.gian.tr.model.Restaurant
import im.gian.tr.restaurant.RestaurantActivity

class CertificationCardAdapter(private val context: Context?) : RecyclerView.Adapter<CertificationCardAdapter.CertificationCardViewHolder>() {
    private val restaurantViewModel: RestaurantViewModel =
        ViewModelProvider(context as RestaurantActivity).get(RestaurantViewModel::class.java)

    private var certificationList: List<Certification>? = restaurantViewModel.certifications.value

    var storage = Firebase.storage

    class CertificationCardViewHolder(private val row: View) : RecyclerView.ViewHolder(row) {
        val textViewCertification: TextView = row.findViewById(R.id.textViewCertification)
        val imageViewCertification: ImageView = row.findViewById(R.id.imageViewCerification)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CertificationCardViewHolder {
        val layout = LayoutInflater.from(parent.context).inflate(R.layout.certification_card_view,
            parent, false)
        return CertificationCardViewHolder(layout)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)

        //Observe for changes in viewmodel
        restaurantViewModel.certifications.observe(context as RestaurantActivity, certificationsObserver)
    }

    override fun onBindViewHolder(holder: CertificationCardViewHolder, position: Int) {
        //Name
        holder.textViewCertification.text = certificationList!![position].name

        //Image
    }

    //Update certification list when viewmodel data changes
    private val certificationsObserver = Observer<List<Certification>> {
        certificationList = restaurantViewModel.certifications.value
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = certificationList!!.size
}