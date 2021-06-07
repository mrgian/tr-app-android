package im.gian.tr.producer.details

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import im.gian.tr.R
import im.gian.tr.producer.ProducerActivity
import im.gian.tr.producer.ProducerViewModel
import im.gian.tr.restaurant.RestaurantActivity
import im.gian.tr.restaurant.RestaurantViewModel

class ImageCardAdapter(private val context: Context?) : RecyclerView.Adapter<ImageCardAdapter.ImageCardViewHolder>() {
    private val producerViewModel: ProducerViewModel =
        ViewModelProvider(context as ProducerActivity).get(ProducerViewModel::class.java)

    private var imageUriList: MutableList<Uri?> = mutableListOf(null)

    var storage = Firebase.storage

    class ImageCardViewHolder(private val row: View) : RecyclerView.ViewHolder(row) {
        val imageViewProducer: ImageView = row.findViewById(R.id.imageViewRestaurant)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)

        storage.reference.child("images/${producerViewModel.producer.value?.id}").list(10).addOnSuccessListener { list->
            list.items.forEachIndexed { index, ref ->
                ref.downloadUrl.addOnSuccessListener {
                    imageUriList.add(it)
                    if(index == 0) imageUriList.removeFirst() //Remove null uri
                    if(index == list.items.size - 1) notifyDataSetChanged() //If last update data
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageCardViewHolder {
        val layout = LayoutInflater.from(parent.context).inflate(R.layout.image_card_view,
            parent, false)

        return ImageCardViewHolder(layout)
    }

    override fun onBindViewHolder(holder: ImageCardViewHolder, position: Int) {
        //Image
        if(imageUriList[position] != null)
            Glide.with(holder.imageViewProducer).load(imageUriList[position]).placeholder(R.drawable.restaurant_placeholder).into(holder.imageViewProducer)
    }

    override fun getItemCount(): Int = imageUriList.size
}