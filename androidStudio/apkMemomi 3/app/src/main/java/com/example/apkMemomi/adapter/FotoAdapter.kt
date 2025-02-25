import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.apkMemomi.R
import com.squareup.picasso.Picasso
import com.example.apkMemomi.objetos.Fotos
import com.example.apkMemomi.objetos.apiClients

class PhotoAdapter(private val photoList: List<Fotos>) : RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder>() {

    inner class PhotoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNombreInvitado: TextView = itemView.findViewById(R.id.tvNombreInvitado)
        val tvDniInvitado: TextView = itemView.findViewById(R.id.tvDniInvitado)
        val imageView: ImageView = itemView.findViewById(R.id.imageView)


        fun bind(Fotos: Fotos) {
            tvNombreInvitado.text = Fotos.NombreInvitado
            tvDniInvitado.text = Fotos.InvitadoDNI
            // Usa Picasso para cargar la imagen
            if (Fotos.Fotos.isNotEmpty()) {

                Picasso.get()
                    .load("${apiClients.BASE_URL_IMAGE}${Fotos.FotoFisic}")
                    .placeholder(R.drawable.ic_no_loading)
                    .error(R.drawable.ic_event)
                    .into(imageView)
            } else {
                imageView.setImageResource(R.drawable.ic_no_loading) // Imagen por defecto
            }
            // Carga la imagen usando Picasso o Glide
            Picasso.get().load("${apiClients.BASE_URL_IMAGE}${Fotos.FotoFisic}").into(imageView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_foto, parent, false)
        return PhotoViewHolder(view)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        holder.bind(photoList[position])
    }

    override fun getItemCount(): Int = photoList.size
}
